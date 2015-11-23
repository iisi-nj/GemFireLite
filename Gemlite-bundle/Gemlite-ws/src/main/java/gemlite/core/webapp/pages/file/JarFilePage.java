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
import gemlite.core.internal.support.jpa.files.domain.ActiveFileId;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.internal.support.jpa.files.service.JarFileService;
import gemlite.core.util.FunctionUtil;
import gemlite.core.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/jars/page")
public class JarFilePage
{
    @RequestMapping(value = "/jar_list", method = RequestMethod.GET)
    public ModelAndView jarlist(Locale locale, ModelAndView modelAndView)
    {
        JarFileService service = JpaContext.getService(JarFileService.class);
        ModelAndView model = new ModelAndView("tools/jar_list");
        List<ReleasedJarFile> list = service.findAll();
        List<JarFileVo> rs = new ArrayList<JarFileVo>();
        //遍历
        for(ReleasedJarFile jar:list)
        {
            JarFileVo vo = new JarFileVo(jar);
            if(StringUtils.isEmpty(jar.getModuleName()))
                continue;
            ActiveFileId activeFile = service.findActiveByName(jar.getModuleName());
            if (activeFile != null && activeFile.getFileId() == jar.getFileId())
            {
                vo.setUsed(true);
            }
            rs.add(vo);
        }
        model.addObject("jarFiles", rs);
        return model;
    }
    
  
  @RequestMapping(value = "/deploy")
  @ResponseBody
  public String deploy(@RequestParam("deployId") long deployId)
  {
    JarFileService service = JpaContext.getService(JarFileService.class);
    
    ReleasedJarFile jarFile = service.getFileById(deployId);
    ActiveFileId activeFile = service.findActiveByName(jarFile.getModuleName());
//    if (activeFile != null && activeFile.getFileId() == jarFile.getFileId())
//      return model;
    if (activeFile == null)
      activeFile = new ActiveFileId();
    activeFile.setFileId(jarFile.getFileId());
    activeFile.setModuleName(jarFile.getModuleName());
    activeFile.setModuleType(jarFile.getModuleType());
    service.updateActiveFileId(activeFile);
    
    DeployParameter param = new DeployParameter(jarFile.getModuleName(),jarFile.getModuleType(),jarFile.getContent());
    Object result = FunctionUtil.deploy(param);
    LogUtil.getCoreLog().info("deploy->" + result);
    //解析result
    if(StringUtils.contains(result.toString(), "success"))
    {
        jarFile.setUpdate_count(jarFile.getUpdate_count()+1);
        service.save(jarFile);
        return "success";
    }
    else
        return result.toString();
  }
  
  @RequestMapping(value = "/del")
  @ResponseBody
  public String del(@RequestParam("id") long id)
  {
    JarFileService service = JpaContext.getService(JarFileService.class);
    
    ReleasedJarFile jarFile = service.getFileById(id);
    ActiveFileId activeFile = service.findActiveByName(jarFile.getModuleName());
    if (activeFile != null)
    {
        //激活状态的不允许删除
        if(activeFile.getFileId() == jarFile.getFileId())
        {
            return "active file del fail!";
        }
    }
    
    service.delete(jarFile);
    
    LogUtil.getCoreLog().info("delete jarFile->" + jarFile.toString());
    //解析result
    return "success";
  }
  
}
