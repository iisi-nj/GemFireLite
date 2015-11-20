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
package gemlite.core.internal.context.registryClass;

import gemlite.core.internal.context.IndexContextImpl;
import gemlite.core.internal.index.IndexTool;
import gemlite.core.internal.measurement.index.IndexPartitionStat;
import gemlite.core.internal.measurement.index.IndexReplicateStat;
import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.annotations.IndexRegion;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.support.jmx.GemliteNode;
import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.core.support.BucketChangeListener;
import gemlite.core.support.EntryChangeListener;
import gemlite.core.util.GemliteHelper;
import gemlite.core.util.LogUtil;

import java.util.Iterator;

import com.gemstone.gemfire.cache.AttributesMutator;
import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.partition.PartitionListener;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;

@GemliteRegistry(value=IndexRegion.class)
public class IndexRegionRegistry extends AbstractGemliteRegistry
{

	@Override
	public void cleanAll() 
	{
  		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
  		if (idc != null)
  		{
  			Iterator<IIndexContext> iters = idc.getIndexContexts();
  			while (iters != null && iters.hasNext())
  			{
				IndexContextImpl context = (IndexContextImpl) iters.next();
				String indexName = "";
				try
				{
					indexName = context.getIndexName();
					idc.removeIndexContext(indexName);
				} catch (Exception e)
				{
					LogUtil.getCoreLog().error(
							"Remove index " + indexName + " failed.", e);
				}
			}	
  		}

	}

	@Override
	public Object getItem(Object key) {
		return null;
	}

	@Override
	protected void doRegistry(IModuleContext context, RegistryParam param)
			throws Exception
	{
		IIndexContext idxContext = (IIndexContext) context;
		String className = param.getClassName();
    	Class<?> clazz = context.getLoader().loadClass(className);
    	IndexRegion bean = clazz.getAnnotation(IndexRegion.class);
    	if(bean.test())
    	{
    		if(!bean.isRegion())
    		{
    	  		Object testIndexTool = clazz.newInstance();
    	  		idxContext.setIndexInstance(testIndexTool, true);
    			registerTestIndexRegion(bean, clazz);
    		}
    	}
    	else
    	{
    	    Object indexTool = clazz.newInstance();
    	    idxContext.setIndexInstance(indexTool, false);
    	    idxContext.setRegionName(bean.regionName());
    	    idxContext.setRegionType(bean.isRegion());
    		registerIndexRegion(bean, clazz);
    		registerListeners(bean.regionName());
    		
    		if(!bean.isRegion())
    		{
    			createMBean(idxContext);
    		}
    		
    		initMappingRegion(idxContext);
            initIndexData(idxContext);  
    	}
	}
	
	
	private void initIndexData(IIndexContext idxContext)
	{
		IndexTool tool = (IndexTool)idxContext.getIndexInstance();
		tool.fullCreate();
	}
	
  	private void initMappingRegion(IIndexContext idxContext)
  	{
  		if(idxContext.isRegion())
  		{
  			IndexTool tool = (IndexTool) idxContext.getIndexInstance();
  			tool.init();
  		}
  	}
  	
	synchronized private void registerTestIndexRegion(IndexRegion bean, Class<?> cls) throws InstantiationException, IllegalAccessException
	{
  		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
  		idc.putIndexNamesByTestRegion(bean);
  		
	    if(LogUtil.getCoreLog().isDebugEnabled())
	    	LogUtil.getCoreLog().debug("register TestIndexRegion, region: " + bean.regionName() + " index: " + bean.indexName());
	}
  	
	synchronized private void registerIndexRegion(IndexRegion bean, Class<?> cls) throws InstantiationException, IllegalAccessException
	{
		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
		idc.putIndexNamesByRegion(bean);
	    
	    if(LogUtil.getCoreLog().isDebugEnabled())
	    	LogUtil.getCoreLog().debug("register IndexRegion, region: " + bean.regionName() + " index: " + bean.indexName());
	}
	
	synchronized private void registerListeners(String regionName)
	{
		Cache c = CacheFactory.getAnyInstance();
		Region r = c.getRegion(regionName);
		
		if(r != null)
		{
			GemliteIndexContext idc = GemliteContext.getTopIndexContext();
			CacheListener ourListener = idc.getCacheListener(regionName);
			if(ourListener==null)
			{
				ourListener = new EntryChangeListener();
				AttributesMutator mutator = r.getAttributesMutator();
				mutator.addCacheListener(ourListener);
				idc.putCacheListener(regionName, ourListener);
			}

			if(PartitionRegionHelper.isPartitionedRegion(r))
			{
				PartitionListener pl = idc.getPartitionListener(regionName);
				if(pl == null)
				{
					pl = new BucketChangeListener(regionName);
					GemliteHelper.addPartitionListener(r, pl);
					idc.putPartitionListener(regionName, pl);
				}
			}
		}
	}
	
	private void createMBean(IIndexContext idxContext)
	{
		String srvName = idxContext.getRegionName()+"-"+ idxContext.getIndexName();
		String oname = MBeanHelper.createMBeanName("index",srvName);
		
		Cache c = CacheFactory.getAnyInstance();
		Region r = c.getRegion(idxContext.getRegionName());
		if(r==null)
			return;
		
		if(PartitionRegionHelper.isPartitionedRegion(r))
		{
		    IndexPartitionStat srv = new IndexPartitionStat();
		    srv.setIndexName(idxContext.getIndexName());
		    srv.setRegionName(idxContext.getRegionName());
		    idxContext.setMbean(srv);
		    GemliteNode.getInstance().registerBean(oname, srv);
		}
		else
		{
		    IndexReplicateStat srv = new IndexReplicateStat();
		    srv.setIndexName(idxContext.getIndexName());
		    srv.setRegionName(idxContext.getRegionName());
		    idxContext.setMbean(srv);
		    GemliteNode.getInstance().registerBean(oname, srv);
		}
	}
}
