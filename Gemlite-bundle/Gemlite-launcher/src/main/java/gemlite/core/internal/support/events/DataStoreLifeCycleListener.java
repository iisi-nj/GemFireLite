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
package gemlite.core.internal.support.events;

import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.util.LogUtil;

import org.springframework.context.ApplicationListener;

public class DataStoreLifeCycleListener implements ApplicationListener<GemliteEvent>
{
  @Override
  public void onApplicationEvent(GemliteEvent event)
  {
    if (LogUtil.getCoreLog().isDebugEnabled())
      LogUtil.getCoreLog().debug("GemliteEvent " + event + " received.");
    if (GemliteEvent.STARTED.equals(event.getSource()))
      onStarted();
    else if (GemliteEvent.STOPPED.equals(event.getSource()))
      onStopped();
  }
  
  private void onStarted()
  {
    GemliteContext.getInstance().onStarted();
  }
  
  private void onStopped()
  {
  }
  
}
