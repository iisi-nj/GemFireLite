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
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import java.util.Collection;
//import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.Set;
//import java.util.concurrent.locks.ReentrantLock;
//
//@SuppressWarnings({"rawtypes","unchecked"})
//public class ObjIdConcurrentMap<V> implements Serializable
//{
//  private static final long serialVersionUID = 7249069246763182397L;
//  static final int DEFAULT_INITIAL_CAPACITY = 16;
//  static final float DEFAULT_LOAD_FACTOR = 0.75F;
//  static final int DEFAULT_CONCURRENCY_LEVEL = 16;
//  static final int MAXIMUM_CAPACITY = 1073741824;
//  static final int MAX_SEGMENTS = 65536;
//  static final int RETRIES_BEFORE_LOCK = 2;
//  final int segmentMask;
//  final int segmentShift;
//  final Segment<V>[] segments;
//  transient Set<Entry<V>> entrySet;
//  transient Collection<V> values;
//  
//  private static int hash(int h)
//  {
//    h += (h << 15 ^ 0xFFFFCD7D);
//    h ^= h >>> 10;
//    h += (h << 3);
//    h ^= h >>> 6;
//    h += (h << 2) + (h << 14);
//    return h ^ h >>> 16;
//  }
//  
//  final Segment<V> segmentFor(int hash)
//  {
//    return this.segments[(hash >>> this.segmentShift & this.segmentMask)];
//  }
//  
//  public ObjIdConcurrentMap(int initialCapacity, float loadFactor, int concurrencyLevel)
//  {
//    if ((loadFactor <= 0.0F) || (initialCapacity < 0) || (concurrencyLevel <= 0))
//    {
//      throw new IllegalArgumentException();
//    }
//    if (concurrencyLevel > 65536)
//    {
//      concurrencyLevel = 65536;
//    }
//    
//    int sshift = 0;
//    int ssize = 1;
//    while (ssize < concurrencyLevel)
//    {
//      ++sshift;
//      ssize <<= 1;
//    }
//    this.segmentShift = (32 - sshift);
//    this.segmentMask = (ssize - 1);
//    this.segments = Segment.newArray(ssize);
//    
//    if (initialCapacity > 1073741824)
//      initialCapacity = 1073741824;
//    int c = initialCapacity / ssize;
//    if (c * ssize < initialCapacity)
//      ++c;
//    int cap = 1;
//    while (cap < c)
//    {
//      cap <<= 1;
//    }
//    for (int i = 0; i < this.segments.length; ++i)
//      this.segments[i] = new Segment(cap, loadFactor);
//  }
//  
//  public ObjIdConcurrentMap(int initialCapacity, float loadFactor)
//  {
//    this(initialCapacity, loadFactor, 16);
//  }
//  
//  public ObjIdConcurrentMap(int initialCapacity)
//  {
//    this(initialCapacity, 0.75F, 16);
//  }
//  
//  public ObjIdConcurrentMap()
//  {
//    this(16, 0.75F, 16);
//  }
//  
//  public boolean isEmpty()
//  {
//    Segment[] segments = this.segments;
//    
//    int[] mc = new int[segments.length];
//    int mcsum = 0;
//    for (int i = 0; i < segments.length; ++i)
//    {
//      if (segments[i].count != 0)
//      {
//        return false;
//      }
//      mcsum += (mc[i] = segments[i].modCount);
//    }
//    
//    if (mcsum != 0)
//    {
//      for (int i = 0; i < segments.length; ++i)
//        if ((segments[i].count != 0) || (mc[i] != segments[i].modCount))
//        {
//          return false;
//        }
//    }
//    return true;
//  }
//  
//  public int size()
//  {
//    Segment[] segments = this.segments;
//    long sum = 0L;
//    long check = 0L;
//    int[] mc = new int[segments.length];
//    
//    for (int k = 0; k < 2; ++k)
//    {
//      check = 0L;
//      sum = 0L;
//      int mcsum = 0;
//      for (int i = 0; i < segments.length; ++i)
//      {
//        sum += segments[i].count;
//        mcsum += (mc[i] = segments[i].modCount);
//      }
//      if (mcsum != 0)
//      {
//        for (int i = 0; i < segments.length; ++i)
//        {
//          check += segments[i].count;
//          if (mc[i] != segments[i].modCount)
//          {
//            check = -1L;
//            break;
//          }
//        }
//      }
//      if (check == sum)
//        break;
//    }
//    if (check != sum)
//    {
//      sum = 0L;
//      for (int i = 0; i < segments.length; ++i)
//        segments[i].lock();
//      for (int i = 0; i < segments.length; ++i)
//        sum += segments[i].count;
//      for (int i = 0; i < segments.length; ++i)
//        segments[i].unlock();
//    }
//    if (sum > 2147483647L)
//    {
//      return 2147483647;
//    }
//    return (int) sum;
//  }
//  
//  public V get(int key)
//  {
//    int hash = hash(key);
//    return segmentFor(hash).get(key, hash);
//  }
//  
//  public boolean containsKey(int key)
//  {
//    int hash = hash(key);
//    return segmentFor(hash).containsKey(key, hash);
//  }
//  
//  public boolean containsValue(Object value)
//  {
//    if (value == null)
//    {
//      throw new NullPointerException();
//    }
//    
//    Segment[] segments = this.segments;
//    int[] mc = new int[segments.length];
//    
//    for (int k = 0; k < 2; ++k)
//    {
//      int mcsum = 0;
//      for (int i = 0; i < segments.length; ++i)
//      {
//        mcsum += (mc[i] = segments[i].modCount);
//        if (segments[i].containsValue(value))
//          return true;
//      }
//      boolean cleanSweep = true;
//      if (mcsum != 0)
//      {
//        for (int i = 0; i < segments.length; ++i)
//        {
//          if (mc[i] != segments[i].modCount)
//          {
//            cleanSweep = false;
//            break;
//          }
//        }
//      }
//      if (cleanSweep)
//      {
//        return false;
//      }
//    }
//    for (int i = 0; i < segments.length; ++i)
//      segments[i].lock();
//    boolean found = false;
//    try
//    {
//      for (int i = 0; i < segments.length; ++i)
//        if (segments[i].containsValue(value))
//        {
//          found = true;
//          break;
//        }
//    }
//    finally
//    {
//      for (int i = 0; i < segments.length; ++i)
//        segments[i].unlock();
//    }
//    return found;
//  }
//  
//  public boolean contains(Object value)
//  {
//    return containsValue(value);
//  }
//  
//  public V put(int key, V value)
//  {
//    if (value == null)
//      throw new NullPointerException();
//    int hash = hash(key);
//    return segmentFor(hash).put(key, hash, value, false);
//  }
//  
//  public V putIfAbsent(int key, V value)
//  {
//    if (value == null)
//      throw new NullPointerException();
//    int hash = hash(key);
//    return segmentFor(hash).put(key, hash, value, true);
//  }
//  
//  public V remove(int key)
//  {
//    int hash = hash(key);
//    return segmentFor(hash).remove(key, hash, null);
//  }
//  
//  public boolean remove(int key, Object value)
//  {
//    int hash = hash(key);
//    if (value == null)
//      return false;
//    return segmentFor(hash).remove(key, hash, value) != null;
//  }
//  
//  public boolean replace(int key, V oldValue, V newValue)
//  {
//    if ((oldValue == null) || (newValue == null))
//      throw new NullPointerException();
//    int hash = hash(key);
//    return segmentFor(hash).replace(key, hash, oldValue, newValue);
//  }
//  
//  public V replace(int key, V value)
//  {
//    if (value == null)
//      throw new NullPointerException();
//    int hash = hash(key);
//    return segmentFor(hash).replace(key, hash, value);
//  }
//  
//  public void clear()
//  {
//    for (int i = 0; i < this.segments.length; ++i)
//      this.segments[i].clear();
//  }
//  
//  private void writeObject(ObjectOutputStream s) throws IOException
//  {
//    s.defaultWriteObject();
//    
//    for (int k = 0; k < this.segments.length; ++k)
//    {
//      Segment seg = this.segments[k];
//      seg.lock();
//      try
//      {
//        HashEntry[] tab = seg.table;
//        for (int i = 0; i < tab.length; ++i)
//          for (HashEntry e = tab[i]; e != null; e = e.next)
//          {
//            s.writeObject(Integer.valueOf(e.key));
//            s.writeObject(e.value);
//          }
//      }
//      finally
//      {
//        seg.unlock();
//      }
//    }
//    s.writeObject(null);
//    s.writeObject(null);
//  }
//  
//  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
//  {
//    s.defaultReadObject();
//    
//    for (int i = 0; i < this.segments.length; ++i)
//    {
//      this.segments[i].setTable(new HashEntry[1]);
//    }
//    
//    while (true)
//    {
//      int key = s.readInt();
//      Object value = s.readObject();
//      
//      put(key, (V) value);
//    }
//  }
//  
//  final class WriteThroughEntry extends ObjIdConcurrentMap.SimpleEntry<V>
//  {
//    WriteThroughEntry(int k, V v)
//    {
//      super(k, v);
//    }
//    
//    public V setValue(V value)
//    {
//      if (value == null)
//        throw new NullPointerException();
//      Object v = super.setValue(value);
//      ObjIdConcurrentMap.this.put(getKey(), value);
//      return (V) v;
//    }
//  }
//  
//  static class SimpleEntry<V> implements ObjIdConcurrentMap.Entry<V>
//  {
//    int key;
//    V value;
//    
//    public SimpleEntry(int key, V value)
//    {
//      this.key = key;
//      this.value = value;
//    }
//    
//    public SimpleEntry(ObjIdConcurrentMap.Entry<V> e)
//    {
//      this.key = e.getKey();
//      this.value = e.getValue();
//    }
//    
//    public int getKey()
//    {
//      return this.key;
//    }
//    
//    public V getValue()
//    {
//      return this.value;
//    }
//    
//    public V setValue(V value)
//    {
//      Object oldValue = this.value;
//      this.value = value;
//      return (V) oldValue;
//    }
//    
//    public boolean equals(Object o)
//    {
//      if (!(o instanceof Map.Entry))
//        return false;
//      Map.Entry e = (Map.Entry) o;
//      return (eq(Integer.valueOf(this.key), e.getKey())) && (eq(this.value, e.getValue()));
//    }
//    
//    public int hashCode()
//    {
//      return this.key ^ ((this.value == null) ? 0 : this.value.hashCode());
//    }
//    
//    public String toString()
//    {
//      return this.key + "=" + this.value;
//    }
//    
//    private static boolean eq(Object o1, Object o2)
//    {
//      return (o1 == null) ? false : (o2 == null) ? true : o1.equals(o2);
//    }
//  }
//  
//  static abstract interface Entry<V>
//  {
//    public abstract int getKey();
//    
//    public abstract V getValue();
//    
//    public abstract V setValue(V paramV);
//    
//    public abstract boolean equals(Object paramObject);
//    
//    public abstract int hashCode();
//  }
//  
//  abstract class HashIterator
//  {
//    int nextSegmentIndex;
//    int nextTableIndex;
//    ObjIdConcurrentMap.HashEntry<V>[] currentTable;
//    ObjIdConcurrentMap.HashEntry<V> nextEntry;
//    ObjIdConcurrentMap.HashEntry<V> lastReturned;
//    
//    HashIterator()
//    {
//      this.nextSegmentIndex = (ObjIdConcurrentMap.this.segments.length - 1);
//      this.nextTableIndex = -1;
//      advance();
//    }
//    
//    public boolean hasMoreElements()
//    {
//      return hasNext();
//    }
//    
//    final void advance()
//    {
//      if ((this.nextEntry != null) && ((this.nextEntry = this.nextEntry.next) != null))
//      {
//        return;
//      }
//      while (this.nextTableIndex >= 0)
//      {
//        if ((this.nextEntry = this.currentTable[(this.nextTableIndex--)]) != null)
//        {
//          return;
//        }
//      }
//      while (this.nextSegmentIndex >= 0)
//      {
//        ObjIdConcurrentMap.Segment seg = ObjIdConcurrentMap.this.segments[(this.nextSegmentIndex--)];
//        if (seg.count != 0)
//        {
//          this.currentTable = seg.table;
//          for (int j = this.currentTable.length - 1; j >= 0; --j)
//            if ((this.nextEntry = this.currentTable[j]) != null)
//            {
//              this.nextTableIndex = (j - 1);
//              return;
//            }
//        }
//      }
//    }
//    
//    public boolean hasNext()
//    {
//      return this.nextEntry != null;
//    }
//    
//    ObjIdConcurrentMap.HashEntry<V> nextEntry()
//    {
//      if (this.nextEntry == null)
//        throw new NoSuchElementException();
//      this.lastReturned = this.nextEntry;
//      advance();
//      return this.lastReturned;
//    }
//    
//    public void remove()
//    {
//      if (this.lastReturned == null)
//        throw new IllegalStateException();
//      ObjIdConcurrentMap.this.remove(this.lastReturned.key);
//      this.lastReturned = null;
//    }
//  }
//  
//  static final class Segment<V> extends ReentrantLock implements Serializable
//  {
//    private static final long serialVersionUID = 2249069246763182397L;
//    volatile transient int count;
//    transient int modCount;
//    transient int threshold;
//    volatile transient ObjIdConcurrentMap.HashEntry<V>[] table;
//    final float loadFactor;
//    
//    Segment(int initialCapacity, float lf)
//    {
//      this.loadFactor = lf;
//      setTable((ObjIdConcurrentMap.HashEntry<V>[]) ObjIdConcurrentMap.HashEntry.newArray(initialCapacity));
//    }
//    
//    static final <K, V> Segment<V>[] newArray(int i)
//    {
//      return new Segment[i];
//    }
//    
//    void setTable(ObjIdConcurrentMap.HashEntry<V>[] newTable)
//    {
//      this.threshold = (int) (newTable.length * this.loadFactor);
//      this.table = newTable;
//    }
//    
//    ObjIdConcurrentMap.HashEntry<V> getFirst(int hash)
//    {
//      ObjIdConcurrentMap.HashEntry[] tab = this.table;
//      return tab[(hash & tab.length - 1)];
//    }
//    
//    V readValueUnderLock(ObjIdConcurrentMap.HashEntry<V> e)
//    {
//      lock();
//      try
//      {
//        V localObject1 = e.value;
//        
//        return (V) localObject1;
//      }
//      finally
//      {
//        unlock();
//      }
//      
//    }
//    
//    V get(int key, int hash)
//    {
//      if (this.count != 0)
//      {
//        ObjIdConcurrentMap.HashEntry e = getFirst(hash);
//        while (e != null)
//        {
//          if ((e.hash == hash) && (key == e.key))
//          {
//            Object v = e.value;
//            if (v != null)
//              return (V) v;
//            return readValueUnderLock(e);
//          }
//          e = e.next;
//        }
//      }
//      return null;
//    }
//    
//    boolean containsKey(int key, int hash)
//    {
//      if (this.count != 0)
//      {
//        ObjIdConcurrentMap.HashEntry e = getFirst(hash);
//        while (e != null)
//        {
//          if ((e.hash == hash) && (key == e.key))
//            return true;
//          e = e.next;
//        }
//      }
//      return false;
//    }
//    
//    boolean containsValue(Object value)
//    {
//      if (this.count != 0)
//      {
//        ObjIdConcurrentMap.HashEntry[] tab = this.table;
//        int len = tab.length;
//        for (int i = 0; i < len; ++i)
//        {
//          for (ObjIdConcurrentMap.HashEntry e = tab[i]; e != null; e = e.next)
//          {
//            Object v = e.value;
//            if (v == null)
//              v = readValueUnderLock(e);
//            if (value.equals(v))
//              return true;
//          }
//        }
//      }
//      return false;
//    }
//    
//    boolean replace(int key, int hash, V oldValue, V newValue)
//    {
//      lock();
//      try
//      {
//        ObjIdConcurrentMap.HashEntry e = getFirst(hash);
//        while ((e != null) && (((e.hash != hash) || (key != e.key))))
//        {
//          e = e.next;
//        }
//        boolean replaced = false;
//        if ((e != null) && (oldValue.equals(e.value)))
//        {
//          replaced = true;
//          e.value = newValue;
//        }
//        boolean bool1 = replaced;
//        
//        return bool1;
//      }
//      finally
//      {
//        unlock();
//      }
//    }
//    
//    V replace(int key, int hash, V newValue)
//    {
//      lock();
//      try
//      {
//        ObjIdConcurrentMap.HashEntry e = getFirst(hash);
//        while ((e != null) && (((e.hash != hash) || (key != e.key))))
//        {
//          e = e.next;
//        }
//        Object oldValue = null;
//        if (e != null)
//        {
//          oldValue = e.value;
//          e.value = newValue;
//        }
//        Object localObject1 = oldValue;
//        
//        return (V) localObject1;
//      }
//      finally
//      {
//        unlock();
//      }
//      
//    }
//    
//    V put(int key, int hash, V value, boolean onlyIfAbsent)
//    {
//      lock();
//      try
//      {
//        int c = this.count;
//        if (c++ > this.threshold)
//          rehash();
//        ObjIdConcurrentMap.HashEntry[] tab = this.table;
//        int index = hash & tab.length - 1;
//        ObjIdConcurrentMap.HashEntry first = tab[index];
//        ObjIdConcurrentMap.HashEntry e = first;
//        while ((e != null) && (((e.hash != hash) || (key != e.key))))
//          e = e.next;
//        V oldValue;
//        if (e != null)
//        {
//          oldValue = (V) e.value;
//          if (!onlyIfAbsent)
//            e.value = value;
//        }
//        else
//        {
//          oldValue = null;
//          this.modCount += 1;
//          tab[index] = new ObjIdConcurrentMap.HashEntry(key, hash, first, value);
//          this.count = c;
//        }
//        V localObject1 = oldValue;
//        
//        return localObject1;
//      }
//      finally
//      {
//        unlock();
//      }
//    }
//    
//    void rehash()
//    {
//      ObjIdConcurrentMap.HashEntry[] oldTable = this.table;
//      int oldCapacity = oldTable.length;
//      if (oldCapacity >= 1073741824)
//      {
//        return;
//      }
//      
//      ObjIdConcurrentMap.HashEntry[] newTable = ObjIdConcurrentMap.HashEntry.newArray(oldCapacity << 1);
//      this.threshold = (int) (newTable.length * this.loadFactor);
//      int sizeMask = newTable.length - 1;
//      for (int i = 0; i < oldCapacity; ++i)
//      {
//        ObjIdConcurrentMap.HashEntry e = oldTable[i];
//        
//        if (e != null)
//        {
//          ObjIdConcurrentMap.HashEntry next = e.next;
//          int idx = e.hash & sizeMask;
//          
//          if (next == null)
//          {
//            newTable[idx] = e;
//          }
//          else
//          {
//            ObjIdConcurrentMap.HashEntry lastRun = e;
//            int lastIdx = idx;
//            ObjIdConcurrentMap.HashEntry last = next;
//            while (last != null)
//            {
//              int k = last.hash & sizeMask;
//              if (k != lastIdx)
//              {
//                lastIdx = k;
//                lastRun = last;
//              }
//              last = last.next;
//            }
//            
//            newTable[lastIdx] = lastRun;
//            
//            for (ObjIdConcurrentMap.HashEntry p = e; p != lastRun; p = p.next)
//            {
//              int k = p.hash & sizeMask;
//              ObjIdConcurrentMap.HashEntry n = newTable[k];
//              newTable[k] = new ObjIdConcurrentMap.HashEntry(p.key, p.hash, n, p.value);
//            }
//          }
//        }
//      }
//      
//      this.table = newTable;
//    }
//    
//    V remove(int key, int hash, Object value)
//    {
//      lock();
//      try
//      {
//        int c = this.count - 1;
//        ObjIdConcurrentMap.HashEntry[] tab = this.table;
//        int index = hash & tab.length - 1;
//        ObjIdConcurrentMap.HashEntry first = tab[index];
//        ObjIdConcurrentMap.HashEntry e = first;
//        while ((e != null) && (((e.hash != hash) || (key != e.key))))
//        {
//          e = e.next;
//        }
//        Object oldValue = null;
//        if (e != null)
//        {
//          V v = (V) e.value;
//          if ((value == null) || (value.equals(v)))
//          {
//            oldValue = v;
//            
//            this.modCount += 1;
//            ObjIdConcurrentMap.HashEntry newFirst = e.next;
//            for (ObjIdConcurrentMap.HashEntry p = first; p != e; p = p.next)
//            {
//              newFirst = new ObjIdConcurrentMap.HashEntry(p.key, p.hash, newFirst, p.value);
//            }
//            tab[index] = newFirst;
//            this.count = c;
//          }
//        }
//        V v = (V) oldValue;
//        
//        return v;
//      }
//      finally
//      {
//        unlock();
//      }
//    }
//    
//    void clear()
//    {
//      if (this.count != 0)
//      {
//        lock();
//        try
//        {
//          ObjIdConcurrentMap.HashEntry[] tab = this.table;
//          for (int i = 0; i < tab.length; ++i)
//            tab[i] = null;
//          this.modCount += 1;
//          this.count = 0;
//        }
//        finally
//        {
//          unlock();
//        }
//      }
//    }
//  }
//  
//  static final class HashEntry<V>
//  {
//    final int key;
//    final int hash;
//    volatile V value;
//    final HashEntry<V> next;
//    
//    HashEntry(int key, int hash, HashEntry<V> next, V value)
//    {
//      this.key = key;
//      this.hash = hash;
//      this.next = next;
//      this.value = value;
//    }
//    
//    static final <V> HashEntry<V>[] newArray(int i)
//    {
//      return new HashEntry[i];
//    }
//  }
//}
