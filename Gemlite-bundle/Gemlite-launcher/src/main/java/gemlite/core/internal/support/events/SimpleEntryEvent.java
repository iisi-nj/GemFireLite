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
package gemlite.core.internal.support.events;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.Operation;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.SerializedCacheValue;
import com.gemstone.gemfire.cache.TransactionId;
import com.gemstone.gemfire.distributed.DistributedMember;

public class SimpleEntryEvent<K,V> implements EntryEvent<K, V>
{

  private Region<K, V> r;
  public SimpleEntryEvent(EntryEvent<K, V> src)
  {
    this.r = src.getRegion();
    this.op = src.getOperation();
    this.arg = src.getCallbackArgument();
    this.key = src.getKey();
    this.oldValue = src.getOldValue();
    this.soldValue = src.getSerializedOldValue();
    this.newValue = src.getNewValue();
    this.snewValue = src.getSerializedNewValue();
  }
  
  

  public SimpleEntryEvent(Region<K, V> r, Operation op, Object arg, K key, V oldValue, SerializedCacheValue<V> soldValue,
      V newValue, SerializedCacheValue<V> snewValue)
  {
    super();
    this.r = r;
    this.op = op;
    this.arg = arg;
    this.key = key;
    this.oldValue = oldValue;
    this.soldValue = soldValue;
    this.newValue = newValue;
    this.snewValue = snewValue;
  }



  @Override
  public Region<K, V> getRegion()
  {
    return r;
  }
  
  private Operation op;
  @Override
  public Operation getOperation()
  {
    return op;
  }
  
  private Object arg;

  @Override
  public Object getCallbackArgument()
  {
    return arg;
  }

  @Override
  public boolean isCallbackArgumentAvailable()
  {
    return false;
  }

  @Override
  public boolean isOriginRemote()
  {
    return false;
  }

  @Override
  public DistributedMember getDistributedMember()
  {
    return null;
  }

  @Override
  public boolean isExpiration()
  {
    return false;
  }

  @Override
  public boolean isDistributed()
  {
    return false;
  }

  private K key;
  @Override
  public K getKey()
  {
    return key;
  }
  
  public void setKey(K k)
  {
    key = k;
  }

  private V oldValue;
  @Override
  public V getOldValue()
  {
    return oldValue;
  }

  private SerializedCacheValue<V> soldValue;
  @Override
  public SerializedCacheValue<V> getSerializedOldValue()
  {
    return soldValue;
  }

  private V newValue;
  @Override
  public V getNewValue()
  {
    return newValue;
  }
  
  private SerializedCacheValue<V> snewValue;

  @Override
  public SerializedCacheValue<V> getSerializedNewValue()
  {
    return snewValue;
  }

  @Override
  public boolean isLocalLoad()
  {
    return false;
  }

  @Override
  public boolean isNetLoad()
  {
    return false;
  }

  @Override
  public boolean isLoad()
  {
    return false;
  }

  @Override
  public boolean isNetSearch()
  {
    return false;
  }

  @Override
  public TransactionId getTransactionId()
  {
    return null;
  }

  @Override
  public boolean isBridgeEvent()
  {
    return false;
  }

  @Override
  public boolean hasClientOrigin()
  {
    return false;
  }

  @Override
  public boolean isOldValueAvailable()
  {
    return false;
  }



  @Override
  public String toString()
  {
    return "AsyncEvent [r=" + r + ", op=" + op + ", arg=" + arg + ", key=" + key + ", oldValue=" + oldValue
        + ", soldValue=" + soldValue + ", newValue=" + newValue + ", snewValue=" + snewValue + "]";
  }

  
}
