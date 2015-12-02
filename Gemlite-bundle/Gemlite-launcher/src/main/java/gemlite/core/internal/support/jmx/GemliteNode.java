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
package gemlite.core.internal.support.jmx;

import gemlite.core.internal.support.context.ManagementRegionHelper;
import gemlite.core.internal.support.hotdeploy.GemliteClassLoader;
import gemlite.core.internal.support.jmx.domain.GemliteNodeConfig;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import org.apache.commons.lang.math.NumberUtils;

public class GemliteNode
{
  
  private static GemliteNode inst = new GemliteNode();
  private GemliteNodeConfig jmxConfig;
  
  private GemliteNode()
  {
    jmxConfig = new GemliteNodeConfig();
  }
  
  public final static GemliteNode getInstance()
  {
    return inst;
  }
  
  /**
   * 记录ip，端口，设置classloader
   */
  public void afterDeploy()
  {
    String memberId = ServerConfigHelper.getProperty("MEMBER_ID");
    String jmxPort = ServerConfigHelper.getProperty("JMX_RMI_PORT");
    jmxConfig.setIp(ServerConfigHelper.getConfig(ITEMS.BINDIP));
    jmxConfig.setPort(NumberUtils.toInt(jmxPort));
    jmxConfig.setMemberId(memberId);
    jmxConfig.setNodeName(ServerConfigHelper.getConfig(ITEMS.NODE_NAME));
    ManagementRegionHelper.writeMemberJmxConfig(memberId, jmxConfig);
    LogUtil.getCoreLog().debug("MXBean agent start at {ip},jmxPort={},httpPort={}", jmxConfig.getIp(),
        jmxConfig.getPort());
    MBeanHelper.setClassLoader(GemliteClassLoader.getInstance());
  }
  
  public void registerBean(String name, Object bean)
  {
    registerModelMBean(name, bean);
  }
  
  public boolean unRegisterBean(String name)
  {
    boolean success = MBeanHelper.unregister(name);
    if (!success)
      return success;
    if (jmxConfig != null)
      jmxConfig.getBeanNames().remove(name);
    return true;
  }
  
  /***
   * on server, register mbean
   * 
   * @param name
   * @param bean
   * @return
   */
  protected boolean registerModelMBean(String name, Object bean)
  {
    boolean success = MBeanHelper.registerModelMBean(name, bean);
    if (!success)
      return success;
    if (jmxConfig != null)
      jmxConfig.getBeanNames().put(name, bean.getClass().getName());
    return true;
  }
  
  public GemliteNodeConfig getJmxConfig()
  {
    return jmxConfig;
  }
}
