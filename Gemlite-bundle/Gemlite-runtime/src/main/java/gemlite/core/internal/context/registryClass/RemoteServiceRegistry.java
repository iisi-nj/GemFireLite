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
package gemlite.core.internal.context.registryClass;

import gemlite.core.annotations.logic.RemoteService;
import gemlite.core.api.logic.AbstractRemoteService;
import gemlite.core.internal.logic.IRemoteService;
import gemlite.core.internal.measurement.RemoteServiceStat;
import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.support.jmx.GemliteNode;
import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 
 * @author ynd
 * 
 */
@GemliteRegistry(RemoteService.class)
@SuppressWarnings("rawtypes")
public class RemoteServiceRegistry extends AbstractGemliteRegistry
{
  
  private Map<String, IRemoteService> beanMap = new ConcurrentHashMap<>();
  private static Map<String,RemoteServiceStat> mbeanMap = new ConcurrentHashMap<>();
  private static Set<String> objectNames = new HashSet<>();
  
  public RemoteServiceRegistry()
  {
    
  }
  
  private void setValue(AbstractRemoteService bean, String field, Object value) throws NoSuchFieldException,
      SecurityException, IllegalArgumentException, IllegalAccessException
  {
    Field f= AbstractRemoteService.class.getDeclaredField(field);
    f.setAccessible(true);
    f.set(bean, value);
    f.setAccessible(false);
  }
  @Override
  protected void doRegistry(IModuleContext context, RegistryParam param) throws Exception
  {
    String className = param.getClassName();
      Class cls = context.getLoader().loadClass(className);
      AbstractRemoteService bean = (AbstractRemoteService)cls.newInstance();
      RemoteService remoteService = bean.getClass().getAnnotation(RemoteService.class);
      setValue(bean, "moduleName", context.getModuleName());
      setValue(bean, "regionName", remoteService.region());
      setValue(bean, "beanName", remoteService.name());
      beanMap.put(remoteService.name(), bean);
      
      if(!context.isCoreModule())
      {
        createMBean(context, remoteService.name());
      }
       
  }
  
  private void createMBean(IModuleContext context,String srvName)
  {
    String oname = MBeanHelper.createServiceMBeanName(srvName);
    if(objectNames.contains(oname))
      return;
    RemoteServiceStat srv = new RemoteServiceStat();
    srv.setModuleName(context.getModuleName());
    srv.setNodeName(ServerConfigHelper.getConfig(ITEMS.NODE_NAME));
    srv.setServiceName(srvName);
    mbeanMap.put(srvName, srv);
    objectNames.add(oname);
    GemliteNode.getInstance().registerBean(oname, srv);
  }
  
  public RemoteServiceStat getStatItem(String srvName)
  {
    return mbeanMap.get(srvName);
  }
  
  @Override
  public void cleanAll()
  {
    beanMap.clear();
    //removed,because we need mbean hold the old value and show the history
//    try
//    {
//      Iterator<String> it = objectNames.iterator();
//      while(it.hasNext())
//      {
//        MBeanHelper.unregister(it.next());
//      }
//      LogUtil.getCoreLog().info("Remote service Mbean removed.");
//    }
//    catch (Exception e)
//    {
//      LogUtil.getCoreLog().error("Remote service Mbean removed failure.",e);
//    }
   
  }
  
  @Override
  public Object getItem(Object key)
  {
    return beanMap.get(key);
  }

  public Map<String, RemoteServiceStat> getMbeanMap()
  {
    return mbeanMap;
  }
  
  public Map<String,IRemoteService> getServiceMap()
  {
    return beanMap;
  }
  
}
