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
@XmlType(name = "RenderMapping", propOrder = {})
@SuppressWarnings({ "all" })
public class RenderMapping extends SettingsBase implements Serializable, Cloneable
{
  private final static long serialVersionUID = 360L;
  protected String defaultSchema;
  @XmlElementWrapper(name = "schemata")
  @XmlElement(name = "schema")
  protected List<MappedSchema> schemata;
  
  public String getDefaultSchema()
  {
    return defaultSchema;
  }
  
  public void setDefaultSchema(String value)
  {
    this.defaultSchema = value;
  }
  
  public List<MappedSchema> getSchemata()
  {
    if (schemata == null)
    {
      schemata = new ArrayList<MappedSchema>();
    }
    return schemata;
  }
  
  public void setSchemata(List<MappedSchema> schemata)
  {
    this.schemata = schemata;
  }
  
  public RenderMapping withDefaultSchema(String value)
  {
    setDefaultSchema(value);
    return this;
  }
  
  public RenderMapping withSchemata(MappedSchema... values)
  {
    if (values != null)
    {
      for (MappedSchema value : values)
      {
        getSchemata().add(value);
      }
    }
    return this;
  }
  
  public RenderMapping withSchemata(Collection<MappedSchema> values)
  {
    if (values != null)
    {
      getSchemata().addAll(values);
    }
    return this;
  }
  
  public RenderMapping withSchemata(List<MappedSchema> schemata)
  {
    setSchemata(schemata);
    return this;
  }
}
