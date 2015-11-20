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
package gemlite.core.internal.support.system;

import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.lang.management.ManagementFactory;
import java.net.ServerSocket;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import mx4j.tools.adaptor.http.HttpAdaptor;
import mx4j.tools.naming.NamingService;

public class GemliteAgent
{
  private static GemliteAgent inst = new GemliteAgent();
  private GemliteAgent()
  {
    
  }
  public final static GemliteAgent getInstance()
  {
    return inst;
  }
  public void startRMIConnector(int jmxPort)
  {
    try
    {
      LogUtil.getCoreLog().info("RMI port:"+jmxPort);
      ServerConfigHelper.setProperty("JMX_RMI_PORT", ""+jmxPort);
      MBeanServer mbs= ManagementFactory.getPlatformMBeanServer();
      ObjectName namingName = ObjectName.getInstance("naming:type=rmiregistry");
      mbs.registerMBean(new NamingService(jmxPort), namingName);
      mbs.invoke(namingName, "start", null, null);
      String jndiPath = "/jmxconnector";
      String bindIp = ServerConfigHelper.getProperty(ITEMS.BINDIP.name());
      JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+bindIp+":" + jmxPort + jndiPath);
      JMXConnectorServer connector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
      
      ObjectName connectorName = null;
      connectorName = new ObjectName("Gemlite:name=RMIConnector");
      mbs.registerMBean(connector, connectorName);
      connector.start();
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("Error start rmi connector",e);
    }
  }
  public void startRMIConnector()
  {
    try
    {
      ServerSocket sso = new ServerSocket(0);
      int port = sso.getLocalPort();
      sso.close();
      startRMIConnector(port);
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("Error start rmi connector",e);
    }
  }
  
  public void startHtmlAdapter(int port)
  {
    
    try
    {
      HttpAdaptor adapter = new HttpAdaptor();
      ObjectName adapterName = new ObjectName("Gemlite:name=HtmlAdapter,port=" + port);
      adapter.setPort(port);
      ManagementFactory.getPlatformMBeanServer().registerMBean(adapter, adapterName);
      adapter.start();
      
      LogUtil.getCoreLog().info("HtmlAdapter started on port: "+port);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void startHtmlAdapter()
  {
    try
    {
    	ServerSocket sso = new ServerSocket(0);
        int port = sso.getLocalPort();
        sso.close();
        LogUtil.getCoreLog().info("HTTP port:"+port);
        ServerConfigHelper.setProperty("JMX_HTTP_PORT", ""+port);
        HttpAdaptor adapter = new HttpAdaptor();
        ObjectName adapterName = new ObjectName("Gemlite:name=HtmlAdapter,port=" + port);
        adapter.setPort(port);
        ManagementFactory.getPlatformMBeanServer().registerMBean(adapter, adapterName);
        adapter.start();
      
        LogUtil.getCoreLog().info("HtmlAdapter started on port: "+port);
    }
    catch (Exception e)
    {
    	e.printStackTrace();
    }
  }
}
