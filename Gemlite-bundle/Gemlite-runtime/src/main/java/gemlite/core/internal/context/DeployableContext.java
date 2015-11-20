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
package gemlite.core.internal.context;

import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.util.LogUtil;

import java.net.URL;

public abstract class DeployableContext implements IModuleContext
{
  private String name;
  private ModuleType type;
  private boolean coreModule;
  private GemliteSibingsLoader loader;
  
  public DeployableContext(String name, ModuleType type, GemliteSibingsLoader loader)
  {
    this.name = name;
    this.type = type;
    this.loader = loader;
    this.coreModule = ModuleType.RUNTIME == type;
  }
  
  @Override
  public void clean()
  {
    if (loader != null)
    {
      LogUtil.getCoreLog().info("Add loader to ClassPathLoader");
      loader.clean();
      LogUtil.getCoreLog().info("Context->" + name + " jar loader closed.");
    }
  }
  
  public GemliteSibingsLoader getLoader()
  {
    return loader;
  }
  
  public String getModuleName()
  {
    return name;
  }
  
  public ModuleType getModuleType()
  {
    return type;
  }
  
  public boolean isCoreModule()
  {
    return coreModule;
  }
  
  public URL getJarURL()
  {
    return  loader.getURL();
  }
  
}
