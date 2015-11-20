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

abstract class AbstractTypedElementDefinition<T extends Definition> extends AbstractDefinition implements
    TypedElementDefinition<T>
{
  private transient DataTypeDefinition type;
  private final T container;
  private final DataTypeDefinition definedType;
  
  public AbstractTypedElementDefinition(T container, String name, int position, DataTypeDefinition definedType,
      String comment)
  {
    super(container.getDatabase(), container.getSchema(), protectName(container.getName(), name, position), comment);
    this.container = container;
    this.definedType = definedType;
  }
  
  private static String protectName(String table, String name, int position)
  {
    if (name == null)
    {
      return "_" + position;
    }
    return name;
  }
  
  @Override
  public DataTypeDefinition getType()
  {
    if (type == null)
    {
      type = mapDefinedType(container, this, definedType);
    }
    return type;
  }
  
  static DataTypeDefinition mapDefinedType(Definition container, Definition child, DataTypeDefinition definedType)
  {
    DataTypeDefinition result = definedType;
    return result;
  }
}
