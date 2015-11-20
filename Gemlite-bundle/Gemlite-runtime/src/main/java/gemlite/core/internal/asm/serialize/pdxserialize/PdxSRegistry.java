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
package gemlite.core.internal.asm.serialize.pdxserialize;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;

public class PdxSRegistry
{
  private final static Map<String, PdxSItem> TYPE_MAP = new HashMap<>();
  private static boolean fixNull = BooleanUtils.toBoolean(System.getProperty("gemlite.core.asm.serizlise.fix-null", "false"));
  private final static PdxSItem register(String fieldDesc, String toMethod, String fromMethod, boolean hasNullMethod)
  {
    PdxSItem dsi = new PdxSItem( toMethod, fromMethod, hasNullMethod);
    TYPE_MAP.put(fieldDesc, dsi);
    return dsi;
  }
  
  static
  {
    //fieldDesc, toMethod, fromMethod, hasNullMethod
    register("Ljava/lang/String;", "writeString", "readString",false);
    register("I", "writeInt", "readInt",false);
    register("Ljava/lang/Integer;", "writeInteger", "readInteger", fixNull);
    register("J", "writeLong", "readLong",false);
    register("Ljava/lang/Long;", "writeLong", "readLong",  fixNull);
    register("D", "writeDouble", "readDouble",false);
    register("Ljava/lang/Double;", "writeDouble", "readDouble",  fixNull);
    register("Ljava/util/Date;", "writeDate", "readDate",false);
    register("Ljava/lang/Object;", "writeObject", "readObject",false);
    
    register("S", "writeShort", "readShort", false);
    register("Ljava/lang/Short;", "writeShort", "readShort", fixNull);
    register("Z", "writeBoolean", "readBoolean", false);
    register("Ljava/lang/Boolean;", "writeBoolean", "readBoolean", fixNull);
    register("B", "writeByte", "readByte", false);
    register("Ljava/lang/Byte;", "writeByte", "readByte", fixNull);
    register("[B", "writeByteArray", "readByteArray", false);
    register("F", "writeFloat", "readFloat", false);
    register("C", "writeChar", "readChar", false);
  }
  
  public final static PdxSItem getDataSItem(String typeName)
  {
    return TYPE_MAP.get(typeName) == null ? new PdxSItem("writeObject", "readObject", false) : TYPE_MAP.get(typeName);
  }
}
