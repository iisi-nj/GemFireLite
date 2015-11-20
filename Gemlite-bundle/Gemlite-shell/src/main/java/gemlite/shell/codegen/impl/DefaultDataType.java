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

import static java.util.Collections.unmodifiableCollection;
import static gemlite.shell.codegen.tools.reflect.Reflect.wrapper;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import gemlite.shell.codegen.database.Configuration;
import gemlite.shell.codegen.database.DataType;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.exception.SQLDialectNotSupportedException;
import gemlite.shell.codegen.types.Interval;

@SuppressWarnings({ "unchecked" })
public class DefaultDataType<T> implements DataType<T>
{
  private static final long serialVersionUID = 4155588654449505119L;
  private static final Pattern NORMALISE_PATTERN = Pattern
      .compile("\"|\\.|\\s|\\(\\w+(\\s*,\\s*\\w+)*\\)|(NOT\\s*NULL)?");
  private static final Pattern TYPE_NAME_PATTERN = Pattern.compile("\\([^\\)]*\\)");
  // -------------------------------------------------------------------------
  // Data type caches
  // -------------------------------------------------------------------------
  private static final Map<String, DataType<?>>[] TYPES_BY_NAME;
  private static final Map<Class<?>, DataType<?>>[] TYPES_BY_TYPE;
  private static final Map<DataType<?>, DataType<?>>[] TYPES_BY_SQL_DATATYPE;
  private static final Map<Class<?>, DataType<?>> SQL_DATATYPES_BY_TYPE;
  // -------------------------------------------------------------------------
  // Precisions
  // -------------------------------------------------------------------------
  private static final int LONG_PRECISION = String.valueOf(Long.MAX_VALUE).length();
  private static final int INTEGER_PRECISION = String.valueOf(Integer.MAX_VALUE).length();
  private static final int SHORT_PRECISION = String.valueOf(Short.MAX_VALUE).length();
  private static final int BYTE_PRECISION = String.valueOf(Byte.MAX_VALUE).length();
  // -------------------------------------------------------------------------
  // Data type attributes
  // -------------------------------------------------------------------------
  private final SQLDialect dialect;
  private final DataType<T> sqlDataType;
  private final Class<T> type;
  private final Class<T[]> arrayType;
  private final String castTypeName;
  private final String castTypeBase;
  private final String typeName;
  private final boolean nullable;
  private final boolean defaulted;
  private final int precision;
  private final int scale;
  private final int length;
  static
  {
    TYPES_BY_SQL_DATATYPE = new Map[SQLDialect.values().length];
    TYPES_BY_NAME = new Map[SQLDialect.values().length];
    TYPES_BY_TYPE = new Map[SQLDialect.values().length];
    for (SQLDialect dialect : SQLDialect.values())
    {
      TYPES_BY_SQL_DATATYPE[dialect.ordinal()] = new LinkedHashMap<DataType<?>, DataType<?>>();
      TYPES_BY_NAME[dialect.ordinal()] = new LinkedHashMap<String, DataType<?>>();
      TYPES_BY_TYPE[dialect.ordinal()] = new LinkedHashMap<Class<?>, DataType<?>>();
    }
    SQL_DATATYPES_BY_TYPE = new LinkedHashMap<Class<?>, DataType<?>>();
    try
    {
      Class.forName(SQLDataType.class.getName());
    }
    catch (Exception ignore)
    {
    }
  }
  
  public DefaultDataType(SQLDialect dialect, DataType<T> sqlDataType, String typeName)
  {
    this(dialect, sqlDataType, sqlDataType.getType(), typeName, typeName, sqlDataType.precision(), sqlDataType.scale(),
        sqlDataType.length(), sqlDataType.nullable(), sqlDataType.defaulted());
  }
  
  public DefaultDataType(SQLDialect dialect, DataType<T> sqlDataType, String typeName, String castTypeName)
  {
    this(dialect, sqlDataType, sqlDataType.getType(), typeName, castTypeName, sqlDataType.precision(), sqlDataType
        .scale(), sqlDataType.length(), sqlDataType.nullable(), sqlDataType.defaulted());
  }
  
  public DefaultDataType(SQLDialect dialect, Class<T> type, String typeName)
  {
    this(dialect, null, type, typeName, typeName, 0, 0, 0, true, false);
  }
  
  public DefaultDataType(SQLDialect dialect, Class<T> type, String typeName, String castTypeName)
  {
    this(dialect, null, type, typeName, castTypeName, 0, 0, 0, true, false);
  }
  
  DefaultDataType(SQLDialect dialect, Class<T> type, String typeName, String castTypeName, int precision, int scale,
      int length, boolean nullable, boolean defaulted)
  {
    this(dialect, null, type, typeName, castTypeName, precision, scale, length, nullable, defaulted);
  }
  
  DefaultDataType(SQLDialect dialect, DataType<T> sqlDataType, Class<T> type, String typeName, String castTypeName,
      int precision, int scale, int length, boolean nullable, boolean defaulted)
  {
    // ---------------------------------
    this.dialect = dialect;
    this.sqlDataType = (dialect == null) ? this : sqlDataType;
    this.type = type;
    this.typeName = typeName;
    this.castTypeName = castTypeName;
    this.castTypeBase = TYPE_NAME_PATTERN.matcher(castTypeName).replaceAll("").trim();
    this.arrayType = (Class<T[]>) Array.newInstance(type, 0).getClass();
    if (precision == 0)
    {
      if (type == Long.class)
      {
        precision = LONG_PRECISION;
      }
      else if (type == Integer.class)
      {
        precision = INTEGER_PRECISION;
      }
      else if (type == Short.class)
      {
        precision = SHORT_PRECISION;
      }
      else if (type == Byte.class)
      {
        precision = BYTE_PRECISION;
      }
    }
    this.nullable = nullable;
    this.defaulted = defaulted;
    this.precision = precision;
    this.scale = scale;
    this.length = length;
    // Register data type in static caches
    // -----------------------------------
    // Dialect-specific data types
    int ordinal = dialect == null ? SQLDialect.DEFAULT.ordinal() : dialect.ordinal();
    if (!TYPES_BY_NAME[ordinal].containsKey(typeName.toUpperCase()))
    {
      String normalised = DefaultDataType.normalise(typeName);
      if (TYPES_BY_NAME[ordinal].get(normalised) == null)
      {
        TYPES_BY_NAME[ordinal].put(normalised, this);
      }
    }
    if (TYPES_BY_TYPE[ordinal].get(type) == null)
    {
      TYPES_BY_TYPE[ordinal].put(type, this);
    }
    if (TYPES_BY_SQL_DATATYPE[ordinal].get(sqlDataType) == null)
    {
      TYPES_BY_SQL_DATATYPE[ordinal].put(sqlDataType, this);
    }
    // Global data types
    if (dialect == null)
    {
      if (SQL_DATATYPES_BY_TYPE.get(type) == null)
      {
        SQL_DATATYPES_BY_TYPE.put(type, this);
      }
    }
  }
  
  @Override
  public final DataType<T> nullable(boolean n)
  {
    return new DefaultDataType<T>(dialect, sqlDataType, type, typeName, castTypeName, precision, scale, length, n,
        defaulted);
  }
  
  @Override
  public final boolean nullable()
  {
    return nullable;
  }
  
  @Override
  public final DataType<T> defaulted(boolean d)
  {
    return new DefaultDataType<T>(dialect, sqlDataType, type, typeName, castTypeName, precision, scale, length,
        nullable, d);
  }
  
  @Override
  public final boolean defaulted()
  {
    return defaulted;
  }
  
  @Override
  public final DataType<T> precision(int p)
  {
    return precision(p, scale);
  }
  
  @Override
  public final DataType<T> precision(int p, int s)
  {
    if (precision == p && scale == s)
    {
      return this;
    }
    else
    {
      return new DefaultDataType<T>(dialect, sqlDataType, type, typeName, castTypeName, p, s, length, nullable,
          defaulted);
    }
  }
  
  @Override
  public final int precision()
  {
    return precision;
  }
  
  @Override
  public final boolean hasPrecision()
  {
    return type == BigInteger.class || type == BigDecimal.class;
  }
  
  @Override
  public final DataType<T> scale(int s)
  {
    if (scale == s)
    {
      return this;
    }
    else
    {
      return new DefaultDataType<T>(dialect, sqlDataType, type, typeName, castTypeName, precision, s, length, nullable,
          defaulted);
    }
  }
  
  @Override
  public final int scale()
  {
    return scale;
  }
  
  @Override
  public final boolean hasScale()
  {
    return type == BigDecimal.class;
  }
  
  @Override
  public final DataType<T> length(int l)
  {
    if (length == l)
    {
      return this;
    }
    else
    {
      return new DefaultDataType<T>(dialect, sqlDataType, type, typeName, castTypeName, precision, scale, l, nullable,
          defaulted);
    }
  }
  
  @Override
  public final int length()
  {
    return length;
  }
  
  @Override
  public final boolean hasLength()
  {
    return type == byte[].class || type == String.class;
  }
  
  @Override
  public final DataType<T> getSQLDataType()
  {
    return sqlDataType;
  }
  
  @Override
  public final DataType<T> getDataType(Configuration configuration)
  {
    // If this is a SQLDataType find the most suited dialect-specific
    // data type
    if (getDialect() == null)
    {
      DataType<?> dataType = TYPES_BY_SQL_DATATYPE[configuration.dialect().family().ordinal()].get(length(0).precision(
          0, 0));
      if (dataType != null)
      {
        return (DataType<T>) dataType.length(length).precision(precision, scale);
      }
    }
    // If this is already the dialect's specific data type, return this
    else if (getDialect().family() == configuration.dialect().family())
    {
      return this;
    }
    // If the SQL data type is not available stick with this data type
    else if (getSQLDataType() == null)
    {
      return this;
    }
    // If this is another dialect's specific data type, recurse
    else
    {
      getSQLDataType().getDataType(configuration);
    }
    return this;
  }
  
  @Override
  public int getSQLType()
  {
    if (type == Blob.class)
    {
      return Types.BLOB;
    }
    else if (type == Boolean.class)
    {
      return Types.BOOLEAN;
    }
    else if (type == BigInteger.class)
    {
      return Types.BIGINT;
    }
    else if (type == BigDecimal.class)
    {
      return Types.DECIMAL;
    }
    else if (type == Byte.class)
    {
      return Types.TINYINT;
    }
    else if (type == byte[].class)
    {
      return Types.BLOB;
    }
    else if (type == Clob.class)
    {
      return Types.CLOB;
    }
    else if (type == Date.class)
    {
      return Types.DATE;
    }
    else if (type == Double.class)
    {
      return Types.DOUBLE;
    }
    else if (type == Float.class)
    {
      return Types.FLOAT;
    }
    else if (type == Integer.class)
    {
      return Types.INTEGER;
    }
    else if (type == Long.class)
    {
      return Types.BIGINT;
    }
    else if (type == Short.class)
    {
      return Types.SMALLINT;
    }
    else if (type == String.class)
    {
      return Types.VARCHAR;
    }
    else if (type == Time.class)
    {
      return Types.TIME;
    }
    else if (type == Timestamp.class)
    {
      return Types.TIMESTAMP;
    }
    // The type byte[] is handled earlier.
    else if (type.isArray())
    {
      return Types.ARRAY;
    }
    else
    {
      return Types.OTHER;
    }
  }
  
  @Override
  public final Class<T> getType()
  {
    return type;
  }
  
  @Override
  public final Class<T[]> getArrayType()
  {
    return arrayType;
  }
  
  @Override
  public final String getTypeName()
  {
    return typeName;
  }
  
  @Override
  public String getTypeName(Configuration configuration)
  {
    return getDataType(configuration).getTypeName();
  }
  
  @Override
  public final String getCastTypeName()
  {
    if (length != 0 && hasLength())
    {
      return castTypeBase + "(" + length + ")";
    }
    else if (precision != 0 && hasPrecision())
    {
      if (scale != 0 && hasScale())
      {
        return castTypeBase + "(" + precision + ", " + scale + ")";
      }
      else
      {
        return castTypeBase + "(" + precision + ")";
      }
    }
    else
    {
      return castTypeName;
    }
  }
  
  @Override
  public String getCastTypeName(Configuration configuration)
  {
    return getDataType(configuration).getCastTypeName();
  }
  
  @Override
  public final SQLDialect getDialect()
  {
    return dialect;
  }
  
  public static DataType<Object> getDefaultDataType(String typeName)
  {
    return new DefaultDataType<Object>(SQLDialect.DEFAULT, Object.class, typeName, typeName);
  }
  
  public static DataType<Object> getDefaultDataType(SQLDialect dialect, String typeName)
  {
    return new DefaultDataType<Object>(dialect, Object.class, typeName, typeName);
  }
  
  public static DataType<?> getDataType(SQLDialect dialect, String typeName)
  {
    DataType<?> result = TYPES_BY_NAME[dialect.ordinal()].get(typeName.toUpperCase());
    // [#3225] Normalise only if necessary
    if (result == null)
    {
      typeName = DefaultDataType.normalise(typeName);
      result = TYPES_BY_NAME[dialect.ordinal()].get(typeName);
    }
    // UDT data types and others are registered using DEFAULT
    if (result == null)
    {
      result = TYPES_BY_NAME[SQLDialect.DEFAULT.ordinal()].get(typeName);
    }
    if (result == null)
    {
      throw new SQLDialectNotSupportedException("Type " + typeName + " is not supported in dialect " + dialect, false);
    }
    return result;
  }
  
  public static <T> DataType<T> getDataType(SQLDialect dialect, Class<T> type)
  {
    return getDataType(dialect, type, null);
  }
  
  public static <T> DataType<T> getDataType(SQLDialect dialect, Class<T> type, DataType<T> fallbackDataType)
  {
    // Treat primitive types the same way as their respective wrapper types
    type = (Class<T>) wrapper(type);
    DataType<?> result = null;
    if (dialect != null)
    {
      result = TYPES_BY_TYPE[dialect.ordinal()].get(type);
    }
    if (result == null)
    {
      if (SQL_DATATYPES_BY_TYPE.get(type) != null)
      {
        return (DataType<T>) SQL_DATATYPES_BY_TYPE.get(type);
      }
      // If we have a "fallback" data type from an outer context
      else if (fallbackDataType != null)
      {
        return fallbackDataType;
      }
      // All other data types are illegal
      else
      {
        throw new SQLDialectNotSupportedException("Type " + type + " is not supported in dialect " + dialect);
      }
    }
    return (DataType<T>) result;
  }
  
  @Override
  public final boolean isNumeric()
  {
    return Number.class.isAssignableFrom(type) && !isInterval();
  }
  
  @Override
  public final boolean isString()
  {
    return type == String.class;
  }
  
  @Override
  public final boolean isDateTime()
  {
    return java.util.Date.class.isAssignableFrom(type);
  }
  
  @Override
  public final boolean isTemporal()
  {
    return isDateTime() || isInterval();
  }
  
  @Override
  public final boolean isInterval()
  {
    return Interval.class.isAssignableFrom(type);
  }
  
  @Override
  public final boolean isLob()
  {
    DataType<T> t = getSQLDataType();
    return (t == SQLDataType.BLOB || t == SQLDataType.CLOB || t == SQLDataType.NCLOB);
  }
  
  @Override
  public final boolean isBinary()
  {
    return type == byte[].class;
  }
  
  @Override
  public final boolean isArray()
  {
    return (!isBinary() && type.isArray());
  }
  
  // ------------------------------------------------------------------------
  // The Object API
  // ------------------------------------------------------------------------
  @Override
  public String toString()
  {
    return getCastTypeName() + " (" + type.getName() + ")";
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((dialect == null) ? 0 : dialect.hashCode());
    result = prime * result + length;
    result = prime * result + precision;
    result = prime * result + scale;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DefaultDataType<?> other = (DefaultDataType<?>) obj;
    if (dialect != other.dialect)
      return false;
    if (length != other.length)
      return false;
    if (precision != other.precision)
      return false;
    if (scale != other.scale)
      return false;
    if (type == null)
    {
      if (other.type != null)
        return false;
    }
    else if (!type.equals(other.type))
      return false;
    if (typeName == null)
    {
      if (other.typeName != null)
        return false;
    }
    else if (!typeName.equals(other.typeName))
      return false;
    return true;
  }
  
  public static String normalise(String typeName)
  {
    return NORMALISE_PATTERN.matcher(typeName.toUpperCase()).replaceAll("");
  }
  
  public static DataType<?> getDataType(SQLDialect dialect, String t, int p, int s)
      throws SQLDialectNotSupportedException
  {
    DataType<?> result = DefaultDataType.getDataType(dialect, t);
    if (result.getType() == BigDecimal.class)
    {
      result = DefaultDataType.getDataType(dialect, getNumericClass(p, s));
    }
    if (result.getType() == Short.class)
      result = DefaultDataType.getDataType(dialect, Integer.class);
    return result;
  }
  
  public static Class<?> getType(SQLDialect dialect, String t, int p, int s) throws SQLDialectNotSupportedException
  {
    return getDataType(dialect, t, p, s).getType();
  }
  
  private static Class<?> getNumericClass(int precision, int scale)
  {
    // Integer numbers
    if (scale == 0)
    {
      return Integer.class;
      // if (precision == 0)
      // return Integer.class;
      // else
      // {
      // if (precision < BYTE_PRECISION)
      // {
      // return Byte.class;
      // }
      // if (precision < SHORT_PRECISION)
      // {
      // return Short.class;
      // }
      // if (precision < INTEGER_PRECISION)
      // {
      // return Integer.class;
      // }
      // if (precision < LONG_PRECISION)
      // {
      // return Long.class;
      // }
      // }
      // // Default integer number
      // return Integer.class;
    }
    // Real numbers should not be represented as float or double
    else
    {
      return Double.class;
    }
  }
  
  static Collection<Class<?>> types()
  {
    return unmodifiableCollection(SQL_DATATYPES_BY_TYPE.keySet());
  }
  
  static Collection<DataType<?>> dataTypes()
  {
    return unmodifiableCollection(SQL_DATATYPES_BY_TYPE.values());
  }
}
