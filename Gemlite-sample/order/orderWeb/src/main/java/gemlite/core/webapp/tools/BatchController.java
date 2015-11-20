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

import gemlite.core.common.DESUtil;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.domain.ConfigKeys;
import gemlite.core.internal.support.jpa.files.domain.ConfigTypes;
import gemlite.core.internal.support.jpa.files.domain.GmBatch;
import gemlite.core.internal.support.jpa.files.service.BatchService;
import gemlite.core.internal.support.jpa.files.service.ConfigService;
import gemlite.core.util.LogUtil;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.commands.Import;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value = "/batch")
public class BatchController
{
   @Autowired
   private Import im;
   private static String PAGE_PREFIX = "batch/";
   public static boolean first = false;
  
  @RequestMapping(value = "", method = RequestMethod.GET)
  public ModelAndView batch(ModelAndView modelAndView)
  {
    modelAndView = new ModelAndView(PAGE_PREFIX + "import");
    //Öpn
    BatchService batchService = JpaContext.getService(BatchService.class);
    List<GmBatch> list = batchService.findAll();
    modelAndView.addObject("list",list);
    return modelAndView;
  }
  
  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public ModelAndView add(ModelAndView modelAndView)
  {
    modelAndView = new ModelAndView(PAGE_PREFIX + "add");
    //ûÖpn“„Mnáo
    ConfigService service = JpaContext.getService(ConfigService.class);
    Map<String,String> map  = service.getConfig(ConfigTypes.importdbconfig.getValue());
    if(map!=null)
    {
      //pswãÆ
      String psw = map.get(ConfigKeys.import_dbpsw.getValue());
      map.put(ConfigKeys.import_dbpsw.getValue(),DESUtil.decrypt(psw));
      modelAndView.addAllObjects(map);
    }
    return modelAndView;
  }
  
  @RequestMapping(value = { "/save" }, method = RequestMethod.POST)
  @ResponseBody
  public boolean saveImport(HttpServletRequest request) throws IOException
  {
      try
      {
          //¾npn
          String region = (String)request.getParameter("region");
          String template = (String)request.getParameter("template");
          boolean forceUpdate = StringUtils.equals(request.getParameter("forceUpdate"),"on");
          String file = (String)request.getParameter("file");
          String delimiter = (String)request.getParameter("delimiter");
          String quote = (String)request.getParameter("quote");
          boolean skipable = StringUtils.equals(request.getParameter("skipable"),"on");
          String columns = (String)request.getParameter("columns");
          String table = (String)request.getParameter("table");
          String encoding = (String)request.getParameter("encoding");
          int linesToSkip = NumberUtils.toInt(request.getParameter("linesToSkip"), 0) ;
          String sortKey = (String)request.getParameter("sortKey");
          String where = (String)request.getParameter("where");
          int pageSize = NumberUtils.toInt(request.getParameter("pageSize"),100);
          int fetchSize = NumberUtils.toInt(request.getParameter("fetchSize"),100000000);
          String dbdriver = (String)request.getParameter("dbdriver");
          String dburl = (String)request.getParameter("dburl");
          String dbuser = (String)request.getParameter("dbuser");
          String dbpsw = (String)request.getParameter("dbpsw");
          im.runJob(region, forceUpdate, template, file, delimiter, quote, skipable, columns, table, encoding, linesToSkip, false, false, dbdriver,dburl,dbuser,dbpsw,sortKey, where, pageSize, fetchSize);
          return true;
      }
      catch (Exception e)
      {
          LogUtil.getCoreLog().error("saveImport error:",e);
          return false;
      }
  }
  
  @RequestMapping(value = "/edit", method = RequestMethod.GET)
  public ModelAndView edit(HttpServletRequest request)
  {
    ModelAndView modelAndView = new ModelAndView(PAGE_PREFIX + "edit");
    Long id   = NumberUtils.toLong(request.getParameter("id"));
    //ûÖpn“„Mnáo
    BatchService service = JpaContext.getService(BatchService.class);
    GmBatch batch  = service.findOne(id);
    
    if(batch!=null)
    {
       //ãbatchWµ
       String cmd = batch.getCmd();
       Map<String,String> map = splitCmd(cmd);
       modelAndView.addAllObjects(map);
    }
    return modelAndView;
  }
  
  
  @RequestMapping(value = { "/list/api" }, method = {RequestMethod.POST,RequestMethod.GET})
  @ResponseBody
  public List<Map<String,Object>> listJobs(HttpServletRequest request) throws IOException
  {
      List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
      try
      {
          im.listJobs("");
          Collection<Map> jobs = (Collection<Map>)im.get(CommandMeta.LIST_JOBS);
          for(Map map:jobs)
          {
              im.describeJob(map.get("job_execution_id").toString());
              Map<String,Object> jobExecution = (Map<String,Object>)im.get(CommandMeta.DESCRIBE_JOB);
              if(jobExecution!=null)
                list.add(jobExecution);
          }
      }
      catch (Exception e)
      {
          LogUtil.getCoreLog().error("list error:",e);
      }
      return list;
  }
  
  
  @RequestMapping(value = { "/execute" }, method = {RequestMethod.POST,RequestMethod.GET})
  @ResponseBody
  public boolean execute(HttpServletRequest request) throws IOException
  {
      try
      {
          String id = request.getParameter("id");
          //$z
          if(StringUtils.isEmpty(id))
              return true;
          //9nidÖ<
          BatchService s = JpaContext.getService(BatchService.class);
          GmBatch batch = s.findOne(NumberUtils.toLong(id));
          if(batch == null || StringUtils.isEmpty(batch.getCmd()))
              return true;
          String cmd = batch.getCmd();
          
          //ã
          Map<String,String> map = splitCmd(cmd);
          //¾npn
          String region = (String)map.get("region");
          String template = (String)map.get("template");
          boolean forceUpdate = StringUtils.equals(map.get("update"),"true");
          String file = (String)map.get("file");
          String delimiter = (String)map.get("delimiter");
          String quote = (String)map.get("quote");
          boolean skipable = StringUtils.equals(map.get("skipable"),"true");
          String columns = (String)map.get("columns");
          String table = (String)map.get("table");
          String encoding = (String)map.get("encoding");
          int linesToSkip = NumberUtils.toInt(map.get("linesToSkip"), 0) ;
          String sortKey = (String)map.get("sortKey");
          String where = (String)map.get("where");
          int pageSize = NumberUtils.toInt(map.get("pageSize"),100);
          int fetchSize = NumberUtils.toInt(map.get("fetchSize"),100000000);
          String dbdriver = (String)map.get("dbdriver");
          String dburl = (String)map.get("dburl");
          String dbuser = (String)map.get("dbuser");
          String dbpsw = (String)map.get("dbpsw");
          im.runJob(region, forceUpdate, template, file, delimiter, quote, skipable, columns, table, encoding, linesToSkip, false, false, dbdriver,dburl,dbuser,dbpsw, sortKey, where, pageSize, fetchSize);
          return true;
      }
      catch(Exception e)
      {
          LogUtil.getCoreLog().error("execute cmd error:"+request.getParameter("cmd"),e);
      }
      return false;
  }
  
  private Map splitCmd(String cmd)
  {
      String[] arr = cmd.split("--");
      HashMap<String,String> map = new HashMap<String,String>();
      for(String str:arr)
      {
          //~0, *z<,‚œ~0,»‰dpn
          String c = StringUtils.trim(str);
          int index = c.indexOf(" ");
          //<pn
          if(index<=0)
              continue;
          
          //úk,v
          String key = StringUtils.trim(StringUtils.substring(c, 0, index));
          String value = StringUtils.trim(StringUtils.substring(c, index));
          map.put(key, value);
      }
      return map;
  }
  
  //api
  @RequestMapping(value = "api/list", method = RequestMethod.GET)
  @ResponseBody
  public List<GmBatch> findJobs(ModelAndView modelAndView)
  {
	BatchService batchService = JpaContext.getService(BatchService.class);
    modelAndView = new ModelAndView(PAGE_PREFIX + "index");
    List<GmBatch> list = batchService.findAll();
    return list;
  }
  
  @RequestMapping(value = "/add")
  public ModelAndView getAdd()
  {
    ModelAndView view = new ModelAndView();
    view.setViewName(PAGE_PREFIX + "add");
    return view;
  }
  
  @RequestMapping(value = "/edit/{id}")
  public ModelAndView getEdit(ModelAndView modelAndView, @PathVariable Long id)
  {
	BatchService batchService = JpaContext.getService(BatchService.class);
    ModelAndView view = new ModelAndView();
    view.setViewName(PAGE_PREFIX + "edit");
    GmBatch batch = batchService.findOne(id);
    if (batch == null)
    {
      view.addObject("errMsg", "jobX(");
    }
    view.addObject("batch", batch);
    return view;
  }
  
  @RequestMapping(value = "/batch/execution", method = RequestMethod.GET)
  public ModelAndView batchExecution(ModelAndView modelAndView)
  {
	BatchService batchService = JpaContext.getService(BatchService.class);
    modelAndView = new ModelAndView(PAGE_PREFIX + "exec");
    int count = batchService.countJobExecutions();
    Collection<Map> list = batchService.listJobExecutions("");
    modelAndView.addObject("list", list);
    modelAndView.addObject("count", count);
    return modelAndView;
  }
  
  @RequestMapping(value = "/batch/execution/{jobExecutionId}", method = RequestMethod.GET)
  public String detail(Model model, @PathVariable Long jobExecutionId)
  {
	BatchService batchService = JpaContext.getService(BatchService.class);
    Map jobExecution = batchService.getJobExecution(jobExecutionId);
    List<Map> steps = batchService.getStepExecutions(jobExecutionId);
    if(jobExecution!=null)
    jobExecution.put("step_count", steps==null||steps.isEmpty()?0:steps.size());
    model.addAttribute("jobExecution", jobExecution);
//    String jobName = (String) jobExecution.get("job_name");
    //List<Map> stpesNames = batchService.getStepNamesForJob(jobName);
    model.addAttribute("steps", steps);
    return PAGE_PREFIX+"execdetail";
  }
  
  @RequestMapping(value = "/import", method = RequestMethod.GET)
  public ModelAndView getImport()
  {
    ModelAndView view = new ModelAndView();
    view.setViewName(PAGE_PREFIX + "import");
    //åâ@	„cmdh
    BatchService bs = JpaContext.getService(BatchService.class);
    List<GmBatch> list = bs.findAll();
    view.addObject("list", list);
    return view;
  }
}
