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

import gemlite.core.internal.admin.measurement.ScannedMethodItem;
import gemlite.core.internal.measurement.MeasureHelper;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;
import gemlite.core.internal.support.hotdeploy.scanner.RegistryMatchedContext;
import gemlite.core.internal.support.jpa.files.service.CheckPointsService;
import gemlite.core.util.GemliteHelper;
import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

public class CheckPointScanner
{
  
  /***
   * [module:[class,[method]]]
   */
  private static Map<String, Map<String, Set<ScannedMethodItem>>> checkMap = new HashMap<>();
  private static Set<String> classNames = new HashSet<>();
  public static boolean DEBUG = false;
  
  public Set<ScannedMethodItem> getMethodNames(String moduleName, String className)
  {
    if (checkMap.get(moduleName) != null)
    {
      Set<ScannedMethodItem> methodNames = checkMap.get(moduleName).get(className);
      return methodNames;
    }
    return null;
  }
  
  /***
   * 
   * @param internalDesc
   * @return
   */
  public boolean matchClassName(String name)
  {
    return classNames.contains(name);
  }
  
  @SuppressWarnings("unchecked")
  public void reloadSettings(String module)
  {
    /*
     * LogUtil.getCoreLog().trace("Load checkpoint settings.");
     * Map<String, Map<String, Set<String>>> m = (Map<String, Map<String, Set<String>>>)
     * GemliteDeployer.getSession().get(
     * "_checkPointMap_");
     */
    Map<String, Set<ScannedMethodItem>> methodNames = null;
    if (DEBUG)
    {
      LogUtil.getCoreLog().trace("Load checkpoint settings.");
      methodNames = (Map<String, Set<ScannedMethodItem>>) GemliteDeployer.getSession().get("_checkPointMap_");
    }
    else
    {
      CheckPointsService service = JpaContext.getService(CheckPointsService.class);
      methodNames = service.findMapByModule(module);
    }
    if (methodNames != null)
    {
      classNames.clear();
      classNames.addAll(methodNames.keySet());
      CheckPointScanner.checkMap.put(module, methodNames);
    }
    
  }
  
  public void processCheckPoint(RegistryMatchedContext rmc)
  {
    LogUtil.getCoreLog().info("Process checkpoint instrument for module {}", rmc.getModuleName());
    Map<String, Set<ScannedMethodItem>> classMethods = checkMap.get(rmc.getModuleName());
    Map<String, ClassNode> cache = rmc.getCachedClassNode();
    Iterator<Entry<String, ClassNode>> it = cache.entrySet().iterator();
    while (classMethods != null && it.hasNext())
    {
      Entry<String, ClassNode> e = it.next();
      Set<ScannedMethodItem> methods = classMethods.get(e.getKey());
      if (methods == null)
        continue;
      ClassNode cn = e.getValue();
      int num = MeasureHelper.instrumentCheckPoint(e.getKey(), cn, methods);
      LogUtil.getCoreLog().trace("CheckPoint class:{},methods:{},num:{}", cn.name, methods, num);
      if (num > 0)
      {
        rmc.getLoader().addDynamicClass(e.getKey(), GemliteHelper.classNodeToBytes(cn));
      }
    }
  }
  
}
