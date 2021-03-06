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
import gemlite.core.internal.support.hotdeploy.DeployParameter;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.internal.support.jpa.files.service.JarFileService;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.FunctionUtil;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.gemstone.gemfire.cache.CacheClosedException;

@Controller
@RequestMapping("/jars/page")
public class JarFilePageUpload
{
    @RequestMapping(value = "/jar_upload", method = RequestMethod.GET)
    public ModelAndView handleUploadShow()
    {
      return new ModelAndView("tools/jar_upload");
    }
    
    @RequestMapping(value = "/jar_upload", method = RequestMethod.POST)
    public ModelAndView handleUploadProcess(@RequestParam("imageFile") MultipartFile file,@RequestParam("desc") String desc)
        throws Exception
    {
      JarFileService service= JpaContext.getService(JarFileService.class);
      String fname = file.getOriginalFilename();
      ModelAndView view = new ModelAndView("tools/jar_upload");
      if(StringUtils.isEmpty(fname))
      {
          view.addObject("msg","�	�
 .");
          return view;
      }
      ReleasedJarFile jarFile = new ReleasedJarFile();
      jarFile.setFileName(fname);
      jarFile.setContent(file.getBytes());
      jarFile.setMd5_str(Util.makeMD5String(file.getBytes()));
      jarFile.setUpdate_count(1);
      jarFile.setUpload_time(new Date());
      jarFile.setDescription(desc);
      //�H,�
      service.splitVersion(jarFile, fname);
      //��/&	�
      if (!service.checkDuplicate(jarFile))
      {
         
          view.addObject("msg",jarFile.getFileName() + " �	�,� �
 .");
          return view;
      }
      //�X
      service.save(jarFile);
      
      //�,�!W�!W{��Xwe,v��SM ��;H,
      String config_server = ServerConfigHelper.getProperty("CONFIG_SERVER");
      URL url=Util.makeFullURL(config_server, "/jars/file/"+jarFile.getFileId());
      ReleasedJarFile ff = service.deploy(url);
      jarFile.setModuleName(ff.getModuleName());
      jarFile.setModuleType(ff.getModuleType());
      service.save(jarFile);
      
      try
      {
          DeployParameter param = new DeployParameter(jarFile.getModuleName(),jarFile.getModuleType(),jarFile.getContent());
          Object result = FunctionUtil.deploy(param);
          LogUtil.getCoreLog().info("deploy->" + result);
      }
      catch(CacheClosedException ce)
      {
          view.addObject("msg","�1%,���/&ޥ0Ƥ!");
          return view;
      }
      catch(Exception e)
      {
          view.addObject("msg","�1%."+e.getMessage());
          return view;
      }
      
      
      LogUtil.getCoreLog().info(
              "Upload jar file:" + fname + " md5:" + jarFile.getMd5_str() + " module:" + jarFile.getModuleName()
                  + " version:" + jarFile.getVersion());
      return new ModelAndView("redirect:/jars/page/jar_list");
    }
}
