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
package gemlite.core.internal.mq.server;

import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.mq.domain.ItemByKey;
import gemlite.core.util.LogUtil;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import com.gemstone.gemfire.cache.Region;

@SuppressWarnings("rawtypes")
public class AsyncProcessTask extends RecursiveAction
{
  /**
   * 
   */
  private static final long serialVersionUID = -7419370279407032311L;
  
  public Region region;
  public IMapperTool tool;
  public List<ItemByKey> items;
  public int hi;
  public int lo;
  private int threshold = 10;
  private MqSyncExecutor executor;
  
  public AsyncProcessTask(MqSyncExecutor executor,Region region, IMapperTool tool, List<ItemByKey> items, int lo, int hi)
  {
    super();
    this.region = region;
    this.tool = tool;
    this.items = items;
    this.hi = hi;
    this.lo = lo;
  }
  
  @Override
  protected void compute()
  {
    int left = hi - lo;
    if (LogUtil.getMqSyncLog().isDebugEnabled())
      LogUtil.getMqSyncLog().debug("Merge domain,threshold=" + threshold + "," + hi + "-" + lo + " left=" + left);
    if (left <= threshold)
    {
      
      for (int i = lo; i < hi; i++)
      {
        ItemByKey item = items.get(i);
        executor.processOneItem(region, item, tool);
      }
      if (LogUtil.getMqSyncLog().isDebugEnabled())
        LogUtil.getMqSyncLog().debug("Done. " + hi + "-" + lo);
    }
    else
    {
      int mid = (lo + hi) / 2;
      if (LogUtil.getMqSyncLog().isDebugEnabled())
        LogUtil.getMqSyncLog().debug("Split," + lo + "-" + mid + " and " + (mid + 1) + "-" + hi);
      
      //AsyncProcessTask t1 = new AsyncProcessTask(executor,region, tool, items, left, mid);
      //AsyncProcessTask t2 = new AsyncProcessTask(executor,region, tool, items, left, mid);
      AsyncProcessTask t1 = new AsyncProcessTask(executor,region, tool, items, lo, mid);
      AsyncProcessTask t2 = new AsyncProcessTask(executor,region, tool, items, mid, hi);
      invokeAll(t1, t2);
    }
  }
  
}
