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
package gemlite.core.internal.measurement;

import gemlite.core.annotations.logic.CheckPoint;
import gemlite.core.annotations.logic.RemoteService;
import gemlite.core.api.logic.LogicSession;
import gemlite.core.internal.admin.measurement.ScannedMethodItem;
import gemlite.core.internal.context.registryClass.RemoteServiceRegistry;
import gemlite.core.internal.support.annotations.GemliteAnnotation;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.util.GemliteHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class MeasureHelper
{
  private static int queueSize = NumberUtils.toInt(System.getProperty("gemlite.core.measure.queue-size", "1024"));
  
  private static ArrayBlockingQueue<RemoteServiceStatItem> measureQueue = new ArrayBlockingQueue<>(queueSize);
  
  private static MeasureMonitorTask monitorTask;
  
  private static String checkPointDesc = Util.getInternalDesc(CheckPoint.class);
  
  private static MeasureTPSTask tpsTask;
  private static ScheduledExecutorService scheduledService;
  
  static class MeasureMonitorTask extends Thread
  {
    private boolean flag = false;
    
    public MeasureMonitorTask()
    {
      super.setDaemon(true);
    }
    
    /***
     * 单线程
     * 有限队列，允许数据丢失
     * 统计值准确，在过程中已计算
     * 计算最大值，并记录
     * 
     */
    @Override
    public void run()
    {
      
      while (!flag)
      {
        try
        {
          RemoteServiceStatItem rsi = measureQueue.take();
          RemoteServiceStat rss = getRemoteServiceStat(rsi.getModuleName(), rsi.getServiceName());
          rss.recordItem(rsi);
          
          // 写日志
          if (LogUtil.getJMXLog().isDebugEnabled())
            LogUtil.getJMXLog().debug(rsi.toString());
          
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      
      if (LogUtil.getCoreLog().isInfoEnabled())
        LogUtil.getCoreLog().info("Measure monitor task stopped.");
    }
  }
  
  static class MeasureTPSTask extends Thread
  {
    public MeasureTPSTask()
    {
      super.setDaemon(true);
    }
    
    @Override
    public void run()
    {
      try
      {
        Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
        while (it.hasNext())
        {
          IModuleContext ctx = it.next();
          RemoteServiceRegistry reg = (RemoteServiceRegistry) ctx.getRegistry(RemoteService.class);
          if (reg != null && reg.getMbeanMap() != null)
          {
            Iterator<RemoteServiceStat> rsit = reg.getMbeanMap().values().iterator();
            while (rsit.hasNext())
            {
              RemoteServiceStat rs = rsit.next();
              rs.calcuteTPS();
            }
          }
          
        }
      }
      catch (Throwable e)
      {
        LogUtil.getCoreLog().debug("", e);
      }
      
    }
  }
  
  public final static void init()
  {
    monitorTask = new MeasureMonitorTask();
    monitorTask.start();
    scheduledService = new ScheduledThreadPoolExecutor(1);
    tpsTask = new MeasureTPSTask();
    scheduledService.scheduleWithFixedDelay(tpsTask, 1, 2, TimeUnit.SECONDS);
    if (LogUtil.getCoreLog().isInfoEnabled())
      LogUtil.getCoreLog().info("Measure monitor task started.");
  }
  
  public final static RemoteServiceStat getRemoteServiceStat(String moduleName, String serviceName)
  {
    IModuleContext ctx = GemliteContext.getModuleContext(moduleName);
    if (ctx != null)
    {
      RemoteServiceRegistry reg = (RemoteServiceRegistry) ctx.getRegistry(RemoteService.class);
      RemoteServiceStat rss = reg.getStatItem(serviceName);
      return rss;
    }
    return null;
    
  }
  
  /***
   * 逻辑调用完成时，将本次记录的数值归档
   */
  public final static void remoteServiceEnd(RemoteServiceStatItem si)
  {
    if (LogUtil.getCoreLog().isDebugEnabled())
      LogUtil.getCoreLog().debug(
          "Record service:" + si.getServiceName() + " start:" + si.getStart() + " end:" + si.getEnd());
    if (si != null)
    {
      measureQueue.offer(si);
    }
  }
  
  /***
   * 
   * @param className
   * @param checkPoint
   * @param start
   * @param end
   */
  public final static void recordCheckPoint(String className, String checkPoint, long start, long end)
  {
    try
    {
      RemoteServiceStatItem si = LogicSession.getSession().getRemoteServiceStatItem();
      CheckPointStat item = new CheckPointStat();
      item.setCheckPoint(checkPoint);
      item.setStart(start);
      item.setEnd(end);
      item.setClassName(className);
      si.addCheckPoint(item);
      if (LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug(
            "End timer:" + className + "." + si.getServiceName() + ":" + checkPoint + " end:" + item.getEnd()
                + " thread:" + Thread.currentThread().getId());
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("Measure error,className:" + className + " checkPoint:" + checkPoint, e);
    }
    
  }
  
  /***
   * /***
   * 1.产生一个新的类，名字=LogicBean.className $$$ TraceCostBean
   * 2.修改LogicBean类对应的方法，计算方法耗时
   * Class ****LogicBean$$$TraceCostBean
   * public long methodName1Cost;
   * public long methodName2Cost;
   * 
   * @param cn
   * @param context
   * @param className
   * @param cn
   * @return
   */
  private final static boolean checkMethodName(ClassNode cn, MethodNode mn, Set<ScannedMethodItem> methods)
  {
    ScannedMethodItem check = new ScannedMethodItem(cn.name,mn.name,mn.desc);
    if (methods != null && methods.contains(check))
      return true;
    if (methods == null)
    {
      GemliteAnnotation ga = GemliteHelper.readAnnotations(cn);
      return ga.getAnnotation(checkPointDesc) != null;
    }
    return false;
  }
  
  public final static int instrumentCheckPoint(String className, ClassNode cn)
  {
    return instrumentCheckPoint(className, cn, null);
  }
  
  public final static void instrumentCheckPoint(String className, MethodNode mn)
  {
    if (LogUtil.getCoreLog().isTraceEnabled())
      LogUtil.getCoreLog().trace("Found check point, class:" + className + " method:" + mn.name);
    
    InsnList insn = mn.instructions;
    List<AbstractInsnNode> returnIndex = new ArrayList<>();
    // 找到所有的return
    int localVarCount = mn.localVariables.size();
    for (int i = 0; i < insn.size(); i++)
    {
      AbstractInsnNode insnNode = (AbstractInsnNode) insn.get(i);
      switch (insnNode.getOpcode())
      {
        case Opcodes.ARETURN:
        case Opcodes.IRETURN:
        case Opcodes.DRETURN:
        case Opcodes.LRETURN:
        case Opcodes.FRETURN:
          returnIndex.add(insnNode.getPrevious());
          break;
        case Opcodes.RETURN:
          returnIndex.add(insnNode);
          break;
      }
    }
    // 计算开始时间
    insn.insert(new VarInsnNode(Opcodes.LSTORE, localVarCount + 2));
    insn.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
    // 计算结束时间
    for (AbstractInsnNode insnNode : returnIndex)
    {
      insn.insertBefore(insnNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis",
          "()J", false));
      insn.insertBefore(insnNode, new VarInsnNode(Opcodes.LSTORE, localVarCount + 4));
      
      insn.insertBefore(insnNode, new LdcInsnNode(className));
      insn.insertBefore(insnNode, new LdcInsnNode(mn.name));
      insn.insertBefore(insnNode, new VarInsnNode(Opcodes.LLOAD, localVarCount + 2));
      insn.insertBefore(insnNode, new VarInsnNode(Opcodes.LLOAD, localVarCount + 4));
      insn.insertBefore(insnNode, new MethodInsnNode(Opcodes.INVOKESTATIC,
          "gemlite/core/internal/measurement/MeasureHelper", "recordCheckPoint",
          "(Ljava/lang/String;Ljava/lang/String;JJ)V", false));
    }
  }
  
  /***
   * 
   * @param className
   * @param cn
   * @param methods
   * @return
   */
  public final static int instrumentCheckPoint(String className, ClassNode cn, Set<ScannedMethodItem> methods)
  {
    int num = 0;
    for (Object obj : cn.methods)
    {
      MethodNode mn = (MethodNode) obj;
      if (checkMethodName(cn, mn, methods))
      {
        instrumentCheckPoint(className, mn);
        num++;
      }
    }
    return num;
  }
}
