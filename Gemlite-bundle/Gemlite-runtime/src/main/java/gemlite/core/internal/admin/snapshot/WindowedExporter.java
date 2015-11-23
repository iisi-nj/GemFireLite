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

import gemlite.core.util.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultSender;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;
import com.gemstone.gemfire.distributed.internal.ReplyProcessor21;
import com.gemstone.gemfire.internal.cache.LocalRegion;
import com.gemstone.gemfire.internal.cache.execute.InternalExecution;
import com.gemstone.gemfire.internal.cache.execute.LocalResultCollector;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class WindowedExporter<K, V> implements RegionSnapshotServiceImpl.Exporter<K, V>
{
  public static final int WINDOW_SIZE = Integer.getInteger("gemfire.WindowedExporter.WINDOW_SIZE", 10).intValue();
  
  public long export(Region<K, V> region, RegionSnapshotServiceImpl.ExportSink sink, SnapshotOptions<K, V> options,ResultSender sender)
      throws IOException
  {
    if(LogUtil.getAppLog().isInfoEnabled())
      LogUtil.getAppLog().info("gemlite.gf.plugin.snapshot.WindowedExporter Start export!");
    long count = 0L;
    boolean error = true;
    LocalRegion local = RegionSnapshotServiceImpl.getLocalRegion(region);
    
    SnapshotPacket last = new SnapshotPacket();
    DistributedMember me = CacheFactory.getAnyInstance().getDistributedSystem().getDistributedMember();
    
    Map param = new HashMap();
    WindowedExportCollector results = new WindowedExportCollector(local, last);
    try
    {
      param.put("me", me.getId());
      InternalExecution exec = (InternalExecution) FunctionService.onRegion(region).withArgs(param).withCollector(results);
      exec.execute("WindowedExportFunction");
      BlockingQueue queue = results.getResult();
      SnapshotPacket packet;
      while ((packet = (SnapshotPacket) queue.take()) != last)
      {
        results.ack(packet);
        sink.write(packet.getRecords());
        count += packet.getRecords().length;
        sender.sendResult("export data,Region:"+region.getFullPath()+" total count:"+count);
      }
      
      error = false;
      FunctionException ex = results.getException();
      if (ex != null)
        throw new IOException(ex);
    }
    catch (FunctionException e)
    {
      e.printStackTrace();
      LogUtil.getAppLog().error("error FunctionException",e);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      LogUtil.getAppLog().error("error InterruptedException",e);
    }
    catch(IOException e)
    {
      e.printStackTrace();
      LogUtil.getAppLog().error("error IOException:",e);
    }
    finally
    {
      if (error)
      {
        results.abort();
      }
    }
    return count;
  }
  
  private static class WindowedExportCollector implements LocalResultCollector<Object, BlockingQueue<SnapshotPacket>>
  {
    private final LocalRegion region;
    private final SnapshotPacket end;
    private final BlockingQueue<SnapshotPacket> entries;
    private final AtomicBoolean done;
    private final Map<DistributedMember, Integer> members;
    private volatile FunctionException exception;
    private volatile ReplyProcessor21 processor;
    
    public WindowedExportCollector(LocalRegion region, SnapshotPacket end)
    {
      this.region = region;
      this.end = end;
      
      this.done = new AtomicBoolean(false);
      this.members = new ConcurrentHashMap();
      
      this.entries = new LinkedBlockingQueue();
    }
    
    public BlockingQueue<SnapshotPacket> getResult() throws FunctionException
    {
      return this.entries;
    }
    
    public BlockingQueue<SnapshotPacket> getResult(long timeout, TimeUnit unit) throws FunctionException,
        InterruptedException
    {
      return getResult();
    }
    
    public FunctionException getException()
    {
      return this.exception;
    }
    
    public void abort()
    {
      try
      {
        if (this.done.compareAndSet(false, true))
        {
          if (InternalDistributedSystem.getLoggerI18n().fineEnabled())
          {
            InternalDistributedSystem.getLoggerI18n().fine("SNP: Aborting export of region");
          }
          this.entries.clear();
          this.entries.put(this.end);
          
          for (Map.Entry entry : this.members.entrySet())
            sendAbort((DistributedMember) entry.getKey(), ((Integer) entry.getValue()).intValue());
        }
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
      }
    }
    
    public void ack(SnapshotPacket packet)
    {
      FlowController.getInstance().sendAck(this.region.getDistributionManager(), packet.getSender(),
          packet.getWindowId(), packet.getPacketId());
    }
    
    public void addResult(DistributedMember memberID, Object result)
    {
      if (!(result instanceof Throwable))
      {
        int flowId = ((SnapshotPacket) result).getWindowId();
        if (this.done.get())
        {
          sendAbort(memberID, flowId);
        }
        else
        {
          this.members.put(memberID, Integer.valueOf(flowId));
        }
      }
      
      if (this.done.get())
      {
        return;
      }
      try
      {
        if (result instanceof Throwable)
        {
          setException((Throwable) result);
          endResults();
        }
        else
        {
          SnapshotPacket sp = (SnapshotPacket) result;
          this.entries.put(sp);
        }
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
      }
    }
    
    public void endResults()
    {
      try
      {
        if (this.done.compareAndSet(false, true))
        {
          if (LogUtil.getAppLog().isInfoEnabled())
          {
            LogUtil.getAppLog().info("SNP: All results received for export of region " + this.region.getName());
          }
          this.entries.put(this.end);
        }
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();
      }
    }
    
    public void clearResults()
    {
      this.entries.clear();
      this.done.set(false);
      this.exception = null;
    }
    
    public void setException(Throwable ex)
    {
      this.exception = ((ex instanceof FunctionException) ? (FunctionException) ex : new FunctionException(ex));
    }
    
    public void setProcessor(ReplyProcessor21 processor)
    {
      this.processor = processor;
    }
    
    public ReplyProcessor21 getProcessor()
    {
      return this.processor;
    }
    
    private void sendAbort(DistributedMember member, int flowId)
    {
      FlowController.getInstance().sendAbort(this.region.getDistributionManager(), flowId, member);
    }
  }
}
