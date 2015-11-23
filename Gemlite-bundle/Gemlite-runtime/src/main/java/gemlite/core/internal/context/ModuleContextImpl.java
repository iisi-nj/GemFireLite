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
package gemlite.core.internal.context;

import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.context.IGemliteRegistry;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleContextImpl extends DeployableContext
{
  /***
   * 
   * @param name
   * @param type
   * @param loader
   */
  public ModuleContextImpl(String name, ModuleType type, GemliteSibingsLoader loader)
  {
    super(name, type, loader);
  }
  
  private Map<String, IGemliteRegistry> registryCache = new ConcurrentHashMap<>();
  
  //private IGemliteClassScanner scanner;
 // private IGemliteBuilder builder;
  
  protected Set<String> classNames;// 记录所有的类名
  
  public void register()
  {
//    init();
    Iterator<IGemliteRegistry> it = getRegistryCache().values().iterator();
    while (it.hasNext())
    {
      IGemliteRegistry reg = it.next();
      if(LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug("Do registry,"+reg);
      reg.register(this);
    }
    
  }
  
  public void setRegistryCache(Map<String, IGemliteRegistry> registryCache)
  {
    this.registryCache = registryCache;
  }
  
  @Override
  public boolean isCoreModule()
  {
    return super.isCoreModule();
  }
  
  @Override
  public void updateRegistryCache(Map<String, IGemliteRegistry> cache)
  {
    registryCache.clear();
    registryCache.putAll(cache);
  }
  
  @Override
  public IGemliteRegistry getRegistry(Class<?> cls)
  {
    String desc =Util.getInternalDesc(cls);
    return registryCache.get(desc);
  }
  
  @Override
  public Map<String, IGemliteRegistry> getRegistryCache()
  {
    return registryCache;
  }
  
  @Override
  public void clean()
  {
    super.clean();
    if (registryCache != null)
      for (IGemliteRegistry reg : registryCache.values())
      {
        reg.cleanAll();
      }
    LogUtil.getCoreLog().info("Module context clean done.");
  }
  
//  public IGemliteClassScanner getScanner()
//  {
//    return scanner;
//  }
//  
//  public void setScanner(IGemliteClassScanner scanner)
//  {
//    this.scanner = scanner;
//  }
  
//  public IGemliteBuilder getBuilder()
//  {
//    return builder;
//  }
//  
//  public void setBuilder(IGemliteBuilder builder)
//  {
//    this.builder = builder;
//  }
  
//  @Override
//  public void init()
//  {
//    if (isCoreModule())
//    {
//      MeasureHelper.init();
//      IndexMeasureHelper.init();
//      GemliteNode.getInstance().initMXBeanEnviroment();
//      try
//      {
//        IndexHelper.loadAllIndexsFromDB();
//      }
//      catch(CannotCreateTransactionException e)
//      {
//          LogUtil.getCoreLog().warn("Load All indexs from db failure",e);
//      }
//      catch(Exception e)
//      {
//          LogUtil.getCoreLog().error("Load Index error:",e);
//      }
//    }
//  }

  
}
