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

public interface Generator
{
  void generate(Database database);
  
  void setStrategy(GeneratorStrategy strategy);
  
  GeneratorStrategy getStrategy();
  
  boolean generateDeprecated();
  
  void setGenerateDeprecated(boolean generateDeprecated);
  
  boolean generateRelations();
  
  void setGenerateRelations(boolean generateRelations);
  
  boolean generateInstanceFields();
  
  void setGenerateInstanceFields(boolean generateInstanceFields);
  
  boolean generateGeneratedAnnotation();
  
  void setGenerateGeneratedAnnotation(boolean generateGeneratedAnnotation);
  
  boolean useSchemaVersionProvider();
  
  void setUseSchemaVersionProvider(boolean useSchemaVersionProvider);
  
  boolean generateRecords();
  
  void setGenerateRecords(boolean generateRecords);
  
  boolean generatePojos();
  
  void setGeneratePojos(boolean generatePojos);
  
  boolean generateImmutablePojos();
  
  void setGenerateImmutablePojos(boolean generateImmutablePojos);
  
  boolean generateInterfaces();
  
  void setGenerateInterfaces(boolean generateInterfaces);
  
  boolean generateDaos();
  
  void setGenerateDaos(boolean generateDaos);
  
  boolean generateJPAAnnotations();
  
  void setGenerateJPAAnnotations(boolean generateJPAAnnotations);
  
  boolean generateValidationAnnotations();
  
  void setGenerateValidationAnnotations(boolean generateValidationAnnotations);
  
  boolean generateGlobalObjectReferences();
  
  void setGenerateGlobalObjectReferences(boolean generateGlobalObjectReferences);
  
  boolean generateGlobalRoutineReferences();
  
  void setGenerateGlobalRoutineReferences(boolean globalRoutineReferences);
  
  boolean generateGlobalSequenceReferences();
  
  void setGenerateGlobalSequenceReferences(boolean globalSequenceReferences);
  
  boolean generateGlobalTableReferences();
  
  void setGenerateGlobalTableReferences(boolean globalTableReferences);
  
  boolean generateGlobalUDTReferences();
  
  void setGenerateGlobalUDTReferences(boolean globalUDTReferences);
  
  boolean fluentSetters();
  
  void setFluentSetters(boolean fluentSetters);
  
  boolean generatePojosEqualsAndHashCode();
  
  void setGeneratePojosEqualsAndHashCode(boolean generatePojosEqualsAndHashCode);
  
  String fullyQualifiedTypes();
  
  void setFullyQualifiedTypes(String fullyQualifiedTypes);
  
  String getTargetDirectory();
  
  void setTargetDirectory(String directory);
  
  String getTargetPackage();
  
  void setTargetPackage(String packageName);
}
