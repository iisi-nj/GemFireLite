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
package gemlite.core.internal.measurement.index;

import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.util.LogUtil;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;

public class IndexMeasureHelper
{
	  private static int queueSize = NumberUtils.toInt(System.getProperty("gemlite.core.index.measure.queue-size", "1024"));
	  
	  private static ArrayBlockingQueue<AbstractIndexStatItem> measureQueue = new ArrayBlockingQueue<>(queueSize);
	  
	  private static MeasureMonitorTask monitorTask;
	  
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
	        	AbstractIndexStatItem aisi = measureQueue.take();
	        	AbstractIndexStat ais = getIndexStat(aisi.getIndexName());
	        	ais.recordItem(aisi);
	          
	          // 写日志
	          if (LogUtil.getJMXLog().isDebugEnabled())
	            LogUtil.getJMXLog().debug(aisi.toString());
	          
	        }
	        catch (Exception e)
	        {
	          e.printStackTrace();
	        }
	      }
	      
	      if (LogUtil.getJMXLog().isInfoEnabled())
	        LogUtil.getJMXLog().info("Measure monitor task stopped.");
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
	    	  GemliteIndexContext idc = GemliteContext.getTopIndexContext();
	    	  
	    	  Iterator<IIndexContext> it = idc.getIndexContexts();
	    	  while(it.hasNext())
	    	  {
	    		  IIndexContext context = it.next();
	    		  if(context != null)
	    		  {
	    			  AbstractIndexStat rs = (AbstractIndexStat) context.getMbean();
	    			  if(rs!=null)
	    				  rs.calcuteTPS();
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
	    if (LogUtil.getJMXLog().isInfoEnabled())
	      LogUtil.getJMXLog().info("Index monitor task started.");
	}
	
	public final static AbstractIndexStat getIndexStat(String indexName)
	{
		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
		IIndexContext context = idc.getIndexContext(indexName);
		if(context != null)
		{
			AbstractIndexStat rss = (AbstractIndexStat) context.getMbean();
			return rss;
		}

	    return null;
	    
	}
	
	  /***
	   * 获取索引数据调用完成时，将本次记录的数值归档
	   */
	public final static void getIndexDataEnd(AbstractIndexStatItem item, long start, long end)
	{
		if (LogUtil.getJMXLog().isDebugEnabled())
			LogUtil.getJMXLog().debug("Record getIndexData:" + "-"+item.getRegionName()+"-"+item.getIndexName() + " start:" + start + " end:" + end);
	    if (item != null)
	    {
	    	measureQueue.offer(item);
	    }
	  }
	  
	
}
