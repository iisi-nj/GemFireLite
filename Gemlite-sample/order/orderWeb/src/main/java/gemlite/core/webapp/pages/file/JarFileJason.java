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
import gemlite.core.internal.support.jpa.files.domain.ActiveFileId;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.internal.support.jpa.files.service.JarFileService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jars/api")
@Transactional
public class JarFileJason
{
  @ResponseBody
  @RequestMapping("/list")
  public List<ReleasedJarFile> list()
  {
    JarFileService service = JpaContext.getService(JarFileService.class);
    List<ReleasedJarFile> list = service.findAll();
    return list;
  }
  
  @RequestMapping("/active")
  @ResponseBody
  public List<ReleasedJarFile> active()
  {
    JarFileService service = JpaContext.getService(JarFileService.class);
    List<ReleasedJarFile> list = service.findActiveFiles();
    return list;
  }
  
  @ResponseBody
  @RequestMapping("/test/{num}")
  public List<RemoteServiceStat> list(@RequestParam(defaultValue = "1") int n)
  {
    List<RemoteServiceStat> list = new ArrayList<>();
    for (int i = 0; i < n; i++)
    {
      RemoteServiceStat st = new RemoteServiceStat();
      list.add(st);
      st.setTps(RandomUtils.nextInt(100));
    }
    return list;
  }
}
