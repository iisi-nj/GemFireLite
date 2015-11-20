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
package gemlite.core.support;

import gemlite.core.annotations.view.EventStatus;
import gemlite.core.internal.domain.CoreMetaData;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.partition.PartitionListenerAdapter;

public class BucketChangeListener extends PartitionListenerAdapter
{
	private String regionName;

	public BucketChangeListener(String regionName)
	{
		this.regionName = regionName;
	}

	@Override
	public void afterRegionCreate(Region<?, ?> region1)
	{
		regionName = region1.getName();
	}

	@Override
	public void afterBucketRemoved(int bucketId, Iterable<?> keys)
	{
		if (CoreMetaData.batchModeValue || StringUtils.isEmpty(regionName))
			return;

		SyncListenerTool.BucketChangeEventOnIndex(bucketId, keys, regionName,
				EventStatus.AfterBucketRemoved);
		
		SyncListenerTool.BucketChangeEventOnView(bucketId, keys, regionName,
				EventStatus.AfterBucketRemoved);
	}

	@Override
	public void afterBucketCreated(int bucketId, Iterable<?> keys)
	{
		if (CoreMetaData.batchModeValue || StringUtils.isEmpty(regionName))
			return;

		SyncListenerTool.BucketChangeEventOnIndex(bucketId, keys, regionName,
				EventStatus.AfterBucketCreated);

		SyncListenerTool.BucketChangeEventOnView(bucketId, keys, regionName,
				EventStatus.AfterBucketCreated);
	}

}
