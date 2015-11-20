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
package gemlite.core.internal.admin;

import gemlite.core.api.ApiConstant;
import gemlite.core.internal.logic.IRemoteAdminService;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
@SuppressWarnings({"unchecked","rawtypes"})
public class AdminFunction implements Function,ApiConstant
{
  
  private static final long serialVersionUID = -8681108988781270789L;
  
  @Override
  public boolean hasResult()
  {
    return true;
  }
  
  @Override
  public void execute(FunctionContext fc)
  {
    AdminSession session = AdminSession.getSession();
    try
    {
      Map<String, Object> map = (Map<String, Object>) fc.getArguments();
      String moduleName = GemliteContext.GEMLITE_RUNTIME_MODULE;
      String beanName = (String) map.get(PARAM_BEAN_NAME);
      Object userArgs = map.get(PARAM_URSER_ARGS);
//      long l0 = System.currentTimeMillis();
//      RemoteServiceStat rss =MeasureHelper.getRemoteServiceStat(moduleName, beanName);
//      session.setModuleName(moduleName);
//      RemoteServiceStatItem si = new RemoteServiceStatItem();
//      si.setModuleName(moduleName);
//      si.setServiceName(beanName);
//      session.put(beanName, si);
//      WeakReference<Thread> ref = new WeakReference<Thread>(Thread.currentThread());
//      si.setThread(ref);
      
      IRemoteAdminService service = AdminServices.getService(moduleName, beanName);
      Object result = service.doExecute(userArgs);
      
      fc.getResultSender().lastResult(result);
//      si.getThread().clear();
//      long l1 = System.currentTimeMillis();
//      rss.incrTotalCount();
//      rss.incrTotalCost(l1-l0);
//      MeasureHelper.remoteServiceEnd(moduleName,beanName,l0,l1);
    }
    catch (Exception e)
    {
        LogUtil.getCoreLog().error("AdminFunction error:",e);
        fc.getResultSender().lastResult(new HashMap());
    }
    finally
    {
      session.clear();
    }
  }
  
  @Override
  public String getId()
  {
    return ApiConstant.REMOTE_ADMIN_FUNCTION;
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
