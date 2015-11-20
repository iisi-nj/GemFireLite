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
package gemlite.core.internal.jmx.manage;

import gemlite.core.annotations.mbean.GemliteMBean;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;
import gemlite.core.internal.support.jmx.AggregateType;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;
import gemlite.core.util.LogUtil;

import java.io.Serializable;
import java.util.Iterator;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@GemliteMBean(name="HotDeploy",config=true)
@ManagedResource
public class HotdeployStat implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 2246835067352948574L;
  
  public HotdeployStat()
  {
  }
  
  
  @ManagedOperation
  @AggregateOperation(value=AggregateType.OPONLYONE)
  public String getModuleNames()
  {
    Iterator<String> names =  GemliteContext.getModuleNames();
    StringBuilder builder = new StringBuilder();
    while(names.hasNext())
    {
      IModuleContext ctx = GemliteContext.getModuleContext(names.next());
      builder.append(ctx.getModuleName()).append("\t").append(ctx.getLoader()).append("\n");
    }
    return builder.toString();
  }
  
  @ManagedOperation
  @AggregateOperation
  public boolean reload(String moduleName)
  {
    IModuleContext mod= GemliteContext.getModuleContext(moduleName);
    try
    {
      GemliteDeployer.getInstance().deploy(mod.getLoader().getURL());
    }
    catch (GemliteException e)
    {
      LogUtil.getCoreLog().error(e);
      return false;
    }
    return true;
  }
}
