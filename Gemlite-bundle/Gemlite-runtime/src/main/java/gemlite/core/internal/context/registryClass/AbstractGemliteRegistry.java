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

import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.IGemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGemliteRegistry implements IGemliteRegistry
{
  private List<RegistryParam> params;
  
  public AbstractGemliteRegistry()
  {
    params = new ArrayList<>();
  }
  
  @Override
  public void addParam(RegistryParam param)
  {
    params.add(param);
  }
  
  @Override
  public void addParams(List<RegistryParam> params)
  {
    this.params.addAll(params);
  }
  
  
  public Object getItems()
  {
    return "Not support";
  }
  
  protected abstract void doRegistry(IModuleContext context, RegistryParam param) throws Exception;
  
  @Override
  public void register(IModuleContext context)
  {
    RegistryParam param=null;
    try
    {
      for (int i=0;i<params.size();i++)
      {
        param = params.get(i);
        doRegistry(context, param);
      }
    }
    catch (Exception e)
    {
      throw new GemliteException("Class:"+param.getClassName(), e);
    }
  }
  
}
