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
package com.jnj.adf.grid.support.listener;

import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionEvent;
import com.gemstone.gemfire.cache.partition.PartitionListener;

/**
 * @author dyang39
 *
 */
@SuppressWarnings("rawtypes")
public class PartitionRegionIndexListener implements PartitionListener,CacheListener{

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheCallback#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterCreate(com.gemstone.gemfire.cache.EntryEvent)
	 */
	@Override
	public void afterCreate(EntryEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterUpdate(com.gemstone.gemfire.cache.EntryEvent)
	 */
	@Override
	public void afterUpdate(EntryEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterInvalidate(com.gemstone.gemfire.cache.EntryEvent)
	 */
	@Override
	public void afterInvalidate(EntryEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterDestroy(com.gemstone.gemfire.cache.EntryEvent)
	 */
	@Override
	public void afterDestroy(EntryEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionInvalidate(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionInvalidate(RegionEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionDestroy(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionDestroy(RegionEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionClear(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionClear(RegionEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionCreate(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionCreate(RegionEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionLive(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionLive(RegionEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.partition.PartitionListener#afterPrimary(int)
	 */
	@Override
	public void afterPrimary(int bucketId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.partition.PartitionListener#afterRegionCreate(com.gemstone.gemfire.cache.Region)
	 */
	@Override
	public void afterRegionCreate(Region<?, ?> region) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.partition.PartitionListener#afterBucketRemoved(int, java.lang.Iterable)
	 */
	@Override
	public void afterBucketRemoved(int bucketId, Iterable<?> keys) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.partition.PartitionListener#afterBucketCreated(int, java.lang.Iterable)
	 */
	@Override
	public void afterBucketCreated(int bucketId, Iterable<?> keys) {
		// TODO Auto-generated method stub
		
	}

}
