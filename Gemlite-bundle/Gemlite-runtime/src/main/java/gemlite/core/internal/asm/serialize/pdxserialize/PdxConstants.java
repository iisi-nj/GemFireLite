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

public class PdxConstants
{
  
  public static final String PDX_SERIALIZABLE = "com/gemstone/gemfire/pdx/PdxSerializable";
  
  public static final String PDX_TODATA = "toData";
  public static final String PDX_FROMDATA = "fromData";
  public static final String VALUE_OF = "valueOf";
  
  public static final String PDX_WRITER_PARAM = "(Lcom/gemstone/gemfire/pdx/PdxWriter;)V";
  public static final String PDX_READER_PARAM = "(Lcom/gemstone/gemfire/pdx/PdxReader;)V";
  public static final String PDX_READER_VALUE = "com/gemstone/gemfire/pdx/PdxReader";
  public static final String PDX_WRITER_VALUE = "com/gemstone/gemfire/pdx/PdxWriter";
  
  public static final String TYPE_BYTECODE_INTEGER = "java/lang/Integer";
  public static final String TYPE_BYTECODE_INTEGER_L = "Ljava/lang/Integer;";
  public static final String TYPE_BYTECODE_INTEGER_J = "(J)Ljava/lang/Integer;";
  
  public static final String TYPE_BYTECODE_LONG = "java/lang/Long";
  public static final String TYPE_BYTECODE_LONG_L = "Ljava/lang/Long;";
  public static final String TYPE_BYTECODE_LONG_J = "(J)Ljava/lang/Long;";
  
  public static final String TYPE_BYTECODE_DOUBLE = "java/lang/Double";
  public static final String TYPE_BYTECODE_DOUBLE_L = "Ljava/lang/Double;";
  public static final String TYPE_BYTECODE_DOUBLE_J = "(J)Ljava/lang/Double;";
  
  public static final String TYPE_BYTECODE_SHORT = "java/lang/Short";
  public static final String TYPE_BYTECODE_SHORT_L = "Ljava/lang/Short;";
  public static final String TYPE_BYTECODE_SHORT_J = "(J)Ljava/lang/Short;";  
  
  public static final String TYPE_BYTECODE_BYTE_B = "B";//byte
  public static final String TYPE_BYTECODE_BYTE = "java/lang/Byte";
  public static final String TYPE_BYTECODE_BOOL_Z = "Z";//boolean
  public static final String TYPE_BYTECODE_BOOL = "java/lang/Boolean";
  
  public final static String AN_Key = "Lgemlite/core/annotations/domain/Key;";
}
