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

import gemlite.core.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/***
 * @DeployConfigure,@AutoSerialize属于优先处理
 *                                       缺省设置
 * @author ynd
 * 
 */
public class RegistryDefineContext
{
  
  private Map<String, ScannedRegistryDefine> regDefMap;
  private Set<ScannedRegistryDefine> orderedDefSet;
  
  public RegistryDefineContext()
  {
    regDefMap = new HashMap<>();
    orderedDefSet = new TreeSet<>();
  }
  
  public void addItem(String key, ScannedRegistryDefine def)
  {
    regDefMap.put(key, def);
    orderedDefSet.add(def);
  }
  
  public List<ScannedRegistryDefine> getScannedRegistryDefines()
  {
    List<ScannedRegistryDefine> list = new ArrayList<>();
    list.addAll(orderedDefSet);
    return list;
  }
  
  /***
   * Lgemlite.xxx.yyy.zzz;
   * 
   * @param internalDesc
   * @return
   */
  public ScannedRegistryDefine matchDefine(String internalDesc)
  {
    ScannedRegistryDefine def = regDefMap.get(internalDesc);
    return def;
  }
  
  public ScannedRegistryDefine matchDefine(Class<?> cls)
  {
    String internalDesc = Util.getInternalDesc(cls);
    return matchDefine(internalDesc);
  }
}
