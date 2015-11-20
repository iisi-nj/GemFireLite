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
import gemlite.core.internal.asm.AsmHelper;
import gemlite.core.internal.asm.serialize.dataserialize.DataSItem;
import gemlite.core.internal.asm.serialize.dataserialize.DataSRegistry;
import gemlite.maven.plugin.support.ByteArrayClassLoader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.CheckClassAdapter;

import com.gemstone.gemfire.DataSerializable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Test1 implements Opcodes
{
  public final static String DATAS_NAME = "com/gemstone/gemfire/DataSerializable";
  
  public static void main(String[] args) throws Exception
  {
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(
        "D:/springsource/gemlite_prod/ynd.test.domain/target/classes/ynd/test/Ac01.class"));
    byte[] bytes = new byte[in.available()];
    in.read(bytes);
    in.close();
    
    ClassReader cr = new ClassReader(bytes);
    ClassNode cn = new ClassNode();
    cr.accept(cn, 0);
    
    cn.interfaces.add(DATAS_NAME);
    implementInterface(cn);
  }
  
  private static void implementInterface(ClassNode cn) throws Exception
  {
    
    // MethodNode fromMethod = new MethodNode(ACC_PUBLIC, "fromData",
    // "(Ljava/io/DataInput;)V", null, new String[] {
    // "java/io/IOException",
    // "java/lang/ClassNotFoundException" });
    // mv = cw.visitMethod(ACC_PUBLIC, "toData", "(Ljava/io/DataOutput;)V",
    // null, new String[] { "java/io/IOException" });
    MethodNode toMethod = new MethodNode(ACC_PUBLIC, "toData", "(Ljava/io/DataOutput;)V", null,
        new String[] { "java/io/IOException" });
    InsnList instToMethod = toMethod.instructions;
    for (int i = 0; i < cn.fields.size(); i++)
    {
      FieldNode fn = (FieldNode) cn.fields.get(i);
      toMethod(cn.name, fn, instToMethod);
    }
    instToMethod.add(new InsnNode(RETURN));
    cn.methods.add(toMethod);
    
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    cn.accept(cw);
    byte[] bt = cw.toByteArray();
    
    ClassReader cr = new ClassReader(bt);
    CheckClassAdapter ca = new CheckClassAdapter(cw);
    FileOutputStream fo = new FileOutputStream("d:/x1.log");
    PrintWriter pw = new PrintWriter(fo);
    ca.verify(cr, true, pw);
    
    ByteArrayClassLoader bacl = new ByteArrayClassLoader(bt);
    
    Class cls = bacl.loadClass("ynd.test.Ac01");
    DataSerializable di = (DataSerializable) cls.newInstance();
    di.toData(null);
  }
  
  public static void toMethod(String owner, FieldNode fn, InsnList inst)
  {
    DataSItem dsi = DataSRegistry.getDataSItem(fn.desc);
    inst.add(new VarInsnNode(ALOAD, 0));
    inst.add(new FieldInsnNode(GETFIELD, owner, fn.name, fn.desc));
    inst.add(new VarInsnNode(ALOAD, 1));
    inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, "com/gemstone/gemfire/DataSerializer", dsi.toMethod, "(" + fn.desc
        + "Ljava/io/DataOutput;)V", false));
  }
}
