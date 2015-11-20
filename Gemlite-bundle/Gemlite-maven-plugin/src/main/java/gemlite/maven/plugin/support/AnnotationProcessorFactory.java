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
package gemlite.maven.plugin.support;

import gemlite.maven.plugin.support.mapper.MapperToolProcessor;
import gemlite.maven.plugin.support.serialize.DataSerializeProcessor;
import gemlite.maven.plugin.support.serialize.PdxSerializeProcessor;

import java.util.HashMap;
import java.util.Map;

public class AnnotationProcessorFactory implements DomainMojoConstant
{
  
  private final static Map<String, ClassProcessor> processorMap = new HashMap<>();
  static
  {
    ClassProcessor process = new DataSerializeProcessor();
    processorMap.put(AN_AutoSerialize_Ds, process);
    
    process = new PdxSerializeProcessor();
    processorMap.put(AN_AutoSerialize_Pdx, process);
    
    
    process = new MapperToolProcessor();
    processorMap.put(AN_Table, process);
    processorMap.put(AN_Region, process);
    processorMap.put(AN_Key, process);
    
    
  }
  
  public final static ClassProcessor getProcessor(String annDesc)
  {
    return processorMap.get(annDesc);
  }
  
}
