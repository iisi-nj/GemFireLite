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

import gemlite.core.internal.jmx.manage.KeyConstants.Indexs;
import gemlite.core.internal.jmx.manage.KeyConstants.Regions;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.commands.IndexCommand;
import gemlite.shell.commands.admin.RegionCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
public class IndexController
{
	private static final int PAGE_SIZE = 20;
	@Autowired
	private IndexCommand ic;

	@Autowired
	private RegionCommand rc;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView listIndex()
	{
		ModelAndView modelAndView = new ModelAndView("tools/index");
		// 获取所有region
		rc.listRegionDetails();
		List<HashMap<String, Object>> regions = (List<HashMap<String, Object>>) rc
				.get(CommandMeta.LIST_REGIONS);
		// 此处不需要排序,因为list regions已经做过排序
		List<HashMap<String, List<String>>> indexs = new ArrayList<HashMap<String, List<String>>>();
		// 遍历region取index数据
		if (regions != null)
			for (HashMap<String, Object> region : regions)
			{
				String regionName = (String) region.get(Regions.regionName
						.name());
				ic.list(true, regionName);
				List<String> list = (List<String>) ic
						.get(CommandMeta.LIST_INDEX);
				if (list != null && list.size() > 0)
				{
					HashMap<String, List<String>> map = new HashMap<String, List<String>>();
					map.put(regionName, list);
					indexs.add(map);
				}
			}

		List<HashMap<String, List<String>>> left = new ArrayList<HashMap<String, List<String>>>();
		List<HashMap<String, List<String>>> right = new ArrayList<HashMap<String, List<String>>>();
		for (int i = 0; i < indexs.size(); i++)
		{
			HashMap<String, List<String>> item = indexs.get(i);
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
	@RequestMapping(value = "/index/describe/{indexName}", method = RequestMethod.GET)
	@ResponseBody
	public List<HashMap<String, Object>> describeIndex(
			@PathVariable String indexName)
	{
		// 去掉后缀
		indexName = StringUtils.replace(indexName, "(Hash Index)", "");
		// 取index数据
		ic.describeIndex(true, indexName);
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) ic
				.get(CommandMeta.DESCRIBE_INDEX);
		return list;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/index/openform/{indexName}", method = RequestMethod.GET)
	@ResponseBody
	public List<HashMap<String, Object>> openQueryForm(
			@PathVariable String indexName)
	{
		// 去掉后缀
		indexName = StringUtils.replace(indexName, "(Hash Index)", "");
		List<HashMap<String, Object>> rtList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> rtMap = new HashMap<String, Object>();
		// 取index数据
		ic.describeIndex(true, indexName);
		List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) ic
				.get(CommandMeta.DESCRIBE_INDEX);
		StringBuffer typeBuff = new StringBuffer();
		StringBuffer valueBuff = new StringBuffer();
		if (list != null && list.size() > 0)
		{
			Map<String, Object> attrMap = list.get(0);
			Object obj = attrMap.get(Indexs.keyFields.name());
			if (obj != null && obj instanceof Map)
			{
				HashMap<String, Object> typeMap = (HashMap<String, Object>) obj;
				Set<Entry<String, Object>> typeSet = typeMap.entrySet();
				if (typeSet.size() > 0)
				{
					for (Entry<String, Object> entry : typeSet)
					{
						if (typeBuff.length() > 0)
						{
							typeBuff.append(",").append(entry.getKey())
									.append("->").append(entry.getValue());
							valueBuff.append(" && ").append(entry.getKey())
									.append("=?");
						} else
						{
							typeBuff.append(entry.getKey()).append("->")
									.append(entry.getValue());
							valueBuff.append(entry.getKey()).append("=?");
						}
					}

				}
			}
			rtMap.put("indexName", indexName);
			rtMap.put("pageNum", 1);
			rtMap.put("pageSize", PAGE_SIZE);
			rtMap.put("paramsDatatype", typeBuff.toString());
			rtMap.put("paramsValue", valueBuff.toString());
			rtList.add(rtMap);
		}
		return rtList;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/index/query/", method = RequestMethod.POST)
	@ResponseBody
	public List<HashMap<String, Object>> queryIndex(HttpServletRequest request)
	{
		String indexName = request.getParameter("indexName");
		indexName = StringUtils.replace(indexName, "(Hash Index)", "");
		String paramsValue = request.getParameter("paramsValue");
		String pageNumStr = request.getParameter("pageNum");
		int pageNum = Integer.valueOf(pageNumStr);
		int pageSize = PAGE_SIZE;
		// 取index数据
		List<HashMap<String, Object>> rtList = new ArrayList<HashMap<String, Object>>();
		ic.queryIndex(true, indexName, paramsValue, pageNum, pageSize);
		Object obj = ic.get(CommandMeta.QUERY_INDEX);
		if (obj == null || obj.toString().length()<10)
		{
			
			HashMap<String, Object> rtMap = new HashMap<String, Object>();
			rtMap.put("EntrySize", 0);
			rtMap.put("PageSize", 20);
			rtMap.put("PageNumber", 1);
			rtMap.put("IP", "");
			rtMap.put("MemberID","");
			rtMap.put("EntryValue", new ArrayList<String>());
			rtList.add(rtMap);
		}

		if (ic.get(CommandMeta.QUERY_INDEX) != null)
		{
			rtList = (List<HashMap<String, Object>>) ic
					.get(CommandMeta.QUERY_INDEX);
		}
		return rtList;
	}

	@RequestMapping(value = "/index/create", method = RequestMethod.POST)
	@ResponseBody
	public String createIndex(HttpServletRequest request)
	{
		String clause = request.getParameter("clause");
		// 取index数据
		return ic.create(true, clause);
	}

	@RequestMapping(value = "/index/drop", method = RequestMethod.POST)
	@ResponseBody
	public String dropIndex(HttpServletRequest request)
	{
		String indexName = request.getParameter("indexName");
		// 取index数据
		return ic.drop(true, indexName);
	}
}
