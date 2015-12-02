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
package gemlite.core.internal.support.jmx;

import gemlite.core.internal.support.context.ManagementRegionHelper;
import gemlite.core.internal.support.context.ManagementRegionHelper.MGM_KEYS;
import gemlite.core.internal.support.jmx.annotation.AggregateAttribute;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;
import gemlite.core.internal.support.jmx.domain.GemliteNodeConfig;
import gemlite.core.internal.support.jmx.domain.JMXBeanFieldInfo;
import gemlite.core.internal.support.jmx.domain.JMXBeanInfo;
import gemlite.core.internal.support.jmx.proxy.AbstractProxy;
import gemlite.core.internal.support.system.GemliteAgent;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.support.system.ServerConfigHelper.TYPES;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBean;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.distributed.internal.MembershipListener;
import com.gemstone.gemfire.distributed.internal.membership.InternalDistributedMember;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;

/***
 * TODO:对于运行中出故障的节点，重新启动后，其统计数据会丢失；在刷新客户端时，统计数据会减少，可以客户端需要移除的项目合并或在服务端做持久化
 * //SAMPLE:service:jmx:rmi://192.168.31.184/jndi/rmi://192.168.31.184:52870/jmxconnector
 * 
 * @author ynd
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GemliteManagerNode
{
  private static Map<String, Class<?>> ClassTypeMapping = new HashMap<String, Class<?>>();
  private static Object ATTR_LOCK = new Object();
  static
  {
    ClassTypeMapping.put("boolean", boolean.class);
    ClassTypeMapping.put("byte", byte.class);
    ClassTypeMapping.put("char", char.class);
    ClassTypeMapping.put("short", short.class);
    ClassTypeMapping.put("int", int.class);
    ClassTypeMapping.put("long", long.class);
    ClassTypeMapping.put("float", float.class);
    ClassTypeMapping.put("double", double.class);
  }
  private static int refreshInterval = 5;// seconds
  
  class JMXClientInfo
  {
    GemliteNodeConfig conf;
    JMXConnector connector;
  }
  
  class RefreshThread extends Thread
  {
    public RefreshThread()
    {
      setDaemon(true);
    }
    
    @Override
    public void run()
    {
      if (LogUtil.getCoreLog().isTraceEnabled())
        LogUtil.getCoreLog().trace("merge muti bean datas");
      try
      {
          processAggregator();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    
    /**
     * 只能处理简单对象，int，double，long,boolean,String
     */
    private void processAggregator()
    {
      if (LogUtil.getCoreLog().isTraceEnabled())
        LogUtil.getCoreLog().trace("Process aggregator,proxyMap.size=" + cacheProxyByObjectName.size());
      Iterator<Entry<String, JMXAggregatorBean>> it = cacheProxyByObjectName.entrySet().iterator();
      //遍历所有聚合bean
      while (it.hasNext())
      {
        Entry<String, JMXAggregatorBean> entry = it.next();
        JMXAggregatorBean bean = entry.getValue();
        Iterator<Entry<String, MBeanProxy>> itDetails = bean.details.entrySet().iterator();
        int size = bean.details.size();
        int count = 0;
        if (LogUtil.getCoreLog().isTraceEnabled())
          LogUtil.getCoreLog().trace("Bean:{} details:{}",entry.getKey(),size);
        ClusterProxy clusterProxy = bean.aggrItem;
        
        if(clusterProxy == null)
        {
          LogUtil.getCoreLog().trace("clusterProxy = bean.aggrItem ,key:{} Bean ClassName: {} ,Details :{}",entry.getKey(), bean.className,bean.details);
          continue;
        }
        
        if (LogUtil.getCoreLog().isTraceEnabled())
        LogUtil.getCoreLog().trace("start process clusterProxy:{},objectName:{}",clusterProxy,clusterProxy.getRemoteObjectName());
        
        HashSet<String> removes = new HashSet<String>();
        clusterProxy.resetValues();
        //遍历所有子节点
        while (itDetails.hasNext())
        {
          count++;
          Entry<String, MBeanProxy> detail = itDetails.next();
          String key = detail.getKey();
          MBeanProxy proxy = detail.getValue();
          JMXBeanInfo beanInfo = proxy.getBeanInfo();
          //掉点的则不再调用,后面计算平均值算法已改,每次都运算平均值
          if(isMemberDeparted(key))
          {
            if (LogUtil.getCoreLog().isTraceEnabled())
              LogUtil.getCoreLog().trace("Do't process clusterProxy isMemberDeparted:{}",key);
            continue;
          }
          try
          {
            proxy.refreshAttributes();
          }
          // 没有找到属性,则删除之
          catch (InstanceNotFoundException e)
          {
            if (LogUtil.getCoreLog().isTraceEnabled())
              LogUtil.getCoreLog().trace("InstanceNotFoundException key:{} error:{}",key,e);
            continue;
          }
          
          if (clusterProxy.isEmpty())
          {
            clusterProxy.setAttributeList(proxy.getAttributeList());
            continue;
          }
          
          if (beanInfo.getFieldInfo() != null)
          {
            if (LogUtil.getCoreLog().isTraceEnabled())
              LogUtil.getCoreLog().trace("Item " + count + " ,field:" + beanInfo.getFieldInfo().size());
            
            for (String name : beanInfo.getAttributeNames())
            {
              JMXBeanFieldInfo fieldInfo = beanInfo.getFieldInfo().get(name);
              if (fieldInfo != null)
              {
                AggregateType aggrType = fieldInfo.getAggrType();
                Object v1 = clusterProxy.getValue(name);
                Object v2 = proxy.getValue(name);
                
                Object v3 = AggregatorHelper.getInstace().doCaculate(v1, v2, aggrType);
                clusterProxy.setValue(name, v3);
              }
            }
          }
          else
          {
            if (LogUtil.getCoreLog().isTraceEnabled())
              LogUtil.getCoreLog().trace("Bean: {} no field need caculate",entry.getKey());
          }
        }
        //更新返回数据
        clusterProxy.updateReturnData();
        if (LogUtil.getCoreLog().isTraceEnabled())
          LogUtil.getCoreLog().trace("finish process clusterProxy :{}, objectName: {} end,rs: {}",clusterProxy,clusterProxy.getRemoteObjectName(),clusterProxy.getReturnvalues());
        
        Iterator<String> rit = removes.iterator();
        while (rit.hasNext())
        {
          // 删除
          bean.details.remove(rit.next());
        }
        
      }
      if (LogUtil.getCoreLog().isTraceEnabled())
        LogUtil.getCoreLog().trace("Process aggregator end.");
    }
  }
  
  class MgmRegionListener extends CacheListenerAdapter<String, Object>
  {
    @Override
    public void afterCreate(EntryEvent<String, Object> e)
    {
      LogUtil.getCoreLog().debug("New config event," + e.getKey() + " " + e.getNewValue());
      Object obj = e.getNewValue();
      
      if (obj instanceof GemliteNodeConfig)
      {
        GemliteNodeConfig conf = (GemliteNodeConfig) obj;
        memberDeparted.put(conf.getMemberKey(), false);
        LogUtil.getCoreLog().debug("Member join,memberId:" + conf.getMemberId() + " memberKey:" + conf.getMemberKey());
        synchronized (ATTR_LOCK)
        {
          createProxyBean(conf);
        }
      }
    }
    
    @Override
    public void afterUpdate(EntryEvent<String, Object> e)
    {
      LogUtil.getCoreLog().debug("Update config event," + e.getKey() + " " + e.getNewValue());
      Object obj = e.getNewValue();
      if (obj instanceof GemliteNodeConfig)
      {
        GemliteNodeConfig conf = (GemliteNodeConfig) obj;
        synchronized (ATTR_LOCK)
        {
          createProxyBean(conf);
        }
      }
    }
  }
  
  class MemberChangeListener implements MembershipListener
  {
    
    @Override
    public void memberJoined(InternalDistributedMember member)
    {
      if (LogUtil.getCoreLog().isInfoEnabled())
        LogUtil.getCoreLog().info("Member Joined,memberId:{}" , member.getId());
    }
    
    @Override
    public void memberDeparted(InternalDistributedMember member, boolean flag)
    {
      String memberKey = memberIdMapping.get(member.getId());
      if (memberKey != null)
        memberDeparted.put(memberKey, true);
      LogUtil.getCoreLog().info("Member departed,memberId: {} memberKey:{}", member.getId(), memberKey);
      // jmxClients.remove(member.getId());
      // Iterator<JMXAggregatorBean> it =
      // cacheProxyByObjectName.values().iterator();
      // while (it.hasNext())
      // {
      // JMXAggregatorBean aggr = it.next();
      // // MBeanProxy proxy = aggr.details.get(memberKey);
      // MBeanHelper.unregister(proxy.detialOName);
      // aggr.details.remove(member.getId());
      // }
      
      // memberIdMapping.remove(member.getId());
    }
    
    @Override
    public void memberSuspect(InternalDistributedMember m1, InternalDistributedMember m2)
    {
    }
    
    @Override
    public void quorumLost(Set<InternalDistributedMember> set, List<InternalDistributedMember> list)
    {
      
    }
    
  }
  
  class ClusterProxy extends AbstractProxy
  {
    public ClusterProxy(ModelMBean originBean)
    {
      super(originBean);
    }
    
    public Object remoteCall(ObjectName oname, String methodName, Object[] params, String[] signature)
    {
      if(LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().info("Start Remote call methodName:{} Call One {}",methodName);
      if (!supportOperation(methodName))
      {
        if(LogUtil.getCoreLog().isDebugEnabled())
          LogUtil.getCoreLog().info("methodName: {} is not a SupportOperation!",methodName);
		  return "";
      }
      Set<Object> list = new HashSet<>();
      Iterator<JMXClientInfo> it = GemliteManagerNode.getInstance().jmxClients.values().iterator();
      AggregateOperation ao = getBeanInfo().getOperInfo().get(methodName);
      while (it.hasNext())
      {
        JMXClientInfo ci = it.next();
        // 判断此点是否已经挂掉,如果是则不调用
        if (isMemberDeparted(ci.conf.getMemberKey()))
        {
          continue;
        }
        try
        {
          Object obj = ci.connector.getMBeanServerConnection().invoke(getRemoteObjectName(), methodName, params,
              signature);
          list.add(obj);
        }
        catch (Exception e)
        {
          LogUtil.getCoreLog().error("{}:{}/{}  methodName:{} error:{}",ci.conf.getIp(),ci.conf.getPort(),ci.conf.getNodeName(),methodName,e);
        }
        // 如果是仅在一个点上执行查询的话,则break
        if (AggregateType.OPONLYONE.equals(ao.value()))
          break;
      }
      
      if(LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().info("Remote call methodName: {} type:{} result size:{}",methodName,ao.value(),list.size());
      
      if (list.size() == 1)
        return list.iterator().next();
      
      // 合并list
      ArrayList<Object> rs = new ArrayList<Object>();
      Iterator<Object> remoteit = list.iterator();
      while (remoteit.hasNext())
      {
        Object obj = remoteit.next();
        if (obj instanceof List)
        {
          List l = (List) obj;
          for (int i = 0; i < l.size(); i++)
          {
            rs.add(l.get(i));
          }
        }
        else
        {
          rs.add(obj);
        }
      }
      return rs;
    }
  }
  
  class MBeanProxy extends AbstractProxy
  {
    public MBeanProxy(ModelMBean originBean, JMXClientInfo clientInfo, String detailOName)
    {
      super(originBean);
      this.clientInfo = clientInfo;
      this.detialOName = detailOName;
    }
    
    String detialOName;
    JMXClientInfo clientInfo;
    String memberKey;// ip:nodeName
    
    public void refreshAttributes() throws InstanceNotFoundException
    {
      if (isMemberDeparted(memberKey))
      {
        return;
      }
      try
      {
        String[] attrNames = getBeanInfo().getAttributeNames();
        AttributeList attrList = clientInfo.connector.getMBeanServerConnection().getAttributes(getRemoteObjectName(),
            attrNames);
        
        if (attrList.size() == getBeanInfo().getAttributeNames().length)
        {
          setAttributeList(attrList);
        }
        else
        {
          //有部分属性获取失败的时候,报出info信息,并且只更新获取到的属性信息
          List<String> missing = new ArrayList<String>(Arrays.asList(attrNames));
          for (Attribute a : attrList.asList())
            missing.remove(a.getName());
          LogUtil.getCoreLog().info(
              "refreshAttributes unsuccessfully ,Did not retrieve: {} waitting next refreshAttributes operation ...",
              missing);
          setValues(attrList);
        }
      }
      // 如果找不到,则删除之后
      catch (InstanceNotFoundException e)
      {
        LogUtil.getCoreLog().trace(
            "refreshAttributes InstanceNotFoundException: RemoteObjectName {} AttributeNames {} error:{}",
            getRemoteObjectName(), getBeanInfo().getAttributeNames(), e.getMessage());
      }
      catch (IOException e)
      {
        LogUtil.getCoreLog().trace("refreshAttributes IOException: RemoteObjectName {} AttributeNames {} error:{}",
            getRemoteObjectName(), getBeanInfo().getAttributeNames(), e.getMessage());
      }
      catch (Exception e)
      {
        LogUtil.getCoreLog().trace("refreshAttributes OtherException: RemoteObjectName {} AttributeNames {} error:{}",
            getRemoteObjectName(), getBeanInfo().getAttributeNames(), e.getMessage());
      }
    }
    
    public Object remoteCall(ObjectName oname, String methodName, Object[] params, String[] signature)
    {
      if (isMemberDeparted(memberKey))
      {
        return null;
      }
      try
      {
        return clientInfo.connector.getMBeanServerConnection().invoke(getRemoteObjectName(), methodName, params,
            signature);
      }
      catch (InstanceNotFoundException | MBeanException | ReflectionException | IOException e)
      {
        LogUtil.getCoreLog().error("remoteCall error:{}", e);
      }
      return null;
    }
    
  }
  
  class JMXAggregatorBean
  {
    public String className;
    public ClusterProxy aggrItem;
    public Map<String, MBeanProxy> details;
    
    public JMXAggregatorBean()
    {
      details = new ConcurrentHashMap<>();
    }
  }
  
  @ManagedResource
  public class ManagerBean
  {
    @ManagedOperation
    public void startMonitor()
    {
      startValueMonitor();
    }
    
    @ManagedOperation
    public void stopMonitor()
    {
      stopValueMonitor();
    }
    
    @ManagedOperation
    public void statusMonitor()
    {
      
    }
    
    @ManagedAttribute
    public int getRefreshInterval()
    {
      return refreshInterval;
    }
    
    @ManagedOperation
    public void changeRefreshInterval(int inval)
    {
      refreshInterval = inval;
      exec.shutdownNow();
      exec = (ScheduledExecutorService) Executors.newSingleThreadScheduledExecutor();
      exec.scheduleAtFixedRate(new RefreshThread(), 1, GemliteManagerNode.refreshInterval, TimeUnit.SECONDS);
    }
  }
  
  public final static String JMX_TYPE_DESC = "L" + AggregateType.class.getName().replaceAll("\\.", "\\/") + ";";
  
  public final static String MGM_REGION_NAME = "_GEMLITE_MGM_";
  
  private static GemliteManagerNode inst = new GemliteManagerNode();
  /***
   * key:beanName
   * value:Local mbean
   */
  private Map<String, JMXAggregatorBean> cacheProxyByObjectName = new ConcurrentHashMap<>();
  /***
   * key:className
   */
  private Map<String, JMXBeanInfo> cacheBeanInfoByClassName;
  
  private ScheduledExecutorService exec;
  private Map<String, JMXClientInfo> jmxClients = new ConcurrentHashMap<>();
  private Map<String, String> memberIdMapping = new ConcurrentHashMap<>();
  private Map<String, Boolean> memberDeparted = new ConcurrentHashMap<>();
  private Region mgmRegion;
  private int rmiPort;
  private int htmlPort;
  
  private GemliteManagerNode()
  {
    cacheBeanInfoByClassName = new HashMap<>();
    exec = (ScheduledExecutorService) Executors.newSingleThreadScheduledExecutor();
  }
  
  public final static GemliteManagerNode getInstance()
  {
    return inst;
  }
  
  public static void main(String[] args)
  {
    GemliteManagerNode.getInstance().startManagerAndLocator();
  }
  
  public void waitForComplete()
  {
    while (true)
    {
      if (!CacheFactory.getAnyInstance().isClosed())
      {
        try
        {
          Thread.sleep(500L);
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
        continue;
      }
      break;
    }
    System.out.println("cacheserver stopped.\n");
  }
  
  private void validatePort()
  {
    ServerConfigHelper.initConfig(TYPES.LOCATOR);
    rmiPort = ServerConfigHelper.getInteger("config.jmx.port");
    htmlPort = ServerConfigHelper.getInteger("config.html.port");
  }
  
  public void startJMXManager()
  {
    validatePort();
    GemFireCacheImpl cache = (GemFireCacheImpl) CacheFactory.getAnyInstance();
    cache.getDistributedSystem().getDistributionManager().addMembershipListener(new MemberChangeListener());
    // FunctionService.registerFunction(new GetJmxConfigsFunction());
    try
    {
      LogUtil.getCoreLog().info("Gemfire cluser connected. ");
      createProxyBeans();
      LogUtil.getCoreLog().info("Proxy bean created.");
      
      MBeanHelper.registerModelMBean("Gemlite:type=Manager", new ManagerBean());
      startValueMonitor();
      LogUtil.getCoreLog().info("Monitor started.");
      GemliteAgent.getInstance().startHtmlAdapter(htmlPort);
      GemliteAgent.getInstance().startRMIConnector(rmiPort);
      
      ManagementRegionHelper.setValue(MGM_KEYS.jmx_host, ServerConfigHelper.getConfig(ITEMS.BINDIP));
      ManagementRegionHelper.setValue(MGM_KEYS.jmx_rmi_port, rmiPort);
      ManagementRegionHelper.setValue(MGM_KEYS.jmx_http_port, htmlPort);
      ManagementRegionHelper.monitorJMXConfig(new MgmRegionListener());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
  
  public void startManagerAndLocator()
  {
    ServerConfigHelper.initConfig();
    ServerConfigHelper.setProperty("bind-address", ServerConfigHelper.getConfig(ITEMS.BINDIP));
    ServerConfigHelper.initLog4j("log4j-jmx.xml");
    ClassPathXmlApplicationContext ctx = Util.initContext("jmx-manager.xml");
    startJMXManager();
    waitForComplete();
    ctx.close();
  }
  
  /***
   * 创建一个代理Bean，与已注册的Bean相对应，显示统计数据，发布统一的操作命令；
   * 相同Class的代理bean只创建一个；
   * 多个节点的bean轮流处理
   * 解析每个bean的属性，平均值、合计值、最大值、最小值；
   * 
   * @param conf
   * @return
   * @throws MalformedObjectNameException
   * @throws IOException
   * @throws InstanceNotFoundException
   * @throws NoSuchMethodException
   * @throws ClassNotFoundException
   */
  protected JMXClientInfo createAggregatorBean(GemliteNodeConfig conf, JMXClientInfo client) throws Exception
  {
    LogUtil.getCoreLog().trace("Create bean from {},beanNames {}",conf.getMemberId(),conf.getBeanNames().keySet());
    Iterator<Entry<String, String>> it = conf.getBeanNames().entrySet().iterator();
    while (it.hasNext())
    {
      Entry<String, String> entry = it.next();
      String objectName = entry.getKey();
      String className = entry.getValue();
      Class originClass = Class.forName(className);
      ObjectName remoteObjectName = new ObjectName(objectName);
      // 每种类型，创建一个合并的代理类
      JMXAggregatorBean aggregator = cacheProxyByObjectName.get(objectName);
      if (aggregator == null)
      {
        aggregator = new JMXAggregatorBean();
        cacheProxyByObjectName.put(objectName, aggregator);
        // aggregator.client = client;
        aggregator.className = className;
      }
      try
      {
        String detailOName = objectName + ",memberId=" + client.conf.getIp() + "/" + client.conf.getNodeName();
        MBeanProxy proxy = createDetailProxyBean(originClass, detailOName, client);
        if(LogUtil.getCoreLog().isTraceEnabled())
          LogUtil.getCoreLog().trace("Finish createDetailProxyBean MBeanProxy:{} objectName:{}",proxy,detailOName);
        JMXBeanInfo beanInfo = cacheBeanInfoByClassName.get(className);
        if (beanInfo == null)
        {
          beanInfo = staticClassInfo(originClass, proxy.getOriginMBean());
          cacheBeanInfoByClassName.put(className, beanInfo);
        }
        proxy.setBeanInfo(beanInfo);
        proxy.setRemoteObjectName(remoteObjectName);
        proxy.refreshAttributes();
        aggregator.details.put(client.conf.getMemberKey(), proxy);
        
        if(aggregator.aggrItem == null)
        {
          ClusterProxy aggrItem = createClusterProxyBean(className, objectName);
          aggrItem.setBeanInfo(beanInfo);
          //默认属性值为其中一个点的数据
          //aggrItem.setAttributeList(proxy.getAttributeList());
          aggregator.aggrItem = aggrItem;
          if(LogUtil.getCoreLog().isTraceEnabled())
            LogUtil.getCoreLog().trace("Finish createClusterProxyBean ClusterProxy:{} objectName:{}",aggrItem,objectName);
        }
      }
      catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchFieldException e)
      {
        LogUtil.getCoreLog().error("proxyName:{},error msg:{}", objectName, e);
      }
    }
    return client;
  }
  
  private ClusterProxy createClusterProxyBean(String className, String proxyName) throws InstantiationException,
      IllegalAccessException, ClassNotFoundException, MalformedObjectNameException
  {
    Object originItem = Class.forName(className).newInstance();
    ModelMBean mmb = MBeanHelper.createModelMBean(originItem);
    ClusterProxy handler = new ClusterProxy(mmb);
    ModelMBean mbean = (ModelMBean) Proxy.newProxyInstance(null, new Class[] { ModelMBean.class }, handler);
    MBeanHelper.registerModelMBean(proxyName, mbean);
    handler.setRemoteObjectName(new ObjectName(proxyName));
    if (LogUtil.getCoreLog().isTraceEnabled())
      LogUtil.getCoreLog().trace("createClusterProxyBean==:{} ObjectName:{}",mbean,proxyName);
    return handler;
  }
  
  /***
   * @param detailOName
   * @param beanInfo
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   * @throws SecurityException
   * @throws NoSuchFieldException
   * @throws NoSuchMethodException
   */
  private MBeanProxy createDetailProxyBean(Class originClass, String detailOName, JMXClientInfo client)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException,
      SecurityException, NoSuchMethodException
  {
    Object originItem = originClass.newInstance();
    ModelMBean mmb = MBeanHelper.createModelMBean(originItem);
    MBeanProxy proxy = new MBeanProxy(mmb, client, detailOName);
    proxy.memberKey = client.conf.getMemberKey();
    Object detailItem = Proxy.newProxyInstance(null, new Class[] { ModelMBean.class }, proxy);
    MBeanHelper.registerModelMBean(detailOName, detailItem);
    if (LogUtil.getCoreLog().isDebugEnabled())
      LogUtil.getCoreLog().debug("createDetailProxyBean==:{} detailOName:{}", detailItem, detailOName);
    return proxy;
  }
  
  /***
   * 1.遍历ManagedAttribute和ManagedOperation
   * 2.取AggregateAttribute和AggregrateOperation标注，记录
   * 
   * @param originItem
   * @param mmb
   * @param beanInfo
   * @throws NoSuchFieldException
   * @throws SecurityException
   * @throws NoSuchMethodException
   */
  private JMXBeanInfo staticClassInfo(Class originClass, ModelMBean mmb) throws Exception
  {
    JMXBeanInfo beanInfo = new JMXBeanInfo();
    MBeanInfo inf = mmb.getMBeanInfo();
    List<String> anList = new ArrayList<>();
    for (MBeanAttributeInfo attr : inf.getAttributes())
    {
      anList.add(attr.getName());
      String getMethodName = (String) attr.getDescriptor().getFieldValue("getMethod");
      Method m = originClass.getMethod(getMethodName);
      AggregateAttribute aa = m.getAnnotation(AggregateAttribute.class);
      AggregateType t = aa != null ? aa.value() : AggregateType.SAME;
      JMXBeanFieldInfo fi = new JMXBeanFieldInfo(t, attr.getName(), getMethodName);
      beanInfo.getFieldInfo().put(attr.getName(), fi);
    }
    String[] names = new String[anList.size()];
    names = anList.toArray(names);
    beanInfo.setAttributeNames(names);
    
    for (MBeanOperationInfo op : inf.getOperations())
    {
      Method m = null;
      MBeanParameterInfo[] sig = op.getSignature();
      Class<?>[] parameterTypes = new Class<?>[sig.length];
      for (int i = 0; i < sig.length; i++)
      {
        MBeanParameterInfo mbeanParam = sig[i];
        Class<?> paramterType = ClassTypeMapping.get(mbeanParam.getType());
        if (paramterType == null)
          parameterTypes[i] = Class.forName(mbeanParam.getType());
        else
          parameterTypes[i] = paramterType;
      }
      m = originClass.getMethod(op.getName(), parameterTypes);
      AggregateOperation ao = m.getAnnotation(AggregateOperation.class);
      // 如果不支持 ao为空或support == false
      beanInfo.getOperInfo().put(op.getName(), ao);
    }
    return beanInfo;
  }
  
  public void startValueMonitor()
  {
    exec.scheduleAtFixedRate(new RefreshThread(), 1, GemliteManagerNode.refreshInterval, TimeUnit.SECONDS);
  }
  
  public void stopValueMonitor()
  {
    exec.shutdownNow();
  }
  
  public boolean isMemberDeparted(String memberKey)
  {
    Boolean bl = memberDeparted.get(memberKey);
    return bl != null ? bl.booleanValue() : false;
  }
  
  private List<GemliteNodeConfig> getJmxConfigs()
  {
    Cache cache = CacheFactory.getAnyInstance();
    Set<DistributedMember> mms = cache.getMembers();
    if (mms.size() == 0)
      return null;
    Execution ex = FunctionService.onMembers(mms);
    List<GemliteNodeConfig> list = (List<GemliteNodeConfig>) ex.execute(new GetJmxConfigsFunction().getId())
        .getResult();
    return list;
  }
  
  /***
   * 1.取管理region
   */
  protected void createProxyBeans()
  {
    List<GemliteNodeConfig> configs = getJmxConfigs();
    if (configs == null)
      return;
    LogUtil.getCoreLog().debug("keys:" + configs.size());
    
    Iterator<GemliteNodeConfig> it = (Iterator<GemliteNodeConfig>) configs.iterator();
    while (it.hasNext())
    {
      GemliteNodeConfig conf = it.next();
      if (conf == null)
        continue;
      createProxyBean(conf);
    }
  }
  
  /***
   * key: conf.ip:conf.nodeName
   * 保存memberId 到 key的映射，用于节点离线处理
   * 
   * @param conf
   */
  private void createProxyBean(GemliteNodeConfig conf)
  {
    LogUtil.getCoreLog().info("Create proxy bean:{}", conf.toString());
   
    try
    {
      JMXClientInfo client = jmxClients.get(conf.getMemberKey());
      if (client == null)
      {
        memberIdMapping.put(conf.getMemberId(), conf.getMemberKey());
        client = new JMXClientInfo();
        String jndiPath = "/jmxconnector";
        
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://" + conf.getIp() + "/jndi/rmi://" + conf.getIp() + ":"
            + conf.getPort() + jndiPath);
        JMXConnector connector = JMXConnectorFactory.connect(url);
        client.connector = connector;
        client.conf = conf;
        jmxClients.put(conf.getMemberKey(), client);
      }
      createAggregatorBean(conf, client);
      LogUtil.getCoreLog().info("Finish createProxyBean key:{} ",conf.getMemberKey());
      
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error("Process notification:" + conf, e);
    }
  }
}
