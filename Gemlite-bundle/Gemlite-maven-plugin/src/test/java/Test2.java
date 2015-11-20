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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Test2 implements Opcodes
{
  public final static String DATAS_NAME = "com/gemstone/gemfire/DataSerializable";
  
  public static void main(String[] args) throws Exception
  {
    
    String name = "abcd";
    System.out.println(name.substring(0));
    File f = new File("D:/springsource/gemlite_prod/ynd.test.domain/target/classes/");
    URLClassLoader ucl= new URLClassLoader(new URL[]{f.toURI().toURL()});
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(
        "D:/springsource/gemlite_prod/ynd.test.domain/target/classes/ynd/test/Ac01.class"));
    byte[] bytes = new byte[in.available()];
    in.read(bytes);
    in.close();
    
    ClassReader cr = new ClassReader(bytes);
    ClassNode cn = new ClassNode();
    cr.accept(cn, 0);
    
    System.out.println(cn.sourceFile+" "+cn.outerClass);
//    for (Object o : cn.methods)
//    {
//      MethodNode mn = (MethodNode) o;
//      System.out.println(mn.name + " " + mn.desc);
//    }
//    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//    for (Object o : cn.visibleAnnotations)
//    {
//      AnnotationNode an = (AnnotationNode) o;
//      if (DomainMojoConstant.AN_Key.equals(an.desc))
//      {
//        Type t = (Type) an.values.get(1);
//        System.out.println(t.getClassName()+" "+t.getInternalName());
//        InputStream x1 =  ucl.getResourceAsStream(t.getInternalName()+".class");
//        System.out.println("&&"+x1.toString());
//      }
//      System.out.println(an.desc + " " + an.values);
//    }
    
  }
  
}
