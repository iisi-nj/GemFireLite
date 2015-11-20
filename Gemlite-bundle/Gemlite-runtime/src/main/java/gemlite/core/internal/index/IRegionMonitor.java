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

import gemlite.core.internal.support.events.SimpleEntryEvent;

/***********************************************************************
 * Module: IRegionMonitor.java
 * Author: ynd
 * Purpose: Defines the Interface IRegionMonitor
 ***********************************************************************/
public interface IRegionMonitor<K,V>
{
  
  public void afterInsert(SimpleEntryEvent<K,V> e);
  
  public void afterDelete(SimpleEntryEvent<K,V> e);
  
  public void afterUpdate(SimpleEntryEvent<K,V> e);
  
  public void afterBucketCreated(int bucketId, Iterable<K> keys);
  
  public void afterBucketRemoved(int bucketId, Iterable<K> keys);
  
}