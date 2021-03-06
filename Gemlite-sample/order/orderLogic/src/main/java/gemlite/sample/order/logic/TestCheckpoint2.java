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
import gemlite.core.api.logic.AbstractRemoteService;
import gemlite.sample.order.domain.Customer;

import java.util.List;
import java.util.Set;

@RemoteService(name="TestCheckpoint2")
public class TestCheckpoint2 extends AbstractRemoteService<List<Customer>>
{

	public List<Customer> doExecute(Object userArgs, Set<Object> filters)
	{
	  f11();
	  f21("dsd");
		return null;
	}
	
	private void f11()
	{
	  
	}
	private void f21(String n){
	  f31();
	}
	private void f31()
	{
	  
	}
}
