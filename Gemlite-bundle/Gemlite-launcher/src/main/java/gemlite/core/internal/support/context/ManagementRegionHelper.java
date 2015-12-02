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
import gemlite.core.internal.support.events.SimpleEntryEvent;
import gemlite.core.internal.support.jmx.domain.GemliteNodeConfig;
import gemlite.core.util.GemliteHelper;
import gemlite.core.util.LogUtil;

import java.util.concurrent.atomic.AtomicBoolean;

import com.gemstone.gemfire.cache.AttributesFactory;
import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionAttributes;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.internal.ClientRegionFactoryImpl;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.internal.cache.InternalRegionArguments;

/***
 * 设置统一的Management region,作为消息通道，作为管理信息持久化和一致性
 * 
 * @author ynd
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class ManagementRegionHelper
{
  public enum MGM_KEYS
  {
    jmx_host, jmx_rmi_port, jmx_http_port, config_db_host, config_db_port,role_jmx_manager
  }
  
  class ManagementRegionListener extends CacheListenerAdapter<String, Object>
  {
    @Override
    public void afterCreate(EntryEvent<String, Object> event)
    {
      if (nodeConfigListener != null && event.getNewValue() instanceof GemliteNodeConfig)
      {
        SimpleEntryEvent<String, Object> e = new SimpleEntryEvent<>(event);
        nodeConfigListener.afterCreate(e);
      }
    }
    
    @Override
    public void afterUpdate(EntryEvent<String, Object> event)
    {
      if (nodeConfigListener != null && event.getNewValue() instanceof GemliteNodeConfig)
      {
        SimpleEntryEvent<String, Object> e = new SimpleEntryEvent<>(event);
        e.setKey(FIX_PREFIX);
        nodeConfigListener.afterUpdate(e);
      }
    }
    
    @Override
    public void afterDestroy(EntryEvent<String, Object> event)
    {
      if (nodeConfigListener != null && event.getNewValue() instanceof GemliteNodeConfig)
      {
        SimpleEntryEvent<String, Object> e = new SimpleEntryEvent<>(event);
        nodeConfigListener.afterDestroy(e);
      }
    }
    
  }
  
  private final static String MGM_REGION_NAME = "_GEMLITE_MGM_";
  private final static ManagementRegionHelper inst = new ManagementRegionHelper();
  private Region<?, ?> configRegion;
  private Region<?, ?> jmxRegion;
  private static AtomicBoolean intialized = new AtomicBoolean();
  
  /***
   * prefix,listenerList
   */
  
  private CacheListener nodeConfigListener;
  private final static String FIX_PREFIX = "gemlite.mgm.region.event.";
  
  private ManagementRegionHelper()
  {
    
  }
  
  private final static Region getConfigRegion()
  {
    if (inst.configRegion == null)
      createConfigRegion();
    return inst.configRegion;
  }
  
  public final static void writeMemberJmxConfig(String key, Object value)
  {
    getConfigRegion().put(key, value);
  }
  
  public final static void setValue(MGM_KEYS key, Object value)
  {
    getConfigRegion().put(key.name(), value);
  }
  
  public final static boolean containsKey(MGM_KEYS key)
  {
    return getConfigRegion().containsKey(key.name());
  }
  public final static Object getValue(MGM_KEYS key)
  {
    return getConfigRegion().get(key.name());
  }
  
  public static void monitorJMXConfig(CacheListener listener)
  {
    inst.nodeConfigListener = listener;
  }
  
  private final RegionAttributes createClientRegionAttribute()
  {
    GemFireCacheImpl cache = GemFireCacheImpl.getInstance();
    ClientRegionFactoryImpl cf = (ClientRegionFactoryImpl) cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
    RegionAttributes ra = (RegionAttributes) GemliteHelper.unsafeInvoke(cf, "createRegionAttributes", null);
    return ra;
  }
  
  private RegionAttributes createServerRegionAttribute(boolean withListener)
  {
    AttributesFactory rf = new AttributesFactory();
    rf.setDataPolicy(DataPolicy.REPLICATE);
    if (withListener)
      rf.addCacheListener(new ManagementRegionListener());
    return rf.create();
  }
  
  private final static void createConfigRegion()
  {
    if (intialized.get())
      return;
    intialized.set(true);
    GemliteRuntimeInfo gri = GemliteContext.getRuntimeInfo();
    RegionAttributes ra = null;
    switch (gri.getNodeType())
    {
      case CLIENT:
        ra = inst.createClientRegionAttribute();
        break;
      case DATASTORE:
        ra = inst.createServerRegionAttribute(false);
        break;
      case LOCATOR:
        ra = inst.createServerRegionAttribute(true);
        break;
      default:
        break;
    }
    GemFireCacheImpl cache = GemFireCacheImpl.getInstance();
    InternalRegionArguments internalArgs = new InternalRegionArguments();
    internalArgs.setIsUsedForMetaRegion(true);
    try
    {
      cache.createVMRegion(MGM_REGION_NAME, ra, internalArgs);
      inst.configRegion = cache.getRegion(MGM_REGION_NAME);
      LogUtil.getCoreLog().debug("Management region created.");
    }
    catch (Exception e)
    {
      intialized.set(false);
      throw new GemliteException("Management region create failure.", e);
    }
    
  }
  
}
