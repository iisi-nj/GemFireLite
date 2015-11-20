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
package gemlite.shell.codegen.tools.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLXML;
import java.sql.Statement;
import gemlite.shell.codegen.database.SQLDialect;

public class JDBCUtils
{
  public static final SQLDialect dialect(Connection connection)
  {
    SQLDialect result = SQLDialect.DEFAULT;
    if (connection != null)
    {
      try
      {
        DatabaseMetaData m = connection.getMetaData();
        String url = m.getURL();
        result = dialect(url);
      }
      catch (SQLException ignore)
      {
      }
    }
    if (result == SQLDialect.DEFAULT)
    {
      // If the dialect cannot be guessed from the URL, take some other
      // measures, e.g. by querying DatabaseMetaData.getDatabaseProductName()
    }
    return result;
  }
  
  public static final SQLDialect dialect(String url)
  {
    if (url == null)
      return SQLDialect.DEFAULT;
    
    if (url.startsWith("jdbc:mysql:") || url.startsWith("jdbc:google:"))
    {
      return SQLDialect.MYSQL;
    }
    else if ((url.startsWith("jdbc:oracle:")) || (url.startsWith("jdbc:oracle:oci")))
    {
      return SQLDialect.ORACLE;
    }
    else if ((url.startsWith("jdbc:sqlserver:")) || (url.startsWith("jdbc:jtds:sqlserver:"))
        || (url.startsWith("jdbc:microsoft:sqlserver:")) || (url.contains(":mssql")))
    {
      return SQLDialect.SQLSERVER;
    }
    else if (url.startsWith("jdbc:sybase:"))
    {
      return SQLDialect.SYBASE;
    }
    return SQLDialect.DEFAULT;
  }
  
  @SuppressWarnings("incomplete-switch")
  public static final String driver(String url)
  {
    switch (dialect(url).family())
    {
      case MYSQL:
        return "com.mysql.jdbc.Driver";
      case ORACLE:
        return "oracle.jdbc.driver.OracleDriver";
      case SQLSERVER:
        return "";
      case SYBASE:
        return "";
    }
    return "java.sql.Driver";
  }
  
  public static final void safeClose(Connection connection)
  {
    if (connection != null)
    {
      try
      {
        connection.close();
      }
      catch (Exception ignore)
      {
      }
    }
  }
  
  public static final void safeClose(Statement statement)
  {
    if (statement != null)
    {
      try
      {
        statement.close();
      }
      catch (Exception ignore)
      {
      }
    }
  }
  
  public static final void safeClose(ResultSet resultSet)
  {
    if (resultSet != null)
    {
      try
      {
        resultSet.close();
      }
      catch (Exception ignore)
      {
      }
    }
  }
  
  public static final void safeClose(ResultSet resultSet, PreparedStatement statement)
  {
    safeClose(resultSet);
    safeClose(statement);
  }
  
  public static final void safeFree(Blob blob)
  {
    if (blob != null)
    {
      try
      {
        blob.free();
      }
      catch (Exception ignore)
      {
      }
      // [#3069] The free() method was added only in JDBC 4.0 / Java 1.6
      catch (AbstractMethodError ignore)
      {
      }
    }
  }
  
  public static final void safeFree(Clob clob)
  {
    if (clob != null)
    {
      try
      {
        clob.free();
      }
      catch (Exception ignore)
      {
      }
      // [#3069] The free() method was added only in JDBC 4.0 / Java 1.6
      catch (AbstractMethodError ignore)
      {
      }
    }
  }
  
  public static final void safeFree(SQLXML xml)
  {
    if (xml != null)
    {
      try
      {
        xml.free();
      }
      catch (Exception ignore)
      {
      }
      // [#3069] The free() method was added only in JDBC 4.0 / Java 1.6
      catch (AbstractMethodError ignore)
      {
      }
    }
  }
  
  public static final void safeFree(Array array)
  {
    if (array != null)
    {
      try
      {
        array.free();
      }
      catch (Exception ignore)
      {
      }
      // [#3069] The free() method was added only in JDBC 4.0 / Java 1.6
      catch (AbstractMethodError ignore)
      {
      }
    }
  }
  
  public static final <T> T wasNull(SQLInput stream, T value) throws SQLException
  {
    return stream.wasNull() ? null : value;
  }
  
  public static final <T> T wasNull(ResultSet rs, T value) throws SQLException
  {
    return rs.wasNull() ? null : value;
  }
  
  public static final <T> T wasNull(CallableStatement statement, T value) throws SQLException
  {
    return statement.wasNull() ? null : value;
  }
  
  private JDBCUtils()
  {
  }
}
