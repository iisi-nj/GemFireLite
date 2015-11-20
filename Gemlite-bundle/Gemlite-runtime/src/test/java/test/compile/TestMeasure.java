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
package test.compile;

import gemlite.core.internal.measurement.MeasureHelper;
import gemlite.core.util.GemliteHelper;

import java.io.InputStream;
import java.io.RandomAccessFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class TestMeasure
{
  public static void main(String[] args) throws Exception
  {
    
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("test/compile/Logic2.class");
    ClassReader cr = new ClassReader(in);
    ClassNode cn = new ClassNode();
    cr.accept(cn, 0);
    
    RandomAccessFile ra = new RandomAccessFile(Thread.currentThread().getContextClassLoader()
        .getResource("test/compile/Logic2.class").getFile().toString(), "r");
    byte[] bbb=  new byte[(int)ra.length()];
    ra.read(bbb);
    ra.close();
    
    ClassReader cr2 = new ClassReader(bbb);
    ClassNode cn2 = new ClassNode();
    cr2.accept(cn2, 0);
    
    MeasureHelper.instrumentCheckPoint("classname", cn);
    byte[] bb = GemliteHelper.classNodeToBytes(cn);
    GemliteHelper.checkAsmBytes(bb, "d:/tmp/measure.check.log");
    
    byte[] bb2 = GemliteHelper.classNodeToBytes(cn2);
    GemliteHelper.checkAsmBytes(bb2, "d:/tmp/before.check.log");
  }
}
