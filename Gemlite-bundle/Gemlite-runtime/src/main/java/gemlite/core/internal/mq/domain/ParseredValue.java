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
package gemlite.core.internal.mq.domain;

import java.util.HashMap;

public class ParseredValue
{
  private long timestamp;
  private String tableName;
  private String op;
  private HashMap<String, String> valueMap=new HashMap<String, String>();
  private HashMap<String, String> updateMap=new HashMap<String, String>();
  private HashMap<String, String> whereMap=new HashMap<String, String>();
  private Object domain;
  public String getTableName()
  {
    return tableName;
  }
  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }
  public String getOp()
  {
    return op;
  }
  public void setOp(String op)
  {
    this.op = op;
  }
  public HashMap<String, String> getValueMap()
  {
    return valueMap;
  }
  public void setValueMap(HashMap<String, String> valueMap)
  {
    this.valueMap = valueMap;
  }
  public long getTimestamp()
  {
    return timestamp;
  }
  public void setTimestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }
  public Object getDomain()
  {
    return domain;
  }
  public void setDomain(Object domain)
  {
    this.domain = domain;
  }
  public HashMap<String, String> getUpdateMap()
  {
    return updateMap;
  }
  public void setUpdateMap(HashMap<String, String> updateMap)
  {
    this.updateMap = updateMap;
  }
  public HashMap<String, String> getWhereMap()
  {
    return whereMap;
  }
  public void setWhereMap(HashMap<String, String> whereMap)
  {
    this.whereMap = whereMap;
  }
  
  @Override
  public String toString() {
	return "ParseredValue [timestamp=" + timestamp + ", tableName=" + tableName
			+ ", op=" + op + ", valueMap=" + valueMap + ", updateMap="
			+ updateMap + ", whereMap=" + whereMap + ", domain=" + domain + "]";
}
  
  
}
