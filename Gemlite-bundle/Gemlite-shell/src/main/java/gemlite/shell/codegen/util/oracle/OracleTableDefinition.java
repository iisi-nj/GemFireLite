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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.util.StringUtils;

import gemlite.shell.codegen.util.AbstractTableDefinition;
import gemlite.shell.codegen.util.ColumnDefinition;
import gemlite.shell.codegen.util.DataTypeDefinition;
import gemlite.shell.codegen.util.DefaultColumnDefinition;
import gemlite.shell.codegen.util.DefaultDataTypeDefinition;
import gemlite.shell.codegen.util.DefaultUniqueKeyDefinition;
import gemlite.shell.codegen.util.SchemaDefinition;
import gemlite.shell.codegen.util.UniqueKeyDefinition;

public class OracleTableDefinition extends AbstractTableDefinition
{
  public OracleTableDefinition(SchemaDefinition schema, String name, String comment)
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
      sql = "SELECT ALL_CONS_COLUMNS.CONSTRAINT_NAME AS CONSTRAINT_NAME ,ALL_CONS_COLUMNS.TABLE_NAME AS TABLE_NAME,ALL_CONS_COLUMNS.COLUMN_NAME AS COLUMN_NAME "
          + "FROM ALL_CONS_COLUMNS JOIN ALL_CONSTRAINTS ON ALL_CONS_COLUMNS.CONSTRAINT_NAME = ALL_CONSTRAINTS.CONSTRAINT_NAME "
          + "WHERE ALL_CONSTRAINTS.CONSTRAINT_TYPE = 'P' AND ALL_CONSTRAINTS.CONSTRAINT_NAME NOT LIKE 'BIN$%' "
          + " AND ALL_CONS_COLUMNS.OWNER = '"
          + this.getSchema().getName()
          + "' AND ALL_CONS_COLUMNS.TABLE_NAME='"
          + this.getName() + "'";
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
      sql = "SELECT ALL_CONS_COLUMNS.CONSTRAINT_NAME AS INDEX_NAME,ALL_CONS_COLUMNS.TABLE_NAME AS TABLE_NAME,ALL_CONS_COLUMNS.COLUMN_NAME AS COLUMN_NAME "
          + "FROM ALL_CONS_COLUMNS JOIN ALL_CONSTRAINTS ON ALL_CONS_COLUMNS.CONSTRAINT_NAME = ALL_CONSTRAINTS.CONSTRAINT_NAME "
          + " WHERE ALL_CONSTRAINTS.CONSTRAINT_TYPE = 'U' AND ALL_CONSTRAINTS.CONSTRAINT_NAME NOT LIKE 'BIN$%'"
          + " AND ALL_CONS_COLUMNS.OWNER = '"
          + this.getSchema().getName()
          + "' AND ALL_CONS_COLUMNS.TABLE_NAME='"
          + this.getName() + "'";
      rs = statement.executeQuery(sql);
      if (rs.getRow() == 0)
      {
        sql = "SELECT ALL_IND_COLUMNS.INDEX_NAME AS INDEX_NAME,ALL_IND_COLUMNS.TABLE_NAME AS TABLE_NAME,ALL_IND_COLUMNS.COLUMN_NAME AS COLUMN_NAME "
            + "FROM ALL_IND_COLUMNS LEFT JOIN ALL_INDEXES ON ALL_IND_COLUMNS.INDEX_NAME = ALL_INDEXES.INDEX_NAME "
            + "WHERE UNIQUENESS = 'UNIQUE' AND ALL_IND_COLUMNS.TABLE_OWNER='"
            + this.getSchema().getName()
            + "' AND ALL_IND_COLUMNS.TABLE_NAME='" + this.getName() + "'";
        rs = statement.executeQuery(sql);
      }
      while (rs.next())
      {
        if (!rs.getString("INDEX_NAME").equals(indexName) && !StringUtils.isEmpty(indexName))
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
    sql = "SELECT DATA_TYPE,DATA_LENGTH,DATA_PRECISION,DATA_SCALE,DATA_DEFAULT,COLUMN_NAME,COLUMN_ID,NULLABLE "
        + "FROM ALL_TAB_COLS WHERE OWNER='" + this.getSchema().getName() + "' AND TABLE_NAME='" + this.getName() + "'";
    rs = statement.executeQuery(sql);
    
    while (rs.next())
    {
      DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), getSchema(), rs.getString("DATA_TYPE"),
          rs.getLong("DATA_LENGTH"), rs.getLong("DATA_PRECISION"), rs.getLong("DATA_SCALE"), rs.getString("NULLABLE")
              .equalsIgnoreCase("Y") ? true : false, false, "");
      
      ColumnDefinition column = new DefaultColumnDefinition(getDatabase().getTable(getSchema(), getName()),
          rs.getString("COLUMN_NAME"), rs.getInt("COLUMN_ID"), type, false, "");
      result.add(column);
    }
    statement.close();
    rs.close();
    return result;
  }
  
}
