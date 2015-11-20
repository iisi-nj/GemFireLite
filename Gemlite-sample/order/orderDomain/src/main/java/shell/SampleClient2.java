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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;

public class SampleClient2
{
	@Option(name = "-name")
	private String name;
	
	@Option(name = "-min")
	private int min;
	
	@Option(name = "-max")
	private int max;
	
	public static void main(String[] args) throws Exception 
	{
		SimpleClient.connect(); 
		SampleClient2 dc = new SampleClient2(); 
		CmdLineParser parser = new CmdLineParser(dc);
		parser.parseArgument(args);
//		dc.doQueryByCustomer();
		
//		ClientCache c =ClientCacheFactory.getAnyInstance();
//		Region r = c.getRegion("customer");		
//		CustomerDBKey key = new CustomerDBKey();
//		key.setName("Johnson");
//		key.setId_num("100200200901010103");
//		r.destroy(key);
//		
//		key.setId_num("320101198009188237");
//		key.setName("Croci");
//		r.destroy(key);
//		
//		key.setId_num("100200200901010102");
//		key.setName("LiuFenh");
//		r.destroy(key);
//		
//		key.setId_num("320303197208083283");
//		key.setName("Mike");
//		r.destroy(key);
//		
//		key.setId_num("100200200901010101");
//		key.setName("Johnson");
//		r.destroy(key);
//		
//		key.setId_num("980378198206045789");
//		key.setName("YangDong");
//		r.destroy(key);
//		
//		key.setId_num("321090195506058934");
//		key.setName("Alex");
//		r.destroy(key);
//		
//		key.setId_num("283982198808082882");
//		key.setName("SuHai");
//		r.destroy(key);
//		
//		key.setId_num("238087199909092345");
//		key.setName("Helen");
//		r.destroy(key);
//		
//		key.setId_num("320106195609013289");
//		key.setName("Jane");
//		r.destroy(key);
//		
//		key.setId_num("320106200805293093");
//		key.setName("Emma");
//		r.destroy(key);
//		
//		key.setId_num("100200200901010102");
//		key.setName("LiuFeng");
//		r.destroy(key);
//		
//		key.setId_num("124873197803032093");
//		key.setName("Brown");
//		r.destroy(key);
	
		SimpleClient.disconnect();
	}
	
	private void doQueryByCustomer()
	{
		if(StringUtils.isNotEmpty(name))
		{
			RemoteResult rr= LogicServices.createHeavyRequest("QueryByCustomerNameLike", name); 
			List<Customer> customer = rr.getResult(List.class);
			for(int i=0; i<customer.size(); i++)
				System.out.println(">>"+customer.get(i));	
		}
		else
		{
			Map map = new HashMap();
			if(min!=0)
				map.put("min", min);
			if(max!=0)
				map.put("max", max);
			
			RemoteResult rr= LogicServices.createHeavyRequest("QueryByCustomerAgeRange", map); 
			List<Customer> customer = rr.getResult(List.class);
			for(int i=0; i<customer.size(); i++)
				System.out.println(">>"+customer.get(i));	
			return ;
		}
	}
	
}
