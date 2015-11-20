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
import java.util.HashMap;
import java.util.List;

import org.springframework.util.StringUtils;

public class SQLServerTableDefinition extends AbstractTableDefinition
{
  public SQLServerTableDefinition(SchemaDefinition schema, String name, String comment)
  {
    super(schema, name, comment);
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
      sql = "SELECT KEY_COLUMN_USAGE.CONSTRAINT_NAME AS CONSTRAINT_NAME ,KEY_COLUMN_USAGE.TABLE_NAME AS TABLE_NAME,KEY_COLUMN_USAGE.COLUMN_NAME AS COLUMN_NAME "
          + "FROM TABLE_CONSTRAINTS JOIN KEY_COLUMN_USAGE ON TABLE_CONSTRAINTS.CONSTRAINT_NAME = KEY_COLUMN_USAGE.CONSTRAINT_NAME "
          + "WHERE ALL_CONSTRAINTS.CONSTRAINT_TYPE = 'PRIMARY KEY' "
          + " AND ALL_CONS_COLUMNS.OWNER = '"
          + this.getSchema().getName() + "' AND KEY_COLUMN_USAGE.TABLE_NAME='" + this.getName() + "'";
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
    HashMap<String, ColumnDefinition> columnMap = new HashMap<String, ColumnDefinition>();
    for (ColumnDefinition c : columns)
      columnMap.put(c.getName(), c);
    String indexName = "";
    try
    {
      statement = conn.createStatement();
      sql = "SELECT KEY_COLUMN_USAGE.CONSTRAINT_NAME AS CONSTRAINT_NAME ,KEY_COLUMN_USAGE.TABLE_NAME AS TABLE_NAME,KEY_COLUMN_USAGE.COLUMN_NAME AS COLUMN_NAME "
          + "FROM TABLE_CONSTRAINTS JOIN KEY_COLUMN_USAGE ON TABLE_CONSTRAINTS.CONSTRAINT_NAME = KEY_COLUMN_USAGE.CONSTRAINT_NAME "
          + "WHERE ALL_CONSTRAINTS.CONSTRAINT_TYPE = 'UNIQUE' "
          + " AND ALL_CONS_COLUMNS.OWNER = '"
          + this.getSchema().getName() + "' AND KEY_COLUMN_USAGE.TABLE_NAME='" + this.getName() + "'";
      rs = statement.executeQuery(sql);
      while (rs.next())
      {
        if (!rs.getString("CONSTRAINT_NAME").equals(indexName) && !StringUtils.isEmpty(indexName))
          break;
        uk.getKeyColumns().add(columnMap.get(rs.getString("COLUMN_NAME")));
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
  
  @Override
  protected List<ColumnDefinition> getElements0() throws SQLException
  {
    Connection conn = getConnection();
    Statement statement = conn.createStatement();
    String sql = null;
    ResultSet rs = null;
    List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();
    sql = "SELECT COLUMNS.COLUMN_NAME,COLUMNS.ORDINAL_POSITION,COLUMNS.DATA_TYPE,COLUMNS.NUMERIC_PRECISION,COLUMNS.NUMERIC_SCALE "
        + "FROM COLUMNS "
        + "JOIN SYS.OBJECTS O ON O.TYPE IN ('U', 'V') AND COLUMNS.TABLE_NAME = O.NAME "
        + "JOIN SYS.COLUMNS C ON C.OBJECT_ID = O.OBJECT_ID AND COLUMNS.TABLE_NAME = C.NAME "
        + "WHERE COLUMNS.TABLE_SCHEMA='"
        + this.getSchema().getName()
        + "' AND COLUMNS.TABLE_NAME='"
        + this.getName()
        + "'";
    rs = statement.executeQuery(sql);
    
    while (rs.next())
    {
      DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), getSchema(), rs.getString("DATA_TYPE"),
          rs.getLong("DATA_LENGTH"), rs.getLong("NUMERIC_PRECISION"), rs.getLong("NUMERIC_SCALE"), false, false, "");
      
      ColumnDefinition column = new DefaultColumnDefinition(getDatabase().getTable(getSchema(), getName()),
          rs.getString("COLUMN_NAME"), rs.getInt("ORDINAL_POSITION"), type, false, "");
      result.add(column);
    }
    statement.close();
    rs.close();
    return result;
  }
  
}
