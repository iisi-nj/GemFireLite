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
package com.jnj.adf.grid.biz;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * @author dyang39
 *
 *         we need define the method for local grid and for global grid the
 *         basic operation: insert/update/delete only for local grid the query
 *         operation: pagination/fuzzy/others on lucene index, for local gir or
 *         global gird
 */
public interface GridOperations {
	/***
	 * client side:
	 * 1.first visit index system ,get the key
	 * 2.use the keys to get corresponding items
	 * server side:
	 * 1.only get items exist in current server
	 * @param path
	 * @param queryString
	 * @param jsonMapper
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> queryForList(String queryString, JsonMapper<T> jsonMapper) throws DataAccessException;

	List<Map<String, Object>> queryForList(String queryString) throws DataAccessException;

	List<String> queryForJsonList(String queryString) throws DataAccessException;

	<T> T queryForObject(String queryString, Class<T> requiredType) throws DataAccessException;

	String queryForJson(String queryString) throws DataAccessException;

	List<String> queryPage(String query, String orderBy, boolean orderAcending, int pageNo, int pageSize);

	<T> List<T> queryPage(String query, String orderBy, boolean orderAcending, int pageNo, int pageSize,
			JsonMapper<T> jsonMapper);
}
