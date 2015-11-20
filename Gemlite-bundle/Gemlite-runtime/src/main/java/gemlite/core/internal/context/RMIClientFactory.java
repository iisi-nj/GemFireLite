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
package gemlite.core.internal.context;

import java.io.IOException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class RMIClientFactory
{
  public static JMXConnector getClient1()
  {
    
    try
    {
      JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:2099/jmxrmi");
//      JMXServiceURL url = new JMXServiceURL(null,null,2099);
      JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
      
      System.out.println(url.toString());
      return jmxc;
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
    
  }
}