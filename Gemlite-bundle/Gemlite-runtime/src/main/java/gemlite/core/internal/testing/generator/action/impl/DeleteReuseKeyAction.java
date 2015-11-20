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
import gemlite.core.internal.testing.generator.util.TableUtil;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class DeleteReuseKeyAction implements OutputAction
{

	@Override
	public Object doAct(OutputTemplate template, Object argument)
	{
		String deleteTemplate = template.getTemplate(TableUtil.DELETE);
		if(StringUtils.isEmpty(deleteTemplate))
			return null;
		
		ReusePool pool = (ReusePool) argument;
		Map<String, Map<String, String>> usedObj = pool.getUsedKey();
		Map<String, String> usedKey = usedObj.get("key");
		Map<String, String> usedValue = usedObj.get("value");
		Set<String> usedKeySet = usedKey.keySet();
		
		StringBuffer sql = new StringBuffer();
		String[] arr = deleteTemplate.split("\\${2}");
		for(int i=0; i<arr.length; i++)
		{
			if(i%2==0)
				sql.append(arr[i]);
			else
			{
				String[] fieldTuple = arr[i].split(":");
				String fName = fieldTuple[0];
				String rValue;
				if(usedKeySet.contains(fName))
				{
					rValue = usedKey.get(fName);
				}
				else
				{
					rValue = usedValue.get(fName);
				}
				sql.append(rValue);
			}
		}
		return sql.toString();
	}

	@Override
	public String getType()
	{
		return "DeleteReuseKey";
	}

}
