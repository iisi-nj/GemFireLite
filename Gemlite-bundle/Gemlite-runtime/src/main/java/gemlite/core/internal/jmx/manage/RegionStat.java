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
package gemlite.core.internal.jmx.manage;

import gemlite.core.annotations.mbean.GemliteMBean;
import gemlite.core.internal.db.DBSynchronizer;
import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.jmx.manage.KeyConstants.Regions;
import gemlite.core.internal.support.jmx.AggregateType;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.support.system.WorkPathHelper;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;

import com.gemstone.gemfire.cache.AttributesMutator;
import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.DiskStoreFactory;
import com.gemstone.gemfire.cache.PartitionAttributes;
import com.gemstone.gemfire.cache.PartitionResolver;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionAttributes;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventQueue;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventQueueFactory;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;

/**
 * shell取region名
 * @author GSONG
 * 2015年1月19日
 */
@SuppressWarnings("rawtypes")
@GemliteMBean(name="RegionManager", config=true)
@ManagedResource
public class RegionStat
{
    private Map<String,String> queuesMap = new HashMap<String, String>();//记录当前region挂载的队列
    private Set<String> diskStores = new HashSet<String>();  //存放已经创建的diskStore名称
    private Set<String> queueStores = new HashSet<String>();   //存入所有已经创建的队列名称
    
    @ManagedOperation
    @AggregateOperation(value=AggregateType.OPONLYONE)
    public List<String> listRegions()
    {
        List<String> regions = new ArrayList<String>();
        Set<Region<?,?>> set = CacheFactory.getAnyInstance().rootRegions();
        if(set!=null)
        {
            Iterator<Region<?, ?>> it = set.iterator();
            while(it.hasNext())
            {
                Region<?,?> r = it.next();
                regions.add(r.getName());
            }            
        }
        return regions;
    }
    
    
    @ManagedOperation
    @AggregateOperation
    public List<HashMap<String,Object>> listRegionDetails()
    {
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        CacheFactory.getAnyInstance().rootRegions();
        Set<Region<?,?>> set = CacheFactory.getAnyInstance().rootRegions();
        if(set!=null)
        {
            Iterator<Region<?, ?>> it = set.iterator();
            while(it.hasNext())
            {
                HashMap<String,Object> map = new HashMap<String,Object>();
                list.add(map);
                Region<?,?> r = it.next();
                map.put(Regions.regionName.name(), r.getName());
                map.put(Regions.size.name(), r.size());
                boolean isP = PartitionRegionHelper.isPartitionedRegion(r); 
                map.put(Regions.regionType.name(), isP?"partitioned-region":"replicated-region");
                IMapperTool tool = DomainRegistry.getMapperTool(r.getName());
                map.put(Regions.asyncqueue.name(), queuesMap.containsKey(r.getName())?queuesMap.get(r.getName()):"");
                if(tool == null)
                {
                  LogUtil.getCoreLog().error("IMapperTool is null region : {}",r.getName());
                  continue;
                }
                map.put(Regions.keyClass.name(), tool.getKeyClass().getName());
                map.put(Regions.valueClass.name(), tool.getValueClass().getName());
                map.put(Regions.keyFields.name(), tool.getKeyFieldNames());
                map.put(Regions.valueFields.name(), tool.getValueFieldNames());
            }         
        }
        
        //排个序
        Collections.sort(list, new Comparator<HashMap<String,Object>>()
        {
            public int compare(HashMap<String,Object> o1,HashMap<String,Object> o2)
            {
                String r1 = (String)o1.get(Regions.regionName.name());
                String r2 = (String)o2.get(Regions.regionName.name());
                return r1.compareTo(r2);
            }
        });
        
        return list;
    }
    
    
    @ManagedOperation
    @AggregateOperation
    public HashMap<String,Object> describeRegion(String regionName)
    {
        HashMap<String,Object> map = new LinkedHashMap<String,Object>();
        Region r = CacheFactory.getAnyInstance().getRegion(regionName);
        if(r!=null)
        {
            IMapperTool tool = DomainRegistry.getMapperTool(r.getName());
            if(tool == null)
                return map;
            map.put(Regions.regionName.name(), r.getName());
            boolean isP = PartitionRegionHelper.isPartitionedRegion(r); 
            map.put(Regions.regionType.name(), isP?"partitioned-region":"replicated-region");
            map.put(Regions.size.name(), r.size());
            map.put(Regions.keyClass.name(), tool.getKeyClass().getName());
            map.put(Regions.valueClass.name(), tool.getValueClass().getName());
            map.put(Regions.keyFields.name(), tool.getKeyFieldNames());
            map.put(Regions.valueFields.name(), tool.getValueFieldNames());
            RegionAttributes atts = r.getAttributes();
            map.put(Regions.statisticsEnabled.name(), atts.getStatisticsEnabled());
            map.put(Regions.cloningEnabled.name(), atts.getCloningEnabled());
            map.put(Regions.asyncqueue.name(), queuesMap.containsKey(r.getName())?queuesMap.get(r.getName()):"");
            CacheListener[] listeners = atts.getCacheListeners();
            String lis = ",";
            for(CacheListener li:listeners)
            {
                lis+=li.getClass().getName();
            }
            lis = lis.substring(1);
            map.put(Regions.cacheListeners.name(), lis);
            
            //判断是否为partiotion region
            if(isP)
            {
                PartitionAttributes pa = atts.getPartitionAttributes();
                PartitionResolver resolver = pa.getPartitionResolver();
                map.put(Regions.partitionResolver.name(), resolver.getName());
                long recoverydelay = pa.getRecoveryDelay();
                map.put(Regions.recoveryDelay.name(), recoverydelay);
                map.put(Regions.redundantCopies.name(),pa.getRedundantCopies());
                map.put(Regions.localMaxMemory.name(),pa.getLocalMaxMemory());
                map.put(Regions.colocatedWith.name(),pa.getColocatedWith());
                map.put(Regions.totalMaxMemory.name(),pa.getTotalMaxMemory());
                map.put(Regions.totalNumBuckets.name(),pa.getTotalNumBuckets());
            }
        }
        return map;
    }
    
    @ManagedOperation
    @AggregateOperation
    public String addSync(String regionName,String queueId,boolean persistent,String diskStoreName,String driver,String url,String user,String password)
    {
        //判断是否已经创建过queuueId的队列,如果创建了,则不能重名
        //TODO 如果彻底清除此队?
        if(queueStores.contains(queueId))
        {
            String s = queueId+" repeat,please choose another queueId!";
            LogUtil.getCoreLog().warn(s);
            return s;
        }
        
        Assert.notNull(regionName, "regionName is null!it must has a value");
        Assert.notNull(queueId, "queueId is null!it must has a value");
        Cache cache = CacheFactory.getAnyInstance();
        Region r = cache.getRegion(regionName);
        AttributesMutator mutator = r.getAttributesMutator();
        //判断当前region是否有同步队列
        if(queuesMap.containsKey(regionName))
        {
            mutator.removeAsyncEventQueueId(queueId);
            
            AsyncEventQueue queue = cache.getAsyncEventQueue(queueId);
            if(queue!=null && cache.getAsyncEventQueues().contains(queue))
             cache.getAsyncEventQueues().remove(queue);
            
            queuesMap.remove(regionName);
        }
        
        
        //创建队列
        AsyncEventQueueFactory factory = cache.createAsyncEventQueueFactory();
        //持久化
        factory.setPersistent(persistent);
        DiskStoreFactory disFac = cache.createDiskStoreFactory();
        WorkPathHelper helper = new WorkPathHelper();
        if(!diskStores.contains(diskStoreName))
        {
            String dir = helper.diskStore(diskStoreName);
            disFac.setDiskDirs(new File[]{new File(dir)});
            disFac.setAllowForceCompaction(true);
            disFac.setAutoCompact(true);
            disFac.setCompactionThreshold(90);
            disFac.setMaxOplogSize(1000);
            disFac.setQueueSize(10000);
            disFac.setTimeInterval(1000);
            disFac.setWriteBufferSize(32768);
            disFac.create(diskStoreName);
            diskStores.add(diskStoreName);
        }
        factory.setDiskStoreName(diskStoreName);
//        factory.setParallel(parallel); 有此项时队列创建无效
        factory.setBatchSize(1000);
        factory.setBatchTimeInterval(1000);
        factory.setMaximumQueueMemory(102400);
        DBSynchronizer listener = new DBSynchronizer();
        listener.init(driver, url, user, password);
        AsyncEventQueue asyncQueue = factory.create(queueId, listener);
        if(LogUtil.getCoreLog().isInfoEnabled())
            LogUtil.getCoreLog().info("create asyncQueue:"+asyncQueue.toString());
        //记录列名
        queueStores.add(queueId);
        //将队列指定给region
        mutator.addAsyncEventQueueId(queueId);
        //数据存入
        queuesMap.put(regionName, queueId);
        return "server:"+ServerConfigHelper.getConfig(ITEMS.BINDIP)+" node:"+ServerConfigHelper.getConfig(ITEMS.NODE_NAME)+" add async queue "+ queueId +" successfully!";
    }
    
    
    @ManagedOperation
    @AggregateOperation
    public String removeSync(String regionName,String queueId)
    {
        Assert.notNull(queueId, "queueId is null!it must has a value");
        Cache cache = CacheFactory.getAnyInstance();
        Region r = cache.getRegion(regionName);
        AttributesMutator mutator = r.getAttributesMutator();
        if(queuesMap.containsKey(regionName))
        {
            mutator.removeAsyncEventQueueId(queueId);
            queuesMap.remove(regionName);
            return "remove sync queue "+queueId+ " success!";
        }
        return "remove sync queue "+queueId+ "fail , reseaon : not found!";
    }
    
    
    @ManagedOperation
    @AggregateOperation
    public List<HashMap<String,Object>> listSync()
    {
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        Cache cache = CacheFactory.getAnyInstance();
        Set<AsyncEventQueue> queues = cache.getAsyncEventQueues();
        for(AsyncEventQueue queue:queues)
        {
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("queueid", queue.getId());
            map.put("ip",ServerConfigHelper.getConfig(ITEMS.BINDIP)+":"+ServerConfigHelper.getConfig(ITEMS.NODE_NAME));
            //添加队列信息基本信息
            map.put("primary",queue.isPrimary());
//            map.put("parallel",queue.isParallel());
            map.put("persistent",queue.isPersistent());
            map.put("diskstore",queue.getDiskStoreName());
            map.put("batchsize",queue.getBatchSize());
            map.put("batchtimeinterval",queue.getBatchTimeInterval());
            map.put("queueSize",queue.size());  
            AsyncEventListener listener = queue.getAsyncEventListener();
            String lis = listener==null?"":listener.getClass().getName();
            map.put("listener", lis);
            map.put("maxmemory", queue.getMaximumQueueMemory());
            list.add(map);
        }
        return list;
    }
    
    @ManagedOperation
    @AggregateOperation
    public String descSync(String queueId)
    {
        Cache cache = CacheFactory.getAnyInstance();
        Set<AsyncEventQueue> queues = cache.getAsyncEventQueues();
        StringBuilder sb = new StringBuilder();
        for(AsyncEventQueue queue:queues)
        {
            //如果存在,则删除
            if(StringUtils.equals(queue.getId(),queueId))
            {
                //首先添加节点信息
                sb.append("-----------------\n");
                sb.append(ServerConfigHelper.getConfig(ITEMS.BINDIP)).append(ServerConfigHelper.getConfig(ITEMS.NODE_NAME)).append("\n");
                //添加队列信息基本信息
                sb.append("QueueId:").append(queue.getId()).append("\n");
                sb.append("Primary:").append(queue.isPrimary()).append("\n");
//                sb.append("Parallel:").append(queue.isParallel()).append("\n");
                sb.append("Persistent:").append(queue.isPersistent()).append("\n");
                sb.append("DiskStore:").append(queue.getDiskStoreName()).append("\n");
                sb.append("BatchSize:").append(queue.getBatchSize()).append("\n");
                sb.append("BatchTimeInterval:").append(queue.getBatchTimeInterval()).append("\n");
                sb.append("QueueSize:").append(queue.size()).append("\n");
                break;
            }
        }
        return sb.toString();
    }

    public Map<String, String> getQueuesMap()
    {
        return queuesMap;
    }

    public void setQueuesMap(Map<String, String> queuesMap)
    {
        this.queuesMap = queuesMap;
    }
}
