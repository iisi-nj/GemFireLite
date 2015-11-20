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
package gemlite.core.internal.domain.utilClass;

import gemlite.core.internal.domain.DomainUtil;
import gemlite.core.internal.domain.IDataSource;
import gemlite.core.util.LogUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

public final class MqDataSource implements IDataSource
{
  private Map<String, String> map;
  
  public MqDataSource(Map<String, String> rs)
  {
    this.map = rs;
  }
  
  public final String getString(String name)
  {
    String str = map.get(name);
    return str;
  }
  
  public final int getInt(String name)
  {
    String str = map.get(name);
    if (str == null)
      return 0;
    int n = NumberUtils.toInt(str);
    return n;
  }
  
  public final long getLong(String name)
  {
    String str = map.get(name);
    long n = NumberUtils.toLong(str);
    return n;
  }
  
  public final Date getDate(String name)
  {
    String str = map.get(name);
    
    Date dt = null;
    try
    {
      dt = DateUtils.parseDate(str, DomainUtil.gs_date_pattern);
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("Name:" + name + " value:" + str, e);
    }
    return dt;
  }
  
  public final double getDouble(String name)
  {
    String str = map.get(name);
    double n = NumberUtils.toDouble(str);
    return n;
  }
  
  @Override
  public short getShort(String name)
  {
    String str = map.get(name);
    short n = NumberUtils.toShort(str);
    return n;
  }
  
  @Override
  public boolean getBoolean(String name)
  {
    String str = map.get(name);
    boolean b = Boolean.parseBoolean(str);
    return b;
  }
  
  @Override
  public byte getByte(String name)
  {
    String str = map.get(name);
    if (str == null)
      return 0;
    byte b = Byte.parseByte(str);
    return b;
  }
  
  @Override
  public BigDecimal getBigDecimal(String name)
  {
    String str = map.get(name);
    if (str == null)
      return new BigDecimal(0);
    return new BigDecimal(str);
  }
  
  @Override
  public UUID getUUID(String name)
  {
    String str = map.get(name);
    if (str == null)
      return null;
    return UUID.fromString(str);
  }
  
  @Override
  public byte[] getByteArray(String name)
  {
    String str = map.get(name);
    if (str == null)
      return null;
    return str.getBytes();
  }
}
