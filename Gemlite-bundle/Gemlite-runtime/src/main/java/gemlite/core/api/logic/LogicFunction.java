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
package gemlite.core.api.logic;

import gemlite.core.api.ApiConstant;
import gemlite.core.internal.logic.IRemoteService;
import gemlite.core.internal.measurement.MeasureHelper;
import gemlite.core.internal.measurement.RemoteServiceStat;
import gemlite.core.internal.measurement.RemoteServiceStatItem;
import gemlite.core.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LogicFunction implements Function, ApiConstant
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
    Set filter = null;
    if (fc instanceof RegionFunctionContext)
    {
      RegionFunctionContext rc = (RegionFunctionContext) fc;
      filter = rc.getFilter();
    }
    LogicSession session = LogicSession.getSession();
    Map<String, Object> map = (Map<String, Object>) fc.getArguments();
    String moduleName = (String) map.get(PARAM_MODULE_NAME);
    String beanName = (String) map.get(PARAM_BEAN_NAME);
    Object userArgs = map.get(PARAM_URSER_ARGS);
    long l0 = System.currentTimeMillis();
    IRemoteService service =null;
    try
    {
      RemoteServiceStat rss = MeasureHelper.getRemoteServiceStat(moduleName, beanName);
      session.setModuleName(moduleName);
//      session.put(PARAM_URSER_ARGS, userArgs);
//      session.put(PARAM_URSER_FILTERS, filter);
      RemoteServiceStatItem si = new RemoteServiceStatItem();
      si.setModuleName(moduleName);
      si.setServiceName(beanName);
      session.setRemoteServiceStatItem(si);
      WeakReference<Thread> ref = new WeakReference<Thread>(Thread.currentThread());
      si.setThread(ref);
      si.setStart(l0);
      service = LogicServices.getService(moduleName, beanName);
      Object result = null;
      if (service != null)
      {
        result = service.doExecute(userArgs,filter);
      }
      long lsend = System.currentTimeMillis();
      si.setSend(lsend);
      fc.getResultSender().lastResult(result);
      si.getThread().clear();
      long l1 = System.currentTimeMillis();
      si.setEnd(l1);
      
      if(rss != null)
      {
    	  rss.incrTotalCount();
    	  rss.incrTotalCost(l1 - l0);
      }
      MeasureHelper.remoteServiceEnd(si);
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("",e);
      fc.getResultSender().sendException(e);
    }
    finally
    {
      session.clear();
    }
  }
  
  @Override
  public String getId()
  {
    return ApiConstant.REMOTE_SERVICE_FUNCTION;
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
