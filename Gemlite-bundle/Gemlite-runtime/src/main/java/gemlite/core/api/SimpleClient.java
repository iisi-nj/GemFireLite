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
package gemlite.core.api;

import gemlite.core.api.func.DownloadServerClassFunction;
import gemlite.core.api.logic.LogicServices;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.FunctionUtil;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.gemfire.function.execution.GemfireOnServerFunctionTemplate;
import org.springframework.data.gemfire.support.ListRegionsOnServerFunction;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.NoAvailableLocatorsException;
import com.gemstone.gemfire.cache.client.NoAvailableServersException;
import com.gemstone.gemfire.internal.ClassPathLoader;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.pdx.internal.TypeRegistry;

public class SimpleClient
{
  private static ClassPathXmlApplicationContext context;
  private static ClientCache clientCache;
  
  public final static boolean connect()
  {
    return connect(true);
  }
  
  public final static boolean connect(boolean initENV)
  {
    if (initENV)
    {
      ServerConfigHelper.initConfig();
      ServerConfigHelper.initLog4j("log4j-client.xml");
    }
    try
    {
      context = Util.initContext("ds-client.xml");
      clientCache = ClientCacheFactory.getAnyInstance();
      LogicServices.initServiceMap();
      createClientRegion();
      addServerClassPath();
    }
    catch (NoAvailableLocatorsException e)
    {
      LogUtil.getCoreLog().info("No Locator found");
      LogUtil.getCoreLog().error("Connect command", e);
      return false;
    }
    catch(NoAvailableServersException e)
    {
      LogUtil.getCoreLog().info("No DataStore found");
      LogUtil.getCoreLog().error("Connect command", e);
      return false;
    }
    return true;
  }
  
  @SuppressWarnings("unchecked")
  public final static void addServerClassPath()
  {
    try
    {
      Map<String, byte[]> allClasses = (Map<String, byte[]>) FunctionUtil.onServer(
          new DownloadServerClassFunction().getId(), Map.class);
      GemliteSibingsLoader loader = new GemliteSibingsLoader();
      loader.addDynamicClasses(allClasses);
      ClassPathLoader.getLatest().addOrReplaceAndSetLatest(loader);
      TypeRegistry typeRegistry = ((GemFireCacheImpl) CacheFactory.getAnyInstance()).getPdxRegistry();
      if (typeRegistry != null)
        typeRegistry.flushCache();
    }
    catch (Exception e)
    {
      LogUtil.getAppLog().error(e.getMessage());
    }
  }
  
  public final static void disconnect()
  {
    context.close();
  }
  
  private final static void createClientRegion()
  {
    GemfireOnServerFunctionTemplate template = new GemfireOnServerFunctionTemplate(clientCache);
    Iterable<String> regionNames = template.executeAndExtract(new ListRegionsOnServerFunction());
    
    ClientRegionFactory<?, ?> clientRegionFactory = null;
    if (regionNames != null && regionNames.iterator().hasNext())
    {
      clientRegionFactory = clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
    }
    
    for (String regionName : regionNames)
    {
      clientRegionFactory.create(regionName);
    }
  }
}
