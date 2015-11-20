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
package gemlite.core.internal.index;

import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;

import java.util.Map;

public class LocalIndexCache
{
//  private static Map<String,Map<String, Object>> indexCacheAllModules = new ConcurrentHashMap<String,Map<String, Object>>();
//  
//  public final static Map<?,?> getIndexCache(String moduleName, String type)
//  {
//	  Map<String, Object> indexCache = indexCacheAllModules.get(moduleName);
//	  if(indexCache != null)
//		  return (Map<?,?>)indexCache.get(type);
//	  else
//		  return null;
//  }
//  
//  public final static void setIndexCache(String moduleName, String type, Object value)
//  {
//	  Map<String, Object> indexCache = indexCacheAllModules.get(moduleName);
//	  if(indexCache == null)
//	  {
//		  indexCache = new ConcurrentHashMap<String, Object>();
//		  indexCacheAllModules.put(moduleName, indexCache);
//	  }
//	  indexCache.put(type, value);
//  }
	
	public final static Map<?,?> getIndexCache(String indexName)
	{
		GemliteIndexContext  idc = GemliteContext.getTopIndexContext();
		IIndexContext context = idc.getIndexContext(indexName);
		if(context != null)
		{
			return (Map<?, ?>) context.getIndexData();
		}
		
		return null;
	}
	
	public final static void setIndexCache(String indexName, Object value)
	{
		GemliteIndexContext  idc = GemliteContext.getTopIndexContext();
		IIndexContext context = idc.getIndexContext(indexName);
		if(context != null)
		{
			context.setIndexData(value);
		}
	}
}
