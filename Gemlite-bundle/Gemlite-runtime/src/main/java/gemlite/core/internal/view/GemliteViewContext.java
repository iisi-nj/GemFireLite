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
package gemlite.core.internal.view;

import gemlite.core.annotations.view.TriggerOn;
import gemlite.core.annotations.view.TriggerType;
import gemlite.core.annotations.view.View;
import gemlite.core.annotations.view.ViewType;
import gemlite.core.api.index.IndexEntrySet;
import gemlite.core.internal.index.IndexTool;
import gemlite.core.internal.measurement.view.ViewMbeanStat;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.jmx.GemliteNode;
import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.core.internal.view.bean.ViewItem;
import gemlite.core.internal.view.trigger.ViewTool;
import gemlite.core.support.BucketChangeListener;
import gemlite.core.support.DomainResolver;
import gemlite.core.support.EntryChangeListener;
import gemlite.core.util.GemliteHelper;
import gemlite.core.util.LogUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.AttributesMutator;
import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.ExpirationAction;
import com.gemstone.gemfire.cache.ExpirationAttributes;
import com.gemstone.gemfire.cache.PartitionAttributesFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionExistsException;
import com.gemstone.gemfire.cache.RegionFactory;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.cache.partition.PartitionListener;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.internal.cache.PartitionedRegion;

public class GemliteViewContext
{
	private static final int TOTAL_NUM_BUCKETS = 479;
	private static final String MBEAN_PARENT_NAME = "view";
	private static final String CONNECTOR_SYMBOL = "-";
	private GemFireCacheImpl cache;
	private static GemliteViewContext inst = new GemliteViewContext();
	private Map<String, ViewItem> viewContext = new ConcurrentHashMap<String, ViewItem>();
	private Map<String, Set<String>> regionToView = new ConcurrentHashMap<String, Set<String>>();

	private GemliteViewContext() {
		cache = GemFireCacheImpl.getInstance();
	}

	public synchronized static GemliteViewContext getInstance()
	{
		return inst;
	}

	public Map<String, Set<String>> getRegionToView()
	{
		return regionToView;
	}

	public Set<String> getViewsByRegion(String regionName)
	{
		return regionToView.get(regionName);
	}

	public void setRegionToView(Map<String, Set<String>> regionToView)
	{
		this.regionToView = regionToView;
	}

	public Map<String, ViewItem> getViewContext()
	{
		return viewContext;
	}

	@SuppressWarnings("rawtypes")
	public boolean createView(Class<ViewTool> cls)
	{
		boolean result = true;
		String viewName = "";
		try
		{
			View f = cls.getAnnotation(View.class);
			ViewItem item = new ViewItem();
			viewName = f.name();
			if (StringUtils.isEmpty(viewName))
				viewName = cls.getSimpleName();
			item.setName(f.name());
			item.setViewType(f.viewType());
			item.setViewToolByCls(cls);
			item.setTriggerType(f.triggerType());
			item.setDelayTime(f.delayTime());
			item.setRegionName(f.regionName());
			item.setIndexName(f.indexName());
			item.setTriggerOn(f.triggerOn());

			result = createViewItem(item);
		} catch (Exception e)
		{
			e.printStackTrace();

			LogUtil.getCoreLog().error("Create view " + viewName + " failed.",
					e);
		}

		return result;
	}

	/**
	 * validate view item
	 * 
	 * @param item
	 * @return
	 */
	private boolean validateViewItem(ViewItem item)
	{
		boolean result = true;
		if (TriggerOn.REGION.equals(item.getTriggerOn()))
		{
			if (StringUtils.isEmpty(item.getRegionName()))
			{
				LogUtil.getCoreLog()
						.error("View  "
								+ item.getName()
								+ " create failed, trigger on region but region name is empty.");
				result = false;
			}
		}

		if (TriggerOn.INDEX.equals(item.getTriggerOn()))
		{
			if (StringUtils.isEmpty(item.getIndexName()))
			{
				LogUtil.getCoreLog()
						.error("View  "
								+ item.getName()
								+ " create failed, trigger on index but index name is empty.");
				result = false;
			}
		}

		if (TriggerType.DELAY.equals(item.getTriggerType())
				&& item.getDelayTime() == 0)
		{
			LogUtil.getCoreLog()
					.error("View  "
							+ item.getName()
							+ " create failed, triggerType is DELAY but delayTime is zero.");
			result = false;
		}

		return result;

	}

	private void registerRegionToView(String regionName, String viewName)
	{
		if (StringUtils.isEmpty(regionName))
			return;

		Set<String> viewSet = regionToView.get(regionName);
		if (viewSet == null)
		{
			viewSet = new ConcurrentSkipListSet<String>();
			viewSet.add(viewName);
			regionToView.put(regionName, viewSet);
		} else
		{
			if (!viewSet.contains(viewName))
				viewSet.add(viewName);
		}
	}

	/**
	 * register view item
	 * 
	 * @param viewItem
	 */
	@SuppressWarnings("rawtypes")
	private void registerViewItem(ViewItem viewItem)
	{
		viewContext.put(viewItem.getName(), viewItem);

		// add viewItem to regionToView
		String viewName = viewItem.getName();
		String regionName = "";
		if (TriggerOn.REGION.equals(viewItem.getTriggerOn()))
		{
			regionName = viewItem.getRegionName();
		}

		// register view on index
		if (TriggerOn.INDEX.equals(viewItem.getTriggerOn()))
		{
			IIndexContext context = this.getIndexContextByIndexName(viewItem
					.getIndexName());

			if (context != null)
			{
				regionName = context.getRegionName();
				IndexTool tool = (IndexTool) context.getIndexInstance();
				tool.registerViewItem(viewItem);
			}
		}

		registerRegionToView(regionName, viewName);
	}

	public ViewItem getViewItem(String name)
	{
		return viewContext.get(name);
	}

	/**
	 * create view
	 * 
	 * @param viewItem
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean createViewItem(ViewItem viewItem)
	{
		boolean result = false;

		if (viewItem == null)
			return result;

		// check view item
		result = validateViewItem(viewItem);
		if (!result)
			return result;

		String viewName = viewItem.getName();
		ViewItem oldItem = viewContext.get(viewName);
		if (oldItem != null)
		{
			// viewItem has exists
			// if (viewItem.equals(oldItem))
			// {
			// if (LogUtil.getCoreLog().isInfoEnabled())
			// {
			// LogUtil.getCoreLog().info(
			// "View " + viewItem
			// + " has been existed in this node.");
			// }
			// return true;
			// }
			// if item has exists ,elete it first
			removeViewItem(viewName);
		}

		Region<Object, Object> viewRegion = cache.getRegion(viewName);
		// local region, if region has been created, clear data hear
		if (viewRegion != null && viewItem.getViewType().equals(ViewType.LOCAL))
		{
			if (LogUtil.getCoreLog().isInfoEnabled())
			{
				LogUtil.getCoreLog()
						.info("View "
								+ viewItem
								+ ", this view's container has been existed in this node.");
			}
			viewRegion.clear();
		}

		/* region has't been created, create region hear */
		if (viewRegion == null)
			result = createRegion(viewItem);

		// register view item
		registerViewItem(viewItem);

		// register listener
		registerPartitionListener(viewItem);

		// register mBean
		this.createMBean(viewItem);

		// initiation data
		initView(viewItem);

		if (LogUtil.getCoreLog().isInfoEnabled())
			LogUtil.getCoreLog().info(
					"View " + viewItem.getName() + " created.");

		result = true;
		return result;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	private boolean createRegion(ViewItem viewItem)
	{
		boolean result = false;
		String viewName = viewItem.getName();
		Region<Object, Object> viewRegion = null;

		RegionFactory<Object, Object> rf = null;
		if (ViewType.LOCAL.equals(viewItem.getViewType()))
		{
			rf = cache.createRegionFactory(RegionShortcut.LOCAL);
		} else if (ViewType.PARTITION.equals(viewItem.getViewType()))
		{
			rf = cache.createRegionFactory(RegionShortcut.PARTITION);
			PartitionAttributesFactory<Object, Object> pa = new PartitionAttributesFactory<Object, Object>();
			pa.setPartitionResolver(new DomainResolver());
			pa.setRecoveryDelay(0);
			pa.setStartupRecoveryDelay(0);
			pa.setTotalNumBuckets(TOTAL_NUM_BUCKETS);
			rf.setPartitionAttributes(pa.create());
		}
		if (rf != null)
		{
		  rf.setStatisticsEnabled(true);
		  rf.setConcurrencyChecksEnabled(true);
		}

		// set entry time to live
		if (TriggerType.DELAY.equals(viewItem.getTriggerType())
				&& viewItem.getDelayTime() > 0)
		{
			ExpirationAttributes timeToLive = new ExpirationAttributes(
					viewItem.getDelayTime(), ExpirationAction.DESTROY);
			rf.setEntryTimeToLive(timeToLive);
		}

		try
		{
	    if (rf != null)
			viewRegion = rf.create(viewName);

		} catch (RegionExistsException e)
		{
			viewRegion = cache.getRegion(viewName);
			LogUtil.getCoreLog().error("Create view " + viewName + " failed.",
					e);
			result = false;
		}
		result = true;
		return result;

	}

	/**
	 * remove view
	 * 
	 * @param viewName
	 */
	@SuppressWarnings("rawtypes")
	public boolean removeViewItem(String viewName)
	{
		ViewItem viewItem = viewContext.get(viewName);
		if (viewItem == null)
		{
			LogUtil.getCoreLog()
					.error("Drop view failed, this view can not be founded in view context.");
			return false;
		}

		if (TriggerOn.REGION.equals(viewItem.getTriggerOn()))
		{
			String regionName = viewItem.getRegionName();
			Set<String> viewSet = regionToView.get(regionName);
			if (viewSet != null)
			{
				viewSet.remove(viewName);
			}
		}
		if (TriggerOn.INDEX.equals(viewItem.getTriggerOn()))
		{
			IIndexContext context = this.getIndexContextByIndexName(viewItem
					.getIndexName());
			IndexTool tool = (IndexTool) context.getIndexInstance();
			tool.removeViewItem(viewItem.getName());
		}

		// remove viewItem from viewContext
		viewContext.remove(viewItem.getName());

		try
		{
			removeMbean(viewItem);

			Region region = cache.getRegion(viewItem.getName());
			if (region != null)
				region.destroyRegion();
		} catch (Exception e)
		{
			LogUtil.getCoreLog().error("drop view " + viewName + " failed.", e);
			e.printStackTrace();
			return false;
		}

		if (LogUtil.getCoreLog().isInfoEnabled())
		{
			LogUtil.getCoreLog().info(
					"drop view " + viewName + " successfully.");
		}

		return true;
	}

	/**
	 * initiation view data
	 * 
	 * @param viewItem
	 */
	private void initView(ViewItem viewItem)
	{
		long start = System.currentTimeMillis();
		if (TriggerOn.INDEX.equals(viewItem.getTriggerOn()))
			this.initViewByIndex(viewItem);
		else if (TriggerOn.REGION.equals(viewItem.getTriggerOn()))
			this.initViewByRegion(viewItem);
		long end = System.currentTimeMillis();
		if (LogUtil.getCoreLog().isInfoEnabled())
			LogUtil.getCoreLog().info(
					"View " + viewItem.getName()
							+ " has been fullCreate, total time is "
							+ (end - start) + " ms.");
	}

	/**
	 * Refresh view Data by full create.
	 * 
	 * @param viewName
	 */
	public void refreshData(String viewName)
	{
		ViewItem viewItem = viewContext.get(viewName);
		if (viewItem == null)
		{
			LogUtil.getCoreLog()
					.error("ViewItem view failed, this view can not be founded in view context.");
			return;
		}

		initView(viewItem);
	}

	/**
	 * initiation view data : trigger on REGION
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initViewByRegion(ViewItem viewItem)
	{
		Cache c = CacheFactory.getAnyInstance();
		Region region = c.getRegion(viewItem.getRegionName());
		if (PartitionRegionHelper.isPartitionedRegion(region))
			region = PartitionRegionHelper.getLocalData(region);
		Iterator iters = region.entrySet().iterator();
		ViewTool viewTool = viewItem.getViewTool();
		if (viewTool != null && iters != null)
			viewTool.fullCreate(iters);
	}

	/**
	 * initiation view data : trigger on INDEX
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initViewByIndex(ViewItem viewItem)
	{
		IIndexContext context = this.getIndexContextByIndexName(viewItem
				.getIndexName());
		if (context == null)
			return;

		IndexTool tool = (IndexTool) context.getIndexInstance();
		String regionName = context.getRegionName();
		if (tool != null)
		{
			IndexEntrySet entrySet = tool.getIndex();
			if (entrySet != null)
			{
				Iterator iters = tool.getAllDataValues(regionName);
				ViewTool viewTool = viewItem.getViewTool();
				if (viewTool != null && iters != null)
					viewTool.fullCreate(iters);
			}
		}

	}

	private IIndexContext getIndexContextByIndexName(String indexName)
	{
		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
		IIndexContext context = idc.getIndexContext(indexName);
		return context;
	}

	/**
	 * register listener.
	 * 
	 * @param viewItem
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void registerPartitionListener(ViewItem viewItem)
	{
		if (!TriggerOn.REGION.equals(viewItem.getTriggerOn()))
			return;
		String regionName = viewItem.getRegionName();
		Region<?, ?> region = cache.getRegion(regionName);
		if (!checkPartitionListener(region))
		{
			CacheListener<?, ?> ourListener = new EntryChangeListener();
			AttributesMutator mutator = region.getAttributesMutator();
			mutator.addCacheListener(ourListener);
		}

		if (PartitionRegionHelper.isPartitionedRegion(region))
		{
			if (!checkBucketListener(region))
			{
				PartitionListener pl = new BucketChangeListener(regionName);
				GemliteHelper.addPartitionListener(region, pl);
			}
		}

	}

	private boolean checkPartitionListener(Region<?, ?> region)
	{
		CacheListener<?, ?>[] listeners = region.getAttributes()
				.getCacheListeners();
		String clsName = EntryChangeListener.class.getName();
		for (CacheListener<?, ?> listener : listeners)
		{
			String cls = listener.getClass().getName();
			if (clsName.equals(cls))
			{
				return true;
			}
		}

		return false;
	}

	private boolean checkBucketListener(Region<?, ?> region)
	{
		PartitionedRegion partRegion = (PartitionedRegion) region;
		PartitionListener[] listeners = partRegion.getPartitionListeners();
		String clsName = BucketChangeListener.class.getName();
		for (PartitionListener listener : listeners)
		{
			String cls = listener.getClass().getName();
			if (clsName.equals(cls))
			{
				return true;
			}
		}

		return false;
	}

	private void createMBean(ViewItem viewItem)
	{
		String srvName = getMbeanServiceName(viewItem);
		String oname = MBeanHelper.createMBeanName(MBEAN_PARENT_NAME, srvName);

		ViewMbeanStat srv = new ViewMbeanStat();
		srv.setViewName(viewItem.getName());
		srv.setTriggerOn(viewItem.getTriggerOn());
		srv.setDelayTime(viewItem.getDelayTime());
		srv.setTriggerType(viewItem.getTriggerType().name());
		srv.setViewType(viewItem.getViewType().name());
		if (TriggerOn.INDEX.equals(viewItem.getTriggerOn()))
		{
			srv.setTargetName(viewItem.getIndexName());

		} else
		{
			srv.setTargetName(viewItem.getRegionName());
		}

		viewItem.setMbean(srv);
		GemliteNode.getInstance().registerBean(oname, srv);
	}

	private void removeMbean(ViewItem viewItem)
	{
		Object mbean = viewItem.getMbean();
		if (mbean != null)
		{
			String srvName = getMbeanServiceName(viewItem);
			String oname = MBeanHelper.createMBeanName(MBEAN_PARENT_NAME,
					srvName);
			GemliteNode.getInstance().unRegisterBean(oname);
			mbean = null;
			viewItem.setMbean(null);
		}
	}

	private String getMbeanServiceName(ViewItem viewItem)
	{
		String srvName = "";
		if (TriggerOn.INDEX.equals(viewItem.getTriggerOn()))
			srvName = viewItem.getName() + CONNECTOR_SYMBOL
					+ viewItem.getIndexName();
		else
			srvName = viewItem.getName() + CONNECTOR_SYMBOL
					+ viewItem.getRegionName();

		return srvName;
	}

//	public static void main(String[] args)
//	{
//		GemliteViewContext context = GemliteViewContext.getInstance();
//		String regionName = "region1";
//		String viewName = "view1_1";
//		context.registerRegionToView(regionName, viewName);
//		System.out.println("test1>>" + context.getRegionToView().toString());
//		
//		regionName = "region2";
//		viewName = "view2_1";
//		context.registerRegionToView(regionName, viewName);
//		System.out.println("test2>>" + context.getRegionToView().toString());
//		
//		regionName = "region1";
//		viewName = "view1_2";
//		context.registerRegionToView(regionName, viewName);
//		System.out.println("test3>>" + context.getRegionToView().toString());
//		
//		regionName = "region1";
//		viewName = "view1_3";
//		context.registerRegionToView(regionName, viewName);
//		System.out.println("test4>>" + context.getRegionToView().toString());
//	}
}
