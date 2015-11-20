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
package gemlite.core.internal.domain;

import gemlite.core.internal.domain.utilClass.DBDataSource;
import gemlite.core.internal.domain.utilClass.FileDataSource;
import gemlite.core.internal.domain.utilClass.MqDataSource;

import java.sql.ResultSet;
import java.util.Map;

import org.springframework.batch.item.file.transform.FieldSet;

public class DomainUtil
{
  
  public static String[] gs_date_pattern = new String[] { "yyyyMMdd", "yyyy-MM-dd HH:mm:ss" };
  
  public final static IDataSource getDataSource(ResultSet rs)
  {
    return new DBDataSource(rs);
  }
  
  public final static IDataSource getDataSource(Map<String, String> map)
  {
    return new MqDataSource(map);
  }
  
  public final static IDataSource getDataSource(FieldSet fs)
  {
    return new FileDataSource(fs);
  }
  
}
