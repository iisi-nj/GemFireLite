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

import gemlite.core.util.LogUtil;

import java.io.File;
import java.net.InetAddress;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;

import com.gemstone.gemfire.distributed.Locator;
import com.gemstone.gemfire.distributed.internal.InternalLocator;

public class LocatorBean implements InitializingBean
{
  
  private InetAddress bind;
  private int port = 12345;
  private Locator locator;
  private File logFile;
  private File state;
  private boolean peerLocator = true;
  private boolean serverLocator = true;
  private String hostnameForClients;
  private String locators;
  private boolean throwOnBindFailure=true;
  
  private Properties prop;
  
  public LocatorBean()
  {
  }
  
  public void startLocator()
  {
    startLocator(port, true);
  }
  
  public void startLocator(int port, boolean throwOnFail)
  {
    try
    {
      LogUtil.getCoreLog().info("Bind at" + bind + ":" + port);
      // locator = InternalLocator.startLocator(port, logFile, state, null, null, bind, prop, true, serverLocator,
      // null);
      locator = InternalLocator.startLocator(port, logFile, state, null, null, bind, prop, peerLocator, serverLocator,
          hostnameForClients,false);
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().info("Locator start failure for port[" + port + "]:", e);
    }
  }
  
  public void stopLocator()
  {
    if (locator != null)
    {
      locator.stop();
      locator = null;
    }
  }
  
  /**
   * @return the throwOnBindFailure
   */
  public boolean isThrowOnBindFailure()
  {
    return throwOnBindFailure;
  }
  
  /**
   * @param throwOnBindFailure
   *          the throwOnBindFailure to set
   */
  public void setThrowOnBindFailure(boolean throwOnBindFailure)
  {
    this.throwOnBindFailure = throwOnBindFailure;
  }
  
  /**
   * @return the bind
   */
  public String getBind()
  {
    return bind.getHostAddress();
  }
  
  /**
   * @param bind
   *          the bind to set
   * @throws Exception
   */
  public void setBind(String bind) throws Exception
  {
    this.bind = InetAddress.getByName(bind);
  }
  
  /**
   * @return the port
   */
  public int getPort()
  {
    return port;
  }
  
  /**
   * @param port
   *          the port to set
   */
  public void setPort(int port)
  {
    this.port = port;
  }
  
  /**
   * @return the locator
   */
  public Locator getLocator()
  {
    return locator;
  }
  
  /**
   * @param locator
   *          the locator to set
   */
  public void setLocator(Locator locator)
  {
    this.locator = locator;
  }
  
  /**
   * @return the log
   */
  public File getLog()
  {
    return logFile;
  }
  
  /**
   * @param log
   *          the log to set
   */
  public void setLog(File log)
  {
    this.logFile = log;
  }
  
  /**
   * @return the state
   */
  public File getState()
  {
    return state;
  }
  
  /**
   * @param state
   *          the state to set
   */
  public void setState(File state)
  {
    this.state = state;
  }
  
  /**
   * @return the peerLocator
   */
  public boolean isPeerLocator()
  {
    return peerLocator;
  }
  
  /**
   * @param peerLocator
   *          the peerLocator to set
   */
  public void setPeerLocator(boolean peerLocator)
  {
    this.peerLocator = peerLocator;
  }
  
  /**
   * @return the serverLocator
   */
  public boolean isServerLocator()
  {
    return serverLocator;
  }
  
  /**
   * @param serverLocator
   *          the serverLocator to set
   */
  public void setServerLocator(boolean serverLocator)
  {
    this.serverLocator = serverLocator;
  }
  
  /**
   * @return the hostnameForClients
   */
  public String getHostnameForClients()
  {
    return hostnameForClients;
  }
  
  /**
   * @param hostnameForClients
   *          the hostnameForClients to set
   */
  public void setHostnameForClients(String hostnameForClients)
  {
    this.hostnameForClients = hostnameForClients;
  }
  
  /**
   * @return the locators
   */
  public String getLocators()
  {
    return locators;
  }
  
  /**
   * @param locators
   *          the locators to set
   */
  public void setLocators(String locators)
  {
    this.locators = locators;
  }
  
  public Properties getProp()
  {
    return prop;
  }
  
  public void setProp(Properties prop)
  {
    this.prop = prop;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override
  public void afterPropertiesSet() throws Exception
  {
  }
  
}
