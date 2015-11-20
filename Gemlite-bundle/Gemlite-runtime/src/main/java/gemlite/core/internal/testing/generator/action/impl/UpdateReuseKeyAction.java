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
package gemlite.core.internal.testing.generator.action.impl;

import gemlite.core.internal.testing.generator.action.OutputAction;
import gemlite.core.internal.testing.generator.pool.ReusePool;
import gemlite.core.internal.testing.generator.template.OutputTemplate;
import gemlite.core.internal.testing.generator.util.RandomUtil;
import gemlite.core.internal.testing.generator.util.TableUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class UpdateReuseKeyAction implements OutputAction
{
	@Override
	public Object doAct(OutputTemplate template, Object argument)
	{
		String updateTemplate = template.getTemplate(TableUtil.UPDATE);
		if(StringUtils.isEmpty(updateTemplate))
			return null;
		
		ReusePool pool = (ReusePool) argument;
		Map<String, Map<String, String>> usedObj = pool.getUsedKey();
		Map<String, String> usedKey = usedObj.get("key");
		Map<String, String> usedValue = usedObj.get("value");
		Set<String> usedKeySet = usedKey.keySet();
		
		Map<String, String> usedIdxKey = new HashMap<String, String>();
		Set<String> idxKeySet = usedIdxKey.keySet();
		StringBuffer sql = new StringBuffer();
		String[] arr = updateTemplate.split("\\${2}");
		for(int i=0; i<arr.length; i++)
		{
			if(i%2==0)
				sql.append(arr[i]);
			else
			{
				String[] fieldTuple = arr[i].split(":");
				String fName = fieldTuple[0];
				String type = fieldTuple[1];
				
				String rValue;
				
				//update clause
				if(i <= arr.length/2)
				{
					if(usedKeySet.contains(fName))
					{
						rValue = usedKey.get(fName);
					}
					else
					{
						rValue = RandomUtil.getValue(type);
					}
					if(idxKeySet.contains(fName))
					{
						usedIdxKey.put(fName, rValue);
					}
				}
				//where clause
				else
				{
					if(usedKeySet.contains(fName))
					{
						rValue = usedKey.get(fName);
					}
					else
					{
						rValue = usedValue.get(fName);
					}

				}
				
				sql.append(rValue);
			}
		}
		
		pool.addUsedIndexKey(usedIdxKey);
		return sql.toString();
	}

	@Override
	public String getType() 
	{
		return "UpdateReuseKey";
	}
}
