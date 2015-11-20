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
package gemlite.core.internal.logic;

import gemlite.core.annotations.logic.RemoteService;
import gemlite.core.internal.context.registryClass.RemoteServiceRegistry;
import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class ListServiceFunction implements Function
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -899735084885518363L;
  
  @Override
  public boolean hasResult()
  {
    return true;
  }
  
  @Override
  public void execute(FunctionContext fc)
  {
    Map<String, Object> result = new HashMap<String, Object>();
    try
    {
      Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
      while (it.hasNext())
      {
        IModuleContext m = it.next();
        if (ModuleType.LOGIC ==m.getModuleType())
        {
          String name = m.getModuleName();
          RemoteServiceRegistry reg = (RemoteServiceRegistry) m.getRegistry(RemoteService.class);
          if(reg != null)
          {
	          Set<String> srvNames = reg.getServiceMap().keySet();
	          HashSet<String> set = new HashSet<>();
	          set.addAll(srvNames);
	          result.put(name, set);
          }
        }
        else
          continue;
      }
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("", e);
    }
    fc.getResultSender().lastResult(result);
  }
  
  @Override
  public String getId()
  {
    return getClass().getName();
  }
  
  @Override
  public boolean optimizeForWrite()
  {
    return false;
  }
  
  @Override
  public boolean isHA()
  {
    return false;
  }
  
}
