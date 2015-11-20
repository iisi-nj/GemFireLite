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

import java.util.Map;
import gemlite.shell.codegen.database.Configuration;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.conf.Settings;

public class DefaultConfiguration implements Configuration
{
  private static final long serialVersionUID = 2629245685896658739L;
  
  @Override
  public Map<Object, Object> data()
  {
    return null;
  }
  
  @Override
  public Object data(Object key)
  {
    return null;
  }
  
  @Override
  public Object data(Object key, Object value)
  {
    return null;
  }
  
  @Override
  public SQLDialect dialect()
  {
    return null;
  }
  
  @Override
  public SQLDialect family()
  {
    return null;
  }
  
  @Override
  public Settings settings()
  {
    return null;
  }
  
  @Override
  public Configuration set(SQLDialect newDialect)
  {
    return null;
  }
  
  @Override
  public Configuration set(Settings newSettings)
  {
    return null;
  }
  
  @Override
  public Configuration derive()
  {
    return null;
  }
  
  @Override
  public Configuration derive(SQLDialect newDialect)
  {
    return null;
  }
  
  @Override
  public Configuration derive(Settings newSettings)
  {
    return null;
  }
}
