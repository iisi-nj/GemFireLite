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
import gemlite.core.api.logic.RemoteResult;
import gemlite.core.internal.logic.IRemoteService;
import gemlite.sample.order.domain.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RemoteService(name="GetNamesFromTel")
public class GetNamesFromTel extends AbstractRemoteService<List<Customer>>
{

	public List<Customer> doExecute(Object userArgs, Set<Object> filters)
	{
		String tel = (String)userArgs;
		IndexClause idx = LogicServices.newIndexClause("CustomerIndex04");
		idx.column("telephone", tel);
		Set<Integer> names = idx.query(Integer.class);
		if(names == null)
			return null;
		
//		return new ArrayList<Object>(names);
		IRemoteService rs = getRemoteService("QueryByCustomerTel");
		RemoteResult rr = rs.remoteExecute(tel, names);
		List<Customer> result = rr.getResult(List.class);

		return result;
	}
}
