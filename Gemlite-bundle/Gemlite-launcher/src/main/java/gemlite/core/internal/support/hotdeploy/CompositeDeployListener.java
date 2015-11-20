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
package gemlite.core.internal.support.hotdeploy;

import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.scanner.RegistryMatchedContext;
import gemlite.core.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class CompositeDeployListener implements DeployListener
{
  private List<DeployListener> dpList = new ArrayList<>();
  
  public void registerDeployListener(DeployListener lisener)
  {
    dpList.add(lisener);
  }
  
  public void removeDeployListener(DeployListener lisener)
  {
    dpList.remove(lisener);
  }
  
  @Override
  public void beforeInstantiate(RegistryMatchedContext mach)
  {
    try
    {
      LogUtil.getCoreLog().info("Deploy beforeInstantiate:" + mach.getModuleName());
      for (DeployListener l : dpList)
      {
        l.beforeInstantiate(mach);
      }
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().warn(e);
    }
  }
  
  public void afterScan(RegistryMatchedContext mach)
  {
    LogUtil.getCoreLog().info("Deploy afterScan:" + mach.getModuleName());
    for (DeployListener l : dpList)
    {
      l.afterScan(mach);
    }
  }
  
  public void afterDeploy(IModuleContext module)
  {
    LogUtil.getCoreLog().info("Deploy afterDeploy:" + module.getModuleName());
    for (DeployListener l : dpList)
    {
      l.afterDeploy(module);
    }
  }
  
  // @Override
  // public void afterClassNodeScanned(RegistryMatchedContext mach,ClassNode cn)
  // {
  // LogUtil.getCoreLog().trace("Deploy afterClassNodeScanned:"+cn.name);
  // for(DeployListener l:dpList)
  // {
  // l.afterClassNodeScanned(mach,cn);
  // }
  // }
  
  @Override
  public void beforeScan(GemliteSibingsLoader loader)
  {
    LogUtil.getCoreLog().info("Deploy beforeScan:" + loader.getURL());
    for (DeployListener l : dpList)
    {
      l.beforeScan(loader);
    }
    
  }
  
}
