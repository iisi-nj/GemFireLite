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
package gemlite.core.internal.support.context;

import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.support.system.ServerConfigHelper.TYPES;

public class GemliteRuntimeInfo
{
  private TYPES nodeType;
  private boolean jmxManagerNode;
  private String nodeName;
  private String workPath;
  private String homePath;
  

  public TYPES getNodeType()
  {
    return nodeType;
  }
  public void setNodeType(TYPES nodeType)
  {
    this.nodeType = nodeType;
  }
  public boolean isJmxManagerNode()
  {
    return jmxManagerNode;
  }
  public void setJmxManagerNode(boolean jmxManagerNode)
  {
    this.jmxManagerNode = jmxManagerNode;
  }
  public String getNodeName()
  {
    return nodeName;
  }
  public void setNodeName(String nodeName)
  {
    this.nodeName = nodeName;
  }
  public String getWorkPath()
  {
    return workPath;
  }
  public void setWorkPath(String workPath)
  {
    this.workPath = workPath;
  }
  public String getHomePath()
  {
    return homePath;
  }
  public void setHomePath(String homePath)
  {
    this.homePath = homePath;
  }
  
}
