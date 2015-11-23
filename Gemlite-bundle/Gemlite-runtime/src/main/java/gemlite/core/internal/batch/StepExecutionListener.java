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
package gemlite.core.internal.batch; 

import gemlite.core.util.LogUtil;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public class StepExecutionListener extends StepExecutionListenerSupport
{
  
  public ExitStatus afterStep(StepExecution stepExecution)
  {
    if (stepExecution.getReadCount() == 0)
    {
      LogUtil.getCoreLog().info("Job executed but readCount is 0 (No record or no one valid line)");
      return ExitStatus.FAILED;
    }
    return ExitStatus.COMPLETED;
  }
  
}
