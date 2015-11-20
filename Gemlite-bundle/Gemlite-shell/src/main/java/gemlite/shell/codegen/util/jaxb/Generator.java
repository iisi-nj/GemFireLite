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
@XmlType(name = "Generator", propOrder = {})
@SuppressWarnings({ "all" })
public class Generator implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(defaultValue = "gemlite.shell.codegen.util.JavaGenerator")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String name = "gemlite.shell.codegen.util.JavaGenerator";
  protected Strategy strategy;
  @XmlElement(required = true)
  protected Database database;
  protected Generate generate;
  protected Target target;
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String value)
  {
    this.name = value;
  }
  
  public Strategy getStrategy()
  {
    return strategy;
  }
  
  public void setStrategy(Strategy value)
  {
    this.strategy = value;
  }
  
  public Database getDatabase()
  {
    return database;
  }
  
  public void setDatabase(Database value)
  {
    this.database = value;
  }
  
  public Generate getGenerate()
  {
    return generate;
  }
  
  public void setGenerate(Generate value)
  {
    this.generate = value;
  }
  
  public Target getTarget()
  {
    return target;
  }
  
  public void setTarget(Target value)
  {
    this.target = value;
  }
  
  public Generator withName(String value)
  {
    setName(value);
    return this;
  }
  
  public Generator withStrategy(Strategy value)
  {
    setStrategy(value);
    return this;
  }
  
  public Generator withDatabase(Database value)
  {
    setDatabase(value);
    return this;
  }
  
  public Generator withGenerate(Generate value)
  {
    setGenerate(value);
    return this;
  }
  
  public Generator withTarget(Target value)
  {
    setTarget(value);
    return this;
  }
}
