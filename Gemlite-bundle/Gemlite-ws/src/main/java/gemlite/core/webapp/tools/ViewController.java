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
package gemlite.core.webapp.tools;

import gemlite.core.internal.jmx.manage.KeyConstants.Regions;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.commands.ViewCommand;
import gemlite.shell.commands.admin.RegionCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/tools")
public class ViewController
{
	@Autowired
	private ViewCommand cmd;

	@Autowired
	private RegionCommand rc;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView listView()
	{
		ModelAndView modelAndView = new ModelAndView("tools/view");
		// 获取所有region
		rc.listRegionDetails();
		List<HashMap<String, Object>> regions = (List<HashMap<String, Object>>) rc
				.get(CommandMeta.LIST_REGIONS);
		// 此处不需要排序
		List<HashMap<String, List<String>>> viewsList = new ArrayList<HashMap<String, List<String>>>();
		// 遍历region取index数据
		if (regions != null)
			for (HashMap<String, Object> region : regions)
			{
				String regionName = (String) region.get(Regions.regionName
						.name());
				cmd.list(true, regionName);
				List<String> list = (List<String>) cmd
						.get(CommandMeta.LIST_VIEW);
				if (list != null && list.size() > 0)
				{
					HashMap<String, List<String>> map = new HashMap<String, List<String>>();
					map.put(regionName, list);
					viewsList.add(map);
				}
			}

		List<HashMap<String, List<String>>> left = new ArrayList<HashMap<String, List<String>>>();
		List<HashMap<String, List<String>>> right = new ArrayList<HashMap<String, List<String>>>();
		for (int i = 0; i < viewsList.size(); i++)
		{
			HashMap<String, List<String>> item = viewsList.get(i);
			if (i % 2 == 0)
				left.add(item);
			else
				right.add(item);
		}
		modelAndView.addObject("left", left);
		modelAndView.addObject("right", right);

		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view/describe/{viewName}", method = RequestMethod.GET)
	@ResponseBody
	public List<HashMap<String, Object>> describeView(
			@PathVariable String viewName)
	{
		//// 去掉后缀
		viewName = viewName.trim();
		// 取v数据
		cmd.describeView(true, viewName);
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) cmd
				.get(CommandMeta.DESCRIBE_VIEW);
		return list;
	}

	@RequestMapping(value = "/view/drop", method = RequestMethod.POST)
	@ResponseBody
	public String dropView(HttpServletRequest request)
	{
		String viewName = request.getParameter("viewName");
		// 取view数据
		return cmd.drop(viewName);
	}
}
