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

import gemlite.core.internal.support.jmx.AggregateType;

public class JMXBeanFieldInfo
{
  private AggregateType aggrType;
  public JMXBeanFieldInfo(AggregateType aggrType, String name, String getName)
  {
    super();
    this.aggrType = aggrType;
    this.name = name;
    this.getName = getName;
  }
  private String name;
  private String getName;
  
  
  public AggregateType getAggrType()
  {
    return aggrType;
  }
  public void setAggrType(AggregateType aggrType)
  {
    this.aggrType = aggrType;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  public String getGetName()
  {
    return getName;
  }
  public void setGetName(String getName)
  {
    this.getName = getName;
  }
}
