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

import gemlite.core.internal.index.def.IDefLoader;
import gemlite.core.internal.index.def.IndexDefLoader;
import gemlite.core.internal.measurement.index.AbstractIndexStat;
import gemlite.core.internal.measurement.index.IndexMeasureHelper;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.annotations.IndexRegion;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.internal.support.hotdeploy.scanner.GemliteClassScannerPro;
import gemlite.core.internal.support.hotdeploy.scanner.GemliteIndexClassScanner;
import gemlite.core.internal.support.hotdeploy.scanner.RegistryMatchedContext;
import gemlite.core.internal.support.hotdeploy.scanner.ScannerIterator;
import gemlite.core.internal.support.jpa.files.domain.GmIndex;
import gemlite.core.internal.support.jpa.files.service.GmIndexService;
import gemlite.core.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class IndexHelper
{
	/**
	 * get all index data from database
	 * 
	 * @param indexFilesList
	 */
	private final static List<GmIndex> getAllIndexs()
	{
		List<GmIndex> gmIndexs = new ArrayList<GmIndex>();
		try
		{
			GmIndexService service = JpaContext.getService(GmIndexService.class);
			if (service != null)
			{
				gmIndexs = service.getAllIndexs();
			}
		} catch (Exception e)
		{
			LogUtil.getCoreLog().error(
					"Failed to get IndexFilesService bean from JpaContext.",e);
		}
		return gmIndexs;

	}

	/**
	 * load all index clause from database
	 */
	public final static void loadAllIndexsFromDB()
	{
		List<GmIndex> gmIndexs = getAllIndexs();

		for (GmIndex index : gmIndexs)
		{
			String indexName = index.getIndexName();
			String clause = index.getIndexClause();
			if (!StringUtils.isBlank(indexName) && !StringUtils.isBlank(clause))
			{
				// check index context exists or not
				if (!IndexHelper.checkIndexExists(indexName))
				{
					try
					{
						createIndex(clause);
					} catch (Exception e)
					{
						LogUtil.getCoreLog().error(
								"Create index [" + indexName + "] failed.", e);
					}

				}

			}
		}
	}

	public final static boolean checkIndexExists(String indexName)
	{
		GemliteIndexContext topIdxContext = GemliteContext.getTopIndexContext();
		IIndexContext oldContext = topIdxContext.getIndexContext(indexName);
		if (oldContext != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Debug data
	 * @param seq
	 */
	private final static void printLoaderClassInfo(String header)
	{
		GemliteIndexContext topIdxContext = GemliteContext.getTopIndexContext();
		Iterator<String> indexNames = topIdxContext.getIndexNames();
		StringBuffer outBuf = new StringBuffer();
		outBuf.append(header).append(">>> ").append("\n");
		while (indexNames.hasNext())
		{
			String indexName = indexNames.next();
			IIndexContext oldContext = topIdxContext.getIndexContext(indexName);
			GemliteSibingsLoader classLoader = oldContext.getLoader();
			outBuf.append("IndexName:").append(indexName)
					.append(" ,ClassByteMap:")
					.append(classLoader.getDynaimcClasses().toString())
					.append("\n");
		}
		LogUtil.getCoreLog().trace(outBuf.toString());
	}

	public final static IIndexContext getIndexContext(String indexName)
	{
		GemliteIndexContext topIdxContext = GemliteContext.getTopIndexContext();
		IIndexContext indexContext = topIdxContext.getIndexContext(indexName);
		return indexContext;
	}

	/**
	 * save index clause to database
	 * 
	 * @param indexFilesList
	 */
	public final static void saveIndexsToDB(List<GmIndex> gmIndexs)
	{
		try
		{
			GmIndexService service = JpaContext.getService(GmIndexService.class);
			if (service != null)
			{
				service.save(gmIndexs);
			}
		} catch (Exception e)
		{
			LogUtil.getCoreLog().error(
					"Failed to get IndexFilesService bean from JpaContext.",e);
		}

	}

	/**
	 * save index clause to database
	 * 
	 * @param indexFilesList
	 */
	public final static void saveIndexToDB(String indexName, String clause)
	{
		GmIndexService service = JpaContext.getService(GmIndexService.class);
		if (service != null)
		{
			try
			{
				service.save(indexName, clause);
			} catch (Exception e)
			{
				if (!service.checkIndexExists(indexName))
				{
					LogUtil.getAppLog().error(
							"Create index[" + indexName + "] failed.", e);
					service.save(indexName, clause);
				}
			}
		} else
		{
			LogUtil.getCoreLog().error(
					"Failed to get IndexFilesService bean from JpaContext.");
		}

	}

	public final static IIndexContext createIndex(String def)
	{
		IIndexContext context = null;
		if (StringUtils.isEmpty(def))
			return context;

		LogUtil.getCoreLog().trace("Create index use defition: {}" ,def);

		// „êindexöIáˆIndexClass
		IDefLoader defLoader = new IndexDefLoader();
		Map<String, byte[]> clazzMap = defLoader.parseDefs(def);
		
		String indexName = defLoader.getName();
		
		LogUtil.getCoreLog().trace("Compile index success,indexName: {},clssSize={}" ,indexName,clazzMap.size());

		// IIndexContext oldContext = IndexHelper.getIndexContext(indexName);
		// if (oldContext != null)
		// {
		// LogUtil.getCoreLog().error("Index Name: " + indexName +
		// " already exists.");
		// return null;
		// }

		if (clazzMap != null && clazzMap.size() > 0)
		{
			GemliteSibingsLoader parentLoader = GemliteContext.getCoreLoader();
			if (parentLoader == null)
			{
				LogUtil.getCoreLog().error("No Core Loader found. indexName={}",indexName);
				return context;
			}
			GemliteSibingsLoader loader = new GemliteSibingsLoader(parentLoader);
			loader.addDynamicClasses(clazzMap);
			
			GemliteClassScannerPro scanner = new GemliteIndexClassScanner();
			// kœv˙∞ÑModule
			ScannerIterator scannerIterator = null;
			try
			{
				scannerIterator = new ScannerIterator(clazzMap);
				RegistryMatchedContext matchedRegistry = scanner.scan(loader,
						scannerIterator);
				matchedRegistry.setModuleInfo(indexName + ":" + def, null);
				context = (IIndexContext) scanner.createModuleContext(loader,
						matchedRegistry);

				if (context != null)
				{
					GemliteIndexContext topIdxContext = GemliteContext
							.getTopIndexContext();
					topIdxContext.putIndexContext(indexName, context);
					scanner.register(context, matchedRegistry);
					IndexHelper.saveIndexToDB(indexName, def);
					LogUtil.getCoreLog().info(
							context.getIndexName() + " created.\n");
				} else
					LogUtil.getCoreLog().error(
							"No Index definition found. Def: " + def);
			} catch (Exception e)
			{
				throw new GemliteException("Scan error , loader url is {}"
						+ loader.getURL(), e);
			} finally
			{
				try
				{
					if (scannerIterator != null)
					{
						scannerIterator.close();
					}
				} catch (IOException e)
				{
					LogUtil.getCoreLog().warn("Close jar inputstream error.");
				}
			}

		}
		//IndexHelper.printLoaderClassInfo("AfterCreateIndex");

		return context;
	}

	public final static IIndexContext dropIndex(String indexName)
	{
		IIndexContext oldContext = null;
		if (StringUtils.isNotEmpty(indexName))
		{
			GemliteIndexContext topIdxContext = GemliteContext
					.getTopIndexContext();
			oldContext = topIdxContext.removeIndexContext(indexName);
			if (oldContext != null)
				LogUtil.getCoreLog().info(indexName + " dropped.\n");
			else
				LogUtil.getCoreLog().error("No index name found.");
		} else
			LogUtil.getCoreLog().error("No index name found.");

		return oldContext;
	}

	public final static List<String> listIndex(String regionName)
	{
		List<String> indexList = new ArrayList<String>();
		GemliteIndexContext topIdxContext = GemliteContext.getTopIndexContext();
		Set<IndexRegion> indexNames = topIdxContext
				.getIndexNamesByRegion(regionName);
		if (indexNames != null)
		{
			for (Iterator<IndexRegion> it = indexNames.iterator(); it.hasNext();)
			{
				IndexRegion index = it.next();
				if (!index.isRegion())
					indexList.add(index.indexName());
				else
					indexList.add(index.indexName() + "(Hash Index)");
			}
		}
		return indexList;
	}

	public final static Map describeIndex(String indexName)
	{
		try
		{
			IIndexContext context = getIndexContext(indexName);
			if (context.isRegion())
				return new HashMap<String, Object>();

			AbstractIndexStat indexStat = IndexMeasureHelper
					.getIndexStat(indexName);
			Map info = indexStat.describeIndex();

			return info;
		} catch (Exception e)
		{
			LogUtil.getCoreLog().error(
					"Describe Index error, indexName: " + indexName, e);
			return null;
		}
	}

	public final static String printIndexValue(String indexName,
			String searchParamStr, int pageNumber, int pageSize)
	{
		try
		{
			AbstractIndexStat indexStat = IndexMeasureHelper
					.getIndexStat(indexName);
			String result = indexStat.printStrIndexValue(searchParamStr,
					pageNumber, pageSize);

			return result;
		} catch (Exception e)
		{
			LogUtil.getCoreLog().error(
					"Query Index error, Index Name: " + indexName
							+ ", Search Parameter: " + searchParamStr
							+ ", Page Number: " + pageNumber + ", Page Size: "
							+ pageSize, e);
			return null;
		}
	}
}
