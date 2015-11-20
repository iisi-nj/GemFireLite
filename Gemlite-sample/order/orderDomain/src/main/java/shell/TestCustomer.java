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
package shell;

import gemlite.core.api.SimpleClient;
import gemlite.core.api.logic.LogicServices;
import gemlite.core.api.logic.RemoteResult;
import gemlite.sample.order.domain.Customer;
import gemlite.sample.order.domain.CustomerDBKey;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class TestCustomer
{
	
	@Test
	public void testCase() throws Exception 
	{
		SimpleClient.connect(); 
		TestCustomer dc = new TestCustomer(); 
		String name = "jcaoooo";
		dc.testAdd(name);
		Thread.sleep(500);
		dc.testUpdate(name);
        Thread.sleep(500);
        dc.testDelete(name);
		SimpleClient.disconnect();
	}
	
	@SuppressWarnings("unchecked")
    private void testAdd(String name)
	{
		RemoteResult rr = LogicServices.createRequestWithFilter("customer", "TestCustomerAdd", name, name); 
		List<CustomerDBKey> list = rr.getResult(List.class);
		
		for(CustomerDBKey dbkey : list)
		{
		    System.out.println(dbkey);
		}
		 Assert.assertEquals(list.size(), 2);
	}
	
	@SuppressWarnings("unchecked")
    private void testUpdate(String name)
    {
        RemoteResult rr = LogicServices.createRequestWithFilter("customer", "TestCustomerUpdate", name, name); 
        List<Customer> valueList = rr.getResult(List.class);
        Assert.assertEquals(valueList.size(), 2);
        for(Customer cus : valueList)
        {
            Assert.assertEquals(cus.getTelephone(), "123456");
        }
    }
	
	@SuppressWarnings("unchecked")
    private void testDelete(String name)
    {
        RemoteResult rr = LogicServices.createRequestWithFilter("customer", "TestCustomerDelete", name, name); 
        List<CustomerDBKey> list = rr.getResult(List.class);
        
        for(CustomerDBKey dbkey : list)
        {
            System.out.println(dbkey);
        }
         Assert.assertEquals(list.size(), 0);
    }
	
}
