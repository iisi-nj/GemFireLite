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
import gemlite.core.internal.index.IndexTool;
import gemlite.core.internal.support.annotations.IndexRegion;
import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.events.SimpleEntryEvent;
import gemlite.core.internal.view.GemliteViewContext;
import gemlite.core.internal.view.bean.ViewItem;
import gemlite.core.internal.view.trigger.ViewTool;
import gemlite.core.util.LogUtil;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.EntryEvent;

public class SyncListenerTool {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void EntryChangeEventOnIndex(EntryEvent event,
			EventStatus status) {
		try {
			SimpleEntryEvent sevent = new SimpleEntryEvent(event);
			String regionName = sevent.getRegion().getName();
			GemliteIndexContext idc = GemliteContext.getTopIndexContext();
			Set<IndexRegion> indexNames = idc.getIndexNamesByRegion(regionName);
			if (indexNames == null)
				return;

			for (IndexRegion name : indexNames) {
				IIndexContext context = idc.getIndexContext(name.indexName());
				IndexTool tool = (IndexTool) context.getIndexInstance();
				if (status == EventStatus.AfterRegionCreate)
					tool.afterInsert(sevent);
				else if (status == EventStatus.AfterRegionUpdate)
					tool.afterUpdate(sevent);
				else if (status == EventStatus.AfterRegionDestroy)
					tool.afterDelete(sevent);
			}
		} catch (Exception e) {
			LogUtil.getMqSyncLog().error(
					">>" + event.getRegion().getName() + " key:"
							+ event.getKey(), e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void BucketChangeEventOnIndex(int bucketId, Iterable<?> keys,
			String regionName, EventStatus status) {
		if (CoreMetaData.batchModeValue || StringUtils.isEmpty(regionName))
			return;

		GemliteIndexContext idc = GemliteContext.getTopIndexContext();
		Set<IndexRegion> indexNames = idc.getIndexNamesByRegion(regionName);
		if (indexNames == null)
			return;
		for (IndexRegion name : indexNames) {
			IIndexContext context = idc.getIndexContext(name.indexName());
			IndexTool tool = (IndexTool) context.getIndexInstance();
			if (status == EventStatus.AfterBucketCreated)
				tool.afterBucketCreated(bucketId, keys);
			else if (status == EventStatus.AfterBucketRemoved)
				tool.afterBucketRemoved(bucketId, keys);

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void EntryChangeEventOnView(EntryEvent event,
			EventStatus status) {
		try {
			String regionName = event.getRegion().getName();
			GemliteViewContext context = GemliteViewContext.getInstance();
			Map<String, Set<String>> regionToView = context.getRegionToView();
			if (regionToView.containsKey(regionName)) {
				SimpleEntryEvent sevent = new SimpleEntryEvent(event);
				Map<String, ViewItem> viewMap = context.getViewContext();
				Set<String> itemSet = regionToView.get(regionName);
				for (String itemName : itemSet) {
					ViewItem viewItem = viewMap.get(itemName);
					ViewTool viewTool = viewItem.getViewTool();
					if (viewTool != null) {
						if (status == EventStatus.AfterRegionCreate)
							viewTool.afterInsert(sevent);
						else if (status == EventStatus.AfterRegionUpdate)
							viewTool.afterUpdate(sevent);
						else if (status == EventStatus.AfterRegionDestroy)
							viewTool.afterDelete(sevent);
					}
				}
			}
		} catch (Exception e) {
			LogUtil.getMqSyncLog().error(
					">>" + event.getRegion().getName() + " key:"
							+ event.getKey(), e);
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void BucketChangeEventOnView(int bucketId, Iterable<?> keys,
			String regionName, EventStatus status) {
		GemliteViewContext context = GemliteViewContext.getInstance();
		Map<String, Set<String>> regionToView = context.getRegionToView();
		if (regionToView.containsKey(regionName)) {
			Map<String, ViewItem> viewMap = context.getViewContext();
			Set<String> itemSet = regionToView.get(regionName);
			for (String itemName : itemSet) {
				ViewItem viewItem = viewMap.get(itemName);
				ViewTool viewTool = viewItem.getViewTool();
				if (viewTool != null) {
					if (status == EventStatus.AfterBucketCreated)
						viewTool.afterBucketCreated(bucketId, keys);
					else if (status == EventStatus.AfterBucketRemoved)
						viewTool.afterBucketRemoved(bucketId, keys);
				}
			}
		}

	}
}
