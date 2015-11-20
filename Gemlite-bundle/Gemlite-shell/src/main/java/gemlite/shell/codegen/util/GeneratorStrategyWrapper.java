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

import gemlite.shell.codegen.tools.StringUtils;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class GeneratorStrategyWrapper extends AbstractGeneratorStrategy
{
  private final Map<Class<?>, Set<String>> reservedColumns = new HashMap<Class<?>, Set<String>>();
  final Generator generator;
  final GeneratorStrategy delegate;
  
  GeneratorStrategyWrapper(Generator generator, GeneratorStrategy delegate)
  {
    this.generator = generator;
    this.delegate = delegate;
  }
  
  @Override
  public String getTargetDirectory()
  {
    return delegate.getTargetDirectory();
  }
  
  @Override
  public void setTargetDirectory(String directory)
  {
    delegate.setTargetDirectory(directory);
  }
  
  @Override
  public String getTargetPackage()
  {
    return delegate.getTargetPackage();
  }
  
  @Override
  public void setTargetPackage(String packageName)
  {
    delegate.setTargetPackage(packageName);
  }
  
  @Override
  public void setInstanceFields(boolean instanceFields)
  {
    delegate.setInstanceFields(instanceFields);
  }
  
  @Override
  public boolean getInstanceFields()
  {
    return delegate.getInstanceFields();
  }
  
  @Override
  public String getJavaIdentifier(Definition definition)
  {
    if (definition instanceof SchemaDefinition && ((SchemaDefinition) definition).isDefaultSchema())
    {
      return "DEFAULT_SCHEMA";
    }
    String identifier = GenerationUtil.convertToJavaIdentifier(delegate.getJavaIdentifier(definition));
    if (definition instanceof ColumnDefinition)
    {
      TypedElementDefinition<?> e = (TypedElementDefinition<?>) definition;
      if (identifier.equals(getJavaIdentifier(e.getContainer())))
      {
        return identifier + "_";
      }
      if (identifier.equals(getJavaPackageName(e.getContainer()).replaceAll("\\..*", "")))
      {
        return identifier + "_";
      }
    }
    return identifier;
  }
  
  @Override
  public String getJavaSetterName(Definition definition, Mode mode)
  {
    return GenerationUtil.convertToJavaIdentifier(delegate.getJavaSetterName(definition, mode));
  }
  
  @Override
  public String getJavaGetterName(Definition definition, Mode mode)
  {
    return GenerationUtil.convertToJavaIdentifier(delegate.getJavaGetterName(definition, mode));
  }
  
  @Override
  public String getJavaMethodName(Definition definition, Mode mode)
  {
    String methodName;
    methodName = delegate.getJavaMethodName(definition, mode);
    methodName = overload(definition, mode, methodName);
    methodName = GenerationUtil.convertToJavaIdentifier(methodName);
    return methodName;
  }
  
  private String overload(Definition definition, Mode mode, String identifier)
  {
    if (!StringUtils.isBlank(definition.getOverload()))
    {
      identifier += getOverloadSuffix(definition, mode, definition.getOverload());
    }
    return identifier;
  }
  
  @SuppressWarnings("unused")
  private String disambiguateMethod11(Definition definition, String method)
  {
    Set<String> reserved = null;
    if (definition instanceof ColumnDefinition)
    {
      if (((ColumnDefinition) definition).getContainer().getPrimaryKey() != null)
      {
        reserved = null;
      }
      else
      {
        reserved = null;
      }
    }
    if (reserved != null)
    {
      if (reserved.contains(method))
      {
        return method + "_";
      }
      // If this is the setter, check if the getter needed disambiguation
      // This ensures that getters and setters have the same name
      if (method.startsWith("set"))
      {
        String base = method.substring(3);
        if (reserved.contains("get" + base) || reserved.contains("is" + base))
        {
          return method + "_";
        }
      }
    }
    return method;
  }
  
  @SuppressWarnings("unused")
  private Set<String> reservedColumns(Class<?> clazz)
  {
    if (clazz == null)
    {
      return Collections.emptySet();
    }
    Set<String> result = reservedColumns.get(clazz);
    if (result == null)
    {
      result = new HashSet<String>();
      reservedColumns.put(clazz, result);
      // Recurse up in class hierarchy
      result.addAll(reservedColumns(clazz.getSuperclass()));
      for (Class<?> c : clazz.getInterfaces())
      {
        result.addAll(reservedColumns(c));
      }
      for (Method m : clazz.getDeclaredMethods())
      {
        String name = m.getName();
        if (name.startsWith("get") && m.getParameterTypes().length == 0)
        {
          result.add(name);
        }
      }
    }
    return result;
  }
  
  @Override
  public String getJavaClassExtends(Definition definition, Mode mode)
  {
    // [#1243] Only POJO mode can accept super classes
    return delegate.getJavaClassExtends(definition, mode);
  }
  
  @Override
  public List<String> getJavaClassImplements(Definition definition, Mode mode)
  {
    // [#1243] All generation modes can accept interfaces
    Set<String> result = new LinkedHashSet<String>(delegate.getJavaClassImplements(definition, mode));
    // [#1528] Generated interfaces (implemented by RECORD and POJO) are
    // always Serializable
    if (mode == Mode.INTERFACE)
    {
      result.add(Serializable.class.getName());
    }
    // [#1528] POJOs only implement Serializable if they don't inherit
    // Serializable from INTERFACE already
    else if (mode == Mode.POJO && !generator.generateInterfaces())
    {
      // replace with annotation
      // result.add(Serializable.class.getName());
    }
    return new ArrayList<String>(result);
  }
  
  @Override
  public String getJavaClassName(Definition definition, Mode mode)
  {
    if (definition instanceof SchemaDefinition && ((SchemaDefinition) definition).isDefaultSchema())
    {
      return "DefaultSchema";
    }
    String className;
    className = delegate.getJavaClassName(definition, mode);
    className = overload(definition, mode, className);
    className = GenerationUtil.convertToJavaIdentifier(className);
    return className;
  }
  
  @Override
  public String getJavaPackageName(Definition definition, Mode mode)
  {
    String[] split = delegate.getJavaPackageName(definition, mode).split("\\.");
    for (int i = 0; i < split.length; i++)
    {
      split[i] = GenerationUtil.convertToJavaIdentifier(split[i]);
    }
    return StringUtils.join(split, ".");
  }
  
  @Override
  public String getJavaMemberName(Definition definition, Mode mode)
  {
    String identifier = GenerationUtil.convertToJavaIdentifier(delegate.getJavaMemberName(definition, mode));
    // [#2781] Disambiguate collisions with the leading package name
    if (identifier.equals(getJavaPackageName(definition, mode).replaceAll("\\..*", "")))
    {
      return identifier + "_";
    }
    return identifier;
  }
  
  @Override
  public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex)
  {
    return delegate.getOverloadSuffix(definition, mode, overloadIndex);
  }
}
