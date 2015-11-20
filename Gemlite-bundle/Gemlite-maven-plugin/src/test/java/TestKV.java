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
//import gemlite.core.internal.domain.IDataSource;
//import gemlite.core.internal.domain.IMapperTool;
//import gemlite.maven.plugin.support.ByteArrayClassLoader;
//import gemlite.maven.plugin.support.DomainMojoHelper;
//
//import java.io.File;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.HashMap;
//
//import org.objectweb.asm.AnnotationVisitor;
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.FieldVisitor;
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.tree.ClassNode;
//
//public class TestKV implements Opcodes
//{
//  public static void main(String[] args)throws Exception
//  {
//    System.setProperty("current-project-name","myd1");
//    System.setProperty("current-project-path","D:/Code/vmgemlite/vmsample/Sample-bundle/Sample-domain/target/classes");
//    String targetDir = "D:/Code/vmgemlite/vmsample/Sample-bundle/Sample-domain/target/classes";
//    //set classpath
//    File f = new File(targetDir);
//    URLClassLoader ucl = new URLClassLoader(new URL[] { f.toURI().toURL() }, Thread.currentThread()
//        .getContextClassLoader());
//    
//    byte[] bt =  TestKV.dump();
//    ClassReader cr = new ClassReader(bt);
//    ClassNode cn = new ClassNode();
//    cr.accept(cn, 0);
//    DomainMojoHelper.dumpBytecode(cn, null);
//    ByteArrayClassLoader cl = new ByteArrayClassLoader(bt,ucl);
//    
//    Class x = cl.loadClass("testcase.build.LbcMapper");
//    IMapperTool tool = (IMapperTool)x.newInstance();
//    
//    HashMap map = new HashMap<>();
//    IDataSource from = DomainUtil.getDataSource(map);
//    tool.mapperValue(from);
//    tool.mapperKey(from);
//    
//    System.out.println("done");
//  }
//  public static byte[] dump() throws Exception
//  {
//    
//    ClassWriter cw = new ClassWriter(0);
//    FieldVisitor fv;
//    MethodVisitor mv;
//    AnnotationVisitor av0;
//    
//    cw.visit(
//        V1_7,
//        ACC_PUBLIC + ACC_SUPER,
//        "testcase/build/LbcMapper",
//        "Ljava/lang/Object;Lgemlite/core/internal/domain/IMapperTool<Lgemlite/sample/domain/cr/lbc/LeftBaseCenterKey;Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;>;",
//        "java/lang/Object", new String[] { "gemlite/core/internal/domain/IMapperTool" });
//    
//    cw.visitSource("LbcMapper.java", null);
//    
//    {
//      fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "valueFieldNames", "Ljava/util/Set;",
//          "Ljava/util/Set<Ljava/lang/String;>;", null);
//      fv.visitEnd();
//    }
//    {
//      fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "keyFieldNames", "Ljava/util/Set;",
//          "Ljava/util/Set<Ljava/lang/String;>;", null);
//      fv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(47, l0);
//      mv.visitTypeInsn(NEW, "java/util/HashSet");
//      mv.visitInsn(DUP);
//      mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V", false);
//      mv.visitFieldInsn(PUTSTATIC, "testcase/build/LbcMapper", "valueFieldNames", "Ljava/util/Set;");
//      Label l1 = new Label();
//      mv.visitLabel(l1);
//      mv.visitLineNumber(48, l1);
//      mv.visitTypeInsn(NEW, "java/util/HashSet");
//      mv.visitInsn(DUP);
//      mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashSet", "<init>", "()V", false);
//      mv.visitFieldInsn(PUTSTATIC, "testcase/build/LbcMapper", "keyFieldNames", "Ljava/util/Set;");
//      Label l2 = new Label();
//      mv.visitLabel(l2);
//      mv.visitLineNumber(50, l2);
//      mv.visitFieldInsn(GETSTATIC, "testcase/build/LbcMapper", "valueFieldNames", "Ljava/util/Set;");
//      mv.visitLdcInsn("1");
//      mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z", true);
//      mv.visitInsn(POP);
//      Label l3 = new Label();
//      mv.visitLabel(l3);
//      mv.visitLineNumber(51, l3);
//      mv.visitFieldInsn(GETSTATIC, "testcase/build/LbcMapper", "valueFieldNames", "Ljava/util/Set;");
//      mv.visitLdcInsn("2");
//      mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z", true);
//      mv.visitInsn(POP);
//      Label l4 = new Label();
//      mv.visitLabel(l4);
//      mv.visitLineNumber(53, l4);
//      mv.visitFieldInsn(GETSTATIC, "testcase/build/LbcMapper", "keyFieldNames", "Ljava/util/Set;");
//      mv.visitLdcInsn("1");
//      mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z", true);
//      mv.visitInsn(POP);
//      Label l5 = new Label();
//      mv.visitLabel(l5);
//      mv.visitLineNumber(54, l5);
//      mv.visitInsn(RETURN);
//      mv.visitMaxs(2, 0);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(10, l0);
//      mv.visitVarInsn(ALOAD, 0);
//      mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
//      mv.visitInsn(RETURN);
//      Label l1 = new Label();
//      mv.visitLabel(l1);
//      mv.visitLocalVariable("this", "Ltestcase/build/LbcMapper;", null, l0, l1, 0);
//      mv.visitMaxs(1, 1);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "isFullColumnKey", "()Z", null, null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(16, l0);
//      mv.visitInsn(ICONST_0);
//      mv.visitInsn(IRETURN);
//      Label l1 = new Label();
//      mv.visitLabel(l1);
//      mv.visitLocalVariable("this", "Ltestcase/build/LbcMapper;", null, l0, l1, 0);
//      mv.visitMaxs(1, 1);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "mapperValue",
//          "(Lgemlite/core/internal/domain/IDataSource;)Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;", null, null);
//      mv.visitCode();
//      mv.visitInsn(ACONST_NULL);
//      mv.visitInsn(ARETURN);
//      mv.visitMaxs(1, 2);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "mapperValue", "(Lgemlite/core/internal/domain/IDataSource;Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;)Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;", null, null);
//      mv.visitCode();
//      mv.visitVarInsn(ALOAD, 2);
//      mv.visitVarInsn(ALOAD, 1);
//      mv.visitLdcInsn("as");
//      mv.visitMethodInsn(INVOKEINTERFACE, "gemlite/core/internal/domain/IDataSource", "getString", "(Ljava/lang/String;)Ljava/lang/String;", true);
//      mv.visitMethodInsn(INVOKEVIRTUAL, "gemlite/sample/domain/cr/lbc/LeftBaseCenter", "setAssign_station", "(Ljava/lang/String;)V", false);
//      mv.visitVarInsn(ALOAD, 2);
//      mv.visitInsn(ARETURN);
//      mv.visitMaxs(3, 3);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "mapperKey",
//          "(Lgemlite/core/internal/domain/IDataSource;)Lgemlite/sample/domain/cr/lbc/LeftBaseCenterKey;", null, null);
//      mv.visitCode();
//      mv.visitTypeInsn(NEW, "gemlite/sample/domain/cr/lbc/LeftBaseCenterKey");
//      mv.visitInsn(DUP);
//      mv.visitMethodInsn(INVOKESPECIAL, "gemlite/sample/domain/cr/lbc/LeftBaseCenterKey", "<init>", "()V", false);
//      mv.visitVarInsn(ASTORE, 2);
//      mv.visitVarInsn(ALOAD, 2);
//      mv.visitVarInsn(ALOAD, 1);
//      mv.visitLdcInsn("assign_station");
//      mv.visitMethodInsn(INVOKEINTERFACE, "gemlite/core/internal/domain/IDataSource", "getString",
//          "(Ljava/lang/String;)Ljava/lang/String;", true);
//      mv.visitMethodInsn(INVOKEVIRTUAL, "gemlite/sample/domain/cr/lbc/LeftBaseCenterKey", "setAssign_station",
//          "(Ljava/lang/String;)V", false);
//      mv.visitVarInsn(ALOAD, 2);
//      mv.visitVarInsn(ALOAD, 1);
//      mv.visitLdcInsn("assign_station");
//      mv.visitMethodInsn(INVOKEINTERFACE, "gemlite/core/internal/domain/IDataSource", "getString",
//          "(Ljava/lang/String;)Ljava/lang/String;", true);
//      mv.visitMethodInsn(INVOKEVIRTUAL, "gemlite/sample/domain/cr/lbc/LeftBaseCenterKey", "setAssign_station",
//          "(Ljava/lang/String;)V", false);
//      mv.visitVarInsn(ALOAD, 2);
//      mv.visitVarInsn(ALOAD, 1);
//      mv.visitLdcInsn("assign_station");
//      mv.visitMethodInsn(INVOKEINTERFACE, "gemlite/core/internal/domain/IDataSource", "getString",
//          "(Ljava/lang/String;)Ljava/lang/String;", true);
//      mv.visitMethodInsn(INVOKEVIRTUAL, "gemlite/sample/domain/cr/lbc/LeftBaseCenterKey", "setAssign_station",
//          "(Ljava/lang/String;)V", false);
//      mv.visitVarInsn(ALOAD, 2);
//      mv.visitInsn(ARETURN);
//      mv.visitMaxs(3, 3);
//      mv.visitEnd();
//    }
//    {
//      mv = cw
//          .visitMethod(ACC_PUBLIC, "value2Key",
//              "(Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;)Lgemlite/sample/domain/cr/lbc/LeftBaseCenterKey;", null,
//              null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(44, l0);
//      mv.visitInsn(ACONST_NULL);
//      mv.visitInsn(ARETURN);
//      Label l1 = new Label();
//      mv.visitLabel(l1);
//      mv.visitLocalVariable("this", "Ltestcase/build/LbcMapper;", null, l0, l1, 0);
//      mv.visitLocalVariable("domain", "Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;", null, l0, l1, 1);
//      mv.visitMaxs(1, 2);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "getValueFieldNames", "()Ljava/util/Set;",
//          "()Ljava/util/Set<Ljava/lang/String;>;", null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(58, l0);
//      mv.visitFieldInsn(GETSTATIC, "testcase/build/LbcMapper", "valueFieldNames", "Ljava/util/Set;");
//      mv.visitInsn(ARETURN);
//      Label l1 = new Label();
//      mv.visitLabel(l1);
//      mv.visitLocalVariable("this", "Ltestcase/build/LbcMapper;", null, l0, l1, 0);
//      mv.visitMaxs(1, 1);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "getKeyClass", "()Ljava/lang/Class;",
//          "()Ljava/lang/Class<Lgemlite/sample/domain/cr/lbc/LeftBaseCenterKey;>;", null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(64, l0);
//      mv.visitInsn(ACONST_NULL);
//      mv.visitInsn(ARETURN);
//      Label l1 = new Label();
//      mv.visitLabel(l1);
//      mv.visitLocalVariable("this", "Ltestcase/build/LbcMapper;", null, l0, l1, 0);
//      mv.visitMaxs(1, 1);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "getValueClass", "()Ljava/lang/Class;",
//          "()Ljava/lang/Class<Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;>;", null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(70, l0);
//      mv.visitInsn(ACONST_NULL);
//      mv.visitInsn(ARETURN);
//      Label l1 = new Label();
//      mv.visitLabel(l1);
//      mv.visitLocalVariable("this", "Ltestcase/build/LbcMapper;", null, l0, l1, 0);
//      mv.visitMaxs(1, 1);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC, "getKeyFieldNames", "()Ljava/util/Set;", "()Ljava/util/Set<Ljava/lang/String;>;",
//          null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(77, l0);
//      mv.visitFieldInsn(GETSTATIC, "testcase/build/LbcMapper", "keyFieldNames", "Ljava/util/Set;");
//      mv.visitInsn(ARETURN);
//      Label l1 = new Label();
//      mv.visitLabel(l1);
//      mv.visitLocalVariable("this", "Ltestcase/build/LbcMapper;", null, l0, l1, 0);
//      mv.visitMaxs(1, 1);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "mapperValue",
//          "(Lgemlite/core/internal/domain/IDataSource;Ljava/lang/Object;)Ljava/lang/Object;", null, null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(1, l0);
//      mv.visitVarInsn(ALOAD, 0);
//      mv.visitVarInsn(ALOAD, 1);
//      mv.visitVarInsn(ALOAD, 2);
//      mv.visitTypeInsn(CHECKCAST, "gemlite/sample/domain/cr/lbc/LeftBaseCenter");
//      mv.visitMethodInsn(
//          INVOKEVIRTUAL,
//          "testcase/build/LbcMapper",
//          "mapperValue",
//          "(Lgemlite/core/internal/domain/IDataSource;Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;)Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;",
//          false);
//      mv.visitInsn(ARETURN);
//      mv.visitMaxs(3, 3);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "mapperValue",
//          "(Lgemlite/core/internal/domain/IDataSource;)Ljava/lang/Object;", null, null);
//      mv.visitCode();
//      mv.visitVarInsn(ALOAD, 0);
//      mv.visitVarInsn(ALOAD, 1);
//      mv.visitMethodInsn(INVOKEVIRTUAL, "testcase/build/LbcMapper", "mapperValue",
//          "(Lgemlite/core/internal/domain/IDataSource;)Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;", false);
//      mv.visitInsn(ARETURN);
//      mv.visitMaxs(2, 2);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "mapperKey",
//          "(Lgemlite/core/internal/domain/IDataSource;)Ljava/lang/Object;", null, null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(1, l0);
//      mv.visitVarInsn(ALOAD, 0);
//      mv.visitVarInsn(ALOAD, 1);
//      mv.visitMethodInsn(INVOKEVIRTUAL, "testcase/build/LbcMapper", "mapperKey",
//          "(Lgemlite/core/internal/domain/IDataSource;)Lgemlite/sample/domain/cr/lbc/LeftBaseCenterKey;", false);
//      mv.visitInsn(ARETURN);
//      mv.visitMaxs(2, 2);
//      mv.visitEnd();
//    }
//    {
//      mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "value2Key",
//          "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
//      mv.visitCode();
//      Label l0 = new Label();
//      mv.visitLabel(l0);
//      mv.visitLineNumber(1, l0);
//      mv.visitVarInsn(ALOAD, 0);
//      mv.visitVarInsn(ALOAD, 1);
//      mv.visitTypeInsn(CHECKCAST, "gemlite/sample/domain/cr/lbc/LeftBaseCenter");
//      mv.visitMethodInsn(INVOKEVIRTUAL, "testcase/build/LbcMapper", "value2Key",
//          "(Lgemlite/sample/domain/cr/lbc/LeftBaseCenter;)Lgemlite/sample/domain/cr/lbc/LeftBaseCenterKey;", false);
//      mv.visitInsn(ARETURN);
//      mv.visitMaxs(2, 2);
//      mv.visitEnd();
//    }
//    cw.visitEnd();
//    
//    return cw.toByteArray();
//  }
//}
