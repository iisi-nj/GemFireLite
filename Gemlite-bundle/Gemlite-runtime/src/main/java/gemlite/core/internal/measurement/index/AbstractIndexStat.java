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
package gemlite.core.internal.measurement.index;

import gemlite.core.api.index.IndexEntrySet;
import gemlite.core.common.DateUtil;
import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.index.IndexTool;
import gemlite.core.internal.jmx.manage.KeyConstants.Indexs;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.jmx.AggregateType;
import gemlite.core.internal.support.jmx.annotation.AggregateAttribute;
import gemlite.core.internal.support.jmx.annotation.AggregateOperation;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;


class BigComparator implements Serializable, Comparator<AbstractIndexStatItem>
{
  private static final long serialVersionUID = 1245326027718944331L;
  
  public BigComparator()
  {
  }
  
  @Override
  public int compare(AbstractIndexStatItem o1, AbstractIndexStatItem o2)
  {
    Long cost1 = o1.getEnd() - o1.getStart();
    Long cost2 = o2.getEnd() - o2.getStart();
    return cost1.compareTo(cost2); 
  }
}

class RecentComparator implements Comparator<AbstractIndexStatItem>, Serializable
{
  private static final long serialVersionUID = -7849435461730512754L;

  public RecentComparator()
  {
  }
  
  @Override
  public int compare(AbstractIndexStatItem o1, AbstractIndexStatItem o2)
  {
	Long timestamp1 = o1.getEnd();
	Long timestamp2 = o2.getEnd();
    return timestamp1.compareTo(timestamp2);
  }
}


@SuppressWarnings({"rawtypes","unchecked"})
public abstract class AbstractIndexStat
{
	private String regionName;
	private String indexName;
	
	protected AtomicLong entrySize = new AtomicLong();
	
	private AtomicLong totalCount = new AtomicLong();
	private AtomicLong totalCost = new AtomicLong();
	private int maxHistorySize =3;
	private int recentHistorySize= 10;
	private int tpsRange = 2;// seconds
	private ConcurrentSkipListSet<AbstractIndexStatItem> maxHistory;
	private ConcurrentSkipListSet<AbstractIndexStatItem> recentHistory;
	private long lastCost;
	private long maxCost;
	private long avgCost;
	  
	private long firstAt;
	private long lastAt;
	  
	private int tps;
	  
	private long prevTimestamp;
	private long prevCount;
	private Map<String,String> keyFields = null; //Name->Type
	private Map<String,String> valueFields = null; //Name->Type
  
	public AbstractIndexStat()
	{
	    maxHistory = new ConcurrentSkipListSet<>(new BigComparator());
	    recentHistory = new ConcurrentSkipListSet<>(new RecentComparator());
	}
	
	private Map<String,String> getValueFields()
	{
		if(valueFields == null)
		{
			valueFields = new HashMap<>();
			try
			{
				IMapperTool mapperTool = DomainRegistry.getMapperTool(regionName);
				Class keyClass = mapperTool.getKeyClass();
				Collection keyFields = mapperTool.getKeyFieldNames();
				
				if(keyFields.size() == 1)
				{
					String fieldName = (String) keyFields.iterator().next();
					String type = keyClass.getSimpleName();
					valueFields.put(fieldName, type);
				}
				else
				{
					for(Iterator it = keyFields.iterator(); it.hasNext();)
					{
						String fieldName = (String) it.next();
						Field field = keyClass.getDeclaredField(fieldName);
						String type = field.getType().getSimpleName();
						valueFields.put(fieldName, type);
					}
				}
			}
			catch(Exception e)
			{
				LogUtil.getCoreLog().error("Get Index Value Info error.", e);
			}
		}
		
		return valueFields;
	}
	
	private Map<String,String> getKeyFields()
	{
		if(keyFields == null)
		{
			keyFields = new HashMap<>();
			try
			{
				GemliteIndexContext idc = GemliteContext.getTopIndexContext();
				IIndexContext context = idc.getIndexContext(indexName);
				if(context != null)
				{
					IndexTool indexTool = (IndexTool) context.getIndexInstance();
					Set idxKeyFields = indexTool.getKeyFieldNames();
					IMapperTool mapperTool = DomainRegistry.getMapperTool(regionName);
					Class valueClass = mapperTool.getValueClass();
					for(Iterator it2 = idxKeyFields.iterator(); it2.hasNext();)
					{
						String idxField = (String) it2.next();
						Field field = valueClass.getDeclaredField(idxField);
						String type = field.getType().getSimpleName();
						keyFields.put(idxField, type);
					}
				}
			}
			catch(Exception e)
			{
				LogUtil.getCoreLog().error("Get Index Key Info error.", e);
			}
		}
		return keyFields;
	}
	
	public void calcuteTPS()
	{
		long timestamp = System.currentTimeMillis();
	    long past = timestamp - prevTimestamp;
	    if (prevTimestamp == 0)
	      prevTimestamp = timestamp;
	    else if (past > (tpsRange * 1000))
	    {
	      long thisCount = totalCount.get();
	      long count = thisCount - prevCount;
	      tps = (int) (count * 1000 / past);
	      prevTimestamp = timestamp;
	      prevCount = thisCount;
	    }
	}
	  
	  /***
	   * ¿‰h
	   * 
	   * @param item
	   */
	  public void recordItem(AbstractIndexStatItem item)
	  {
	    if(firstAt==0)
	      firstAt = item.getStart();
	    lastAt = item.getEnd();
	    long cost = item.getEnd() - item.getStart();
	    if(cost>maxCost)
	      maxCost = cost;
	    lastCost = cost;
	    
	    if(totalCount.get()>0)
	    	avgCost = (int)( totalCost.get()/totalCount.get());
	    
	    maxHistory.add(item);
	    recentHistory.add(item);
	    if(maxHistory.size()>maxHistorySize)
	      maxHistory.pollFirst();
	    if(recentHistory.size()>recentHistorySize)
	      recentHistory.pollFirst();
	    
	  }
	  
	  @ManagedOperation
	  public void resetStat()
	  {
		totalCount.set(0l);  
		totalCost.set(0l);
	    maxCost=0l;
	    lastCost=0l;
	    avgCost=0l;
	    maxHistory.clear();
	    recentHistory.clear();
	    firstAt = 0l;
	    lastAt = 0l;
	    tps = 0;
	    prevTimestamp =0l;
	    prevCount = 0l;
	  }
	  
	  public void incrTotalCount()
	  {
	    totalCount.incrementAndGet();
	  }
	  
	  public void incrTotalCost(long cost)
	  {
	    totalCost.addAndGet(cost);
	  }
	  
	  @AggregateAttribute(AggregateType.SUM)
	  @ManagedAttribute(description = ";!p")
	  public long getTotalCount()
	  {
	    return totalCount.get();
	  }
	  
	  @AggregateAttribute(AggregateType.SUM)
	  @ManagedAttribute(description = ";ö")
	  public long getTotalCost()
	  {
	    return totalCost.get();
	  }
	
	  @AggregateAttribute(AggregateType.SAME)
	  @ManagedAttribute(description = ";ö „†ò°UÝXpÏ")
	  public int getMaxHistorySize()
	  {
	    return maxHistorySize;
	  }
	  
	  @AggregateAttribute(AggregateType.SAME)
	  @ManagedAttribute(description = " Ñ(„†ò°UÝXpÏ")
	  public int getRecentHistorySize()
	  {
	    return recentHistorySize;
	  }
	  
	  @AggregateAttribute(AggregateType.MAX)
	  @ManagedAttribute
	  public long getLastAt()
	  {
	    return lastAt;
	  }
	  
	  @AggregateAttribute(AggregateType.MIN)
	  @ManagedAttribute
	  public long getFirstAt()
	  {
	    return firstAt;
	  }
	  @AggregateAttribute(AggregateType.SAME)
	  @ManagedAttribute(description="¡—TPSô”öô")
	  public int getTpsRange()
	  {
	    return tpsRange;
	  }
	  
	  @AggregateAttribute(AggregateType.SUM)
	  @ManagedAttribute(description = "tps")
	  public int getTps()
	  {
	    return tps;
	  }
	  
	  @AggregateAttribute(AggregateType.MAX)
	  @ManagedAttribute(description = " Ñ !(„ö")
	  public long getLastCost()
	  {
	    return lastCost;
	  }
	  @AggregateAttribute(AggregateType.MAX)
	  @ManagedAttribute(description = " 'ö")
	  public long getMaxCost()
	  {
	    return maxCost;
	  }
	  @AggregateAttribute(AggregateType.AVG)
	  @ManagedAttribute(description = "sGö")  
	  public long getAvgCost()
	  {
		return avgCost;
	  }
	  
	  @AggregateOperation(support=true)
	  @ManagedOperation
	  public List<AbstractIndexStatItem> getMaxHistory()
	  {
	    List<AbstractIndexStatItem> list = new ArrayList<>();
	    list.addAll(maxHistory);
	    return list;
	  }
	  @AggregateOperation(support=true)
	  @ManagedOperation
	  public List<AbstractIndexStatItem> getRecentHistory()
	  {
	    List<AbstractIndexStatItem> list = new ArrayList<>();
	    list.addAll(recentHistory);
	    return list;
	  }
	  
	@ManagedAttribute(description="Region Name")
	public String getRegionName()
	{
		return regionName;
	}
	
	public void setRegionName(String regionName)
	{
		this.regionName = regionName;
	}
	
	@ManagedAttribute(description="Index Name")
	public String getIndexName()
	{
		return indexName;
	}
	
	public void setIndexName(String indexName) 
	{
		this.indexName = indexName;
	}
	
	public void incrEntrySize()
	{
		entrySize.incrementAndGet();
	}
	
	public void decrEntrySize()
	{
		entrySize.decrementAndGet();
	}
	
	public abstract long getEntrySize();
	public abstract boolean isPartitionedRegion();

	public Map describeIndex()
	{
		Map<String,Object> infoMap = new HashMap<String,Object>();
		infoMap.put(Indexs.indexName.name(), indexName);
		infoMap.put(Indexs.regionName.name(), regionName);
		infoMap.put(Indexs.regionType.name(), isPartitionedRegion()?"Partitioned":"Replicated");
		infoMap.put(Indexs.entrySize.name(), getEntrySize());
	    keyFields = getKeyFields();
	    valueFields = getValueFields();
		infoMap.put(Indexs.keyFields.name(), keyFields);
		infoMap.put(Indexs.valueFields.name(), valueFields);
		
		return infoMap;
	}
	
	private Object transformParameter(String key, String value)
	{
		Object result = null;
		String type = getKeyFields().get(key);
		if (type.equalsIgnoreCase("String"))
			result = value;
		else if(type.equalsIgnoreCase("int"))
			result = Integer.parseInt(value);
		else if(type.equalsIgnoreCase("Integer"))
			result = new Integer(value);
		else if(type.equalsIgnoreCase("long"))
			result = Long.parseLong(value);
		else if(type.equalsIgnoreCase("Long"))
			result = new Long(value);
		else if(type.equalsIgnoreCase("double"))
			result = Double.parseDouble(value);
		else if(type.equalsIgnoreCase("Double"))
			result = new Double(value);
		else if(type.equalsIgnoreCase("float"))
			result = Float.parseFloat(value);
		else if(type.equalsIgnoreCase("Float"))
			result = new Float(value);
		else if(type.equalsIgnoreCase("boolean"))
			result = Boolean.parseBoolean(value);
		else if(type.equalsIgnoreCase("Boolean"))
			result = new Boolean(value);
		else if(type.equalsIgnoreCase("Date"))
			result = DateUtil.parse(value);
		else
			result = value;
		
		return result;
		
	}

	public String printStrIndexValue(String searchParamStr, int pageNumber, int pageSize)
	{
    	try
    	{
	    	if(StringUtils.isNotEmpty(searchParamStr))
	    	{
	    		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
	    		IIndexContext context = idc.getIndexContext(indexName);
	    		if(context != null)
	    		{
	    			IndexTool idxTool = (IndexTool) context.getIndexInstance();
					IndexEntrySet<Object, Set<Object>> indexData = idxTool.getIndex();
	    			String[] pairs = searchParamStr.split("&&");
		    		Map<String, Object> searchParams = new HashMap<String, Object>();
		    		for(String pair : pairs)
		    		{
		    			String[] kv = pair.split("=");
		    			String key = kv[0];
		    			String value = kv[1];
		    			searchParams.put(key, transformParameter(key, value));
		    		}
		    		Object idxKey = indexData.mapperKey(searchParams);
		    		Set<Object> value = (Set<Object>) indexData.getValue(idxKey);
		    		if(value != null)
		    		{
			    		List<Object> rnValue = new ArrayList(value);
		    			StringBuffer sb = new StringBuffer();
		    			sb.append("MemberID: "+ ServerConfigHelper.getProperty("MEMBER_ID"));
		    			sb.append("\tIP: " + ServerConfigHelper.getConfig(ITEMS.BINDIP));
		    			sb.append("\tPort: " + ServerConfigHelper.getProperty("JMX_RMI_PORT"));
		    			sb.append("\tEntrySize: " + rnValue.size());
		    			sb.append("\tPageNumber: " + pageNumber);
		    			sb.append("\tPageSize: " + pageSize + "\n");
		    			
		    			if(pageNumber<1 || pageSize<1) 
		    				return "";
		    			
		    			int startPos = (pageNumber-1)*pageSize;
		    			int endPos = Math.min(pageNumber*pageSize, rnValue.size());
		    			
		    			List<Object> pageList = rnValue.subList(startPos, endPos);	    			
		    			for(Iterator<Object> it = pageList.iterator(); it.hasNext();)
		    			{
		    				Object obj = it.next();
		    				sb.append(obj.toString()+"\n");
		    			}
		    			return sb.toString();
		    		}
	    		}
	    		
	    	}
    	}
    	catch (Exception e)
    	{
    		LogUtil.getCoreLog().error("Print Index Value Error, searchParamStr=" + searchParamStr, e);
    	}
    	return "";
	}
	
	public Map<String, Object> printIndexValue(String searchParamStr, int pageNumber, int pageSize)
	{
		Map<String, Object> rtMap = new HashMap<String, Object>();
		List<Object> valueList = new ArrayList<Object>();
		try
		{
			if (StringUtils.isNotEmpty(searchParamStr))
			{
				GemliteIndexContext idc = GemliteContext.getTopIndexContext();
				IIndexContext context = idc.getIndexContext(indexName);
				if (context != null)
				{
					IndexTool idxTool = (IndexTool) context.getIndexInstance();
					IndexEntrySet<Object, Set<Object>> indexData = idxTool
							.getIndex();
					String[] pairs = searchParamStr.split("&&");
					Map<String, Object> searchParams = new HashMap<String, Object>();
					for (String pair : pairs)
					{
						String[] kv = pair.split("=");
						String key = kv[0].trim();
						String value = kv[1].trim();
						searchParams.put(key, transformParameter(key, value));
					}
					Object idxKey = indexData.mapperKey(searchParams);
					Set<Object> value = (Set<Object>) indexData
							.getValue(idxKey);
					if (value != null)
					{
						List<Object> rnValue = new ArrayList(value);
						rtMap.put("MemberID",
								ServerConfigHelper.getProperty("MEMBER_ID"));
						rtMap.put("IP",
								ServerConfigHelper.getConfig(ITEMS.BINDIP));
						rtMap.put("EntrySize", rnValue.size());
						rtMap.put("PageNumber", pageNumber);
						rtMap.put("PageSize", pageSize);

						if (pageNumber < 1 || pageSize < 1)
							return rtMap;

						int startPos = (pageNumber - 1) * pageSize;
						int endPos = Math.min(pageNumber * pageSize,
								rnValue.size());

						List<Object> pageList = rnValue.subList(startPos,
								endPos);
						for (Iterator<Object> it = pageList.iterator(); it
								.hasNext();)
						{
							Object obj = it.next();
							valueList.add(obj.toString());
						}
						rtMap.put("EntryValue", valueList);
						return rtMap;
					}
				}

			}
		} catch (Exception e)
		{
			LogUtil.getCoreLog()
					.error("Print Index Value Error, searchParamStr="
							+ searchParamStr, e);
		}
		return rtMap;
	}

}
