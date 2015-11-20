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
package gemlite.shell.codegen.util.h2;

import java.math.BigDecimal;
import java.util.Date;

import gemlite.shell.codegen.database.DataType;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.impl.DefaultDataType;
import gemlite.shell.codegen.impl.SQLDataType;

public class H2DataType
{
  
  // -------------------------------------------------------------------------
  // Default SQL data types and synonyms thereof
  // -------------------------------------------------------------------------
  
  public static final DataType<Byte> TINYINT = new DefaultDataType<Byte>(SQLDialect.H2, SQLDataType.TINYINT, "tinyint");
  public static final DataType<Short> SMALLINT = new DefaultDataType<Short>(SQLDialect.H2, SQLDataType.SMALLINT,
      "smallint");
  public static final DataType<Short> INT2 = new DefaultDataType<Short>(SQLDialect.H2, SQLDataType.SMALLINT, "int2");
  public static final DataType<Integer> INT = new DefaultDataType<Integer>(SQLDialect.H2, SQLDataType.INTEGER, "int");
  public static final DataType<Integer> INTEGER = new DefaultDataType<Integer>(SQLDialect.H2, SQLDataType.INTEGER,
      "integer");
  public static final DataType<Integer> MEDIUMINT = new DefaultDataType<Integer>(SQLDialect.H2, SQLDataType.INTEGER,
      "mediumint");
  public static final DataType<Integer> INT4 = new DefaultDataType<Integer>(SQLDialect.H2, SQLDataType.INTEGER, "int4");
  public static final DataType<Integer> SIGNED = new DefaultDataType<Integer>(SQLDialect.H2, SQLDataType.INTEGER,
      "signed");
  public static final DataType<Boolean> BOOLEAN = new DefaultDataType<Boolean>(SQLDialect.H2, SQLDataType.BOOLEAN,
      "boolean");
  public static final DataType<Boolean> BOOL = new DefaultDataType<Boolean>(SQLDialect.H2, SQLDataType.BOOLEAN, "bool");
  public static final DataType<Boolean> BIT = new DefaultDataType<Boolean>(SQLDialect.H2, SQLDataType.BIT, "bit");
  public static final DataType<Long> BIGINT = new DefaultDataType<Long>(SQLDialect.H2, SQLDataType.BIGINT, "bigint");
  public static final DataType<Long> INT8 = new DefaultDataType<Long>(SQLDialect.H2, SQLDataType.BIGINT, "int8");
  public static final DataType<BigDecimal> DECIMAL = new DefaultDataType<BigDecimal>(SQLDialect.H2,
      SQLDataType.DECIMAL, "decimal");
  public static final DataType<BigDecimal> DEC = new DefaultDataType<BigDecimal>(SQLDialect.H2, SQLDataType.DECIMAL,
      "dec");
  public static final DataType<BigDecimal> NUMBER = new DefaultDataType<BigDecimal>(SQLDialect.H2, SQLDataType.NUMERIC,
      "number");
  public static final DataType<BigDecimal> NUMERIC = new DefaultDataType<BigDecimal>(SQLDialect.H2,
      SQLDataType.NUMERIC, "numeric");
  public static final DataType<Double> DOUBLE = new DefaultDataType<Double>(SQLDialect.H2, SQLDataType.DOUBLE, "double");
  public static final DataType<Double> FLOAT = new DefaultDataType<Double>(SQLDialect.H2, SQLDataType.FLOAT, "float");
  public static final DataType<Double> FLOAT4 = new DefaultDataType<Double>(SQLDialect.H2, SQLDataType.FLOAT, "float4");
  public static final DataType<Double> FLOAT8 = new DefaultDataType<Double>(SQLDialect.H2, SQLDataType.FLOAT, "float8");
  public static final DataType<Float> REAL = new DefaultDataType<Float>(SQLDialect.H2, SQLDataType.REAL, "real");
  public static final DataType<Date> TIME = new DefaultDataType<Date>(SQLDialect.H2, SQLDataType.TIME, "time");
  public static final DataType<Date> DATE = new DefaultDataType<Date>(SQLDialect.H2, SQLDataType.DATE, "date");
  public static final DataType<Date> TIMESTAMP = new DefaultDataType<Date>(SQLDialect.H2, SQLDataType.TIMESTAMP,
      "timestamp");
  public static final DataType<Date> DATETIME = new DefaultDataType<Date>(SQLDialect.H2, SQLDataType.TIMESTAMP,
      "datetime");
  public static final DataType<byte[]> BINARY = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BINARY, "binary");
  public static final DataType<byte[]> VARBINARY = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.VARBINARY,
      "varbinary");
  public static final DataType<byte[]> LONGVARBINARY = new DefaultDataType<byte[]>(SQLDialect.H2,
      SQLDataType.LONGVARBINARY, "longvarbinary");
  public static final DataType<byte[]> BLOB = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BLOB, "blob");
  public static final DataType<Object> OTHER = new DefaultDataType<Object>(SQLDialect.H2, SQLDataType.OTHER, "other");
  public static final DataType<String> VARCHAR = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.VARCHAR,
      "varchar");
  public static final DataType<String> VARCHAR2 = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.VARCHAR,
      "varchar2");
  public static final DataType<String> CHAR = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.CHAR, "char");
  public static final DataType<String> CHARACTER = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.CHAR,
      "character");
  public static final DataType<String> LONGVARCHAR = new DefaultDataType<String>(SQLDialect.H2,
      SQLDataType.LONGVARCHAR, "longvarchar");
  public static final DataType<String> CLOB = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.CLOB, "clob");
  public static final DataType<String> NVARCHAR = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.NVARCHAR,
      "nvarchar");
  public static final DataType<String> NVARCHAR2 = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.NVARCHAR,
      "nvarchar2");
  public static final DataType<String> NCHAR = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.NCHAR, "nchar");
  public static final DataType<String> NCLOB = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.NCLOB, "nclob");
  
  // -------------------------------------------------------------------------
  // Compatibility types for supported SQLDialect.H2, SQLDataTypes
  // -------------------------------------------------------------------------
  
  protected static final DataType<String> __LONGNVARCHAR = new DefaultDataType<String>(SQLDialect.H2,
      SQLDataType.LONGNVARCHAR, "longvarchar");
  
  // -------------------------------------------------------------------------
  // Compatibility types for supported Java types
  // -------------------------------------------------------------------------
  protected static final DataType<Byte> __TINYINTUNSIGNED = new DefaultDataType<Byte>(SQLDialect.H2,
      SQLDataType.TINYINTUNSIGNED, "smallint");
  protected static final DataType<Short> __SMALLINTUNSIGNED = new DefaultDataType<Short>(SQLDialect.H2,
      SQLDataType.SMALLINTUNSIGNED, "int");
  protected static final DataType<Integer> __INTEGERUNSIGNED = new DefaultDataType<Integer>(SQLDialect.H2,
      SQLDataType.INTEGERUNSIGNED, "bigint");
  protected static final DataType<Long> __BIGINTUNSIGNED = new DefaultDataType<Long>(SQLDialect.H2,
      SQLDataType.BIGINTUNSIGNED, "number");
  
  // -------------------------------------------------------------------------
  // Dialect-specific data types and synonyms thereof
  // -------------------------------------------------------------------------
  
  public static final DataType<Short> YEAR = new DefaultDataType<Short>(SQLDialect.H2, SQLDataType.SMALLINT, "year");
  public static final DataType<Long> IDENTITY = new DefaultDataType<Long>(SQLDialect.H2, SQLDataType.BIGINT, "identity");
  public static final DataType<Date> SMALLDATETIME = new DefaultDataType<Date>(SQLDialect.H2, SQLDataType.TIMESTAMP,
      "smalldatetime");
  public static final DataType<byte[]> RAW = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BLOB, "raw");
  public static final DataType<byte[]> BYTEA = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BLOB, "bytea");
  public static final DataType<byte[]> TINYBLOB = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BLOB,
      "tinyblob");
  public static final DataType<byte[]> MEDIUMBLOB = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BLOB,
      "mediumblob");
  public static final DataType<byte[]> LONGBLOB = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BLOB,
      "longblob");
  public static final DataType<byte[]> IMAGE = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BLOB, "image");
  public static final DataType<byte[]> OID = new DefaultDataType<byte[]>(SQLDialect.H2, SQLDataType.BLOB, "oid");
  public static final DataType<String> VARCHAR_CASESENSITIVE = new DefaultDataType<String>(SQLDialect.H2,
      SQLDataType.VARCHAR, "varchar_casesensitive");
  public static final DataType<String> VARCHAR_IGNORECASE = new DefaultDataType<String>(SQLDialect.H2,
      SQLDataType.VARCHAR, "varchar_ignorecase");
  public static final DataType<String> TINYTEXT = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.CLOB,
      "tinytext");
  public static final DataType<String> TEXT = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.CLOB, "text");
  public static final DataType<String> MEDIUMTEXT = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.CLOB,
      "mediumtext");
  public static final DataType<String> LONGTEXT = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.CLOB,
      "longtext");
  public static final DataType<String> NTEXT = new DefaultDataType<String>(SQLDialect.H2, SQLDataType.NCLOB, "ntext");
}
