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
@XmlType(name = "Target", propOrder = {})
@SuppressWarnings({ "all" })
public class Target implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(defaultValue = "gemlite.shell.codegen.generated")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String packageName = "gemlite.shell.codegen.generated";
  @XmlElement(required = true, defaultValue = "target/generated-sources/gemlite")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String directory = "target/generated-sources/gemlite";
  
  public String getPackageName()
  {
    return packageName;
  }
  
  public void setPackageName(String value)
  {
    this.packageName = value;
  }
  
  public String getDirectory()
  {
    return directory;
  }
  
  public void setDirectory(String value)
  {
    this.directory = value;
  }
  
  public Target withPackageName(String value)
  {
    setPackageName(value);
    return this;
  }
  
  public Target withDirectory(String value)
  {
    setDirectory(value);
    return this;
  }
}
