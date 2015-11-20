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

import gemlite.core.common.DateUtil;
import gemlite.core.internal.domain.IDataSource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.transform.FieldSet;

public final class FileDataSource implements IDataSource
{
  private FieldSet rs;
  
  public FileDataSource(FieldSet rs)
  {
    this.rs = rs;
  }
  
  public final String getString(String name)
  {
    return StringUtils.trim(rs.readString(name));
  }
  
  public final int getInt(String name)
  {
    return rs.readInt(name);
  }
  
  public final long getLong(String name)
  {
    return rs.readLong(name);
  }
  
  public final Date getDate(String name)
  {
    return DateUtil.stringToDate(rs.readString(name));
  }
  
  public final double getDouble(String name)
  {
    return org.apache.commons.lang.math.NumberUtils.toDouble(rs.readString(name));
  }
  
  @Override
  public short getShort(String name)
  {
    return org.apache.commons.lang.math.NumberUtils.toShort(rs.readString(name));
  }
  
  @Override
  public boolean getBoolean(String name)
  {
    return rs.readBoolean(name);
  }
  
  @Override
  public byte getByte(String name)
  {
    return rs.readByte(name);
  }
  
  @Override
  public BigDecimal getBigDecimal(String name)
  {
    return rs.readBigDecimal(name);
  }
  
  @Override
  public UUID getUUID(String name)
  {
    return UUID.fromString(rs.readString(name));
  }
  
  @Override
  public byte[] getByteArray(String name)
  {
    String str = rs.readString(name);
    if (str == null)
      return null;
    return str.getBytes();
  }
}
