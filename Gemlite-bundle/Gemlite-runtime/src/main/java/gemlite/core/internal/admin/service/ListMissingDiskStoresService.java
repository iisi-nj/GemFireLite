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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gemstone.gemfire.admin.internal.AdminDistributedSystemImpl;
import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;


@SuppressWarnings("rawtypes")
@AdminService(name = "ListMissingDiskStoresService")
public class ListMissingDiskStoresService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    StringBuilder sb = new StringBuilder();
    Cache cache = CacheFactory.getAnyInstance();
    InternalDistributedSystem ads = (InternalDistributedSystem) cache.getDistributedSystem();
    Set s = AdminDistributedSystemImpl.getMissingPersistentMembers(ads.getDistributionManager());
    Iterator it = s.iterator();
    if (s.isEmpty())
    {
      sb.append("The distributed system did not have any missing disk stores");      
    }
    else
    {
      while (it.hasNext())
      {
        Object o = it.next();
        sb.append(o).append("\n");
      }
    }
    return sb.toString();
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
    // TODO Auto-generated method stub
    
  }
}
