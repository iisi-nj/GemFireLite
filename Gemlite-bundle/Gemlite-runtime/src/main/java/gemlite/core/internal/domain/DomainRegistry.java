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

import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.hotdeploy.GemliteClassLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class DomainRegistry
{
  private static Map<String, String> tableToRegionMap = new ConcurrentHashMap<>();
  private static Map<String, String> regionToTableMap = new ConcurrentHashMap<>();
  
  private static Map<String, IMapperTool> itemsByRegion = new ConcurrentHashMap<>();
  
  public final static String tableToRegion(String tableName)
  {
    return tableToRegionMap.get(tableName);
  }
  
  public final static String regionToTable(String regionName)
  {
    return regionToTableMap.get(regionName);
  }
  
  public final static IMapperTool getMapperTool(String regionName)
  {
    IMapperTool ref = itemsByRegion.get(regionName);
//    if (ref == null)
//    {
//      throw new GemliteException("MappTool is null, regionName:" + regionName);
//    }
    return ref;
  }
  
  @SuppressWarnings("unchecked")
  public final static void registerDomain(String tableName, String regionName, String className)
  {
    
    try
    {
      Class<IMapperTool> cls = (Class<IMapperTool>) Class.forName(className, false, GemliteClassLoader.getInstance());
      IMapperTool tool = cls.newInstance();
      tableToRegionMap.put(tableName, regionName);
      regionToTableMap.put(regionName, tableName);
      itemsByRegion.put(regionName, tool);
    }
    catch (Exception e)
    {
      throw new GemliteException("tableName:" + tableName + " regionName:" + regionName + " className:" + className, e);
    }
    
  }
  
}
