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
package gemlite.shell.codegen.database;

import java.util.EnumSet;
import java.util.Set;

public enum SQLDialect
{
  DEFAULT(null, false), H2("H2", false), MYSQL("MySQL", false), ORACLE("Oracle", false), SYBASE("Sybase", false), SQLSERVER(
      "SQLServer", false);
  private static final SQLDialect[] FAMILIES;
  static
  {
    Set<SQLDialect> set = EnumSet.noneOf(SQLDialect.class);
    for (SQLDialect dialect : values())
    {
      set.add(dialect.family());
    }
    FAMILIES = set.toArray(new SQLDialect[set.size()]);
  }
  private final String name;
  private final boolean commercial;
  private final SQLDialect family;
  private SQLDialect predecessor;
  
  private SQLDialect(String name, boolean commercial)
  {
    this(name, commercial, null, null);
  }
  
  private SQLDialect(String name, boolean commercial, SQLDialect family)
  {
    this(name, commercial, family, null);
  }
  
  private SQLDialect(String name, boolean commercial, SQLDialect family, SQLDialect predecessor)
  {
    this.name = name;
    this.commercial = commercial;
    this.family = family == null ? this : family;
    this.predecessor = predecessor == null ? this : predecessor;
    if (family != null)
      family.predecessor = this;
  }
  
  public final boolean commercial()
  {
    return commercial;
  }
  
  public final SQLDialect family()
  {
    return family;
  }
  
  public final SQLDialect predecessor()
  {
    return predecessor;
  }
  
  public final boolean precedes(SQLDialect other)
  {
    if (family != other.family)
      return false;
    SQLDialect candidate = other;
    while (candidate != null)
    {
      if (this == candidate)
        return true;
      if (candidate == candidate.predecessor())
        return false;
      candidate = candidate.predecessor();
    }
    return false;
  }
  
  public final String getName()
  {
    return name;
  }
  
  public final String getNameLC()
  {
    return name == null ? null : name.toLowerCase();
  }
  
  public final String getNameUC()
  {
    return name == null ? null : name.toUpperCase();
  }
  
  public static final SQLDialect[] families()
  {
    return FAMILIES.clone();
  }
}
