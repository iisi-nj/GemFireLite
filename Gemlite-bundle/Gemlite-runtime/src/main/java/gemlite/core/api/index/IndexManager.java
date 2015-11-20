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
package gemlite.core.api.index;

import gemlite.core.internal.index.IndexTool;
import gemlite.core.internal.support.annotations.IndexRegion;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class IndexManager
{
	public static final String PACKAGE_NAME = "gemlite.dynamic.index";
	
	public final static Map<String, IndexEntrySet> getAllIndexData(String regionName)
	{
		Map<String, IndexEntrySet> result = new HashMap<String, IndexEntrySet>();
		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
		//idc.getIndexNamesByRegion(regionName);
	    Set<IndexRegion> idxSet = idc.getIndexNamesByRegion(regionName);
	    for(Iterator<IndexRegion> it = idxSet.iterator(); it.hasNext();)
	    {
	    	IndexRegion idxRegion = it.next();
	    	IndexTool idxTool = (IndexTool) idc.getIndexContext(idxRegion.indexName());
	    	IndexEntrySet indexData = idxTool.getIndex();
	    	result.put(idxRegion.indexName(), indexData);
	    }
	    return result;
	}
	
	public final static IndexEntrySet getIndexData(String indexName)
	{
		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
		IIndexContext context =  idc.getIndexContext(indexName);
		if(context!=null)
		{
			IndexTool idxTool = (IndexTool) context.getIndexInstance();
			if(idxTool != null)
			{
				IndexEntrySet indexData = idxTool.getIndex();
				return indexData;
			}
			else
			  LogUtil.getCoreLog().error("GetIndexData: IndexName " + indexName + " IndexTool is null.");
		}
		else
		  LogUtil.getCoreLog().error("GetIndexData: IndexName " + indexName + " IndexContext is null.");
		
		return null;
	}
	
//	public final static void fullCreate(String indexName)
//	{
//		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
//		IIndexContext context =  idc.getIndexContext(indexName);
//		if(context!=null)
//		{
//			IndexTool idxTool = (IndexTool) context.getIndexInstance();
//			if(idxTool != null)
//				idxTool.fullCreate();
//			else
//			{
//				if(LogUtil.getCoreLog().isDebugEnabled())
//					LogUtil.getCoreLog().error("FullCreate: IndexName " + indexName + " IndexTool is null.");
//			}
//		}
//		else
//		{
//			if(LogUtil.getCoreLog().isDebugEnabled())
//				LogUtil.getCoreLog().error("FullCreate: IndexName " + indexName + " IndexContext is null.");
//		}
//	}
}
