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
package gemlite.domain.tools.context;

import java.util.List;

public class Table
{
  private String tableName;
  private String regionName;
  private String className;
  private boolean single_filed_pk;
  private boolean has_key_class;
  
  private List<Column> dbKey;
  
  public String getTableName()
  {
    return this.tableName;
  }
  
  public void setTableName(String tableName)
  {
    this.tableName = tableName;
    this.className = (Character.toUpperCase(tableName.charAt(0)) + tableName.substring(1));
  }
  
  public String getClassName()
  {
    return this.className;
  }
  
  public String getRegionName()
  {
    return this.regionName;
  }
  
  public void setRegionName(String regionName)
  {
    this.regionName = regionName;
  }
  
  public List<Column> getDbKey()
  {
    return dbKey;
  }
  
  public void setDbKey(List<Column> dbKey)
  {
    this.dbKey = dbKey;
  }

  public boolean isSingle_filed_pk()
  {
    return single_filed_pk;
  }

  public void setSingle_filed_pk(boolean single_filed_pk)
  {
    this.single_filed_pk = single_filed_pk;
  }
  
  public void setClassName(String className)
  {
    this.className = className;
  }

  public boolean isHas_key_class()
  {
    return has_key_class;
  }

  public void setHas_key_class(boolean has_key_class)
  {
    this.has_key_class = has_key_class;
  }
}
