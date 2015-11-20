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
package gemlite.core.internal.view.func;

import gemlite.core.internal.support.FunctionIds;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.view.GemliteViewContext;
import gemlite.core.internal.view.bean.ViewItem;
import gemlite.core.internal.view.trigger.ViewTool;
import gemlite.core.util.LogUtil;
import gemlite.core.util.ViewUtil.CommandType;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class ViewManagerFunc implements Function
{
	private static final long serialVersionUID = -7565852080677183025L;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FunctionContext context)
	{
		Map<String, Object> paramsMap = (Map<String, Object>) context
				.getArguments();
		if (paramsMap == null || paramsMap.entrySet() == null
				|| paramsMap.entrySet().size() == 0)
		{
			context.getResultSender().lastResult("");
			return;
		}
		Object type = paramsMap.get("CommandType");
		if (type == null)
		{
			context.getResultSender().lastResult("");
			return;
		}
		String commandType = (String) type;
		String result = "";
		if (CommandType.LIST.name().equals(commandType))
		{
			String regionName = (String) paramsMap.get("RegionName");
			result = listViews(regionName);
		} else if (CommandType.DROP.name().equals(commandType))
		{
			result = dropView(paramsMap);

		} else if (CommandType.DESCRIBE.name().equals(commandType))
		{
			result = describeView(paramsMap);

		} else if (CommandType.REFULLCREATE.name().equals(commandType))
		{
			result = reFullCreateView(paramsMap);

		} else if (CommandType.CREATE.name().equals(commandType))
		{
			result = createView(paramsMap);

		} else
		{
			result = "";
		}

		context.getResultSender().lastResult(result);
	}

	/**
	 * List all views.
	 * 
	 * @return
	 */
	private String listViews(String regionName)
	{
		String msg = getNodeInfo();
		StringBuffer sb = new StringBuffer();
		sb.append(msg + "\n");
		sb.append("-------------------------------\n");
		try
		{
			GemliteViewContext ctx = GemliteViewContext.getInstance();
			if (StringUtils.isEmpty(regionName))
			{
				Map<String, ViewItem> views = ctx.getViewContext();
				Iterator<String> iters = views.keySet().iterator();
				int iseq = 0;
				while (iters.hasNext())
				{
					String viewName = iters.next();
					sb.append((iseq + 1) + ": " + viewName + "\n");
				}
			} else
			{
				Set<String> views = ctx.getViewsByRegion(regionName);
				int iseq = 0;
				if (views != null)
				{
					for (String viewName : views)
					{
						sb.append((iseq + 1) + ": " + viewName + "\n");
					}
				}
			}
		} catch (Exception e)
		{
		}
		return sb.toString();
	}

	/**
	 * Drop target view.
	 * 
	 * @param paramsMap
	 * @return
	 */
	private String dropView(Map<String, Object> paramsMap)
	{
		String msg = getNodeInfo();

		String viewName = (String) paramsMap.get("ViewName");
		if (StringUtils.isEmpty(viewName))
		{
			String result = msg + "Input param can't be null.";
			return result;
		}

		GemliteViewContext ctx = GemliteViewContext.getInstance();
		boolean success = ctx.removeViewItem(viewName);
		String result = success == true ? msg + "View " + viewName
				+ "drop successfully." : msg + "View " + viewName
				+ "drop failed.";
		return result;
	}

	/**
	 * Describe target view.
	 * 
	 * @param paramsMap
	 * @return
	 */
	private String describeView(Map<String, Object> paramsMap)
	{
		String msg = getNodeInfo();
		String viewName = (String) paramsMap.get("ViewName");
		if (StringUtils.isEmpty(viewName))
		{
			String result = msg + "Input param can't be null.";
			return result;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(msg + "\n");
		sb.append("-------------------------------\n");
		try
		{
			GemliteViewContext ctx = GemliteViewContext.getInstance();
			ViewItem item = ctx.getViewItem(viewName);
			if (item != null)
			{
				String idxName = item.getIndexName();
				String regionName = item.getRegionName();
				String triggerType = item.getTriggerType().name();
				String triggerOn = item.getTriggerOn().name();
				String viewType = item.getViewType().name();
				String viewTool = item.getViewTool().getClass().getName();
				int delayTime = item.getDelayTime();
				Region<Object, Object> region = CacheFactory.getAnyInstance()
						.getRegion(item.getName());
				int entryCount = region.size();
				sb.append("View Name: " + viewName + "\n");
				sb.append("View Type: " + viewType + "\n");
				sb.append("Trigger On: " + triggerOn + "\n");
				sb.append("Region Name: " + regionName + "\n");
				sb.append("Index Name: " + idxName + "\n");
				sb.append("Trigger Type: " + triggerType + "\n");
				sb.append("Entry time to live: " + delayTime + "\n");
				sb.append("View Entry Count: " + entryCount + "\n");
				sb.append("View Tool: " + viewTool + "\n");
			} else
			{
				sb.append("Can't find the view item's information in view context.\n");
			}

		} catch (Exception e)
		{
			LogUtil.getCoreLog().error(sb.toString(), e);
		}
		return sb.toString();
	}

	/**
	 * Refresh view data by full create.
	 * 
	 * @param paramsMap
	 * @return
	 */
	private String reFullCreateView(Map<String, Object> paramsMap)
	{
		String msg = getNodeInfo();
		String viewName = (String) paramsMap.get("ViewName");
		if (StringUtils.isEmpty(viewName))
		{
			String result = msg + "Input param can't be null.";
			return result;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(msg + "\n");

		GemliteViewContext ctx = GemliteViewContext.getInstance();
		try
		{
			ctx.refreshData(viewName);
		} catch (Exception e)
		{
			e.printStackTrace();
			sb.append("RefullCreated view failed." + "\n");
			sb.append(e.getMessage() + "\n");
			return sb.toString();
		}

		sb.append("RefullCreated view successfully.");
		return sb.toString();
	}

	/**
	 * ReCreate view after deleted.
	 * 
	 * @param paramsMap
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String createView(Map<String, Object> paramsMap)
	{
		String msg = getNodeInfo();
		String clsName = (String) paramsMap.get("ClassName");
		if (StringUtils.isEmpty(clsName))
		{
			String result = msg + "Input param can't be null.";
			return result;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(msg + "\n");
		GemliteViewContext ctx = GemliteViewContext.getInstance();
		Class<ViewTool> cls = null;
		try
		{
			cls = (Class<ViewTool>) ViewManagerFunc.class.getClassLoader()
					.loadClass(clsName);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			sb.append("Create view failed, class " + clsName
					+ " not found in classloader.");
			return sb.toString();
		}

		try
		{
			ctx.createView(cls);
		} catch (Exception e)
		{
			e.printStackTrace();
			sb.append("Create view failed." + "\n");
			sb.append(e.getMessage() + "\n");
			return sb.toString();
		}

		sb.append("Create view successfully.");
		return sb.toString();
	}

	@Override
	public String getId()
	{
		return FunctionIds.MANAGER_VIEW_FUNCTION;
	}

	@Override
	public boolean hasResult()
	{
		return true;
	}

	@Override
	public boolean isHA()
	{
		return false;
	}

	@Override
	public boolean optimizeForWrite()
	{
		return false;
	}

	private String getNodeInfo()
	{
		String memberId = ServerConfigHelper.getProperty("MEMBER_ID");
		String ip = ServerConfigHelper.getConfig(ITEMS.BINDIP);
		String port = ServerConfigHelper.getProperty("JMX_RMI_PORT");
		return "MemberID: " + memberId + " IP: " + ip + " Port: " + port + " ";
	}

}
