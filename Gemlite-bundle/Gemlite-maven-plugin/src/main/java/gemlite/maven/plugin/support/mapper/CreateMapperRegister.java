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

import gemlite.core.internal.asm.AsmHelper;
import gemlite.core.internal.support.context.DomainMapperHelper;
import gemlite.core.internal.support.context.IDomainRegistry;
import gemlite.maven.plugin.support.DomainMojoConstant;
import gemlite.maven.plugin.support.DomainMojoHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

@SuppressWarnings("unchecked")
public class CreateMapperRegister implements Opcodes,  DomainMojoConstant
{
  public final static String INTERFACE_NAME = IDomainRegistry.class.getName().replaceAll("\\.","\\/") ;
  private final static Pattern classNameCharacterPattern = Pattern.compile("[^0-9a-zA-Z_]"); 
  private ClassNode reg;
  private byte[] fileBytes;
  private InsnList insn;
  private String projectName = System.getProperty("current-project-name");
  private String projectPath = System.getProperty("current-project-path");
  private String fileName;
  
  public static void main(String[] args)
  {
    CreateMapperRegister r = new CreateMapperRegister();
    String n = "Sample-domain";
    n = r.filterProjectName(n);
    System.out.println(n);
  }
  public String filterProjectName(String str) 
  {
    Matcher mat = classNameCharacterPattern.matcher(str);
    return mat.replaceAll("_").trim();

  }
  /***
   * @param domain
   * @return
   * @throws IOException
   */
  
  public synchronized void getIDomainRegistryClass( ClassNode domain) throws IOException
  {
    if (reg == null)
    {
      projectName = filterProjectName(projectName);
      String packagePath = DomainMapperHelper.defaultMpperPackage.replaceAll("\\.", "\\/");
      String internalName =packagePath + projectName + "$$$IDomainRegisterImpl";
      String path = projectPath+"/"+packagePath+"/";
      File f = new File(path);
      if( !f.exists())
        f.mkdirs();
      fileName = path+"/"+projectName+"$$$IDomainRegisterImpl.class";
      findTableNameAndRegionName(domain);
      reg = new ClassNode();
      reg.version = V1_7;
      reg.access = ACC_PUBLIC + ACC_SUPER;
      reg.name =internalName;
      reg.superName = "java/lang/Object";
      reg.interfaces.add(INTERFACE_NAME);
      reg.signature = null;
      addConstructFunction(reg);
      insn = implementInterface(reg);
    }
  }
  
  
  private String[] findTableNameAndRegionName(ClassNode domain)
  {
    String[] names= new String[2];
    for (Object o : domain.visibleAnnotations)
    {
      AnnotationNode an = (AnnotationNode) o;
      if (DomainMojoHelper.log().isDebugEnabled())
        DomainMojoHelper.log().debug("Annotation:" + an.desc + " value:" + an.values);
      if (AN_Region.equals(an.desc))
      {
        names[0] = (String)an.values.get(1);
      }
      else if (AN_Table.equals(an.desc))
      {
        names[1] =  (String)an.values.get(1);
      }
    }
    return names;
    
  }
  
  public void addRegistryItem( ClassNode domain , String mapperClassName)
  {
    String[] names=findTableNameAndRegionName(domain);
    String regionName = names[0];
    String tableName = names[1];
    insn.add(new LdcInsnNode(tableName));
    insn.add(new LdcInsnNode(regionName));
    insn.add(new LdcInsnNode(mapperClassName));
    insn.add(AsmHelper.newMethodInsnNode(INVOKESTATIC, "gemlite/core/internal/domain/DomainRegistry", "registerDomain",
        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false));
  }
  
  
  public void endProcess()
  {
    insn.add(new InsnNode(RETURN));
    try
    {
      writeClassFile();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private void writeClassFile() throws IOException
  {
    if (DomainMojoHelper.log().isDebugEnabled())
      DomainMojoHelper.log().debug("Create mapper registry class:"+reg.name);
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    reg.accept(cw);
    fileBytes = cw.toByteArray();
    File regFile = new File(fileName);
    regFile.delete();
    BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(regFile));
    bo.write(fileBytes);
    bo.close();
    if (DomainMojoHelper.log().isDebugEnabled())
      DomainMojoHelper.log().debug(reg.name + " write file done.");
  }
  
  private void addConstructFunction(ClassNode reg)
  {
    MethodNode mn = new MethodNode(ACC_PUBLIC, "<init>", "()V", null, null);
    insn = mn.instructions;
    insn.add(new VarInsnNode(ALOAD, 0));
    insn.add(AsmHelper.newMethodInsnNode(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false));
    insn.add(new InsnNode(RETURN));
    reg.methods.add(mn);
  }
  
  private InsnList implementInterface(ClassNode reg) throws IOException
  {
    if (DomainMojoHelper.log().isDebugEnabled())
      DomainMojoHelper.log().debug("Add doRegistry()V method...");
    MethodNode mn = new MethodNode(ACC_PUBLIC, "doRegistry", "()V", null, null);
    InsnList insn = mn.instructions;
    reg.methods.add(mn);
    return insn;
  }
  public ClassNode getReg()
  {
    return reg;
  }
  public byte[] getFileBytes()
  {
    return fileBytes;
  }
  
}
