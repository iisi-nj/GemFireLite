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
package gemlite.shell.service.admin;

import java.util.List;

import gemlite.core.util.IndexUtil;
import gemlite.core.util.LogUtil;

import org.springframework.stereotype.Component;

@Component
public class IndexService
{
	public String printIndexValue(String indexName, String searchParamStr, int pageNumber, int pageSize)
	{
		try
		{
			LogUtil.getCoreLog().info("Query Index Name: " + indexName + ", Search Parameter: " + searchParamStr
										+ ", Page Number: " + pageNumber + ", Page Size: " + pageSize);
			StringBuffer sb = new StringBuffer();
			List list = IndexUtil.printIndexValue(indexName, searchParamStr, pageNumber, pageSize);
			for(int i=0; i<list.size(); i++)
				sb.append(list.get(i) + "\n");
			return sb.toString();
		}
		catch(Exception e)
		{
			LogUtil.getCoreLog().error("Query Index Name: " + indexName + ", Search Parameter: " + searchParamStr
					+ ", Page Number: " + pageNumber + ", Page Size: " + pageSize, e);
			return e.getMessage();
		}
	}
	
	public String describeIndex(String indexName)
	{
		try
		{
			LogUtil.getCoreLog().info("Describe Index: " + indexName);
			StringBuffer sb = new StringBuffer();
			List list = IndexUtil.describeIndex(indexName);
			for(int i=0; i<list.size(); i++)
				sb.append(list.get(i) + "\n");
			return sb.toString();
		}
		catch(Exception e)
		{
			LogUtil.getCoreLog().error("Describe Index: " + indexName,e);
			return e.getMessage();
		}
	}
	
	public String list(String regionName)
	{
		try
		{
			LogUtil.getCoreLog().info("List Index by region: " + regionName);
			StringBuffer sb = new StringBuffer();
			List list = IndexUtil.listIndex(regionName);
			for(int i=0; i<list.size(); i++)
				sb.append(list.get(i) + "\n");
			return sb.toString();
		}
		catch(Exception e)
		{
			LogUtil.getCoreLog().error("List Index by region: " + regionName,e);
			return e.getMessage();
		}
	}
	
	public String drop(String indexName)
	{
		try
		{
			LogUtil.getCoreLog().info("Drop Index:" + indexName);
			StringBuffer sb = new StringBuffer();
			List list = IndexUtil.dropIndex(indexName);
			for(int i=0; i<list.size(); i++)
				sb.append(list.get(i) + "\n");
			return sb.toString();
		}
		catch(Exception e)
		{
			LogUtil.getCoreLog().error("Drop Index:" + indexName,e);
			return e.getMessage();
		}
	}
	
	public String create(String clause)
	{
		try
		{
			LogUtil.getCoreLog().info("Index Definition:" + clause);
			StringBuffer sb = new StringBuffer();
			List list = IndexUtil.createIndex(clause);
			for(int i=0; i<list.size(); i++)
				sb.append(list.get(i) + "\n");
			
			return sb.toString();
		}
		catch(Exception e)
		{
			LogUtil.getCoreLog().error(clause,e);
			return e.getMessage();
		}
	}
}
