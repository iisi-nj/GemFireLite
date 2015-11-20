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
package gemlite.core.internal.mq.server;

import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.mq.domain.ItemByKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;

public class MqSyncFunction implements Function
{

  private static final long serialVersionUID = -2277084651803851214L;

  @Override
  public boolean hasResult()
  {
    return true;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public void execute(FunctionContext fc)
  {
    RegionFunctionContext rc = (RegionFunctionContext) fc;
    Set<ItemByKey>  items = (Set<ItemByKey>)rc.getFilter();
    MqSyncExecutor executor = new MqSyncExecutor();
    Region r = rc.getDataSet();
    String tableName = DomainRegistry.regionToTable(r.getName());
    List<ItemByKey> list = new ArrayList<>();
    list.addAll(items);
    executor.processByOneKey(r, tableName, list);
   
    fc.getResultSender().lastResult("done");
  }

  @Override
  public String getId()
  {
    return MqSyncFunction.class.getName();
  }

  @Override
  public boolean optimizeForWrite()
  {
    return true;
  }

  @Override
  public boolean isHA()
  {
    return false;
  }
  
}
