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
package gemlite.core.internal.index.func;

import gemlite.core.internal.index.IndexHelper;
import gemlite.core.internal.support.FunctionIds;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class DropIndexFunction implements Function
{
	private static final long serialVersionUID = -2604351396756876455L;

  @Override
  public boolean hasResult()
  {
    return true;
  }
  
  @Override
  public void execute(FunctionContext fc)
  {
	  String memberId = ServerConfigHelper.getProperty("MEMBER_ID");
	  String ip = ServerConfigHelper.getConfig(ITEMS.BINDIP);
	  String port = ServerConfigHelper.getProperty("JMX_RMI_PORT");
	  String msg = "MemberID: " + memberId + " IP: " + ip + " Port: " + port + " ";  
	  try
	  {
		  String indexName = (String) fc.getArguments();
		  IIndexContext context = IndexHelper.dropIndex(indexName);
		  String result = context != null ? msg+"drop successfully" : msg+"drop failed";
		  fc.getResultSender().lastResult(result);
	  }
	  catch (Exception e)
	  {
		  LogUtil.getCoreLog().error(msg+"drop failed", e);
		  fc.getResultSender().lastResult(msg+"drop failed");
		  fc.getResultSender().sendException(new GemliteException(e));
	  }
  }
  
  @Override
  public String getId()
  {
    return FunctionIds.DROP_INDEX_FUNCTION;
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
