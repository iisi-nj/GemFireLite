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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class IndexKeySet
{
	private String regionName;
	//indexName->{indexKeyFieldName, indexKeyFieldType}
	private Map<String, Map<String, String>> indexKeyMap = new HashMap<String, Map<String, String>>();
	
	public void putIndexKeyFields(String indexName, Map<String, String> fieldMap)
	{
		indexKeyMap.put(indexName, fieldMap);
	}
	
	public Map<String, String> getIndexKeyFields(String indexName)
	{
		return indexKeyMap.get(indexName);
	}

	public String getRegionName()
	{
		return regionName;
	}

	public void setRegionName(String regionName) 
	{
		this.regionName = regionName;
	}
	
	public boolean contains(String fieldName)
	{
		for(Iterator it = indexKeyMap.values().iterator(); it.hasNext();)
		{
			Map<String, String> fieldsMap = (Map<String, String>) it.next();
			if(fieldsMap.keySet().contains(fieldName))
				return true;
		}
		
		return false;
	}
}
