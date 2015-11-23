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
package gemlite.core.internal.support.jpa.files.dao;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
interface GmBatchExtDao
{
  /**
   * 计算有几个job
   * 
   * @return
   */
  public int countJobExecutions();
  
  /**
   * 取出执行的job
   * 
   * @return
   */
  public List<Map> queryJobExecutions(String status);
  
  /**
   * 查询取出正在执行的job个数
   * @return
   */
  public int countRunningJob();
  
  /**
   * 取job执行明细
   * 
   * @param executionId
   * @return
   */
  public Map queryJobExecutionById(Long executionId);
  
  /**
   * 根据job名取step
   * 
   * @param jobName
   * @return
   */
  public List<Map> queryStepNamesForJob(String jobName);
  
  /**
   * 根据执行id取step
   * 
   * @param jobExecutionId
   * @return
   */
  public List<Map> queryStepExecutionsById(Long jobExecutionId);
}
