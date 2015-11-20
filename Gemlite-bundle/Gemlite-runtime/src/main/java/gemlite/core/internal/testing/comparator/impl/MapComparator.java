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
package gemlite.core.internal.testing.comparator.impl;

import gemlite.core.internal.testing.comparator.CompareFunction;
import gemlite.core.internal.testing.comparator.ResultComparator;

import java.util.List;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

public class MapComparator implements ResultComparator
{
	private String regionName;

	public MapComparator(String regionName)
	{
		this.regionName = regionName;
	}

	@Override
	public boolean compare()
	{
		ClientCache cc = ClientCacheFactory.getAnyInstance();
		Execution ex = FunctionService.onServers(cc.getDefaultPool());
		ResultCollector rc = ex.withArgs(regionName).execute(
				new CompareFunction().getId());
		List result = (List) rc.getResult();
		// if (result.contains(Boolean.FALSE))
		if (result != null && result.size() > 0)
		{
			if (result.get(0).equals("unmatched"))
				return false;
			else
				return true;
		} else
			return false;
	}

	public String compareData()
	{
		ClientCache cc = ClientCacheFactory.getAnyInstance();
		Execution ex = FunctionService.onServers(cc.getDefaultPool());
		ResultCollector rc = ex.withArgs(regionName).execute(
				new CompareFunction().getId());
		List result = (List) rc.getResult();
		if (result != null && result.size() > 0)
		{
			return result.get(0).toString();
		} else
			return "";
	}
}
