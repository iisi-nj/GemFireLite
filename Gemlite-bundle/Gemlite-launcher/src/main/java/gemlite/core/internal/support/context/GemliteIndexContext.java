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
package gemlite.core.internal.support.context;

import gemlite.core.internal.support.annotations.IndexRegion;
import gemlite.core.internal.support.jpa.files.service.GmIndexService;
import gemlite.core.util.LogUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.partition.PartitionListener;

public class GemliteIndexContext
{
  // indexName -> indexContext
  private final Map<String, IIndexContext> contextMap = new ConcurrentHashMap<>();
  
  // regionName -> Set(indexName)
  private final Map<String, Set<IndexRegion>> regionMap = new ConcurrentHashMap<>();
  
  // regionName -> Set(indexName)
  private final Map<String, Set<IndexRegion>> testRegionMap = new ConcurrentHashMap<>();
  
  // regionName -> listeners
  private final Map<String, CacheListener> regionListeners = new ConcurrentHashMap<>();
  private final Map<String, PartitionListener> partitionListeners = new ConcurrentHashMap<>();
  
  // regionName -> Map<?,?>
  private static Map<String, Object> testCache = new ConcurrentHashMap<String, Object>();
  
  public final Map<?, ?> getTestCache(String regionName)
  {
    return (Map<?, ?>) testCache.get(regionName);
  }
  
  public void setLocalCache(String regionName, Object value)
  {
    testCache.put(regionName, value);
  }
  
  public final Set<String> getAllTestRegions()
  {
    return testCache.keySet();
  }
  
  public final Iterator<IIndexContext> getIndexContexts()
  {
    Collection<IIndexContext> c = contextMap.values();
    return c == null ? null : c.iterator();
  }
  
  public final Iterator<String> getIndexNames()
  {
    Set<String> k = contextMap.keySet();
    return k == null ? null : k.iterator();
  }
  
  public final IIndexContext getIndexContext(String indexName)
  {
    return contextMap.get(indexName);
  }
  
  public void putIndexContext(String indexName, IIndexContext idc)
  {
    contextMap.put(indexName, idc);
  }
  
  public final CacheListener getCacheListener(String regionName)
  {
    return regionListeners.get(regionName);
  }
  
  public void putCacheListener(String regionName, CacheListener c)
  {
    regionListeners.put(regionName, c);
  }
  
  public final PartitionListener getPartitionListener(String regionName)
  {
    return partitionListeners.get(regionName);
  }
  
  public void putPartitionListener(String regionName, PartitionListener pl)
  {
    partitionListeners.put(regionName, pl);
  }
  
  public final Set<IndexRegion> getIndexNamesByRegion(String regionName)
  {
    return regionMap.get(regionName);
  }
  
  public void putIndexNamesByRegion(IndexRegion bean)
  {
    Set<IndexRegion> list = regionMap.get(bean.regionName());
    if (list == null)
    {
      list = new ConcurrentSkipListSet<IndexRegion>(new Comparator<IndexRegion>()
      {
        
        @Override
        public int compare(IndexRegion o1, IndexRegion o2)
        {
          
          return new CompareToBuilder().append(o1.orderNo(), o2.orderNo()).append(o1.indexName(), o2.indexName())
              .toComparison();
        }
      });
      
      regionMap.put(bean.regionName(), list);
    }
    
    list.add(bean);
  }
  
  public final Set<IndexRegion> getIndexNamesByTestRegion(String regionName)
  {
    return testRegionMap.get(regionName);
  }
  
  public void putIndexNamesByTestRegion(IndexRegion bean)
  {
    Set<IndexRegion> list = testRegionMap.get(bean.regionName());
    if (list == null)
    {
      list = new ConcurrentSkipListSet<IndexRegion>(new Comparator<IndexRegion>()
      {
        
        @Override
        public int compare(IndexRegion o1, IndexRegion o2)
        {
          
          return new CompareToBuilder().append(o1.orderNo(), o2.orderNo()).append(o1.indexName(), o2.indexName())
              .toComparison();
        }
      });
      
      testRegionMap.put(bean.regionName(), list);
    }
    
    list.add(bean);
  }
  
  // public IIndexContext createIndex(String def)
  // {
  //
  // GemliteSibingsLoader loader = new GemliteSibingsLoader();
  // GemliteClassScannerPro scanner = new GemliteClassScannerPro();
  // // kÏvú°„Module
  // // RegistryMatchedContext matchedRegistry = scanner.scan(loader);
  //
  //
  // IIndexContext context = scan(def);
  // if (context != null)
  // {
  // String indexName = context.getIndexName();
  // GemliteIndexContext topIdxContext = GemliteContext.getTopIndexContext();
  // IIndexContext oldContext = topIdxContext.getIndexContext(indexName);
  // if(oldContext == null)
  // {
  // topIdxContext.putIndexContext(indexName, context);
  // context.register();
  // }
  // else
  // {
  // LogUtil.getCoreLog().error("Index Name: " + indexName +
  // " already exists.");
  // return null;
  // }
  // LogUtil.getCoreLog().info(context.getIndexName() + " created.\n");
  // }
  // else
  // LogUtil.getCoreLog().error("No Index definition found. Def: " + def);
  //
  // return context;
  // }
  
  // private IIndexContext scan(String def)
  // {
  // String builderClassName = "gemlite.core.internal.GemliteBuilder";
  // IGemliteBuilder builder = null;
  // try
  // {
  // builder =
  // (IGemliteBuilder)GemliteClassLoader.getInstance().loadClass(builderClassName).newInstance();
  // }
  // catch (InstantiationException | IllegalAccessException |
  // ClassNotFoundException e)
  // {
  // throw new GemliteException("Builder class:" + builderClassName, e);
  // }
  //
  // if (LogUtil.getCoreLog().isDebugEnabled())
  // LogUtil.getCoreLog().debug(
  // "Create builder->" + builder + " classloader->" +
  // builder.getClass().getClassLoader());
  //
  // IGemliteIndexScanner scanner = builder.createIndexScanner();
  // IIndexContext context = scanner.scan(def);
  // return context;
  // }
  
  // public IIndexContext dropIndex(String indexName)
  // {
  // IIndexContext oldContext = null;
  // if(StringUtils.isNotEmpty(indexName))
  // {
  // oldContext = removeIndexContext(indexName);
  // if(oldContext != null)
  // LogUtil.getCoreLog().info(indexName + " dropped.\n");
  // else
  // LogUtil.getCoreLog().error("No index name found.");
  // }
  // else
  // LogUtil.getCoreLog().error("No index name found.");
  //
  // return oldContext;
  // }
  //
  // public List<String> listIndex(String regionName)
  // {
  // List<String> indexList = new ArrayList<String>();
  // Set<IndexRegion> indexNames = regionMap.get(regionName);
  // if(indexNames != null)
  // {
  // for(Iterator<IndexRegion> it=indexNames.iterator(); it.hasNext();)
  // {
  // IndexRegion index = it.next();
  // if(!index.isRegion())
  // indexList.add(index.indexName());
  // else
  // indexList.add(index.indexName()+"(Hash Index)");
  // }
  // }
  // return indexList;
  // }
  
  // public Map describeIndex(String indexName)
  // {
  // try
  // {
  // IIndexContext context = contextMap.get(indexName);
  // if(context.isRegion())
  // return new HashMap<String,Object>();
  //
  // String className =
  // "gemlite.core.internal.measurement.index.IndexMeasureHelper";
  // Class indexMeasureHelper =
  // GemliteClassLoader.getInstance().loadClass(className);
  // Method m = indexMeasureHelper.getDeclaredMethod("getIndexStat",
  // String.class);
  // Object indexStat = m.invoke(indexMeasureHelper, indexName);
  // String indexStatClassName =
  // "gemlite.core.internal.measurement.index.AbstractIndexStat";
  // Class indexStatClass =
  // GemliteClassLoader.getInstance().loadClass(indexStatClassName);
  // Method m2 = indexStatClass.getDeclaredMethod("describeIndex");
  // Map info = (Map) m2.invoke(indexStat);
  // return info;
  // }
  // catch(Exception e)
  // {
  // LogUtil.getCoreLog().error("Describe Index error, indexName: " + indexName,
  // e);
  // return null;
  // }
  // }
  //
  // public String printIndexValue(String indexName, String searchParamStr, int
  // pageNumber, int pageSize)
  // {
  // try
  // {
  // String className =
  // "gemlite.core.internal.measurement.index.IndexMeasureHelper";
  // Class indexMeasureHelper =
  // GemliteClassLoader.getInstance().loadClass(className);
  // Method m = indexMeasureHelper.getDeclaredMethod("getIndexStat",
  // String.class);
  // Object indexStat = m.invoke(indexMeasureHelper, indexName);
  // String indexStatClassName =
  // "gemlite.core.internal.measurement.index.AbstractIndexStat";
  // Class indexStatClass =
  // GemliteClassLoader.getInstance().loadClass(indexStatClassName);
  // Method m2 = indexStatClass.getDeclaredMethod("printIndexValue",
  // String.class, int.class, int.class);
  // String result = (String)
  // m2.invoke(indexStat,searchParamStr,pageNumber,pageSize);
  // return result;
  // }
  // catch(Exception e)
  // {
  // LogUtil.getCoreLog().error("Query Index error, Index Name: " + indexName +
  // ", Search Parameter: " + searchParamStr
  // + ", Page Number: " + pageNumber + ", Page Size: " + pageSize, e);
  // return null;
  // }
  // }
  
  public IIndexContext removeIndexContext(String indexName)
  {
    IIndexContext oldContext = contextMap.get(indexName);
    
    if (oldContext != null)
    {
      String regionName = oldContext.getRegionName();
      removeIndexNamesByRegion(indexName, regionName);
      removeIndexNamesByTestRegion(indexName, regionName);
      oldContext.close();
      contextMap.remove(indexName);
      removeIndexFromDB(indexName);
    }
    
    return oldContext;
  }
  
  private void removeIndexNamesByRegion(String indexName, String regionName)
  {
    Set<IndexRegion> indexNames = regionMap.get(regionName);
    if (indexNames != null)
    {
      Set<IndexRegion> copy = new HashSet<>(indexNames);
      for (Iterator<IndexRegion> it = copy.iterator(); it.hasNext();)
      {
        IndexRegion region = it.next();
        if (region.indexName().equalsIgnoreCase(indexName))
        {
          indexNames.remove(region);
        }
      }
    }
    
    if (indexNames == null || indexNames.size() == 0)
      regionMap.remove(regionName);
  }
  
  private void removeIndexNamesByTestRegion(String indexName, String regionName)
  {
    Set<IndexRegion> indexNames = testRegionMap.get(regionName);
    if (indexNames != null)
    {
      Set<IndexRegion> copy = new HashSet<>(indexNames);
      for (Iterator<IndexRegion> it = copy.iterator(); it.hasNext();)
      {
        IndexRegion region = it.next();
        if (region.regionName().equalsIgnoreCase(regionName))
        {
          indexNames.remove(region);
        }
      }
    }
    
    if (indexNames == null || indexNames.size() == 0)
      testRegionMap.remove(regionName);
  }
  
  private void removeIndexFromDB(String indexName)
  {
    GmIndexService service = JpaContext.getService(GmIndexService.class);
    if (service != null)
    {
      service.deleteIndex(indexName);
      LogUtil.getCoreLog().info("Delete index [" + indexName + "] from database successfully.");
    }
    else
    {
      LogUtil.getCoreLog().error("Failed to get IndexFilesService bean from JpaContext.");
    }
  }
  
  public int getIndexContextMapSize()
  {
    return contextMap.entrySet().size();
  }
}
