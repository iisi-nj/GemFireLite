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
package gemlite.sample.order.logic;

import gemlite.core.annotations.logic.RemoteService;
import gemlite.core.api.index.clause.IndexClause;
import gemlite.core.api.logic.AbstractRemoteService;
import gemlite.core.api.logic.LogicServices;
import gemlite.sample.order.domain.Customer;
import gemlite.sample.order.domain.CustomerDBKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.Region;

@RemoteService(name="QueryByCustomerNameLike")
public class QueryByCustomerNameLike extends AbstractRemoteService<Object>
{
	public Object doExecute(Object userArgs, Set<Object> filters)
	{
		Region<CustomerDBKey, Customer> customerRegion = LogicServices.getRegion("customer");
		IndexClause idx = LogicServices.newIndexClause("CustomerIndex06");
		List<Customer> result = new ArrayList<Customer>();
		
		String name = (String)userArgs;
		if(StringUtils.isNotEmpty(name))
		{
			idx.column("name").like(name);
		}
		
		Set<CustomerDBKey> customerDBKeySet = idx.query(CustomerDBKey.class);

		if(customerDBKeySet != null)
		{
			for(CustomerDBKey key : customerDBKeySet)
			{
				Customer customer = customerRegion.get(key);
				result.add(customer);
			}
		}
		return result;
	}
}
