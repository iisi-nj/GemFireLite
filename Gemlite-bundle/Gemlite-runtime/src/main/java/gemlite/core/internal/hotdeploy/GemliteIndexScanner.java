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
package gemlite.core.internal.hotdeploy;
//
//import gemlite.core.internal.context.IndexContextImpl;
//import gemlite.core.internal.index.IndexHelper;
//import gemlite.core.internal.index.def.IDefLoader;
//import gemlite.core.internal.index.def.IndexDefLoader;
//import gemlite.core.internal.support.IGemliteIndexScanner;
//import gemlite.core.internal.support.context.IIndexContext;
//import gemlite.core.internal.support.hotdeploy.GemliteIndexLoader;
//import gemlite.core.util.LogUtil;
//
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//
///***
// * 
// * @author Johnson
// * 
// */
//
//public class GemliteIndexScanner implements IGemliteIndexScanner
//{
//	@Override
//	public IIndexContext scan(String def)
//	{
//		IIndexContext context = null;
//		if (StringUtils.isEmpty(def))
//			return context;
//
//		if (LogUtil.getCoreLog().isDebugEnabled())
//		{
//			LogUtil.getCoreLog().debug("Index Defition: " + def);
//		}
//
//		// „êindexöIáˆIndexClass
//		IDefLoader defLoader = new IndexDefLoader();
//		Map<String, byte[]> clazzMap = defLoader.parseDefs(def);
//		String indexName = defLoader.getName();
//		
//		context = IndexHelper.getIndexContext(indexName);
//		if (context != null)
//		{
//			return context;
//		}
//
//		if (clazzMap != null && clazzMap.size() > 0)
//		{
//			GemliteIndexLoader loader = new GemliteIndexLoader();
//			loader.addIndexClasses(clazzMap);
//			context = new IndexContextImpl(indexName, def, loader);
//			//save index definition to database.
//			IndexHelper.saveIndexToDB(indexName, def);
//	
//		}
//
//		return context;
//	}
//
//}
