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

import gemlite.core.internal.support.annotations.ModuleType;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ActiveFileId implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -4421962918018913806L;
  
  private long fileId;
  @Id
  private String moduleName;
  private ModuleType moduleType;
  
  public long getFileId()
  {
    return fileId;
  }
  
  public void setFileId(long fileId)
  {
    this.fileId = fileId;
  }
  
  public String getModuleName()
  {
    return moduleName;
  }
  
  public void setModuleName(String moduleName)
  {
    this.moduleName = moduleName;
  }
  
  public ModuleType getModuleType()
  {
    return moduleType;
  }
  
  public void setModuleType(ModuleType moduleType)
  {
    this.moduleType = moduleType;
  }
  
}
