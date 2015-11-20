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

import gemlite.core.commands.DataStore;
import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.support.events.GemliteEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;

/***
 * regionName -> index tool 1,index tool2
 * 
 * @author ynd
 * 
 */

@GemliteRegistry(value=ApplicationListener.class)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ApplicationListenerRegistry extends AbstractGemliteRegistry
{
  private List<ApplicationListener> listeners = new ArrayList<>();
  
  @Override
  protected void doRegistry(IModuleContext context, RegistryParam param) throws Exception
  {
    String className = param.getClassName();
    Class<ApplicationListener<GemliteEvent>> cls = (Class<ApplicationListener<GemliteEvent>>) context.getLoader()
        .loadClass(className);
    ApplicationListener listener = cls.newInstance();
    listeners.add(listener);
    if (getApplicationEventMulticaster() != null)
      getApplicationEventMulticaster().addApplicationListener(listener);
  }
  
  private synchronized ApplicationEventMulticaster getApplicationEventMulticaster()
  {
    try
    {
      Field field = DataStore.class.getDeclaredField("mainContext");
      field.setAccessible(true);
      AbstractApplicationContext ctx = (AbstractApplicationContext) field.get(DataStore.getInstance());
      if (ctx == null)
        return null;
      Method m = AbstractApplicationContext.class.getDeclaredMethod("getApplicationEventMulticaster");
      m.setAccessible(true);
      ApplicationEventMulticaster eventCaster = (ApplicationEventMulticaster) m.invoke(ctx);
      field.setAccessible(false);
      m.setAccessible(false);
      return eventCaster;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  @Override
  public void cleanAll()
  {
    for (ApplicationListener l : listeners)
    {
      getApplicationEventMulticaster().removeApplicationListener(l);
    }
    
  }
  
  @Override
  public Object getItem(Object key)
  {
    return null;
  }
}
