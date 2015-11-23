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
package gemlite.core.internal.context;
//
//import gemlite.core.internal.asm.serialize.DataSerializeHelper;
//import gemlite.core.internal.common.GemliteHelper;
//import gemlite.core.internal.context.registryClass.AdminServiceRegistry;
//import gemlite.core.internal.context.registryClass.ApplicationListenerRegistry;
//import gemlite.core.internal.context.registryClass.DeployConfigureRegistry;
//import gemlite.core.internal.context.registryClass.FunctionRegistry;
//import gemlite.core.internal.context.registryClass.MBeanRegistry;
//import gemlite.core.internal.context.registryClass.RemoteServiceRegistry;
//import gemlite.core.internal.context.registryClass.ViewItemRegistry;
//import gemlite.core.internal.hotdeploy.ScannedItem;
//import gemlite.core.internal.measurement.MeasureHelper;
//import gemlite.core.internal.support.context.IGemliteRegistry;
//
//import java.io.IOException;
//
//import org.objectweb.asm.tree.AnnotationNode;
//import org.objectweb.asm.tree.ClassNode;
//
///***
// * function,listener,index,logic bean,app name
// * 
// * @author ynd
// * 
// */
//public class GemliteRegistryHelper
//{
//  public final static String APP_LISTENER = "APP_LISTENER";
//  public final static String FUNCTION = "FUNCTION";
//  //public final static String INDEX = "INDEX";
//  public final static String DEPLOY_CONFIGURE = "DEPLOY_CONFIGURE";
//  public final static String LISTENER = "LISTENER";
//  public final static String NONE = "NONE";
//  //public final static String INDEX_DEF = "INDEX_DEF";
//  public final static String REMOTE_SERVICE = "LOGIC_BEAN";
//  public final static String REMOTE_ADMIN_SERVICE = "ADMIN_BEAN"; // 管理娄bean
//  public final static String MODEL_MBEAN = "MODEL_MBEAN"; // 管理娄bean
//  
//  public final static String VIEW = "VIEW"; // INDEX CACHE 
//  
//  public final static IGemliteRegistry createRegistry(String type)
//  {
//    IGemliteRegistry registry = null;
//    switch (type)
//    {
//      case FUNCTION:
//        registry = new FunctionRegistry();
//        break;
//      case REMOTE_SERVICE:
//        registry = new RemoteServiceRegistry();
//        break;
//      case DEPLOY_CONFIGURE:
//        registry = new DeployConfigureRegistry();
//        break;
////      case INDEX_DEF:
////        registry = new IndexDefRegistry();
////        break;
//      case APP_LISTENER:
//        registry = new ApplicationListenerRegistry();
//        break;
//      case MODEL_MBEAN:
//        registry = new MBeanRegistry();
//        break;
//      case VIEW:
//          registry = new ViewItemRegistry();
//          break;
//      case REMOTE_ADMIN_SERVICE:
//        registry = new AdminServiceRegistry();
//        break;
//      default:
//        break;
//    }
//    return registry;
//  }
//  
//  /***
//   * 
//   * @param className
//   * @param cn
//   * @return
//   * @throws IOException
//   * @throws InstantiationException
//   * @throws IllegalAccessException
//   * @throws ClassNotFoundException
//   */
//  public final static ScannedItem analyzeClassBytes(String className, ClassNode cn) throws IOException,
//      InstantiationException, IllegalAccessException, ClassNotFoundException
//  {
//    ScannedItem scannedItem = null;
//    boolean classChanged = false;
//    if (cn.interfaces != null && cn.interfaces.size() > 0)
//    {
//      for (int i = 0; i < cn.interfaces.size(); i++)
//      {
//        String name = (String) cn.interfaces.get(i);
//        switch (name)
//        {
//          case "com/gemstone/gemfire/cache/execute/Function":
//            scannedItem = new ScannedItem(className, FUNCTION);
//            break;
//          case "org/springframework/context/ApplicationListener":
//            scannedItem = new ScannedItem(className, APP_LISTENER);
//            break;
//        }
//        if(scannedItem!=null)
//          break;
//      }
//    }
//    if ( cn.visibleAnnotations != null)
//    {
//      for (int i = 0; i < cn.visibleAnnotations.size(); i++)
//      {
//        AnnotationNode ann = (AnnotationNode) cn.visibleAnnotations.get(i);
//        switch (ann.desc)
//        {
//          case "Lgemlite/core/annotations/DeployConfigure;":
//            scannedItem = new ScannedItem(className, DEPLOY_CONFIGURE);
//            GemliteHelper.readAnnotationValues(scannedItem, ann);
//            break;
//          case "Lgemlite/core/annotations/domain/AutoSerialize;":
//            DataSerializeHelper.getInstance().process(cn);
//            classChanged = true;
//            break;
//          case "Lgemlite/core/annotations/admin/AdminService;":
//            scannedItem = new ScannedItem(className, REMOTE_ADMIN_SERVICE);
//            break;
//          case "Lgemlite/core/annotations/logic/RemoteService;":
//            scannedItem = new ScannedItem(className, REMOTE_SERVICE);
//            break;
//          case "Lgemlite/core/annotations/view/View;":
//              scannedItem = new ScannedItem(className, VIEW);
//              break;  
//          case "Lgemlite/core/annotations/mbean/GemliteMBean;":
//            scannedItem = new ScannedItem(className, MODEL_MBEAN);
//            break;
////          case "Lgemlite/core/annotations/index/IndexRegion;":
////            scannedItem = new ScannedItem(className, INDEX_DEF);
////            break;
//          
//          default:
//            break;
//        }
//      }
//    }
//    
//    boolean processed = MeasureHelper.instrumentCheckPoint(className, cn);
//    classChanged = classChanged || processed;
//    if (classChanged)
//    {
//      byte[] processedBytes = GemliteHelper.classNodeToBytes(cn);
//      if (scannedItem != null)
//        scannedItem.setBytesTransformed(processedBytes);
//      classChanged = false;
//    }
//    return scannedItem;
//  }
//  
//}
