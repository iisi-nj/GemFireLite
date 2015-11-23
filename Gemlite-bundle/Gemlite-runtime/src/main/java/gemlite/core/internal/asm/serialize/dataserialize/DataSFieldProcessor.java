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

import gemlite.core.internal.asm.AsmHelper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.VarInsnNode;

public class DataSFieldProcessor implements Opcodes
{
  /***
   * @param owner
   * @param fn
   * @param inst
   */
  private void initToMethodStack(String owner, FieldNode fn, InsnList inst)
  {
    inst.add(new VarInsnNode(ALOAD, 0));
    inst.add(new FieldInsnNode(GETFIELD, owner, fn.name, fn.desc));
    inst.add(new VarInsnNode(ALOAD, 1));
  }
  
  /***
   * mv.visitMethodInsn(INVOKESTATIC, "com/gemstone/gemfire/DataSerializer",
   * "writeLong", "(Ljava/lang/Long;Ljava/io/DataOutput;)V", false);
   * 
   * @param fn
   * @param inst
   * @param dsi
   */
  private void writeValue(FieldNode fn, InsnList inst, DataSItem dsi)
  {
    inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, "com/gemstone/gemfire/DataSerializer", dsi.toMethod, "(" + fn.desc
        + "Ljava/io/DataOutput;)V", false));
  }
  
  /***
   * 
   * @param inst
   * @param dsi
   */
  private void fieldValueIsNull(FieldNode fn, InsnList inst)
  {
    if ("Ljava/lang/Integer;".equals(fn.desc))
    {
      inst.add(new InsnNode(ICONST_0));
      inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(J)Ljava/lang/Integer;", false));
    }
    else if ("Ljava/lang/Long;".equals(fn.desc))
    {
      inst.add(new InsnNode(LCONST_0));
      inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, "java/lang/Lang", "valueOf", "(J)Ljava/lang/Long;", false));
    }
    else if ("Ljava/lang/Double;".equals(fn.desc))
    {
      inst.add(new InsnNode(DCONST_0));
      inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, "java/lang/Double", "valueOf", "(J)Ljava/lang/Double;", false));
    }
    else if("Ljava/lang/Short;".equals(fn.desc))
    {
      inst.add(new InsnNode(DCONST_0));
      inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, "java/lang/Short", "valueOf", "(J)Ljava/lang/Short;", false));
    }
  }
  
  public void toMethod(String owner, FieldNode fn, InsnList inst)
  {
    initToMethodStack(owner, fn, inst);
    
    DataSItem dsi = DataSRegistry.getDataSItem(fn.desc);
    if (!dsi.hasNullMethod)
    {
      writeValue(fn, inst, dsi);
    }
    else
    {
      LabelNode elseLabel = new LabelNode();
      LabelNode endLabel = new LabelNode();
      
      // if 判断
      inst.add(new VarInsnNode(ALOAD, 0));
      inst.add(new FieldInsnNode(GETFIELD, owner, fn.name, fn.desc));
      inst.add(new JumpInsnNode(IFNULL, elseLabel));
      // 非空
      initToMethodStack(owner, fn, inst);
      writeValue(fn, inst, dsi);
      // 非空处理完成，跳转
      inst.add(new JumpInsnNode(GOTO, endLabel));
      // 空值处理
      inst.add(elseLabel);
      fieldValueIsNull(fn, inst);
      inst.add(new VarInsnNode(ALOAD, 1));
      writeValue(fn, inst, dsi);
    }
  }
  
  /***
   * mv.visitVarInsn(ALOAD, 0); mv.visitVarInsn(ALOAD, 1);
   * mv.visitMethodInsn(INVOKESTATIC, "com/gemstone/gemfire/DataSerializer",
   * dsi.fromMethod, "(Ljava/io/DataInput;)" + dsi.fieldDesc, false);
   * mv.visitFieldInsn(PUTFIELD, internalClassName, pd.getName(),
   * dsi.fieldDesc);
   * 
   * @param owner
   * @param fn
   * @param inst
   */
  public void fromMethod(String owner, FieldNode fn, InsnList inst)
  {
    
    inst.add(new VarInsnNode(ALOAD, 0));
    inst.add(new VarInsnNode(ALOAD, 1));
    
    DataSItem dsi = DataSRegistry.getDataSItem(fn.desc);
    inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, "com/gemstone/gemfire/DataSerializer", dsi.fromMethod,
        "(Ljava/io/DataInput;)" + fn.desc, false));
    inst.add(new FieldInsnNode(PUTFIELD,owner, fn.name, fn.desc));
  }
  
}
