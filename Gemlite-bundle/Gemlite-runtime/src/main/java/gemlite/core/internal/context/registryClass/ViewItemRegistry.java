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

import gemlite.core.annotations.view.View;
import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.view.GemliteViewContext;
import gemlite.core.internal.view.bean.ViewItem;
import gemlite.core.internal.view.trigger.ViewTool;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

@GemliteRegistry(View.class)
public class ViewItemRegistry extends AbstractGemliteRegistry
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void doRegistry(IModuleContext context, RegistryParam param)
			throws Exception
	{
		String className = param.getClassName();
		Class<ViewTool> cls = (Class<ViewTool>) context.getLoader()
				.loadClass(className);
		View f = cls.getAnnotation(View.class);
		ViewItem item = new ViewItem();
		//item.setName(f.name());
		String viewName = f.name();
		if (StringUtils.isEmpty(viewName))
			viewName = cls.getSimpleName();
		
		item.setName(viewName);
		item.setViewType(f.viewType());
		item.setViewToolByCls(cls);
		item.setTriggerType(f.triggerType());
		item.setDelayTime(f.delayTime());
		item.setRegionName(f.regionName());
		item.setIndexName(f.indexName());
		item.setTriggerOn(f.triggerOn());
		GemliteViewContext.getInstance().createViewItem(item);
	}

	@Override
	public void cleanAll()
	{
		GemliteViewContext context = GemliteViewContext.getInstance();
		Map<String, ViewItem> viewMap = context.getViewContext();
		if (viewMap != null && viewMap.values() != null
				&& viewMap.values().size() > 0)
		{
			Iterator<ViewItem> iters = viewMap.values().iterator();
			while (iters.hasNext())
			{
				ViewItem item = iters.next();
				if (item != null)
				{
					context.removeViewItem(item.getName());
				}
			}
		}
	}

	@Override
	public Object getItem(Object key)
	{
		return null;
	}

}
