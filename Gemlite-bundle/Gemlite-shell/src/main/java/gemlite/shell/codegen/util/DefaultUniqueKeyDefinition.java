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
package gemlite.shell.codegen.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultUniqueKeyDefinition extends AbstractDefinition implements UniqueKeyDefinition
{
  private final List<ColumnDefinition> keyColumns;
  private final TableDefinition table;
  private final boolean isPrimaryKey;
  
  public DefaultUniqueKeyDefinition(SchemaDefinition schema, String name, TableDefinition table, boolean isPrimaryKey)
  {
    super(schema.getDatabase(), schema, name, null);
    this.keyColumns = new ArrayList<ColumnDefinition>();
    this.table = table;
    this.isPrimaryKey = isPrimaryKey;
  }
  
  @Override
  public boolean isPrimaryKey()
  {
    return isPrimaryKey;
  }
  
  @Override
  public List<Definition> getDefinitionPath()
  {
    return Arrays.<Definition> asList(getSchema(), this);
  }
  
  @Override
  public List<ColumnDefinition> getKeyColumns()
  {
    return keyColumns;
  }
  
  @Override
  public TableDefinition getTable()
  {
    return table;
  }
}
