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
package gemlite.core.api.view;

import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;

public class ViewHelper
{
	private Cache cache;
	private String viewName;
	private Region<Object, Object> region = null;

	public ViewHelper(String viewName)
	{
		this.viewName = viewName;
		cache = CacheFactory.getAnyInstance();
		region = cache.getRegion(viewName);
	}

	public boolean isExists()
	{
		if (region == null)
			region = cache.getRegion(viewName);

		if (region == null)
		{
			LogUtil.getAppLog().error(
					"View [" + viewName + "] can't find in the node.");
			return false;
		}
		return true;
	}

	/**
	 * get all values
	 * 
	 * @param viewName
	 * @return
	 */
	public Set<Map.Entry<Object, Object>> getAll()
	{
		if (isExists())
			return region.entrySet();
		else
			return null;

	}

	/**
	 * get value
	 * 
	 * @param key
	 * @return
	 */
	public Object getValue(Object key)
	{
		Object value = null;
		if (isExists())
			value = region.get(key);

		return value;
	}

	/**
	 * get values
	 * 
	 * @param keys
	 * @return
	 */
	public Map<Object, Object> getValues(Set<String> keys)
	{
		Map<Object, Object> values = new HashMap<Object, Object>();

		if (isExists())
		{
			for (String key : keys)
			{
				Object value = region.get(key);
				if (value != null)
					values.put(key, value);
			}
		}

		return values;
	}

	/**
	 * set value
	 * 
	 * @param key
	 * @param value
	 */
	public void putValue(Object key, Object value)
	{
		try
		{
			if (isExists())
				region.put(key, value);
		} catch (Exception e)
		{
			e.printStackTrace();
			LogUtil.getAppLog().error(
					"View " + viewName + " put value failed.", e);
		}
	}

	/**
	 * remove value
	 * 
	 * @param key
	 */
	public void removeValue(Object key)
	{
		if (isExists())
			region.remove(key);
	}

	/**
	 * set values
	 * 
	 * @param values
	 */
	public void putValues(Map<Object, Object> values)
	{
		try
		{
			if (isExists())
			{
				Iterator<Entry<Object, Object>> iters = values.entrySet()
						.iterator();
				while (iters.hasNext())
				{
					Entry<Object, Object> entry = iters.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					region.put(key, value);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			LogUtil.getAppLog().error(
					"View " + viewName + " put value failed.", e);
		}
	}

	/**
	 * contains Key
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(Object key)
	{
		if (isExists())
		{
			return region.containsKey(key);
		}

		return false;
	}

	public boolean containsValue(Object value)
	{
		if (isExists())
		{
			return region.containsValue(value);
		}

		region.clear();

		return false;
	}

	public int size()
	{
		if (isExists())
			return region.size();
		return 0;
	}

	public void clear()
	{
		if (isExists())
		{
			region.clear();
			if (LogUtil.getAppLog().isInfoEnabled())
				LogUtil.getAppLog().info(
						"View [" + viewName + "] has been cleared.");
		}
	}

	public void destory()
	{
		if (isExists())
		{
			region.destroyRegion();
			if (LogUtil.getAppLog().isInfoEnabled())
				LogUtil.getAppLog().info(
						"View [" + viewName + "] has been destoryed.");
		}
	}

}
