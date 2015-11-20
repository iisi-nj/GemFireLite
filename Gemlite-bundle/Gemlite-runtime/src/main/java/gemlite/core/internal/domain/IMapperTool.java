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
package gemlite.core.internal.domain;

import java.util.List;

public interface IMapperTool<K, V>
{
  public boolean isFullColumnKey();
  
  public V mapperValue(IDataSource from);
  
  /***
   * MQ merge
   * 
   * @param from
   * @param oldValue
   * @return
   */
  public V mapperValue(IDataSource from, V oldValue);
  
  /***
   * MQ
   * 
   * @param from
   * @param oldValue
   * @return
   */
  public K mapperKey(IDataSource from);
  
  public K value2Key(V domain);
  
  public Class<K> getKeyClass();
  public Class<V> getValueClass();
  /***
   * ArrayList	˘a-^'öIzè
   * @return
   */
  public List<String> getValueFieldNames();
  /***
   * ArrayList	˘a-^'öIzè
   * @return
   */
  public List<String> getKeyFieldNames();
}
