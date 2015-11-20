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
import gemlite.core.internal.jmx.manage.KeyConstants.Indexs;
import gemlite.core.internal.support.FunctionIds;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.util.Iterator;
import java.util.Map;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class DescribeIndexFunction implements Function
{
  private static final long serialVersionUID = 2846040091357465065L;

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
	  StringBuffer sb = new StringBuffer();
	  sb.append(msg + "\n");
	  try
	  {
		  String indexName = (String) fc.getArguments();
		  Map indexInfoMap = IndexHelper.describeIndex(indexName);
		  if(indexInfoMap != null && indexInfoMap.size() > 0)
		  {		  
			String idxName = (String) indexInfoMap.get(Indexs.indexName.name());
			String regionName = (String) indexInfoMap.get(Indexs.regionName.name());
			String regionType = (String) indexInfoMap.get(Indexs.regionType.name());
			long entrySize = (long) indexInfoMap.get(Indexs.entrySize.name());
			Map<String,String> keyFields = (Map<String, String>) indexInfoMap.get(Indexs.keyFields.name());
			Map<String,String> valueFields = (Map<String, String>) indexInfoMap.get(Indexs.valueFields.name());
			
			sb.append("-------------------------------\n");
			sb.append("Index Name: " + idxName + "\n");
			sb.append("Region Name: " + regionName + "\n");
			sb.append("Region Type: " + regionType + "\n");
			sb.append("Index Entry Size: " + entrySize + "\n");
			sb.append("\nIndex Key Fields:\n");
			int pos = 1;
			for(Iterator<String> it=keyFields.keySet().iterator(); it.hasNext();)
			{
				String field = it.next();
				String type = keyFields.get(field);
				sb.append(pos + ": " + field + "(" + type + ")\n");
				pos++;
			}
			
			pos = 1;
			sb.append("\nIndex Value Fields:\n");
			for(Iterator<String> it=valueFields.keySet().iterator(); it.hasNext();)
			{
				String field = it.next();
				String type = valueFields.get(field);
				sb.append(pos + ": " + field + "(" + type + ")\n");
				pos++;
			}
		  }
		  else
		  {
			  sb.append("Not supported yet.\n");
		  }
		  
		  fc.getResultSender().lastResult(sb.toString());
    }
    catch (Exception e)
    {
    	LogUtil.getCoreLog().error(sb.toString(), e);
    	fc.getResultSender().lastResult(sb.toString());
    	fc.getResultSender().sendException(new GemliteException(e));
    }
  }
  
  @Override
  public String getId()
  {
    return FunctionIds.DESCRIBE_INDEX_FUNCTION;
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
