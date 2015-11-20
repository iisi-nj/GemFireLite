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
package gemlite.shell.commands;

import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.util.LogUtil;
import gemlite.shell.admin.dao.AdminDao;
import gemlite.shell.common.GemliteStatus;
import gemlite.shell.service.batch.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.client.ClientCacheFactory;

@Component
public class Client extends AbstractCommand
{
  @Autowired
  private ClientService clientService;
  
  @Autowired
  private AdminDao adminDao;
  
  @CliAvailabilityIndicator({ "connect" })
  public boolean isCommandAvailable()
  {
    return true;
  }
  
  @CliCommand(value = "connect", help = "connect")
  public void connect()
  {
   
    if (GemliteStatus.isConnected())
      LogUtil.getCoreLog().info("Aready connect to:" + clientService.getLocators());
    clientService.connect();
    if (!JpaContext.isContextInitialized())
      JpaContext.init();
    
    if (GemliteStatus.isConnected())
    {
      LogUtil.getCoreLog().info("Connected to locator:"+clientService.getLocators());
      adminDao.setClientPool(ClientCacheFactory.getAnyInstance().getDefaultPool());
    }
  }
  
  public boolean isConnect()
  {
      return GemliteStatus.isConnected();
  }
  
}
