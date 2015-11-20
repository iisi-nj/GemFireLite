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

import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.internal.support.jpa.files.service.JarFileService;
import gemlite.core.util.LogUtil;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/jars/file")
public class JarFileDownload
{
  
  @RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> getJarContent(@PathVariable String fileId)
  {
    JarFileService service = JpaContext.getService(JarFileService.class);
    byte[] bytes = null;
    try
    {
      long id = NumberUtils.toLong(fileId);
      ReleasedJarFile jarFile = service.getFileById(id);
      LogUtil.getCoreLog().info("FileId:" + fileId + " name:" + jarFile.getFileName() );
      bytes = jarFile.getContent();
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      headers.setContentDispositionFormData("attachment", jarFile.getFileName() );
      return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("", e);
    }
    return new ResponseEntity<byte[]>(null, null, HttpStatus.CREATED);
  }
  
}
