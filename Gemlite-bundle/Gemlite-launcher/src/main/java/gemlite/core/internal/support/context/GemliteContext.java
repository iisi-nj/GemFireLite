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

import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;
import gemlite.core.internal.support.hotdeploy.GemliteDeployerFunction;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.internal.support.hotdeploy.JarURLFinder;
import gemlite.core.internal.support.hotdeploy.JarURLFinderFactory;
import gemlite.core.internal.support.jmx.GemliteNode;
import gemlite.core.internal.support.jmx.GetJmxConfigsFunction;
import gemlite.core.util.LogUtil;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.execute.FunctionService;

public class GemliteContext
{
  protected final static Map<String, IModuleContext> contextMap = new ConcurrentHashMap<>();
  protected final static Map<String, GemliteSibingsLoader> loaderMap = new ConcurrentHashMap<>();
  
  private final static GemliteIndexContext idxContext = new GemliteIndexContext();
  private final static GemliteContext context = new GemliteContext();
  
  public final static String GEMLITE_RUNTIME_MODULE = "_gemlite_runtime_";
  
  private static GemliteRuntimeInfo runtimeInfo = new GemliteRuntimeInfo(); 
  
  private GemliteContext()
  {
  }
  
  public final static GemliteSibingsLoader getCoreLoader()
  {
    IModuleContext coreContext = contextMap.get(GEMLITE_RUNTIME_MODULE);
    if (coreContext != null)
    {
      String coreName = coreContext.getModuleName();
      if (StringUtils.isNotEmpty(coreName))
        return loaderMap.get(coreName);
    }
    
    return null;
  }
  
  public final static GemliteContext getInstance()
  {
    return context;
  }
  
  public final static IModuleContext getCoreModule()
  {
    return getModuleContext(GEMLITE_RUNTIME_MODULE);
  }
  
  public final static GemliteIndexContext getTopIndexContext()
  {
    return idxContext;
  }
  
  public final static Iterator<IModuleContext> getModuleContexts()
  {
    return contextMap.values().iterator();
  }
  
  public final static Iterator<GemliteSibingsLoader> getLoaders()
  {
    return loaderMap.values().iterator();
  }
  
  public final static Iterator<String> getModuleNames()
  {
    return contextMap.keySet().iterator();
  }
  
  public final static IModuleContext getModuleContext(String name)
  {
    return contextMap.get(name);
  }
  
  public IModuleContext onDeployed(URL url)
  {
    IModuleContext ctx = GemliteDeployer.getInstance().deploy(url);
    if (ctx == null)
      return ctx;
    LogUtil.getCoreLog().info("Deploy jar,new module context=" + ctx + " new jar loader=" + ctx.getLoader());
    return ctx;
  }
  
  public void putModuleContext(String name, IModuleContext mc)
  {
    contextMap.put(name, mc);
    loaderMap.put(name, mc.getLoader());
  }
  
  public void putLodaer(String name, GemliteSibingsLoader loader)
  {
    loaderMap.put(name, loader);
  }
  
  public void onStarted()
  {
    try
    {
      registerStaticFunction();
//      GemliteNode.getInstance().onStarted();
      JpaContext.init();
      JarURLFinder finder = JarURLFinderFactory.getFinderImpl();
      finder.doFind();
      URL coreUrl = finder.getCoreJarUrl();
      if (coreUrl != null)
      {
        IModuleContext coreContext = GemliteDeployer.getInstance().deploy(coreUrl);
        contextMap.put(coreContext.getModuleName(), coreContext);
      }
      URL[] dps = finder.getURLsOnStartup();
      LogUtil.getCoreLog().info("Startup jars:" + dps);
      if (dps != null)
      {
        for (URL url : dps)
        {
          if (url != null)
          {
            try
            {
              IModuleContext moduleContext = GemliteDeployer.getInstance().deploy(url);
              contextMap.put(moduleContext.getModuleName(), moduleContext);
            }
            catch (GemliteException e)
            {
              LogUtil.getCoreLog().error("url:", url, e);
            }
            
          }
        }
      }
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().warn("No runtime jar at startup time.", e);
    }
  }
  public static void registerStaticFunction()
  {
    FunctionService.registerFunction(new GemliteDeployerFunction());
    FunctionService.registerFunction(new GetJmxConfigsFunction());
  }
  public void onStopped()
  {
    
  }

  public static GemliteRuntimeInfo getRuntimeInfo()
  {
    return runtimeInfo;
  }
}
