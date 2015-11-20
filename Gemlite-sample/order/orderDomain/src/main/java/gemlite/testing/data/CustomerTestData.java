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
//import gemlite.sample.order.domain.Customer;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * customerKÕpnž°
// * @author Administrator
// *
// */
//public class CustomerTestData extends TestDataImpl<Customer>
//{
//	/**
//	 * §7‹pnv™epn“
//	 */
//	@Override
//	public boolean CreateAndInsertData()
//	{
//		dataList = new ArrayList<Customer>();
//		for (int i = 1; i < 10; i++)
//		{
//			Customer customer = new Customer();
//			customer.setName("test_" + i);
//			customer.setId_num("32010119800918800" + i);
//			customer.setTelephone("1377662180" + i);
//			customer.setSex(1);
//			customer.setAge(20 + i);
//			dataList.add(customer);
//		}
//
//		return insertdata();
//	}
//
//	/**
//	 * ™epn“
//	 * :ÝÁÍ™edÇ(„/H dÒe¹
//	 * @return
//	 */
//	@Transactional
//	public boolean insertdata()
//	{
//		if (!isDbConnect())
//			return false;
//
//		String deleteSql = "delete from customer where name=? and id_num=? ";
//		JdbcTemplate template = dbManager.getJdbcTemplate();
//		template.batchUpdate(deleteSql, new BatchPreparedStatementSetter()
//		{
//
//			@Override
//			public void setValues(PreparedStatement ps, int i)
//					throws SQLException
//			{
//				Customer customer = dataList.get(i);
//				ps.setString(1, customer.getName());
//				ps.setString(2, customer.getId_num());
//			}
//
//			@Override
//			public int getBatchSize()
//			{
//				return dataList.size();
//			}
//		});
//
//		int[] result = template.batchUpdate(getInsertSql(),
//				new BatchPreparedStatementSetter()
//				{
//
//					@Override
//					public void setValues(PreparedStatement ps, int i)
//							throws SQLException
//					{
//						Customer customer = dataList.get(i);
//						ps.setString(1, customer.getName());
//						ps.setString(2, customer.getId_num());
//						ps.setString(3, customer.getTelephone());
//						ps.setInt(4, customer.getSex());
//						ps.setInt(5, customer.getAge());
//					}
//
//					@Override
//					public int getBatchSize()
//					{
//						return dataList.size();
//					}
//
//				});
//
//		System.out.println("Table [Customer] insert data finished.");
//		return true;
//	}
//
//	public static void main(String[] args)
//	{
//		CustomerTestData insetData = new CustomerTestData();
//		insetData.CreateAndInsertData();
//
//	}
//
//	@Override
//	public void setInsertSql()
//	{
//		intSqlTemplate = "insert into customer(name,id_num,telephone,sex,age) values (?,?,?,?,?)";
//	}
//
//	@Override
//	public String getInsertSql()
//	{
//		return intSqlTemplate;
//	}
//	
//	@Override
//	public Class<Customer> getDomainClass()
//	{
//		return Customer.class;
//	}
//
//}
