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
package gemlite.core.internal.common;
//
//import gemlite.core.internal.hotdeploy.ScannedItem;
//import gemlite.core.internal.support.GemliteException;
//import gemlite.core.util.LogUtil;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassWriter;
//import org.objectweb.asm.tree.AnnotationNode;
//import org.objectweb.asm.tree.ClassNode;
//import org.objectweb.asm.util.CheckClassAdapter;
//import org.objectweb.asm.util.TraceClassVisitor;
//
//import com.gemstone.gemfire.cache.Region;
//import com.gemstone.gemfire.cache.partition.PartitionListener;
//import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;
//import com.gemstone.gemfire.internal.cache.PartitionedRegion;
//
//public class GemliteHelper
//{
//  public final static GemliteAnnotation readAnnotations(ClassNode cn)
//  {
//    Map<String, Map<Object, Object>> anns = new HashMap<>();
//    for (int j = 0; cn.visibleAnnotations!=null&& j < cn.visibleAnnotations.size(); j++)
//    {
//      AnnotationNode ann = (AnnotationNode) cn.visibleAnnotations.get(j);
//      for (int i = 0; i < ann.values.size(); i += 2)
//      { 
//        Object k = ann.values.get(i);
//        Object v = ann.values.get(i + 1);
//        if (v instanceof Object[])
//        {
//          Object[] v0 = (Object[]) v;
//          v = v0[1];
//        }
//        Map<Object, Object> m1 = anns.get(ann.desc);
//        if (m1 == null)
//        {
//          m1 = new HashMap<>();
//          anns.put(ann.desc, m1);
//        }
//        m1.put(k, v);
//      }
//    }
//    String clsname = cn.name.replace('/','.');
//    GemliteAnnotation ga = new GemliteAnnotation(clsname,anns);
//    return ga;
//    
//  }
//  
//  public final static void readAnnotationValues(ScannedItem scanItem, AnnotationNode ann)
//  {
//    for (int i = 0; i < ann.values.size(); i += 2)
//    {
//      Object k = ann.values.get(i);
//      Object v = ann.values.get(i + 1);
//      if (v instanceof Object[])
//      {
//        Object[] v0 = (Object[]) v;
//        v = v0[1];
//      }
//      scanItem.addAnnValue(k, v);
//    }
//  }
//  
//  public final static Object getAnnotationValue(AnnotationNode ann, String name)
//  {
//    if (ann.values != null && ann.values.size() > 0)
//    {
//      for (int i = 0; i < ann.values.size(); i++)
//      {
//        Object key = ann.values.get(i);
//        if (name.equals(key))
//          return ann.values.get(i + 1);
//      }
//    }
//    return null;
//  }
//  
//  public final static byte[] classNodeToBytes(ClassNode cn)
//  {
//    byte[] bt = null;
//    try
//    {
//      ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//      cn.accept(cw);
//      bt = cw.toByteArray();
//    }
//    catch (Exception e)
//    {
//      throw new GemliteException(cn.name, e);
//    }
//    return bt;
//  }
//  
//  /***
//   * 
//   * @param cr
//   */
//  public final static void checkAsmBytes(byte[] bytes, String checkFile)
//  {
//    try
//    {
//      FileOutputStream fo = new FileOutputStream(checkFile);
//      PrintWriter pw = new PrintWriter(fo);
//      checkAsmBytes(bytes, pw);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//  }
//  
//  public final static void checkAsmBytes(byte[] bytes, PrintWriter pw)
//  {
//    try
//    {
//      ClassReader cr = new ClassReader(bytes);
//      CheckClassAdapter.verify(cr, true, pw);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//  }
//  
//  public final static void dumpBytecode(byte[] bytes, PrintWriter pw)
//  {
//    ClassReader cr = new ClassReader(bytes);
//    ClassNode cn = new ClassNode();
//    cr.accept(cn, 0);
//    dumpBytecode(cn, pw);
//  }
//  
//  public final static void dumpBytecode(ClassNode cn, PrintWriter pw)
//  {
//    pw = pw == null ? new PrintWriter(System.out) : pw;
//    TraceClassVisitor ca = new TraceClassVisitor(pw);
//    cn.accept(ca);
//  }
//  
//  public final static void writeTempClassFile(byte[] bt, String className)
//  {
//    FileOutputStream fo;
//    try
//    {
//      fo = new FileOutputStream("d:/11.class");
//      fo.write(bt);
//      fo.close();
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
//    
//  }
//  
//  public final static void addPartitionListener(Region<?, ?> r, PartitionListener listener)
//  {
//    if (PartitionRegionHelper.isPartitionedRegion(r))
//    {
//      try
//      {
//        PartitionedRegion pr = (PartitionedRegion) r;
//        PartitionListener[] pl = pr.getPartitionListeners();
//        PartitionListener[] newPl = new PartitionListener[pl.length + 1];
//        System.arraycopy(pl, 0, newPl, 0, pl.length);
//        newPl[pl.length] = listener;
//        
//        Field f = pr.getClass().getDeclaredField("partitionListeners");
//        f.setAccessible(true);
//        f.set(pr, newPl);
//        f.setAccessible(false);
//      }
//      catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
//      {
//        LogUtil.getCoreLog().error("Set partitionListener error on:" + r.getName(), e);
//      }
//    }
//  }
//  
//  public final static void removePartitionListener(Region<?, ?> r, PartitionListener listener)
//  {
//    if (PartitionRegionHelper.isPartitionedRegion(r))
//    {
//      try
//      {
//        PartitionedRegion pr = (PartitionedRegion) r;
//        PartitionListener[] pl = pr.getPartitionListeners();
//        List<PartitionListener> newPl = new ArrayList<PartitionListener>();
//        for (int i = 0; i < pl.length; i++)
//        {
//          PartitionListener l = pl[i];
//          if (l != listener)
//            newPl.add(l);
//        }
//        
//        Field f = r.getClass().getDeclaredField("partitionListeners");
//        f.setAccessible(true);
//        f.set(pr, newPl.toArray(new PartitionListener[0]));
//        f.setAccessible(false);
//      }
//      catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
//      {
//        LogUtil.getCoreLog().error("Set partitionListener error on:" + r.getName(), e);
//      }
//    }
//  }
//  
//}
