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
package gemlite.core.api.index.clause;

import gemlite.core.api.index.IndexEntrySet;
import gemlite.core.api.index.IndexManager;
import gemlite.core.api.index.clause.ColumnOp.QueryType;
import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class IndexClauseImpl implements IndexClause
{
  class ColumnClauseImpl implements ColumnClause
  {
    private IndexClauseImpl indexClause;
    private ColumnOp op;
     
    public ColumnClauseImpl(IndexClauseImpl indexClause, String name)
    {
      this.indexClause = indexClause;
      this.op = new ColumnOp();
      op.name = name;
    }
    
    @Override
    public IndexClauseImpl equalsTo(Object value)
    {
      op.value = value;
      op.type = QueryType.Equal;
      return indexClause;
    }
    
    protected ColumnOp getColumnOp()
    {
      return op;
    }
    
    protected void setValue(Object value)
    {
    	op.value = value;
    }
    
    protected void setType(QueryType type)
    {
    	op.type = type;
    }
    
	@Override
	public IndexClause biggerThan(Object value)
	{
		op.fromValue = value;
		op.fromInclusive = false;
	    op.type = QueryType.Bigger;
	    return indexClause;
	}

	@Override
	public IndexClause biggerThanOrEquals(Object value) 
	{
		op.fromValue = value;
		op.fromInclusive = true;
	    op.type = QueryType.Bigger;
	    return indexClause;
	}

	@Override
	public IndexClause lessThan(Object value) 
	{
		op.toValue = value;
		op.toInclusive = false;
		op.type = QueryType.Less;
		return indexClause;
	}

	@Override
	public IndexClause lessThanOrEquals(Object value) 
	{
		op.toValue = value;
		op.toInclusive = true;
		op.type = QueryType.Less;
		return indexClause;
	}

	@Override
	public IndexClause like(Object value) 
	{
		op.value = value;
		op.type = QueryType.Like;
		return indexClause;
	}
  }
  
  private Set<ColumnClauseImpl> columS = new HashSet<>();
  @SuppressWarnings("rawtypes")
  private IndexEntrySet idxSet;
  
  public IndexClauseImpl(String indexName)
  {
	  idxSet = IndexManager.getIndexData(indexName);
  }
  
  @Override
  public ColumnClause column(String columnName)
  {
    ColumnClauseImpl colunmClause = new ColumnClauseImpl(this, columnName);
    columS.add(colunmClause);
    return colunmClause;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public <T> Set<T> query(Class<T> T)
  {
    int columnSize = columS.size();
    if(columnSize == 1)
    {
    	ColumnClauseImpl col = columS.iterator().next();
    	QueryType type = col.getColumnOp().type;
    	if(type == QueryType.Equal)
    	{
    	    Map m = new HashMap();
    	    m.put(col.getColumnOp().name, col.getColumnOp().value);
    	    if(idxSet != null)
        	{
        		Object k = idxSet.mapperKey(m);
        		Set<T> it = (Set<T>) idxSet.getValue(k);
        		return it;
        	}
    	}
    	else if(type == QueryType.Like)
    	{
    		Map m = new HashMap();
    	    m.put(col.getColumnOp().name, col.getColumnOp().value);
    	    if(idxSet != null)
        	{
        		Object k = idxSet.mapperKey(m);
        		Set<T> it = (Set<T>) idxSet.getLikeValue(k);
        		return it;
        	}
    	}
    	else if(type == QueryType.Bigger)
    	{
    		Map fromMap = new HashMap();
    		fromMap.put(col.getColumnOp().name, col.getColumnOp().fromValue);
    		if(idxSet != null)
        	{
        		Object k = idxSet.mapperKey(fromMap);
        		Set<T> it = (Set<T>) idxSet.getBiggerValue(k, col.getColumnOp().fromInclusive);
        		return it;
        	}
    	}
    	else if(type == QueryType.Less)
    	{
    		Map toMap = new HashMap();
    		toMap.put(col.getColumnOp().name, col.getColumnOp().toValue);
    		if(idxSet != null)
        	{
        		Object k = idxSet.mapperKey(toMap);
        		Set<T> it = (Set<T>) idxSet.getLessValue(k, col.getColumnOp().toInclusive);
        		return it;
        	}
    	}
    	else
    	{
    		LogUtil.getCoreLog().error("Unsupported Query Type: " + type);
    	}
    }
    else if(columnSize == 2)
    {
    	Iterator<ColumnClauseImpl> iterator = columS.iterator();
    	ColumnClauseImpl col1 = iterator.next();
    	ColumnClauseImpl col2 = iterator.next();
    	QueryType type1 = col1.getColumnOp().type;
    	QueryType type2 = col2.getColumnOp().type;
    	
    	if(type1 == QueryType.Equal && type2 == QueryType.Equal)
    	{
    		Map m = new HashMap();
    	    m.put(col1.getColumnOp().name, col1.getColumnOp().value);
    	    m.put(col2.getColumnOp().name, col2.getColumnOp().value);
    	    if(idxSet != null)
        	{
        		Object k = idxSet.mapperKey(m);
        		Set<T> it = (Set<T>) idxSet.getValue(k);
        		return it;
        	}
    	}
    	else if(type1 == QueryType.Less && type2 == QueryType.Bigger)
    	{
    		Map fromMap = new HashMap();
    		Map toMap = new HashMap();
    		fromMap.put(col2.getColumnOp().name, col2.getColumnOp().fromValue);
    		toMap.put(col1.getColumnOp().name, col1.getColumnOp().toValue);
    		if(idxSet != null)
    		{
    			Object fromKey = idxSet.mapperKey(fromMap);
    			Object toKey = idxSet.mapperKey(toMap);
    			Set<T> it = (Set<T>) idxSet.getBetweenValue(fromKey, col2.getColumnOp().fromInclusive, toKey, col1.getColumnOp().toInclusive);
    			return it;
    		}
    	}
    	else if(type1 == QueryType.Bigger && type2 == QueryType.Less)
    	{
    		Map fromMap = new HashMap();
    		Map toMap = new HashMap();
    		fromMap.put(col1.getColumnOp().name, col1.getColumnOp().fromValue);
    		toMap.put(col2.getColumnOp().name, col2.getColumnOp().toValue);
    		if(idxSet != null)
    		{
    			Object fromKey = idxSet.mapperKey(fromMap);
    			Object toKey = idxSet.mapperKey(toMap);
    			Set<T> it = (Set<T>) idxSet.getBetweenValue(fromKey, col1.getColumnOp().fromInclusive, toKey, col2.getColumnOp().toInclusive);
    			return it;
    		}
    	}
    	else
    	{
    		LogUtil.getCoreLog().error("Unsupported Query Type: " + type1 + " and " + type2);
    	}
    }
    else
    {
    	Map m = new HashMap();
    	for (ColumnClauseImpl col : columS)
    	{
    		QueryType type = col.getColumnOp().type;
    		if(type != QueryType.Equal)
    		{
    			LogUtil.getCoreLog().error("Unsupported Query Type: " + type + " and column size: " + columnSize);
        		return new HashSet();
    		}
    		
    		m.put(col.getColumnOp().name, col.getColumnOp().value);
    	}
	
    	if(idxSet != null)
    	{
    		Object k = idxSet.mapperKey(m);
    		Set<T> it = (Set<T>) idxSet.getValue(k);
    		return it;
    	}
    }
	
	return new HashSet();
  }
  
  @Override
  public IndexClause column(String columnName, Object value)
  {
    ColumnClauseImpl colunmClause = new ColumnClauseImpl(this, columnName);
    colunmClause.setValue(value);
    colunmClause.setType(QueryType.Equal);
    columS.add(colunmClause);
    return this;
  }
}
