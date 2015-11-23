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
package gemlite.core.internal.jmx.manage;

import gemlite.core.annotations.mbean.GemliteMBean;
import gemlite.core.internal.index.IndexHelper;
import gemlite.core.internal.jmx.manage.KeyConstants.Indexs;
import gemlite.core.internal.measurement.index.AbstractIndexStat;
import gemlite.core.internal.measurement.index.IndexMeasureHelper;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@SuppressWarnings({"rawtypes","unchecked"})
@GemliteMBean(name="IndexManager", config=true)
@ManagedResource
public class IndexDeployStat implements Serializable
{
	private static final long serialVersionUID = -5459877750200847290L;
	private String msg;
	
	public IndexDeployStat()
	{
		//String memberId = ServerConfigHelper.getProperty("MEMBER_ID");
		String ip = ServerConfigHelper.getConfig(ITEMS.BINDIP);
		String port = ServerConfigHelper.getProperty("JMX_RMI_PORT");
		String node = ServerConfigHelper.getProperty(ITEMS.NODE_NAME.name());
		msg =  ip + ":" + port + " "+node;
	}
  
 
	@ManagedOperation
	@AggregateOperation
	public String createIndex(String def)
	{
		IIndexContext context = IndexHelper.createIndex(def);
		String result = context != null ? msg+"create successfully" : msg+"create failed";
		return result;
	}
	
	@ManagedOperation
	@AggregateOperation
	public String dropIndex(String indexName)
	{
		IIndexContext context = IndexHelper.dropIndex(indexName);
		String result = context != null ? msg+"drop successfully " : msg+"drop failed";
		return result;
	}
	
	@ManagedOperation
	@AggregateOperation
	public List<String> listIndex(String regionName)
	{
		List<String> indexList =  IndexHelper.listIndex(regionName);
		return indexList;
	}
	
    @ManagedOperation
	@AggregateOperation
	public HashMap<String, Object> describeIndex(String indexName)
	{
		AbstractIndexStat stat = IndexMeasureHelper.getIndexStat(indexName);
		if(stat == null)
		    return null;
		Map indexInfoMap = stat.describeIndex();
		if(indexInfoMap == null || indexInfoMap.size() == 0)
			return null;
		
		String idxName = (String) indexInfoMap.get(Indexs.indexName.name());
		String regionName = (String) indexInfoMap.get(Indexs.regionName.name());
		String regionType = (String) indexInfoMap.get(Indexs.regionType.name());
		long entrySize = (long) indexInfoMap.get(Indexs.entrySize.name());
		Map<String,String> keyFields = (Map<String, String>) indexInfoMap.get(Indexs.keyFields.name());
		Map<String,String> valueFields = (Map<String, String>) indexInfoMap.get(Indexs.valueFields.name());
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		//此ip信息为展示时排序使用
		map.put(Indexs.ip.name(), ServerConfigHelper.getConfig(ITEMS.BINDIP)+ServerConfigHelper.getProperty(ITEMS.NODE_NAME.name()));
		map.put(Indexs.ipinfo.name(), msg);
		map.put(Indexs.indexName.name(), idxName);
		map.put(Indexs.regionName.name(), regionName);
		map.put(Indexs.regionType.name(), regionType);
		map.put(Indexs.entrySize.name(), entrySize);
		map.put(Indexs.keyFields.name(), keyFields);
		map.put(Indexs.valueFields.name(), valueFields);
		return map;
	}
	
//	@ManagedOperation
//	@AggregateOperation
//	public String printIndexValue(String indexName, String searchParamStr, int pageNumber, int pageSize)
//	{
//		AbstractIndexStat stat = IndexMeasureHelper.getIndexStat(indexName);
//		String value = stat.printIndexValue(searchParamStr, pageNumber, pageSize);
//		return value;
//	}
	
	@ManagedOperation
	@AggregateOperation
	public Map<String, Object> printIndexValue(String indexName, String searchParamStr, int pageNumber, int pageSize)
	{
		AbstractIndexStat stat = IndexMeasureHelper.getIndexStat(indexName);
		Map<String, Object> rtMap = stat.printIndexValue(searchParamStr, pageNumber, pageSize);
		return rtMap;
	}
	
}
