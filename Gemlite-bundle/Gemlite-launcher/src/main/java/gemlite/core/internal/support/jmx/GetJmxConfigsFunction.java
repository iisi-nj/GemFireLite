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
package gemlite.core.internal.support.jmx;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class GetJmxConfigsFunction implements Function
{

  /**
   * 
   */
  private static final long serialVersionUID = 585076363266102507L;

  @Override
  public boolean hasResult()
  {
    return true;
  }

  @Override
  public void execute(FunctionContext fc)
  {
    fc.getResultSender().lastResult(GemliteNode.getInstance().getJmxConfig());
  }

  @Override
  public String getId()
  {
    return getClass().getName();
  }

  @Override
  public boolean optimizeForWrite()
  {
    return false;
  }

  @Override
  public boolean isHA()
  {
    return false;
  }
  
}