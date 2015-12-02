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

import gemlite.core.util.LogUtil;

import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBean;

import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;

public class MBeanHelper
{
  private final static GemliteMBeanExporter exp;
  static
  {
    exp = new GemliteMBeanExporter();
    Map<String, Object> beans = new HashMap<>();
    exp.setAutodetect(true);
    exp.setBeans(beans);
    MetadataMBeanInfoAssembler assembler = new MetadataMBeanInfoAssembler();
    assembler.setUseStrictCasing(false);
    assembler.setAttributeSource(new AnnotationJmxAttributeSource());
    exp.setNamingStrategy(new MetadataNamingStrategy());
    
    exp.setAssembler(assembler);
  }
  
  public final static void setClassLoader(ClassLoader loader)
  {
    exp.setBeanClassLoader(loader);
  }
  
  public final static boolean unregister(String name)
  {
    try
    {
      if (exp.getServer() == null)
      {
        exp.setServer(ManagementFactory.getPlatformMBeanServer());
      }
      ObjectName oname = new ObjectName(name);
      if (exp.getServer().isRegistered(oname))
      {
        exp.unregisterManagedResource(oname);
        
      }
      return true;
    }
    catch (MalformedObjectNameException e)
    {
      LogUtil.getCoreLog().debug("Bean:" + name, e);
    }
    return false;
  }
  public final static boolean registerModelMBean(String name, Object bean)
  {
    try
    {
      if (exp.getServer() == null)
      {
        exp.setServer(ManagementFactory.getPlatformMBeanServer());
      }
      ObjectName oname = new ObjectName(name);
      if (exp.getServer().isRegistered(oname))
      {
        LogUtil.getCoreLog().debug("MBean:" + name + " has registered");
      }
      else
        exp.registerManagedResource(bean, oname);
      return true;
    }
    catch (MalformedObjectNameException e)
    {
      LogUtil.getCoreLog().debug("Bean:" + name, e);
    }
    return false;
    
  }
  public final static ModelMBean createModelMBean(Object bean)
  {
    return exp.createAndConfigureMBean(bean, bean.getClass().getName());
  }
  
  public final static Object createProxy(InvocationHandler handler)
  {
    return Proxy.newProxyInstance(null, new Class[]{ModelMBean.class}, handler);
  }
  
  /**
   * 创建逻辑service类 MbeanName 只为逻辑架包服务
   * @param name
   * @return
   */
  public final static String createServiceMBeanName(String name)
  {
    StringBuilder builder = new StringBuilder();
    
    builder.append("Gemlite:type=service,name="+name);
    
    return builder.toString();
  }
  
  /**
   * 创建管理类MbeanName 类型是 manager
   * @param name
   * @return
   */
  public final static String createManagerMBeanName(String name)
  {
    StringBuilder builder = new StringBuilder();
    
    builder.append("Gemlite:type=manager,name="+name);
    
    return builder.toString();
  }
  
  /**
   * 通用类MbeanName 创建
   * @param type
   * @param name
   * @return
   */
  public final static String createMBeanName(String type, String name)
  {
    StringBuilder builder = new StringBuilder();
    
    builder.append("Gemlite:type="+type+",name="+name);
    
    return builder.toString();
  }
}
