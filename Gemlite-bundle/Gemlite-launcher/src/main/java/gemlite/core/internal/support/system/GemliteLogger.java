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
package gemlite.core.internal.support.system;

import gemlite.core.util.LogUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GemliteLogger
{
  private final static Logger mqSyncLog;
  private final static Logger checkLog;
  private final static Logger sqlLog;
  private final static Logger coreLog;
  private final static Logger testLog;
  private final static Logger jmxLog;
  
  static
  {
    coreLog = LogManager.getLogger("gemlite.coreLog");
    mqSyncLog = LogManager.getLogger("gemlite.mqSyncLog");
    checkLog = LogManager.getLogger("gemlite.checkLog");
    sqlLog = LogManager.getLogger("gemlite.sqlLog");
    testLog = LogManager.getLogger("gemlite.testLog");
    jmxLog = LogManager.getLogger("gemlite.jmxLog");
  }
  public static Logger getJMXLog()
  {
    return jmxLog;
  }
  
  public static Logger getTestLog()
  {
    return testLog;
  }
  
  public static Logger getCoreLog()
  {
    return coreLog;
  }
  
  public static Logger getMqSyncLog()
  {
    return mqSyncLog;
  }
  
  public static Logger getCheckLog()
  {
    return checkLog;
  }
  
  public static Logger getSqllog()
  {
    return sqlLog;
  }
  
  private long t1;
  private long t2;
  
  public static LogUtil newInstance()
  {
    LogUtil logUtil = new LogUtil();
    logUtil.startTimer();
    return logUtil;
  }
  
  public void startTimer()
  {
    t1 = System.currentTimeMillis();
  }
  
  public long cost()
  {
    t2 = System.currentTimeMillis();
    long d = t2 - t1;
    startTimer();
    return d;
  }
}
