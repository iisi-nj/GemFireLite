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
package gemlite.sample.order.common;
//
//import gemlite.core.support.CompoundObject;
//import gemlite.core.support.CompoundObjectFactory;
//import gemlite.sample.order.domain.CustomerDBKey;
//
//import com.gemstone.gemfire.cache.EntryOperation;
//import com.gemstone.gemfire.cache.PartitionResolver;
//
//public class CompoundFilterDomainResolver implements PartitionResolver<Object, Object> 
//{
//	public void close()
//	{
//		
//	}
//
//	public String getName() 
//	{
//		return getClass().getName();
//	}
//
//	public Object getRoutingObject(EntryOperation<Object, Object> op) 
//	{
//		Object key = op.getKey();
//		if(key instanceof CustomerDBKey)
//		{
//			CustomerDBKey ckey = (CustomerDBKey) key;
//			CompoundObject filter = CompoundObjectFactory.newCompoundFilter();
//			filter.addElement("name", ckey.getName()).addElement("sex", ckey.getSex());
//			return filter;
//		}
//		return key;
//	}
//}
