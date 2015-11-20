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

public class SybaseTableDefinition extends AbstractTableDefinition
{
  public SybaseTableDefinition(SchemaDefinition schema, String name, String comment)
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
      sql = "select all_cons_columns.constraint_name as constraint_name ,all_cons_columns.table_name as table_name,all_cons_columns.column_name as column_name from all_cons_columns join all_constraints on all_cons_columns.constraint_name = all_constraints.constraint_name "
          + " where all_constraints.constraint_type = 'P' and all_constraints.constraint_name not like 'bin$%'"
          + "and all_cons_columns.owner = '"
          + this.getSchema().getName()
          + "' and all_cons_columns.table_name='"
          + this.getName() + "'";
      rs = statement.executeQuery(sql);
      while (rs.next())
      {
        for (ColumnDefinition c : columns)
        {
          if (rs.getString("column_name").equalsIgnoreCase(c.getName()))
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
      sql = "select all_cons_columns.constraint_name as index_name,all_cons_columns.table_name as table_name,all_cons_columns.column_name as column_name from all_cons_columns join all_constraints on all_cons_columns.constraint_name = all_constraints.constraint_name "
          + " where all_constraints.constraint_type = 'U' and all_constraints.constraint_name not like 'bin$%'"
          + "and all_cons_columns.owner = '"
          + this.getSchema().getName()
          + "' and all_cons_columns.table_name='"
          + this.getName() + "'";
      rs = statement.executeQuery(sql);
      if (rs.getRow() == 0)
      {
        sql = "select ALL_IND_COLUMNS.INDEX_NAME as index_name,ALL_IND_COLUMNS.TABLE_NAME as table_name,ALL_IND_COLUMNS.COLUMN_NAME as column_name "
            + "from ALL_IND_COLUMNS left join all_indexes on ALL_IND_COLUMNS.INDEX_NAME = all_indexes.INDEX_NAME "
            + "where uniqueness = 'UNIQUE' and ALL_IND_COLUMNS.TABLE_OWNER='"
            + this.getSchema().getName()
            + "' and ALL_IND_COLUMNS.TABLE_NAME='" + this.getName() + "'";
        rs = statement.executeQuery(sql);
      }
      while (rs.next())
      {
        if (!rs.getString("index_name").equals(indexName) && !StringUtils.isEmpty(indexName))
          break;
        uk.getKeyColumns().add(columnMap.get(rs.getString("column_name")));
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
    sql = "SELECT SYSTABCOL.COLUMN_NAME,SYSTABCOL.COLUMN_ID,SYSDOMAIN.DOMAIN_NAME,SYSTABCOL.WIDTH,SYSTABCOL.SCALE,SYSTABCOL.DEFAULT "
        + "FROM SYSTABCOL "
        + "JOIN SYSTAB ON SYSTABCOL.TABLE_ID = SYSTAB.TABLE_ID "
        + "JOIN SYSDOMAIN ON SYSTABCOL.DOMAIN_ID = SYSDOMAIN.DOMAIN_ID "
        + "WHERE SYSTAB.TABLE_NAME = '"
        + this.getName() + "' " + "ORDER BY SYSTABCOL.COLUMN_ID";
    rs = statement.executeQuery(sql);
    
    while (rs.next())
    {
      DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), getSchema(), rs.getString("data_type"),
          rs.getLong("data_length"), rs.getLong("data_precision"), rs.getLong("data_scale"), rs.getString("nullable")
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
