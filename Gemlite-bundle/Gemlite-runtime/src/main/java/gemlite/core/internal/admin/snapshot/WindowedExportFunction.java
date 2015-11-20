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
package gemlite.core.internal.admin.snapshot;

import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.EntryDestroyedException;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.gemstone.gemfire.cache.execute.ResultSender;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.internal.cache.LocalRegion;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class WindowedExportFunction implements Function
{
  private static final long serialVersionUID = 8165740216491381910L;
  private volatile transient FlowController.Window window;
  
  public void execute(FunctionContext context)
  {
    if(LogUtil.getAppLog().isInfoEnabled())
      LogUtil.getAppLog().info("WindowedExportFunction start,node:"+System.getProperty(ITEMS.NODE_NAME.name()));
    RegionFunctionContext ctx = (RegionFunctionContext) context;
    
    Map args = (HashMap) ctx.getArguments();
    ResultSender rs = ctx.getResultSender();
    
    Region region = ctx.getDataSet();
    if (PartitionRegionHelper.isPartitionedRegion(region))
    {
      region = PartitionRegionHelper.getLocalDataForContext(ctx);
    }
    
    LocalRegion local = RegionSnapshotServiceImpl.getLocalRegion(region);
    this.window = FlowController.getInstance().create(region, (String)args.get("me"), WindowedExporter.WINDOW_SIZE);
    try
    {
      int bufferSize = 0;
      List buffer = new ArrayList();
      DistributedMember me = CacheFactory.getAnyInstance().getDistributedSystem().getDistributedMember();
      for (Iterator iter = region.entrySet().iterator(); (iter.hasNext()) && (!this.window.isAborted());)
      {
        Map.Entry entry = (Map.Entry) iter.next();
        try
        {
          SnapshotOptions options = (SnapshotOptions)args.get("options");
          if (options == null || (options.getFilter() == null) || (options.getFilter().accept(entry)))
          {
            SnapshotPacket.SnapshotRecord rec = new SnapshotPacket.SnapshotRecord(local, entry);
            buffer.add(rec);
            bufferSize += rec.getSize();
          }
        }
        catch (EntryDestroyedException e)
        {
          e.printStackTrace();
          LogUtil.getAppLog().error("error EntryDestroyedException:",e);
        }
        catch (IOException e)
        {
          e.printStackTrace();
          throw new FunctionException(e);
        }
        
        if (bufferSize > RegionSnapshotServiceImpl.BUFFER_SIZE)
        {
          this.window.waitForOpening();
          rs.sendResult(new SnapshotPacket(this.window.getWindowId(), me, buffer));
          if(LogUtil.getAppLog().isInfoEnabled())
            LogUtil.getAppLog().info("Sent entries in region " + region.getName() + " size:"+buffer.size());
          buffer.clear();
          bufferSize = 0;
        }
      }
      
      this.window.waitForOpening();
      rs.lastResult(new SnapshotPacket(this.window.getWindowId(), me, buffer));
      if(LogUtil.getAppLog().isInfoEnabled())
        LogUtil.getAppLog().info("Sent all entries in region " + region.getName() + " last size:"+buffer.size());
    }
    catch (InterruptedException e) 
    {
      e.printStackTrace();
      LogUtil.getAppLog().error("InterruptedException error:",e);
    }
    finally
    {
      this.window.close();
    }
  }
  
  public boolean hasResult()
  {
    return true;
  }
    
  public String getId()
  {
    return "WindowedExportFunction";
  }
  
  public boolean optimizeForWrite()
  {
    return false;
  }
  
  public boolean isHA()
  {
    return true;
  }
}
