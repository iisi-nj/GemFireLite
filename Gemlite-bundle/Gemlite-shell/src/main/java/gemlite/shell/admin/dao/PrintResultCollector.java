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
package gemlite.shell.admin.dao;

import java.util.concurrent.TimeUnit;

import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.distributed.DistributedMember;

@SuppressWarnings("rawtypes")
public class PrintResultCollector implements ResultCollector
{
  @Override
  public void addResult(DistributedMember arg0, Object arg1)
  {
    System.out.println(arg1);
  }

  @Override
  public Object getResult() throws FunctionException
  {
    return null;
  }

  @Override
  public Object getResult(long l, TimeUnit timeunit) throws FunctionException, InterruptedException
  {
    return null;
  }

  @Override
  public void endResults()
  {
    System.out.println("finish!");
  }

  @Override
  public void clearResults()
  {
    
  }
}
