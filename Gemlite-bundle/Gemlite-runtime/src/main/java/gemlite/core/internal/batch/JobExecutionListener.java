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

import gemlite.core.common.DateUtil;
import gemlite.core.util.LogUtil;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobExecutionListener extends JobExecutionListenerSupport
{
  @Override
  public void afterJob(JobExecution exec)
  {
    super.afterJob(exec);
    String name = exec.getJobParameters().getString("name");
    long cost = (exec.getEndTime().getTime()-exec.getStartTime().getTime())/1000;
    String s1 = DateUtil.format(exec.getCreateTime(), "HH:mm:ss.SSS");
    String s2 = DateUtil.format(exec.getEndTime(), "HH:mm:ss.SSS");
    LogUtil.getAppLog().info("Job:" + name + " finished.Cost:" + cost+" seconds.Start:"+s1+" end:"+s2);
  }
  
}