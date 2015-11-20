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
package gemlite.core.internal.batch;

import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IDataSource;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.domain.utilClass.FileDataSource;
import gemlite.core.util.LogUtil;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

@SuppressWarnings("rawtypes")
public class CommonFileMapper implements FieldSetMapper
{
  private String regionName;
  
  @Override
  public Object mapFieldSet(FieldSet fieldSet) throws BindException
  {
    IMapperTool tool = DomainRegistry.getMapperTool(regionName);
    if (tool == null)
    {
      LogUtil.getCheckLog().info("IMapperTool is null,skip one line.Region:" + regionName);
      return null;
    }
    IDataSource ds = new FileDataSource(fieldSet);
    Object value = tool.mapperValue(ds);
    return value;
  }
  
  public String getRegionName()
  {
    return regionName;
  }
  
  public void setRegionName(String regionName)
  {
    this.regionName = regionName;
  }
  
}
