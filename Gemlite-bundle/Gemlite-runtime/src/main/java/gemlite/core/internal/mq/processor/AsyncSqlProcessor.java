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
package gemlite.core.internal.mq.processor;

import gemlite.core.internal.mq.MqReceiveException;
import gemlite.core.internal.mq.MqThreadPool;
import gemlite.core.internal.mq.domain.ItemByRegion;
import gemlite.core.internal.mq.domain.ParseredValue;
import gemlite.core.internal.mq.parser.ITableParser;
import gemlite.core.internal.mq.parser.ParserTask;
import gemlite.core.internal.mq.sender.IDomainSender;
import gemlite.core.internal.mq.sender.SenderTask;
import gemlite.core.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/****
 * 
 * @author ynd
 *         1.接受1-200个包
 *         2.解析sql，转为对象
 *         3.按key处理，结构为{key:list}，list中对象按timeStamp排序
 *         4.考虑主键变更的情况
 */

public class AsyncSqlProcessor implements IMessageProcessor
{
  protected ITableParser parser;
  protected IMapperDao dao;
  protected IDomainSender sender;
  
  protected ExecutorCompletionService<Boolean> senderService;
  protected List<Future<List<ParseredValue>>> parserTasks;
  
  public AsyncSqlProcessor(ITableParser parser, IMapperDao dao, IDomainSender sender)
  {
    this.parser = parser;
    this.dao = dao;
    this.sender = sender;
    senderService = new ExecutorCompletionService<>(MqThreadPool.mqParserTaskPool);
    parserTasks = new ArrayList<>();
  }
  
  @Override
  public void parserOneMessage(String msg)
  {
    ParserTask task = new ParserTask(msg, parser);
    Future<List<ParseredValue>> future = MqThreadPool.mqParserTaskPool.submit(task);
    parserTasks.add(future);
  }
  
  /**
   * 处理SQL
   * 
   * @param routeKey
   * @param pvList
   * @param clientCache
   */
  @Override
  public void remoteProcess()
  {
    try
    {
      List<ParseredValue> pvList = waitParserComplete();
      if (LogUtil.getMqSyncLog().isDebugEnabled())
        LogUtil.getMqSyncLog().debug("ParseredValue size:" + pvList.size());
      // 如果消息列表为空,则返回不做
      if (pvList.size() == 0)
      {
        return;
      }
      Map<String, ItemByRegion> map = mergeDomainByRegion(pvList);
      sendToServer(map);
    }
    catch (Exception e)
    {
      throw new MqReceiveException(e);
    }
    
  }
  
  /***
   * 等待sql解析任务完成
   * 
   * @return
   * @throws InterruptedException
   * @throws ExecutionException
   */
  private List<ParseredValue> waitParserComplete() throws InterruptedException, ExecutionException
  {
    List<ParseredValue> pvList = new ArrayList<>();
    for (Future<List<ParseredValue>> task : parserTasks)
    {
      List<ParseredValue> subList = task.get();
      pvList.addAll(subList);
    }
	//提取到task list中的所有线程值后，需要清空task list
	parserTasks.clear();
    return pvList;
  }
  
  /***
   * 
   * @param pvList
   * @return
   */
  private Map<String, ItemByRegion> mergeDomainByRegion(List<ParseredValue> pvList)
  {
    if (LogUtil.getMqSyncLog().isDebugEnabled())
      LogUtil.getMqSyncLog().debug("Start merge pvList, [Region,[Key,[MqSyncDomain]] ...");
    Map<String, ItemByRegion> map = new ConcurrentHashMap<>();
    Map<Object, Object> originKeyMap = new HashMap<Object, Object>();
    //MergeTask task = new MergeTask(pvList, dao, 0, pvList.size() - 1, originKeyMap, map);
    MergeTask task = new MergeTask(pvList, dao, 0, pvList.size(), originKeyMap, map);
    MqThreadPool.mqMergeTaskPool.submit(task);
    task.join();
    try
    {
      long l0 = System.currentTimeMillis();
      
      // pool.awaitTermination(5, TimeUnit.SECONDS);
      long l1 = System.currentTimeMillis();
      //System.out.println(">>>>>>" + (l1 - l0) + " map(" + map.values().toString() + ")");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (task.isCompletedAbnormally())
    {
      LogUtil.getMqSyncLog().error(">>", task.getException());
      System.exit(-1);
    }
    return map;
  }
  
  /***
   * @param map
   * @throws Exception
   */
  private void sendToServer(Map<String, ItemByRegion> map) throws Exception
  {
    Iterator<ItemByRegion> it = map.values().iterator();
    while (it.hasNext())
    {
      ItemByRegion item = it.next();
      SenderTask task = new SenderTask(item, sender);
      senderService.submit(task);
    }
    
    Future<Boolean> task = senderService.poll();
    while (task != null)
    {
      Boolean success = task.get();
      task = senderService.poll();
      //System.out.println("task:" + task + " success?" + success);
    }
    
  }
  
}
