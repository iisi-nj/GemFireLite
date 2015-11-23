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

import gemlite.core.support.IPartitionResolver;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

/***
 * key相同
 * @author ynd
 *
 */
public class ItemByKey implements DataSerializable,IPartitionResolver
{
  /**
   * 
   */
  private static final long serialVersionUID = 6615968155381257726L;
  public Object key;
  public Object originKey;
  public HashMap<String, String> values;
  public boolean nodeChange;
  public String operation;
  
  public ItemByKey()
  {
	  super();	  
  }
  
  public ItemByKey(Object key, Object originKey, HashMap<String, String> values, boolean nodeChange, String operation)
  {
    super();
    this.key = key;
    this.originKey = originKey;
    this.values = values;
    this.nodeChange = nodeChange;
    this.operation = operation;
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
    ItemByKey other = (ItemByKey) obj;
    if (key == null)
    {
      if (other.key != null)
        return false;
    }
    else if (!key.equals(other.key))
      return false;
    return true;
  }

  @Override
  public void toData(DataOutput out) throws IOException
  {
    DataSerializer.writeObject(key, out);
    DataSerializer.writeObject(originKey, out);
    DataSerializer.writeBoolean(nodeChange, out);
    DataSerializer.writeHashMap(values, out);
    DataSerializer.writeString(operation, out);
   
  }

  @Override
  public void fromData(DataInput in) throws IOException, ClassNotFoundException
  {
    key = DataSerializer.readObject(in);
    originKey = DataSerializer.readObject(in);
    nodeChange = DataSerializer.readBoolean(in);
    values= DataSerializer.readHashMap(in);
    operation = DataSerializer.readString(in);
  }

  public Object getKey()
  {
    return key;
  }

  public void setKey(Object key)
  {
    this.key = key;
  }

  public Object getOriginKey()
  {
    return originKey;
  }

  public void setOriginKey(Object originKey)
  {
    this.originKey = originKey;
  }

  public HashMap<String, String> getValues()
  {
    return values;
  }

  public void setValues(HashMap<String, String> values)
  {
    this.values = values;
  }

  public boolean isNodeChange()
  {
    return nodeChange;
  }

  public void setNodeChange(boolean nodeChange)
  {
    this.nodeChange = nodeChange;
  }

  public String getOperation()
  {
    return operation;
  }

  public void setOperation(String operation)
  {
    this.operation = operation;
  }

  @Override
  public Object getResolver()
  {
    return key;
  }
  
}
