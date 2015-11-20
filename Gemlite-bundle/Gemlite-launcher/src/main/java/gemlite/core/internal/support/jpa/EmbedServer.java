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
package gemlite.core.internal.support.jpa;

import gemlite.core.internal.support.context.ManagementRegionHelper;
import gemlite.core.internal.support.context.ManagementRegionHelper.MGM_KEYS;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.h2.tools.Server;

public class EmbedServer
{
  private static Server server;
  private static AtomicBoolean initialized = new AtomicBoolean();
  
  public static void processConnectionURL()
  {
    String jmxHost=(String)ManagementRegionHelper.getValue(MGM_KEYS.jmx_host); 
    String host = ServerConfigHelper.getConfig(ITEMS.BINDIP);
    String port = ServerConfigHelper.getProperty("config.db.port");
    ServerConfigHelper.setProperty("config.db.url",  "jdbc:h2:tcp://" + host + ":" + port + "/gemlite_manager;USER=sa");
    ServerConfigHelper.setProperty("config.jmx.ip",jmxHost);
   
  }
  
  public static void startServer()
  {
    if (initialized.get())
      return;
    initialized.set(true);
    String workPath = ServerConfigHelper.getWorkPath();
    String host = ServerConfigHelper.getConfig(ITEMS.BINDIP);
    String port = ServerConfigHelper.getProperty("config.db.port");
    ManagementRegionHelper.setValue(MGM_KEYS.config_db_host, host);
    ManagementRegionHelper.setValue(MGM_KEYS.config_db_port, port);
    try
    {
      String[] params = new String[]{"-tcpPort",port,"-tcp","-tcpAllowOthers","-baseDir",workPath+"/h2_home"};
      server = Server.createTcpServer(params);
      server.start();
      //jdbc:h2:tcp://localhost:8081/testaaa/qqq1
      String urlConn = "jdbc:h2:tcp://" + host + ":" + port + "/gemlite_manager;USER=sa";
      ServerConfigHelper.setProperty("config.db.url", urlConn);
      
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().warn(e);
      initialized.set(false);
    }
  }
}
