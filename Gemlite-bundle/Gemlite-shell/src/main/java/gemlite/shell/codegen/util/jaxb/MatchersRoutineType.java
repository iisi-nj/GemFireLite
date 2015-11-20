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
package gemlite.shell.codegen.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersRoutineType", propOrder = {})
@SuppressWarnings({ "all" })
public class MatchersRoutineType implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String expression;
  protected MatcherRule routineClass;
  protected MatcherRule routineMethod;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String routineImplements;
  
  public String getExpression()
  {
    return expression;
  }
  
  public void setExpression(String value)
  {
    this.expression = value;
  }
  
  public MatcherRule getRoutineClass()
  {
    return routineClass;
  }
  
  public void setRoutineClass(MatcherRule value)
  {
    this.routineClass = value;
  }
  
  public MatcherRule getRoutineMethod()
  {
    return routineMethod;
  }
  
  public void setRoutineMethod(MatcherRule value)
  {
    this.routineMethod = value;
  }
  
  public String getRoutineImplements()
  {
    return routineImplements;
  }
  
  public void setRoutineImplements(String value)
  {
    this.routineImplements = value;
  }
  
  public MatchersRoutineType withExpression(String value)
  {
    setExpression(value);
    return this;
  }
  
  public MatchersRoutineType withRoutineClass(MatcherRule value)
  {
    setRoutineClass(value);
    return this;
  }
  
  public MatchersRoutineType withRoutineMethod(MatcherRule value)
  {
    setRoutineMethod(value);
    return this;
  }
  
  public MatchersRoutineType withRoutineImplements(String value)
  {
    setRoutineImplements(value);
    return this;
  }
}
