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
package gemlite.core.internal.support.hotdeploy.scanner;


public class ScannedRegistryDefine implements Comparable<ScannedRegistryDefine>
{
  String matchKey;
  String registryClassName;
  int priority;
  boolean noninstance;
  
  public ScannedRegistryDefine(boolean noninstance)
  {
    this();
    this.noninstance = noninstance;
  }
  
  public ScannedRegistryDefine()
  {
  }
  
  
  @Override
  public int compareTo(ScannedRegistryDefine o)
  {
    int n = priority - o.priority;
    if(n==0)
    {
      n=o.matchKey.compareTo(matchKey);
    }
    return n;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ScannedRegistryDefine other = (ScannedRegistryDefine) obj;
    if (matchKey == null)
    {
      if (other.matchKey != null)
        return false;
    }
    else if (!matchKey.equals(other.matchKey))
      return false;
    return true;
  }
}