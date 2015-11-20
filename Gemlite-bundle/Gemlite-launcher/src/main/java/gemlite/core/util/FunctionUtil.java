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
package gemlite.core.util;

import gemlite.core.internal.support.FunctionIds;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.DeployParameter;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;

import java.net.URL;
import java.util.List;

import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

@SuppressWarnings("rawtypes")
public class FunctionUtil
{
  
  public final static String deploy(URL url)
  {
    IModuleContext mc= GemliteDeployer.getInstance().deploy(url);
    DeployParameter param = new DeployParameter(mc.getModuleType(),url);
    return deploy(param);
  }
  public final static String deploy(DeployParameter param)
  {
    Execution ex = FunctionService.onServers(ClientCacheFactory.getAnyInstance().getDefaultPool());
    ex = ex.withArgs(param);
    ResultCollector rc = ex.execute(FunctionIds.DEPLOY_FUNCTION);
    List rs = (List) rc.getResult();
    String str = (String) rs.get(0);
    return str;
  }
  public final static <T> T onServer(String functionId, Class<T> T)
  {
    return onServer(functionId, null, T);
  }
  
  @SuppressWarnings("unchecked")
  public final static <T> T onServer(String functionId, Object args, Class<T> T)
  {
    Execution ex = FunctionService.onServer(ClientCacheFactory.getAnyInstance().getDefaultPool());
    if (args != null)
      ex = ex.withArgs(args);
    ResultCollector rc = ex.execute(functionId);
    List<T> l = (List<T>) rc.getResult();
    return l.get(0);
  }

}
