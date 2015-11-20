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
import gemlite.sample.order.vo.DetailVO;
import gemlite.sample.order.vo.OrderVO;
import gemlite.sample.order.vo.OutputVO;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class SampleClient
{
	@Option(name = "-name")
	private String name;
	
	@Option(name = "-id")
	private String id;
	
	@Option(name = "-f", usage="True for Query Order using Customer Name")
	public boolean flag;
	
	@Option(name = "-tel")
	private String tel;
	
	public static void main(String[] args) throws Exception 
	{
		SimpleClient.connect(); 
		SampleClient dc = new SampleClient(); 
		CmdLineParser parser = new CmdLineParser(dc);
		parser.parseArgument(args);
		
		if(dc.flag)
			dc.doQueryOrderByCustomer();
		else
			dc.doQueryByCustomer();
		
		SimpleClient.disconnect();
	}
	
	private Object doQueryOrderByCustomer()
	{
		RemoteResult rr = LogicServices.createRequestWithFilter("customer", "QueryOrderByCustomerName", name, name); 
		List<OutputVO> outputVOList = rr.getResult(List.class);
		for(int i=0; i<outputVOList.size(); i++)
			printOutputVO(outputVOList.get(i));
		
		return outputVOList;
	}
	
	private void printOutputVO(OutputVO vo)
	{
		System.out.println("########################################################");
		System.out.println("Customer Name: " + vo.getName());
		System.out.println("Customer ID Number: " + vo.getId_num());
		System.out.println("Customer Sex: " + vo.getSex());
		System.out.println("Customer Age: " + vo.getAge());
		
		List<OrderVO> list = vo.getOrders();
		for(int i=0; i<list.size(); i++)
		{
			OrderVO orderVO = list.get(i);
			System.out.println("=====>Order Sequence: " + orderVO.getSequence());
			System.out.println("=====>Order Date: " + orderVO.getOrderdate());
			List<DetailVO> details = orderVO.getDetails();
			for(int j=0; j<details.size(); j++)
			{
				DetailVO detailVO = details.get(j);
				System.out.println("=====>=====>Detail Sequence: " + detailVO.getSequence());
				System.out.println("=====>=====>Product ID: " + detailVO.getProduct().getProduct_id());
				System.out.println("=====>=====>Product Name: " + detailVO.getProduct().getName());
				System.out.println("=====>=====>Product Price: " + detailVO.getProduct().getPrice());
				System.out.println("=====>=====>Product Quantity: " + detailVO.getProduct_quantity());
			}

		}
		System.out.println("########################################################");
	}
	
	private void doQueryByCustomer()
	{
		if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(id))
		{
			Customer customer = queryByCustomerPK(name, id);
			System.out.println(">>"+customer);
			return;
		}
		
		if(StringUtils.isNotEmpty(name) && StringUtils.isEmpty(id))
		{
			List<Customer> customerList = queryByCustomerName(name);
			for(int i=0; i<customerList.size(); i++)
				System.out.println(">>"+customerList.get(i));
			return;
		}
		
		if(StringUtils.isNotEmpty(id))
		{
			List<Customer> customerList = queryByCustomerID(id);
			for(int i=0; i<customerList.size(); i++)
				System.out.println(">>"+customerList.get(i));
			return;
		}
		
		if(StringUtils.isNotEmpty(tel))
		{
			List<Customer> customerList = queryByCustomerTel(tel);
			for(int i=0; i<customerList.size(); i++)
				System.out.println(">>"+customerList.get(i));
		}
	}
	
	private List<Customer> queryByCustomerTel(String tel)
	{
		RemoteResult rr= LogicServices.createRequestWithFilter("CustomerIndex04", "GetNamesFromTel", tel, tel);
		if (rr == null)
			return new ArrayList<Customer>();
		
		List<Customer> customer = rr.getResult(List.class);
		return customer;
	}
	
	private List<Customer> queryByCustomerID(String id)
	{
		RemoteResult rr= LogicServices.createHeavyRequest("QueryByCustomerID", id); 
		List<Customer> customer = rr.getResult(List.class);
		
		return customer;
	}
	
	private Customer queryByCustomerPK(String name, String id)
	{
		CustomerDBKey key = new CustomerDBKey();
		key.setName(name);
		key.setId_num(id);
		RemoteResult rr= LogicServices.createRequestWithFilter("customer", "QueryByCustomerDBKey", key, key); 
		Customer customer = rr.getResult(Customer.class);
		
		return customer;
	}
	
	private List<Customer> queryByCustomerName(String name)
	{
		RemoteResult rr= LogicServices.createRequestWithFilter("customer", "QueryByCustomerName", name, name); 
		List<Customer> customer = rr.getResult(List.class);
		
		return customer;
	}
}
