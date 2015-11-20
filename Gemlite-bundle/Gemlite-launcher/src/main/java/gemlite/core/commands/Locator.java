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

import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.ManagementRegionHelper;
import gemlite.core.internal.support.context.ManagementRegionHelper.MGM_KEYS;
import gemlite.core.internal.support.jmx.GemliteManagerNode;
import gemlite.core.internal.support.jpa.EmbedServer;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.support.system.ServerConfigHelper.TYPES;
import gemlite.core.internal.support.system.WorkPathHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.RSAUtils;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.distributed.DistributedLockService;
import com.gemstone.org.jgroups.blocks.DistributedLockManager;

public class Locator
{
  @Option(name = "-name", usage = "Locator name,default:node0,node1...")
  private String name;
  @Option(name = "-configXml", usage = "Locator config file")
  private String configXml = "ds-locator.xml";
  @Option(name = "-port", usage = "Locator config file")
  private int port = 12345;
  
  private static Locator lcInstance = new Locator();
  
  private ClassPathXmlApplicationContext mainContext;
  
  public final static Locator getInstance()
  {
    return lcInstance;
  }
  
  private Locator()
  {
    
  }
  
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    Locator lc = new Locator();
    lc.start(args);
  }
  
  public void start(String[] args)
  {
    
    basicStart(args);
    GemliteContext.registerStaticFunction();
    if(!ManagementRegionHelper.containsKey(MGM_KEYS.role_jmx_manager))
    {
      String ip = ServerConfigHelper.getConfig(ITEMS.BINDIP);
      String name = ServerConfigHelper.getConfig(ITEMS.NODE_NAME);
      ManagementRegionHelper.setValue(MGM_KEYS.role_jmx_manager, ip+"/"+name);
      GemliteManagerNode.getInstance().startJMXManager();
      EmbedServer.startServer();
    }
    else
    {
      String str = (String) ManagementRegionHelper.getValue(MGM_KEYS.role_jmx_manager);
      LogUtil.getCoreLog().info("JMX_MANAGER and Embed H2 has started at {}",str);
    }
    
    waitForComplete();
  }
  
  public void waitForComplete()
  {
    try
    {
      if(!RSAUtils.checkLicense())
        System.exit(0);
      Thread.sleep(Long.MAX_VALUE);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
  
  public void basicStart(String[] args)
  {
    if (System.getProperty("GF6") == null)
    {
      System.setProperty("GF6", "");
    }
    try
    {
      
      ServerConfigHelper.initConfig(TYPES.LOCATOR);
      CmdLineParser cp = new CmdLineParser(this);
      cp.parseArgument(args);
      
      System.getProperties().put("locatorPort", port);
      name = WorkPathHelper.verifyServerName(ITEMS.GS_WORK, "locator", name);
      ServerConfigHelper.setProperty(ServerConfigHelper.ITEMS.NODE_NAME.name(), name);
      ServerConfigHelper.setProperty(ServerConfigHelper.ITEMS.NODE_TYPE.name(), "locator");
      ServerConfigHelper.initLog4j("classpath:log4j2-server.xml");
      
      
      LogUtil.getCoreLog().info("Locator starting ...");
      mainContext = new ClassPathXmlApplicationContext(new String[] { configXml }, false);
      mainContext.setValidating(true);
      
      mainContext.refresh();
      LogUtil.getCoreLog().info("Locator started.");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
  
}
