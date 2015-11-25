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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import gemlite.core.common.DateUtil;
import gemlite.core.internal.admin.measurement.ScannedMethodItem;
import gemlite.core.internal.measurement.RemoteServiceStatItem;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.domain.GmCheckPoints;
import gemlite.core.internal.support.jpa.files.service.CheckPointsService;
import gemlite.core.util.LogUtil;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.commands.Query;
import gemlite.shell.commands.admin.Monitor;

@SuppressWarnings({"unchecked"})
@Controller
public class ServiceController
{
  @Autowired
  private Monitor monitor;
  
  @Autowired
  private Query query;
  
  private ObjectMapper objectMapper = null;
  
  public ServiceController() {
      objectMapper = new ObjectMapper();
  }
  
  
  @RequestMapping(value = "/service", method = RequestMethod.GET)
  public ModelAndView service(Locale locale, ModelAndView modelAndView)
  {
    //调用service
      try
      {
          monitor.services();
          LinkedHashMap<String, HashMap<String, Object>> rs = (LinkedHashMap<String, HashMap<String, Object>>)(monitor.get(CommandMeta.LIST_SERVICES));
          modelAndView = new ModelAndView("monitor/services");
          modelAndView.addObject("list", rs); 
      }
      catch(Exception e)
      {
         LogUtil.getCoreLog().error("Service List error {}",e.getMessage());
          modelAndView = new ModelAndView("monitor/services");
          modelAndView.addObject("list", new ArrayList<HashMap<String,Object>>()); 
          modelAndView.addObject("msg",e.getMessage());
      }
    return modelAndView;
  }
  
  
  @RequestMapping(value = "/service/api", method = RequestMethod.GET)
  public void serviceapi(HttpServletResponse response)
  {
      //调用service
      try
      {
          monitor.services();
          LinkedHashMap<String, HashMap<String, Object>> rs = (LinkedHashMap<String, HashMap<String, Object>>)(monitor.get(CommandMeta.LIST_SERVICES));
          if(rs ==null || rs.size()<=0)
              return;
          //对数据进行json处理
          String[] arr = {"firstAt","lastAt"};
          Iterator<Entry<String, HashMap<String, Object>>> it = rs.entrySet().iterator();
          List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
          while(it.hasNext())
          {
              HashMap<String,Object> data = it.next().getValue();
              for(String key:arr)
              {
                  Long firstAt = NumberUtils.toLong(data.get(key).toString());
                  if(firstAt == null)
                    firstAt = 0L;
                  String str1 = "";
                  if(firstAt!=null && firstAt>0)
                  str1 = DateUtil.format(new Date(firstAt), DateUtil.yyyy_MM_dd_HHmmssSSS);
                  data.put(key, str1);
              }
              list.add(data);
          }
          objectMapper.writeValue(response.getWriter(),list);
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
  }
  
  
  @RequestMapping(value = "/service/recent/api", method = RequestMethod.GET)
  public List<RemoteServiceStatItem> recentHistoryApi(HttpServletRequest request)
  {
      //调用service
      try
      {
          String name = request.getParameter("name");
          String operation = request.getParameter("operation");
          int size = Integer.parseInt(request.getParameter("size"));
          List<RemoteServiceStatItem> list = monitor.getOperation(name,operation, size);
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
      //调用service
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
      //调用service
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
      
      //取到一个list
      List<HashMap<String,Object>> rs = new ArrayList<HashMap<String,Object>>();
      
      //读取所有的已经勾选的数据
      CheckPointsService service = JpaContext.getService(CheckPointsService.class);
      List<GmCheckPoints> cps = service.findByModuleAndServicename(moduleName, serviceName);
      //处理成hashmap
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
      
      //对nodes进行解析
      String[] arr = nodes.split("#");
      CheckPointsService service = JpaContext.getService(CheckPointsService.class);
      
      //根据servicename,module删除所有的数据
      service.deleteByModuleAndServicename(module, servicename);
      
      for(int i=0;i<arr.length;i++)
      {
          String str = arr[i];
          //解析出class,method
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
      
      //重新加载
      monitor.reload(module);
      return true;
  }
  
  @RequestMapping(value = "/service/execute", method = RequestMethod.GET)
  @ResponseBody
  public String execute(HttpServletRequest request)
  {
      //调用service
      try
      {   
          String module = request.getParameter("module");
          String service = request.getParameter("service");
          String type = request.getParameter("type");
          String values = request.getParameter("values");
          query.doQuery(module, service, type, values);
          Object rs = query.get(CommandMeta.QUERY);
          return rs == null?"no result":rs.toString();
      }
      catch(Exception e)
      {
          LogUtil.getCoreLog().error("recent error:",e);
      }
      return "";
  }
}
