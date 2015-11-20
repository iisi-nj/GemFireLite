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

import gemlite.shell.codegen.util.AbstractTableDefinition;
import gemlite.shell.codegen.util.ColumnDefinition;
import gemlite.shell.codegen.util.DataTypeDefinition;
import gemlite.shell.codegen.util.DefaultColumnDefinition;
import gemlite.shell.codegen.util.DefaultDataTypeDefinition;
import gemlite.shell.codegen.util.DefaultUniqueKeyDefinition;
import gemlite.shell.codegen.util.SchemaDefinition;
import gemlite.shell.codegen.util.UniqueKeyDefinition;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

public class MySQLTableDefinition extends AbstractTableDefinition
{
  public MySQLTableDefinition(SchemaDefinition schema, String name, String comment)
  {
    super(schema, name, comment);
  }
  
  @Override
  public List<ColumnDefinition> getElements0() throws SQLException
  {
    Connection conn = getConnection();
    Statement statement = conn.createStatement();
    String sql = null;
    ResultSet rs = null;
    List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();
    sql = "SELECT ORDINAL_POSITION,COLUMN_NAME,COLUMN_COMMENT,COLUMN_TYPE,DATA_TYPE,IS_NULLABLE, "
        + "COLUMN_DEFAULT,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE,EXTRA "
        + "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=' " + this.getSchema().getName() + "' AND TABLE_NAME='"
        + this.getName() + "'";
    rs = statement.executeQuery(sql);
    
    while (rs.next())
    {
      String dataType = rs.getString("DATA_TYPE");
      if (getDatabase().supportsUnsignedTypes())
      {
        if (Arrays.asList("tinyint", "smallint", "mediumint", "int", "bigint").contains(dataType.toLowerCase()))
        {
          if (rs.getString("COLUMN_TYPE").toLowerCase().contains("unsigned"))
          {
            dataType += "unsigned";
          }
        }
      }
      
      DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), getSchema(), dataType,
          rs.getLong("CHARACTER_MAXIMUM_LENGTH"), rs.getLong("NUMERIC_PRECISION"), rs.getLong("NUMERIC_SCALE"),
          rs.getBoolean("IS_NULLABLE"), rs.getString("COLUMN_DEFAULT") != null, getName() + "_"
              + rs.getString("COLUMN_NAME"));
      
      ColumnDefinition column = new DefaultColumnDefinition(getDatabase().getTable(getSchema(), getName()),
          rs.getString("COLUMN_NAME"), rs.getInt("ORDINAL_POSITION"), type, "AUTO_INCREMENT".equalsIgnoreCase(rs
              .getString("EXTRA")), rs.getString("COLUMN_COMMENT"));
      result.add(column);
    }
    statement.close();
    rs.close();
    return result;
  }
  
  @Override
  public UniqueKeyDefinition getPrimaryKey()
  {
    Connection conn = getConnection();
    Statement statement = null;
    String sql = null;
    ResultSet rs = null;
    UniqueKeyDefinition pk = new DefaultUniqueKeyDefinition(getSchema(), "PRIMARY", this, true);
    List<ColumnDefinition> columns = getColumns();
    try
    {
      statement = conn.createStatement();
      sql = "SELECT INDEX_NAME,COLUMN_NAME,NON_UNIQUE,SEQ_IN_INDEX FROM INFORMATION_SCHEMA.STATISTICS "
          + "WHERE TABLE_SCHEMA = '" + this.getSchema().getName() + "' AND TABLE_NAME='" + this.getName()
          + "' AND INDEX_NAME = '" + "PRIMARY'";
      rs = statement.executeQuery(sql);
      while (rs.next())
      {
        for (ColumnDefinition c : columns)
        {
          if (rs.getString("COLUMN_NAME").equalsIgnoreCase(c.getName()))
          {
            pk.getKeyColumns().add(c);
          }
        }
      }
      statement.close();
      rs.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return pk;
  }
  
  @Override
  public List<UniqueKeyDefinition> getUniqueKeys()
  {
    Connection conn = getConnection();
    Statement statement = null;
    String sql = null;
    ResultSet rs = null;
    List<UniqueKeyDefinition> uks = new ArrayList<UniqueKeyDefinition>();
    UniqueKeyDefinition uk = new DefaultUniqueKeyDefinition(getSchema(), "UNIQUE", this, false);
    List<ColumnDefinition> columns = getColumns();
    String indexName = "";
    try
    {
      statement = conn.createStatement();
      sql = "SELECT INDEX_NAME,COLUMN_NAME,NON_UNIQUE,SEQ_IN_INDEX FROM INFORMATION_SCHEMA.STATISTICS "
          + "WHERE TABLE_SCHEMA = '" + this.getSchema().getName() + "' AND TABLE_NAME='" + this.getName()
          + "' AND INDEX_NAME <> '" + "PRIMARY' AND NON_UNIQUE=" + 0L + " ORDER BY INDEX_NAME,SEQ_IN_INDEX";
      rs = statement.executeQuery(sql);
      while (rs.next())
      {
        if (!rs.getString("INDEX_NAME").equals(indexName) && !StringUtils.isEmpty(indexName))
          break;
        for (ColumnDefinition c : columns)
        {
          if (rs.getString("COLUMN_NAME").equalsIgnoreCase(c.getName()))
          {
            uk.getKeyColumns().add(c);
          }
        }
      }
      statement.close();
      rs.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    uks.add(uk);
    return uks;
  }
  
}
