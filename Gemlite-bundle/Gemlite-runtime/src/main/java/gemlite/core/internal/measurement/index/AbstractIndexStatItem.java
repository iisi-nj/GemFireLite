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
package gemlite.core.internal.measurement.index;

import java.io.Serializable;
import java.lang.ref.WeakReference;

public class AbstractIndexStatItem implements Serializable
{
  private static final long serialVersionUID = -5612863243944948139L;
  private long start;
  private long end;
  private String regionName;
  private String indexName;
  
  private transient WeakReference<Thread> thread;

  public long getStart()
  {
    return start;
  }
  public void setStart(long start)
  {
    this.start = start;
  }
  public long getEnd()
  {
    return end;
  }
  public void setEnd(long end)
  {
    this.end = end;
  }
  public WeakReference<Thread> getThread()
  {
    return thread;
  }
  public void setThread(WeakReference<Thread> thread)
  {
    this.thread = thread;
  }
  public String getRegionName() 
  {
	return regionName;
  }
  public void setRegionName(String regionName)
  {
	this.regionName = regionName;
  }
  public String getIndexName()
  {
	return indexName;
  }
  public void setIndexName(String indexName)
  {
	this.indexName = indexName;
  }
  @Override
  public String toString()
  {
	return "AbstractIndexStatItem [start=" + start + ", end=" + end
			+ ", regionName=" + regionName
			+ ", indexName=" + indexName + "]";
  }
  

}
