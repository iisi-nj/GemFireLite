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
package gemlite.core.internal.measurement.view;

import gemlite.core.annotations.view.TriggerOn;

import java.io.Serializable;
import java.lang.ref.WeakReference;

public class AbstractViewStatItem implements Serializable
{
	private static final long serialVersionUID = 4366187863134710596L;
	private long start;
	private long end;
	private String targetName;
	private TriggerOn triggerOn;
	private String viewName;

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

	public String getTargetName()
	{
		return targetName;
	}

	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}

	public TriggerOn getTriggerOn()
	{
		return triggerOn;
	}

	public void setTriggerOn(TriggerOn triggerOn)
	{
		this.triggerOn = triggerOn;
	}

	public String getViewName()
	{
		return viewName;
	}

	public void setIndexName(String viewName)
	{
		this.viewName = viewName;
	}

	@Override
	public String toString()
	{
		if (TriggerOn.INDEX.equals(triggerOn))
		{
			return "AbstractViewStatItem [start=" + start + ", end=" + end
					+ ", indexName=" + targetName + ", viewName=" + viewName
					+ "]";
		} else
		{
			return "AbstractViewStatItem [start=" + start + ", end=" + end
					+ ", regionName=" + targetName + ", viewName=" + viewName
					+ "]";
		}

	}

}
