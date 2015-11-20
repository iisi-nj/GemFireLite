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
import java.util.List;

import org.springframework.util.StringUtils;

public class H2TableDefinition extends AbstractTableDefinition
{
  public H2TableDefinition(SchemaDefinition schema, String name, String comment)
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
    sql = "SELECT COLUMN_NAME,ORDINAL_POSITION,TYPE_NAME,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE, "
        + "IS_NULLABLE,COLUMN_DEFAULT,REMARKS,SEQUENCE_NAME " + "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='"
        + this.getSchema().getName() + "' AND TABLE_NAME='" + this.getName() + "'";
    rs = statement.executeQuery(sql);
    
    while (rs.next())
    {
      String dataType = rs.getString("TYPE_NAME");
      DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(), getSchema(), dataType,
          rs.getLong("CHARACTER_MAXIMUM_LENGTH"), rs.getLong("NUMERIC_PRECISION"), rs.getLong("NUMERIC_SCALE"),
          rs.getBoolean("IS_NULLABLE"), rs.getString("COLUMN_DEFAULT") != null, getName() + "_"
              + rs.getString("COLUMN_NAME"));
      
      ColumnDefinition column = new DefaultColumnDefinition(getDatabase().getTable(getSchema(), getName()),
          rs.getString("COLUMN_NAME"), rs.getInt("ORDINAL_POSITION"), type, false, "");
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
      sql = "SELECT CONSTRAINTS.TABLE_SCHEMA, CONSTRAINTS.TABLE_NAME, CONSTRAINTS.CONSTRAINT_NAME, INDEXES.COLUMN_NAME "
          + " FROM INFORMATION_SCHEMA.CONSTRAINTS"
          + " JOIN INFORMATION_SCHEMA.INDEXES"
          + " ON CONSTRAINTS.TABLE_SCHEMA = INDEXES.TABLE_SCHEMA "
          + " AND CONSTRAINTS.TABLE_NAME = INDEXES.TABLE_NAME"
          + " AND CONSTRAINTS.UNIQUE_INDEX_NAME = INDEXES.INDEX_NAME"
          + " WHERE CONSTRAINTS.TABLE_SCHEMA = '"
          + this.getSchema().getName()
          + "' AND CONSTRAINTS.TABLE_NAME='"
          + this.getName()
          + "' AND CONSTRAINTS.CONSTRAINT_TYPE ='PRIMARY KEY'";
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
      sql = "SELECT CONSTRAINTS.TABLE_SCHEMA, CONSTRAINTS.TABLE_NAME, CONSTRAINTS.CONSTRAINT_NAME, INDEXES.COLUMN_NAME "
          + " FROM INFORMATION_SCHEMA.CONSTRAINTS"
          + " JOIN INFORMATION_SCHEMA.INDEXES"
          + " ON CONSTRAINTS.TABLE_SCHEMA = INDEXES.TABLE_SCHEMA "
          + " AND CONSTRAINTS.TABLE_NAME = INDEXES.TABLE_NAME"
          + " AND CONSTRAINTS.UNIQUE_INDEX_NAME = INDEXES.INDEX_NAME"
          + " WHERE CONSTRAINTS.TABLE_SCHEMA = '"
          + this.getSchema().getName()
          + "' AND CONSTRAINTS.TABLE_NAME='"
          + this.getName()
          + "' AND CONSTRAINTS.CONSTRAINT_TYPE ='UNIQUE'";
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
