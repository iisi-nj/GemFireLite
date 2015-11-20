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
import gemlite.sample.order.domain.Detail;
import gemlite.sample.order.domain.DetailKey;
import gemlite.sample.order.domain.OrderKey;
import gemlite.sample.order.domain.Ordermain;
import gemlite.sample.order.domain.Product;
import gemlite.sample.order.vo.DetailVO;
import gemlite.sample.order.vo.OrderVO;
import gemlite.sample.order.vo.OutputVO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.gemstone.gemfire.cache.Region;

@RemoteService(name = "QueryOrderByCustomerName")
public class QueryOrderByCustomerName extends AbstractRemoteService<List<OutputVO>>
{
  public List<OutputVO> doExecute(Object userArgs, Set<Object> filters)
  {
    String name = (String) userArgs;
    
    Region<Integer, Product> prodRegion = LogicServices.getRegion("product");
    Region<CustomerDBKey, Customer> customerRegion = LogicServices.getRegion("customer");
    Region<Integer, Ordermain> orderRegion = LogicServices.getColocatedRegion("customer", "ordermain");
    Region<Integer, Detail> detailRegion = LogicServices.getColocatedRegion("customer", "detail");
    
    IndexClause idx = LogicServices.newIndexClause("CustomerIndex01");
    IndexClause idx2 = LogicServices.newIndexClause("OrderIndex01");
    IndexClause idx3 = LogicServices.newIndexClause("DetailIndex01");
    
    idx.column("name", name);
    Set<CustomerDBKey> customerDBKeySet = getKeySet(idx);
    
    List<OutputVO> result = process(customerDBKeySet, customerRegion, name, idx2, orderRegion, detailRegion, idx3,
        prodRegion);
    
    return result;
  }
  
  private List<OutputVO> process(Set<CustomerDBKey> customerDBKeySet, Region<CustomerDBKey, Customer> customerRegion,
      String name, IndexClause idx2, Region<Integer, Ordermain> orderRegion, Region<Integer, Detail> detailRegion,
      IndexClause idx3, Region<Integer, Product> prodRegion)
  {
    List<OutputVO> result = new ArrayList<OutputVO>();
    for (Iterator<CustomerDBKey> it = customerDBKeySet.iterator(); it.hasNext();)
    {
      Customer customer = customerRegion.get(it.next());
      OutputVO outputVO = new OutputVO();
      outputVO.setName(name);
      outputVO.setId_num(customer.getId_num());
      outputVO.setAge(customer.getAge());
      outputVO.setSex(customer.getSex());
      outputVO.setTelephone(customer.getTelephone());
      
      List<OrderVO> orderVOList = new ArrayList<OrderVO>();
      idx2.column("name", name).column("id_num", customer.getId_num());
      Set<OrderKey> ordermainKeySet = getKeySet2(idx2);
      if (ordermainKeySet == null)
        continue;
      
      for (Iterator<OrderKey> it2 = ordermainKeySet.iterator(); it2.hasNext();)
      {
        Ordermain ordermain = (Ordermain) orderRegion.get(it2.next());
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderdate(ordermain.getOrderdate());
        orderVO.setSequence(ordermain.getSequence());
        
        idx3.column("ord_sequence", ordermain.getSequence());
        Set<DetailKey> detailKeySet = getKeySet3(idx3);
        List<DetailVO> detailList = new ArrayList<DetailVO>();
        for (Iterator<DetailKey> it3 = detailKeySet.iterator(); it3.hasNext();)
        {
          Detail detail = (Detail) detailRegion.get(it3.next());
          DetailVO detailVO = new DetailVO();
          detailVO.setSequence(detail.getSequence());
          detailVO.setProduct_quantity(detail.getProduct_quantity());
          Product prod = prodRegion.get(detail.getProduct_id());
          detailVO.setProduct(prod);
          detailList.add(detailVO);
        }
        
        orderVO.setDetails(detailList);
        orderVOList.add(orderVO);
      }
      outputVO.setOrders(orderVOList);
      result.add(outputVO);
    }
    return result;
  }
  
  private Set<CustomerDBKey> getKeySet(IndexClause idx)
  {
    return idx.query(CustomerDBKey.class);
  }
  
  private Set<OrderKey> getKeySet2(IndexClause idx2)
  {
    return idx2.query(OrderKey.class);
  }
  
  private Set<DetailKey> getKeySet3(IndexClause idx3)
  {
    return idx3.query(DetailKey.class);
  }
}
