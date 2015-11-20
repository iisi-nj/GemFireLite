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
package gemlite.core.internal.admin.service;

import gemlite.core.annotations.admin.AdminService;
import gemlite.core.internal.admin.AbstractRemoteAdminService;
import gemlite.core.util.LogUtil;

import java.util.Map;
import java.util.concurrent.CancellationException;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.control.RebalanceOperation;
import com.gemstone.gemfire.cache.control.RebalanceResults;
import com.gemstone.gemfire.cache.control.ResourceManager;

@AdminService(name = "RebalanceService")
public class RebalanceService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    Cache cahce = CacheFactory.getAnyInstance();
    ResourceManager manager = cahce.getResourceManager();
    RebalanceOperation op = manager.createRebalanceFactory().start();
    RebalanceResults results = null;
    try
    {
      results = op.getResults();
    }
    catch (CancellationException e)
    {
      e.printStackTrace();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    if (results != null)
    {
      LogUtil.getAppLog().info("Took " + results.getTotalTime() + " milliseconds\n");
      LogUtil.getAppLog().info("Transfered " + results.getTotalBucketTransferBytes() + "bytes\n");
      String msg = "Took " + results.getTotalTime() + " milliseconds\nTransfered " + results.getTotalBucketTransferBytes() + "bytes\n";
      return msg;
    }
    else
    {
      return false;
    }
  }
  
  @Override
  public void doExeception(Map<String, Object> args)
  {
    
  }
  
}
