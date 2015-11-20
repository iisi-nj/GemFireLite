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
package gemlite.core.internal.support.jpa.files.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gm_config")
public class GmConfig implements Serializable
{
  private static final long serialVersionUID = 3557398354419969959L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(unique=true,length=32)
  private String key;
  @Column(length=128)
  private String value;
  @Column(length=32)
  private String type;
  
  public GmConfig()
  {
      
  }
  
  public GmConfig(String key, String value, String type)
  {
    super();
    this.key = key;
    this.value = value;
    this.type = type;
  }

public Long getId()
  {
    return id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getKey()
  {
    return key;
  }
  
  public void setKey(String key)
  {
    this.key = key;
  }
  
  public String getValue()
  {
    return value;
  }
  
  public void setValue(String value)
  {
    this.value = value;
  }
  
  public String getType()
  {
    return type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String toString()
  {
    return "GmConfig [id=" + id + ", key=" + key + ", value=" + value + ", type=" + type + "]";
  }
}
