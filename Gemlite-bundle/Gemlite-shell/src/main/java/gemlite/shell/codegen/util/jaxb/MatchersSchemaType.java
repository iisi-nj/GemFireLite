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
@XmlType(name = "MatchersSchemaType", propOrder = {})
@SuppressWarnings({ "all" })
public class MatchersSchemaType implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String expression;
  protected MatcherRule schemaClass;
  protected MatcherRule schemaIdentifier;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String schemaImplements;
  
  public String getExpression()
  {
    return expression;
  }
  
  public void setExpression(String value)
  {
    this.expression = value;
  }
  
  public MatcherRule getSchemaClass()
  {
    return schemaClass;
  }
  
  public void setSchemaClass(MatcherRule value)
  {
    this.schemaClass = value;
  }
  
  public MatcherRule getSchemaIdentifier()
  {
    return schemaIdentifier;
  }
  
  public void setSchemaIdentifier(MatcherRule value)
  {
    this.schemaIdentifier = value;
  }
  
  public String getSchemaImplements()
  {
    return schemaImplements;
  }
  
  public void setSchemaImplements(String value)
  {
    this.schemaImplements = value;
  }
  
  public MatchersSchemaType withExpression(String value)
  {
    setExpression(value);
    return this;
  }
  
  public MatchersSchemaType withSchemaClass(MatcherRule value)
  {
    setSchemaClass(value);
    return this;
  }
  
  public MatchersSchemaType withSchemaIdentifier(MatcherRule value)
  {
    setSchemaIdentifier(value);
    return this;
  }
  
  public MatchersSchemaType withSchemaImplements(String value)
  {
    setSchemaImplements(value);
    return this;
  }
}
