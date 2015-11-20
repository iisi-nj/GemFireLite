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
package gemlite.core.internal.support.context;

import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

public interface RegistryParam
{
  public String getClassName();// class or file
  public ClassNode getClassNode();
//  public String getRegistryType();
  public Object getParam(String key);
  public Map<String, Object> getParams();
  public Object getAnnValue();
  public Object getAnnValue(String key);
}
