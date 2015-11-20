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
package gemlite.core.internal.context;

import gemlite.core.internal.index.IndexTool;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.internal.support.jmx.GemliteNode;
import gemlite.core.internal.support.jmx.MBeanHelper;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class IndexContextImpl extends IIndexContext
{
	private String indexName;
	private String indexDef;
	private GemliteSibingsLoader loader;
	private Object indexTool;
	private Object indexData;
	private String regionName;
	private boolean isRegion;
	private Object mbean;
	
	private Object testIndexTool;
	private Object testIndexData;
	
		
	public IndexContextImpl(String indexName, String indexDef, GemliteSibingsLoader loader)
	{
		this.indexName = indexName;
		this.indexDef = indexDef;
		this.loader = loader;
	}
	
	@Override
	public String getIndexName()
	{
		return indexName;
	}

	@Override
	public String getIndexDef() 
	{
		return indexDef;
	}

	@Override
	public GemliteSibingsLoader getLoader()
	{
		return loader;
	}
	
	@Override
	public Object getIndexInstance(boolean test)
	{
		return test?testIndexTool:indexTool;
	}

	@Override
	public Object getIndexData(boolean test)
	{
		return test?testIndexData:indexData;
	}

	@Override
	public void setIndexData(Object data, boolean test) 
	{
		if(test)
			testIndexData = data;
		else
			indexData = data;
	}

	@Override
	public Object getIndexInstance()
	{
		return getIndexInstance(false);
	}

	@Override
	public Object getIndexData() 
	{
		return getIndexData(false);
	}

	@Override
	public void setIndexData(Object data)
	{
		setIndexData(data, false);
	}
	
	@Override
	public String getRegionName() 
	{
		return regionName;
	}

	@Override
	public boolean isRegion()
	{
		return isRegion;
	}

	@Override
	public Object getMbean() 
	{
		return mbean;
	}
	
//	@Override
//	public void register() 
//	{
//		try
//		{
//			String indexName = "";
//			Map<String, byte[]> clazzMap = loader.getAllIndexClasses();
//			for(Iterator<String> it = clazzMap.keySet().iterator(); it.hasNext();)
//			{
//				String className = it.next();
//				byte[] content = clazzMap.get(className);
//				ClassReader cr = new ClassReader(content);
//				ClassNode cn = new ClassNode();
//				cr.accept(cn, 0);
//				
//				if (cn.visibleAnnotations != null)
//			    {
//			      for (int i = 0; i < cn.visibleAnnotations.size(); i++)
//			      {
//			        AnnotationNode ann = (AnnotationNode) cn.visibleAnnotations.get(i);
//			        if ("Lgemlite/core/annotations/domain/AutoSerialize;".equals(ann.desc))
//			        {
//			        	DataSerializeHelper.getInstance().process(cn);
//			          	byte[] newBytes= GemliteHelper.classNodeToBytes(cn);
//			          	loader.addIndexClass(className, newBytes);
//			          	loader.loadClass(className);
//			        }
//			        else if("Lgemlite/core/annotations/IndexRegion;".equals(ann.desc))
//			        {
//			        	Class<?> clazz = loader.loadClass(className);
//			        	IndexRegion bean = clazz.getAnnotation(IndexRegion.class);
//			        	indexName = bean.indexName();
//			        	if(bean.test())
//			        	{
//			        		if(!bean.isRegion())
//			        			registerTestIndexRegion(bean, clazz);
//			        	}
//			        	else
//			        	{
//			        		registerIndexRegion(bean, clazz);
//			        		registerListeners(bean.regionName());
//			        		
//			        		if(!bean.isRegion())
//			        		{
//			        			createMBean();
//			        		}
//			        	}
//			        }
//			      }
//			    }
//				else //…è?{
//				{
//		          	loader.loadClass(className);
//				}
//			}
//			
//			initMappingRegion();
//			long start = System.currentTimeMillis();
//			initIndexData();		
//			long end = System.currentTimeMillis();
//			if (LogUtil.getAppLog().isInfoEnabled())
//				LogUtil.getAppLog().info(
//						indexName + " has been fullCreate, total time is "
//								+ (end - start) + " ms.");
//			
//		}
//		catch (Exception e)
//		{
//			LogUtil.getCoreLog().error("Register Index Classes Fail!", e);
//		}
//
//	}
	
//	private void initIndexData()
//	{
//		IndexTool tool = (IndexTool) indexTool;
//		tool.fullCreate();
//	}
//	
//  	private void initMappingRegion()
//  	{
//  		if(isRegion)
//  		{
//  			IndexTool tool = (IndexTool) indexTool;
//  			tool.init();
//  		}
//  	}
	
//  	synchronized private void registerTestIndexRegion(IndexRegion bean, Class<?> cls) throws InstantiationException, IllegalAccessException
//	{
//  		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
//  		idc.putIndexNamesByTestRegion(bean);
//  		testIndexTool = cls.newInstance();
//
//	    if(LogUtil.getCoreLog().isDebugEnabled())
//	    	LogUtil.getCoreLog().debug("register TestIndexRegion, region: " + bean.regionName() + " index: " + bean.indexName());
//	}
//  	
//	synchronized private void registerIndexRegion(IndexRegion bean, Class<?> cls) throws InstantiationException, IllegalAccessException
//	{
//		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
//		idc.putIndexNamesByRegion(bean);
//		
//	    indexTool = cls.newInstance();
//	    regionName = bean.regionName();
//	    isRegion = bean.isRegion();
//	    
//	    if(LogUtil.getCoreLog().isDebugEnabled())
//	    	LogUtil.getCoreLog().debug("register IndexRegion, region: " + bean.regionName() + " index: " + bean.indexName());
//	}
//	
//	synchronized private void registerListeners(String regionName)
//	{
//		Cache c = CacheFactory.getAnyInstance();
//		Region r = c.getRegion(regionName);
//		
//		if(r != null)
//		{
//			GemliteIndexContext idc = GemliteContext.getTopIndexContext();
//			CacheListener ourListener = idc.getCacheListener(regionName);
//			if(ourListener==null)
//			{
//				ourListener = new EntryChangeListener();
//				AttributesMutator mutator = r.getAttributesMutator();
//				mutator.addCacheListener(ourListener);
//				idc.putCacheListener(regionName, ourListener);
//			}
//
//			if(PartitionRegionHelper.isPartitionedRegion(r))
//			{
//				PartitionListener pl = idc.getPartitionListener(regionName);
//				if(pl == null)
//				{
//					pl = new BucketChangeListener(regionName);
//					GemliteHelper.addPartitionListener(r, pl);
//					idc.putPartitionListener(regionName, pl);
//				}
//			}
//		}
//	}

//	private void createMBean()
//	{
//		String srvName = regionName+"-"+indexName;
//		String oname = MBeanHelper.createMBeanName("index",srvName);
//		
//		Cache c = CacheFactory.getAnyInstance();
//		Region r = c.getRegion(regionName);
//		if(r==null)
//			return;
//		
//		if(PartitionRegionHelper.isPartitionedRegion(r))
//		{
//		    IndexPartitionStat srv = new IndexPartitionStat();
//		    srv.setIndexName(indexName);
//		    srv.setRegionName(regionName);
//		    mbean = srv;
//		    GemliteNode.getInstance().registerBean(oname, srv);
//		}
//		else
//		{
//		    IndexReplicateStat srv = new IndexReplicateStat();
//		    srv.setIndexName(indexName);
//		    srv.setRegionName(regionName);
//		    mbean = srv;
//		    GemliteNode.getInstance().registerBean(oname, srv);
//		}
//	}

	@Override
	public void close()
	{
		if(mbean != null)
		{
			String srvName = regionName+"-"+indexName;
			String oname = MBeanHelper.createMBeanName("index",srvName);
			GemliteNode.getInstance().unRegisterBean(oname);
			mbean = null;
		}
		
		if(StringUtils.isNotEmpty(indexName))
			indexName = null;
		if(StringUtils.isNotEmpty(indexDef))
			indexDef = null;
		if(StringUtils.isNotEmpty(regionName))
			regionName = null;
		
		if(loader != null)
		{
			loader.clean();
			loader = null;
		}
		if(indexTool !=  null)
		{
			IndexTool tool = (IndexTool) indexTool;
	  		tool.clear();
	  		indexTool = null;
	  		indexData = null;
		}
		
		if(testIndexTool !=  null)
			testIndexTool = null;
		if(testIndexData != null)
		{
			((Map<?,?>)testIndexData).clear();
			testIndexData = null;
		}

	}

	@Override
	public void setIndexInstance(Object instance, boolean test)
	{
		if(test)
			testIndexTool = instance;
		else
			indexTool = instance;
	}

	@Override
	public void setRegionName(String regionName) 
	{
		this.regionName = regionName;
	}

	@Override
	public void setRegionType(boolean isRegion)
	{
		this.isRegion = isRegion;
	}

	@Override
	public void setMbean(Object mbean)
	{
		this.mbean = mbean;
	}

}
