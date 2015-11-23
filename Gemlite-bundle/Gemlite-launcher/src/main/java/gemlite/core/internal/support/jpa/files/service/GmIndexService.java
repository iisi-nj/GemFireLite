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
package gemlite.core.internal.support.jpa.files.service;

import gemlite.core.internal.support.jpa.files.dao.GmIndexDao;
import gemlite.core.internal.support.jpa.files.domain.GmIndex;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gemstone.gemfire.internal.lang.StringUtils;

@Service
public class GmIndexService
{
	@Autowired
	private GmIndexDao dao;

	/**
	 * 保存索引
	 * 
	 * @param indexFile
	 */
	@Transactional
	public void save(GmIndex gmIndex)
	{
		
		if (!checkIndexValueValid(gmIndex.getIndexName(), gmIndex.getIndexClause()))
			return;

		GmIndex oldIndex = dao.getByIndexName(gmIndex.getIndexName());
		if (oldIndex != null && oldIndex.getIndexClause() != null
				&& oldIndex.getIndexClause().equals(gmIndex.getIndexClause().trim()))
			return;

		if (gmIndex != null)
		{
			GmIndex old = dao.getByIndexName(gmIndex.getIndexName());
			if (old != null)
			{
				dao.save(gmIndex);
			}
		}
	}

	private boolean checkIndexValueValid(String indexName, String clause)
	{
		if (StringUtils.isBlank(indexName) || StringUtils.isBlank(clause))
			return false;

		return true;
	}

	/**
	 * 保存索引
	 * 
	 * @param indexFile
	 */
	@Transactional
	public void save(String indexName, String clause)
	{
		if (!checkIndexValueValid(indexName, clause))
			return;

		GmIndex oldIndex = dao.getByIndexName(indexName);
		if (oldIndex != null && oldIndex.getIndexClause() != null)
		{
			if (oldIndex.getIndexClause().trim().equals(clause.trim()))
				return;
		}

		GmIndex gmIndex = new GmIndex();
		gmIndex.setIndexName(indexName);
		gmIndex.setIndexClause(clause);
		gmIndex.setUpload_time(new Date());
		dao.save(gmIndex);
	}

	/**
	 * 保存索引
	 * 
	 * @param indexFile
	 */
	@Transactional
	public void save(List<GmIndex> gmIndexList)
	{
		if (gmIndexList == null || gmIndexList.size() == 0)
			return;
		for (GmIndex index : gmIndexList)
		{
			if (index != null)
				this.save(index);
		}
	}

	/**
	 * 删除制定索引
	 * 
	 * @param indexName
	 */
	@Transactional
	public void deleteIndex(String indexName)
	{
		if (StringUtils.isBlank(indexName))
			return;

		GmIndex index = dao.getByIndexName(indexName);
		if (index != null)
			dao.delete(index);

	}

	/**
	 * 清空表
	 */
	public void deleteAllIndex()
	{
		dao.deleteAll();
	}

	/**
	 * 获取所有index索引文件列表
	 * 
	 * @param indexName
	 * @return
	 */
	public List<GmIndex> getAllIndexs()
	{
		List<GmIndex> indexList = dao.findAll();
		return indexList;
	}
	
	/**
	 * find all index count 
	 * @return
	 */
	public int getAllIndexCount()
	{
		return dao.findAll().size();
	}
	
	public boolean checkIndexExists(String indexName)
	{
		boolean result = false;
		if (dao.getByIndexName(indexName) != null)
		{
			result = true;
		}
		
		return result;
	}

}
