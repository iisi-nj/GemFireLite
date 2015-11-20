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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class YearToMonth extends Number implements Interval, Comparable<YearToMonth>
{
  private static final long serialVersionUID = 1308553645456594273L;
  private static final Pattern PATTERN = Pattern.compile("(\\+|-)?(\\d+)-(\\d+)");
  private final boolean negative;
  private final int years;
  private final int months;
  
  public YearToMonth(int years)
  {
    this(years, 0, false);
  }
  
  public YearToMonth(int years, int months)
  {
    this(years, months, false);
  }
  
  private YearToMonth(int years, int months, boolean negative)
  {
    // Perform normalisation. Specifically, Postgres may return intervals
    // such as 0-13
    if (months >= 12)
    {
      years += (months / 12);
      months %= 12;
    }
    this.negative = negative;
    this.years = years;
    this.months = months;
  }
  
  public static YearToMonth valueOf(String string)
  {
    if (string != null)
    {
      Matcher matcher = PATTERN.matcher(string);
      if (matcher.find())
      {
        boolean negative = "-".equals(matcher.group(1));
        int years = Integer.parseInt(matcher.group(2));
        int months = Integer.parseInt(matcher.group(3));
        return new YearToMonth(years, months, negative);
      }
    }
    return null;
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  @Override
  public final YearToMonth neg()
  {
    return new YearToMonth(years, months, !negative);
  }
  
  @Override
  public final YearToMonth abs()
  {
    return new YearToMonth(years, months, false);
  }
  
  public final int getYears()
  {
    return years;
  }
  
  public final int getMonths()
  {
    return months;
  }
  
  @Override
  public final int getSign()
  {
    return negative ? -1 : 1;
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  @Override
  public final int intValue()
  {
    return (negative ? -1 : 1) * (12 * years + months);
  }
  
  @Override
  public final long longValue()
  {
    return intValue();
  }
  
  @Override
  public final float floatValue()
  {
    return intValue();
  }
  
  @Override
  public final double doubleValue()
  {
    return intValue();
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  @Override
  public final int compareTo(YearToMonth that)
  {
    if (years < that.years)
    {
      return -1;
    }
    if (years > that.years)
    {
      return 1;
    }
    if (months < that.months)
    {
      return -1;
    }
    if (months > that.months)
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
    result = prime * result + months;
    result = prime * result + years;
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
    YearToMonth other = (YearToMonth) obj;
    if (months != other.months)
      return false;
    if (years != other.years)
      return false;
    return true;
  }
  
  @Override
  public final String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(negative ? "-" : "+");
    sb.append(years);
    sb.append("-");
    sb.append(months);
    return sb.toString();
  }
}
