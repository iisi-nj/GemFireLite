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
package gemlite.core.internal.support.jmx.domain;

import gemlite.core.internal.support.jmx.annotation.AggregateOperation;

import java.util.HashMap;
import java.util.Map;

public class JMXBeanInfo
{
  public JMXBeanInfo()
  {
    super();
    this.fieldInfo = new HashMap<>();
    this.operInfo = new HashMap<>();
  }
  
  private Map<String, JMXBeanFieldInfo> fieldInfo;
  private Map<String, AggregateOperation> operInfo;
  private String[] attributeNames;
  private Map<String, Integer> nameIndex;
  
  public Map<String, JMXBeanFieldInfo> getFieldInfo()
  {
    return fieldInfo;
  }
  public void setFieldInfo(Map<String, JMXBeanFieldInfo> fieldInfo)
  {
    this.fieldInfo = fieldInfo;
  }
  public Map<String, AggregateOperation> getOperInfo()
  {
    return operInfo;
  }
  public void setOperInfo(Map<String, AggregateOperation> operInfo)
  {
    this.operInfo = operInfo;
  }
  public String[] getAttributeNames()
  {
    return attributeNames;
  }
  public void setAttributeNames(String[] attributeNames)
  {
    this.attributeNames = attributeNames;
  }
  public Map<String, Integer> getNameIndex()
  {
    return nameIndex;
  }
  public void setNameIndex(Map<String, Integer> nameIndex)
  {
    this.nameIndex = nameIndex;
  }
}
