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
import gemlite.core.internal.domain.DomainUtil;
import gemlite.core.internal.domain.IDataSource;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.util.GemliteHelper;
import gemlite.maven.plugin.support.ByteArrayClassLoader;
import gemlite.maven.plugin.support.DomainMojoHelper;
import gemlite.maven.plugin.support.ProcessResult;
import gemlite.maven.plugin.support.mapper.MapperToolProcessor;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class TestMapper
{
  
  @SuppressWarnings("rawtypes")
  public final static Map<String, Object> checkInstrument(String classpath, String className)
  {
    Map<String, Object> map = new HashMap<>();
    try
    {
      System.setProperty("current-project-name", "checkInstrument");
      System.setProperty("current-project-path", classpath);
      String targetDir = classpath;
      
      File f = new File(classpath);
      URLClassLoader ucl = new URLClassLoader(new URL[] { f.toURI().toURL() }, Thread.currentThread()
          .getContextClassLoader());
      
      String classFileName = className.replace('.', '/') + ".class";
      // set Domain class file
      File file = new File(targetDir + "/" + classFileName);
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
      byte[] bytes = new byte[in.available()];
      in.read(bytes);
      in.close();
      
      // --------------------Mapper start-------------------------------
      ClassReader cr = new ClassReader(bytes);
      ClassNode cn = new ClassNode();
      cr.accept(cn, 0);
      DomainMojoHelper.setLoader(ucl);
      MapperToolProcessor proc = new MapperToolProcessor();
      ProcessResult result = proc.process(file, bytes, cn);
      proc.getMapperRegister().endProcess();
      
      // mapper check
      byte[] mapperClassBytes = result.newBytes;
      ClassNode mapper = result.node;
      String mapperClassName = mapper.name.replaceAll("\\/", "\\.");
      
      System.out.println("------------ Dump bytecode ------------");
      
      ByteArrayOutputStream bo = new ByteArrayOutputStream();
      PrintWriter pw = new PrintWriter(bo);
      GemliteHelper.dumpBytecode(mapper, pw);
      pw.flush();
      byte[] dumpBytes = bo.toByteArray();
      map.put("dumpBytes", new String(dumpBytes));
      bo.reset();
      
      System.out.println("------------ Dump bytecode end ------------");
      GemliteHelper.checkAsmBytes(mapperClassBytes, pw);
      byte[] verifyBytes = bo.toByteArray();
      map.put("verifyBytes", new String(verifyBytes));
      // Test gen mapper class
      ByteArrayClassLoader ba = new ByteArrayClassLoader(mapperClassBytes, ucl);
      Class cls = ba.loadClass(mapperClassName);
      map.put("cls", cls);
      System.out.println("Class -> " + cls);
      
       // Test gen reg class
       ClassNode reg = proc.getMapperRegister().getReg();
       String regClassName = reg.name.replaceAll("\\/", "\\.");
       bo.reset();
       GemliteHelper.dumpBytecode(reg, pw);
       byte[] regDump = bo.toByteArray();
       map.put("regDump", new String(regDump));
       bo.reset();
       GemliteHelper.checkAsmBytes(proc.getMapperRegister().getFileBytes(), pw);
       byte[] regVerify = bo.toByteArray();
       map.put("regVerify", new String(regVerify));
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return map;
    
  }
  
  @SuppressWarnings("rawtypes")
  public static void main(String[] args) throws Exception
  {
    Map<String, Object> check = checkInstrument(
        "D:/Code/vmgemlite/vmsample/Sample-bundle/Sample-domain/target/classes", "gemlite.sample.domain.Customer");
    String dumpBytes = check.get("dumpBytes").toString();
    String verifyBytes = check.get("verifyBytes").toString();
    Class cls = (Class) check.get("cls");
    String regDump = check.get("regDump").toString();
    String regVerify = check.get("regVerify").toString();
    
    RandomAccessFile ra = new RandomAccessFile("d:/checkMapper.log", "rw");
    ra.writeBytes("\n----------------Dump------------------");
    ra.writeBytes(dumpBytes);
   
    ra.writeBytes("\n----------------Verify------------------");
    ra.writeBytes(verifyBytes);
    
    ra.writeBytes("\n----------------regDump------------------");
    ra.writeBytes(regDump);
   
    ra.writeBytes("\n----------------regVerify------------------");
    ra.writeBytes(regVerify);
   
    ra.close();
    
    
    IMapperTool tool = (IMapperTool) cls.newInstance();
    System.err.println("Mapper done! Instance " + tool + " " + tool.getValueFieldNames() + " "
        + tool.getKeyFieldNames());
    // tool.value2Key(null);
    HashMap map = new HashMap<>();
    IDataSource from = DomainUtil.getDataSource(map);
    tool.mapperValue(from);
    tool.mapperKey(from);
    
    
    
  }
}
