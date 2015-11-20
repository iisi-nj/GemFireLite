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

import gemlite.core.internal.testing.benchmark.Run;

import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;


@Component
public class Benchmark extends AbstractCommand
{

  @CliAvailabilityIndicator({ "run" })
  public boolean isCommandAvailable()
  {
    return true;
  }
  
  @CliCommand(value = "run", help = "benchmark run")
  public void run(@CliOption(key = { "t", "threadsNum" }, unspecifiedDefaultValue = "1") String threadsNum,
          @CliOption(key = { "c", "queryCount" }, unspecifiedDefaultValue = "1") String queryCount,
          @CliOption(key = { "f", "testDataFile" }, mandatory = true) String testDataFile,
          @CliOption(key = { "q", "queryClass" }, mandatory = true) String queryClass,
          @CliOption(key = { "s", "show" }, mandatory = false,specifiedDefaultValue="false",unspecifiedDefaultValue="false") boolean show)
  {
      String args[] = new String[]{threadsNum,queryCount,testDataFile,queryClass};
      Run.execute(show,args);
  }
}
