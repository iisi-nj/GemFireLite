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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionEvent;
import com.gemstone.gemfire.cache.RegionMembershipListener;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.util.RegionMembershipListenerAdapter;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.distributed.internal.DM;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FlowController
{
  private static int MAX_PERMITS = 1073741823;
  private static FlowController instance = new FlowController();
  private ProcessorKeeper21 processors;
  
  public static FlowController getInstance()
  {
    return instance;
  }
  
  private FlowController()
  {
    this.processors = new ProcessorKeeper21();
  }
  
  public <K, V> Window create(Region<K, V> region, String sink, int windowSize)
  {
    WindowImpl w = new WindowImpl(region, sink, windowSize);
    int id = this.processors.put(w);
    
    w.setWindowId(id);
    return w;
  }
  
  public void sendAck(DM dmgr, DistributedMember member, int windowId, String packetId)
  {
    if (LogUtil.getAppLog().isDebugEnabled())
    {
      LogUtil.getAppLog().debug("Sending ACK for packet " + packetId + " on window " + windowId + " to member " + member);
    }
    if (dmgr.getDistributionManagerId().equals(member))
    {
      processACK(packetId, windowId);
    }
    else
    {
      Map param = new HashMap();
      param.put("windowId", windowId);
      param.put("packetId", packetId);
      param.put("command", "ack");
      Execution ex = FunctionService.onMember(member).withArgs(param);
      ex.execute("ACKFunction");
    }
  }
  
  
  public void sendAbort(DM dmgr, int windowId, DistributedMember member)
  {
    if (LogUtil.getAppLog().isDebugEnabled())
    {
      LogUtil.getAppLog().debug("Sending ABORT to member " + member + " for window " + windowId);
    }
    if (dmgr.getDistributionManagerId().equals(member))
    {
      processAbort(windowId);
    }
    else
    {
      Map param = new HashMap();
      param.put("windowId", windowId);
      param.put("command", "abort");
      Execution ex = FunctionService.onMember(member).withArgs(param);
      ex.execute("ACKFunction");
    }
  }
  
  /**
   * 处理ack
   * @param packetId
   * @param windowId
   */
  public void processACK(String packetId,int windowId)
  {
    if (LogUtil.getAppLog().isDebugEnabled())
    {
      LogUtil.getAppLog().debug("Received ACK for packet " + packetId + " at window " + windowId + " on host:" + System.getProperty(ITEMS.BINDIP.name())+" node:"+System.getProperty(ITEMS.NODE_NAME.name()));
    }
    WindowImpl win = (FlowController.WindowImpl)FlowController.getInstance().processors.retrieve(windowId);
    if (win != null)
    win.ack(packetId);
  }
  
  public void processAbort(int windowId)
  {
    if(LogUtil.getAppLog().isDebugEnabled())
    {
      LogUtil.getAppLog().debug("Received Abort for window " + windowId + " on host:" + System.getProperty(ITEMS.BINDIP.name())+" node:"+System.getProperty(ITEMS.NODE_NAME.name()));
    }
    WindowImpl win = (WindowImpl) this.processors.retrieve(windowId);
    if (win != null)
    win.abort();
  }
  
  private static class WindowImpl<K, V> implements FlowController.Window
  {
    private final Semaphore permits;
    private final AtomicBoolean abort;
    private final Region<K, V> region;
    private final RegionMembershipListener<K, V> crash;
    private volatile int windowId;
    
    public WindowImpl(Region<K, V> region, String sink, int size)
    {
      this.permits = new Semaphore(size);
      this.abort = new AtomicBoolean(false);
      
      this.region = region;
      final String dmId = sink;
      this.crash = new RegionMembershipListenerAdapter()
      {
        public void afterRemoteRegionCrash(RegionEvent event)
        {
          if (event.getDistributedMember().getId().equals(dmId))
          {
            if (InternalDistributedSystem.getLoggerI18n().fineEnabled())
            {
              InternalDistributedSystem.getLoggerI18n().fine(dmId + " has crashed, closing window");
            }
            FlowController.WindowImpl.this.abort();
          }
        }
      };
      region.getAttributesMutator().addCacheListener(this.crash);
    }
    
    public void close()
    {
      if(LogUtil.getAppLog().isDebugEnabled())
      {
        LogUtil.getAppLog().debug("---------------close window---------------");
      }
      FlowController.instance.processors.remove(this.windowId);
      this.region.getAttributesMutator().removeCacheListener(this.crash);
      this.permits.release(MAX_PERMITS);
      if(LogUtil.getAppLog().isDebugEnabled())
      {
        LogUtil.getAppLog().debug("---------------after close window availablePermits:"+permits.availablePermits());
      }
    }
    
    public int getWindowId()
    {
      return this.windowId;
    }
    
    public boolean isAborted()
    {
      return this.abort.get();
    }
    
    public boolean isOpen()
    {
      return this.permits.availablePermits() > 0;
    }
    
    public void waitForOpening() throws InterruptedException
    {
      if(LogUtil.getAppLog().isDebugEnabled())
      {
        LogUtil.getAppLog().debug("this.permits.availablePermits:"+this.permits.availablePermits());
      }
      this.permits.acquire();
    }
    
    private void ack(String packetId)
    {
      if(LogUtil.getAppLog().isDebugEnabled())
      {
        LogUtil.getAppLog().debug("=============release a packetId:"+packetId);
      }
      this.permits.release();
      if(LogUtil.getAppLog().isDebugEnabled())
      {
        LogUtil.getAppLog().debug("=============after release availablePermits:"+permits.availablePermits());
      }
    }
    
    private void abort()
    {
      if(LogUtil.getAppLog().isDebugEnabled())
      {
        LogUtil.getAppLog().debug("---------------abort---------------");
      }
      this.abort.set(true);
      this.permits.release(MAX_PERMITS);
      if(LogUtil.getAppLog().isDebugEnabled())
      {
        LogUtil.getAppLog().debug("---------------after abort availablePermits:"+permits.availablePermits());
      }
    }
    
    private void setWindowId(int id)
    {
      this.windowId = id;
    }
  }
  
  public static abstract interface Window
  {
    public abstract int getWindowId();
    
    public abstract boolean isAborted();
    
    public abstract boolean isOpen();
    
    public abstract void waitForOpening() throws InterruptedException;
    
    public abstract void close();
  }
}
