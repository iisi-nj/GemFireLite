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
package gemlite.testing.data;
//
//import gemlite.testing.util.DBManager;
//
//import java.util.List;
//
//public class TestDataImpl<T> implements ITestData<T>
//{
//	protected DBManager dbManager;
//	protected List<T> dataList;
//	protected String intSqlTemplate;
//
//	public TestDataImpl()
//	{
//		dbManager = DBManager.getInstance();
//		setInsertSql();
//	}
//
//	@Override
//	public List<T> getDataList()
//	{
//		return dataList;
//	}
//
//	public boolean isDbConnect()
//	{
//		if (!dbManager.isUseable())
//		{
//			System.out.println("Connection database failed.");
//			return false;
//		}
//
//		return true;
//	}
//
//	@Override
//	public boolean CreateAndInsertData()
//	{
//		return false;
//	}
//
//	@Override
//	public void setInsertSql()
//	{
//		intSqlTemplate = "";
//		
//	}
//
//	@Override
//	public String getInsertSql()
//	{
//		return intSqlTemplate;
//	}
//
//	@Override
//	public Class<T> getDomainClass()
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
