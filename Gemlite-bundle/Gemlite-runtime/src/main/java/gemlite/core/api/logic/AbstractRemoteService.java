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
package gemlite.core.api.logic;

import gemlite.core.api.ApiConstant;
import gemlite.core.internal.logic.IRemoteService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;

@SuppressWarnings("rawtypes")
public abstract class AbstractRemoteService<R> implements IRemoteService<R>,ApiConstant
{
  protected String regionName;
  protected String beanName;
  protected String moduleName;
  
  
  protected LogicSession getSession()
  {
    return LogicSession.getSession();
  }
  
  protected IRemoteService getRemoteService(String serviceName)
  {
    return LogicServices.getService(moduleName, serviceName);
  }
  
  @Override
  public RemoteResult remoteExecute(Object userArgs, Set<Object> filters)
  {
    Cache c = CacheFactory.getAnyInstance();
    
    Region r = c.getRegion(regionName);
    Map<String, Object> map = new HashMap<>();
    //class com.gemstone.gemfire.internal.cache.execute.PartitionedRegionFunctionExecutor
    Execution ex = FunctionService.onRegion(r);
    ex = ex.withFilter(filters);
    map.put(PARAM_MODULE_NAME, moduleName);
    map.put(PARAM_BEAN_NAME, beanName);
    map.put(PARAM_URSER_ARGS, userArgs);
    ex = ex.withArgs(map);
    RemoteResult rr = new RemoteResult(ex);
    return rr;
  }
}
