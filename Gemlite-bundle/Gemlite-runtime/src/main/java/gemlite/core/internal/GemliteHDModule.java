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
package gemlite.core.internal;

import gemlite.core.annotations.DeployConfigure;
import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.context.GemliteContext;

@DeployConfigure(value=GemliteContext.GEMLITE_RUNTIME_MODULE,type=ModuleType.RUNTIME)
public class GemliteHDModule
{
}
