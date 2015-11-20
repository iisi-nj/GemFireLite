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
package gemlite.core.api.index;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public interface IndexEntrySet<K,V>
{
	public K mapperKey(Map<String,Object> map);
	public Iterator<K> getKeyIterator();
	public Iterator<V> getValueIterator();
	public V getValue(K key);
	public V getValue(K key, IndexActionCallback<V> callback);
	public Map<K, V> getValues(Collection<K> keys);
	public Map<K, V> getValues(Collection<K> keys, IndexActionCallback<V> callback);

	public V getBetweenValue(K fromKey,boolean fromInclusive,K toKey,boolean toInclusive);
	public V getLessValue(K toKey, boolean inclusive);
	public V getBiggerValue(K fromKey, boolean inclusive);
	public V getLikeValue(K key);
}
