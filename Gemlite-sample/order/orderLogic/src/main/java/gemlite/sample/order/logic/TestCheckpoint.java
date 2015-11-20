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
import gemlite.core.api.index.IndexEntrySet;
import gemlite.core.api.logic.AbstractRemoteService;
import gemlite.core.api.logic.LogicServices;
import gemlite.sample.order.domain.Customer;
import gemlite.sample.order.domain.CustomerDBKey;

import java.util.List;
import java.util.Set;

@RemoteService(name = "TestCheckpoint")
public class TestCheckpoint extends AbstractRemoteService<List<Customer>>
{
  
  public List<Customer> doExecute(Object userArgs, Set<Object> filters)
  {
    CustomerDBKey k = new CustomerDBKey();
    IndexEntrySet set = LogicServices.getIndexData("d");
    f1();
    f2("dsd");
    LogicServices.getService("TestCheckpoint2");
    
    Tool1 tool = new Tool1();
    tool.f11();
    tool.f21("dd");
    return null;
  }
  
  private void f1()
  {
    TestCheckpoint2 cc = (TestCheckpoint2) LogicServices.getService("TestCheckpoint2");
    cc.doExecute(null, null);
  }
  
  private void f2(String n)
  {
    
    f3();
  }
  
  private void f3()
  {
    
  }
  
  private void f3(int k)
  {
    
  }
}
