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

import gemlite.core.internal.jmx.manage.KeyConstants.Regions;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.commands.admin.Async;
import gemlite.shell.commands.admin.RegionCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings({"unchecked"})
@Controller
@RequestMapping("/tools")
public class AsyncController
{
    @Autowired
    private Async async;
    @Autowired
    private RegionCommand rc;
    
    @RequestMapping(value = "/async", method = RequestMethod.GET)
    public ModelAndView listAsync(Locale locale, ModelAndView modelAndView)
    {
      modelAndView = new ModelAndView("monitor/async");
//      async.listSync();
//      List<HashMap<String,Object>> list = (List<HashMap<String,Object>>)async.get(CommandMeta.LIST_ASYNCQUEUE);
//      //按queueid排序
//      if(list!=null)
//      Collections.sort(list,new Comparator<HashMap<String,Object>>()
//      {
//         public int compare(HashMap<String,Object> o1,HashMap<String,Object> o2)
//          {
//              return ((String)o1.get("queueid")).compareTo((String)o2.get("queueid"));
//          }
//      });
//      modelAndView.addObject("list",list);      
      return modelAndView;
    }
    
    @RequestMapping(value = "/async/api", method = RequestMethod.GET)
    @ResponseBody
    public List<HashMap<String,Object>> listAsyncApi(Locale locale, ModelAndView modelAndView)
    {
      async.listSync();
      List<HashMap<String,Object>> list = (List<HashMap<String,Object>>)async.get(CommandMeta.LIST_ASYNCQUEUE);
      //按queueid排序
      if(list!=null)
      Collections.sort(list,new Comparator<HashMap<String,Object>>()
      {
         public int compare(HashMap<String,Object> o1,HashMap<String,Object> o2)
          {
              return ((String)o1.get("queueid")).compareTo((String)o2.get("queueid"));
          }
      });
      return list;
    }
    
    
    @RequestMapping(value = "/async/add", method = RequestMethod.GET)
    public ModelAndView addAsync(HttpServletRequest request)
    {
      ModelAndView modelAndView = new ModelAndView("monitor/async_add");
      rc.listRegionDetails();
      List<HashMap<String,Object>> set = (List<HashMap<String,Object>>)rc.get(CommandMeta.LIST_REGIONS);
      List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
      String regionName = request.getParameter("regionName");
      for(HashMap<String, Object> map:set)
      {
          HashMap<String, String> item = new HashMap<String, String>();
          String name = (String)map.get(Regions.regionName.name());
          item.put("name", name);
          if(StringUtils.equals(regionName, name))
          {
              item.put("selected", "selected=\"selected\"");
          }
          list.add(item);
      }
      
      modelAndView.addObject("list",list);      
      return modelAndView;
    }
    
    @RequestMapping(value = "/async/add", method = RequestMethod.POST)
    @ResponseBody
    public boolean saveAsync(HttpServletRequest request)
    {
      String region = (String)request.getParameter("region");
      String queueId = (String)request.getParameter("queueId");
      boolean persistent = StringUtils.equals((String)request.getParameter("persistent"),"on");
//      boolean parallel = StringUtils.equals((String)request.getParameter("parallel"),"on");
      String diskStoreName = (String)request.getParameter("diskstorename");
      String driver = (String)request.getParameter("driver");
      String url = (String)request.getParameter("url");
      String user = (String)request.getParameter("user");
      String password = (String)request.getParameter("password");
      String rs = async.addAsync(region, queueId, persistent, diskStoreName, driver, url, user, password);
      if(StringUtils.contains(rs, "successfully"))
      return true;
      return false;
    }
}
