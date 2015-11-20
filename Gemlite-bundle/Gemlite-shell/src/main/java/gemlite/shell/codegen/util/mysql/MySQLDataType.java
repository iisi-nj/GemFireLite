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
package gemlite.shell.codegen.util.mysql;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import gemlite.shell.codegen.database.DataType;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.impl.DefaultDataType;
import gemlite.shell.codegen.impl.SQLDataType;

public class MySQLDataType
{
  // -------------------------------------------------------------------------
  // Default SQL data types and synonyms thereof
  // -------------------------------------------------------------------------
  public static final DataType<Byte> TINYINT = new DefaultDataType<Byte>(SQLDialect.MYSQL, SQLDataType.TINYINT,
      "tinyint", "signed");
  public static final DataType<Byte> TINYINTUNSIGNED = new DefaultDataType<Byte>(SQLDialect.MYSQL,
      SQLDataType.TINYINTUNSIGNED, "tinyintunsigned", "unsigned");
  public static final DataType<Short> SMALLINT = new DefaultDataType<Short>(SQLDialect.MYSQL, SQLDataType.SMALLINT,
      "smallint", "signed");
  public static final DataType<Short> SMALLINTUNSIGNED = new DefaultDataType<Short>(SQLDialect.MYSQL,
      SQLDataType.SMALLINTUNSIGNED, "smallintunsigned", "unsigned");
  public static final DataType<Integer> INT = new DefaultDataType<Integer>(SQLDialect.MYSQL, SQLDataType.INTEGER,
      "int", "signed");
  public static final DataType<Integer> INTUNSIGNED = new DefaultDataType<Integer>(SQLDialect.MYSQL,
      SQLDataType.INTEGERUNSIGNED, "intunsigned", "unsigned");
  public static final DataType<Integer> MEDIUMINT = new DefaultDataType<Integer>(SQLDialect.MYSQL, SQLDataType.INTEGER,
      "mediumint", "signed");
  public static final DataType<Integer> MEDIUMINTUNSIGNED = new DefaultDataType<Integer>(SQLDialect.MYSQL,
      SQLDataType.INTEGERUNSIGNED, "mediumintunsigned", "unsigned");
  public static final DataType<Integer> INTEGER = new DefaultDataType<Integer>(SQLDialect.MYSQL, SQLDataType.INTEGER,
      "integer", "signed");
  public static final DataType<Integer> INTEGERUNSIGNED = new DefaultDataType<Integer>(SQLDialect.MYSQL,
      SQLDataType.INTEGERUNSIGNED, "integerunsigned", "unsigned");
  public static final DataType<Long> BIGINT = new DefaultDataType<Long>(SQLDialect.MYSQL, SQLDataType.BIGINT, "bigint",
      "signed");
  public static final DataType<Long> BIGINTUNSIGNED = new DefaultDataType<Long>(SQLDialect.MYSQL,
      SQLDataType.BIGINTUNSIGNED, "bigintunsigned", "unsigned");
  public static final DataType<Double> DOUBLE = new DefaultDataType<Double>(SQLDialect.MYSQL, SQLDataType.DOUBLE,
      "double", "decimal");
  public static final DataType<Double> FLOAT = new DefaultDataType<Double>(SQLDialect.MYSQL, SQLDataType.FLOAT,
      "float", "decimal");
  public static final DataType<Float> REAL = new DefaultDataType<Float>(SQLDialect.MYSQL, SQLDataType.REAL, "real",
      "decimal");
  public static final DataType<Boolean> BOOLEAN = new DefaultDataType<Boolean>(SQLDialect.MYSQL, SQLDataType.BOOLEAN,
      "boolean", "unsigned");
  public static final DataType<Boolean> BOOL = new DefaultDataType<Boolean>(SQLDialect.MYSQL, SQLDataType.BOOLEAN,
      "bool", "unsigned");
  public static final DataType<Boolean> BIT = new DefaultDataType<Boolean>(SQLDialect.MYSQL, SQLDataType.BIT, "bit",
      "unsigned");
  public static final DataType<BigDecimal> DECIMAL = new DefaultDataType<BigDecimal>(SQLDialect.MYSQL,
      SQLDataType.DECIMAL, "decimal", "decimal");
  public static final DataType<BigDecimal> DEC = new DefaultDataType<BigDecimal>(SQLDialect.MYSQL, SQLDataType.DECIMAL,
      "dec", "decimal");
  public static final DataType<String> VARCHAR = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.VARCHAR,
      "varchar", "char");
  public static final DataType<String> CHAR = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.CHAR, "char",
      "char");
  public static final DataType<String> TEXT = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.CLOB, "text",
      "char");
  public static final DataType<byte[]> BLOB = new DefaultDataType<byte[]>(SQLDialect.MYSQL, SQLDataType.BLOB, "blob",
      "binary");
  public static final DataType<byte[]> BINARY = new DefaultDataType<byte[]>(SQLDialect.MYSQL, SQLDataType.BINARY,
      "binary", "binary");
  public static final DataType<byte[]> VARBINARY = new DefaultDataType<byte[]>(SQLDialect.MYSQL, SQLDataType.VARBINARY,
      "varbinary", "binary");
  public static final DataType<Date> DATE = new DefaultDataType<Date>(SQLDialect.MYSQL, SQLDataType.DATE, "date",
      "date");
  public static final DataType<Date> TIME = new DefaultDataType<Date>(SQLDialect.MYSQL, SQLDataType.TIME, "time",
      "time");
  public static final DataType<Date> TIMESTAMP = new DefaultDataType<Date>(SQLDialect.MYSQL, SQLDataType.TIMESTAMP,
      "timestamp", "datetime");
  public static final DataType<Date> DATETIME = new DefaultDataType<Date>(SQLDialect.MYSQL, SQLDataType.TIMESTAMP,
      "datetime", "datetime");
  // -------------------------------------------------------------------------
  // Compatibility types for supported SQLDialect.MYSQL, SQLDataTypes
  // -------------------------------------------------------------------------
  protected static final DataType<String> __NCHAR = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.NCHAR,
      "char", "char");
  protected static final DataType<String> __NCLOB = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.NCLOB,
      "clob", "char");
  protected static final DataType<String> __LONGNVARCHAR = new DefaultDataType<String>(SQLDialect.MYSQL,
      SQLDataType.LONGNVARCHAR, "varchar", "char");
  protected static final DataType<BigDecimal> __NUMERIC = new DefaultDataType<BigDecimal>(SQLDialect.MYSQL,
      SQLDataType.NUMERIC, "decimal", "decimal");
  protected static final DataType<String> __NVARCHAR = new DefaultDataType<String>(SQLDialect.MYSQL,
      SQLDataType.NVARCHAR, "varchar", "char");
  protected static final DataType<String> __LONGVARCHAR = new DefaultDataType<String>(SQLDialect.MYSQL,
      SQLDataType.LONGVARCHAR, "varchar", "char");
  protected static final DataType<byte[]> __LONGVARBINARY = new DefaultDataType<byte[]>(SQLDialect.MYSQL,
      SQLDataType.LONGVARBINARY, "varbinary", "binary");
  // -------------------------------------------------------------------------
  // Compatibility types for supported Java types
  // -------------------------------------------------------------------------
  protected static final DataType<Integer> __BIGINTEGER = new DefaultDataType<Integer>(SQLDialect.MYSQL,
      SQLDataType.DECIMAL_INTEGER, "decimal", "decimal");
  protected static final DataType<UUID> __UUID = new DefaultDataType<UUID>(SQLDialect.MYSQL, SQLDataType.UUID,
      "varchar", "char");
  // -------------------------------------------------------------------------
  // Dialect-specific data types and synonyms thereof
  // -------------------------------------------------------------------------
  public static final DataType<String> TINYTEXT = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.CLOB,
      "tinytext", "char");
  public static final DataType<String> MEDIUMTEXT = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.CLOB,
      "mediumtext", "char");
  public static final DataType<String> LONGTEXT = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.CLOB,
      "longtext", "char");
  public static final DataType<String> ENUM = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.VARCHAR,
      "enum", "char");
  public static final DataType<String> SET = new DefaultDataType<String>(SQLDialect.MYSQL, SQLDataType.VARCHAR, "set",
      "char");
  public static final DataType<byte[]> TINYBLOB = new DefaultDataType<byte[]>(SQLDialect.MYSQL, SQLDataType.BLOB,
      "tinyblob", "binary");
  public static final DataType<byte[]> MEDIUMBLOB = new DefaultDataType<byte[]>(SQLDialect.MYSQL, SQLDataType.BLOB,
      "mediumblob", "binary");
  public static final DataType<byte[]> LONGBLOB = new DefaultDataType<byte[]>(SQLDialect.MYSQL, SQLDataType.BLOB,
      "longblob", "binary");
  public static final DataType<Date> YEAR = new DefaultDataType<Date>(SQLDialect.MYSQL, SQLDataType.DATE, "year",
      "date");
}
