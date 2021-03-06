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

import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.domain.ConfigKeys;
import gemlite.core.internal.support.jpa.files.domain.ConfigTypes;
import gemlite.core.internal.support.jpa.files.service.ConfigService;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.CmdUtils;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/tools/cluster")
public class ClusterController
{

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView index()
    {
        ModelAndView view = new ModelAndView("tools/cluster_manage");
        ConfigService service = JpaContext.getService(ConfigService.class);
        Map<String,String> map = service.getConfig(ConfigTypes.clusterconfig.getValue());
        String cluster_list  = map.get(ConfigKeys.cluster_list.getValue());
        String ips[] = StringUtils.split(cluster_list,",");
        String cluster_primaryip = map.get(ConfigKeys.cluster_primaryip.getValue());
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(); 
        for(String ip:ips)
        {
            Map<String,Object> obj = new HashMap<String, Object>();
            obj.put("ip",ip);
            if(StringUtils.equals(cluster_primaryip, ip))
                obj.put("isP", true);
            else obj.put("isP", false);
            list.add(obj);
        }
        view.addObject("list", list);
        return view;
    }
    /**
     * �Ƥ,�,,�
     */
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    @ResponseBody
    public boolean deploy()
    {
        //, e��ƤMn
        ConfigService service = JpaContext.getService(ConfigService.class);
        Map<String,String> map = service.getConfig(ConfigTypes.clusterconfig.getValue());
        
        //�0:hh�;�:h
        String cluster_list  = map.get(ConfigKeys.cluster_list.getValue());
        String cluster_primaryip = map.get(ConfigKeys.cluster_primaryip.getValue());
        String ips[] = StringUtils.split(cluster_list,",");
        if(ips==null)return false;
        
          String homeDir = ServerConfigHelper.getConfig(ITEMS.GS_HOME);
          for(String ip:ips)
          {
                  String cmd = CmdUtils.format(CmdUtils.scp, homeDir,ip,getParentDir(homeDir));
                  String bashrc = CmdUtils.format(CmdUtils.scp, homeDir+"/.bashrc",ip,"~");
                  try
                  {
                      //�@	�gLbashrc�
                      CmdUtils.exeCmd(bashrc);
                      LogUtil.getCoreLog().info("Do deploy command {} ",bashrc);
                      
                      if(!StringUtils.equals(ip, cluster_primaryip))
                      {
                        CmdUtils.exeCmd(cmd.toString());
                        LogUtil.getCoreLog().info("Do deploy command {} ",cmd);
                      }
                  }
                  catch(Exception e)
                  {
                      LogUtil.getCoreLog().info("Do deploy command : {} ,Exception {}",cmd.toString(),e);
                      return false;
                  }
          }
          return true;
    }
    
    /**
     * ��hoemDir�
��U
     * /home/gemfire/order
     * @param homeDir
     * @return
     */
    private String getParentDir(String homeDir)
    {
       if(StringUtils.endsWith(homeDir, "/"))
           homeDir = StringUtils.substring(homeDir,0,homeDir.length()-1);
       int index = StringUtils.lastIndexOf(homeDir, "/");
       String left = StringUtils.substring(homeDir,0,index);
       return left;
    }
    
    /**
     * /�Locator,, e
     */
    @RequestMapping(value = "/start_locator", method = RequestMethod.POST)
    @ResponseBody
    public boolean startLocator()
    {
        //�0:hh�;�:h
        ConfigService service = JpaContext.getService(ConfigService.class);
        Map<String,String> map = service.getConfig(ConfigTypes.clusterconfig.getValue());
        String cluster_list  = map.get(ConfigKeys.cluster_list.getValue());
        String ips[] = StringUtils.split(cluster_list,",");
        if(ips==null)return false;
        String username = map.get(ConfigKeys.cluster_username.getValue());
        String psw = map.get(ConfigKeys.cluster_userpsw.getValue());
        String start_locator = map.get(ConfigKeys.cluster_start_locator.getValue());
        String locator_list = map.get(ConfigKeys.cluster_locatorlist.getValue());
        // �������/&�t
        try
        {
            //�h�ip
            for(String ip:ips)
            {
                boolean running = checkThreadStatus(ip, username, psw ,CmdUtils.grep_locator ,"gemlite.core.commands.Locator");
                if(running)
                {
                    LogUtil.getCoreLog().info("Start Locator fail , {} Locator is running...",ip);
                    return false;
                }
            }
            
            
            //���, �/�locator,	glocatorMn��e/�
            String cmd = CmdUtils.format(CmdUtils.start_locator, ServerConfigHelper.getConfig(ITEMS.GS_HOME),start_locator);
            //M�locator list
            if(locator_list!=null)
            {
                String locatorarr[] = StringUtils.split(locator_list,",");
                for(String locatorip:locatorarr)
                {
                    CmdUtils.exeCmd(locatorip, username, psw, cmd);
                }
            }
            return true;
        }
        catch(Exception e)
        {
            LogUtil.getCoreLog().error("check file error:",e);
        }
        return false;
    }
    
    /**
     * /�Ƥ
     */
    @RequestMapping(value = "/start_datastore", method = RequestMethod.POST)
    @ResponseBody
    public boolean startServer()
    {
        //�0:hh�;�:h
        ConfigService service = JpaContext.getService(ConfigService.class);
        Map<String,String> map = service.getConfig(ConfigTypes.clusterconfig.getValue());
        String cluster_list  = map.get(ConfigKeys.cluster_list.getValue());
        String ips[] = StringUtils.split(cluster_list,",");
        if(ips==null)return false;
        String username = map.get(ConfigKeys.cluster_username.getValue());
        String psw = map.get(ConfigKeys.cluster_userpsw.getValue());
        String start_datastore = map.get(ConfigKeys.cluster_start_datastore.getValue());
        // �������/&�t
        try
        {
            //�h�ip
            for(String ip:ips)
            {
                boolean running = checkThreadStatus(ip, username, psw ,CmdUtils.grep_datastore ,"gemlite.core.commands.DataStore");
                if(running)
                {
                    LogUtil.getCoreLog().info("Start DataStore fail , {} DataStore is running...",ip);
                    return false;
                }
            }
            
            //���, �/�locator,	glocatorMn��e/�
            String cmd = CmdUtils.format(CmdUtils.start_datastore, ServerConfigHelper.getConfig(ITEMS.GS_HOME),start_datastore);
            //M�locator list
            if(cluster_list!=null)
            {
                String clusterarr[] = StringUtils.split(cluster_list,",");
                for(String ip:clusterarr)
                {
                    CmdUtils.exeCmd(ip, username, psw, cmd);
                }
            }
            return true;
        }
        catch(Exception e)
        {
            LogUtil.getCoreLog().error("check file error:",e);
        }
        return false;
    }
    
    
    /**
     * /�monitor
     */
    @RequestMapping(value = "/start_monitor", method = RequestMethod.POST)
    @ResponseBody
    public boolean startMonitor()
    {
        //�0:hh�;�:h
        ConfigService service = JpaContext.getService(ConfigService.class);
        Map<String,String> map = service.getConfig(ConfigTypes.clusterconfig.getValue());
        String cluster_list  = map.get(ConfigKeys.cluster_list.getValue());
        String ips[] = StringUtils.split(cluster_list,",");
        if(ips==null)return false;
        String username = map.get(ConfigKeys.cluster_username.getValue());
        String psw = map.get(ConfigKeys.cluster_userpsw.getValue());
        String cluster_primaryip = map.get(ConfigKeys.cluster_primaryip.getValue());
        // �������/&�t
        try
        {
            //�h�ip
            boolean running = checkThreadStatus(cluster_primaryip, username, psw ,CmdUtils.grep_monitor ,"gemlite.core.command.GemliteMonitor");
            if(running)
            {
                LogUtil.getCoreLog().info("Start GemliteMonitor fail , {} Monitor is running...",cluster_primaryip);
                return false;
            }
            
            //���, �/�locator,	glocatorMn��e/�
            String cmd = CmdUtils.format(CmdUtils.start_monitor,ServerConfigHelper.getConfig(ITEMS.GS_HOME));
            CmdUtils.exeCmd(cmd);
            return true;
        }
        catch(Exception e)
        {
            LogUtil.getCoreLog().error("check file error:",e);
        }
        return false;
    }
    
    private boolean checkThreadStatus(String ip,String username,String psw,String cmd,String thread) throws Exception
    {
        String rs = CmdUtils.exeCmd(ip, username, psw, cmd);
        if(StringUtils.contains(rs, thread))
        {
            LogUtil.getCoreLog().warn("{} : {} is running {}",ip,thread,rs);
            return true;
        }
        return false;
    }
    
    /**
     * �@	Ƥ��,�o
     * 	�;:?
     * d;:
/��Locator,DataStore�ŵ
     */
    @RequestMapping(value = "/list_cluster", method = RequestMethod.POST)
    @ResponseBody
    public void listCluster()
    {
        ConfigService service = JpaContext.getService(ConfigService.class);
        Map<String,String> map = service.getConfig(ConfigTypes.clusterconfig.getValue());
        String cluster_list  = map.get(ConfigKeys.cluster_list.getValue());
        String ips[] = StringUtils.split(cluster_list,",");
    }
}
