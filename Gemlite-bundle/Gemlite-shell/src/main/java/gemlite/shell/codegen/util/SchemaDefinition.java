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

import java.util.Arrays;
import java.util.List;
import gemlite.shell.codegen.tools.StringUtils;

public class SchemaDefinition extends AbstractDefinition
{
  public SchemaDefinition(Database database, String name, String comment)
  {
    super(database, null, name, comment);
  }
  
  public final List<TableDefinition> getTables()
  {
    return getDatabase().getTables(this);
  }
  
  @SuppressWarnings("deprecation")
  @Override
  public final String getOutputName()
  {
    return getDatabase().getOutputSchema(getInputName());
  }
  
  @Override
  public final List<Definition> getDefinitionPath()
  {
    return Arrays.<Definition> asList(this);
  }
  
  public boolean isDefaultSchema()
  {
    return StringUtils.isBlank(getOutputName());
  }
}
