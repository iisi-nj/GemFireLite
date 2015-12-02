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
package gemlite.core.internal.support.hotdeploy;

import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.DomainMapperHelper;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.support.events.EventDispatcher;
import gemlite.core.internal.support.events.GemliteEvent;
import gemlite.core.internal.support.hotdeploy.scanner.GemliteClassScannerPro;
import gemlite.core.internal.support.hotdeploy.scanner.RegistryMatchedContext;
import gemlite.core.internal.support.hotdeploy.scanner.ScannedRegistryDefine;
import gemlite.core.internal.support.hotdeploy.scanner.ScannerIterator;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.orm.jpa.JpaSystemException;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.internal.ClassPathLoader;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.pdx.internal.TypeRegistry;

/***
 * 
 * @author ynd
 * 
 */
public class GemliteDeployer
{
  static String builderClassName = "gemlite.core.internal.GemliteBuilder";
  
  private final static GemliteDeployer inst = new GemliteDeployer();
  private final static GemliteClassScannerPro scanner = new GemliteClassScannerPro();
  private ThreadLocal<Map<String, Object>> deploySession = new ThreadLocal<>();
  private static final String CURRENT_LOADER = "CURRENT_LOADER";
  private static final String CURRENT_MATCHED_REG_CTX = "CURRENT_MATCHED_REG_CTX";
  private CompositeDeployListener compositeDpl = new CompositeDeployListener();
  
  private GemliteDeployer()
  {
    Map<String, Object> m = new HashMap<>();
    deploySession.set(m);
  }
  
  public final static GemliteDeployer getInstance()
  {
    return inst;
  }
  
  public final static CompositeDeployListener getDeployListener()
  {
    return getInstance().compositeDpl;
  }
  
  public static Map<String, Object> getSession()
  {
    return getInstance().deploySession.get();
  }
  
  public void setValue(String key, Object value)
  {
    deploySession.get().put(key, value);
  }
  
  public Object getValue(String key)
  {
    return deploySession.get().get(key);
  }
  
  public GemliteSibingsLoader getCurrentLoader()
  {
    return (GemliteSibingsLoader) deploySession.get().get(CURRENT_LOADER);
  }
  
  public RegistryMatchedContext getMatchedContext()
  {
    return (RegistryMatchedContext) deploySession.get().get(CURRENT_MATCHED_REG_CTX);
  }
  
  public void registerDeployListener(DeployListener lisener)
  {
    compositeDpl.registerDeployListener(lisener);
  }
  
  public void removeDeployListener(DeployListener lisener)
  {
    compositeDpl.registerDeployListener(lisener);
  }
  
  public static void main(String[] args)
  {
    try
    {
      ServerConfigHelper.initConfig();
      ServerConfigHelper.initLog4j("log4j-test.xml");
      GemliteDeployer deploy = new GemliteDeployer();
      URL runtimeURL = Util
          .strToURL("file:/d:/code/vmgemlite/vmgemlite/Gemlite-bundle/Gemlite-runtime/target/classes/");
      deploy.deploy(runtimeURL);
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
  }
  
  public RegistryMatchedContext scan(URL url)
  {
    GemliteSibingsLoader loader = new GemliteSibingsLoader(url);
    return scan(loader);
  }
  public RegistryMatchedContext scan(GemliteSibingsLoader loader)
  {
    if(deploySession.get() == null)
    {
        Map<String, Object> m = new HashMap<>();
        deploySession.set(m);
    }
    deploySession.get().put(CURRENT_LOADER, loader);
    ScannerIterator scannerIterator = null;
    try
    {
      scannerIterator = new ScannerIterator(loader.getURL());
      RegistryMatchedContext matchedRegistry = scanner.scan(loader, scannerIterator);
      deploySession.get().put(CURRENT_LOADER, loader);
      deploySession.get().put(CURRENT_MATCHED_REG_CTX, matchedRegistry);
      return matchedRegistry;
    }
    catch (Exception e)
    {
      throw new GemliteException("Scan error , loader url is {}" + loader.getURL(), e);
    }
    finally
    {
      try
      {
        if (scannerIterator != null)
        {
          scannerIterator.close();
        }
      }
      catch (IOException e)
      {
        LogUtil.getCoreLog().warn("Close jar inputstream error.");
      }
    }
  }
  
  public IModuleContext redeploy(String name)
  {
    IModuleContext module = GemliteContext.getModuleContext(name);
    return deploy(module.getJarURL());
  }
  
  /***
   * create new context
   * 
   * @param url
   * @return
   */
  public IModuleContext deploy(URL url)
  {
    if(url==null)
      return null;
    // 扫描并创建新的Module
    ScannerIterator scannerIterator = null;
    try
    {
      GemliteSibingsLoader loader = new GemliteSibingsLoader(url);
      compositeDpl.beforeScan(loader);
      RegistryMatchedContext matchedRegistry = scan(url);
      compositeDpl.afterScan(matchedRegistry);
      IModuleContext context = scanner.createModuleContext(getCurrentLoader(), matchedRegistry);
      // 注销旧的Module
      IModuleContext oldContext = GemliteContext.getModuleContext(context.getModuleName());
      cleanOldContext(oldContext, context.getLoader());
      // 注册新的Module
      ScannedRegistryDefine dpl = scanner.getRegistryDefine(DeployListener.class);
      RegistryParam item2 = matchedRegistry.getSingleItem(dpl);
      if (item2 != null)
      {
        LogUtil.getCoreLog().trace("DeployListener found at {}", context.getModuleName());
      }
      
      compositeDpl.beforeInstantiate(matchedRegistry);
      scanner.register(context, matchedRegistry);
      GemliteContext.getInstance().putModuleContext(context.getModuleName(), context);
      compositeDpl.afterDeploy(context);
      DomainMapperHelper.scanMapperRegistryClass(context.getLoader());
      GemliteEvent event = new GemliteEvent(GemliteEvent.AFTER_DEPLOY);
      EventDispatcher.sendEvent(event);
      LogUtil.getCoreLog().info(context.getModuleName() + " deployed.\n");
      return context;
    }
    catch (Exception e)
    {
      throw new GemliteException("Scan error , loader url is {}" + url, e);
    }
    finally
    {
      try
      {
        if (scannerIterator != null)
        {
          scannerIterator.close();
        }
      }
      catch (IOException e)
      {
        LogUtil.getCoreLog().warn("Close jar inputstream error.");
      }
    }
  }
  
  private void cleanOldContext(IModuleContext oldContext, ClassLoader newLoader)
  {
    if (oldContext != null)
    {
      ClassPathLoader.getLatest().removeAndSetLatest(oldContext.getLoader());
      oldContext.clean();
    }
    ClassPathLoader.getLatest().addOrReplaceAndSetLatest(newLoader);
    try
    {
      TypeRegistry typeRegistry = ((GemFireCacheImpl) CacheFactory.getAnyInstance()).getPdxRegistry();
      if (typeRegistry != null)
        typeRegistry.flushCache();
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().warn(e.getMessage());
    }
  }
  
}
