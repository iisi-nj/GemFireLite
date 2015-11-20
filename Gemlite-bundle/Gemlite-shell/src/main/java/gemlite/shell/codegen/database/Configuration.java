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

import java.io.Serializable;
import java.util.Map;
import gemlite.shell.codegen.conf.Settings;

public interface Configuration extends Serializable
{
  // -------------------------------------------------------------------------
  // Custom data
  // -------------------------------------------------------------------------
  Map<Object, Object> data();
  
  Object data(Object key);
  
  Object data(Object key, Object value);
  
  // -------------------------------------------------------------------------
  // Getters
  // -------------------------------------------------------------------------
  SQLDialect dialect();
  
  SQLDialect family();
  
  Settings settings();
  
  // -------------------------------------------------------------------------
  // Setters
  // -------------------------------------------------------------------------
  Configuration set(SQLDialect newDialect);
  
  Configuration set(Settings newSettings);
  
  // -------------------------------------------------------------------------
  // Derivation methods
  // -------------------------------------------------------------------------
  Configuration derive();
  
  Configuration derive(SQLDialect newDialect);
  
  Configuration derive(Settings newSettings);
}
