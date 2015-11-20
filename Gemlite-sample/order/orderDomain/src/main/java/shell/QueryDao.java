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

import java.util.List;

import gemlite.core.api.logic.LogicServices;
import gemlite.core.api.logic.RemoteResult;
import gemlite.core.internal.testing.benchmark.IQueryDao;
import gemlite.sample.order.vo.DetailVO;
import gemlite.sample.order.vo.OrderVO;
import gemlite.sample.order.vo.OutputVO;

public class QueryDao implements IQueryDao
{
  public void doQuery(boolean show, String... args)
  {
    doQueryOrderByCustomer(args[0]);
  }
  
  private Object doQueryOrderByCustomer(String name)
  {
    RemoteResult rr = LogicServices.createRequestWithFilter("customer", "QueryOrderByCustomerName", name, name);
    List<OutputVO> outputVOList = rr.getResult(List.class);
    for (int i = 0; i < outputVOList.size(); i++)
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
    for (int i = 0; i < list.size(); i++)
    {
      OrderVO orderVO = list.get(i);
      System.out.println("=====>Order Sequence: " + orderVO.getSequence());
      System.out.println("=====>Order Date: " + orderVO.getOrderdate());
      List<DetailVO> details = orderVO.getDetails();
      for (int j = 0; j < details.size(); j++)
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
  
}
