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
package gemlite.shell.codegen.util;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface GeneratorStrategy
{
  String getTargetDirectory();
  
  void setTargetDirectory(String directory);
  
  String getTargetPackage();
  
  void setTargetPackage(String packageName);
  
  void setInstanceFields(boolean instanceFields);
  
  boolean getInstanceFields();
  
  String getJavaIdentifier(Definition definition);
  
  List<String> getJavaIdentifiers(Collection<? extends Definition> definitions);
  
  List<String> getJavaIdentifiers(Definition... definitions);
  
  String getFullJavaIdentifier(Definition definition);
  
  List<String> getFullJavaIdentifiers(Collection<? extends Definition> definitions);
  
  List<String> getFullJavaIdentifiers(Definition... definitions);
  
  String getJavaSetterName(Definition definition);
  
  String getJavaSetterName(Definition definition, Mode mode);
  
  String getJavaGetterName(Definition definition);
  
  String getJavaGetterName(Definition definition, Mode mode);
  
  String getJavaMethodName(Definition definition);
  
  String getJavaMethodName(Definition definition, Mode mode);
  
  String getJavaClassExtends(Definition definition);
  
  String getJavaClassExtends(Definition definition, Mode mode);
  
  List<String> getJavaClassImplements(Definition definition);
  
  List<String> getJavaClassImplements(Definition definition, Mode mode);
  
  String getJavaClassName(Definition definition);
  
  String getJavaClassName(Definition definition, Mode mode);
  
  String getJavaPackageName(Definition definition);
  
  String getJavaPackageName(Definition definition, Mode mode);
  
  String getJavaMemberName(Definition definition);
  
  String getJavaMemberName(Definition definition, Mode mode);
  
  String getFullJavaClassName(Definition definition);
  
  String getFullJavaClassName(Definition definition, Mode mode);
  
  String getFileName(Definition definition);
  
  String getFileName(Definition definition, Mode mode);
  
  File getFile(Definition definition);
  
  File getFile(Definition definition, Mode mode);
  
  String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex);
  
  enum Mode
  {
    DEFAULT, RECORD, POJO, KEY, INTERFACE, DAO, ENUM
  }
}
