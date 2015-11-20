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
package gemlite.core.internal.support.context;

import gemlite.core.internal.support.jpa.EmbedServer;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JpaContext
{
  private static ClassPathXmlApplicationContext jpaContext;
  
  public synchronized static void init()
  {
    EmbedServer.processConnectionURL();
    if (jpaContext == null)
      jpaContext = Util.initContext("jpa/ds-jpa.xml");
  }
  
  public final static boolean isContextInitialized()
  {
    return jpaContext != null;
  }
  
  public final static <T> T getService(Class<T> cls)
  {
    if (jpaContext == null)
    {
      LogUtil.getCoreLog().warn(
          "You need do JpaContext.init() before useing! Now start init JpaContext,please wait a moment...");
      ServerConfigHelper.initConfig();
      try
      {
        jpaContext = Util.initContext("jpa/ds-jpa.xml");
        if (jpaContext != null)
          return jpaContext.getBean(cls);
      }
      catch (Exception e)
      {
        LogUtil.getCoreLog().error("", e);
      }
      
    }
    
    if (jpaContext != null)
    {
      try
      {
        T bean = jpaContext.getBean(cls);
        return bean;
      }
      catch (Exception e)
      {
        LogUtil.getCoreLog().error("", e);
      }
    }
    
    return null;
  }
  
  public final static synchronized void close()
  {
    if (jpaContext != null)
      jpaContext.close();
  }
}
