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
package gemlite.shell.service.admin;

import gemlite.core.util.LogUtil;
import gemlite.core.util.ViewUtil;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ViewService
{
	/**
	 * list all views.
	 * 
	 * @return
	 */
	public List<String> listViews(String regionName)
	{
		try
		{
			if (LogUtil.getAppLog().isDebugEnabled())
				LogUtil.getAppLog().debug("List all views: ");
			List<String> list = ViewUtil.listViews(regionName);
			return list;
		} catch (Exception e)
		{
			LogUtil.getAppLog().error("List all views failed.", e);
			return null;
		}
	}

	/**
	 * Describe view by view name.
	 * 
	 * @param viewName
	 * @return
	 */
	public List<String> describeView(String viewName)
	{
		try
		{
			if (LogUtil.getAppLog().isDebugEnabled())
				LogUtil.getAppLog().debug("Describe View: " + viewName);
			List<String> list = ViewUtil.describeView(viewName);
			return list;
		} catch (Exception e)
		{
			LogUtil.getAppLog().error(
					"Describe View: " + viewName + " failed.", e);
			return null;
		}
	}

	/**
	 * Drop view by view name.
	 * 
	 * @param viewName
	 * @return
	 */
	public String dropView(String viewName)
	{
		try
		{
			if (LogUtil.getAppLog().isDebugEnabled())
				LogUtil.getAppLog().debug("Drop View: " + viewName);
			StringBuffer sb = new StringBuffer();
			List<String> list = ViewUtil.dropView(viewName);
			for (int i = 0; i < list.size(); i++)
				sb.append(list.get(i) + "\n");

			return sb.toString();
		} catch (Exception e)
		{
			LogUtil.getAppLog().error("Drop View: " + viewName + " failed.", e);
			return e.getMessage();
		}
	}

	/**
	 * Refresh view data.
	 * 
	 * @param viewName
	 * @return
	 */
	public String refullcreate(String viewName)
	{
		try
		{
			if (LogUtil.getAppLog().isDebugEnabled())
				LogUtil.getAppLog().debug("ReFullCreate View: " + viewName);
			StringBuffer sb = new StringBuffer();
			List<String> list = ViewUtil.refullcreate(viewName);
			for (int i = 0; i < list.size(); i++)
				sb.append(list.get(i) + "\n");

			return sb.toString();
		} catch (Exception e)
		{
			LogUtil.getAppLog().error(
					"ReFullCreate View: " + viewName + " failed.", e);
			return e.getMessage();
		}
	}

	/**
	 * Create view by class name.
	 * 
	 * @param clsName
	 * @return
	 */
	public String create(String clsName)
	{
		try
		{
			if (LogUtil.getAppLog().isDebugEnabled())
				LogUtil.getAppLog().debug(
						"Create View failed. source class name is " + clsName
								+ ".");
			StringBuffer sb = new StringBuffer();
			List<String> list = ViewUtil.create(clsName);
			for (int i = 0; i < list.size(); i++)
				sb.append(list.get(i) + "\n");

			return sb.toString();
		} catch (Exception e)
		{
			LogUtil.getAppLog().error(
					"Create View failed, class name is " + clsName + " .", e);
			return e.getMessage();
		}
	}
}
