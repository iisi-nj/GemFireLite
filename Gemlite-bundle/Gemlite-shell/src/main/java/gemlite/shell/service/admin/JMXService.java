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
package gemlite.shell.service.admin;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.stereotype.Component;

@Component
public class JMXService
{
  class CachedBeanInfo
  {
    ObjectName oname;
    Set<String> attributes;
    Map<String, String[]> operSignatures;
  }
  
  private MBeanServerConnection serverConnection;
  private Map<String, CachedBeanInfo> beanInfoCache;
  
  public JMXService()
  {
    beanInfoCache = new HashMap<>();
  }
  
  public synchronized MBeanServerConnection getConnection()
  {
	  return serverConnection;
  }
  
  private synchronized CachedBeanInfo getCachedBeanInfo(String name)
  {
    try
    {
      CachedBeanInfo cacheItem = beanInfoCache.get(name);
      if (cacheItem == null)
      {
        cacheItem = new CachedBeanInfo();
        cacheItem.oname = new ObjectName(name);
        MBeanInfo info = serverConnection.getMBeanInfo(cacheItem.oname);
        if (info == null)
        {
          return null;
        }
        Map<String, String[]> signatureMap = new HashMap<>();
        cacheItem.operSignatures = signatureMap;
        for (MBeanOperationInfo oper : info.getOperations())
        {
          String[] opNames = null;
          if(oper.getSignature().length>0)
            opNames = new String[oper.getSignature().length];
          for (int i = 0; i < oper.getSignature().length; i++)
          {
            opNames[i] = (oper.getSignature()[i]).getType();// .getName();
          }
          signatureMap.put(oper.getName(), opNames);
        }
        cacheItem.attributes = new HashSet<>();
        for (MBeanAttributeInfo attr : info.getAttributes())
        {
          cacheItem.attributes.add(attr.getName());
        }
      }
      return cacheItem;
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("MBean:" + name, e);
    }
    return null;
  }
  
  public void connect(String ip,int port)
  {
    if (serverConnection == null)
    {
        doConnect(ip, port);
    }
    //判断是否因为服务断开需要的重连
    else
    {
        boolean needReConnect = false;
        try
        {
            //测试是否需要重连
            serverConnection.getMBeanCount();
        }
        catch (Exception e) {
            needReConnect = true;
        }
        
        if(needReConnect)
            doConnect(ip, port);
        else
            LogUtil.getCoreLog().info("Aready connect to JMX: " + ip + ":" + port);
    }
  }
  
  private void doConnect(String ip,int port)
  {

      if(port<=0)
      {
        port = ServerConfigHelper.getInteger("config.jmx.port");
      }
      if("localhost".equals(ip))
      {
        InetAddress addr;
        try
        {
          addr = InetAddress.getLocalHost();
          ip = addr.getHostAddress();
        }
        catch (UnknownHostException e)
        {
          LogUtil.getCoreLog().info("IP:'localhost' error");
          LogUtil.getCoreLog().error(ip,e);
        }
      }
      try
      {
        //JMXServiceURL url = new JMXServiceURL(null, ip, port);
        String jndiPath = "/jmxconnector";
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+ip+":" + port + jndiPath);
        JMXConnector connector = JMXConnectorFactory.connect(url);
        serverConnection = connector.getMBeanServerConnection();
        LogUtil.getCoreLog().info("Connect to jmx manager "+ip+":"+port);
      }
      catch (IOException e)
      {
        LogUtil.getCoreLog().error(ip + ":" + port, e.getMessage());
        LogUtil.getCoreLog().info("Connected to jmx manager "+ip+":"+port+" failed");
      }
    
  }
  
  public Object invokeOperation(String beanName, String operation)
  {
    Object[] nullArgs= null;
    return invokeOperation(beanName, operation,nullArgs);
  }
  public Object invokeOperation(String beanName, String operation, Object... params)
  {
    if (serverConnection == null)
    {
      LogUtil.getCoreLog().info("ServerConnection is null");
      return null;
    }
    CachedBeanInfo info = getCachedBeanInfo(beanName);
    if (info == null)
    {
      LogUtil.getCoreLog().info("No " + beanName + " support");
      return null;
    }
    try
    {
      String[] signature = info.operSignatures.get(operation);
      Object obj = serverConnection.invoke(info.oname, operation, params, signature);
      return obj;
    }
    catch (ReflectionException | InstanceNotFoundException | MBeanException | IOException e)
    {
      LogUtil.getCoreLog().error(beanName + " " + operation, e);
    }
    return null;
  }
  
  public Set<ObjectInstance> listMBeans()
  {
      String name = "Gemlite:type=service,name=*";
      if (serverConnection == null)
      {
        LogUtil.getCoreLog().info("ServerConnection is null");
        return null;
      }
      try
      {
        ObjectName objName = new ObjectName(name);
        Set<ObjectInstance> obj = serverConnection.queryMBeans(objName, null);
        return obj;
      }
      catch (Exception e)
      {
        LogUtil.getCoreLog().error("queryMBeans", e);
      }
      return null;
  }
  
  public Object getAttribute(String beanName, String prop)
  {
    if (serverConnection == null)
    {
      LogUtil.getCoreLog().info("ServerConnection is null");
      return null;
    }
    CachedBeanInfo info = getCachedBeanInfo(beanName);
    if (info == null)
    {
      LogUtil.getCoreLog().info("No " + beanName + " support");
      return null;
    }
    try
    {
      return serverConnection.getAttribute(info.oname, prop);
    }
    catch (ReflectionException | InstanceNotFoundException | MBeanException | IOException | AttributeNotFoundException e)
    {
      LogUtil.getCoreLog().error(beanName + " " + prop, e);
    }
    return null;
  }
  
  /**
   * 取属性列表数据
   * @param beanName
   * @param attributes
   * @return
   */
//  public AttributeList getAttributes(String beanName)
//  {
//      if (serverConnection == null)
//      {
//        LogUtil.getCoreLog().info("ServerConnection is null");
//        return null;
//      }
//      CachedBeanInfo info = getCachedBeanInfo(beanName);
//      if (info == null)
//      {
//        LogUtil.getCoreLog().info("No " + beanName + " support");
//        return null;
//      }
//      try
//      {
//        //将attributes转成String[]
//        String[] attrs = new String[info.attributes.size()];
//        int i = 0;
//        for(String att:info.attributes)
//        {
//            attrs[i++] = att;
//        }
//        return serverConnection.getAttributes(info.oname, attrs);
//      }
//      catch (ReflectionException | InstanceNotFoundException | IOException e)
//      {
//        LogUtil.getCoreLog().error(beanName, e);
//      }
//      return null;
//  }
  
  
  @SuppressWarnings("unchecked")
  public Map<String,Object> getAttributesValues(String beanName)
  {
    return (Map<String,Object>)invokeOperation(beanName, "getAttributesValues");
  }
}
