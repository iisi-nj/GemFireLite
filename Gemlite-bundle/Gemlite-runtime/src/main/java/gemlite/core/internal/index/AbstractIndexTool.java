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
package gemlite.core.internal.index;

import gemlite.core.internal.view.bean.ViewItem;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbstractIndexTool<K, V> implements IndexTool<K, V>
{
	private Map<String, ViewItem> viewContext = new ConcurrentHashMap<String, ViewItem>();

	@Override
	public Region init()
	{
		return null;
	}
	
	
	protected Object[] getLocalDataValues(String regionName)
	{
		Cache c = CacheFactory.getAnyInstance();
		Region r = c.getRegion(regionName);
		if (PartitionRegionHelper.isPartitionedRegion(r))
			r = PartitionRegionHelper.getLocalData(r);
		Object[] arr = r.values().toArray();
		return arr;
	}
	
	@Override
	public Iterator getAllDataValues(String regionName)
	{
		Cache c = CacheFactory.getAnyInstance();
		Region r = c.getRegion(regionName);
		if (PartitionRegionHelper.isPartitionedRegion(r))
			r = PartitionRegionHelper.getLocalData(r);
		
		Iterator iters = null;
		if (r != null)
			iters = r.entrySet().iterator();
		return iters;
	}
	
	@Override
	public Map<String, ViewItem> getViewContext()
	{
		return viewContext;
	}
	
	@Override
	public void registerViewItem(ViewItem item)
	{
		if (item != null)
		{
			String viewItem = item.getName();
			this.viewContext.put(viewItem, item);
		}
	}
	
	@Override
	public void removeViewItem(String viewName)
	{
		this.viewContext.remove(viewName);
	}

	

}
