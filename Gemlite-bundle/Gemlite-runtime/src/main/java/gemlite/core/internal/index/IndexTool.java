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
package gemlite.core.internal.index;

import gemlite.core.api.index.IndexEntrySet;
import gemlite.core.internal.view.bean.ViewItem;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gemstone.gemfire.cache.Region;


public interface IndexTool<K,V> extends IRegionMonitor<K,V>
{
	public Region init();
	public void fullCreate();
	public void clear();
	public Class getKeyClass();
	public Set getKeyFieldNames();
	public IndexEntrySet getIndex();
	
	Iterator getAllDataValues(String regionName);
	
	Map<String, ViewItem> getViewContext();
	void registerViewItem(ViewItem item);
	void removeViewItem(String viewName);
}
