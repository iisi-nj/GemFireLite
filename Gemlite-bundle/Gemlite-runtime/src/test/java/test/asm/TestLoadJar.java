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
package test.asm;
//
//import gemlite.core.internal.hotdeploy.GemliteClassScanner;
//import gemlite.core.internal.support.context.IModuleContext;
//import gemlite.core.internal.support.hotdeploy.DeployParameter;
//import gemlite.core.internal.support.hotdeploy.GemliteJarLoader;
//import gemlite.core.internal.support.system.ServerConfigHelper;
//import gemlite.core.util.LogUtil;
//
//import java.io.File;
//import java.lang.reflect.Method;
//import java.net.URL;
//
//public class TestLoadJar
//{
//  public static void main(String[] args) throws Exception
//  {
//    ServerConfigHelper.initConfig();
//    ServerConfigHelper.initLog4j("classpath:log4j-server.xml");
//    LogUtil.getCoreLog().debug("xxxxxxxxxxxxxxxxxxxxxxxx");
//    File f = new File( "D:/Code/vmgemlite/vmsample/Sample-bundle/Sample-logic/target/Sample-logic-0.0.1-SNAPSHOT.jar");
//    File f2 = new File( "D:/Code/vmgemlite/vmsample/Sample-bundle/Sample-domain/target/Sample-domain-0.0.1-SNAPSHOT.jar");
//    GemliteClassScanner scan = new GemliteClassScanner();
//    DeployParameter dp = new DeployParameter();
//    GemliteJarLoader loader = new GemliteJarLoader(new URL[]{f2.toURI().toURL()});
//    IModuleContext context = scan.scan(loader);
//    context.register();
//    
//    Object obj =  loader.loadClass("sample.problem.measure.MyLogicBean").newInstance();
//    Method m = obj.getClass().getDeclaredMethod("test1");
//    m.invoke(obj);
//  }
//}
