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

import gemlite.core.annotations.view.TriggerOn;
import gemlite.core.common.DateUtil;
import gemlite.core.internal.jmx.manage.KeyConstants.Views;
import gemlite.core.internal.support.jmx.AggregateType;
import gemlite.core.internal.support.jmx.annotation.AggregateAttribute;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;

class ViewBigComparator implements Serializable,
		Comparator<AbstractViewStatItem>
{
	private static final long serialVersionUID = -77333958323599427L;

	public ViewBigComparator() {
	}

	@Override
	public int compare(AbstractViewStatItem o1, AbstractViewStatItem o2)
	{
		Long cost1 = o1.getEnd() - o1.getStart();
		Long cost2 = o2.getEnd() - o2.getStart();
		return cost1.compareTo(cost2);
	}
}

class RecentComparator implements Comparator<AbstractViewStatItem>,
		Serializable
{
	private static final long serialVersionUID = -8312107932645948825L;

	public RecentComparator() {
	}

	@Override
	public int compare(AbstractViewStatItem o1, AbstractViewStatItem o2)
	{
		Long timestamp1 = o1.getEnd();
		Long timestamp2 = o2.getEnd();
		return timestamp1.compareTo(timestamp2);
	}
}

public abstract class AbstractViewStat
{
	private String targetName;
	private TriggerOn triggerOn;
	private String viewName;
	private String viewType;
	private String triggerType;
	private int delayTime;

	protected AtomicLong entrySize = new AtomicLong();

	private AtomicLong totalCount = new AtomicLong();
	private AtomicLong totalCost = new AtomicLong();
	private int maxHistorySize = 3;
	private int recentHistorySize = 10;
	private int tpsRange = 2;// seconds
	private ConcurrentSkipListSet<AbstractViewStatItem> maxHistory;
	private ConcurrentSkipListSet<AbstractViewStatItem> recentHistory;
	private long lastCost;
	private long maxCost;
	private long avgCost;

	private long firstAt;
	private long lastAt;

	private int tps;

	private long prevTimestamp;
	private long prevCount;
	private Map<String, String> keyFields = null; // Name->Type
	private Map<String, String> valueFields = null; // Name->Type

	public AbstractViewStat() {
		maxHistory = new ConcurrentSkipListSet<>(new ViewBigComparator());
		recentHistory = new ConcurrentSkipListSet<>(new RecentComparator());
	}

	private Map<String, String> getValueFields()
	{
		valueFields = new HashMap<String, String>();
		return valueFields;
	}

	private Map<String, String> getKeyFields()
	{
		keyFields = new HashMap<String, String>();
		return keyFields;
	}

	public void calcuteTPS()
	{
		long timestamp = System.currentTimeMillis();
		long past = timestamp - prevTimestamp;
		if (prevTimestamp == 0)
			prevTimestamp = timestamp;
		else if (past > (tpsRange * 1000))
		{
			long thisCount = totalCount.get();
			long count = thisCount - prevCount;
			tps = (int) (count * 1000 / past);
			prevTimestamp = timestamp;
			prevCount = thisCount;
		}
	}

	/***
	 * ¿‰h
	 * 
	 * @param item
	 */
	public void recordItem(AbstractViewStatItem item)
	{
		if (firstAt == 0)
			firstAt = item.getStart();
		lastAt = item.getEnd();
		long cost = item.getEnd() - item.getStart();
		if (cost > maxCost)
			maxCost = cost;
		lastCost = cost;

		if (totalCount.get() > 0)
			avgCost = (int) (totalCost.get() / totalCount.get());

		maxHistory.add(item);
		recentHistory.add(item);
		if (maxHistory.size() > maxHistorySize)
			maxHistory.pollFirst();
		if (recentHistory.size() > recentHistorySize)
			recentHistory.pollFirst();

	}

	@ManagedOperation
	public void resetStat()
	{
		totalCount.set(0l);
		totalCost.set(0l);
		maxCost = 0l;
		lastCost = 0l;
		avgCost = 0l;
		maxHistory.clear();
		recentHistory.clear();
		firstAt = 0l;
		lastAt = 0l;
		tps = 0;
		prevTimestamp = 0l;
		prevCount = 0l;
	}

	public void incrTotalCount()
	{
		totalCount.incrementAndGet();
	}

	public void incrTotalCost(long cost)
	{
		totalCost.addAndGet(cost);
	}

	@AggregateAttribute(AggregateType.SUM)
	@ManagedAttribute(description = "Total Count")
	public long getTotalCount()
	{
		return totalCount.get();
	}

	@AggregateAttribute(AggregateType.SUM)
	@ManagedAttribute(description = "Total Cost")
	public long getTotalCost()
	{
		return totalCost.get();
	}

	@AggregateAttribute(AggregateType.SAME)
	@ManagedAttribute(description = "Max History Size")
	public int getMaxHistorySize()
	{
		return maxHistorySize;
	}

	@AggregateAttribute(AggregateType.SAME)
	@ManagedAttribute(description = "Recent History Size")
	public int getRecentHistorySize()
	{
		return recentHistorySize;
	}

	@AggregateAttribute(AggregateType.MAX)
	@ManagedAttribute
	public long getLastAt()
	{
		return lastAt;
	}

	@AggregateAttribute(AggregateType.MIN)
	@ManagedAttribute
	public long getFirstAt()
	{
		return firstAt;
	}

	@AggregateAttribute(AggregateType.SAME)
	@ManagedAttribute(description = "Tps Sum Range")
	public int getTpsRange()
	{
		return tpsRange;
	}

	@AggregateAttribute(AggregateType.SUM)
	@ManagedAttribute(description = "tps")
	public int getTps()
	{
		return tps;
	}

	@AggregateAttribute(AggregateType.MAX)
	@ManagedAttribute(description = "Last Cost")
	public long getLastCost()
	{
		return lastCost;
	}

	@AggregateAttribute(AggregateType.MAX)
	@ManagedAttribute(description = "Max Const")
	public long getMaxCost()
	{
		return maxCost;
	}

	@AggregateAttribute(AggregateType.AVG)
	@ManagedAttribute(description = "Average Cost")
	public long getAvgCost()
	{
		return avgCost;
	}

	@AggregateOperation(support = true)
	@ManagedOperation
	public List<AbstractViewStatItem> getMaxHistory()
	{
		List<AbstractViewStatItem> list = new ArrayList<>();
		list.addAll(maxHistory);
		return list;
	}

	@AggregateOperation(support = true)
	@ManagedOperation
	public List<AbstractViewStatItem> getRecentHistory()
	{
		List<AbstractViewStatItem> list = new ArrayList<>();
		list.addAll(recentHistory);
		return list;
	}

	@ManagedAttribute(description = "Target Name")
	public String getTargetName()
	{
		return targetName;
	}

	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}

	@ManagedAttribute(description = "Trigger On")
	public String getTriggerOn()
	{
		return triggerOn.name();
	}

	public void setTriggerOn(TriggerOn triggerOn)
	{
		this.triggerOn = triggerOn;
	}

	@ManagedAttribute(description = "View Name")
	public String getViewName()
	{
		return viewName;
	}

	public void setViewName(String viewName)
	{
		this.viewName = viewName;
	}

	public void incrEntrySize()
	{
		entrySize.incrementAndGet();
	}

	public void decrEntrySize()
	{
		entrySize.decrementAndGet();
	}

	@ManagedAttribute(description = "View Type")
	public String getViewType()
	{
		return viewType;
	}

	public void setViewType(String viewType)
	{
		this.viewType = viewType;
	}

	@ManagedAttribute(description = "Trigger Type")
	public String getTriggerType()
	{
		return triggerType;
	}

	public void setTriggerType(String triggerType)
	{
		this.triggerType = triggerType;
	}

	@ManagedAttribute(description = "Entry Time To Live")
	public int getDelayTime()
	{
		return delayTime;
	}

	public void setDelayTime(int delayTime)
	{
		this.delayTime = delayTime;
	}

	public abstract long getEntrySize();

	public Map<String, Object> describeView()
	{
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put(Views.viewName.name(), this.viewName);
		infoMap.put(Views.triggerOn.name(), this.triggerOn.name());
		infoMap.put(Views.targetName.name(), this.targetName);
		infoMap.put(Views.viewType.name(), this.viewType);
		infoMap.put(Views.triggerType.name(), this.triggerType);
		infoMap.put(Views.EntryTimeToLive.name(), this.delayTime);

		infoMap.put(Views.entrySize.name(), getEntrySize());
		keyFields = getKeyFields();
		valueFields = getValueFields();
		infoMap.put(Views.keyFields.name(), keyFields);
		infoMap.put(Views.valueFields.name(), valueFields);

		return infoMap;
	}

	private Object transformParameter(String key, String value)
	{
		Object result = null;
		String type = getKeyFields().get(key);
		if (type.equalsIgnoreCase("String"))
			result = value;
		else if (type.equalsIgnoreCase("int"))
			result = Integer.parseInt(value);
		else if (type.equalsIgnoreCase("Integer"))
			result = new Integer(value);
		else if (type.equalsIgnoreCase("long"))
			result = Long.parseLong(value);
		else if (type.equalsIgnoreCase("Long"))
			result = new Long(value);
		else if (type.equalsIgnoreCase("double"))
			result = Double.parseDouble(value);
		else if (type.equalsIgnoreCase("Double"))
			result = new Double(value);
		else if (type.equalsIgnoreCase("float"))
			result = Float.parseFloat(value);
		else if (type.equalsIgnoreCase("Float"))
			result = new Float(value);
		else if (type.equalsIgnoreCase("boolean"))
			result = Boolean.parseBoolean(value);
		else if (type.equalsIgnoreCase("Boolean"))
			result = new Boolean(value);
		else if (type.equalsIgnoreCase("Date"))
			result = DateUtil.parse(value);
		else
			result = value;

		return result;

	}

	public String printIndexValue(String searchParamStr, int pageNumber,
			int pageSize)
	{
		return "";
	}

}
