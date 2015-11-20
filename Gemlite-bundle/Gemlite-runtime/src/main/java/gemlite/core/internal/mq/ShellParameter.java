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

import org.kohsuke.args4j.Option;

public class ShellParameter
{
  @Option(name = "-configXml", usage = "configXml")
  private String configXml;
  @Option(name = "-dao", usage = "")
  private String mapperDaoClass;
  
  @Option(name = "-debug", usage = "debug mode Yes(1) or No(0), default: 0")
  private int debug = 0;
  @Option(name = "-skipError", usage = "skip error Yes or No, default: false")
  private int skipError = 0;
  @Option(name = "-skipErrorCount", usage = "skip error count")
  private int skipErrorCount = 100;
  
  @Option(name = "-b", usage = "batch size, default: 1")
  private int batchSize = 1;
  @Option(name = "-t", usage = "wait tme,default:1000")
  private long waitTime = 1000;
  
  @Option(name = "-async", usage = "default:false ,true/false")
  private String async = "true";
  
  public boolean checkValid()
  {
    return true;
  }
  
  public String getConfigXml()
  {
    return configXml;
  }

  public void setConfigXml(String configXml)
  {
    this.configXml = configXml;
  }

  public String getMapperDaoClass()
  {
    return mapperDaoClass;
  }

  public void setMapperDaoClass(String mapperDaoClass)
  {
    this.mapperDaoClass = mapperDaoClass;
  }

  public boolean isDebug()
  {
    return debug == 1;
  }

  public void setDebug(int debug)
  {
    this.debug = debug;
  }

  public int getSkipError()
  {
    return skipError;
  }

  public void setSkipError(int skipError)
  {
    this.skipError = skipError;
  }

  public int getSkipErrorCount()
  {
    return skipErrorCount;
  }

  public void setSkipErrorCount(int skipErrorCount)
  {
    this.skipErrorCount = skipErrorCount;
  }

  public int getBatchSize()
  {
    return batchSize;
  }

  public void setBatchSize(int batchSize)
  {
    this.batchSize = batchSize;
  }

  public long getWaitTime()
  {
    return waitTime;
  }

  public void setWaitTime(long waitTime)
  {
    this.waitTime = waitTime;
  }

  public String getAsync()
  {
    return async;
  }

  public void setAsync(String async)
  {
    this.async = async;
  }

  @Override
  public String toString()
  {
    return "configXml=" + configXml + "\nmapperDaoClass=" + mapperDaoClass + "\ndebug=" + debug
        + "\nskipError=" + skipError + "\nskipErrorCount=" + skipErrorCount + "\nbatchSize=" + batchSize
        + "\nwaitTime=" + waitTime + "\nasync=" + async ;
  }
}
