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
package loader;

import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.util.ResourceUtils;

public class TestScannerCp
{
  public static void main(String[] args) throws Exception
  {
    ServerConfigHelper.initConfig();
    ServerConfigHelper.initLog4j("log4j2-server.xml");
    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    LoggerConfig conf = context.getConfiguration().getLoggerConfig(LogUtil.getCoreLog().getName());
    conf.setLevel(Level.INFO);
    context.updateLoggers();
    
    
    Map<String, Map<String, Set<String>>> cpm1 = new HashMap<>();
    Map<String, Set<String>> cpm2 = new HashMap();
    cpm1.put("_order_module_", cpm2);
    Set<String> cps1=new HashSet<>();
    cpm2.put("gemlite.sample.order.logic.TestCheckpoint", cps1);
    cps1.add("f1");
    cps1.add("f2");
    GemliteDeployer.getSession().put("_checkPointMap_", cpm1);
    
    
    // JarURLFinder finder = new ClasspathURLFinder();
    // finder.doFind();
    File f1 = ResourceUtils.getFile("D:/Code/vmgemlite/vmgemlite/Gemlite-bundle/Gemlite-runtime/target/classes");
    IModuleContext m1= GemliteDeployer.getInstance().deploy(f1.toURI().toURL());
    System.out.println("loader1:" + m1.getLoader().getResource("."));
    File f2 = ResourceUtils.getFile("D:/Code/vmgemlite/gltraining/order/orderLogic/target/classes");
    IModuleContext m2= GemliteDeployer.getInstance().deploy(f2.toURI().toURL());
    System.out.println("loader2:" + m2.getLoader().getResource("."));
    
    
    File f3 = ResourceUtils.getFile("D:/Code/vmgemlite/gltraining/order/orderLogic/target/classes");
    IModuleContext m3= GemliteDeployer.getInstance().deploy(f3.toURI().toURL());
    System.out.println("loader3:" + m3.getLoader().getResource("."));
  }
}
