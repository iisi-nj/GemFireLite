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
package gemlite.core.internal.hotdeploy;

import gemlite.core.internal.index.IndexHelper;
import gemlite.core.internal.measurement.MeasureHelper;
import gemlite.core.internal.measurement.index.IndexMeasureHelper;
import gemlite.core.internal.measurement.view.ViewMeasureHelper;
import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.DeployListener;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.internal.support.hotdeploy.scanner.RegistryMatchedContext;
import gemlite.core.internal.support.jmx.GemliteNode;
import gemlite.core.util.LogUtil;

import org.springframework.transaction.CannotCreateTransactionException;

public class RuntimeDeployListener implements DeployListener
{
  CheckPointScanner cp = new CheckPointScanner();
  
  /***
   * if checkpoint list exist,match and cache classNode
   */
  @Override
  public void afterScan(RegistryMatchedContext mach)
  {
    
  }
  
  /***
   * process checkpoint
   */
  @Override
  public void beforeInstantiate(RegistryMatchedContext mach)
  {
    if (mach.getModuleType() == ModuleType.LOGIC)
    {
      cp.reloadSettings(mach.getModuleName());    
      cp.processCheckPoint(mach);
    }
  }
  
  /***
   * 1.runtime module
   * load checkpoint from db
   * 2.logic module
   * 
   */
  @Override
  public void afterDeploy(IModuleContext module)
  {
    if (module.getModuleType() == ModuleType.RUNTIME)
    {
      MeasureHelper.init();
      IndexMeasureHelper.init();
      ViewMeasureHelper.init();
      try
      {
        IndexHelper.loadAllIndexsFromDB();
      }
      catch (CannotCreateTransactionException e)
      {
        LogUtil.getCoreLog().warn("Load All indexs from db failure", e);
      }
      catch (Exception e)
      {
        LogUtil.getCoreLog().error("Load Index error:", e);
      }
    }
    GemliteNode.getInstance().afterDeploy();
  }
  
//  @Override
//  public void afterClassNodeScanned(RegistryMatchedContext mach, ClassNode cn)
//  {
//    String name = cn.name.replace('/', '.');
//    if (cp.matchClassName(name))
//    {
//      LogUtil.getCoreLog().trace("CheckPoint matched class:{}", name);
//      mach.getCachedClassNode().put(name, cn);
//    }
//  }

  @Override
  public void beforeScan(GemliteSibingsLoader loader)
  {
  }
  
}
