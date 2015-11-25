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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;

import gemlite.core.common.DateUtil;
import gemlite.core.internal.common.JavaTypes;
import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.jmx.manage.KeyConstants.Regions;
import gemlite.core.util.LogUtil;
import gemlite.core.webapp.MsgStore;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.commands.admin.ExecuteSql;
import gemlite.shell.commands.admin.RegionCommand;

@SuppressWarnings({"unchecked","rawtypes"})
@Controller
@RequestMapping("/tools")
public class ToolsController
{
  private static final Logger logger = LogUtil.getCoreLog();
  
  @Autowired
  private RegionCommand rc;
  
  @Autowired
  private ExecuteSql es;
  
  private ObjectMapper objectMapper = null;
  
  public ToolsController() {
      objectMapper = new ObjectMapper();
  }
  
  @RequestMapping(value = "/region", method = RequestMethod.GET)
  public ModelAndView listregion(Locale locale, ModelAndView modelAndView)
  {
    modelAndView = new ModelAndView("tools/region");
    rc.listRegionDetails();
    List<HashMap<String,Object>> list = (List<HashMap<String,Object>>)rc.get(CommandMeta.LIST_REGIONS);
    if(list == null)
        return modelAndView;
    //取两部分,一部分放左边,一部分放右边
    //按regionName排序
    Collections.sort(list, new Comparator<HashMap<String, Object>>()
    {
        @Override
        public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
          return ((String)o1.get("regionName")).compareTo((String)o2.get("regionName"));
        }
    });
    
    List<HashMap<String,Object>> left = new ArrayList<HashMap<String,Object>>();
    List<HashMap<String,Object>> right = new ArrayList<HashMap<String,Object>>();
    for(int i=0;i<list.size();i++)
    {
        HashMap<String,Object> item = list.get(i);
        if(i%2==0)
        left.add(item);
        else
            right.add(item);
    }
    modelAndView.addObject("left",left);
    modelAndView.addObject("right",right);
    return modelAndView;
  }
  
  
  @RequestMapping(value = "/region/update/{regionName}", method = {RequestMethod.POST,RequestMethod.GET})
  public ModelAndView updateregion(@PathVariable String regionName)
  {
    ModelAndView modelAndView = new ModelAndView("tools/region_update");
    rc.describeRegion(regionName);
    HashMap<String,Object> map = (HashMap<String,Object>)(rc.get(CommandMeta.DESCRIBE_REGION));
    
    if(map == null)
    {
        modelAndView.addObject("msg",MsgStore.needInit);
        return modelAndView;
    }
    modelAndView.addObject("region",map);
    
    Map<String,Object> keyMap = new LinkedHashMap<String,Object>();
    Map<String,Object> valueMap = new LinkedHashMap<String,Object>();
    //解析key字段
    String keyClassName = (String)map.get(Regions.keyClass.name());
    String valueClassName = (String)map.get(Regions.valueClass.name());
    try
    {
        Class<?> keyClass = Class.forName(keyClassName);
        //根据key字段取到字段类型
        List<String> keyFields = (List<String>)map.get(Regions.keyFields.name());
        for(String key:keyFields)
        {
            try
            {
                Field fi = keyClass.getDeclaredField(key);
                keyMap.put(key, fi.getType().getName());
            }
            catch(NoSuchFieldException e)
            {
                //如果没有,说明是单key字段
                keyMap.put(key, keyClass.getName());
            }
        }
        modelAndView.addObject("keys",keyMap);
        
        
        Class<?> valueClass = Class.forName(valueClassName);
        //根据key字段取到字段类型
        List<String> valueFields = (List<String>)map.get(Regions.valueFields.name());
        for(String value:valueFields)
        {
            Field fi = valueClass.getDeclaredField(value);
            valueMap.put(value, fi.getType().getName());
        }
        modelAndView.addObject("values",valueMap);
    }
    catch(ClassNotFoundException |NoSuchFieldException e)
    {
      e.printStackTrace();   
    }
    return modelAndView;
  }
  
  
  @RequestMapping(value = "/region/get", method = {RequestMethod.GET,RequestMethod.POST})
  public void getValue(HttpServletRequest request,HttpServletResponse response)
  {
    String regionName = request.getParameter(Regions.regionName.name());
    IMapperTool tool = DomainRegistry.getMapperTool(regionName);    
    //解析key字段
    Class keyClass = tool.getKeyClass();
    List<String> keyFields = tool.getKeyFieldNames();
    try
    {
        //实例化keyClass
        //判断key的类型
        Object key = null;
        if(JavaTypes.contains(keyClass.getName()))
        {
            String keyName = keyFields.iterator().next();
            Object value = request.getParameter(keyName);
            key = JavaTypes.convert(keyClass.getName(), value);
        }
        else
        {
            key = keyClass.newInstance();
            for(String keyName:keyFields)
            {
                Object value = request.getParameter(keyName);
                if(value == null)
                    return ;
                Field field = keyClass.getDeclaredField(keyName);
                field.setAccessible(true);
                field.set(key, JavaTypes.convert(field.getType().getName(), value));
                field.setAccessible(false);
            }
        }
        
        Region r = ClientCacheFactory.getAnyInstance().getRegion(regionName);
        Object value = r.get(key);
        
        objectMapper.writeValue(response.getWriter(), value);
    }
    catch(IllegalAccessException |NoSuchFieldException |InstantiationException |IOException e)
    {
        logger.error("get region:"+regionName,e);
    }
  }
  
  
  @RequestMapping(value = "/region/save", method = {RequestMethod.GET,RequestMethod.POST})
  public void saveValue(HttpServletRequest request,HttpServletResponse response)
  {
    String regionName = request.getParameter(Regions.regionName.name());
    IMapperTool tool = DomainRegistry.getMapperTool(regionName);    
    //解析key字段
    List<String> valueFields = tool.getValueFieldNames();
    Class valueClass = tool.getValueClass();
    try
    {
        //实例化valueClass
        //判断key的类型
        Object value = null;
        if(JavaTypes.contains(valueClass.getName()))
        {
            String keyName = valueFields.iterator().next();
            Object v = request.getParameter(keyName);
            value = JavaTypes.convert(valueClass.getName(), v);
        }
        else
        {
            value = valueClass.newInstance();
            for(String keyName:valueFields)
            {
                Object v = request.getParameter(keyName);
                if(value == null)
                    return ;
                Field field = valueClass.getDeclaredField(keyName);
                field.setAccessible(true);
                field.set(value, JavaTypes.convert(field.getType().getName(), v));
                field.setAccessible(false);
            }
        }
        
        Region r = ClientCacheFactory.getAnyInstance().getRegion(regionName);
        Object key = tool.value2Key(value);
        r.put(key, value);
        
        objectMapper.writeValue(response.getWriter(), true);
    }
    catch(IllegalAccessException |NoSuchFieldException |InstantiationException |IOException e)
    {
        logger.error("save region:"+regionName,e);
    }
  }
  
  @RequestMapping(value = "/region/del", method = {RequestMethod.GET,RequestMethod.POST})
  public void delValue(HttpServletRequest request,HttpServletResponse response)
  {
      String regionName = request.getParameter(Regions.regionName.name());
      IMapperTool tool = DomainRegistry.getMapperTool(regionName);    
      //解析key字段
      Class keyClass = tool.getKeyClass();
      List<String> keyFields = tool.getKeyFieldNames();
      try
      {
          //实例化keyClass
          //判断key的类型
          Object key = null;
          if(JavaTypes.contains(keyClass.getName()))
          {
              String keyName = keyFields.iterator().next();
              Object value = request.getParameter(keyName);
              key = JavaTypes.convert(keyClass.getName(), value);
          }
          else
          {
              key = keyClass.newInstance();
              for(String keyName:keyFields)
              {
                  Object value = request.getParameter(keyName);
                  if(value == null)
                      return ;
                  Field field = keyClass.getDeclaredField(keyName);
                  field.setAccessible(true);
                  field.set(key, JavaTypes.convert(field.getType().getName(), value));
                  field.setAccessible(false);
              }
          }
          
          Region r = ClientCacheFactory.getAnyInstance().getRegion(regionName);
          r.destroy(key);
          objectMapper.writeValue(response.getWriter(), true);
      }
      catch(IllegalAccessException |NoSuchFieldException |InstantiationException |IOException e)
      {
          logger.error("delete region:"+regionName,e);
      }
  }
  
  @RequestMapping(value = "/region/sizem/{regionName}", method = {RequestMethod.POST,RequestMethod.GET})
  public ModelAndView sizemregion(@PathVariable String regionName)
  {
    ModelAndView modelAndView = new ModelAndView("tools/region_sizem");
    rc.executeSizem(regionName);
    List<HashMap<String,Object>> rs = (List<HashMap<String,Object>>)rc.get(CommandMeta.SIZEM);
    if(rs == null)
    {
        modelAndView.addObject("msg",MsgStore.needInit);
        return modelAndView;
    }
    //转换,将回车换成<br>
    modelAndView.addObject("sizem",rs );
    modelAndView.addObject("regionName",regionName);
    return modelAndView;
  }
  
  @RequestMapping(value = "/region/prb/{regionName}", method = {RequestMethod.POST,RequestMethod.GET})
  public ModelAndView prbregion(@PathVariable String regionName)
  {
    ModelAndView modelAndView = new ModelAndView("tools/region_prb");
    String prb = rc.executePrb(regionName);
    if(prb == null)
    {
        modelAndView.addObject("msg",MsgStore.needInit);
        return modelAndView;
    }
    prb = prb.replaceAll("\n", "<br/>");
    prb = prb.replaceAll("\t", "&nbsp;&nbsp;&nbsp;");
    modelAndView.addObject("prb",prb);
    modelAndView.addObject("regionName",regionName);
    return modelAndView;
  }
  
  
  @RequestMapping(value = "/executesql", method = RequestMethod.GET)
  public ModelAndView executeSql()
  {
    return new ModelAndView("tools/execute_sql");
  }
  
  
  @RequestMapping(value = "/executesql", method = RequestMethod.POST)
  @ResponseBody
  public ModelAndView doSql(HttpServletRequest request,ModelAndView modelAndView)
  {
      modelAndView = new ModelAndView("tools/execute_sql");
      String sql = request.getParameter("sql");
      String file = request.getParameter("file");
      boolean success = es.execute(sql, file);
      modelAndView.addObject("sql",sql);
      modelAndView.addObject("file",file);
      if(!success){
          modelAndView.addObject("msg","执行出错,请检查错误日志");
      }
      else
      {
          modelAndView.addObject("msg","执行成功!"+DateUtil.format(new Date(),DateUtil.yyyy_MM_dd_HHmmss_SSS));
      }
      return modelAndView;
  }
}
