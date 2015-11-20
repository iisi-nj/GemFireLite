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
package gemlite.core.internal.admin.measurement;

import gemlite.core.annotations.mbean.GemliteMBean;
import gemlite.core.api.logic.LogicServices;
import gemlite.core.internal.hotdeploy.CheckPointScanner;
import gemlite.core.internal.logic.IRemoteService;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.internal.support.hotdeploy.JarURLFinder;
import gemlite.core.internal.support.hotdeploy.finderClass.ClasspathURLFinder;
import gemlite.core.internal.support.jmx.AggregateType;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.GemliteHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.ResourceUtils;

@GemliteMBean(name = "CheckPointService", config = true)
@ManagedResource(description = "Remote service stat", persistLocation = "", persistPeriod = 10)
public class CheckPointService
{
  private final static String CONSTRUCT_NAME = "<init>";
  
  public static void main(String[] args) throws Exception
  {
    ServerConfigHelper.initConfig();
    ServerConfigHelper.initLog4j("log4j2-server.xml");
    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    LoggerConfig conf = context.getConfiguration().getLoggerConfig(LogUtil.getCoreLog().getName());
    conf.setLevel(Level.INFO);
    context.updateLoggers();
    
    CheckPointService c = new CheckPointService();
    c.ListMethods();
    
  }
  
  private void ListMethods() throws Exception
  {
    JarURLFinder finder = new ClasspathURLFinder();
    finder.doFind();
    GemliteDeployer deployer = GemliteDeployer.getInstance();
     deployer.deploy(finder.getCoreJarUrl());
    
    File f2 = ResourceUtils.getFile("D:/Code/vmgemlite/gltraining/order/orderLogic/target/classes");
    IModuleContext m2 = deployer.deploy(f2.toURI().toURL());
    GemliteSibingsLoader loader2 = m2.getLoader();
    
    CheckPointScanner.DEBUG = true;
    String clsName = "gemlite/sample/order/logic/TestCheckpoint";
    CheckPointService ms = new CheckPointService();
    CheckPointContext ctx = ms.analazyCheckPoint(loader2, clsName);
    
    System.out.println("****************************************************************");
    Iterator<ScannedMethodItem> it = ctx.scannedItems.iterator();
    while (it.hasNext())
    {
      ScannedMethodItem item = it.next();
      travelItems(item, "");
    }
  }
  
  private void travelItems(ScannedMethodItem m, String prefix)
  {
    System.out.println(prefix + m.className + "->" + m.methodName+" ->"+m.methodDesc);
    for (ScannedMethodItem mm : m.children)
    {
      travelItems(mm, prefix + "--");
    }
  }
  
  @AggregateOperation(support = true, value = AggregateType.OPONLYONE)
  @ManagedOperation
  @SuppressWarnings("rawtypes")
  public List<ScannedMethodItem> listMethods(String module, String serviceName)
  {
    
    IRemoteService srv = LogicServices.getService(module, serviceName);
    IModuleContext ctx = GemliteContext.getModuleContext(module);
    String internalName = Util.getInternalName(srv.getClass());
    CheckPointContext cp = analazyCheckPoint(ctx.getLoader(), internalName);
    return cp.scannedItems;
  }
  
  private CheckPointContext analazyCheckPoint(GemliteSibingsLoader loader2, String cls)
  {
    CheckPointContext ctx = new CheckPointContext(loader2);
    analazyClass(ctx, cls, "doExecute", null);
    return ctx;
  }
  
  private void analazyClass(CheckPointContext ctx, String cls, String method, ScannedMethodItem caller)
  {
    GemliteSibingsLoader loader = ctx.loader;
    loader.getResource(cls + ".class");
    InputStream in = loader.getResourceAsStream(cls + ".class");
    if (in == null)
    {
      LogUtil.getCoreLog().error("name={} url={} ", cls, loader.getURL());
      return;
    }
    ClassNode cn = GemliteHelper.toClassNode(in);
    analazyClass(ctx, cn, method, caller);
  }
  
  private void analazyClass(CheckPointContext ctx, ClassNode cn, String method, ScannedMethodItem caller)
  {
    for (int i = 0; cn.methods != null && i < cn.methods.size(); i++)
    {
      MethodNode mn = (MethodNode) cn.methods.get(i);
      if (mn.name.equals(method))
      {
        if (caller == null)
          caller = new ScannedMethodItem(cn.name, mn.name,mn.desc);
        if (ctx.allSet.contains(caller))
          return;
        if (mn.name.equals("doExecute"))
          ctx.scannedItems.add(caller);
        ctx.allSet.add(caller);
        analyzeMethod(ctx, cn, mn, caller);
        return;
      }
    }
  }
  
  private void analyzeMethod(CheckPointContext ctx, ClassNode cn, MethodNode mn, ScannedMethodItem caller)
  {
    // Çä‰Í„ýp
    Set<ScannedMethodItem> methods = new HashSet<ScannedMethodItem>();
    GemliteSibingsLoader loader = ctx.loader;
    InsnList insnList = mn.instructions;
    for (int j = 0; j < insnList.size(); j++)
    {
      AbstractInsnNode insn = insnList.get(j);
      
      if (insn.getType() == AbstractInsnNode.METHOD_INSN)
      {
        MethodInsnNode methodInsn = (MethodInsnNode) insn;
        if (CONSTRUCT_NAME.equals(methodInsn.name))
          continue;
        URL url = loader.findResource(methodInsn.owner + ".class");
        boolean blCurrentModule = url != null;
        if (blCurrentModule)
        {
          ScannedMethodItem item = new ScannedMethodItem(methodInsn.owner, methodInsn.name, methodInsn.desc);
          LogUtil.getCoreLog().trace("I={} owner={} name={} op={}", j, methodInsn.owner, methodInsn.name,
              methodInsn.getOpcode());
          
          // $­/&/Í„ýp
          if (methods.contains(item))
            continue;
          
          caller.children.add(item);
          
          // °Umethods
          methods.add(item);
          
          if (!methodInsn.owner.equals(cn.name))
            analazyClass(ctx, item.className, item.methodName, item);
          else
            analazyClass(ctx, cn, item.methodName, item);
        }
      }
    }
  }
  
}
