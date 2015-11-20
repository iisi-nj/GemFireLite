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

import gemlite.core.internal.support.context.RegistryParam;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

public class ScannedItem implements RegistryParam
{
  private String className;//class or file
  private ClassNode classNode;
  private Map<Object,Object> annValues;
//  private String registryType;
  private byte[] bytesTransformed;
  private Map<String, Object> params;
  public ScannedItem(String className,ClassNode cn)
  {
    annValues = new HashMap<>();
    params=new HashMap<>();
    this.className= className;
    this.classNode = cn;
//    this.registryType = registryType;
    params.put("className", className);
    params.put("classNode", cn);
  }
  
  public Map<String, Object> getParams()
  {
    return params;
  }
  public Object getParam(String key)
  {
    return params.get(key);
  }
  public void addParam(String key,Object value)
  {
    params.put(key, value);
  }
  public void  addAnnValue( Object key,Object value)
  {
    annValues.put(key, value);
  }
  public Object getAnnValue()
  {
   return getAnnValue("value");
  }
  public Object getAnnValue(String key)
  {
    return annValues.get(key);
  }
  
  public String getClassName()
  {
    return className;
  }
  
  public ClassNode getClassNode()
  {
    return classNode;
  }


//  public String getRegistryType()
//  {
//    return registryType;
//  }

  public byte[] getBytesTransformed()
  {
    return bytesTransformed;
  }

  public void setBytesTransformed(byte[] bytesTransformed)
  {
    this.bytesTransformed = bytesTransformed;
  }

  @Override
  public String toString()
  {
    return "class:"+className;
  }


  
}
