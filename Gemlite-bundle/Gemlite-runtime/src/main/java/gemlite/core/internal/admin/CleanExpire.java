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
package gemlite.core.internal.admin;

import com.gemstone.gemfire.cache.CustomExpiry;
import com.gemstone.gemfire.cache.ExpirationAction;
import com.gemstone.gemfire.cache.ExpirationAttributes;
import com.gemstone.gemfire.cache.Region.Entry;

public class CleanExpire implements CustomExpiry<Object, Object>
{
  
  @Override
  public void close()
  {
    
  }
  
  @Override
  public ExpirationAttributes getExpiry(Entry<Object, Object> arg0)
  {
    return new ExpirationAttributes(1, ExpirationAction.DESTROY);
  }
}
