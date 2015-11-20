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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.cache.EntryDestroyedException;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.internal.InternalDataSerializer;
import com.gemstone.gemfire.internal.cache.CachedDeserializable;
import com.gemstone.gemfire.internal.cache.LocalRegion;
import com.gemstone.gemfire.internal.cache.Token;
import com.gemstone.gemfire.internal.util.BlobHelper;

public class SnapshotPacket implements DataSerializable
{
  private static final long serialVersionUID = -4368981007449464743L;
  private int windowId;
  private String packetId;
  private DistributedMember sender;
  private SnapshotRecord[] records;
  
  public SnapshotPacket()
  {
  }
  
  public SnapshotPacket(DataInput in) throws IOException, ClassNotFoundException
  {
    fromData(in);
  }
  
  public SnapshotPacket(int windowId, DistributedMember sender, List<SnapshotRecord> recs)
  {
    this.windowId = windowId;
    this.packetId = UUID.randomUUID().toString();
    this.sender = sender;
    this.records = ((SnapshotRecord[]) recs.toArray(new SnapshotRecord[recs.size()]));
  }
  
  public int getWindowId()
  {
    return this.windowId;
  }
  
  public String getPacketId()
  {
    return this.packetId;
  }
  
  public DistributedMember getSender()
  {
    return this.sender;
  }
  
  public SnapshotRecord[] getRecords()
  {
    return this.records;
  }
  
  public void toData(DataOutput out) throws IOException
  {
    out.writeInt(this.windowId);
    InternalDataSerializer.writeString(this.packetId, out);
    InternalDataSerializer.writeObject(this.sender, out);
    InternalDataSerializer.writeArrayLength(this.records.length, out);
    for (SnapshotRecord rec : this.records)
      rec.toData(out);
  }
  
  public void fromData(DataInput in) throws IOException, ClassNotFoundException
  {
    this.windowId = in.readInt();
    this.packetId = InternalDataSerializer.readString(in);
    this.sender = ((DistributedMember) InternalDataSerializer.readObject(in));
    
    int count = InternalDataSerializer.readArrayLength(in);
    this.records = new SnapshotRecord[count];
    
    for (int i = 0; i < count; ++i)
    {
      SnapshotRecord rec = new SnapshotRecord();
      rec.fromData(in);
      this.records[i] = rec;
    }
  }
  
  public static class SnapshotRecord implements DataSerializable
  {
    private static final long serialVersionUID = -6438619684354075289L;
    private byte[] key;
    private byte[] value;
    
    public SnapshotRecord()
    {
    }
    
    public SnapshotRecord(byte[] key, byte[] value)
    {
      this.key = key;
      this.value = value;
    }
    
    public <K, V> SnapshotRecord(K keyObj, V valObj) throws IOException
    {
      this.key = BlobHelper.serializeToBlob(keyObj);
      this.value = convertToBytes(valObj);
    }
    
    public <K, V> SnapshotRecord(DataInput in) throws IOException, ClassNotFoundException
    {
      fromData(in);
    }
    
    public <K, V> SnapshotRecord(LocalRegion region, Map.Entry<K, V> entry) throws IOException
    {
      this.key = BlobHelper.serializeToBlob(entry.getKey());
      if ((entry instanceof LocalRegion.NonTXEntry) && (region != null))
        this.value = convertToBytes(((LocalRegion.NonTXEntry) entry).getRegionEntry().getValueInVMOrDiskWithoutFaultIn(
            region));
      else
        this.value = convertToBytes(entry.getValue());
    }
    
    public byte[] getKey()
    {
      return this.key;
    }
    
    public byte[] getValue()
    {
      return this.value;
    }
    
    public Object getKeyObject() throws IOException, ClassNotFoundException
    {
      return BlobHelper.deserializeBlob(this.key);
    }
    
    public Object getValueObject() throws IOException, ClassNotFoundException
    {
      return (this.value == null) ? null : BlobHelper.deserializeBlob(this.value);
    }
    
    public boolean hasValue()
    {
      return this.value != null;
    }
    
    public int getSize()
    {
      return this.key.length + ((this.value == null) ? 0 : this.value.length);
    }
    
    public void toData(DataOutput out) throws IOException
    {
      InternalDataSerializer.writeByteArray(this.key, out);
      InternalDataSerializer.writeByteArray(this.value, out);
    }
    
    public void fromData(DataInput in) throws IOException, ClassNotFoundException
    {
      this.key = InternalDataSerializer.readByteArray(in);
      this.value = InternalDataSerializer.readByteArray(in);
    }
    
    private byte[] convertToBytes(Object val) throws IOException
    {
      if (Token.isRemoved(val))
        throw new EntryDestroyedException();
      if (Token.isInvalid(val))
        return null;
      if (val instanceof CachedDeserializable)
        return ((CachedDeserializable) val).getSerializedValue();
      if (val != null)
      {
        return BlobHelper.serializeToBlob(val);
      }
      return null;
    }
  }
}