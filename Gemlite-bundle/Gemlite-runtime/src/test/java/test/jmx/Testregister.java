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
package test.jmx;

import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.core.internal.support.system.GemliteAgent;
import gemlite.core.util.Util;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBean;

public class Testregister
{
  public static void main(String[] args) throws MalformedObjectNameException
  {
    
    f1();
    try
    {
      System.setProperty("KK_HOME", "d:/tmp");
      MyBean s2 = new MyBean();
      ModelMBean mb= MBeanHelper.createModelMBean(s2);
      System.err.println(mb.getMBeanInfo());
      MBeanHelper.registerModelMBean("Gemlite:type=MyBean", mb);
      
      MBeanServer s =  ManagementFactory.getPlatformMBeanServer();
      ObjectInstance oo = s.getObjectInstance(new ObjectName("Gemlite:type=MyBean"));
      System.out.println(oo);
      GemliteAgent.getInstance().startHtmlAdapter(9092);
      
      mb.load();
      
      s2.setInt1(1);
      s2.setInt1(1);
      s2.setInt1(1);
      s2.setInt1(1);
      s2.setInt1(3);
      Thread.sleep(Long.MAX_VALUE);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  private static void f1()
  {
    System.out.println(Util.getCallingClassName()+"."+Util.getCallingMethodName());
  }
}
