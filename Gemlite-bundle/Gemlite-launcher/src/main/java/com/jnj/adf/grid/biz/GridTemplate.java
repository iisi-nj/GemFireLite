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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.jnj.adf.grid.domain.GridInfo;

/**
 * @author dyang39
 *
 */
public class GridTemplate extends GridAccessor implements GridOperations, GridBasicOperations {
	private GridInfo gridInfo;
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridBasicOperations#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridBasicOperations#get(java.lang.Object)
	 */
	@Override
	public <K, V> V get(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridBasicOperations#getAll(java.util.Collection)
	 */
	@Override
	public <K, V> Map<K, V> getAll(Collection<?> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridBasicOperations#put(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public <K, V> V put(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridBasicOperations#putAll(java.util.Map)
	 */
	@Override
	public <K, V> void putAll(Map<? extends K, ? extends V> map) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridBasicOperations#remove(java.lang.Object)
	 */
	@Override
	public <K, V> V remove(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridBasicOperations#removeAll()
	 */
	@Override
	public void removeAll() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridIndexOperations#queryForList(java.lang.String,
	 * java.lang.String, com.jnj.biz.JsonMapper)
	 */
	@Override
	public <T> List<T> queryForList( String queryString, JsonMapper<T> jsonMapper)
			throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridIndexOperations#queryForList(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> queryForList( String queryString) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridIndexOperations#queryForJsonList(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<String> queryForJsonList( String queryString) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridIndexOperations#queryForObject(java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T queryForObject( String queryString, Class<T> requiredType) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridIndexOperations#queryForJson(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String queryForJson( String queryString) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridIndexOperations#queryPage(java.lang.String,
	 * java.lang.String, java.lang.String, boolean, int, int)
	 */
	@Override
	public List<String> queryPage( String query, String orderBy, boolean orderAcending, int pageNo,
			int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.biz.GridIndexOperations#queryPage(java.lang.String,
	 * java.lang.String, java.lang.String, boolean, int, int,
	 * com.jnj.biz.JsonMapper)
	 */
	@Override
	public <T> List<T> queryPage( String query, String orderBy, boolean orderAcending, int pageNo,
			int pageSize, JsonMapper<T> jsonMapper) {
		// TODO Auto-generated method stub
		return null;
	}

}
