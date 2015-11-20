/*                                                                         
 * Copyright 2010-2013 the original author or authors.                     
 *                                                                         
 * Licensed under the Apache License, Version 2.0 (the "License");         
 * you may not use this file except in compliance with the License.        
 * You may obtain a copy of the License at                                 
 *                                                                         
 *      http://www.apache.org/licenses/LICENSE-2.0                         
 *                                                                         
 * Unless required by applicable law or agreed to in writing, software     
 * distributed under the License is distributed on an "AS IS" BASIS,       
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and     
 * limitations under the License.                                          
 */                                                                        
package gemlite.core.internal.mq.server;

import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IDataSource;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.domain.utilClass.MqDataSource;
import gemlite.core.internal.index.BenchmarkLocalCache;
import gemlite.core.internal.index.ITestIndexTool;
import gemlite.core.internal.mq.domain.MqSyncDomain;
import gemlite.core.internal.mq.domain.SyncType;
import gemlite.core.internal.support.annotations.IndexRegion;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class LocalCacheSyncFunction implements Function
{
	private static final long serialVersionUID = -2091604789300596520L;

	@Override
	public boolean hasResult()
	{
		return true;
	}

	@Override
	public void execute(FunctionContext fc)
	{
		MqSyncDomain md = (MqSyncDomain) fc.getArguments();
		saveToLocalCache(md);
		fc.getResultSender().lastResult("done");
	}

	@Override
	public String getId()
	{
		return LocalCacheSyncFunction.class.getName();
	}

	@Override
	public boolean optimizeForWrite()
	{
		return true;
	}

	@Override
	public boolean isHA()
	{
		return false;
	}

	private void saveToLocalCache(MqSyncDomain md)
	{
		String tableName = md.getTableName();
		String regionName = DomainRegistry.tableToRegion(tableName);
		IMapperTool tool = DomainRegistry.getMapperTool(regionName);

		Map<Object, Object> cachedMap = getCachedMap(regionName);

		Object getKey = md.isPkChanged() ? md.getOldKey() : md.getKey();
		Object putKey = md.getKey();
		if (SyncType.INSERT.equals(md.getOp()))
		{
			IDataSource source = new MqDataSource(md.getValue());
			Object newValue = tool.mapperValue(source);
			cachedMap.put(putKey, newValue);
			saveToIndexCache(regionName, SyncType.INSERT, null, newValue);
		} else if (SyncType.UPDATE.equals(md.getOp()))
		{
			Object oldValue = cachedMap.get(getKey);
			if (oldValue != null)
			{
				IDataSource source = new MqDataSource(md.getValue());
				Object newValue = tool.mapperValue(source, oldValue);
				cachedMap.put(putKey, newValue);
				saveToIndexCache(regionName, SyncType.INSERT, null, newValue);
				if (md.isPkChanged())
				{
					saveToIndexCache(regionName, SyncType.DELETE, oldValue,
							null);
					cachedMap.remove(getKey);
				}
			}
		} else if (SyncType.DELETE.equals(md.getOp()))
		{
			Object oldValue = cachedMap.get(putKey);
			if (oldValue != null)
			{
				saveToIndexCache(regionName, SyncType.DELETE, oldValue, null);
				cachedMap.remove(putKey);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<Object, Object> getCachedMap(String regionName)
	{
		Map<Object, Object> map = (Map<Object, Object>) BenchmarkLocalCache
				.getLocalCache(regionName);
		if (map == null)
		{
			map = new ConcurrentHashMap<Object, Object>();
			BenchmarkLocalCache.setLocalCache(regionName, map);
		}

		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveToIndexCache(String regionName, String syncType,
			Object oldValue, Object newValue)
	{
		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
		Set<IndexRegion> indexNames = idc.getIndexNamesByTestRegion(regionName);
		if (indexNames == null)
			return;

		for (IndexRegion name : indexNames)
		{
			IIndexContext context = idc.getIndexContext(name.indexName());
			if (context != null)
			{
				if (context.isRegion())
					continue;
				ITestIndexTool indexTool = (ITestIndexTool) context
						.getIndexInstance(true);
				if (syncType.equals(SyncType.INSERT))
				{
					indexTool.afterInsert(oldValue, newValue);
				} else if (syncType.equals(SyncType.DELETE))
				{
					indexTool.afterDelete(oldValue, newValue);
				} else if (syncType.equals(SyncType.UPDATE))
				{
					indexTool.afterUpdate(oldValue, newValue);
				}
			}
		}
	}
}
