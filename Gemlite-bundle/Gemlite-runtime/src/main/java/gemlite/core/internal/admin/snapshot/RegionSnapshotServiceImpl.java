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

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.internal.ProxyRegion;
import com.gemstone.gemfire.cache.execute.ResultSender;
import com.gemstone.gemfire.internal.cache.CachedDeserializableFactory;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.internal.cache.LocalDataSet;
import com.gemstone.gemfire.internal.cache.LocalRegion;
import com.gemstone.gemfire.internal.cache.Token;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RegionSnapshotServiceImpl<K, V> implements RegionSnapshotService<K, V>
{
  private static final int IMPORT_CONCURRENCY;
  static final int BUFFER_SIZE;
  private final Region<K, V> region;
  
  public RegionSnapshotServiceImpl(Region<K, V> region)
  {
    this.region = region;
  }
  
  public SnapshotOptions<K, V> createOptions()
  {
    return new SnapshotOptionsImpl();
  }
  
  public void save(File snapshot, SnapshotOptions.SnapshotFormat format,ResultSender sender) throws IOException
  {
    save(snapshot, format, createOptions(),sender);
  }
  
  public void save(File snapshot, SnapshotOptions.SnapshotFormat format, SnapshotOptions<K, V> options,ResultSender sender)
      throws IOException
  {
    assert (!CacheFactory.getAnyInstance().getCacheTransactionManager().exists());
    
    Exporter exp = createExporter(this.region);
    
    long count = 0L;
    GFSnapshot.SnapshotWriter writer = GFSnapshot.create(snapshot, this.region.getFullPath());
    try
    {
      SnapshotWriterSink sink = new SnapshotWriterSink(writer);
      count = exp.export(this.region, sink, options,sender);
    }
    finally
    {
      writer.snapshotComplete();
      sender.sendResult("Finish export data,Region:"+this.region.getFullPath()+" count:"+count+" !!!");
      LogUtil.getAppLog().info("Finish export data,Region:"+this.region.getFullPath()+" count:"+count+" !!!");
    }
  }
  
  public void load(File snapshot, SnapshotOptions.SnapshotFormat format,ResultSender sender) throws IOException, ClassNotFoundException
  {
    load(snapshot, format, createOptions(),sender);
  }
  
  public void load(File snapshot, SnapshotOptions.SnapshotFormat format, SnapshotOptions<K, V> options,ResultSender sender)
      throws IOException, ClassNotFoundException
  {
    final LocalRegion local = getLocalRegion(this.region);
    
    long count = 0L;
    long bytes = 0L;
    
    LinkedList puts = new LinkedList();
    GFSnapshot.GFSnapshotImporter in = new GFSnapshot.GFSnapshotImporter(snapshot);
    try
    {
      int bufferSize = 0;
      int putCount = 0;
      Map buffer = new HashMap();
      
      SnapshotPacket.SnapshotRecord record = in.readSnapshotRecord();
      int no = 1;
      while (record != null)
      {
        bytes += record.getSize();
        Object key = record.getKeyObject();
        
        Object val = Token.INVALID;
        if (record.hasValue())
        {
          byte[] data = record.getValue();
          
          if ((data.length > 0) && (data[0] == 46))
          {
            val = record.getValueObject();
          }
          else
            val = CachedDeserializableFactory.create(record.getValue());
          
        }
        
        if (includeEntry(options, key, val))
        {
          buffer.put(key, val);
          bufferSize += record.getSize();
          count += 1L;
          putCount += 1;
          
          if (bufferSize > BUFFER_SIZE)
          {
            if (puts.size() == IMPORT_CONCURRENCY)
            {
              ((Future) puts.removeFirst()).get();
            }
            
            final Map copy = new HashMap(buffer);
            ExecutorService executor = (ExecutorService) GemFireCacheImpl.getExisting("Importing region from snapshot")
                .getDistributionManager().getWaitingThreadPool();
            Future f = executor.submit(new Runnable()
            {
              public void run()
              {
                basicImportPutAll(local, copy);
              }
            });
            puts.addLast(f);
            buffer.clear();
            sender.sendResult("region:"+ this.region.getFullPath()+" "+no+" input buffersize:"+bufferSize+" count:"+putCount+" total count:"+count+" total bytes:"+bytes);
            LogUtil.getAppLog().info("region:"+ this.region.getFullPath()+" "+no+" input buffersize:"+bufferSize+" count:"+putCount+" total count:"+count+" total bytes:"+bytes);
            no++;
            bufferSize = 0;
            putCount = 0;
          }
        }
        record = in.readSnapshotRecord();
      }
      
      if (!buffer.isEmpty())
      {
        basicImportPutAll(local,buffer);
        sender.sendResult("finish region:"+ this.region.getFullPath()+" "+no+" input buffersize:"+bufferSize+" count:"+putCount+" total count:"+count+" total bytes:"+bytes);
        LogUtil.getAppLog().info("finish region:"+ this.region.getFullPath()+" "+no+" input buffersize:"+bufferSize+" count:"+putCount+" total count:"+count+" total bytes:"+bytes);
        no++;
      }
      else
      {
        sender.sendResult("finish region:"+ this.region.getFullPath()+" input buffersize:"+bufferSize+" count:"+putCount+" total count:"+count+" total bytes:"+bytes);
        LogUtil.getAppLog().info("finish region:"+ this.region.getFullPath()+" input buffersize:"+bufferSize+" count:"+putCount+" total count:"+count+" total bytes:"+bytes);
      }
      while (!puts.isEmpty())
      {
        ((Future) puts.removeFirst()).get();
      }
    }
    catch (InterruptedException e)
    {
      if (!puts.isEmpty())
      {
        ((Future) puts.removeFirst()).cancel(true);
      }
      throw ((IOException) new InterruptedIOException().initCause(e));
    }
    catch (ExecutionException e)
    {
      if (!puts.isEmpty()) 
      {
        ((Future) puts.removeFirst()).cancel(true);
      }
      throw new IOException(e);
    }
    finally
    {
      in.close();
    }
  }
  
  private void basicImportPutAll(LocalRegion local,Map copy)
  {
    boolean skipCallbacks = true;
//    EntryEventImpl event = new EntryEventImpl(local, Operation.PUTALL_CREATE, null, null, null, true, local.getCache().getMyId(), !skipCallbacks);
    //gemfire663H,„ã
//    DistributedPutAllOperation putAllOp = new DistributedPutAllOperation(event, copy.size(), false);
    //local.basicPutAll(copy, putAllOp);
    //7.0H,„(b„
    local.basicImportPutAll(copy, skipCallbacks);
  }
  
  private boolean includeEntry(SnapshotOptions<K, V> options, Object key, Object val)
  {
    // if (options.getFilter() != null) {
    // Map.Entry entry = new Map.Entry() {
    // public V setValue(V value) { throw new UnsupportedOperationException(); }
    // public K getKey() { return this.val$key; }
    // public V getValue() {
    // if (val instanceof CachedDeserializable) {
    // return ((CachedDeserializable)this.val$val).getDeserializedForReading();
    // }
    // return null;
    // }
    // };
    // return options.getFilter().accept(entry);
    // }
    return true;
  }
  
  static <K, V> Exporter<K, V> createExporter(Region<?, ?> region)
  {
//    String pool = region.getAttributes().getPoolName();
    // if (pool != null) {
    // return new ClientExporter(PoolManager.find(pool));
    // }
    // if (InternalDistributedSystem.getAnyInstance().isLoner())
    // {
    // return new LocalExporter();
    // }
    
    return new WindowedExporter();
  }
  
  static LocalRegion getLocalRegion(Region<?, ?> region)
  {
    if (region instanceof LocalDataSet)
      return ((LocalDataSet) region).getProxy();
    if (region instanceof ProxyRegion)
    {
      return (LocalRegion) ((ProxyRegion) region).getRealRegion();
    }
    return (LocalRegion) region;
  }
  
  static
  {
    IMPORT_CONCURRENCY = Integer.getInteger("gemfire.RegionSnapshotServiceImpl.IMPORT_CONCURRENCY", 10).intValue();
    BUFFER_SIZE = Integer.getInteger("gemfire.RegionSnapshotServiceImpl.BUFFER_SIZE", 1048576).intValue();
  }
  
  static final class ResultSenderSink implements RegionSnapshotServiceImpl.ExportSink
  {
    private final ResultSender<SnapshotPacket.SnapshotRecord[]> sender;
    
    public ResultSenderSink(ResultSender<SnapshotPacket.SnapshotRecord[]> sender)
    {
      this.sender = sender;
    }
    
    public void write(SnapshotPacket.SnapshotRecord[] records) throws IOException
    {
      this.sender.sendResult(records);
    }
  }
  
  static final class SnapshotWriterSink implements RegionSnapshotServiceImpl.ExportSink
  {
    private final GFSnapshot.SnapshotWriter writer;
    private long bytes;
    
    public SnapshotWriterSink(GFSnapshot.SnapshotWriter writer)
    {
      this.writer = writer;
    }
    
    public void write(SnapshotPacket.SnapshotRecord[] records) throws IOException
    {
      for (SnapshotPacket.SnapshotRecord rec : records)
      {
        this.writer.snapshotEntry(rec);
        this.bytes += rec.getSize();
      }
    }
    
    public long getBytesWritten()
    {
      return this.bytes;
    }
  }
  
  public static abstract interface Exporter<K, V>
  {
    public abstract long export(Region<K, V> paramRegion, RegionSnapshotServiceImpl.ExportSink paramExportSink,
        SnapshotOptions<K, V> paramSnapshotOptions,ResultSender sender) throws IOException;
  }
  
  public static abstract interface ExportSink
  {
    public abstract void write(SnapshotPacket.SnapshotRecord[] paramArrayOfSnapshotRecord) throws IOException;
  }
}