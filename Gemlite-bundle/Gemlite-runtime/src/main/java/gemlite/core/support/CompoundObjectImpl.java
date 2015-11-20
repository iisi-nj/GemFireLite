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
package gemlite.core.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CompoundObjectImpl implements CompoundObject
{
	private Map<String, Object> map;
	
	public CompoundObjectImpl()
	{
		map = new HashMap<>();
	}

	@Override
	public CompoundObject addElement(String name, Object value) 
	{
		if(StringUtils.isNotEmpty(name) && value != null)
			map.put(name, value);
		return this;
	}
	
	@Override
	public <T> T valueOf(String name, Class<T> T) {
		Object result = map.get(name);
		if (result != null)
			return T.cast(result);

		return null;
	}
	
	@Override
	public int hashCode() 
	{
		return map.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompoundObjectImpl other = (CompoundObjectImpl) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}
	
	
}
