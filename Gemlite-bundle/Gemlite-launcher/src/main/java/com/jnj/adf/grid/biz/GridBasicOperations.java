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
package com.jnj.adf.grid.biz;

import java.util.Collection;
import java.util.Map;

/**
 * @author dyang39
 *
 * we need define the method for local grid and for global grid
 * the basic operation: insert/update/delete only for local grid
 * the query operation: pagination/fuzzy/others on lucene index, for local gir or global gird
 */
public interface GridBasicOperations {
	boolean containsKey(Object key);

	<K, V> V get(K key);

	<K, V> Map<K, V> getAll(Collection<?> keys);

	<K, V> V put(K key, V value);

	<K, V> void putAll(Map<? extends K, ? extends V> map);

	<K, V> V remove(K key);
	
	/***
	 * clear all
	 */
	void removeAll();
}
