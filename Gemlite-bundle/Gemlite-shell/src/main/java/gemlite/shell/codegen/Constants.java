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
package gemlite.shell.codegen;

public final class Constants
{
  public static final String MINOR_VERSION = "1.0";
  public static final String VERSION = "1.0.0";
  public static final String FULL_VERSION = "1.0.0";
  public static final String XSD_RUNTIME = "gemlite-runtime-1.0.0.xsd";
  public static final String NS_RUNTIME = "http://www.gemlite.org/xsd/" + XSD_RUNTIME;
  public static final String XSD_META = "gemlite-meta-1.0.0.xsd";
  public static final String NS_META = "http://www.gemlite.org/xsd/" + XSD_META;
  public static final String XSD_CODEGEN = "gemlite-codegen-1.0.0.xsd";
  public static final String NS_CODEGEN = "http://www.gemlite.org/xsd/" + XSD_CODEGEN;
  public static final int MAX_ROW_DEGREE = 22;
  
  private Constants()
  {
  }
}
