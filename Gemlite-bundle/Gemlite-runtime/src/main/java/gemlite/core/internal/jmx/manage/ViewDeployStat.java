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
import gemlite.core.internal.jmx.manage.KeyConstants.Views;
import gemlite.core.internal.measurement.view.AbstractViewStat;
import gemlite.core.internal.measurement.view.ViewMeasureHelper;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.view.GemliteViewContext;
import gemlite.core.internal.view.bean.ViewItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.StringUtils;

@SuppressWarnings({ "rawtypes"})
@GemliteMBean(name = "ViewManager", config = true)
@ManagedResource
public class ViewDeployStat implements Serializable
{
	private static final long serialVersionUID = -5459877750200847290L;
	private String msg;

	public ViewDeployStat() {
		// String memberId = ServerConfigHelper.getProperty("MEMBER_ID");
		String ip = ServerConfigHelper.getConfig(ITEMS.BINDIP);
		String port = ServerConfigHelper.getProperty("JMX_RMI_PORT");
		String node = ServerConfigHelper.getProperty(ITEMS.NODE_NAME.name());
		msg = ip + ":" + port + " " + node;
	}

	@ManagedOperation
	@AggregateOperation
	public String dropView(String viewName)
	{
		GemliteViewContext context = GemliteViewContext.getInstance();
		context.removeViewItem(viewName);
		String result = context != null ? msg + "drop successfully " : msg
				+ "drop failed";
		return result;
	}

	@ManagedOperation
	@AggregateOperation
	public List<String> listViews(String regionName)
	{
		List<String> viewList = new ArrayList<String>();
		GemliteViewContext context = GemliteViewContext.getInstance();
		if (context != null)
		{
			if (StringUtils.isEmpty(regionName))
			{
				Map<String, ViewItem> viewMap = context.getViewContext();
				if (viewMap != null && viewMap.values().size() > 0)
				{
					Iterator<ViewItem> iters = viewMap.values().iterator();
					while (iters.hasNext())
					{
						ViewItem item = iters.next();
						if (item != null)
						{
							viewList.add(item.getName());
						}
					}
				}

			} else
			{
				Set<String> valueSet = context.getViewsByRegion(regionName);
				if (valueSet != null)
				{
					for (String value : valueSet)
						viewList.add(value);
				}
			}

		}
		return viewList;
	}

	@ManagedOperation
	@AggregateOperation
	public HashMap<String, Object> describeView(String viewName)
	{
		AbstractViewStat stat = ViewMeasureHelper.getViewStat(viewName);
		if (stat == null)
			return null;
		Map viewInfoMap = stat.describeView();
		if (viewInfoMap == null || viewInfoMap.size() == 0)
			return null;

		String targetName = (String) viewInfoMap.get(Views.targetName.name());
		String viewType = (String) viewInfoMap.get(Views.viewType.name());
		String triggerOn = (String) viewInfoMap.get(Views.triggerOn.name());
		long entrySize = (long) viewInfoMap.get(Views.entrySize.name());
		String triggerType = (String) viewInfoMap.get(Views.triggerType.name());
		int entryTimeToLive = (Integer) viewInfoMap.get(Views.EntryTimeToLive
				.name());

		HashMap<String, Object> map = new HashMap<String, Object>();
		// 此ip信息为展示时排序使用
		map.put(Views.ip.name(), ServerConfigHelper.getConfig(ITEMS.BINDIP)
				+ ServerConfigHelper.getProperty(ITEMS.NODE_NAME.name()));
		map.put(Views.ipinfo.name(), msg);
		map.put(Views.viewName.name(), viewName);
		map.put(Views.targetName.name(), targetName);
		map.put(Views.triggerOn.name(), triggerOn);
		map.put(Views.viewType.name(), viewType);
		map.put(Views.triggerType.name(), triggerType);
		map.put(Views.EntryTimeToLive.name(), entryTimeToLive);
		map.put(Views.entrySize.name(), entrySize);
		return map;

	}

}
