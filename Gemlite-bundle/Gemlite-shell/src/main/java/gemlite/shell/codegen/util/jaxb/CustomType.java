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
@XmlType(name = "CustomType", propOrder = {})
@SuppressWarnings({ "all" })
public class CustomType implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String name;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String type;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String converter;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String binding;
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String value)
  {
    this.name = value;
  }
  
  public String getType()
  {
    return type;
  }
  
  public void setType(String value)
  {
    this.type = value;
  }
  
  public String getConverter()
  {
    return converter;
  }
  
  public void setConverter(String value)
  {
    this.converter = value;
  }
  
  public String getBinding()
  {
    return binding;
  }
  
  public void setBinding(String value)
  {
    this.binding = value;
  }
  
  public CustomType withName(String value)
  {
    setName(value);
    return this;
  }
  
  public CustomType withType(String value)
  {
    setType(value);
    return this;
  }
  
  public CustomType withConverter(String value)
  {
    setConverter(value);
    return this;
  }
  
  public CustomType withBinding(String value)
  {
    setBinding(value);
    return this;
  }
}
