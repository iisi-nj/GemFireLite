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
package gemlite.core.webapp;

import gemlite.core.internal.support.context.DomainMapperHelper;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;
import gemlite.shell.commands.Client;
import gemlite.shell.commands.admin.ManageNode;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
    ManageNode mn;
    
    @Autowired
    Client client;
    
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(Locale locale, ModelAndView modelAndView,HttpServletRequest request) {
//	    if(LogUtil.getCoreLog().isDebugEnabled())
//		LogUtil.getCoreLog().debug("Welcome home! The client connect is {}.",  mn.isConnect() && client.isConnect());
		modelAndView = new ModelAndView("index");
		modelAndView.addObject("connect", mn.isConnect() && client.isConnect());
		return modelAndView;
	}
	
	@RequestMapping(value = "/init", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public boolean init(HttpServletRequest request)
    {
//        if (mn.isConnect() && client.isConnect())
//            return true;
	    
        //取env数据,输出
        ServerConfigHelper.initConfig();
        ServerConfigHelper.initLog4j("classpath:log4j2-server.xml");
        
        //在这里注册domain
        DomainMapperHelper.scanMapperRegistryClass();
        
        try
        {
            client.connect();
            String ip = ServerConfigHelper.getProperty("config.jmx.ip");
            if(StringUtils.isEmpty(ip))
                throw new Exception("please config config.jmx.ip property~");
            //默认使用配置config.jmx.port
            mn.execute(ip, -1);
        }
        catch(Exception e)
        {
            LogUtil.getCoreLog().error("init client error:", e);
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/need", method = RequestMethod.GET)
    @ResponseBody
    public boolean needInit()
    {
        return !(mn.isConnect() && client.isConnect());
    }
}
