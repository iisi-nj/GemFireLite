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
package gemlite.shell.codegen.util.sqlserver;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import gemlite.shell.codegen.database.DataType;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.impl.DefaultDataType;
import gemlite.shell.codegen.impl.SQLDataType;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SQLServerDataType
{
  public static final DataType<Byte> TINYINT = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.TINYINTUNSIGNED,
      "tinyint");
  public static final DataType<Short> SMALLINT = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.SMALLINT,
      "smallint");
  public static final DataType<Integer> INT = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.INTEGER, "int");
  public static final DataType<Long> BIGINT = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.BIGINT, "bigint");
  public static final DataType<Double> FLOAT = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.FLOAT, "float");
  public static final DataType<Float> REAL = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.REAL, "real");
  public static final DataType<BigDecimal> NUMERIC = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.NUMERIC,
      "numeric");
  public static final DataType<BigDecimal> DECIMAL = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.DECIMAL,
      "decimal");
  public static final DataType<Boolean> BIT = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.BIT, "bit");
  public static final DataType<Date> DATE = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.DATE, "date");
  public static final DataType<Date> DATETIME = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.TIMESTAMP,
      "datetime");
  public static final DataType<Date> TIME = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.TIME, "time");
  public static final DataType<String> VARCHAR = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.VARCHAR,
      "varchar");
  public static final DataType<String> CHAR = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.CHAR, "char");
  public static final DataType<String> TEXT = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.CLOB, "text");
  public static final DataType<String> NVARCHAR = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.NVARCHAR,
      "nvarchar");
  public static final DataType<String> NCHAR = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.NCHAR, "nchar");
  public static final DataType<String> NTEXT = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.NCLOB, "ntext");
  public static final DataType<byte[]> VARBINARY = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.VARBINARY,
      "varbinary", "varbinary(max)");
  public static final DataType<byte[]> BINARY = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.BINARY, "binary");
  
  protected static final DataType<byte[]> __BLOB = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.BLOB, "binary");
  protected static final DataType<Boolean> __BOOLEAN = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.BOOLEAN,
      "bit");
  protected static final DataType<Double> __DOUBLE = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.DOUBLE,
      "float");
  protected static final DataType<byte[]> __LONGVARBINARY = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.LONGVARBINARY, "varbinary", "varbinary(max)");
  protected static final DataType<String> __LONGVARCHAR = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.LONGVARCHAR, "varchar");
  protected static final DataType<String> __NCLOB = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.NCLOB, "text");
  protected static final DataType<String> __LONGNVARCHAR = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.LONGNVARCHAR, "varchar");
  protected static final DataType<Byte> __BYTE = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.TINYINT,
      "signed tinyint", "tinyint");
  
  protected static final DataType<Integer> __BIGINTEGER = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.DECIMAL_INTEGER, "numeric");
  protected static final DataType<Short> __SMALLINTUNSIGNED = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.SMALLINTUNSIGNED, "int");
  protected static final DataType<Integer> __INTEGERUNSIGNED = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.INTEGERUNSIGNED, "bigint");
  protected static final DataType<Long> __BIGINTUNSIGNED = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.BIGINTUNSIGNED, "numeric");
  
  public static final DataType<Date> SMALLDATETIME = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.TIMESTAMP, "smalldatetime");
  public static final DataType<Date> DATETIME2 = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.TIMESTAMP,
      "datetime2");
  public static final DataType<Date> DATETIMEOFFSET = new DefaultDataType(SQLDialect.SQLSERVER,
      SQLDataType.TIMESTAMP, "datetimeoffset");
  public static final DataType<BigDecimal> MONEY = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.DECIMAL,
      "money");
  public static final DataType<BigDecimal> SMALLMONEY = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.DECIMAL,
      "smallmoney");
  public static final DataType<byte[]> IMAGE = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.BINARY, "image");
  public static final DataType<UUID> UNIQUEIDENTIFIER = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.UUID,
      "uniqueidentifier");
  public static final DataType<Long> ROWVERSION = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.BIGINT,
      "rowversion");
  public static final DataType<Long> TIMESTAMP = new DefaultDataType(SQLDialect.SQLSERVER, SQLDataType.BIGINT,
      "timestamp");
}
