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

import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class ACKFunction implements Function
{
  private static final long serialVersionUID = 4951779088225695225L;

  @Override
  public boolean hasResult()
  {
    return true;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public void execute(FunctionContext fc)
  {
    Map param = (HashMap)fc.getArguments();
    String command = (String)param.get("command");
    if("ack".equals(command))
    {
      String packetId = (String)param.get("packetId");
      int windowId = (Integer)param.get("windowId");
      FlowController.getInstance().processACK(packetId, windowId);
    }
    else if("abort".equals(command))
    {
      int windowId = (Integer)param.get("windowId");
      FlowController.getInstance().processAbort(windowId);
    }
    else
    {
      LogUtil.getAppLog().error("unknow command:"+command);
    }
    fc.getResultSender().lastResult(command);
  }

  @Override
  public String getId()
  {
    return "ACKFunction";
  }

  @Override
  public boolean optimizeForWrite()
  {
    return false;
  }
  //ack需要ha
  @Override
  public boolean isHA()
  {
    return true;
  }
}
