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
package gemlite.core.internal.testing.generator.util;

import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.index.IndexTool;
import gemlite.core.internal.support.annotations.IndexRegion;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.testing.generator.pool.IndexKeySet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class TableUtil
{
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	
	/**
	 * INSERT
	 */
	public static final int ACT_INS = 0;
	public static final int ACT_INS_UPD = 1;
	public static final int ACT_INS_DEL = 2;
	public static final int ACT_INS_UPD_DEL = 3;

	
	
	public static final Map<String, String> getValueFields(String tableName) throws NoSuchFieldException, SecurityException
	{
		Map<String,String> result = new HashMap<String,String>();
		String regionName = DomainRegistry.tableToRegion(tableName);
		IMapperTool tool = DomainRegistry.getMapperTool(regionName);
		Class valueClass = tool.getValueClass();
		List fieldNames = tool.getValueFieldNames();
		for(Iterator it = fieldNames.iterator(); it.hasNext();)
		{
			String fieldName = (String) it.next();
			//Field field = valueClass.getField(fieldName);
			Field field = valueClass.getDeclaredField(fieldName);
			String type = field.getType().getSimpleName();
			result.put(fieldName, type);
		}
		
		return result;
	}
	
	public static final Map<String, String> getKeyFields(String tableName) throws NoSuchFieldException, SecurityException
	{
		Map<String,String> result = new HashMap<String,String>();
		String regionName = DomainRegistry.tableToRegion(tableName);
		IMapperTool tool = DomainRegistry.getMapperTool(regionName);
		Class keyClass = tool.getKeyClass();
		List keyFields = tool.getKeyFieldNames();
		if(keyFields.size() == 1)
		{
			String fieldName = (String) keyFields.iterator().next();
			String type = keyClass.getSimpleName();
			result.put(fieldName, type);
		}
		else
		{
			for(Iterator it = keyFields.iterator(); it.hasNext();)
			{
				String fieldName = (String) it.next();
				//Field field = keyClass.getField(fieldName);
				Field field = keyClass.getDeclaredField(fieldName);
				String type = field.getType().getSimpleName();
				result.put(fieldName, type);
			}
		}
		
		return result;
	}
	
	public static final IndexKeySet getIdxKeyFields(String tableName) throws NoSuchFieldException, SecurityException
	{
	    IndexKeySet idxKeySet = new IndexKeySet();
		String regionName = DomainRegistry.tableToRegion(tableName);
		
		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
	    Set<IndexRegion> indexNames = idc.getIndexNamesByRegion(regionName); 
		if(indexNames==null)
			return null;

		for(IndexRegion name:indexNames)
		{
			Map<String,String> map = new HashMap<String,String>();
			IIndexContext context = idc.getIndexContext(name.indexName());
			if (context.isRegion())
				continue;
			IndexTool indexTool = (IndexTool) context.getIndexInstance();
			Class idxKeyCls = indexTool.getKeyClass();
			Set idxKeyFields = indexTool.getKeyFieldNames();
			for(Iterator it2 = idxKeyFields.iterator(); it2.hasNext();)
			{
				String idxField = (String) it2.next();
				//Field field = idxKeyCls.getField(idxField);
				Field field = idxKeyCls.getDeclaredField(idxField);
				String type = field.getType().getSimpleName();
				map.put(idxField, type);
			}
			idxKeySet.putIndexKeyFields(name.indexName(), map);
		}

		idxKeySet.setRegionName(regionName);
		return idxKeySet;
	}
}
