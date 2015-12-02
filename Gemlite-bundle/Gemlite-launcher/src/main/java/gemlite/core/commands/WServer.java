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
package gemlite.core.commands;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 启动嵌入式server
 * args[0]=warPath <> null
 * args[1]=port default = 8080
 * args[2]=contextPath default = /
 * 
 * @author GSONG
 *         2015年4月28日
 */
public class WServer
{
  public static void main(String[] args2) throws Exception
  {
    String[] defaultArgs = new String[] { "D:/work/data/Gemlite-demo/target/Gemlite-demo-0.0.1-SNAPSHOT.war", "8082", "/" };
    defaultArgs = args2 == null || args2.length == 0 ? defaultArgs : args2;
    ServerConfigHelper.initConfig();
    ServerConfigHelper.initLog4j("classpath:log4j2-server.xml");
    ServerConfigHelper.setProperty("bind-address", ServerConfigHelper.getConfig(ITEMS.BINDIP));
    int port = 8080;
    String contextPath = "/";
    String warPath = "";
    if (defaultArgs.length < 1)
    {
      LogUtil.getCoreLog().error(
          "Start error,plelase Start Ws server like this : java gemlite.core.command.WServer /home/ws.war");
      return;
    }
    
    warPath = defaultArgs[0];
    File file = new File(warPath);
    if (!file.exists())
    {
      LogUtil.getCoreLog().error("Error input:" + defaultArgs[0] + " war path is not existing!");
      return;
    }
    if (!file.isFile())
    {
      LogUtil.getCoreLog().error("Error input:" + defaultArgs[0] + " war path is not a valid file!");
      return;
    }
    
    if (defaultArgs.length > 1)
    {
      port = NumberUtils.toInt(defaultArgs[1]);
      if (port <= 0 || port >= 65535)
      {
        LogUtil.getCoreLog().error("Port Error:" + defaultArgs[1] + "　,not a valid port , make sure port>0 and port<65535");
        return;
      }
    }
    
    if (defaultArgs.length > 2)
    {
      contextPath += StringUtils.replace(defaultArgs[2], "/", "");
    }
    
    try
    {
      // 设置jetty_home
      String jetty_home =  ServerConfigHelper.getConfig(ITEMS.GS_WORK) + File.separator + "jetty_home";
      jetty_home += File.separator + StringUtils.replace(contextPath, "/", "")+port;
      File jfile = new File(jetty_home);
      jfile.mkdirs();
      System.setProperty("jetty.home", jetty_home);
      String jetty_logs = jetty_home + File.separator + "logs" + File.separator;
      File logsFile = new File(jetty_logs);
      logsFile.mkdirs();
      System.setProperty("jetty.logs", jetty_logs);
      
      Server server = new Server();
      HttpConfiguration config = new HttpConfiguration();
      ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(config));
      connector.setReuseAddress(true);
      connector.setIdleTimeout(30000);
      connector.setPort(port);
      server.addConnector(connector);
      
      WebAppContext webapp = new WebAppContext();
      webapp.setContextPath(contextPath);
      webapp.setWar(warPath);
      String tmpStr = jetty_home + File.separator + "webapps" + File.separator;
      File tmpDir = new File(tmpStr);
      tmpDir.mkdirs();
      webapp.setTempDirectory(tmpDir);
      
      // 设置优先从环境变量读取架包
      // webapp.setExtraClasspath(extrapath);
      webapp.setParentLoaderPriority(true);
      
      // 配置Log
      RequestLogHandler requestLogHandler = new RequestLogHandler();
      NCSARequestLog requestLog = new NCSARequestLog(jetty_logs + File.separator + "jetty-yyyy_mm_dd.request.log");
      requestLog.setRetainDays(30);
      requestLog.setAppend(true);
      requestLog.setExtended(false);
      requestLog.setLogTimeZone(TimeZone.getDefault().getID());
      requestLogHandler.setRequestLog(requestLog);
      webapp.setHandler(requestLogHandler);
      
      ContextHandler ch = webapp.getServletContext().getContextHandler();
      ch.setLogger(new Slf4jLog("gemlite.coreLog"));
      server.setHandler(webapp);
      server.start();
      
      System.out.println("-----------------------------------------------------");
      LogUtil.getCoreLog().info(
          "Ws Server started,You can visite -> http://" + ServerConfigHelper.getConfig(ITEMS.BINDIP) + ":" + port
              + contextPath);
      
      server.join();
      
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("Ws Server error:", e);
    }
  }
}
