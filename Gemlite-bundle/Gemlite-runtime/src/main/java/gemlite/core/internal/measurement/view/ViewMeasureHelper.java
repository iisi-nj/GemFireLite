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
package gemlite.core.internal.measurement.view;

import gemlite.core.internal.view.GemliteViewContext;
import gemlite.core.internal.view.bean.ViewItem;
import gemlite.core.util.LogUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.NumberUtils;

public class ViewMeasureHelper
{
	  private static int queueSize = NumberUtils.toInt(System.getProperty("gemlite.core.view.measure.queue-size", "1024"));
	  
	  private static ArrayBlockingQueue<AbstractViewStatItem> measureQueue = new ArrayBlockingQueue<>(queueSize);
	  
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
	        	AbstractViewStatItem aisi = measureQueue.take();
	        	AbstractViewStat ais = getViewStat(aisi.getViewName());
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
	    	  GemliteViewContext context = GemliteViewContext.getInstance();
	    	  Map<String, ViewItem> viewsMap = context.getViewContext();
	    	  
	    	  Iterator<ViewItem> it = viewsMap.values().iterator();
	    	  while(it.hasNext())
	    	  {
	    		  ViewItem viewItem = it.next();
	    		  if(viewItem != null)
	    		  {
	    			  AbstractViewStat rs =(AbstractViewStat) viewItem.getMbean();
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
	      LogUtil.getJMXLog().info("View monitor task started.");
	}
	
	public final static AbstractViewStat getViewStat(String viewName)
	{
		GemliteViewContext idc = GemliteViewContext.getInstance();
		ViewItem item = idc.getViewItem(viewName);
		if(item != null)
		{
			AbstractViewStat rss = (AbstractViewStat) item.getMbean();
			return rss;
		}

	    return null;
	    
	}
	

	public final static void getViewDataEnd(AbstractViewStatItem item, long start, long end)
	{
	    if (item != null)
	    {
	    	measureQueue.offer(item);
	    }
	  }
	  
	
}
