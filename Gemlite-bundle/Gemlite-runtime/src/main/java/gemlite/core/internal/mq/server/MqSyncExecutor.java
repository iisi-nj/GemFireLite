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
   *          �;.�*�\
   * 
   *          (�7��0��KM��Zǆ�\�v
   *          �key��\�	 as
   *          1.�X(9key �*key�	 a�U���9 d
   *          2.X(9key�9KM�9K�G��� a�U��9key�ŵ(�7����
   *          3.�� �9Key
   *          4.9key �/insert
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
		// ��;.��getKey�hSM�\M !�;.(� !��)
		// putKey�hSM�\���;.1�withFilter/	g, *;.M�put����肹�ŵ
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
			// ���\F/�	�pn��	�8\b�e�pn�U�
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
