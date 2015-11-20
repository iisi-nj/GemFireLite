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
package gemlite.core.internal.admin.service;

import gemlite.core.annotations.admin.AdminService;
import gemlite.core.internal.admin.AbstractRemoteAdminService;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.util.Map;

import org.apache.logging.log4j.Logger;


/**
 * (î9„î9log4jMnH
 * @author gsong
 */
@AdminService(name = "Log4jService")
public class Log4jService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  
  
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    ServerConfigHelper.initLog4j("classpath:log4j-server.xml");
    //ûÖ@	log4j„SM„log§+
    StringBuilder sb = new StringBuilder();
    sb.append(LogUtil.getAppLog().getName()).append(":");
    sb.append(getLogLevel(LogUtil.getAppLog())).append("\n");
    sb.append(LogUtil.getLogicLog().getName()).append(":");
    sb.append(getLogLevel(LogUtil.getLogicLog())).append("\n");
    sb.append(LogUtil.getMqSyncLog().getName()).append(":");
    sb.append(getLogLevel(LogUtil.getMqSyncLog())).append("\n");
    return System.getProperty(ITEMS.BINDIP.name())+":"+System.getProperty(ITEMS.NODE_NAME.name())+"ok.\n"+sb.toString();
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
    // TODO Auto-generated method stub
    
  }
  
  private String getLogLevel(Logger logger)
  {
    String level = "unknown";
//    if(logger.isDebugEnabled())
//    {
//      level = LogLevel.DEBUG.getLabel();
//    }
//    else if(logger.isInfoEnabled())
//    {
//      level = LogLevel.INFO.getLabel();
//    }
//    else if(logger.isWarnEnabled())
//    {
//      level = LogLevel.WARN.getLabel();
//    }
//    else if(logger.isErrorEnabled())
//    {
//      level = LogLevel.ERROR.getLabel();
//    }
    return level;
  }
}

