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
package gemlite.shell.codegen.types;

import gemlite.shell.codegen.tools.StringUtils;

public final class DayToSecond extends Number implements Interval, Comparable<DayToSecond>
{
  private static final long serialVersionUID = -3853596481984643811L;
  private final boolean negative;
  private final int days;
  private final int hours;
  private final int minutes;
  private final int seconds;
  private final int nano;
  
  public DayToSecond(int days)
  {
    this(days, 0, 0, 0, 0, false);
  }
  
  public DayToSecond(int days, int hours)
  {
    this(days, hours, 0, 0, 0, false);
  }
  
  public DayToSecond(int days, int hours, int minutes)
  {
    this(days, hours, minutes, 0, 0, false);
  }
  
  public DayToSecond(int days, int hours, int minutes, int seconds)
  {
    this(days, hours, minutes, seconds, 0, false);
  }
  
  public DayToSecond(int days, int hours, int minutes, int seconds, int nano)
  {
    this(days, hours, minutes, seconds, nano, false);
  }
  
  private DayToSecond(int days, int hours, int minutes, int seconds, int nano, boolean negative)
  {
    // Perform normalisation. Specifically, Postgres may return intervals
    // such as 24:00:00, 25:13:15, etc...
    if (nano >= 1000000000)
    {
      seconds += (nano / 1000000000);
      nano %= 1000000000;
    }
    if (seconds >= 60)
    {
      minutes += (seconds / 60);
      seconds %= 60;
    }
    if (minutes >= 60)
    {
      hours += (minutes / 60);
      minutes %= 60;
    }
    if (hours >= 24)
    {
      days += (hours / 24);
      hours %= 24;
    }
    this.negative = negative;
    this.days = days;
    this.hours = hours;
    this.minutes = minutes;
    this.seconds = seconds;
    this.nano = nano;
  }
  
  public static DayToSecond valueOf(double milli)
  {
    double abs = Math.abs(milli);
    int n = (int) ((abs % 1000) * 1000000.0);
    abs = Math.floor(abs / 1000);
    int s = (int) (abs % 60);
    abs = Math.floor(abs / 60);
    int m = (int) (abs % 60);
    abs = Math.floor(abs / 60);
    int h = (int) (abs % 24);
    abs = Math.floor(abs / 24);
    int d = (int) abs;
    DayToSecond result = new DayToSecond(d, h, m, s, n);
    if (milli < 0)
    {
      result = result.neg();
    }
    return result;
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  @Override
  public final int intValue()
  {
    return (int) doubleValue();
  }
  
  @Override
  public final long longValue()
  {
    return (long) doubleValue();
  }
  
  @Override
  public final float floatValue()
  {
    return (float) doubleValue();
  }
  
  @Override
  public final double doubleValue()
  {
    return getTotalMilli();
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  @Override
  public final DayToSecond neg()
  {
    return new DayToSecond(days, hours, minutes, seconds, nano, !negative);
  }
  
  @Override
  public final DayToSecond abs()
  {
    return new DayToSecond(days, hours, minutes, seconds, nano, false);
  }
  
  public final int getDays()
  {
    return days;
  }
  
  public final int getHours()
  {
    return hours;
  }
  
  public final int getMinutes()
  {
    return minutes;
  }
  
  public final int getSeconds()
  {
    return seconds;
  }
  
  public final int getMilli()
  {
    return nano / 1000000;
  }
  
  public final int getMicro()
  {
    return nano / 1000;
  }
  
  public final int getNano()
  {
    return nano;
  }
  
  public final double getTotalDays()
  {
    return getSign()
        * (nano / (24.0 * 3600.0 * 1000000000.0) + seconds / (24.0 * 3600.0) + minutes / (24.0 * 60.0) + hours / 24.0 + days);
  }
  
  public final double getTotalHours()
  {
    return getSign() * (nano / (3600.0 * 1000000000.0) + seconds / 3600.0 + minutes / 60.0 + hours + 24.0 * days);
  }
  
  public final double getTotalMinutes()
  {
    return getSign() * (nano / (60.0 * 1000000000.0) + seconds / 60.0 + minutes + 60.0 * hours + 60.0 * 24.0 * days);
  }
  
  public final double getTotalSeconds()
  {
    return getSign() * (nano / 1000000000.0 + seconds + 60.0 * minutes + 3600.0 * hours + 3600.0 * 24.0 * days);
  }
  
  public final double getTotalMilli()
  {
    return getSign()
        * (nano / 1000000.0 + 1000.0 * seconds + 1000.0 * 60.0 * minutes + 1000.0 * 3600.0 * hours + 1000.0 * 3600.0 * 24.0 * days);
  }
  
  public final double getTotalMicro()
  {
    return getSign()
        * (nano / 1000.0 + 1000000.0 * seconds + 1000000.0 * 60.0 * minutes + 1000000.0 * 3600.0 * hours + 1000000.0 * 3600.0 * 24.0 * days);
  }
  
  public final double getTotalNano()
  {
    return getSign()
        * (nano + 1000000000.0 * seconds + 1000000000.0 * 60.0 * minutes + 1000000000.0 * 3600.0 * hours + 1000000000.0 * 3600.0 * 24.0 * days);
  }
  
  @Override
  public final int getSign()
  {
    return negative ? -1 : 1;
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  @Override
  public final int compareTo(DayToSecond that)
  {
    if (days < that.days)
    {
      return -1;
    }
    if (days > that.days)
    {
      return 1;
    }
    if (hours < that.hours)
    {
      return -1;
    }
    if (hours > that.hours)
    {
      return 1;
    }
    if (minutes < that.minutes)
    {
      return -1;
    }
    if (minutes > that.minutes)
    {
      return 1;
    }
    if (seconds < that.seconds)
    {
      return -1;
    }
    if (seconds > that.seconds)
    {
      return 1;
    }
    if (nano < that.nano)
    {
      return -1;
    }
    if (nano > that.nano)
    {
      return 1;
    }
    return 0;
  }
  
  @Override
  public final int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + days;
    result = prime * result + hours;
    result = prime * result + minutes;
    result = prime * result + nano;
    result = prime * result + seconds;
    return result;
  }
  
  @Override
  public final boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DayToSecond other = (DayToSecond) obj;
    if (days != other.days)
      return false;
    if (hours != other.hours)
      return false;
    if (minutes != other.minutes)
      return false;
    if (nano != other.nano)
      return false;
    if (seconds != other.seconds)
      return false;
    return true;
  }
  
  @Override
  public final String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(negative ? "-" : "+");
    sb.append(days);
    sb.append(" ");
    if (hours < 10)
      sb.append("0");
    sb.append(hours);
    sb.append(":");
    if (minutes < 10)
      sb.append("0");
    sb.append(minutes);
    sb.append(":");
    if (seconds < 10)
      sb.append("0");
    sb.append(seconds);
    sb.append(".");
    sb.append(StringUtils.leftPad("" + nano, 9, "0"));
    return sb.toString();
  }
}
