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
import gemlite.core.internal.admin.AdminUtil;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;

import java.util.HashMap;
import java.util.Map;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;

@AdminService(name = "ListMembersService")
public class ListMembersService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    try
    {
      Cache cache = CacheFactory.getAnyInstance();
      HashMap<String, String> members = new HashMap<String, String>();
      String ip = AdminUtil.getIp() + " " + System.getProperty(ITEMS.NODE_NAME.name());
      members.put(cache.getDistributedSystem().getDistributedMember().getId(), ip);
      return members;
    }
    catch (Exception e)
    {
      return "Could not fetch the list of members. " + e.getMessage();
    }
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
    // TODO Auto-generated method stub
    
  }
}
