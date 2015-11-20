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
package gemlite.core.internal.support.jpa.files.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * @author ynd
 *
 */
@Entity
@Table(name = "gm_index")
public class GmIndex implements Serializable
{
	private static final long serialVersionUID = -7603547810550507334L;
	@Id
	private String indexName;

	private String indexClause;
	
	private Date upload_time;

	public String getIndexClause()
	{
		return indexClause;
	}

	public void setIndexClause(String indexClause)
	{
		this.indexClause = indexClause;
	}

	public Date getUpload_time()
	{
		return upload_time;
	}

	public void setUpload_time(Date upload_time)
	{
		this.upload_time = upload_time;
	}

	public String getIndexName()
	{
		return indexName;
	}

	public void setIndexName(String indexName)
	{
		this.indexName = indexName;
	}

}
