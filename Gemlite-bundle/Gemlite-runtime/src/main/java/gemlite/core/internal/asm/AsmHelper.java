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
package gemlite.core.internal.asm;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;

public class AsmHelper implements Opcodes
{
  private final static Set<String> fullSimpleTypes;
  static
  {
    fullSimpleTypes = new HashSet<>();
    fullSimpleTypes.add("Ljava/lang/Integer;");
    fullSimpleTypes.add("Ljava/lang/Long;");
    fullSimpleTypes.add("Ljava/lang/Double;");
    fullSimpleTypes.add("Ljava/lang/Float;");
    fullSimpleTypes.add("Ljava/lang/Short;");
    fullSimpleTypes.add("Ljava/lang/Boolean;");
    fullSimpleTypes.add("Ljava/lang/Byte;");
  }
  
  public final static boolean needTypeConvert(String fromDesc, String toDesc)
  {
    return fullSimpleTypes.contains(toDesc) && !fromDesc.equals(toDesc);
  }
  
  public final static MethodInsnNode newMethodInsnNode(final int opcode, final String owner, final String name,
      final String desc, boolean isInterface)
  {
    //return new MethodInsnNode(opcode, owner, name, desc);
    return new MethodInsnNode(opcode, owner, name, desc, isInterface);
  }
  
  public final static int getReturnCodeByType(String typeDesc)
  {
    switch (typeDesc)
    {
      case "I":
        return Opcodes.IRETURN;
      case "J":
        return Opcodes.LRETURN;
      case "D":
        return Opcodes.DRETURN;
      case "F":
        return Opcodes.FRETURN;
    }
    
    return Opcodes.ARETURN;
  }
  
  public static void main(String[] args)
  {
    String s = AsmHelper.toViewName("LStkd;");
    System.out.println(s);
  }
  
  public final static String toViewName(String typeDesc)
  {
    switch (typeDesc)
    {
      case "I":
        return "Integer";
      case "J":
        return "Long";
      case "D":
        return "Double";
      case "F":
        return "Float";
      case "S":
        return "Short";
      case "Z":
        return "Boolean";
      case "B":
        return "Byte";
      case "V":
        return "void";
      default:
          typeDesc = typeDesc.substring(1, typeDesc.length() - 1).replace('/', '.');
    }
    return typeDesc;
  }
  
  public final static String toFullName(String typeDesc)
  {
    switch (typeDesc)
    {
      case "I":
        return "Ljava/lang/Integer;";
      case "J":
        return "Ljava/lang/Long;";
      case "D":
        return "Ljava/lang/Double;";
      case "F":
        return "Ljava/lang/Float;";
      case "S":
        return "Ljava/lang/Short";
      case "Z":
        return "Ljava/lang/Boolean";
      case "B":
        return "Ljava/lang/Byte";
    }
    return typeDesc;
  }
  
  public final static void addTypeConvert(InsnList insn, String typeDesc)
  {
    switch (typeDesc)
    {
      case "I":
        insn.add(newMethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
        break;
      case "J":
        insn.add(newMethodInsnNode(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false));
        break;
      case "D":
        insn.add(newMethodInsnNode(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false));
        break;
      case "F":
        insn.add(newMethodInsnNode(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false));
        break;
      case "S":
        insn.add(newMethodInsnNode(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false));
        break;
      case "Z":
        insn.add(newMethodInsnNode(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false));
        break;
      case "B":
        insn.add(newMethodInsnNode(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false));
        break;
    }
  }
}
