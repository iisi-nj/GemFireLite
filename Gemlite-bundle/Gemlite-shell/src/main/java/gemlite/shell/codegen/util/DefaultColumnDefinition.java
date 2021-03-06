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

import java.util.List;

public class DefaultColumnDefinition extends AbstractTypedElementDefinition<TableDefinition> implements
    ColumnDefinition
{
  private final int position;
  private final boolean isIdentity;
  
  public DefaultColumnDefinition(TableDefinition table, String name, int position, DataTypeDefinition type,
      boolean isIdentity, String comment)
  {
    super(table, name, position, type, comment);
    this.position = position;
    this.isIdentity = isIdentity;
  }
  
  @Override
  public final int getPosition()
  {
    return position;
  }
  
  @Override
  public final boolean isIdentity()
  {
    return isIdentity;
  }
  
  @Override
  public final boolean isNullable()
  {
    return getType().isNullable();
  }
  
  @Override
  public TableDefinition getContainer()
  {
    return null;
  }
  
  @Override
  public List<Definition> getDefinitionPath()
  {
    return null;
  }
}
