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
@XmlType(name = "MatchersFieldType", propOrder = {})
@SuppressWarnings({ "all" })
public class MatchersFieldType implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String expression;
  protected MatcherRule fieldIdentifier;
  protected MatcherRule fieldMember;
  protected MatcherRule fieldSetter;
  protected MatcherRule fieldGetter;
  
  public String getExpression()
  {
    return expression;
  }
  
  public void setExpression(String value)
  {
    this.expression = value;
  }
  
  public MatcherRule getFieldIdentifier()
  {
    return fieldIdentifier;
  }
  
  public void setFieldIdentifier(MatcherRule value)
  {
    this.fieldIdentifier = value;
  }
  
  public MatcherRule getFieldMember()
  {
    return fieldMember;
  }
  
  public void setFieldMember(MatcherRule value)
  {
    this.fieldMember = value;
  }
  
  public MatcherRule getFieldSetter()
  {
    return fieldSetter;
  }
  
  public void setFieldSetter(MatcherRule value)
  {
    this.fieldSetter = value;
  }
  
  public MatcherRule getFieldGetter()
  {
    return fieldGetter;
  }
  
  public void setFieldGetter(MatcherRule value)
  {
    this.fieldGetter = value;
  }
  
  public MatchersFieldType withExpression(String value)
  {
    setExpression(value);
    return this;
  }
  
  public MatchersFieldType withFieldIdentifier(MatcherRule value)
  {
    setFieldIdentifier(value);
    return this;
  }
  
  public MatchersFieldType withFieldMember(MatcherRule value)
  {
    setFieldMember(value);
    return this;
  }
  
  public MatchersFieldType withFieldSetter(MatcherRule value)
  {
    setFieldSetter(value);
    return this;
  }
  
  public MatchersFieldType withFieldGetter(MatcherRule value)
  {
    setFieldGetter(value);
    return this;
  }
}
