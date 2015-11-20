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
package gemlite.core.webapp.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gemlite.core.common.DESUtil;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.domain.DataModelConf;
import gemlite.core.internal.support.jpa.files.service.DataModelService;
import gemlite.shell.commands.admin.Domain;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/tools")
public class DataModelController
{
  @Autowired
  private Domain command;
  
  @RequestMapping(value = "/model", method = RequestMethod.GET)
  public ModelAndView model()
  {
    ModelAndView modelAndView = new ModelAndView("tools/data_model");
    
    DataModelService service = JpaContext.getService(DataModelService.class);
    List<DataModelConf> confList = service.findAll();
    if (confList != null && confList.size() > 0)
    {
      DataModelConf conf = confList.get(0);
      String pwd = conf.getPwd();
      conf.setPwd(DESUtil.decrypt(pwd));
      HashMap<String, String> map = getValueMap(conf);
      modelAndView.addAllObjects(map);
    }
    return modelAndView;
  }
  
  @RequestMapping(value = "/model", method = RequestMethod.POST)
  @ResponseBody
  public ModelAndView model(HttpServletRequest request, ModelAndView modelAndView)
  {
    modelAndView = new ModelAndView("tools/data_model");
    String driver = StringUtils.trim((String) request.getParameter("driver"));
    String url = StringUtils.trim((String) request.getParameter("url"));
    String user = StringUtils.trim((String) request.getParameter("user"));
    String pwd = StringUtils.trim((String) request.getParameter("pwd"));
    String name = StringUtils.trim((String) request.getParameter("name"));
    String inputSchema = StringUtils.trim((String) request.getParameter("inputSchema"));
    String includes = StringUtils.trim((String) request.getParameter("includes"));
    String excludes = StringUtils.trim((String) request.getParameter("excludes"));
    String packageName = StringUtils.trim((String) request.getParameter("packageName"));
    String directory = StringUtils.trim((String) request.getParameter("directory"));
    
    DataModelConf conf = new DataModelConf(driver, url, user, pwd, name, inputSchema, includes, excludes, packageName,
        directory);
    try
    {
      HashMap<String, String> map = getValueMap(conf);
      modelAndView.addAllObjects(map);
      command.webExecute(driver, url, user, pwd, name, inputSchema, includes, excludes, packageName, directory);
      DataModelService service = JpaContext.getService(DataModelService.class);
      service.deleteAll();
      service.save(conf);
      modelAndView.addObject("msg", "Successed!");
    }
    catch (Exception e)
    {
      modelAndView.addObject("msg", "Failed! Please check the configuration. " + e);
    }
    
    return modelAndView;
  }
  
  private HashMap<String, String> getValueMap(DataModelConf conf)
  {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("driver", conf.getDriver());
    map.put("url", conf.getUrl());
    map.put("user", conf.getUser());
    map.put("pwd", conf.getPwd());
    map.put("name", conf.getName());
    map.put("inputSchema", conf.getInputSchema());
    map.put("includes", conf.getIncludes());
    map.put("excludes", conf.getExcludes());
    map.put("packageName", conf.getPackageName());
    map.put("directory", conf.getDirectory());
    return map;
  }
}
