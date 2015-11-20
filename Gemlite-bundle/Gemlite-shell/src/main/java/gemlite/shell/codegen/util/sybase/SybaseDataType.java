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
package gemlite.shell.codegen.util.sybase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import gemlite.shell.codegen.database.DataType;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.impl.DefaultDataType;
import gemlite.shell.codegen.impl.SQLDataType;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SybaseDataType
{
  public static final DataType<Byte> UNSIGNEDTINYINT = new DefaultDataType(SQLDialect.SYBASE,
      SQLDataType.TINYINTUNSIGNED, "unsigned tinyint");
  public static final DataType<Byte> TINYINT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.TINYINTUNSIGNED,
      "tinyint");
  public static final DataType<Short> SMALLINT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.SMALLINT,
      "smallint");
  public static final DataType<Short> UNSIGNEDSMALLLINT = new DefaultDataType(SQLDialect.SYBASE,
      SQLDataType.SMALLINTUNSIGNED, "unsigned smallint");
  public static final DataType<Integer> INT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.INTEGER, "int");
  public static final DataType<Integer> INTEGER = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.INTEGER, "integer");
  public static final DataType<Integer> UNSIGNEDINT = new DefaultDataType(SQLDialect.SYBASE,
      SQLDataType.INTEGERUNSIGNED, "unsigned int");
  public static final DataType<Long> BIGINT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.BIGINT, "bigint");
  public static final DataType<Long> UNSIGNEDBIGINT = new DefaultDataType(SQLDialect.SYBASE,
      SQLDataType.BIGINTUNSIGNED, "unsigned bigint");
  public static final DataType<Double> DOUBLE = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.DOUBLE, "double");
  public static final DataType<Double> FLOAT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.FLOAT, "float");
  public static final DataType<Float> REAL = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.REAL, "real");
  public static final DataType<BigDecimal> DECIMAL = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.DECIMAL,
      "decimal");
  public static final DataType<BigDecimal> NUMERIC = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.NUMERIC,
      "numeric");
  public static final DataType<Boolean> BIT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.BIT, "bit");
  public static final DataType<String> VARCHAR = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.VARCHAR, "varchar");
  public static final DataType<String> CHAR = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.CHAR, "char");
  public static final DataType<String> LONGNVARCHAR = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.LONGNVARCHAR,
      "long nvarchar");
  public static final DataType<String> LONGVARCHAR = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.LONGVARCHAR,
      "long varchar");
  public static final DataType<String> NCHAR = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.NCHAR, "nchar");
  public static final DataType<String> NTEXT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.NCLOB, "ntext");
  public static final DataType<String> NVARCHAR = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.NVARCHAR,
      "nvarchar");
  public static final DataType<String> TEXT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.CLOB, "text");
  public static final DataType<Date> DATE = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.DATE, "date");
  public static final DataType<Date> TIME = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.TIME, "time");
  public static final DataType<Date> DATETIME = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.TIMESTAMP,
      "datetime");
  public static final DataType<Date> TIMESTAMP = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.TIMESTAMP,
      "timestamp");
  public static final DataType<byte[]> BINARY = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.BINARY, "binary");
  public static final DataType<byte[]> LONGBINARY = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.LONGVARBINARY,
      "long binary");
  public static final DataType<byte[]> VARBINARY = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.VARBINARY,
      "varbinary");
  
  protected static final DataType<byte[]> __BLOB = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.BLOB, "binary");
  protected static final DataType<Boolean> __BOOLEAN = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.BOOLEAN,
      "bit");
  protected static final DataType<Byte> __BYTE = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.TINYINT, "tinyint");
  protected static final DataType<Byte> __BYTESIGNED = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.TINYINT,
      "signed tinyint");
  
  protected static final DataType<Integer> __BIGINTEGER = new DefaultDataType(SQLDialect.SYBASE,
      SQLDataType.DECIMAL_INTEGER, "decimal");
  
  public static final DataType<BigDecimal> MONEY = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.DECIMAL, "money");
  public static final DataType<BigDecimal> SMALLMONEY = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.DECIMAL,
      "smallmoney");
  public static final DataType<String> UNIQUEIDENTIFIERSTR = new DefaultDataType(SQLDialect.SYBASE,
      SQLDataType.VARCHAR, "uniqueidentifierstr");
  public static final DataType<String> XML = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.VARCHAR, "xml");
  public static final DataType<UUID> UNIQUEIDENTIFIER = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.UUID,
      "uniqueidentifier");
  public static final DataType<Date> DATETIMEOFFSET = new DefaultDataType(SQLDialect.SYBASE,
      SQLDataType.TIMESTAMP, "datetimeoffset");
  public static final DataType<Date> SMALLDATETIME = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.TIMESTAMP,
      "smalldatetime");
  public static final DataType<Date> TIMESTAMPWITHTIMEZONE = new DefaultDataType(SQLDialect.SYBASE,
      SQLDataType.TIMESTAMP, "timestampwithtimezone");
  public static final DataType<byte[]> IMAGE = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.BINARY, "image");
  public static final DataType<byte[]> VARBIT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.VARBINARY, "varbit");
  public static final DataType<byte[]> LONGVARBIT = new DefaultDataType(SQLDialect.SYBASE, SQLDataType.LONGVARBINARY,
      "longvarbit");
}
