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
package gemlite.shell.service.batch;

import gemlite.core.api.SimpleClient;
import gemlite.shell.common.GemliteStatus;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolManager;

@Component
public class ClientService
{
  private String locators;
  
  public void connect()
  {
    if (!GemliteStatus.isConnected())
    {
      boolean success = SimpleClient.connect(false);
      if (success)
      {
        Iterator<Pool> it = PoolManager.getAll().values().iterator();
        StringBuilder su = new StringBuilder("Pool:");
        while (it.hasNext())
        {
          Pool pool = it.next();
          List<InetSocketAddress> locators = pool.getLocators();
          for (InetSocketAddress addr : locators)
          {
            su.append(addr.getHostString()).append(":").append(addr.getPort()).append(",");
          }
        }
        locators = su.toString();
        GemliteStatus.setConnected(true);
      }
      
    }
  }
  
  public void disconnect()
  {
    SimpleClient.disconnect();
    GemliteStatus.setConnected(false);
  }
  
  public String getLocators()
  {
    return locators;
  }
}
