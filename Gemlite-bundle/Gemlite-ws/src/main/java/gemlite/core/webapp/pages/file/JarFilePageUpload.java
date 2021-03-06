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
import gemlite.core.util.FunctionUtil;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
    public ModelAndView handleUploadShow(HttpServletRequest request)
    {
//      String request_url = request.getRequestURL().toString();
//      String request_uri = request.getRequestURI();
//      String context = request.getContextPath();
//      String real_url = StringUtils.replace(request_url, request_uri, "");
//      real_url = real_url+context;
//      System.err.println("request_url:"+request_url);
//      System.err.println("request_uri:"+request_uri);
//      System.err.println("context"+context);
//      System.err.println("real_url:"+real_url);
      return new ModelAndView("tools/jar_upload");
    }
    
    @RequestMapping(value = "/jar_upload", method = RequestMethod.POST)
    public ModelAndView handleUploadProcess(@RequestParam("imageFile") MultipartFile file,@RequestParam("desc") String desc,HttpServletRequest request)
        throws Exception
    {
      String request_url = request.getRequestURL().toString();
      String request_uri = request.getRequestURI();
      String context = request.getContextPath();
      String real_url = StringUtils.replace(request_url, request_uri, "");
      real_url = real_url+context;
      JarFileService service= JpaContext.getService(JarFileService.class);
      String fname = file.getOriginalFilename();
      ModelAndView view = new ModelAndView("tools/jar_upload");
      if(StringUtils.isEmpty(fname))
      {
          view.addObject("msg","请选择架包上传.");
          return view;
      }
      ReleasedJarFile jarFile = new ReleasedJarFile();
      jarFile.setFileName(fname);
      jarFile.setContent(file.getBytes());
      jarFile.setMd5_str(Util.makeMD5String(file.getBytes()));
      jarFile.setUpdate_count(1);
      jarFile.setUpload_time(new Date());
      jarFile.setDescription(desc);
      //分析版本名称
      service.splitVersion(jarFile, fname);
      //较验是否有变化
//      if (!service.checkDuplicate(jarFile))
//      {
//         
//          view.addObject("msg",jarFile.getFileName() + " 没有变化,无需重复上传.");
//          return view;
//      }
      //保存
      service.save(jarFile);
      
      //发布,成功后将模块名及模块类型保存起来,并且更新当前最新激活版本
      URL url=Util.makeFullURL(real_url, "/jars/file/"+jarFile.getFileId());
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
          view.addObject("msg","发布失败,请检查是否连接到集群!");
          return view;
      }
      catch(Exception e)
      {
          view.addObject("msg","发布失败."+e.getMessage());
          return view;
      }
      
      
      LogUtil.getCoreLog().info(
              "Upload jar file:" + fname + " md5:" + jarFile.getMd5_str() + " module:" + jarFile.getModuleName()
                  + " version:" + jarFile.getVersion());
      return new ModelAndView("redirect:/jars/page/jar_list");
    }
}
