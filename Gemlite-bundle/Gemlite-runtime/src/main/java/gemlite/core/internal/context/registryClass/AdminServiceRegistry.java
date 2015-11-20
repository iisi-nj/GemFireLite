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

import gemlite.core.annotations.admin.AdminService;
import gemlite.core.internal.admin.AbstractRemoteAdminService;
import gemlite.core.internal.logic.IRemoteAdminService;
import gemlite.core.internal.measurement.RemoteServiceStat;
import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.support.jmx.GemliteNode;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 
 * @author gsong
 * 
 */

@GemliteRegistry(value=AdminService.class)
@SuppressWarnings("rawtypes")
public class AdminServiceRegistry extends AbstractGemliteRegistry
{
  
  private Map<String, IRemoteAdminService> beanMap = new ConcurrentHashMap<>();
  private Map<String, RemoteServiceStat> mbeanMap = new ConcurrentHashMap<>();
  
  private void setValue(AbstractRemoteAdminService bean, String field, Object value) throws NoSuchFieldException,
      SecurityException, IllegalArgumentException, IllegalAccessException
  {
    Field f = AbstractRemoteAdminService.class.getDeclaredField(field);
    f.setAccessible(true);
    f.set(bean, value);
    f.setAccessible(false);
  }
  
  @Override
  protected void doRegistry(IModuleContext context, RegistryParam param) throws Exception
  {
    String className = param.getClassName();
    Class cls = context.getLoader().loadClass(className);
    AbstractRemoteAdminService bean = (AbstractRemoteAdminService) cls.newInstance();
    AdminService adminService = bean.getClass().getAnnotation(AdminService.class);
    setValue(bean, "moduleName", context.getModuleName());
    setValue(bean, "beanName", adminService.name());
    beanMap.put(adminService.name(), bean);
    
  }
  
  private void createMBean(IModuleContext context, String srvName)
  {
    RemoteServiceStat srv = new RemoteServiceStat();
    srv.setModuleName(context.getModuleName());
    srv.setNodeName(ServerConfigHelper.getConfig(ITEMS.NODE_NAME));
    srv.setServiceName(srvName);
    mbeanMap.put(srvName, srv);
    GemliteNode.getInstance().registerBean(srvName, srv);
  }
  
  @Override
  public void cleanAll()
  {
  }
  
  @Override
  public Object getItem(Object key)
  {
    return beanMap.get(key);
  }
  
}
