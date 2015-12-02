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
package gemlite.core.internal.support.hotdeploy.scanner;

import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.support.hotdeploy.DeployListener;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.tree.ClassNode;

/***
 * @DeployConfigure,@AutoSerialize属于优先处理
 *                                       缺省设置
 * @author ynd
 * 
 */
public class RegistryMatchedContext
{
  private GemliteSibingsLoader loader;
  private Map<ScannedRegistryDefine, List<RegistryParam>> scannedItems;
  private String moduleName;
  private ModuleType moduleType;
  private Map<String, ClassNode> cachedClassNode = new HashMap<>();
  
  public RegistryMatchedContext()
  {
    scannedItems = new TreeMap<>();
  }
  
  public void setModuleInfo(String name,String type)
  {
    
    this.moduleName = name;
    this.moduleType = StringUtils.isEmpty(type)?ModuleType.LOGIC: ModuleType.valueOf(type);
  }
  
//  public Map<String, ClassNode> getClassNodeCache()
//  {
//    return nodeCache;
//  }
//  public void cacheClassNode(String className,ClassNode cn)
//  {
//    nodeCache.put(className, cn);
//  }
  
  public RegistryParam getSingleItem(ScannedRegistryDefine matchKey)
  {
    List<RegistryParam> l = scannedItems.get(matchKey);
    return l != null && l.size() == 1 ? l.get(0) : null;
  }
  
  
  public List<RegistryParam> getItems(ScannedRegistryDefine matchKey)
  {
    return scannedItems.get(matchKey);
  }
  
  public void addItem(ScannedRegistryDefine key, RegistryParam item)
  {
    List<RegistryParam> list = scannedItems.get(key);
    if (list == null)
    {
      list = new ArrayList<>();
      scannedItems.put(key, list);
    }
    list.add(item);
  }
  public Map<ScannedRegistryDefine, List<RegistryParam>> getScannedItems()
  {
    return scannedItems;
  }

  public String getModuleName()
  {
    return moduleName;
  }

  public ModuleType getModuleType()
  {
    return moduleType;
  }

  public GemliteSibingsLoader getLoader()
  {
    return loader;
  }

  public void setLoader(GemliteSibingsLoader loader)
  {
    this.loader = loader;
  }

  public Map<String, ClassNode> getCachedClassNode()
  {
    return cachedClassNode;
  }

  public void setCachedClassNode(Map<String, ClassNode> cachedClassNode)
  {
    this.cachedClassNode = cachedClassNode;
  }

}
