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
package gemlite.core.internal.mq.domain;

import gemlite.core.internal.mq.MqConstant;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

public class MqSyncDomain implements DataSerializable,Comparable<MqSyncDomain>
{
  private static final long serialVersionUID = -3437296083789102960L;

  // 时间戳 用于比较 同步更新的时序
  private long timestamp;
  
  //标记，是insert/update还是delete
  private String op;
  
  private Object key;
  
  private Object oldKey;
  
  //传递的结果值，insert/update时是valueMap,detele时为空
  private HashMap<String, String> value;
  
  private String tableName;
  
  private String regionName;
  
  public boolean isPkChanged()
  {
    return MqConstant.UPDATE.equals(op)&&oldKey!=null&&!oldKey.equals(key);
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((key == null) ? 0 : key.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MqSyncDomain other = (MqSyncDomain) obj;
    if (key == null)
    {
      if (other.key != null)
        return false;
    }
    else if (!key.equals(other.key))
      return false;
    if (oldKey == null)
    {
      if (other.oldKey != null)
        return false;
    }
    else if (!oldKey.equals(other.oldKey))
      return false;
    if (op == null)
    {
      if (other.op != null)
        return false;
    }
    else if (!op.equals(other.op))
      return false;
    if (regionName == null)
    {
      if (other.regionName != null)
        return false;
    }
    else if (!regionName.equals(other.regionName))
      return false;
    if (tableName == null)
    {
      if (other.tableName != null)
        return false;
    }
    else if (!tableName.equals(other.tableName))
      return false;
    if (timestamp != other.timestamp)
      return false;
    if (value == null)
    {
      if (other.value != null)
        return false;
    }
    else if (!value.equals(other.value))
      return false;
    return true;
  }

  @Override
  public void toData(DataOutput out) throws IOException
  {
    DataSerializer.writePrimitiveLong(timestamp, out);
    DataSerializer.writeString(op, out);
    DataSerializer.writeObject(key, out);
    DataSerializer.writeObject(oldKey, out);
    DataSerializer.writeHashMap(value, out);
    DataSerializer.writeString(tableName, out);
    DataSerializer.writeString(regionName, out);
  }

  @Override
  public void fromData(DataInput in) throws IOException, ClassNotFoundException
  {
    timestamp = DataSerializer.readPrimitiveLong(in);
    op = DataSerializer.readString(in);
    key = DataSerializer.readObject(in);
    oldKey = DataSerializer.readObject(in);
    value = DataSerializer.readHashMap(in);
    tableName = DataSerializer.readString(in);
    regionName = DataSerializer.readString(in);
  }

  public long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }

  public String getOp()
  {
    return op;
  }

  public void setOp(String op)
  {
    this.op = op;
  }

  public Object getKey()
  {
    return key;
  }

  public void setKey(Object key)
  {
    this.key = key;
  }


  public String getTableName()
  {
    return tableName;
  }

  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }

  public Object getOldKey()
  {
    return oldKey;
  }

  public void setOldKey(Object oldKey)
  {
    this.oldKey = oldKey;
  }

  public String getRegionName()
  {
    return regionName;
  }

  public void setRegionName(String regionName)
  {
    this.regionName = regionName;
  }

  @Override
  public String toString()
  {
    return "MqSyncDomain [" + timestamp + "," + op + ", " + key + "," + oldKey + ","
        + value + "," + tableName + "," + regionName + "]";
  }

  @Override
  public int compareTo(MqSyncDomain o)
  {
    return (timestamp>o.getTimestamp())?1:(timestamp==o.getTimestamp()?0:-1);
  }

  public HashMap<String, String> getValue()
  {
    return value;
  }

  public void setValue(HashMap<String, String> value)
  {
    this.value = value;
  }
  
}

