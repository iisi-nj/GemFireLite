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
package gemlite.domain.tools;

import gemlite.domain.tools.context.Column;
import gemlite.domain.tools.context.Table;
import gemlite.domain.tools.template.VelocityHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Exporter
{
  private String suffix = "DBKey";
  private Map<Table, List<Column>> parsedDdls;
  
  public Exporter(Map<Table, List<Column>> parsedDdls)
  {
    this.parsedDdls = parsedDdls;
  }
  
  public void generateJava(String packageName, String outputPath)
  {
    for (Map.Entry entry : this.parsedDdls.entrySet())
    {
      Table table = (Table) entry.getKey();
      List columnList = (List) entry.getValue();
      
      Map paddingMap = new HashMap();
      paddingMap.put("packageName", packageName);
      paddingMap.put("table", table);
      paddingMap.put("columnList", columnList);
      paddingMap.put("suffix", suffix);
      VelocityHelper.mergeTemplate(paddingMap, outputPath + "/" + table.getClassName() + ".java");
      if (table.isHas_key_class())
      {
        table.setClassName(table.getClassName() + suffix);
        paddingMap.put("keyClass", true);
        paddingMap.put("packageName", packageName);
        paddingMap.put("table", table);
        paddingMap.put("columnList", table.getDbKey());
        VelocityHelper.mergeTemplate(paddingMap, outputPath + "/" + table.getClassName() + ".java");
      }
    }
  }
}
