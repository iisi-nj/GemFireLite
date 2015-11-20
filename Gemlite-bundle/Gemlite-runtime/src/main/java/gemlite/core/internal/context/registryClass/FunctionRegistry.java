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
package gemlite.core.internal.context.registryClass;

import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionService;


@GemliteRegistry(value=Function.class)
public class FunctionRegistry extends AbstractGemliteRegistry
{
  private Map<String, Function> deployedFunctions = new HashMap<>();
  
  @SuppressWarnings("unchecked")
  @Override
  protected void doRegistry(IModuleContext context, RegistryParam param) throws Exception
  {
    String className = param.getClassName();
    Class<Function> cls = (Class<Function>) context.getLoader().loadClass(className);
    Function f = cls.newInstance();
    FunctionService.registerFunction(f);
    LogUtil.getCoreLog().info("Register function ,id=" + f.getId());
    deployedFunctions.put(f.getId(), f);
  }
  
  @Override
  public void cleanAll()
  {
    Iterator<Function> values = deployedFunctions.values().iterator();
    while (values.hasNext())
    {
      Function f = values.next();
      FunctionService.unregisterFunction(f.getId());
      LogUtil.getCoreLog().info("Unregister function ,id=" + f.getId());
    }
    deployedFunctions.clear();
    deployedFunctions = null;
  }
  
  @Override
  public Object getItem(Object key)
  {
    return deployedFunctions.get(key);
  }
  
}
