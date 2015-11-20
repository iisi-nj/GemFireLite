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
package gemlite.shell.codegen.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MappedSchema", propOrder = {})
@SuppressWarnings({ "all" })
public class MappedSchema extends SettingsBase implements Serializable, Cloneable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(required = true)
  protected String input;
  protected String output;
  @XmlElementWrapper(name = "tables")
  @XmlElement(name = "table")
  protected List<MappedTable> tables;
  
  public String getInput()
  {
    return input;
  }
  
  public void setInput(String value)
  {
    this.input = value;
  }
  
  public String getOutput()
  {
    return output;
  }
  
  public void setOutput(String value)
  {
    this.output = value;
  }
  
  public List<MappedTable> getTables()
  {
    if (tables == null)
    {
      tables = new ArrayList<MappedTable>();
    }
    return tables;
  }
  
  public void setTables(List<MappedTable> tables)
  {
    this.tables = tables;
  }
  
  public MappedSchema withInput(String value)
  {
    setInput(value);
    return this;
  }
  
  public MappedSchema withOutput(String value)
  {
    setOutput(value);
    return this;
  }
  
  public MappedSchema withTables(MappedTable... values)
  {
    if (values != null)
    {
      for (MappedTable value : values)
      {
        getTables().add(value);
      }
    }
    return this;
  }
  
  public MappedSchema withTables(Collection<MappedTable> values)
  {
    if (values != null)
    {
      getTables().addAll(values);
    }
    return this;
  }
  
  public MappedSchema withTables(List<MappedTable> tables)
  {
    setTables(tables);
    return this;
  }
}
