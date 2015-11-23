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
package gemlite.core.internal.admin.snapshot;

import java.io.File;
import java.io.IOException;

import com.gemstone.gemfire.cache.execute.ResultSender;

@SuppressWarnings({"rawtypes" })
public abstract interface RegionSnapshotService<K, V>
{
  public abstract SnapshotOptions<K, V> createOptions();
  
  public abstract void save(File paramFile, SnapshotOptions.SnapshotFormat paramSnapshotFormat,ResultSender sender) throws IOException;
  
  public abstract void save(File paramFile, SnapshotOptions.SnapshotFormat paramSnapshotFormat,
      SnapshotOptions<K, V> paramSnapshotOptions,ResultSender sender) throws IOException;
  
  public abstract void load(File paramFile, SnapshotOptions.SnapshotFormat paramSnapshotFormat,ResultSender sender) throws IOException,
      ClassNotFoundException;
  
  public abstract void load(File paramFile, SnapshotOptions.SnapshotFormat paramSnapshotFormat,
      SnapshotOptions<K, V> paramSnapshotOptions,ResultSender sender) throws IOException, ClassNotFoundException;
}
