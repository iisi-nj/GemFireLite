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
package gemlite.core.webapp.pages.file;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

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
  private long lastCost;
  private long maxCost;
  private long avgCost;
  
  private long firstAt;
  private long lastAt;
  
  private int tps;
  
  private long prevTimestamp;
  private long prevCount;
  
  private String nodeName;
  
  
  public RemoteServiceStat()
  {
  }


  public AtomicLong getTotalCount()
  {
    return totalCount;
  }


  public void setTotalCount(AtomicLong totalCount)
  {
    this.totalCount = totalCount;
  }


  public AtomicLong getTotalCost()
  {
    return totalCost;
  }


  public void setTotalCost(AtomicLong totalCost)
  {
    this.totalCost = totalCost;
  }


  public String getServiceName()
  {
    return serviceName;
  }


  public void setServiceName(String serviceName)
  {
    this.serviceName = serviceName;
  }


  public String getModuleName()
  {
    return moduleName;
  }


  public void setModuleName(String moduleName)
  {
    this.moduleName = moduleName;
  }


  public int getMaxHistorySize()
  {
    return maxHistorySize;
  }


  public void setMaxHistorySize(int maxHistorySize)
  {
    this.maxHistorySize = maxHistorySize;
  }


  public int getRecentHistorySize()
  {
    return recentHistorySize;
  }


  public void setRecentHistorySize(int recentHistorySize)
  {
    this.recentHistorySize = recentHistorySize;
  }


  public int getTpsRange()
  {
    return tpsRange;
  }


  public void setTpsRange(int tpsRange)
  {
    this.tpsRange = tpsRange;
  }


  public long getLastCost()
  {
    return lastCost;
  }


  public void setLastCost(long lastCost)
  {
    this.lastCost = lastCost;
  }


  public long getMaxCost()
  {
    return maxCost;
  }


  public void setMaxCost(long maxCost)
  {
    this.maxCost = maxCost;
  }


  public long getAvgCost()
  {
    return avgCost;
  }


  public void setAvgCost(long avgCost)
  {
    this.avgCost = avgCost;
  }


  public long getFirstAt()
  {
    return firstAt;
  }


  public void setFirstAt(long firstAt)
  {
    this.firstAt = firstAt;
  }


  public long getLastAt()
  {
    return lastAt;
  }


  public void setLastAt(long lastAt)
  {
    this.lastAt = lastAt;
  }


  public int getTps()
  {
    return tps;
  }


  public void setTps(int tps)
  {
    this.tps = tps;
  }


  public long getPrevTimestamp()
  {
    return prevTimestamp;
  }


  public void setPrevTimestamp(long prevTimestamp)
  {
    this.prevTimestamp = prevTimestamp;
  }


  public long getPrevCount()
  {
    return prevCount;
  }


  public void setPrevCount(long prevCount)
  {
    this.prevCount = prevCount;
  }


  public String getNodeName()
  {
    return nodeName;
  }


  public void setNodeName(String nodeName)
  {
    this.nodeName = nodeName;
  }
  
  
}
