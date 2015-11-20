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

import gemlite.core.annotations.mbean.GemliteMBean;
import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.support.jmx.GemliteNode;
import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.core.util.LogUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/***
 * 
 * @author ynd
 * 
 */
@GemliteRegistry(GemliteMBean.class)
@SuppressWarnings("rawtypes")
public class MBeanRegistry extends AbstractGemliteRegistry
{
  
  private Set<String> objectNames = new HashSet<>();
  
  @SuppressWarnings("unchecked")
  @Override
  protected void doRegistry(IModuleContext context, RegistryParam param) throws Exception
  {
    String className = param.getClassName();
    Class cls = context.getLoader().loadClass(className);
    Object bean = cls.newInstance();
    GemliteMBean gm = (GemliteMBean) cls.getAnnotation(GemliteMBean.class);
    if (gm.config())
    {
      LogUtil.getCoreLog().debug("Hotdeploy config mxbean.");
      String oname = MBeanHelper.createManagerMBeanName(gm.name());
      GemliteNode.getInstance().registerBean(oname, bean);
//      MBeanHelper.registerModelMBean(oname, bean);
      objectNames.add(oname);
      if (LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug("MBean " + oname + " Class:" + className + " registered.");
      
    }
    
  }
  
  @Override
  public void cleanAll()
  {
    try
    {
      Iterator<String> it = objectNames.iterator();
      while (it.hasNext())
      {
        MBeanHelper.unregister(it.next());
      }
      LogUtil.getCoreLog().info("Mbean removed.");
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("MBean removed failure.", e);
    }
    
  }
  
  @Override
  public Object getItem(Object key)
  {
    return objectNames;
  }
  
  @Override
  public Object getItems()
  {
    return objectNames;
  }
}
