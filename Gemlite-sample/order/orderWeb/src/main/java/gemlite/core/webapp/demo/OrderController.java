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
package gemlite.core.webapp.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gemlite.core.api.logic.LogicServices;
import gemlite.core.api.logic.RemoteResult;
import gemlite.core.common.DateUtil;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;
import gemlite.sample.order.domain.Customer;
import gemlite.sample.order.domain.CustomerDBKey;
import gemlite.sample.order.vo.DetailVO;
import gemlite.sample.order.vo.OrderVO;
import gemlite.sample.order.vo.OutputVO;
import gemlite.shell.commands.Client;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/demo")
public class OrderController {
	@Autowired
	private Client client;

	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public ModelAndView get() {
		ModelAndView modelAndView = new ModelAndView("demo/customer");
		// getConnection();
		return modelAndView;
	}

	@RequestMapping(value = "/customer", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView post(HttpServletRequest request,
			ModelAndView modelAndView) {

		List<Customer> list = new ArrayList<Customer>();
		modelAndView = new ModelAndView("demo/customer");
		String c_name = StringUtils.trim((String) request
				.getParameter("c_name"));
		boolean c_like = StringUtils.equals(
				(String) request.getParameter("c_like"), "on");
		String c_id = StringUtils.trim((String) request.getParameter("c_id"));
		Integer c_age_begin = StringUtils.isEmpty((String) request
				.getParameter("c_age_begin")) ? null : Integer
				.valueOf((String) request.getParameter("c_age_begin"));
		Integer c_age_end = StringUtils.isEmpty((String) request
				.getParameter("c_age_end")) ? null : Integer
				.valueOf((String) request.getParameter("c_age_end"));
		String c_tel = StringUtils.trim((String) request.getParameter("c_tel"));
		getConnection();
		if (StringUtils.isNotEmpty(c_name) && StringUtils.isNotEmpty(c_id)
				&& !c_like) {
			CustomerDBKey key = new CustomerDBKey();
			key.setName(c_name);
			key.setId_num(c_id);
			RemoteResult rr = LogicServices.createRequestWithFilter("customer",
					"QueryByCustomerDBKey", key, key);
			if (rr != null) {
				Customer cust = rr.getResult(Customer.class);
				if (cust != null)
					list.add(cust);
			}
		} else if (c_like && StringUtils.isNotEmpty(c_name)) {
			RemoteResult rr = LogicServices.createHeavyRequest(
					"QueryByCustomerNameLike", c_name);
			if (rr != null)
				list = rr.getResult(List.class);
		} else if (!c_like && StringUtils.isNotEmpty(c_name)
				&& StringUtils.isEmpty(c_id)) {

			RemoteResult rr = LogicServices.createRequestWithFilter("customer",
					"QueryByCustomerName", c_name, c_name);
			if (rr != null)
				list = rr.getResult(List.class);
		}

		else if (StringUtils.isNotEmpty(c_id)) {
			RemoteResult rr = LogicServices.createHeavyRequest(
					"QueryByCustomerID", c_id);
			if (rr != null)
				list = rr.getResult(List.class);
		}

		else if (StringUtils.isNotEmpty(c_tel)) {

			RemoteResult rr = LogicServices.createRequestWithFilter(
					"CustomerIndex04", "GetNamesFromTel", c_tel, c_tel);
			if (rr != null)
				list = rr.getResult(List.class);
		} else if (c_age_begin != null && c_age_end != null) {
			Map map = new HashMap();
			if (c_age_begin != 0)
				map.put("min", c_age_begin);
			if (c_age_end != 0)
				map.put("max", c_age_end);

			RemoteResult rr = LogicServices.createHeavyRequest(
					"QueryByCustomerAgeRange", map);
			if (rr != null)
				list = rr.getResult(List.class);
		}

		modelAndView.addObject("list", list);
		// set user's query terms
		modelAndView.addObject("c_name", c_name);
		modelAndView.addObject("c_id", c_id);
		modelAndView.addObject("c_age_begin", c_age_begin);
		modelAndView.addObject("c_age_end", c_age_end);
		modelAndView.addObject("c_tel", c_tel);

		return modelAndView;
	}

	@RequestMapping(value = "/order", method = RequestMethod.GET)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<HashMap> getOrder(HttpServletRequest request) {
		String name = request.getParameter("name");
		List<OutputVO> outputVOList = null;
		getConnection();
		RemoteResult rr = LogicServices.createRequestWithFilter("customer",
				"QueryOrderByCustomerName", name, name);
		outputVOList = rr.getResult(List.class);
		List<HashMap> detailList = new ArrayList<HashMap>();
		for (int i = 0; i < outputVOList.size(); i++) {
			List<OrderVO> list = outputVOList.get(i).getOrders();
			for (int j = 0; j < list.size(); j++) {
				OrderVO orderVO = list.get(j);
				for (int k = 0; k < orderVO.getDetails().size(); k++) {
					DetailVO detailVO = orderVO.getDetails().get(k);
					HashMap rs = new HashMap();
					rs.put("order_sequence", orderVO.getSequence());
					rs.put("order_date",
							DateUtil.format(orderVO.getOrderdate()));
					rs.put("detail_sequence", detailVO.getSequence());
					rs.put("product_id", detailVO.getProduct().getProduct_id());
					rs.put("product_name", detailVO.getProduct().getName());
					rs.put("product_price", detailVO.getProduct().getPrice());
					rs.put("product_quantity", detailVO.getProduct_quantity());

					detailList.add(rs);
				}
			}
		}
		return detailList;
	}

	private void getConnection() {
		if (!client.isConnect()) {
			ServerConfigHelper.initConfig();
			ServerConfigHelper.initLog4j("log4j-web.xml");
			try {
				client.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
