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
package gemlite.core.internal.mq.sender;

import gemlite.core.internal.mq.domain.ItemByRegion;

import java.util.concurrent.Callable;

/**
 * @author ynd
 *
 */
public class SenderTask implements Callable<Boolean>
{
  private ItemByRegion item;
  private IDomainSender sender ;
  
  
  /**
   * @param pkg
   * @param parser
   */
  public SenderTask(ItemByRegion item, IDomainSender sender )
  {
    this.item = item;
    this.sender = sender;
  }


  @Override
  public Boolean call() throws Exception
  {
    Boolean bl = sender.doSend(item);
    return bl;
  }
  
}
