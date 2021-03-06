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
import gemlite.core.internal.domain.DomainUtil;
import gemlite.core.internal.domain.IDataSource;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.mq.MqConstant;
import gemlite.core.internal.mq.MqThreadPool;
import gemlite.core.internal.mq.domain.ItemByKey;
import gemlite.core.util.LogUtil;

import java.util.List;

import com.gemstone.gemfire.CopyHelper;
import com.gemstone.gemfire.cache.Region;


@SuppressWarnings({ "rawtypes","unchecked" })
public class MqSyncExecutor
{
  /***
   * @param r
   * @param tableName
   * @param mqDomains
   *          ，相同主键的多个操作
   * 
   *          在客户端发送到服务端之前，已经做过了操作的合并
   *          相同key的操作只会有一条，即：
   *          1.若不存在改key， 每个key只有一条记录，新增、修改或者删除
   *          2.存在改key，那改之前和改之后的均只出现一条记录，连续改key的情况在客户端已经处理掉
   *          3.服务端不需要处理改Key
   *          4.改key一定是insert
   * 
   */
  
  public void processByOneKey(Region r, String tableName, List<ItemByKey> items)
  {
    String regionName = r.getName();
    IMapperTool tool = DomainRegistry.getMapperTool(regionName);
    for (ItemByKey item : items)
    {
      processOneItem(r, item, tool);
    }
  }
  
	public void processASync(Region region, String tableName,
			List<ItemByKey> items)
	{
		String regionName = region.getName();
		IMapperTool tool = DomainRegistry.getMapperTool(regionName);
		AsyncProcessTask task = new AsyncProcessTask(this, region, tool, items,
				0, items.size());
		MqThreadPool.mqSyncTaskPool.execute(task);

	}

	protected void processOneItem(Region r, ItemByKey item, IMapperTool tool)
	{
		String regionName = r.getName();
		IDataSource ds = DomainUtil.getDataSource(item.values);
		Object oldValue = null;
		if (item.nodeChange)
		{
			if (null != item.originKey)
				oldValue = r.get(item.originKey);
		}
		// 如发生主键变更，getKey代表当前操作前一次的主键(会发生 多次变更)
		// putKey代表当前操作对应的主键，由于withFilter是按照第一个主键分配服务，put时可能发生跨节点的情况
		if (MqConstant.INSERT.equals(item.operation))
		{
			Object newValue = null;
			if (item.nodeChange)
			{
				if (null != item.originKey)
					oldValue = r.get(item.originKey);
				oldValue = CopyHelper.copy(oldValue);
				if (oldValue == null)
					newValue = tool.mapperValue(ds);
				else
					newValue = tool.mapperValue(ds,oldValue);				
			}
			if (null != newValue)
				r.put(item.key, newValue);
			
		} else if (MqConstant.UPDATE.equals(item.operation))
		{
			oldValue = r.get(item.key);
			oldValue = CopyHelper.copy(oldValue);
			// 更新操作，但是没有原数据，可以选择异常停止，或者写入部分数据，记录错误
			// TODO:
			if (oldValue == null)
			{
				// oldValue = (ISyncDomain) mapper.mapper(md.getValue());
				LogUtil.getMqSyncLog().error(
						"Update get null by key:" + item.key + " region:"
								+ regionName);
			}
			//oldValue = tool.mapperValue(ds, oldValue);
			Object newValue = null;
			if (oldValue == null)
				newValue = tool.mapperValue(ds);
			else
				newValue = tool.mapperValue(ds,oldValue);
			
			r.put(item.key, newValue);
		} else if (MqConstant.DELETE.equals(item.operation))
		{
			r.remove(item.key);
		} else
		{
			LogUtil.getMqSyncLog().error(
					"Error operation type:" + item.operation + " region:"
							+ regionName);
		}
	}
}
