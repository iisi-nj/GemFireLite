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
import gemlite.core.internal.support.jpa.files.service.ConfigService;
import gemlite.core.util.LogUtil;
import gemlite.shell.commands.Import;
import gemlite.shell.service.batch.ImportService;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 配置导入数据库的数据
 * @author GSONG
 * 2015年6月2日
 */
@Controller
@RequestMapping(value = "/settings")
public class ImportDbConfigController
{
    @Autowired
    private ImportService im;
    
    private static String PAGE_PREFIX = "settings/";
    
    
    @RequestMapping(value = "/import-db-config", method = RequestMethod.GET)
    public ModelAndView add(ModelAndView modelAndView)
    {
      modelAndView = new ModelAndView(PAGE_PREFIX + "import-db-config");
      
      ConfigService service = JpaContext.getService(ConfigService.class);
      Map<String,String> map  = service.getConfig(ConfigTypes.importdbconfig.getValue());
      if(map!=null)
      {
         //将psw解密
         String psw = map.get(ConfigKeys.import_dbpsw.getValue());
         map.put(ConfigKeys.import_dbpsw.getValue(),DESUtil.decrypt(psw));
         modelAndView.addAllObjects(map);
      }
      return modelAndView;
    }
    
    @RequestMapping(value = { "/import-db-config-save" }, method = RequestMethod.POST)
    @ResponseBody
    public boolean saveImport(HttpServletRequest request) throws IOException
    {
        try
        {
            //设置数据
            String dbdriver = (String)request.getParameter("dbdriver");
            String dburl = (String)request.getParameter("dburl");
            String dbuser = (String)request.getParameter("dbuser");
            String dbpsw = (String)request.getParameter("dbpsw");
            im.saveDbConfig(dbdriver, dburl, dbuser, dbpsw);
            return true;
        }
        catch (Exception e)
        {
            LogUtil.getCoreLog().error("saveImport error:",e);
            return false;
        }
    } 
}
