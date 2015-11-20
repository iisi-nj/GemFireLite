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

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RemoteServiceStatItem implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 5919156302355360991L;
  private long start;
  private long end;
  private long send;
  private List<CheckPointStat> checkPoints;
  private String serviceName;
  private String moduleName;
  
  private transient WeakReference<Thread> thread;
  
  public RemoteServiceStatItem()
  {
    checkPoints = new ArrayList<>();
  }
  
    
  public void addCheckPoint(CheckPointStat cp)
  {
    checkPoints.add(cp);
  }
  public long getStart()
  {
    return start;
  }
  public void setStart(long start)
  {
    this.start = start;
  }
  public long getEnd()
  {
    return end;
  }
  public void setEnd(long end)
  {
    this.end = end;
  }
  public List<CheckPointStat> getCheckPoints()
  {
    return checkPoints;
  }
  public WeakReference<Thread> getThread()
  {
    return thread;
  }
  public void setThread(WeakReference<Thread> thread)
  {
    this.thread = thread;
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


  @Override
  public String toString()
  {
    return moduleName+"."+serviceName+"(" + start + "," + end +","+(end-start) +") " + checkPoints ;
  }


  public long getSend()
  {
    return send;
  }


  public void setSend(long send)
  {
    this.send = send;
  }
}
