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
package gemlite.core.internal.asm.serialize;

import gemlite.core.internal.asm.serialize.pdxserialize.PdxConstants;
import gemlite.core.internal.asm.serialize.pdxserialize.PdxSFieldProcessor;

import java.io.IOException;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PdxSerializeHelper implements Opcodes
{
  public final static String DATAS_NAME = PdxConstants.PDX_SERIALIZABLE;
  private PdxSFieldProcessor fp = new PdxSFieldProcessor();
  
  private final static PdxSerializeHelper instance = new PdxSerializeHelper();
  private PdxSerializeHelper()
  {
    
  }
  public final static PdxSerializeHelper getInstance()
  {
    return instance;
  }
  
  public boolean process(ClassNode cn) throws IOException
  {
    boolean result =  addPdxSerializeInterface(cn);
    return result;
  }
  
  
  /***
   * @param cn
   * @return true:add a interface
   */
  
  private boolean addPdxSerializeInterface(ClassNode cn)
  {
    List interfaces = cn.interfaces;
    for (int i = 0; i < interfaces.size(); i++)
    {
      String interfaceName = (String) interfaces.get(i);
      if (DATAS_NAME.equals(interfaceName))
      {
        return false;
      }
    }
    interfaces.add(DATAS_NAME);
    
    implementInterface(cn);
    
    return true;
  }
  
  private void implementInterface(ClassNode cn)
  {
    MethodNode toMethod = new MethodNode(ACC_PUBLIC, PdxConstants.PDX_TODATA, PdxConstants.PDX_WRITER_PARAM, null,
        new String[] {});
    InsnList instToMethod = toMethod.instructions;
    
    MethodNode fromMethod = new MethodNode(ACC_PUBLIC, PdxConstants.PDX_FROMDATA, PdxConstants.PDX_READER_PARAM, null, 
        new String[] {});
    InsnList instFromMethod = fromMethod.instructions;
    for (int i = 0; i < cn.fields.size(); i++)
    {
      FieldNode fn = (FieldNode) cn.fields.get(i);
      
      fp.toMethod(cn.name, fn, instToMethod);
      fp.fromMethod(cn.name, fn, instFromMethod);
    }
    
    //markIdentityFields
    fp.markIdentityFields(cn, instToMethod);
    
    instToMethod.add(new InsnNode(RETURN));
    cn.methods.add(toMethod);
    
    instFromMethod.add(new InsnNode(RETURN));
    cn.methods.add(fromMethod);
    
//    if (DomainMojoHelper.log().isDebugEnabled())
//      DomainMojoHelper.log().debug(cn.name + " add toData and fromData method.");
  }
  
  public void endProcess()
  {
    
  }
  
}
