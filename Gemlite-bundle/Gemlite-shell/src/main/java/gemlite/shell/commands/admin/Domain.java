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

import gemlite.shell.codegen.service.CodegenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;

@Component
public class Domain extends AbstractAdminCommand
{
  @Autowired
  private CodegenService service;
  
  @CliAvailabilityIndicator({ "codegen" })
  public boolean isCommandAvailable()
  {
    return true;
  }
  
  @CliCommand(value = "codegen", help = "generates Domain classes from your database schema")
  public void execute()
  {
    service.execute();
  }
  
  public void webExecute(String driver, String url, String user, String pwd, String name, String inputSchema,
      String includes, String excludes, String packageName, String directory) throws Exception
  {
    service.webExecute(driver, url, user, pwd, name, inputSchema, includes, excludes, packageName, directory);
  }
}
