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
package gemlite.core.api.logic;

import gemlite.core.annotations.logic.RemoteService;
import gemlite.core.api.index.IndexEntrySet;
import gemlite.core.api.index.IndexManager;
import gemlite.core.api.index.clause.IndexClause;
import gemlite.core.api.index.clause.IndexClauseImpl;
import gemlite.core.internal.logic.IRemoteService;
import gemlite.core.internal.logic.ListServiceFunction;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.IGemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.util.FunctionUtil;
import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;
import com.ibm.db2.jcc.am.mo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LogicServices
{
  private static String simpleModuleName = "";
  private static Map<String, Set<String>> services;
  
  public final static void initServiceMap()
  {
    try
    {
      services = (Map<String, Set<String>>) FunctionUtil.onServer(new ListServiceFunction().getId(), Map.class);
      Iterator<String> it = services.keySet().iterator();
      if (it.hasNext())
        simpleModuleName = it.next();
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("", e);
    }
  }
  
  public final static IRemoteService getService(String serviceName)
  {
    return getService(LogicSession.getSession().getModuleName(), serviceName);
  }
  
  public final static IRemoteService getService(String moduleName, String serviceName)
  {
    IModuleContext mc = GemliteContext.getModuleContext(moduleName);
    IGemliteRegistry registry = mc.getRegistry(RemoteService.class);
    IRemoteService service = (IRemoteService) registry.getItem(serviceName);
    return service;
  }
  
  /***
   * 
   * @param regionName
   * @param indexName
   * @return
   */
  public final static IndexEntrySet getIndexData(String indexName)
  {
    return IndexManager.getIndexData(indexName);
  }
  
  public final static IndexClause newIndexClause(String indexName)
  {
    return new IndexClauseImpl(indexName);
  }
  
  /***
   * 
   * @param regionName
   * @return
   */
  public final static Map<String, IndexEntrySet> getAllIndexData(String regionName)
  {
    return IndexManager.getAllIndexData(regionName);
  }
  
  /***
   * always return localdata for partition region
   * 
   * @param name
   * @return
   */
  public final static <K, V> Region<K, V> getRegion(String name)
  {
    Region<K, V> r = CacheFactory.getAnyInstance().getRegion(name);
    if (PartitionRegionHelper.isPartitionedRegion(r))
    {
      r = PartitionRegionHelper.getLocalData(r);
    }
    return r;
  }
  
  public final static <K, V> Region<K, V> getColocatedRegion(String region, String coRegion)
  {
    Region r = CacheFactory.getAnyInstance().getRegion(region);
    if (PartitionRegionHelper.isPartitionedRegion(r))
    {
      r = PartitionRegionHelper.getLocalData(r);
      Map<String, Region<?, ?>> colMap = PartitionRegionHelper.getColocatedRegions(r);
      return (Region<K, V>) colMap.get("/" + coRegion);
    }
    else
      return null;
  }
  
  /***
   * always return primary localdata for partition region
   * 
   * @param name
   * @return
   */
  public final static <K, V> Region<K, V> getPrimaryRegion(String name)
  {
    Region<K, V> r = CacheFactory.getAnyInstance().getRegion(name);
    if (PartitionRegionHelper.isPartitionedRegion(r))
    {
      r = PartitionRegionHelper.getLocalData(r);
      r = PartitionRegionHelper.getLocalPrimaryData(r);
    }
    return r;
  }
  
  private final static void checkModuleAndServiceName(String moduleName, String beanName)
  {
    if(StringUtils.isEmpty(moduleName))
      moduleName = simpleModuleName;
    if(services.isEmpty())
      LogicServices.initServiceMap();
    Set<String> beanNames = services.get(moduleName);
    if (beanNames == null)
      throw new GemliteException("No module:" + moduleName + ". Modules:" + services.keySet());
    if (!beanNames.contains(beanName))
      throw new GemliteException("No service:" + beanName + ". Services:" + beanNames);
  }
  
  /***
   * 
   * @param beanName
   * @param userArgs
   * @return
   */
  public final static RemoteResult createRequest(String beanName, Object userArgs)
  {
    return createRequest(simpleModuleName, beanName, userArgs);
  }
  
  public final static RemoteResult createHeavyRequest(String beanName, Object userArgs)
  {
    return createHeavyRequest(simpleModuleName, beanName, userArgs);
  }
  
  /***
   * 
   * @param moduleName
   * @param beanName
   * @param userArgs
   * @return
   */
  public final static RemoteResult createHeavyRequest(String moduleName, String beanName, Object userArgs)
  {
    checkModuleAndServiceName(moduleName, beanName);
    Execution ex = FunctionService.onServers(ClientCacheFactory.getAnyInstance().getDefaultPool());
    Map<String, Object> map = new HashMap<>();
    map.put("moduleName", moduleName);
    map.put("beanName", beanName);
    map.put("userArgs", userArgs);
    ex = ex.withArgs(map);
    RemoteResult result = new RemoteResult(ex);
    return result;
  }
  
  /***
   * 
   * @param moduleName
   * @param beanName
   * @param userArgs
   * @return
   */
  public final static RemoteResult createRequest(String moduleName, String beanName, Object userArgs)
  {
    checkModuleAndServiceName(moduleName, beanName);
    Execution ex = FunctionService.onServer(ClientCacheFactory.getAnyInstance().getDefaultPool());
    Map<String, Object> map = new HashMap<>();
    map.put("moduleName", moduleName);
    map.put("beanName", beanName);
    map.put("userArgs", userArgs);
    ex = ex.withArgs(map);
    RemoteResult result = new RemoteResult(ex);
    return result;
  }
  
  /***
   * 
   * @param regionName
   * @param beanName
   * @param userArgs
   * @param simpleFilter
   * @return
   */
  public final static RemoteResult createRequestWithFilter(String regionName, String beanName, Object userArgs, Object simpleFilter)
  {
    HashSet<Object> set =null;
    if(simpleFilter!=null)
    {
      set = new HashSet<>();
      set.add(simpleFilter);
    }
    return createRequestWithFilter(regionName, beanName, userArgs, set);
  }
  
  /**
   * 
   * @param regionName
   * @param beanName
   * @param userArgs
   * @param filter
   * @return
   */
  public final static RemoteResult createRequestWithFilter(String regionName, String beanName, Object userArgs, Set<Object> filter)
  {
    return createRequestWithFilter(simpleModuleName, regionName, beanName, userArgs, filter);
  }
  
  /**
   * 
   * @param moduleName
   * @param regionName
   * @param beanName
   * @param userArgs
   * @param filter
   * @return
   */
  public final static RemoteResult createRequestWithFilter(String moduleName, String regionName, String beanName,
      Object userArgs, Set<Object> filter)
  {
	if(StringUtils.isEmpty(regionName))
		return null;
	Region r = ClientCacheFactory.getAnyInstance().getRegion(regionName);
	if (r == null)
		return null;
	
    Execution ex = FunctionService.onRegion(r);
    Map<String, Object> map = new HashMap<>();
    map.put("moduleName", moduleName);
    map.put("beanName", beanName);
    map.put("userArgs", userArgs);
    ex = ex.withArgs(map);
    if (filter != null)
      ex = ex.withFilter(filter);
    RemoteResult result = new RemoteResult(ex);
    return result;
  }
  
  
  /**
   * 
   * @param moduleName
   * @param regionName
   * @param beanName
   * @param userArgs
   * @param filter
   * @return
   */
  public final static RemoteResult createRequestWithFilterServer(String moduleName, String regionName, String beanName,
      Object userArgs, Set<Object> filter)
  {
    if(StringUtils.isEmpty(regionName))
        return null;
    Region r = CacheFactory.getAnyInstance().getRegion(regionName);
    if (r == null)
        return null;
    
    Execution ex = FunctionService.onRegion(r);
    Map<String, Object> map = new HashMap<>();
    map.put("moduleName", moduleName);
    map.put("beanName", beanName);
    map.put("userArgs", userArgs);
    ex = ex.withArgs(map);
    if (filter != null)
      ex = ex.withFilter(filter);
    RemoteResult result = new RemoteResult(ex);
    return result;
  }
  
  
}
