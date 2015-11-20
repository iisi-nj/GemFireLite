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
package gemlite.maven.plugin.support.mapper;

import java.util.HashMap;
import java.util.Map;

public class MapperToolRegistry
{
  static class Item1
  {
    String desc;
    String getMethod;
  }
  
  private final static Map<String, Item1> TYPE_MAP = new HashMap<>();
  
  private final static Item1 register1(String simpleDesc, String fullDesc, String getMethod)
  {
    Item1 item = new Item1();
    item.desc = simpleDesc;
    item.getMethod = getMethod;
    TYPE_MAP.put(simpleDesc, item);
    if (fullDesc != null)
    {
      TYPE_MAP.put(fullDesc, item);
    }
    return item;
  }
  
  static
  {
    register1("I", "Ljava/lang/Integer;", "getInt");
    register1("J", "Ljava/lang/Long;", "getLong");
    register1("D", "Ljava/lang/Double;", "getDouble");
    register1("F", "Ljava/lang/Float;", "getFloat");
    register1("Ljava/util/Date;", null, "getDate");
    register1("Ljava/lang/String;", "", "getString");
    
    register1("S", "Ljava/lang/Short;", "getShort");
    register1("Z", "Ljava/lang/Boolean;", "getBoolean");
    register1("B", "Ljava/lang/Byte;", "getByte");
    register1("Ljava/math/BigDecimal;", null, "getBigDecimal");
    register1("Ljava/util/UUID;", null, "getUUID");
    register1("[B", null, "getByteArray");
  }
  
  public final static Item1 getDataSItem(String desc)
  {
    return TYPE_MAP.get(desc) == null ? TYPE_MAP.get("Ljava/lang/String;") : TYPE_MAP.get(desc);
  }
}
