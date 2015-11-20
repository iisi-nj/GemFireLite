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
package gemlite.core.util;

import gemlite.core.internal.support.FunctionIds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

@SuppressWarnings("rawtypes")
public class IndexUtil
{
  public final static List createIndex(String clause)
  {
	  Execution ex = FunctionService.onServers(ClientCacheFactory.getAnyInstance().getDefaultPool());
	  ex = ex.withArgs(clause);
	  ResultCollector rc = ex.execute(FunctionIds.CREATE_INDEX_FUNCTION);
	  List rs = (List) rc.getResult();
	  return rs;
  }
  
  public final static List dropIndex(String indexName)
  {
	  Execution ex = FunctionService.onServers(ClientCacheFactory.getAnyInstance().getDefaultPool());
	  ex = ex.withArgs(indexName);
	  ResultCollector rc = ex.execute(FunctionIds.DROP_INDEX_FUNCTION);
	  List rs = (List) rc.getResult();
	  return rs;
  }
  
  public final static List listIndex(String regionName)
  {
	  Execution ex = FunctionService.onServers(ClientCacheFactory.getAnyInstance().getDefaultPool());
	  ex = ex.withArgs(regionName);
	  ResultCollector rc = ex.execute(FunctionIds.LIST_INDEX_FUNCTION);
	  List rs = (List) rc.getResult();
	  return rs;
  }

  public final static List describeIndex(String indexName)
  {
	  Execution ex = FunctionService.onServers(ClientCacheFactory.getAnyInstance().getDefaultPool());
	  ex = ex.withArgs(indexName);
	  ResultCollector rc = ex.execute(FunctionIds.DESCRIBE_INDEX_FUNCTION);
	  List rs = (List) rc.getResult();
	  return rs;
  }
  
  public final static List printIndexValue(String indexName, String searchParamStr, int pageNumber, int pageSize)
  {
	  Execution ex = FunctionService.onServers(ClientCacheFactory.getAnyInstance().getDefaultPool());
	  Map map = new HashMap();
	  map.put("indexName", indexName);
	  map.put("searchParam", searchParamStr);
	  map.put("pageNum", pageNumber);
	  map.put("pageSize", pageSize);
	  ex = ex.withArgs(map);
	  ResultCollector rc = ex.execute(FunctionIds.QUERY_INDEX_FUNCTION);
	  List rs = (List) rc.getResult();
	  return rs;
  }
}
