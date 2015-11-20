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
package gemlite.core.internal.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public interface IDataSource
{
  public String getString(String name);
  
  public int getInt(String name);
  
  public long getLong(String name);
  
  public Date getDate(String name);
  
  public double getDouble(String name);
  
  public short getShort(String name);
  
  public boolean getBoolean(String name);
  
  public byte getByte(String name);
  
  public BigDecimal getBigDecimal(String name);
  
  public UUID getUUID(String name);
  
  public byte[] getByteArray(String name);
}
