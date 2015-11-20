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
package gemlite.core.internal.support.jmx.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GemliteNodeConfig implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 2727509463362806627L;
  private String ip;
  private int port;
  private String memberId;
  private String nodeName;
  /***
   * objectName: className
   */
  private Map<String,String> beanNames=new HashMap<>();
  public String getMemberKey()
  {
    StringBuilder bl = new StringBuilder();
    bl.append(ip).append(":").append(nodeName);
    return bl.toString();
  }
  public String getIp()
  {
    return ip;
  }
  public void setIp(String ip)
  {
    this.ip = ip;
  }
  public int getPort()
  {
    return port;
  }
  public void setPort(int port)
  {
    this.port = port;
  }
  public String getMemberId()
  {
    return memberId;
  }
  public void setMemberId(String memberId)
  {
    this.memberId = memberId;
  }
  public Map<String, String> getBeanNames()
  {
    return beanNames;
  }
  public void setBeanNames(Map<String, String> beanNames)
  {
    this.beanNames = beanNames;
  }
  public String getNodeName()
  {
    return nodeName;
  }
  public void setNodeName(String nodeName)
  {
    this.nodeName = nodeName;
  }
  @Override
  public String toString()
  {
    return "GemliteNodeConfig [ip=" + ip + ", port=" + port + ", memberId=" + memberId + ", nodeName=" + nodeName
        + ", beanNames=" + beanNames + "]";
  }
}
