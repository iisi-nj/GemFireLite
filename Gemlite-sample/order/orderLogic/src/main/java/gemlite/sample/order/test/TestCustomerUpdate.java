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
package gemlite.sample.order.test;

import gemlite.core.annotations.logic.RemoteService;
import gemlite.core.api.index.clause.IndexClause;
import gemlite.core.api.logic.AbstractRemoteService;
import gemlite.core.api.logic.LogicServices;
import gemlite.sample.order.domain.Customer;
import gemlite.sample.order.domain.CustomerDBKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.gemstone.gemfire.cache.Region;

@RemoteService(name="TestCustomerUpdate")
public class TestCustomerUpdate extends AbstractRemoteService<List<Customer>>
{
	public List<Customer> doExecute(Object userArgs, Set<Object> filters)
	{
		String name = (String)userArgs;
		Region<CustomerDBKey, Customer> customerRegion = LogicServices.getRegion("customer");
		Customer customer01 = new Customer();
		customer01.setName(name);
		customer01.setId_num("01");
		customer01.setTelephone("123456");
		CustomerDBKey dbkey01 = new CustomerDBKey();
		dbkey01.setId_num(customer01.getId_num());
		dbkey01.setName(customer01.getName());
		customerRegion.put(dbkey01, customer01);
		
		Customer customer02 = new Customer();
		customer02.setName(name);
		customer02.setId_num("02");
        customer02.setTelephone("123456");
		CustomerDBKey dbkey02 = new CustomerDBKey();
		dbkey02.setId_num(customer02.getId_num());
		dbkey02.setName(customer02.getName());
		
        customerRegion.put(dbkey02, customer02);
        
		IndexClause idx = LogicServices.newIndexClause("CustomerIndex01");
		idx.column("name", name);
		Set<CustomerDBKey> customerDBKeySet = idx.query(CustomerDBKey.class);
		List<Customer> list = new ArrayList<Customer>();
		
		for(CustomerDBKey key : customerDBKeySet)
		{
		    list.add(customerRegion.get(key));
		}
//        map.put("customerDBKeySet", customerDBKeySet);
//        map.put("valuseList", list);
		return list;
	}
}
