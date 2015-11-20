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
package gemlite.core.internal.asm.serialize.dataserialize;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;

public class DataSRegistry
{
  private final static Map<String, DataSItem> TYPE_MAP = new HashMap<>();
  private static boolean fixNull = BooleanUtils.toBoolean(System.getProperty("gemlite.core.asm.serizlise.fix-null", "false"));
  private final static DataSItem register(String fieldDesc, String toMethod, String fromMethod, boolean hasNullMethod)
  {
    DataSItem dsi = new DataSItem( toMethod, fromMethod, hasNullMethod);
    TYPE_MAP.put(fieldDesc, dsi);
    return dsi;
  }
  
  static
  {
    //fieldDesc, toMethod, fromMethod, hasNullMethod
    register("Ljava/lang/String;", "writeString", "readString",false);
    register("I", "writePrimitiveInt", "readPrimitiveInt",false);
    register("Ljava/lang/Integer;", "writeInteger", "readInteger", fixNull);
    register("J", "writePrimitiveLong", "readPrimitiveLong",false);
    register("Ljava/lang/Long;", "writeLong", "readLong",  fixNull);
    register("D", "writePrimitiveDouble", "readPrimitiveDouble",false);
    register("Ljava/lang/Double;", "writeDouble", "readDouble",  fixNull);
    register("Ljava/util/Date;", "writeDate", "readDate",false);
    register("Ljava/lang/Object;", "writeObject", "readObject",false);
    register("Ljava/lang/HashMap;", "writeHashMap", "readHashMap",false);
    
    register("S", "writePrimitiveShort", "readPrimitiveShort", false);
    register("Ljava/lang/Short;", "writeShort", "readShort", fixNull);
    register("Z", "writePrimitiveBoolean", "readPrimitiveBoolean", false);
    register("Ljava/lang/Boolean;", "writeBoolean", "readBoolean", fixNull);
    register("B", "writePrimitiveByte", "readPrimitiveByte", false);
    register("Ljava/lang/Byte;", "writeByte", "readByte", fixNull);
    register("[B", "writeByteArray", "readByteArray", false);
  }
  
  public final static DataSItem getDataSItem(String typeName)
  {
    return TYPE_MAP.get(typeName) == null ? new DataSItem("writeObject", "readObject", false) : TYPE_MAP.get(typeName);
  }
}
