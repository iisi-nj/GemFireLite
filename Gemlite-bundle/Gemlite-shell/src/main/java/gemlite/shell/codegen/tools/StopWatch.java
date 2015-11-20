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
package gemlite.shell.codegen.tools;

import gemlite.core.util.LogUtil;

public final class StopWatch
{
  private long start;
  private long split;
  
  public StopWatch()
  {
    this.start = System.nanoTime();
    this.split = start;
  }
  
  public void splitTrace(String message)
  {
    if (LogUtil.getCoreLog().isTraceEnabled())
    {
      LogUtil.getCoreLog().trace(message, splitMessage());
    }
  }
  
  public void splitDebug(String message)
  {
    if (LogUtil.getCoreLog().isDebugEnabled())
    {
      LogUtil.getCoreLog().debug(message, splitMessage());
    }
  }
  
  public void splitInfo(String message)
  {
    if (LogUtil.getCoreLog().isInfoEnabled())
    {
      LogUtil.getCoreLog().info(message, splitMessage());
    }
  }
  
  public long split()
  {
    return System.nanoTime() - start;
  }
  
  private String splitMessage()
  {
    final long temp = split;
    split = System.nanoTime();
    if (temp == start)
    {
      return "Total: " + format(split - start);
    }
    else
    {
      return "Total: " + format(split - start) + ", +" + format(split - temp);
    }
  }
  
  public static String format(long nanoTime)
  {
    // If more than one minute, format in HH:mm:ss
    if (nanoTime > (60L * 1000L * 1000000L))
    {
      return formatHours(nanoTime / (1000L * 1000000L));
    }
    // If more than one second, display seconds with milliseconds
    else if (nanoTime > (1000L * 1000000L))
    {
      return ((nanoTime / 1000000L) / 1000.0) + "s";
    }
    // If less than one second, display milliseconds with microseconds
    else
    {
      return ((nanoTime / 1000L) / 1000.0) + "ms";
    }
  }
  
  public static String formatHours(long seconds)
  {
    long s = seconds % 60L;
    long m = (seconds / 60L) % 60L;
    long h = (seconds / 3600L);
    StringBuilder sb = new StringBuilder();
    if (h == 0)
    {
      // nop
    }
    else if (h < 10)
    {
      sb.append("0");
      sb.append(h);
      sb.append(":");
    }
    else
    {
      sb.append(h);
      sb.append(":");
    }
    if (m < 10)
    {
      sb.append("0");
      sb.append(m);
      sb.append(":");
    }
    else
    {
      sb.append(m);
      sb.append(":");
    }
    if (s < 10)
    {
      sb.append("0");
      sb.append(s);
    }
    else
    {
      sb.append(s);
    }
    return sb.toString();
  }
}
