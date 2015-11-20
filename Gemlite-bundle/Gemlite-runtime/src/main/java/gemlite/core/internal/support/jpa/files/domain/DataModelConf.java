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
@Table(name = "data_model_conf")
public class DataModelConf implements Serializable
{
  private static final long serialVersionUID = -4582272251492351943L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(length = 512)
  private String driver;
  @Column(length = 512)
  private String url;
  @Column(length = 512)
  private String user;
  
  public DataModelConf()
  {
    
  }
  
  public DataModelConf(String driver, String url, String user, String pwd, String name, String inputSchema,
      String includes, String excludes, String packageName, String directory)
  {
    super();
    this.driver = driver;
    this.url = url;
    this.user = user;
    this.pwd = pwd;
    this.name = name;
    this.inputSchema = inputSchema;
    this.includes = includes;
    this.excludes = excludes;
    this.packageName = packageName;
    this.directory = directory;
  }
  
  @Column(length = 512)
  private String pwd;
  @Column(length = 512)
  private String name;
  @Column(length = 512)
  private String inputSchema;
  @Column(length = 1024)
  private String includes;
  @Column(length = 1024)
  private String excludes;
  @Column(length = 512)
  private String packageName;
  @Column(length = 512)
  private String directory;
  
  public String getDriver()
  {
    return driver;
  }
  
  public void setDriver(String driver)
  {
    this.driver = driver;
  }
  
  public String getUrl()
  {
    return url;
  }
  
  public void setUrl(String url)
  {
    this.url = url;
  }
  
  public String getUser()
  {
    return user;
  }
  
  public void setUser(String user)
  {
    this.user = user;
  }
  
  public String getPwd()
  {
    return pwd;
  }
  
  public void setPwd(String pwd)
  {
    this.pwd = pwd;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getInputSchema()
  {
    return inputSchema;
  }
  
  public void setInputSchema(String inputSchema)
  {
    this.inputSchema = inputSchema;
  }
  
  public String getIncludes()
  {
    return includes;
  }
  
  public void setIncludes(String includes)
  {
    this.includes = includes;
  }
  
  public String getExcludes()
  {
    return excludes;
  }
  
  public void setExcludes(String excludes)
  {
    this.excludes = excludes;
  }
  
  public String getPackageName()
  {
    return packageName;
  }
  
  public void setPackageName(String packageName)
  {
    this.packageName = packageName;
  }
  
  public String getDirectory()
  {
    return directory;
  }
  
  public void setDirectory(String directory)
  {
    this.directory = directory;
  }
  
  public long getId()
  {
    return id;
  }
  
  public void setId(long id)
  {
    this.id = id;
  }
}
