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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gemlite.shell.codegen.util.jaxb.Generator;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@SuppressWarnings({ "all" })
@XmlRootElement(name = "configuration")
public class Configuration implements Serializable
{
  protected Jdbc jdbc;
  @XmlElement(required = true)
  protected Generator generator;
  
  public Jdbc getJdbc()
  {
    return jdbc;
  }
  
  public void setJdbc(Jdbc value)
  {
    this.jdbc = value;
  }
  
  public Generator getGenerator()
  {
    return generator;
  }
  
  public void setGenerator(Generator value)
  {
    this.generator = value;
  }
  
  public Configuration withJdbc(Jdbc value)
  {
    setJdbc(value);
    return this;
  }
  
  public Configuration withGenerator(Generator value)
  {
    setGenerator(value);
    return this;
  }
}
