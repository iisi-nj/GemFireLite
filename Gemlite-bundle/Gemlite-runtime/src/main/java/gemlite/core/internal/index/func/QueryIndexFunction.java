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
import gemlite.core.util.LogUtil;

import java.util.Map;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class QueryIndexFunction implements Function
{
	private static final long serialVersionUID = 5364831132519321378L;

	@Override
	public boolean hasResult()
	{
		return true;
	}
  
	@Override
	public void execute(FunctionContext fc)
	{
	  try
	  {
		  Map param = (Map) fc.getArguments();
		  String indexName = (String) param.get("indexName");
		  String searchParam = (String) param.get("searchParam");
		  int pageNum = (int) param.get("pageNum");
		  int pageSize = (int) param.get("pageSize");
		  String result = IndexHelper.printIndexValue(indexName, searchParam, pageNum, pageSize);
		  fc.getResultSender().lastResult(result);
	  }
	  catch (Exception e)
	  {
		  LogUtil.getCoreLog().error("", e);
		  fc.getResultSender().lastResult("");
		  fc.getResultSender().sendException(new GemliteException(e));
	  }
  }
  
  @Override
  public String getId()
  {
    return FunctionIds.QUERY_INDEX_FUNCTION;
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
