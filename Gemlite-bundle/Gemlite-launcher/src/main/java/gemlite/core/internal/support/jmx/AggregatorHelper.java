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
package gemlite.core.internal.support.jmx;

import gemlite.core.util.LogUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AggregatorHelper
{
  private final static AggregatorHelper inst = new AggregatorHelper();
  
  private AggregatorHelper()
  {
    
  }
  
  public final static AggregatorHelper getInstace()
  {
    return inst;
  }
  
  public Object doCaculate(Object v1, Object v2, AggregateType aggrType)
  {
    try
    {
      Object v3 = v1;
      if (v2 != null)
      {
        switch (aggrType)
        {
          case AVG:
            v3 = sum(v1, v2);
            //每次都计算平均值,否则若遇到掉点,则有可能什么计算不到
            v3 = avg(v3, 2);
            break;
          case SUM:
            v3 = sum(v1, v2);
            break;
          case MIN:
            v3 = min(v1, v2);
            break;
          case MAX:
            v3 = max(v1, v2);
            break;
          default:
            break;
        }
      }
      
      if (LogUtil.getCoreLog().isTraceEnabled())
        LogUtil.getCoreLog().trace("Type:{} item: {} aggr: {}  result: {}",aggrType,v1,v2,v3);
      
      return v3;
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("doCaculate error:{}", e);
    }
    return null;
  }
  
  private Object avg(Object o1, int size)
  {
    if (o1 instanceof Integer)
    {
      Integer i1 = (Integer) o1;
      return i1 / size;
    }
    else if (o1 instanceof Long)
    {
      Long i1 = (Long) o1;
      return i1 / size;
    }
    else if (o1 instanceof Double)
    {
      Double i1 = (Double) o1;
      return i1 / size;
    }
    return null;
  }
  
  private Object sum(Object o1, Object o2)
  {
    if (o1 instanceof Integer)
    {
      Integer i1 = (Integer) o1;
      Integer i2 = o2 == null ? 0 : (Integer) o2;
      return i1 + i2;
    }
    else if (o1 instanceof Long)
    {
      Long i1 = (Long) o1;
      Long i2 = o2 == null ? 0 : (Long) o2;
      return i1 + i2;
    }
    else if (o1 instanceof Double)
    {
      Double i1 = (Double) o1;
      Double i2 = o2 == null ? 0 : (Double) o2;
      return i1 + i2;
    }
    return null;
  }
  
  private Object min(Object o1, Object o2)
  {
    //对于时间类型的判断
    if(o1 instanceof Long && o2 instanceof Long)
    {
        if((Long)o1==0 || (Long)o2==0)
        {
            return (Long)o1>0?o1:o2;
        }
        else
        {
            Comparable c1 = (Comparable) o1;
            Comparable c2 = (Comparable) o2;
            if (c1.compareTo(c2) <= 0)
              return o1;
            else
              return o2;
        }
    }
    else
    {
        Comparable c1 = (Comparable) o1;
        Comparable c2 = (Comparable) o2;
        if (c1.compareTo(c2) <= 0)
          return o1;
        else
          return o2;
    }
  }
  
  private Object max(Object o1, Object o2)
  {
    Comparable c1 = (Comparable) o1;
    Comparable c2 = (Comparable) o2;
    if (c1.compareTo(c2) <= 0)
      return o2;
    else
      return o1;
  }
}
