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
package launcher;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class test1F implements Function
{

  @Override
  public boolean hasResult()
  {
    return true;
  }

  @Override
  public void execute(FunctionContext context)
  {
     String ss=ServerConfigHelper.getProperty("MEMBER_ID");
     context.getResultSender().lastResult(ss);
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
