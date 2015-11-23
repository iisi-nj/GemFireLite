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
package gemlite.shell.commands.admin;

import gemlite.shell.common.GemliteStatus;
import gemlite.shell.service.admin.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
@Component
public class Clean extends AbstractAdminCommand
{
  @Autowired
  private AdminService service;
  
  @CliAvailabilityIndicator({ "clean" })
  public boolean isCommandAvailable()
  {
    return GemliteStatus.isConnected();
  }
  
  @CliCommand(value = "clean", help = "clear data")
  public String execute(@CliOption(key="region",mandatory=true,optionContext="disable-string-converter param.context.region") String name)
  {
    return service.doClean(name);
  }
}
