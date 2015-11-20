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
package gemlite.shell.codegen.impl;

import java.util.HashMap;
import java.util.Map;
import gemlite.shell.codegen.database.Configuration;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.database.Scope;
import gemlite.shell.codegen.conf.Settings;

abstract class AbstractScope implements Scope
{
  private final Configuration configuration;
  private final Map<Object, Object> data;
  
  AbstractScope(Configuration configuration)
  {
    this.data = new HashMap<Object, Object>();
    // The Configuration can be null when unattached objects are
    // executed or when unattached Records are stored...
    if (configuration == null)
    {
      configuration = new DefaultConfiguration();
    }
    this.configuration = configuration;
  }
  
  AbstractScope(AbstractScope scope)
  {
    this.data = scope.data;
    this.configuration = scope.configuration;
  }
  
  // ------------------------------------------------------------------------
  // ------------------------------------------------------------------------
  @Override
  public final Configuration configuration()
  {
    return configuration;
  }
  
  @Override
  public final Settings settings()
  {
    return Utils.settings(configuration());
  }
  
  @Override
  public final SQLDialect dialect()
  {
    return Utils.configuration(configuration()).dialect();
  }
  
  @Override
  public final SQLDialect family()
  {
    return dialect().family();
  }
  
  @Override
  public final Map<Object, Object> data()
  {
    return data;
  }
  
  @Override
  public final Object data(Object key)
  {
    return data.get(key);
  }
  
  @Override
  public final Object data(Object key, Object value)
  {
    return data.put(key, value);
  }
}
