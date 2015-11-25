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
package gemlite.maven.plugin;

import gemlite.core.annotations.domain.AutoSerialize;
import gemlite.maven.plugin.support.AnnotationProcessorFactory;
import gemlite.maven.plugin.support.ClassProcessor;
import gemlite.maven.plugin.support.DomainMojoConstant;
import gemlite.maven.plugin.support.DomainMojoHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.util.StringUtils;

import aj.org.objectweb.asm.Type;

public class DomainMojoExecutor implements Opcodes
{
  private String autoSerializeType; // default value : ds
  
  public DomainMojoExecutor()
  {
    
  }
  
  public DomainMojoExecutor(String autoSerializeType)
  {
    this.autoSerializeType = autoSerializeType;
  }
  
  protected final static Set<ClassProcessor> usedProcessorSet = new HashSet<>();
  
  public static void main(String[] args) throws Exception
  {
    DomainClassFileVistor fv = new DomainClassFileVistor("pdx");
    fv.processClassFile(Paths
        .get("D:/springsource/gemlite_prod/ynd.test.domain/target/classes/ynd/test/", "Ac01.class"));
    
    DomainMojoExecutor ex = new DomainMojoExecutor();
    ex.execute("D:/springsource/gemlite_prod/ynd.test.domain/target/classes/", "proj1");
  }
  
  public boolean execute(String outputDir, String projectName)
  {
    try
    {
      scanClassFiles(outputDir);
      for (ClassProcessor cp : usedProcessorSet)
      {
        cp.endProcess();
      }
      return true;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  private void scanClassFiles(final String outputDir) throws IOException
  {
    long l1 = System.currentTimeMillis();
    Path path = Paths.get(outputDir);
    DomainClassFileVistor fv = new DomainClassFileVistor(autoSerializeType);
    Files.walkFileTree(path, fv);
    long l2 = System.currentTimeMillis();
    DomainMojoHelper.log().info(
        "Domain class processed,files:" + fv.fileCount + " classes:" + fv.classCount + " cost:" + (l2 - l1));
  }
}

class PreClassInstrument extends SimpleFileVisitor<Path>
{
  public int fileCount = 0;
  public int classCount = 0;
  public List<File> classFiles = new ArrayList<>();
  
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
  {
    String fileName = file.toFile().getAbsolutePath();
    fileCount++;
    if (fileName.endsWith(".class"))
    {
      classCount++;
      classFiles.add(file.toFile());
    }
    return super.visitFile(file, attrs);
  }
  
}

class DomainClassFileVistor extends SimpleFileVisitor<Path>
{
  private String autoSerializeType; // default value : pdx
  
  public int fileCount = 0;
  public int classCount = 0;
  
  public DomainClassFileVistor(String autoSerializeType)
  {
    this.autoSerializeType = autoSerializeType;
  }
  
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
  {
    
    String fileName = file.toFile().getAbsolutePath();
    fileCount++;
    if (fileName.endsWith(".class"))
    {
      classCount++;
      if (DomainMojoHelper.log().isDebugEnabled())
        DomainMojoHelper.log().debug("Found class file :" + fileName);
      processClassFile(file);
    }
    return super.visitFile(file, attrs);
  }
  
  private ClassProcessor getClassProcessor(String autoSerializeType)
  {
    if (StringUtils.isEmpty(autoSerializeType))
      return null;
    
    if (AutoSerialize.Type.DS.name().equals(autoSerializeType))
    {
      return AnnotationProcessorFactory.getProcessor(DomainMojoConstant.AN_AutoSerialize_Ds);
    }
    else if (AutoSerialize.Type.PDX.name().equals(autoSerializeType))
    {
      return AnnotationProcessorFactory.getProcessor(DomainMojoConstant.AN_AutoSerialize_Pdx);
    }
    else
      return null;
  }
  
  @SuppressWarnings({ "unchecked", "null" })
  private String getAutoSerializeType(AnnotationNode an)
  {
    String serialType = autoSerializeType;
    
    if (an == null)
      return serialType;
    
    List<Object> valueList = an.values;
    if (valueList == null || valueList.size() <= 1)
    {
      return serialType;
    }
    else
    {
      try
      {
        String[] types = (String[]) valueList.get(1);
        if (types != null || types.length > 1)
        {
          serialType = types[1];
        }
      }
      catch (Exception e)
      {
        DomainMojoHelper.log().error("Get AutoSerialize type failed.");
        return serialType;
      }
    }
    return serialType;
  }
  
  public void processClassFile(Path file) throws IOException
  {
    String autoSerialType = autoSerializeType;
    
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.toFile()));
    byte[] bytes = new byte[in.available()];
    in.read(bytes);
    in.close();
    ClassReader cr = new ClassReader(bytes);
    ClassNode cn = new ClassNode();
    cr.accept(cn, 0);
    Set<ClassProcessor> used = new HashSet<>();
    if (DomainMojoHelper.log().isDebugEnabled())
      DomainMojoHelper.log().debug("Class name:" + cn.name + " Annotation:" + cn.visibleAnnotations);
    
    if (cn.visibleAnnotations == null)
      return;
    
    String desc = "";
    for (Object a : cn.visibleAnnotations)
    {
      AnnotationNode an = (AnnotationNode) a;
      
      desc = an.desc;
      ClassProcessor cp = null;
      if (DomainMojoConstant.AN_AutoSerialize.equals(desc))
      {
        autoSerialType = getAutoSerializeType(an);
        cp = getClassProcessor(autoSerialType);
      }
      else
        cp = AnnotationProcessorFactory.getProcessor(desc);
      
      if (cp == null)
        continue;
      DomainMojoExecutor.usedProcessorSet.add(cp);
      if (DomainMojoHelper.log().isDebugEnabled())
        DomainMojoHelper.log().debug("Annotation:" + desc + " processor:" + cp);
      // 未处理过
      if (cp != null && !used.contains(cp))
      {
        if (DomainMojoHelper.log().isDebugEnabled())
          DomainMojoHelper.log().debug("Processor " + cp + " processing...");
        used.add(cp);
        long l1 = System.currentTimeMillis();
        cp.process(file.toFile(), bytes, cn);
        long l2 = System.currentTimeMillis();
        if (DomainMojoHelper.log().isDebugEnabled())
          DomainMojoHelper.log().debug("Processor " + cp + " processed, cost:" + (l2 - l1));
      }
    }
  }
}
