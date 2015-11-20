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
import java.util.List;

import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.util.AbstractDatabase;
import gemlite.shell.codegen.util.SchemaDefinition;
import gemlite.shell.codegen.util.TableDefinition;

public class OracleDatabase extends AbstractDatabase
{
  @Override
  public SQLDialect getDialect()
  {
    return SQLDialect.ORACLE;
  }
  
  @Override
  protected List<SchemaDefinition> getSchemata0() throws SQLException
  {
    List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();
    for (String schemata : getInputSchemata())
      result.add(new SchemaDefinition(this, schemata, ""));
    return result;
  }
  
  @Override
  protected List<TableDefinition> getTables0() throws SQLException
  {
    Connection conn = getConnection();
    Statement statement = conn.createStatement();
    List<String> inputSchemata = getInputSchemata();
    String sql = null;
    ResultSet rs = null;
    List<TableDefinition> result = new ArrayList<TableDefinition>();
    for (String schemata : inputSchemata)
    {
      SchemaDefinition schema = new SchemaDefinition(this, schemata, "");
      sql = "SELECT TABLE_NAME,COMMENTS FROM ALL_TAB_COMMENTS " + "WHERE OWNER='" + schemata.trim()
          + "' AND TABLE_NAME NOT LIKE '%$%'";
      rs = statement.executeQuery(sql);
      while (rs.next())
      {
        String tableName = rs.getString("TABLE_NAME");
        String comment = rs.getString("COMMENTS");
        OracleTableDefinition table = new OracleTableDefinition(schema, tableName, comment);
        result.add(table);
      }
      statement.close();
      rs.close();
    }
    return result;
  }
  
}
