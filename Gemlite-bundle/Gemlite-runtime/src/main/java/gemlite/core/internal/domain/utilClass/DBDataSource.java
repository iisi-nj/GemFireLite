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

import gemlite.core.internal.domain.IDataSource;
import gemlite.core.util.LogUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public final class DBDataSource implements IDataSource
{
  private ResultSet rs;
  
  public DBDataSource(ResultSet rs)
  {
    this.rs = rs;
  }
  
  public final String getString(String name)
  {
    String str = "";
    try
    {
      str = StringUtils.trim(rs.getString(name));
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return str;
  }
  
  public final int getInt(String name)
  {
    int n = 0;
    try
    {
      n = rs.getInt(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return n;
  }
  
  public final long getLong(String name)
  {
    long n = 0;
    try
    {
      n = rs.getLong(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return n;
  }
  
  public final Date getDate(String name)
  {
    Date n = null;
    try
    {
      n = rs.getDate(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return n;
  }
  
  public final double getDouble(String name)
  {
    double n = 0;
    try
    {
      n = rs.getDouble(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return n;
  }
  
  @Override
  public short getShort(String name)
  {
    short n = 0;
    try
    {
      n = rs.getShort(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return n;
  }
  
  @Override
  public boolean getBoolean(String name)
  {
    boolean b = false;
    try
    {
      b = rs.getBoolean(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return b;
  }
  
  @Override
  public byte getByte(String name)
  {
    byte b = 0;
    try
    {
      b = rs.getByte(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return b;
  }
  
  @Override
  public BigDecimal getBigDecimal(String name)
  {
    BigDecimal n = new BigDecimal(0);
    try
    {
      n = rs.getBigDecimal(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return n;
  }
  
  @Override
  public UUID getUUID(String name)
  {
    String str = "";
    try
    {
      str = StringUtils.trim(rs.getString(name));
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return UUID.fromString(str);
  }
  
  @Override
  public byte[] getByteArray(String name)
  {
    byte[] n = null;
    try
    {
      n = rs.getBytes(name);
    }
    catch (SQLException e)
    {
      LogUtil.getCoreLog().error(name, e);
    }
    return n;
  }
}
