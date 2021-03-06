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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gemlite.core.internal.jmx.manage.KeyConstants.Regions;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.commands.IndexCommand;
import gemlite.shell.commands.admin.RegionCommand;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/tools")
public class IndexController
{
   @Autowired
   private IndexCommand ic;
   
   @Autowired
   private RegionCommand rc;
   
   @SuppressWarnings("unchecked")
   @RequestMapping(value="/index",method=RequestMethod.GET)
   public ModelAndView listIndex()
   {
       ModelAndView modelAndView = new ModelAndView("tools/index");
       //��@	region
       rc.listRegionDetails();
       List<HashMap<String,Object>> regions = (List<HashMap<String,Object>>)rc.get(CommandMeta.LIST_REGIONS);
       //d ���,�:list regions��Zǒ�
       List<HashMap<String,List<String>>> indexs = new ArrayList<HashMap<String,List<String>>>();
       //M�region�indexpn
       if(regions != null)
       for(HashMap<String, Object> region:regions)
       {
           String regionName = (String)region.get(Regions.regionName.name());
           ic.list(true,regionName);
           List<String> list = (List<String>)ic.get(CommandMeta.LIST_INDEX);
           if(list !=null && list.size()>0)
           {
               HashMap<String, List<String>> map = new HashMap<String, List<String>>();
               map.put(regionName, list);
               indexs.add(map);
           }
       }
       
       List<HashMap<String,List<String>>> left = new ArrayList<HashMap<String,List<String>>>();
       List<HashMap<String,List<String>>> right = new ArrayList<HashMap<String,List<String>>>();
       for(int i=0;i<indexs.size();i++)
       {
           HashMap<String,List<String>> item = indexs.get(i);
           if(i%2==0)
           left.add(item);
           else
               right.add(item);
       }
       modelAndView.addObject("left",left);
       modelAndView.addObject("right",right);
       
       return modelAndView;
   }
   
   
   @SuppressWarnings("unchecked")
   @RequestMapping(value="/index/describe/{indexName}",method=RequestMethod.GET)
   @ResponseBody
   public List<HashMap<String,Object>> describeIndex(@PathVariable String indexName)
   {
       //�� 
       indexName = StringUtils.replace(indexName, "(Hash Index)", "");
       //�indexpn
       ic.describeIndex(true, indexName);
       List<HashMap<String,Object>> list = (List<HashMap<String,Object>>)ic.get(CommandMeta.DESCRIBE_INDEX);
       return list;
   }
   
   @RequestMapping(value="/index/create",method=RequestMethod.POST)
   @ResponseBody
   public String createIndex(HttpServletRequest request)
   {
       String clause = request.getParameter("clause");
       //�indexpn
       return ic.create(true, clause);
   }
   
   @RequestMapping(value="/index/drop",method=RequestMethod.POST)
   @ResponseBody
   public String dropIndex(HttpServletRequest request)
   {
       String indexName = request.getParameter("indexName");
       //�indexpn
       return ic.drop(true, indexName);
   }
}
