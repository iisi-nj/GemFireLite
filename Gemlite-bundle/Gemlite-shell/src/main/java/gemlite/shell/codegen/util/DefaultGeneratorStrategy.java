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

import java.util.ArrayList;
import java.util.List;
import gemlite.shell.codegen.tools.StringUtils;

public class DefaultGeneratorStrategy extends AbstractGeneratorStrategy
{
  private String targetDirectory;
  private String targetPackage;
  private boolean instanceFields;
  
  // -------------------------------------------------------------------------
  // Initialisation
  // -------------------------------------------------------------------------
  @Override
  public final void setInstanceFields(boolean instanceFields)
  {
    this.instanceFields = instanceFields;
  }
  
  @Override
  public final boolean getInstanceFields()
  {
    return instanceFields;
  }
  
  @Override
  public final String getTargetDirectory()
  {
    return targetDirectory;
  }
  
  @Override
  public final void setTargetDirectory(String directory)
  {
    this.targetDirectory = directory;
  }
  
  @Override
  public final String getTargetPackage()
  {
    return targetPackage;
  }
  
  @Override
  public final void setTargetPackage(String packageName)
  {
    this.targetPackage = packageName;
  }
  
  // -------------------------------------------------------------------------
  // Strategy methods
  // -------------------------------------------------------------------------
  @Override
  public String getJavaIdentifier(Definition definition)
  {
    return GenerationUtil.convertToJavaIdentifier(definition.getOutputName().toUpperCase());
  }
  
  @Override
  public String getJavaSetterName(Definition definition, Mode mode)
  {
    return "set" + getJavaClassName0(definition, Mode.DEFAULT);
  }
  
  @Override
  public String getJavaGetterName(Definition definition, Mode mode)
  {
    return "get" + getJavaClassName0(definition, Mode.DEFAULT);
  }
  
  @Override
  public String getJavaMethodName(Definition definition, Mode mode)
  {
    return getJavaClassName0LC(definition, Mode.DEFAULT);
  }
  
  @Override
  public String getJavaClassExtends(Definition definition, Mode mode)
  {
    return null;
  }
  
  @Override
  public List<String> getJavaClassImplements(Definition definition, Mode mode)
  {
    List<String> result = new ArrayList<String>();
    return result;
  }
  
  @Override
  public String getJavaClassName(Definition definition, Mode mode)
  {
    return getJavaClassName0(definition, mode);
  }
  
  @Override
  public String getJavaPackageName(Definition definition, Mode mode)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(getTargetPackage());
    return sb.toString();
  }
  
  @Override
  public String getJavaMemberName(Definition definition, Mode mode)
  {
    return getJavaClassName0LC(definition, mode);
  }
  
  private String getJavaClassName0LC(Definition definition, Mode mode)
  {
    String result = getJavaClassName0(definition, mode);
    return result.substring(0, 1).toLowerCase() + result.substring(1);
  }
  
  private String getJavaClassName0(Definition definition, Mode mode)
  {
    StringBuilder result = new StringBuilder();
    String cc = StringUtils.toCamelCase(definition.getOutputName());
    StringBuilder uc = new StringBuilder();
    uc.append(definition.getOutputName().substring(0, 1).toUpperCase());
    uc.append(definition.getOutputName().substring(1).toLowerCase());
    result.append(GenerationUtil.convertToJavaIdentifier(uc.toString()));
    if (Mode.KEY.equals(mode))
      result.append("Key");
    return result.toString();
  }
  
  @Override
  public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex)
  {
    return overloadIndex;
  }
}
