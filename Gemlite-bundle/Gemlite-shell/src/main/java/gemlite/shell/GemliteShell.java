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
package gemlite.shell;

import gemlite.core.internal.support.context.DomainMapperHelper;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.shell.support.GShellComponent;

import org.springframework.shell.Bootstrap;
import org.springframework.shell.event.ShellStatus;
import org.springframework.shell.event.ShellStatus.Status;
import org.springframework.shell.event.ShellStatusListener;

public class GemliteShell
{
  private Bootstrap bootstrap;
  private GShellComponent shell;
  public static void main(String[] args)
  {
    GemliteShell gshell = new GemliteShell();
    gshell.run(args);
  }
  protected void run(String[] args)
  {
    ServerConfigHelper.initConfig("env.properties");
    ServerConfigHelper.initLog4j("log4j-shell.xml");
    DomainMapperHelper.scanMapperRegistryClass();
    System.setProperty("jline.WindowsTerminal.directConsole", "false");
    String[] contextPath={ "gemlite-shell.xml" };
    bootstrap = new Bootstrap(args,contextPath);
    shell = (GShellComponent)bootstrap.getJLineShellComponent();
    shell.setDevelopmentMode(true);
    shell.addShellStatusListener(new ShellStatusListener()
    {
      @Override
      public void onShellStatusChange(ShellStatus oldStatus, ShellStatus newStatus)
      {
        if(newStatus.getStatus()==Status.STARTED)
        {
          DomainMapperHelper.scanMapperRegistryClass();
          shell.executeCommand("connect");
          shell.executeCommand("mn connect");
        }
          
      }
    });
    shell.start();
    shell.waitForComplete();
  }
}
