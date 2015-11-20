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
package gemlite.core.internal.view.bean;

import gemlite.core.annotations.view.TriggerOn;
import gemlite.core.annotations.view.TriggerType;
import gemlite.core.annotations.view.ViewType;
import gemlite.core.internal.view.trigger.ViewTool;

import java.io.Serializable;

public class ViewItem implements Serializable
{
  private static final long serialVersionUID = 2455130910746041035L;
  /**
   * view name
   */
  private String name;
  /**
   * view type(local or partition)
   */
  private ViewType viewType;
  
  /**
   * trigger type(real or delay)
   */
  private TriggerType triggerType;
  
  /**
   * delayTime
   */
  private int delayTime;
  
  private String indexName;
  
  private TriggerOn triggerOn;
  
  private ViewTool<?, ?> viewTool;
  
  private String regionName;
  
  private Object mbean;
  
  public ViewItem()
  {
    
  }
  
  @SuppressWarnings("rawtypes")
  public ViewItem(String name, ViewType viewType, TriggerType triggerType, int delayTime, Class<ViewTool> ViewToolCls)
  {
    this.name = name;
    this.viewType = viewType;
    this.triggerType = triggerType;
    this.delayTime = delayTime;
    try
    {
      this.viewTool = ViewToolCls.newInstance();
    }
    catch (InstantiationException | IllegalAccessException e)
    {
      this.viewTool = null;
      e.printStackTrace();
    }
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public ViewType getViewType()
  {
    return viewType;
  }
  
  public void setViewType(ViewType viewType)
  {
    this.viewType = viewType;
  }
  
  public TriggerType getTriggerType()
  {
    return triggerType;
  }
  
  public void setTriggerType(TriggerType triggerType)
  {
    this.triggerType = triggerType;
  }
  
  public int getDelayTime()
  {
    return delayTime;
  }
  
  public void setDelayTime(int delayTime)
  {
    this.delayTime = delayTime;
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
  
  public TriggerOn getTriggerOn()
  {
    return triggerOn;
  }
  
  public void setTriggerOn(TriggerOn triggerOn)
  {
    this.triggerOn = triggerOn;
  }
  
  public ViewTool<?, ?> getViewTool()
  {
    return viewTool;
  }
  
  public void setViewTool(ViewTool<?, ?> viewTool)
  {
    this.viewTool = viewTool;
  }
  
  @SuppressWarnings("rawtypes")
  public void setViewToolByCls(Class<ViewTool> ViewToolCls)
  {
    try
    {
      this.viewTool = ViewToolCls.newInstance();
    }
    catch (InstantiationException | IllegalAccessException e)
    {
      this.viewTool = null;
      e.printStackTrace();
    }
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + name.hashCode();
    result = prime * result + ((viewType == null) ? 0 : viewType.hashCode());
    result = prime * result + ((triggerType == null) ? 0 : triggerType.hashCode());
    result = prime * result + ((triggerOn == null) ? 0 : triggerOn.hashCode());
    result = prime * result + ((viewTool == null) ? 0 : viewTool.hashCode());
    result = prime * result + ((regionName == null) ? 0 : regionName.hashCode());
    result = prime * result + ((indexName == null) ? 0 : indexName.hashCode());
    
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (getClass() != obj.getClass())
      return false;
    ViewItem old = (ViewItem) obj;
    if (!name.equals(old.name))
      return false;
    if (viewType != old.viewType)
      return false;
    if (triggerType != old.triggerType)
      return false;
    
    if (triggerOn != old.triggerOn)
      return false;
    if (null != regionName && !regionName.equals(old.regionName))
      return false;
    if (null != indexName && !indexName .equals(old.indexName))
      return false;
    
    return true;
  }
  
  public Object getMbean()
  {
    return mbean;
  }
  
  public void setMbean(Object mbean)
  {
    this.mbean = mbean;
  }
  
  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("ViewItem [");
    builder.append("name=").append(name);
    builder.append(", viewType=").append(viewType.name());
    builder.append(", triggerType=").append(triggerType.name());
    builder.append(", delayTime").append(delayTime);
    builder.append(", triggerOn=").append(triggerOn.name());
    builder.append(", viewTool=").append(viewTool.getClass().getName());
    builder.append(", triggerOn=").append(triggerOn.name());
    builder.append(", regionName=").append(regionName);
    builder.append(", indexName=").append(indexName);
    builder.append("]");
    
    return builder.toString();
  }
  
}
