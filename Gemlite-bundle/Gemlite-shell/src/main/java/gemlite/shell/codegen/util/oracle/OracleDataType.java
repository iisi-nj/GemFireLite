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
package gemlite.shell.codegen.util.oracle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import gemlite.shell.codegen.database.DataType;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.impl.DefaultDataType;
import gemlite.shell.codegen.impl.SQLDataType;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class OracleDataType
{
  public static final DataType<BigDecimal> NUMBER = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.NUMERIC,
      "number");
  public static final DataType<BigDecimal> NUMERIC = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.NUMERIC,
      "numeric");
  public static final DataType<BigDecimal> DECIMAL = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DECIMAL,
      "decimal");
  public static final DataType<BigDecimal> DEC = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DECIMAL, "dec");
  public static final DataType<String> VARCHAR2 = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.VARCHAR,
      "varchar2", "varchar2(4000)");
  public static final DataType<String> VARCHAR = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.VARCHAR, "varchar",
      "varchar2(4000)");
  public static final DataType<String> CHAR = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.CHAR, "char",
      "varchar2(4000)");
  public static final DataType<String> CLOB = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.CLOB, "clob");
  public static final DataType<String> NVARCHAR2 = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.NVARCHAR,
      "nvarchar2", "varchar2(4000)");
  public static final DataType<String> NVARCHAR = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.NVARCHAR,
      "nvarchar", "varchar2(4000)");
  public static final DataType<String> NCHAR = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.NCHAR, "nchar",
      "varchar2(4000)");
  public static final DataType<String> NCLOB = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.NCLOB, "nclob");
  public static final DataType<Date> DATE = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DATE, "date");
  public static final DataType<Date> TIMESTAMP = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.TIMESTAMP,
      "timestamp");
  public static final DataType<byte[]> BLOB = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BLOB, "blob");
  public static final DataType<Date> INTERVALYEARTOMONTH = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.INTERVALYEARTOMONTH, "interval year to month");
  public static final DataType<Date> INTERVALDAYTOSECOND = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.INTERVALDAYTOSECOND, "interval day to second");
  
  protected static final DataType<byte[]> __BINARY = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BINARY, "blob");
  protected static final DataType<Long> __BIGINT = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BIGINT, "number",
      "number(19)");
  protected static final DataType<Boolean> __BIT = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BIT, "number",
      "number(1)");
  protected static final DataType<Boolean> __BOOLEAN = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BOOLEAN,
      "number", "number(1)");
  protected static final DataType<Double> __DOUBLE = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DOUBLE,
      "number");
  protected static final DataType<Double> __FLOAT = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.FLOAT, "number");
  protected static final DataType<Integer> __INTEGER = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.INTEGER,
      "number", "number(10)");
  protected static final DataType<byte[]> __LONGVARBINARY = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.LONGVARBINARY, "blob");
  protected static final DataType<String> __LONGVARCHAR = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.LONGVARCHAR, "varchar2", "varchar2(4000)");
  protected static final DataType<String> __LONGNVARCHAR = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.LONGNVARCHAR, "varchar2", "varchar2(4000)");
  protected static final DataType<Float> __REAL = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.REAL, "number");
  protected static final DataType<Short> __SMALLINT = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.SMALLINT,
      "number", "number(5)");
  protected static final DataType<Date> __TIME = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.TIME, "timestamp");
  protected static final DataType<Byte> __TINYINT = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.TINYINT,
      "number", "number(3)");
  protected static final DataType<byte[]> __VARBINARY = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.VARBINARY,
      "blob");
  protected static final DataType<Byte> __TINYINTUNSIGNED = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.TINYINTUNSIGNED, "number", "number(3)");
  protected static final DataType<Short> __SMALLINTUNSIGNED = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.SMALLINTUNSIGNED, "number", "number(5)");
  protected static final DataType<Integer> __INTEGERUNSIGNED = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.INTEGERUNSIGNED, "number", "number(10)");
  protected static final DataType<Long> __BIGINTUNSIGNED = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.BIGINTUNSIGNED, "number", "number(20)");
  
  protected static final DataType<Integer> __BIGINTEGER = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.DECIMAL_INTEGER, "number");
  protected static final DataType<UUID> __UUID = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.UUID, "varchar2",
      "varchar2(36)");
  
  public static final DataType<String> LONG = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.CLOB, "long");
  public static final DataType<byte[]> RAW = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BLOB, "raw");
  public static final DataType<byte[]> LONGRAW = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BLOB, "longraw");
  public static final DataType<byte[]> BFILE = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BLOB, "bfile");
  
  public static final DataType<Integer> BINARY_INTEGER = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.INTEGER,
      "binary_integer");
  public static final DataType<Integer> PLS_INTEGER = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.INTEGER,
      "pls_integer");
  public static final DataType<Integer> NATURAL = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.INTEGER, "natural");
  public static final DataType<Integer> NATURALN = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.INTEGER,
      "naturaln");
  public static final DataType<Integer> POSITIVE = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.INTEGER,
      "positive");
  public static final DataType<Integer> POSITIVEN = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.INTEGER,
      "positiven");
  public static final DataType<Integer> SIGNTYPE = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.INTEGER,
      "signtype");
  public static final DataType<Double> REAL = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DOUBLE, "real");
  public static final DataType<Double> DOUBLE_PRECISION = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DOUBLE,
      "double_precision");
  public static final DataType<Double> BINARY_DOUBLE = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DOUBLE,
      "binary_double");
  public static final DataType<BigDecimal> FLOAT = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DECIMAL, "float");
  public static final DataType<BigDecimal> BINARY_FLOAT = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DECIMAL,
      "binary_float");
  public static final DataType<Integer> INTEGER = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.DECIMAL_INTEGER, "integer");
  public static final DataType<Integer> INT = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.DECIMAL_INTEGER,
      "int");
  public static final DataType<Integer> SMALLINT = new DefaultDataType(SQLDialect.ORACLE,
      SQLDataType.DECIMAL_INTEGER, "smallint");
  public static final DataType<Boolean> BOOLEAN = new DefaultDataType(SQLDialect.ORACLE, SQLDataType.BOOLEAN, "boolean");
}
