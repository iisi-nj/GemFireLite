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

import static gemlite.shell.codegen.util.AbstractDatabase.getDefinition;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import gemlite.shell.codegen.tools.StringUtils;

public abstract class AbstractElementContainerDefinition<E extends TypedElementDefinition<?>> extends
    AbstractDefinition
{
  protected static final Pattern PRECISION_SCALE = Pattern.compile("\\((\\d+)\\s*(?:,\\s*(\\d+))?\\)");
  private List<E> elements;
  
  public AbstractElementContainerDefinition(SchemaDefinition schema, String name, String comment)
  {
    super(schema.getDatabase(), schema, name, comment);
  }
  
  @Override
  public final List<Definition> getDefinitionPath()
  {
    return Arrays.<Definition> asList(getSchema(), this);
  }
  
  protected final List<E> getElements()
  {
    if (elements == null)
    {
      elements = new ArrayList<E>();
      try
      {
        Database db = getDatabase();
        List<E> e = getElements0();
        // [#2603] Filter exclude / include also for table columns
        if (this instanceof TableDefinition && db.getIncludeExcludeColumns())
        {
          elements = db.filterExcludeInclude(e);
        }
        else
        {
          elements = e;
        }
      }
      catch (SQLException e)
      {
      }
    }
    return elements;
  }
  
  protected final E getElement(String name)
  {
    return getElement(name, false);
  }
  
  protected final E getElement(String name, boolean ignoreCase)
  {
    return getDefinition(getElements(), name, ignoreCase);
  }
  
  protected final E getElement(int index)
  {
    return getElements().get(index);
  }
  
  protected abstract List<E> getElements0() throws SQLException;
  
  protected Number parsePrecision(String typeName)
  {
    if (typeName.contains("("))
    {
      Matcher m = PRECISION_SCALE.matcher(typeName);
      if (m.find())
      {
        if (!StringUtils.isBlank(m.group(1)))
        {
          return Integer.valueOf(m.group(1));
        }
      }
    }
    return 0;
  }
  
  protected Number parseScale(String typeName)
  {
    if (typeName.contains("("))
    {
      Matcher m = PRECISION_SCALE.matcher(typeName);
      if (m.find())
      {
        if (!StringUtils.isBlank(m.group(2)))
        {
          return Integer.valueOf(m.group(2));
        }
      }
    }
    return 0;
  }
  
  protected String parseTypeName(String typeName)
  {
    return typeName.replace(" NOT NULL", "");
  }
  
  protected boolean parseNotNull(String typeName)
  {
    return typeName.toUpperCase().contains("NOT NULL");
  }
}
