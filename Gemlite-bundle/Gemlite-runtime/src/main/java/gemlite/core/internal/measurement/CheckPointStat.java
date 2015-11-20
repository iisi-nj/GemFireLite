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

public class CheckPointStat implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -6359994735807769669L;
  private String className;
  private String checkPoint;
  private long start;
  private long end;
  
  public String getClassName()
  {
    return className;
  }
  public void setClassName(String className)
  {
    this.className = className;
  }
  public String getCheckPoint()
  {
    return checkPoint;
  }
  public void setCheckPoint(String checkPoint)
  {
    this.checkPoint = checkPoint;
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
  @Override
  public String toString()
  {
    return "CP:" + checkPoint + "(" + start + ","
        + end +","+(end-start)+ ")";
  }
}
