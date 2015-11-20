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

import gemlite.core.annotations.domain.AutoSerialize.Type;

public interface DomainMojoConstant
{
  public final static String AN_AutoSerialize = "Lgemlite/core/annotations/domain/AutoSerialize;";
  public final static String AN_Table = "Lgemlite/core/annotations/domain/Table;";
  public final static String AN_Region = "Lgemlite/core/annotations/domain/Region;";
  public final static String AN_Key = "Lgemlite/core/annotations/domain/Key;";
  
  public final static String AN_AutoSerialize_Pdx = "Lgemlite/core/annotations/domain/AutoSerialize;" + "_" + Type.PDX.name();  
  public final static String AN_AutoSerialize_Ds = "Lgemlite/core/annotations/domain/AutoSerialize;" + "_" + Type.DS.name();
}
