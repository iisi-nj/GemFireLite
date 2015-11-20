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

import gemlite.core.internal.asm.AsmHelper;
import gemlite.core.internal.asm.serialize.PdxSerializeHelper;
import gemlite.core.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class PdxSFieldProcessor implements Opcodes
{
  
  /***
   * 
   * 
   * @param fn
   * @param inst
   * @param dsi
   */
  private void writeValue(String owner, FieldNode fn, InsnList inst, PdxSItem dsi)
  {
    String desc = new StringBuffer("(Ljava/lang/String;").append(fn.desc)
        .append(")Lcom/gemstone/gemfire/pdx/PdxWriter;").toString();
    
    inst.add(new VarInsnNode(ALOAD, 1));
    inst.add(new LdcInsnNode(fn.name));
    inst.add(new VarInsnNode(ALOAD, 0));
    inst.add(new FieldInsnNode(GETFIELD, owner, fn.name, fn.desc));
    
    // add INVOKEVIRTUAL method
    if (PdxConstants.TYPE_BYTECODE_BYTE_B.equals(fn.desc)) // data type ->byte
      inst.add(AsmHelper.newMethodInsnNode(INVOKEVIRTUAL, PdxConstants.TYPE_BYTECODE_BYTE, "byteValue", fn.desc, false));
    else if (PdxConstants.TYPE_BYTECODE_BOOL_Z.equals(fn.desc)) // data type ->
                                                                // boolean
      inst.add(AsmHelper.newMethodInsnNode(INVOKEVIRTUAL, PdxConstants.TYPE_BYTECODE_BOOL, "booleanValue", fn.desc,
          false));
    
    inst.add(AsmHelper.newMethodInsnNode(INVOKEINTERFACE, PdxConstants.PDX_WRITER_VALUE, dsi.toMethod, desc, true));
    inst.add(new InsnNode(POP));
  }
  
  public void toMethod(String owner, FieldNode fn, InsnList inst)
  {
    PdxSItem dsi = PdxSRegistry.getDataSItem(fn.desc);
    
    if (dsi != null)
      writeValue(owner, fn, inst, dsi);
  }
  
  /***
   * 
   * 
   * @param owner
   * @param fn
   * @param inst
   */
  public void fromMethod(String owner, FieldNode fn, InsnList inst)
  {
    PdxSItem dsi = PdxSRegistry.getDataSItem(fn.desc);
    if (dsi == null)
    {
      LogUtil.getAppLog().error("Can't get dataSItem {}.", fn.desc);
      return;
    }
    
    String desc = "(Ljava/lang/String;)" + fn.desc;
    
    inst.add(new VarInsnNode(ALOAD, 0));
    inst.add(new VarInsnNode(ALOAD, 1));
    inst.add(new LdcInsnNode(fn.name));
    
    inst.add(AsmHelper.newMethodInsnNode(INVOKEINTERFACE, PdxConstants.PDX_READER_VALUE, dsi.fromMethod, desc, true));
    
    // add INVOKEVIRTUAL method
    if (PdxConstants.TYPE_BYTECODE_BYTE_B.equals(fn.desc)) // data type ->byte
      inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, PdxConstants.TYPE_BYTECODE_BYTE, PdxConstants.VALUE_OF, "("
          + fn.desc + ")Ljava/lang/Byte;", false));
    else if (PdxConstants.TYPE_BYTECODE_BOOL_Z.equals(fn.desc)) // data type ->
                                                                // boolean
      inst.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, PdxConstants.TYPE_BYTECODE_BOOL, PdxConstants.VALUE_OF, "("
          + fn.desc + ")Ljava/lang/Boolean;", false));
    
    inst.add(new FieldInsnNode(PUTFIELD, owner, fn.name, fn.desc));
  }
  
  private boolean isKeyField(FieldNode fn)
  {
    if (fn.visibleAnnotations != null)
    {
      for (Object o : fn.visibleAnnotations)
      {
        AnnotationNode an = (AnnotationNode) o;
        if (PdxConstants.AN_Key.equals(an.desc))
        {
          return true;
        }
      }
    }
    return false;
  }
  
  private List<FieldNode> findKeyNodeListByDomain(ClassNode domain)
  {
    List<FieldNode> fieldNodeList = new ArrayList<FieldNode>();
    
    if (domain != null)
    {
      for (int i = 0; i < domain.fields.size(); i++)
      {
        FieldNode fn = (FieldNode) domain.fields.get(i);
        if (isKeyField(fn))
        {
          fieldNodeList.add(fn);
        }
      }
      
      // if can't get the key field, to get all fields in this class
      if (fieldNodeList.size() == 0)
      {
        for (int i = 0; i < domain.fields.size(); i++)
        {
          FieldNode fn = (FieldNode) domain.fields.get(i);
          if (fn != null)
            fieldNodeList.add(fn);
        }
      }
    }
    
    return fieldNodeList;
  }
  
  /**
   * aload_1
   * ldc "product_id"
   * invokeinterface
   * com/gemstone/gemfire/pdx/PdxWriter/markIdentityField(Ljava/lang
   * /String;)Lcom/gemstone/gemfire/pdx/PdxWriter;
   * pop
   * 
   * 
   * @param cn
   * @param inst
   */
  public void markIdentityFields(ClassNode cn, InsnList inst)
  {
    List<FieldNode> fieldNodeList = null;
    try
    {
      fieldNodeList = findKeyNodeListByKeyClass(cn);
    }
    catch (IOException e)
    {
      if (LogUtil.getCoreLog() != null)
        LogUtil.getCoreLog().error("Domain " + cn.name + " find keyclass failed.", e);
    }
    
    if (fieldNodeList == null || fieldNodeList.size() == 0)
    {
      fieldNodeList = findKeyNodeListByDomain(cn);
    }
    
    if (fieldNodeList == null || fieldNodeList.size() == 0)
    {
      if (LogUtil.getCoreLog() != null)
        LogUtil.getCoreLog().info("Domain " + cn.name + " can't find key field.");
      return;
    }
    
    for (FieldNode fn : fieldNodeList)
    {
      inst.add(new VarInsnNode(ALOAD, 1));
      inst.add(new LdcInsnNode(fn.name));
      inst.add(AsmHelper.newMethodInsnNode(INVOKEINTERFACE, PdxConstants.PDX_WRITER_VALUE, "markIdentityField",
          "(Ljava/lang/String;)Lcom/gemstone/gemfire/pdx/PdxWriter;", true));
      inst.add(new InsnNode(POP));
    }
  }
  
  private List<FieldNode> findKeyNodeListByKeyClass(ClassNode domain) throws IOException
  {
    List<FieldNode> fieldNodeList = new ArrayList<FieldNode>();
    
    ClassNode key = null;
    
    for (Object o : domain.visibleAnnotations)
    {
      AnnotationNode an = (AnnotationNode) o;
      
      if (PdxConstants.AN_Key.equals(an.desc))
      {
        Type t = (Type) an.values.get(1);
        InputStream keyClassIn = PdxSerializeHelper.class.getClassLoader().getResourceAsStream(
            t.getInternalName() + ".class");
        byte[] keyClassBytes = new byte[keyClassIn.available()];
        keyClassIn.read(keyClassBytes);
        keyClassIn.close();
        ClassReader cr = new ClassReader(keyClassBytes);
        key = new ClassNode();
        cr.accept(key, 0);
        break;
      }
    }
    
    if (key != null)
    {
      for (int i = 0; i < key.fields.size(); i++)
      {
        FieldNode fn = (FieldNode) key.fields.get(i);
        if (fn != null)
          fieldNodeList.add(fn);
      }
    }
    return fieldNodeList;
  }
  
}
