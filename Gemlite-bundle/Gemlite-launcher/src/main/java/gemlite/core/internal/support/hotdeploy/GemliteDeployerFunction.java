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

import gemlite.core.internal.support.FunctionIds;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.finderClass.JpaURLFinder;
import gemlite.core.util.LogUtil;

import java.io.BufferedInputStream;
import java.net.URL;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class GemliteDeployerFunction implements Function
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -6255282249130350337L;
  
  @Override
  public boolean hasResult()
  {
    return true;
  }
  
  @Override
  public void execute(FunctionContext fc)
  {
    try
    {
      DeployParameter dp = (DeployParameter) fc.getArguments();
      if (dp == null)
      {
        fc.getResultSender().lastResult("Deploy jar error,deploy parameter is null.");
        return;
      }
      
      byte[] bytes = dp.getBytes();
      if (bytes == null)
      {
        BufferedInputStream bi = new BufferedInputStream(dp.getUrl().openStream());
        bytes = new byte[bi.available()];
        bi.read(bytes);
        bi.close();
      }
      URL localUrl = JpaURLFinder.createTempJarFile(dp.getModuleName(), bytes);
      IModuleContext context = GemliteContext.getInstance().onDeployed(localUrl);
      String result = context != null ? "success" : "failure";
      fc.getResultSender().lastResult("Deploy jar,url=" + dp + " result:" + result);
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("Deploy function ,", e);
      fc.getResultSender().lastResult("Deploy failure:" + e.getMessage());
      fc.getResultSender().sendException(new GemliteException(e));
    }
  }
  
  @Override
  public String getId()
  {
    return FunctionIds.DEPLOY_FUNCTION;
  }
  
  @Override
  public boolean optimizeForWrite()
  {
    return false;
  }
  
  @Override
  public boolean isHA()
  {
    return false;
  }
  
}
