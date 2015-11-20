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
package gemlite.core.webapp.monitor;

import gemlite.core.common.DateUtil;
import gemlite.core.internal.admin.measurement.ScannedMethodItem;
import gemlite.core.internal.measurement.RemoteServiceStatItem;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.core.internal.support.jpa.files.domain.GmCheckPoints;
import gemlite.core.internal.support.jpa.files.service.CheckPointsService;
import gemlite.core.util.LogUtil;
import gemlite.core.webapp.MsgStore;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.commands.admin.Monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings({"unchecked"})
@Controller
public class ServiceController
{
  @Autowired
  private Monitor monitor;
  
  private ObjectMapper objectMapper = null;
  
  public ServiceController() {
      objectMapper = new ObjectMapper();
  }
  
  
  @RequestMapping(value = "/service", method = RequestMethod.GET)
  public ModelAndView service(Locale locale, ModelAndView modelAndView)
  {
    //(service
      try
      {
          monitor.services();
          List<HashMap<String,Object>> rs = (List<HashMap<String,Object>>)(monitor.get(CommandMeta.LIST_SERVICES));
          modelAndView = new ModelAndView("monitor/services");
          Collections.sort(rs, new Comparator<HashMap<String,Object>>() {
              @Override
              public int compare(HashMap<String,Object> o1, HashMap<String,Object> o2) {
                return ((String)o1.get("serviceName")).compareTo((String)o2.get("serviceName"));
              }
            });
          modelAndView.addObject("list", rs); 
      }
      catch(Exception e)
      {
          modelAndView = new ModelAndView("monitor/services");
          modelAndView.addObject("list", new ArrayList<HashMap<String,Object>>()); 
          modelAndView.addObject("msg",MsgStore.needInit);
      }
    return modelAndView;
  }
  
  
  @RequestMapping(value = "/service/api", method = RequestMethod.GET)
  public void serviceapi(HttpServletResponse response)
  {
      //(service
      try
      {
          monitor.services();
          List<HashMap<String,Object>> rs = (List<HashMap<String,Object>>)(monitor.get(CommandMeta.LIST_SERVICES));
          if(rs ==null || rs.size()<=0)
              return;
          //˘ˆÙ€L<
          String[] arr = {"firstAt","lastAt"};
          for(HashMap<String,Object> data:rs)
          {
              for(String key:arr)
              {
                  Long firstAt = 0L;
                  try
                  {
                      firstAt = (Long)data.get(key);
                  }
                  catch(Exception e)
                  {
                      LogUtil.getCoreLog().error("firstAt is null:"+data.toString());
                  }
                  String str1 = "";
                  if(firstAt!=null && firstAt>0)
                  str1 = DateUtil.format(new Date(firstAt), DateUtil.yyyy_MM_dd_HHmmssSSS);
                  data.put(key, str1);
              }
          }
          
          Collections.sort(rs, new Comparator<HashMap<String,Object>>() {
              @Override
              public int compare(HashMap<String,Object> o1, HashMap<String,Object> o2) {
                  if(o1==null || o1.get("serviceName")==null || o2==null || o2.get("serviceName")==null)
                  {
                      LogUtil.getCoreLog().info("services has null value:{}{}",o1,o2);
                      return 0;
                  }
                return ((String)o1.get("serviceName")).compareTo((String)o2.get("serviceName"));
              }
            });
          
          objectMapper.writeValue(response.getWriter(),rs);
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
  }
  
  
  @RequestMapping(value = "/service/recent/api", method = RequestMethod.GET)
  public List<RemoteServiceStatItem> recentHistoryApi(HttpServletRequest request)
  {
      //(service
      try
      {
          String name = request.getParameter("name");
          String operation = request.getParameter("operation");
          int size = Integer.parseInt(request.getParameter("size"));
          String oname = MBeanHelper.createMBeanName(name);
          List<RemoteServiceStatItem> list = monitor.getOperation(oname, operation, size);
          return list;
      }
      catch(Exception e)
      {
          LogUtil.getCoreLog().error("recent error:",e);
      }
      return null;
  }
  
  
  @RequestMapping(value = "/service/recent", method = RequestMethod.GET)
  public ModelAndView recentHistory(HttpServletRequest request)
  {
      ModelAndView modelAndView = new ModelAndView("monitor/recent_history");
      //(service
      try
      {
          List<RemoteServiceStatItem> list = recentHistoryApi(request);
          modelAndView.addObject("list",list);
      }
      catch(Exception e)
      {
          LogUtil.getCoreLog().error("recent error:",e);
      }
      return modelAndView;
  }
  
  @RequestMapping(value = "/service/max", method = RequestMethod.GET)
  public ModelAndView maxHistory(HttpServletRequest request)
  {
      ModelAndView modelAndView = new ModelAndView("monitor/max_history");
      //(service
      try
      {
          List<RemoteServiceStatItem> list = recentHistoryApi(request);
          modelAndView.addObject("list",list);
      }
      catch(Exception e)
      {
          LogUtil.getCoreLog().error("max error:",e);
      }
      return modelAndView;
  }
  
  
  @RequestMapping(value = "/service/funclist", method = RequestMethod.GET)
  @ResponseBody
  public List<HashMap<String,Object>> functionList(HttpServletRequest request)
  {
      String serviceName = request.getParameter("serviceName");
      String moduleName = request.getParameter("moduleName");
      monitor.checkpoints(moduleName, serviceName);
      
      List<ScannedMethodItem> funcs = (List<ScannedMethodItem>)monitor.get(CommandMeta.LIST_CHECKPOINTS);
      
      //÷0 *list
      List<HashMap<String,Object>> rs = new ArrayList<HashMap<String,Object>>();
      
      //˚÷@	ÑÚœ˛	Ñpn
      CheckPointsService service = JpaContext.getService(CheckPointsService.class);
      List<GmCheckPoints> cps = service.findByModuleAndServicename(moduleName, serviceName);
      //hashmap
      Set<String> set = new HashSet<String>();
      if(cps!=null)
      {
          for(GmCheckPoints cp:cps)
          {
              set.add(cp.getClassname()+cp.getMethod()+cp.getDesc());
          }
      }
      
      for(int i=0;i<funcs.size();i++)
      {
          ScannedMethodItem item = funcs.get(i);
          processItem(rs,item,serviceName,moduleName,"",set);
      }
      return rs;
  }
  
  private void processItem(List<HashMap<String,Object>> rs,ScannedMethodItem item,String serviceName,String moduleName,String parentId,Set<String> set)
  {
      String id = item.getClassName()+item.getMethodName();
      HashMap<String,Object> map = new HashMap<String, Object>();
      map.put("id", id);
      map.put("pId", parentId);
      map.put("name", item.getMethodName()+item.getMethodDesc());
      if(StringUtils.isEmpty(parentId))
      map.put("isParent", true);
      map.put("open", true);
      map.put("chkDisabled", false);
      map.put("serviceName", serviceName);
      map.put("moduleName", moduleName);
      map.put("class", item.getClassName());
      map.put("method", item.getMethodName());
      map.put("desc", item.getMethodDesc());
      if(set.contains(item.getClassName()+item.getMethodName()+item.getMethodDesc()))
          map.put("checked", true);
      rs.add(map);
      
      if(item.hasChildren())
      {
          List<ScannedMethodItem> children = item.getChildren();
          for(ScannedMethodItem child:children)
          processItem(rs, child, serviceName, moduleName, id,set);
      }
  }
  
  
  @RequestMapping(value = "/service/funcsave", method = RequestMethod.POST)
  @ResponseBody
  public boolean functionSave(HttpServletRequest request)
  {
      String nodes = request.getParameter("nodes");
      if(StringUtils.isEmpty(nodes))
          return false;
      String servicename = request.getParameter("serviceName");
      String module = request.getParameter("moduleName");
      
      //˘nodes€L„ê
      String[] arr = nodes.split("#");
      CheckPointsService service = JpaContext.getService(CheckPointsService.class);
      
      //9nservicename,module d@	Ñpn
      service.deleteByModuleAndServicename(module, servicename);
      
      for(int i=0;i<arr.length;i++)
      {
          String str = arr[i];
          //„ê˙class,method
          String[] arr1 = str.split(",");
          if(arr1.length != 3)
              continue;
          String classname = arr1[0];
          String method = arr1[1];
          String desc = arr1[2];
          GmCheckPoints cp = new GmCheckPoints();
          cp.setClassname(classname);
          cp.setMethod(method);
          cp.setModule(module);
          cp.setServicename(servicename);
          cp.setDesc(desc);
          service.save(cp);
      }
      
      //Õ∞†}
      monitor.reload(module);
      return true;
  }
}
