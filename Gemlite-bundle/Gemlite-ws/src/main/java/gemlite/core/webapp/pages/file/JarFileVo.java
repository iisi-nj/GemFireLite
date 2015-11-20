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
package gemlite.core.webapp.pages.file;

import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;

import java.io.Serializable;
import java.util.Date;

public class JarFileVo implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1380691496910853099L;
  private ReleasedJarFile domain;
  private boolean used;
  

  public JarFileVo(ReleasedJarFile domain)
  {
    super();
    this.domain = domain;
    this.used = false;
  }

  public String getName()
  {
    return domain.getFileName();
  }

  public String getVersion()
  {
    return domain.getVersion();
  }

  public byte[] getContent()
  {
    return domain.getContent();
  }

  public Date getUpload_time()
  {
    return domain.getUpload_time();
  }

  public String getMd5_str()
  {
    return domain.getMd5_str();
  }

  public String getType()
  {
    return domain.getModuleType().name();
  }

  public long getId()
  {
    return domain.getFileId();
  }

  public String getDesc()
  {
    return domain.getDescription();
  }

  public int getUpdate_count()
  {
    return domain.getUpdate_count();
  }

  public boolean isUsed()
  {
    return used;
  }

  public void setUsed(boolean used)
  {
    this.used = used;
  }
}
