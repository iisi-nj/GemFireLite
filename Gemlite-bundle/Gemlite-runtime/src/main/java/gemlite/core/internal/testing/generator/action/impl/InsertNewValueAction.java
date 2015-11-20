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
import gemlite.core.internal.testing.generator.pool.IndexKeySet;
import gemlite.core.internal.testing.generator.pool.ReusePool;
import gemlite.core.internal.testing.generator.template.OutputTemplate;
import gemlite.core.internal.testing.generator.util.RandomUtil;
import gemlite.core.internal.testing.generator.util.TableUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class InsertNewValueAction implements OutputAction
{
	@Override
	public Object doAct(OutputTemplate template, Object argument)
	{
		String insertTemplate = template.getTemplate(TableUtil.INSERT);
		String tableName = template.getSource();
		if(StringUtils.isEmpty(insertTemplate))
			return null;
		
		ReusePool pool = (ReusePool) argument;
		try
		{
			Map<String,String> keyMap = TableUtil.getKeyFields(tableName);
			Set<String> keyNameSet = keyMap.keySet();
			IndexKeySet idxKeySet =  TableUtil.getIdxKeyFields(tableName);
			
			Map<String, String> usedKey = new HashMap<String, String>();
			Map<String, String> usedValue = new HashMap<String, String>();
			Map<String, String> usedIdxKey = new HashMap<String, String>();
			
			StringBuffer sql = new StringBuffer();
			String[] arr = insertTemplate.split("\\${2}");
			for(int i=0; i<arr.length; i++)
			{
				if(i%2==0)
					sql.append(arr[i]);
				else
				{
					String[] fieldTuple = arr[i].split(":");
					String fName = fieldTuple[0];
					String type = fieldTuple[1];
					String rValue = RandomUtil.getValue(type);
					sql.append(rValue);
					if(keyNameSet.contains(fName))
					{
						usedKey.put(fName, rValue);
					}
					else
					{
						usedValue.put(fName, rValue);
					}
					if(idxKeySet != null && idxKeySet.contains(fName))
					{
						usedIdxKey.put(fName, rValue);
					}
				}
			}
			
			pool.addUsedKey(usedKey, usedValue);
			pool.addUsedIndexKey(usedIdxKey);
			return sql.toString();
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getType() 
	{
		return "InsertNewValue";
	}

}
