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

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"rawtypes","unchecked"})
public class ProcessorKeeper21
{
  //private final ObjIdConcurrentMap<Object> map = new ObjIdConcurrentMap();
  private final ConcurrentMap map = new ConcurrentHashMap();
  private final boolean useWeakRefs;
  private final AtomicInteger nextKey = new AtomicInteger(1);
  
  public ProcessorKeeper21()
  {
    this(true);
  }
  
  public ProcessorKeeper21(boolean useWeakRefs)
  {
    this.useWeakRefs = useWeakRefs;
  }
  
  private int getNextId()
  {
    int id = this.nextKey.getAndIncrement();
    if (id <= 0)
    {
      synchronized (this.nextKey)
      {
        id = this.nextKey.get();
        if (id <= 0)
        {
          this.nextKey.set(1);
        }
      }
      id = this.nextKey.getAndIncrement();
    }
    return id;
  }
  
  public int put(Object processor)
  {
    Object obj;
    if (this.useWeakRefs)
    {
      obj = new WeakReference(processor);
    }
    else
      obj = processor;
    int id;
    do
      id = getNextId();
    while (this.map.putIfAbsent(id, obj) != null);
    return id;
  }
  
  public Object retrieve(int id)
  {
    Object o = null;
    if (this.useWeakRefs)
    {
      WeakReference ref = (WeakReference) this.map.get(id);
      if (ref != null)
      {
        o = ref.get();
        if (o == null)
        {
          this.map.remove(id, ref);
        }
      }
    }
    else
    {
      o = this.map.get(id);
    }
    
    return o;
  }
  
  public void remove(int id)
  {
    this.map.remove(id);
  }
}