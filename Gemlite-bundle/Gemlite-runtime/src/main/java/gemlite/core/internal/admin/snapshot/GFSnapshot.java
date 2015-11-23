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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.cache.CacheClosedException;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.pdx.internal.EnumInfo;
import com.gemstone.gemfire.pdx.internal.PdxType;
import com.gemstone.gemfire.pdx.internal.TypeRegistry;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GFSnapshot
{
  public static final int SNAP_VER_1 = 1;
  public static final int SNAP_VER_2 = 2;
  private static final byte[] SNAP_FMT = { 71, 70, 83 };
  
  public static void main(String[] args) throws Exception
  {
    if (args.length != 1)
    {
      System.out.println("Usage: GFSnapshot <file>");
      System.exit(1);
    }
    
    GFSnapshotImporter imp = new GFSnapshotImporter(new File(args[0]));
    try
    {
      System.out.println("Snapshot format is version " + imp.getVersion());
      System.out.println("Snapshot region is " + imp.getRegionName());
      
      ExportedRegistry reg = imp.getPdxTypes();
      Map<Integer, PdxType> types = reg.types();
      System.out.println("Found " + types.size() + " PDX types:");
      for (Map.Entry entry : types.entrySet())
      {
        System.out.println("\t" + entry.getKey() + " = " + entry.getValue());
      }
      
      Map<Integer, EnumInfo> enums = reg.enums();
      System.out.println("Found " + enums.size() + " PDX enums: ");
      for (Map.Entry entry : enums.entrySet())
      {
        System.out.println("\t" + entry.getKey() + " = " + entry.getValue());
      }
      
      System.out.println();
      
      SnapshotPacket.SnapshotRecord record = imp.readSnapshotRecord();
      while (record != null)
      {
        System.out.println(record.getKeyObject() + " = " + record.getValueObject());
        record = imp.readSnapshotRecord();
      }
    }
    finally
    {
      imp.close();
    }
  }
  
  public static SnapshotWriter create(File snapshot, String region) throws IOException
  {
    final GFSnapshotExporter out = new GFSnapshotExporter(snapshot, region);
    return new SnapshotWriter()
    {
      public void snapshotEntry(SnapshotPacket.SnapshotRecord entry) throws IOException
      {
        out.writeSnapshotEntry(entry);
      }
      
      public void snapshotComplete() throws IOException
      {
        out.close();
      }
    };
  }
  
  public static <K, V> SnapshotIterator<K, V> read(File snapshot) throws IOException, ClassNotFoundException
  {
    return new SnapshotIterator()
    {
      GFSnapshot.GFSnapshotImporter in;
      private boolean foundNext;
      private Map.Entry<K, V> next;
      
      public boolean hasNext() throws IOException, ClassNotFoundException
      {
        if (!this.foundNext)
        {
          return moveNext();
        }
        return true;
      }
      
      public Map.Entry<K, V> next() throws IOException, ClassNotFoundException
      {
        if ((!this.foundNext) && (!moveNext()))
        {
          throw new NoSuchElementException();
        }
        Map.Entry result = this.next;
        
        this.foundNext = false;
        this.next = null;
        
        return result;
      }
      
      public void close() throws IOException
      {
        this.in.close();
      }
      
      private boolean moveNext() throws IOException, ClassNotFoundException
      {
        SnapshotPacket.SnapshotRecord record;
        if ((record = this.in.readSnapshotRecord()) != null)
        {
          this.foundNext = true;
          
          final Object key = record.getKeyObject();
          final Object value = record.getValueObject();
          
          this.next = new Map.Entry()
          {
            public K getKey()
            {
              return (K) key;
            }
            
            public V getValue()
            {
              return (V) value;
            }
            
            public V setValue(Object value)
            {
              throw new UnsupportedOperationException();
            }
            
          };
          return true;
        }
        
        close();
        return false;
      }
    };
  }
  
  static class GFSnapshotImporter
  {
    private final byte version;
    private final String region;
    private final ExportedRegistry pdx;
    private final DataInputStream dis;
    
    public GFSnapshotImporter(File in) throws IOException, ClassNotFoundException
    {
      this.pdx = new ExportedRegistry();
      
      FileInputStream fis = new FileInputStream(in);
      FileChannel fc = fis.getChannel();
      DataInputStream tmp = new DataInputStream(fis);
      long entryPosition;
      try
      {
        this.version = tmp.readByte();
        if (this.version == 1)
        {
          throw new IOException("1");
        }
        if (this.version == 2)
        {
          byte[] format = new byte[3];
          tmp.readFully(format);
          if (!Arrays.equals(format, GFSnapshot.SNAP_FMT))
          {
            throw new IOException("not equals");
          }
          
          long registryPosition = tmp.readLong();
          
          this.region = tmp.readUTF();
          entryPosition = fc.position();
          
          if (registryPosition != -1L)
          {
            fc.position(registryPosition);
            this.pdx.fromData(tmp);
          }
        }
        else
        {
          throw new IOException(this.version + "");
        }
      }
      finally
      {
        tmp.close();
      }
      
      checkPdxTypeCompatibility();
      checkPdxEnumCompatibility();
      
      this.dis = new DataInputStream(new BufferedInputStream(new FileInputStream(in)));
      this.dis.skip(entryPosition);
    }
    
    public byte getVersion()
    {
      return this.version;
    }
    
    public String getRegionName()
    {
      return this.region;
    }
    
    public ExportedRegistry getPdxTypes()
    {
      return this.pdx;
    }
    
    public SnapshotPacket.SnapshotRecord readSnapshotRecord() throws IOException, ClassNotFoundException
    {
      byte[] key = DataSerializer.readByteArray(this.dis);
      if (key == null)
      {
        return null;
      }
      
      byte[] value = DataSerializer.readByteArray(this.dis);
      return new SnapshotPacket.SnapshotRecord(key, value);
    }
    
    public void close() throws IOException
    {
      this.dis.close();
    }
    
    private TypeRegistry getRegistry()
    {
      GemFireCacheImpl gfc = GemFireCacheImpl.getInstance();
      if (gfc != null)
      {
        return gfc.getPdxRegistry();
      }
      return null;
    }
    
    private void checkPdxTypeCompatibility()
    {
      TypeRegistry tr = getRegistry();
      if (tr == null)
      {
        return;
      }
      
      // Map types = new TreeMap(this.pdx.types());
      // Collection entries = tr.typeMap().entrySet();
      // for (Map.Entry entry : entries) {
      // PdxType conflicting =
      // this.pdx.getType(((Integer)entry.getKey()).intValue());
      // if ((conflicting != null) &&
      // (!((PdxType)entry.getValue()).equals(conflicting))) {
      // throw new
      // PdxSerializationException(LocalizedStrings.Snapshot_PDX_CONFLICT_0_1.toLocalizedString(new
      // Object[] { entry.getValue(), conflicting }));
      // }
      //
      // }
      //
      // for (Map.Entry entry : types.entrySet()) {
      // int id = tr.defineType((PdxType)entry.getValue());
      // if (id != ((Integer)entry.getKey()).intValue()) {
      // PdxType existing = tr.getType(((Integer)entry.getKey()).intValue());
      // throw new
      // PdxSerializationException(LocalizedStrings.Snapshot_PDX_CONFLICT_0_1.toLocalizedString(new
      // Object[] { existing, entry.getValue() }));
      // }
      // }
    }
    
    private void checkPdxEnumCompatibility()
    {
      TypeRegistry tr = getRegistry();
      if (tr == null)
      {
        return;
      }
      
      // Map enums = new TreeMap(this.pdx.enums());
      // Collection entries = tr.enumMap().entrySet();
      // for (Map.Entry entry : entries) {
      // EnumInfo conflicting = (EnumInfo)enums.remove(entry.getKey());
      // if ((conflicting != null) &&
      // (!((EnumInfo)entry.getValue()).equals(conflicting))) {
      // throw new
      // PdxSerializationException(LocalizedStrings.Snapshot_PDX_CONFLICT_0_1.toLocalizedString(new
      // Object[] { entry.getValue(), conflicting }));
      // }
      //
      // }
      //
      // for (Map.Entry entry : enums.entrySet()) {
      // int id = tr.defineEnum((EnumInfo)entry.getValue());
      // if (id != ((Integer)entry.getKey()).intValue()) {
      // EnumInfo existing =
      // tr.getEnumInfoById(((Integer)entry.getKey()).intValue());
      // throw new
      // PdxSerializationException(LocalizedStrings.Snapshot_PDX_CONFLICT_0_1.toLocalizedString(new
      // Object[] { existing, entry.getValue() }));
      // }
      // }
    }
  }
  
  static class GFSnapshotExporter
  {
    private final FileChannel fc;
    private final DataOutputStream dos;
    
    public GFSnapshotExporter(File out, String region) throws IOException
    {
      FileOutputStream fos = new FileOutputStream(out);
      this.fc = fos.getChannel();
      
      this.dos = new DataOutputStream(new BufferedOutputStream(fos));
      
      this.dos.writeByte(2);
      
      this.dos.write(GFSnapshot.SNAP_FMT);
      
      this.dos.writeLong(-1L);
      
      this.dos.writeUTF(region);
    }
    
    public void writeSnapshotEntry(SnapshotPacket.SnapshotRecord entry) throws IOException
    {
      entry.toData(this.dos);
    }
    
    public void close() throws IOException
    {
      DataSerializer.writeByteArray(null, this.dos);
      
      this.dos.flush();
      long registryPosition = this.fc.position();
      try
      {
        GemFireCacheImpl cache = GemFireCacheImpl
            .getForPdx("PDX registry is unavailable because the Cache has been closed.");
        new ExportedRegistry(cache.getPdxRegistry()).toData(this.dos);
      }
      catch (CacheClosedException e)
      {
        new ExportedRegistry().toData(this.dos);
      }
      
      this.dos.flush();
      this.fc.position(4L);
      this.dos.writeLong(registryPosition);
      
      this.dos.close();
    }
  }
  
  public static abstract interface SnapshotWriter
  {
    public abstract void snapshotEntry(SnapshotPacket.SnapshotRecord paramSnapshotRecord) throws IOException;
    
    public abstract void snapshotComplete() throws IOException;
  }
}
