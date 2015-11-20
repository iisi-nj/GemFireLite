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
@XmlType(name = "Schema", propOrder = {})
@SuppressWarnings({ "all" })
public class Schema implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(required = true, defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String inputSchema = "";
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String outputSchema;
  @XmlElement(defaultValue = "false")
  protected Boolean outputSchemaToDefault = false;
  
  public String getInputSchema()
  {
    return inputSchema;
  }
  
  public void setInputSchema(String value)
  {
    this.inputSchema = value;
  }
  
  public String getOutputSchema()
  {
    return outputSchema;
  }
  
  public void setOutputSchema(String value)
  {
    this.outputSchema = value;
  }
  
  public Boolean isOutputSchemaToDefault()
  {
    return outputSchemaToDefault;
  }
  
  public void setOutputSchemaToDefault(Boolean value)
  {
    this.outputSchemaToDefault = value;
  }
  
  public Schema withInputSchema(String value)
  {
    setInputSchema(value);
    return this;
  }
  
  public Schema withOutputSchema(String value)
  {
    setOutputSchema(value);
    return this;
  }
  
  public Schema withOutputSchemaToDefault(Boolean value)
  {
    setOutputSchemaToDefault(value);
    return this;
  }
}
