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
package gemlite.core.internal.admin.service;

import gemlite.core.annotations.admin.AdminService;
import gemlite.core.internal.admin.AbstractRemoteAdminService;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheClosedException;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;

@SuppressWarnings({ "rawtypes", "unchecked" })
@AdminService(name = "ListRegionsService")
public class ListRegionsService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    try
    {
      Cache cache = CacheFactory.getAnyInstance();
      Set<Region<?,?>> regions = cache.rootRegions();
      if ((regions.isEmpty()) || (regions == null))
      {
        return "region is empty";
      }
      else
      {
        TreeSet regionInformationSet = new TreeSet();
        for (Region region : regions)
        {
          regionInformationSet.add(region.getFullPath().substring(1));
        }
       return regionInformationSet;
      }
    }
    catch (CacheClosedException e)
    {
      return e.getMessage();
    }
    catch (Exception e)
    {
      return e.getMessage();
    }
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
  }
}
