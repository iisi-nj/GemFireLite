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
package gemlite.core.internal.mq;

/**
 * @author ynd
 *
 */
public class MqReceiveException extends RuntimeException
{
  private static final long serialVersionUID = -8486414477391418789L;
  private boolean shouldStop = true;  
  private boolean needReconnect = false;
  
  
  public MqReceiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  public MqReceiveException(String message, Throwable cause)
  {
    super(message, cause);
  }
  public MqReceiveException(String message)
  {
    super(message);
  }
  /**
   * @param shouldStop
   */
  public MqReceiveException(boolean shouldStop)
  {
    super();
    this.shouldStop = shouldStop;
  }
  public MqReceiveException()
  {
  }
  
  
  /**
   * @param cause
   */
  public MqReceiveException(Throwable cause)
  {
    super(cause);
  }
  /**
   * @return the shouldStop
   */
  public boolean isShouldStop()
  {
    return shouldStop;
  }
  /**
   * @param shouldStop the shouldStop to set
   */
  public void setShouldStop(boolean shouldStop)
  {
    this.shouldStop = shouldStop;
  }
  public boolean isNeedReconnect()
  {
    return needReconnect;
  }
  public void setNeedReconnect(boolean needReconnect)
  {
    this.needReconnect = needReconnect;
  }
}
