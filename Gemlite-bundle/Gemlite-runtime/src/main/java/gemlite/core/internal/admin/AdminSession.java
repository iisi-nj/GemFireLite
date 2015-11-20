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
package gemlite.core.internal.admin;

import gemlite.core.api.ApiConstant;
import gemlite.core.internal.measurement.RemoteServiceStatItem;

import java.util.HashMap;
import java.util.Map;

public class AdminSession implements ApiConstant
{
  private static ThreadLocal<AdminSession> logicRuntime = new ThreadLocal<>();
  private Map<String, Object> values = new HashMap<>();
  
  private String moduleName;
  
  public Object getUserArgs()
  {
    return values.get(PARAM_URSER_ARGS);
  }
  
  public Object get(Object key)
  {
    return values.get(key);
  }
  
  public Object put(String key, Object value)
  {
    return values.put(key, value);
  }
  
  public void setRemoteServiceStatItem(RemoteServiceStatItem si)
  {
    values.put("RemoteServiceStatItem", si);
  }
  
  public RemoteServiceStatItem getRemoteServiceStatItem()
  {
    return  (RemoteServiceStatItem)values.get("RemoteServiceStatItem");
  }
  
  public void putAll(Map<? extends String, ? extends Object> m)
  {
    values.putAll(m);
  }
  
  public final static AdminSession getSession()
  {
    AdminSession session = logicRuntime.get();
    if (session == null)
      session = new AdminSession();
    return session;
  }
  
  public String getModuleName()
  {
    return moduleName;
  }
  
  public void setModuleName(String moduleName)
  {
    this.moduleName = moduleName;
  }
  
  public void clear()
  {
    moduleName = null;
    values.clear();
  }
  
}
