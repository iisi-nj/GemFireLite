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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/***
 * @author ynd
 *
 */
@Entity
public class ReleasedJarFile implements Serializable
{
  private static final long serialVersionUID = -1625906988583920519L;
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private long fileId;
  private String moduleName;
  private ModuleType moduleType;
  
  private String fileName;
  private String version;
  @Column(columnDefinition="BINARY")
  private byte[] content;
  private Date upload_time;
  private String md5_str;
  private String description;
  private int update_count;
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
  public String getFileName()
  {
    return fileName;
  }
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  public byte[] getContent()
  {
    return content;
  }
  public void setContent(byte[] content)
  {
    this.content = content;
  }
  public Date getUpload_time()
  {
    return upload_time;
  }
  public void setUpload_time(Date upload_time)
  {
    this.upload_time = upload_time;
  }
  public String getMd5_str()
  {
    return md5_str;
  }
  public void setMd5_str(String md5_str)
  {
    this.md5_str = md5_str;
  }
  public String getDescription()
  {
    return description;
  }
  public void setDescription(String desc)
  {
    this.description = desc;
  }
  public int getUpdate_count()
  {
    return update_count;
  }
  public void setUpdate_count(int update_count)
  {
    this.update_count = update_count;
  }
  public String getVersion()
  {
    return version;
  }
  public void setVersion(String version)
  {
    this.version = version;
  }
@Override
public String toString()
{
    return "ReleasedJarFile [fileId=" + fileId + ", moduleName=" + moduleName + ", moduleType=" + moduleType + ", fileName=" + fileName + ", version="
            + version + ", upload_time=" + upload_time + ", md5_str=" + md5_str + ", desc=" + description + ", update_count=" + update_count + "]";
}
}
