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
package gemlite.core.internal.mq.sender;

import gemlite.core.internal.mq.domain.ItemByKey;
import gemlite.core.internal.mq.domain.ItemByRegion;
import gemlite.core.internal.mq.server.MqSyncFunction;

import java.util.HashSet;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

public class GFDomainSender implements IDomainSender
{

  @SuppressWarnings("rawtypes")
  @Override
  public Boolean doSend(Object obj)
  {
	ItemByRegion item = (ItemByRegion) obj;
    String regionName = item.regionName;
    Cache c = CacheFactory.getAnyInstance();
    Region r = c.getRegion(regionName);
    Execution ex =  FunctionService.onRegion(r);
    HashSet<ItemByKey> set = new HashSet<>( item.itemsByKey.values());
    ex = ex.withFilter(set);
    ResultCollector rc=  ex.execute(new MqSyncFunction().getId());
    rc.getResult();
    return null;
  }



  
}
