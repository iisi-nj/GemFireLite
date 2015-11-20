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
package gemlite.core.internal.testing.generator.template.impl;

import gemlite.core.internal.testing.generator.template.OutputTemplate;
import gemlite.core.internal.testing.generator.util.TableUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SqlTemplate implements OutputTemplate
{
	private static String GAP = "$$";
	private Map<String, String> templateMap;
	private String table;
	
	public SqlTemplate(String table)
	{
		this.table = table;
		templateMap = genSqlTemplate(table);
	}
	
	@Override
	public String getTemplate(String type)
	{
		String template = templateMap.get(type);
		return template;
	}

	@Override
	public String getSource() 
	{
		return table;
	}
	
	private Map<String, String> genSqlTemplate(String table)
	{
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer fieldList = new StringBuffer();
		StringBuffer valueList = new StringBuffer();
		
		Map<String, String> valueFields;
		try {
			valueFields = TableUtil.getValueFields(table);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return map;
		}
		
		for(Iterator<String> it = valueFields.keySet().iterator(); it.hasNext();)
		{
			String fName = it.next();
			fieldList.append(fName).append(",");
			
			String valueType = valueFields.get(fName);
			valueList.append(GAP).append(fName+":"+valueType).append(GAP).append(",");
		}
		
		StringBuffer insert = new StringBuffer();
		insert.append("insert into ").append(table).append("(").append(fieldList,0,fieldList.length()-1)
		.append(") values (").append(valueList,0,valueList.length()-1).append(")");
		
		map.put(TableUtil.INSERT, insert.toString());
		
		StringBuffer updateList = new StringBuffer();
		StringBuffer whereList = new StringBuffer();
		for(Iterator<String> it = valueFields.keySet().iterator(); it.hasNext();)
		{
			String fName = it.next();
			String valueType = valueFields.get(fName);
			updateList.append(fName).append("=").append(GAP).append(fName+":"+valueType).append(GAP).append(",");
			whereList.append(fName).append("=").append(GAP).append(fName+":"+valueType).append(GAP).append(" and ");
		}
		StringBuffer update = new StringBuffer();
		update.append("update ").append(table).append(" set ")
		.append(updateList,0,updateList.length()-1).append(" where ")
		.append(whereList,0,whereList.length()-5);
		
		map.put(TableUtil.UPDATE, update.toString());
		
		StringBuffer delete = new StringBuffer();
		delete.append("delete from ").append(table).append(" where ")
		.append(whereList,0,whereList.length()-5);
		
		map.put(TableUtil.DELETE, delete.toString());
		
		return map;
	}
}
