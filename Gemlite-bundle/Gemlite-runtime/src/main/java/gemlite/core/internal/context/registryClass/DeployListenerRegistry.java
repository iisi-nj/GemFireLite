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
import gemlite.core.internal.support.hotdeploy.DeployListener;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;

import java.util.ArrayList;
import java.util.List;

/***
 * regionName -> index tool 1,index tool2
 * 
 * @author ynd
 * 
 */

@GemliteRegistry(value=DeployListener.class)
@SuppressWarnings({ "unchecked" })
public class DeployListenerRegistry extends AbstractGemliteRegistry
{
  private List<DeployListener> listeners = new ArrayList<>();
  
  @Override
  protected void doRegistry(IModuleContext context, RegistryParam param) throws Exception
  {
    String className = param.getClassName();
    Class<DeployListener> cls = (Class<DeployListener>) context.getLoader()
        .loadClass(className);
    DeployListener listener = cls.newInstance();
    GemliteDeployer.getInstance().registerDeployListener(listener);
  }
  
  
  @Override
  public void cleanAll()
  {
    for (DeployListener l : listeners)
    {
      GemliteDeployer.getInstance().registerDeployListener(l);
    }
    
  }
  
  @Override
  public Object getItem(Object key)
  {
    return null;
  }
}
