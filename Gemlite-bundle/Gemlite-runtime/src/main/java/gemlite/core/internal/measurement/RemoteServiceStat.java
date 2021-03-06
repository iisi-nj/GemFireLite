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
package gemlite.core.internal.measurement;

import gemlite.core.annotations.mbean.GemliteMBean;
import gemlite.core.internal.support.jmx.AggregateType;
import gemlite.core.internal.support.jmx.annotation.AggregateAttribute;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@GemliteMBean(name = "Service")
@ManagedResource(description = "Remote service stat",persistLocation="",persistPeriod=10)
public class RemoteServiceStat implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5332377139630844755L;
  private AtomicLong totalCount = new AtomicLong();
  private AtomicLong totalCost = new AtomicLong();
  private String serviceName;
  private String moduleName;
  
  private int maxHistorySize =3;
  private int recentHistorySize= 10;
  private int tpsRange = 2;// seconds
  private ConcurrentSkipListSet<RemoteServiceStatItem> maxHistory;
  private ConcurrentSkipListSet<RemoteServiceStatItem> recentHistory;
  private long lastCost;
  private long maxCost;
  private long avgCost;
  
  private long firstAt;
  private long lastAt;
  
  private int tps;
  
  private long prevTimestamp;
  private long prevCount;
  
  private String nodeName;
  
  public class BigComparator implements Serializable, Comparator<RemoteServiceStatItem>
  {
    private static final long serialVersionUID = 1245326027718944331L;
    
    public BigComparator()
    {
    }
    
    @Override
    public int compare(RemoteServiceStatItem o1, RemoteServiceStatItem o2)
    {
      long cost1 = o1.getEnd() - o1.getStart();
      long cost2 = o2.getEnd() - o2.getStart();
      return (int) (cost2 - cost1);
    }
  }
  
  public class RecentComparator implements Comparator<RemoteServiceStatItem>, Serializable
  {
    
    private static final long serialVersionUID = 8687576702066021447L;
    
    public RecentComparator()
    {
    }
    
    @Override
    public int compare(RemoteServiceStatItem o1, RemoteServiceStatItem o2)
    {
      return (int) (o2.getEnd() - o1.getEnd());
    }
  }
  
  public RemoteServiceStat()
  {
    maxHistory = new ConcurrentSkipListSet<>(new BigComparator());
    recentHistory = new ConcurrentSkipListSet<>(new RecentComparator());
  }
  
  public void calcuteTPS()
  {
    long timestamp = System.currentTimeMillis();
    long past = timestamp - prevTimestamp;
    if (prevTimestamp == 0)
      prevTimestamp = timestamp;
    else if (past > (tpsRange * 1000))
    {
      long thisCount = totalCount.get();
      long count = thisCount - prevCount;
      tps = (int) (count * 1000 / past);
      prevTimestamp = timestamp;
      prevCount = thisCount;
    }
  }
  
  /***
   * 线程安全
   * 
   * @param item
   */
  public void recordItem(RemoteServiceStatItem item)
  {
    if(firstAt==0)
      firstAt = item.getStart();
    lastAt = item.getEnd();
    long cost = item.getEnd() - item.getStart();
    if(cost>maxCost)
      maxCost = cost;
    lastCost = cost;
    
    if(totalCount.get()>0)
    avgCost = (int)( totalCost.get()/totalCount.get());
    
    maxHistory.add(item);
    recentHistory.add(item);
    if(maxHistory.size()>maxHistorySize)
      maxHistory.pollLast();
    if(recentHistory.size()>recentHistorySize)
      recentHistory.pollLast();
    
  }
  
  @ManagedOperation
  public void resetStat(){
    maxCost=0;
    lastCost=0;
    maxHistory.clear();
    recentHistory.clear();
  }
  
  public void incrTotalCount()
  {
    totalCount.incrementAndGet();
  }
  
  public void incrTotalCost(long cost)
  {
    totalCost.addAndGet(cost);
  }
  
  @AggregateAttribute(AggregateType.SUM)
  @ManagedAttribute(description = "总次数")
  public long getTotalCount()
  {
    return totalCount.get();
  }
  
  @AggregateAttribute(AggregateType.SUM)
  @ManagedAttribute(description = "总耗时")
  public long getTotalCost()
  {
    return totalCost.get();
  }
  
  @AggregateAttribute(AggregateType.SAME)
  @ManagedAttribute(description = "服务名")
  public String getServiceName()
  {
    return serviceName;
  }
  
  public void setServiceName(String serviceName)
  {
    this.serviceName = serviceName;
  }
  
  @AggregateAttribute(AggregateType.SAME)
  @ManagedAttribute
  public String getModuleName()
  {
    return moduleName;
  }
  
  public void setModuleName(String moduleName)
  {
    this.moduleName = moduleName;
  }
  
  @AggregateAttribute(AggregateType.SAME)
  @ManagedAttribute(description = "最大值历史")
  public int getMaxHistorySize()
  {
    return maxHistorySize;
  }
  
  @AggregateAttribute(AggregateType.SAME)
  @ManagedAttribute(description = "访问历史")
  public int getRecentHistorySize()
  {
    return recentHistorySize;
  }
  
  @AggregateAttribute(AggregateType.MAX)
  @ManagedAttribute
  public long getLastAt()
  {
    return lastAt;
  }
  
  @AggregateAttribute(AggregateType.MIN)
  @ManagedAttribute
  public long getFirstAt()
  {
    return firstAt;
  }
  
  @AggregateAttribute(AggregateType.SAME)
  @ManagedAttribute
  public String getNodeName()
  {
    return nodeName;
  }
  
  public void setNodeName(String nodeName)
  {
    this.nodeName = nodeName;
  }
  @AggregateAttribute(AggregateType.SAME)
  @ManagedAttribute(description="tps range,seconds")
  public int getTpsRange()
  {
    return tpsRange;
  }
  
  public void setTpsRange(int range)
  {
    this.tpsRange = range;
  }
  
  @AggregateAttribute(AggregateType.SUM)
  @ManagedAttribute(description = "最近10秒(tpsRange) tps")
  public int getTps()
  {
    return tps;
  }
  
  public void setTps(int tps)
  {
    this.tps = tps;
  }
  
  @AggregateAttribute(AggregateType.MAX)
  @ManagedAttribute(description = "last")
  public long getLastCost()
  {
    return lastCost;
  }
  @AggregateAttribute(AggregateType.MAX)
  @ManagedAttribute(description = "max")
  public long getMaxCost()
  {
    return maxCost;
  }
  
  @AggregateOperation(support=true)
  @ManagedOperation
  public List<RemoteServiceStatItem> getMaxHistory()
  {
    List<RemoteServiceStatItem> list = new ArrayList<>();
    list.addAll(maxHistory);
    return list;
  }
  @AggregateOperation(support=true)
  @ManagedOperation
  public List<RemoteServiceStatItem> getRecentHistory()
  {
    List<RemoteServiceStatItem> list = new ArrayList<>();
    list.addAll(recentHistory);
    return list;
  }
  
}
