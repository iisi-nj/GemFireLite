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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Jdbc", propOrder = {})
@SuppressWarnings({ "all" })
public class Jdbc implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String driver;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String url;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String schema;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String user;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String password;
  @XmlElementWrapper(name = "properties")
  @XmlElement(name = "property")
  protected List<Property> properties;
  
  public String getDriver()
  {
    return driver;
  }
  
  public void setDriver(String value)
  {
    this.driver = value;
  }
  
  public String getUrl()
  {
    return url;
  }
  
  public void setUrl(String value)
  {
    this.url = value;
  }
  
  public String getSchema()
  {
    return schema;
  }
  
  public void setSchema(String value)
  {
    this.schema = value;
  }
  
  public String getUser()
  {
    return user;
  }
  
  public void setUser(String value)
  {
    this.user = value;
  }
  
  public String getPassword()
  {
    return password;
  }
  
  public void setPassword(String value)
  {
    this.password = value;
  }
  
  public List<Property> getProperties()
  {
    if (properties == null)
    {
      properties = new ArrayList<Property>();
    }
    return properties;
  }
  
  public void setProperties(List<Property> properties)
  {
    this.properties = properties;
  }
  
  public Jdbc withDriver(String value)
  {
    setDriver(value);
    return this;
  }
  
  public Jdbc withUrl(String value)
  {
    setUrl(value);
    return this;
  }
  
  public Jdbc withSchema(String value)
  {
    setSchema(value);
    return this;
  }
  
  public Jdbc withUser(String value)
  {
    setUser(value);
    return this;
  }
  
  public Jdbc withPassword(String value)
  {
    setPassword(value);
    return this;
  }
  
  public Jdbc withProperties(Property... values)
  {
    if (values != null)
    {
      for (Property value : values)
      {
        getProperties().add(value);
      }
    }
    return this;
  }
  
  public Jdbc withProperties(Collection<Property> values)
  {
    if (values != null)
    {
      getProperties().addAll(values);
    }
    return this;
  }
  
  public Jdbc withProperties(List<Property> properties)
  {
    setProperties(properties);
    return this;
  }
}
