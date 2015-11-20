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
package gemlite.shell.codegen.impl;

import gemlite.shell.codegen.database.DataType;
import gemlite.shell.codegen.util.mysql.MySQLDataType;
import gemlite.shell.codegen.util.oracle.OracleDataType;
import gemlite.shell.codegen.util.sqlserver.SQLServerDataType;
import gemlite.shell.codegen.util.sybase.SybaseDataType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public final class SQLDataType
{
  // -------------------------------------------------------------------------
  // String types
  // -------------------------------------------------------------------------
  
  public static final DataType<String> VARCHAR = new DefaultDataType<String>(null, String.class, "varchar");
  
  public static final DataType<String> CHAR = new DefaultDataType<String>(null, String.class, "char");
  
  public static final DataType<String> LONGVARCHAR = new DefaultDataType<String>(null, String.class, "longvarchar");
  
  public static final DataType<String> CLOB = new DefaultDataType<String>(null, String.class, "clob");
  
  public static final DataType<String> NVARCHAR = new DefaultDataType<String>(null, String.class, "nvarchar");
  
  public static final DataType<String> NCHAR = new DefaultDataType<String>(null, String.class, "nchar");
  
  public static final DataType<String> LONGNVARCHAR = new DefaultDataType<String>(null, String.class, "longnvarchar");
  
  public static final DataType<String> NCLOB = new DefaultDataType<String>(null, String.class, "nclob");
  // -------------------------------------------------------------------------
  // Boolean types
  // -------------------------------------------------------------------------
  
  public static final DataType<Boolean> BOOLEAN = new DefaultDataType<Boolean>(null, Boolean.class, "boolean");
  
  public static final DataType<Boolean> BIT = new DefaultDataType<Boolean>(null, Boolean.class, "bit");
  // -------------------------------------------------------------------------
  // Integer types
  // -------------------------------------------------------------------------
  
  public static final DataType<Byte> TINYINT = new DefaultDataType<Byte>(null, Byte.class, "tinyint");
  
  public static final DataType<Short> SMALLINT = new DefaultDataType<Short>(null, Short.class, "smallint");
  
  public static final DataType<Integer> INTEGER = new DefaultDataType<Integer>(null, Integer.class, "integer");
  
  public static final DataType<Long> BIGINT = new DefaultDataType<Long>(null, Long.class, "bigint");
  
  public static final DataType<Integer> DECIMAL_INTEGER = new DefaultDataType<Integer>(null, Integer.class,
      "decimal_integer");
  // -------------------------------------------------------------------------
  // Unsigned integer types
  // -------------------------------------------------------------------------
  
  public static final DataType<Byte> TINYINTUNSIGNED = new DefaultDataType<Byte>(null, Byte.class, "tinyintunsigned");
  
  public static final DataType<Short> SMALLINTUNSIGNED = new DefaultDataType<Short>(null, Short.class,
      "smallintunsigned");
  
  public static final DataType<Integer> INTEGERUNSIGNED = new DefaultDataType<Integer>(null, Integer.class,
      "integerunsigned");
  
  public static final DataType<Long> BIGINTUNSIGNED = new DefaultDataType<Long>(null, Long.class, "bigintunsigned");
  // -------------------------------------------------------------------------
  // Floating point types
  // -------------------------------------------------------------------------
  
  public static final DataType<Double> DOUBLE = new DefaultDataType<Double>(null, Double.class, "double");
  
  public static final DataType<Double> FLOAT = new DefaultDataType<Double>(null, Double.class, "float");
  
  public static final DataType<Float> REAL = new DefaultDataType<Float>(null, Float.class, "real");
  // -------------------------------------------------------------------------
  // Numeric types
  // -------------------------------------------------------------------------
  
  public static final DataType<BigDecimal> NUMERIC = new DefaultDataType<BigDecimal>(null, BigDecimal.class, "numeric");
  
  public static final DataType<BigDecimal> DECIMAL = new DefaultDataType<BigDecimal>(null, BigDecimal.class, "decimal");
  // -------------------------------------------------------------------------
  // Datetime types
  // -------------------------------------------------------------------------
  
  public static final DataType<Date> DATE = new DefaultDataType<Date>(null, Date.class, "date");
  
  public static final DataType<Date> TIMESTAMP = new DefaultDataType<Date>(null, Date.class, "timestamp");
  
  public static final DataType<Date> TIME = new DefaultDataType<Date>(null, Date.class, "time");
  
  public static final DataType<Date> INTERVALYEARTOMONTH = new DefaultDataType<Date>(null, Date.class,
      "interval year to month");
  
  public static final DataType<Date> INTERVALDAYTOSECOND = new DefaultDataType<Date>(null, Date.class,
      "interval day to second");
  // -------------------------------------------------------------------------
  // Binary types
  // -------------------------------------------------------------------------
  
  public static final DataType<byte[]> BINARY = new DefaultDataType<byte[]>(null, byte[].class, "binary");
  
  public static final DataType<byte[]> VARBINARY = new DefaultDataType<byte[]>(null, byte[].class, "varbinary");
  
  public static final DataType<byte[]> LONGVARBINARY = new DefaultDataType<byte[]>(null, byte[].class, "longvarbinary");
  
  public static final DataType<byte[]> BLOB = new DefaultDataType<byte[]>(null, byte[].class, "blob");
  // -------------------------------------------------------------------------
  // Other types
  // -------------------------------------------------------------------------
  
  public static final DataType<Object> OTHER = new DefaultDataType<Object>(null, Object.class, "other");
  
  public static final DataType<UUID> UUID = new DefaultDataType<UUID>(null, UUID.class, "uuid");
  // -------------------------------------------------------------------------
  // Static initialisation of dialect-specific data types
  // -------------------------------------------------------------------------
  static
  {
    // Load all dialect-specific data types
    try
    {
      Class.forName(MySQLDataType.class.getName());
      Class.forName(OracleDataType.class.getName());
      Class.forName(SQLServerDataType.class.getName());
      Class.forName(SybaseDataType.class.getName());
    }
    catch (Exception ignore)
    {
    }
  }
  
  private SQLDataType()
  {
  }
}
