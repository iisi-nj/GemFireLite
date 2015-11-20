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
package gemlite.core.internal.testing.generator.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReusePool
{
	private String table;
	private List<Map<String, Map<String, String>>> keySet = new ArrayList<Map<String, Map<String, String>>>();                 
	private List<Map<String, String>> idxKeySet = new ArrayList<Map<String, String>>();
	
	public String getTable()
	{
		return table;
	}
	public void setTable(String table)
	{
		this.table = table;
	}
	
	public void addUsedKey(Map<String, String> key, Map<String, String> value)
	{
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		map.put("key", key);
		map.put("value", value);
		keySet.add(map);
	}
	
	public Map<String, Map<String, String>> getUsedKey()
	{
		Random r = new Random();
		return getUsedKey(r.nextInt(keySet.size()));
	}
	
	public Map<String, Map<String, String>> getUsedKey(int pos)
	{
		return keySet.get(pos);
	}
	
	public void addUsedIndexKey(Map<String, String> idxKey)
	{
		idxKeySet.add(idxKey);
	}
	
	public Map<String, String> getUsedIdxKey()
	{
		Random r = new Random();
		return getUsedIdxKey(r.nextInt(idxKeySet.size()));
	}
	
	public Map<String, String> getUsedIdxKey(int pos)
	{
		return idxKeySet.get(pos);
	}
}
