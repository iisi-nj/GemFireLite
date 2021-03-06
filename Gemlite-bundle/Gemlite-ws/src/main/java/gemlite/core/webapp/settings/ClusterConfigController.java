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
package gemlite.core.webapp.settings;

import gemlite.core.common.DESUtil;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.domain.ConfigKeys;
import gemlite.core.internal.support.jpa.files.domain.ConfigTypes;
import gemlite.core.internal.support.jpa.files.domain.GmConfig;
import gemlite.core.internal.support.jpa.files.service.ConfigService;
import gemlite.core.util.LogUtil;
import gemlite.shell.service.batch.ImportService;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 集群信息配置,目前此功能只为ws所用
 * @author GSONG
 * 2015年6月8日
 */
@Controller
@RequestMapping(value = "/settings")
public class ClusterConfigController
{
    private static String PAGE_PREFIX = "settings/";
    
    @RequestMapping(value = "/cluster-config", method = RequestMethod.GET)
    public ModelAndView add(ModelAndView modelAndView)
    {
      modelAndView = new ModelAndView(PAGE_PREFIX + "cluster-config");
      
      ConfigService service = JpaContext.getService(ConfigService.class);
      Map<String,String> map  = service.getConfig(ConfigTypes.clusterconfig.getValue());
      map.put(ConfigKeys.cluster_rootpsw.getValue(), DESUtil.decrypt(map.get(ConfigKeys.cluster_rootpsw.getValue())));
      map.put(ConfigKeys.cluster_userpsw.getValue(), DESUtil.decrypt(map.get(ConfigKeys.cluster_userpsw.getValue())));
      if(map!=null)
      {
         modelAndView.addAllObjects(map);
      }
      return modelAndView;
    }
    
    @RequestMapping(value = { "/cluster-config-save" }, method = RequestMethod.POST)
    @ResponseBody
    public boolean saveClusterConfig(HttpServletRequest request) throws IOException
    {
        try
        {
            ConfigService service = JpaContext.getService(ConfigService.class);
            ConfigKeys[] commonkeys = {ConfigKeys.cluster_username,ConfigKeys.cluster_list,ConfigKeys.cluster_primaryip,ConfigKeys.cluster_locatorlist,ConfigKeys.cluster_start_datastore,ConfigKeys.cluster_start_locator};
            ConfigKeys[] pswkeys= {ConfigKeys.cluster_rootpsw,ConfigKeys.cluster_userpsw};
            //设置数据
            for(ConfigKeys key:commonkeys)
            {
                String value = (String)request.getParameter(key.getValue());
                saveConf(service, key.getValue(), value,false);
            }
            
            for(ConfigKeys key:pswkeys)
            {
                String value = (String)request.getParameter(key.getValue());
                saveConf(service, key.getValue(), value,true);                
            }
        }
        catch (Exception e)
        {
            LogUtil.getCoreLog().error("saveClusterConfig error:",e);
            return false;
        }
        return true;
    } 
    
    private void saveConf(ConfigService service,String key,String value,boolean isPsw) throws  Exception
    {
        //跟数据库原值对比,如果一至,则不修改,否则需要修改
        GmConfig conf = service.findConfigByKey(key);
        if(conf != null)
        {
            if(isPsw)
            value =  DESUtil.encrypt(value);
            //如果数据没有变化,则直接返回不保存
           if(StringUtils.equals(conf.getValue(),value))
               return;
          //如果是密码,则需要加密
          conf.setValue(value);
        }
        else
        {
           //如果是密码,则需要加密
           if(isPsw)
           value =  DESUtil.encrypt(value);
           conf = new GmConfig(key, value, ConfigTypes.clusterconfig.getValue());
        }
        service.saveConf(conf);
    }
}
