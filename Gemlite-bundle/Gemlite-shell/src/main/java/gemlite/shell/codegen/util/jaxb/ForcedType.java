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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ForcedType", propOrder = {})
@SuppressWarnings({ "all" })
public class ForcedType implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String name;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String expression;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String expressions;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String types;
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String value)
  {
    this.name = value;
  }
  
  public String getExpression()
  {
    return expression;
  }
  
  public void setExpression(String value)
  {
    this.expression = value;
  }
  
  public String getExpressions()
  {
    return expressions;
  }
  
  public void setExpressions(String value)
  {
    this.expressions = value;
  }
  
  public String getTypes()
  {
    return types;
  }
  
  public void setTypes(String value)
  {
    this.types = value;
  }
  
  public ForcedType withName(String value)
  {
    setName(value);
    return this;
  }
  
  public ForcedType withExpression(String value)
  {
    setExpression(value);
    return this;
  }
  
  public ForcedType withExpressions(String value)
  {
    setExpressions(value);
    return this;
  }
  
  public ForcedType withTypes(String value)
  {
    setTypes(value);
    return this;
  }
}
