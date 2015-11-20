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

import gemlite.shell.codegen.exception.SQLDialectNotSupportedException;
import gemlite.shell.codegen.impl.DefaultDataType;
import gemlite.shell.codegen.tools.StopWatch;
import gemlite.shell.codegen.tools.StringUtils;

import java.io.File;
import java.lang.reflect.TypeVariable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import gemlite.shell.codegen.util.GeneratorStrategy.Mode;

public class JavaGenerator extends AbstractGenerator
{
  private final StopWatch watch = new StopWatch();
  private Database database;
  private Set<File> files = new LinkedHashSet<File>();
  
  @Override
  public final void generate(Database db)
  {
    DatatypeConverter.printDateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
    this.database = db;
    this.database.addFilter(new AvoidAmbiguousClassesFilter());
    this.database.setIncludeRelations(generateRelations());
    String url = "";
    try
    {
      Connection connection = database.getConnection();
      if (connection != null)
        url = connection.getMetaData().getURL();
    }
    catch (SQLException ignore)
    {
    }
    System.out.println("----------------------------------------------------------");
    System.out.println("  Thank you for using gemlite and gemlite's code generator");
    System.out.println("");
    System.out.println("Database parameters");
    System.out.println("----------------------------------------------------------");
    System.out.println("  dialect -->" + database.getDialect());
    System.out.println("  URL -->" + url);
    System.out.println("  target dir -->" + getTargetDirectory());
    System.out.println("  target package -->" + getTargetPackage());
    System.out.println("  includes -->" + Arrays.asList(database.getIncludes()));
    System.out.println("  excludes -->" + Arrays.asList(database.getExcludes()));
    System.out.println("----------------------------------------------------------");
    System.out.println("");
    System.out.println("DefaultGenerator parameters");
    System.out.println("----------------------------------------------------------");
    System.out.println("  strategy -->" + strategy.delegate.getClass());
    System.out.println("  pojos -->" + generatePojos());
    System.out.println("  pojos equals and hashcode -->" + generatePojosEqualsAndHashCode());
    System.out.println("----------------------------------------------------------");
    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------
    for (SchemaDefinition schema : database.getSchemata())
    {
      try
      {
        generate(schema);
      }
      catch (Exception e)
      {
        System.err.println("Error generating code for schema " + schema + "; Exception -->" + e);
      }
    }
  }
  
  private final void generate(SchemaDefinition schema)
  {
    // ----------------------------------------------------------------------
    if (generatePojos() && database.getTables(schema).size() > 0)
    {
      generatePojos(schema);
    }
    // ----------------------------------------------------------------------
    System.out.println("----------------------------------------------------------");
    empty(getStrategy().getFile(schema).getParentFile(), ".java", files);
    files.clear();
    watch.splitInfo("GENERATION FINISHED: " + schema.getQualifiedName());
  }
  
  private class AvoidAmbiguousClassesFilter implements Database.Filter
  {
    private Map<String, String> included = new HashMap<String, String>();
    
    @Override
    public boolean exclude(Definition definition)
    {
      if (definition instanceof ColumnDefinition)
        return false;
      String name = getStrategy().getFullJavaClassName(definition);
      String nameLC = name.toLowerCase();
      String existing = included.put(nameLC, name);
      if (existing == null)
        return false;
      System.err.println("Ambiguous type name -->" + "The object " + definition.getQualifiedOutputName()
          + " generates a type " + name + " which conflicts with the existing type " + existing
          + " on some operating systems. Use a custom generator strategy to disambiguate the types.");
      return true;
    }
  }
  
  protected void generatePojos(SchemaDefinition schema)
  {
    System.out.println("Generating table POJOs");
    for (TableDefinition table : database.getTables(schema))
    {
      try
      {
        generatePojo(table);
      }
      catch (Exception e)
      {
        System.err.println("Error while generating table POJO " + table + "; Exception -->" + e);
      }
    }
    watch.splitInfo("Table POJOs generated");
  }
  
  protected void generatePojo(TableDefinition table)
  {
    System.out.println("Generating POJO -->" + getStrategy().getFileName(table, Mode.POJO));
    JavaWriter out = newJavaWriter(getStrategy().getFile(table, Mode.POJO));
    generatePojo(table, out);
    out.close();
  }
  
  protected void generateKey(TableDefinition table)
  {
    System.out.println("Generating KEY  -->" + getStrategy().getFileName(table, Mode.KEY));
    JavaWriter out = newJavaWriter(getStrategy().getFile(table, Mode.KEY));
    generateKey(table, out);
    out.close();
  }
  
  protected void generatePojo(TableDefinition table, JavaWriter out)
  {
    generatePojo((Definition) table, out, false);
  }
  
  protected void generateKey(TableDefinition table, JavaWriter out)
  {
    generatePojo((Definition) table, out, true);
  }
  
  private void generatePojo(Definition table, JavaWriter out, boolean isKeyMode)
  {
    final String className = getStrategy().getJavaClassName(table, isKeyMode ? Mode.KEY : Mode.POJO);
    final String superName = out.ref(getStrategy().getJavaClassExtends(table, Mode.POJO));
    final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(table, Mode.POJO));
    if (generateInterfaces())
    {
      interfaces.add(out.ref(getStrategy().getFullJavaClassName(table, Mode.INTERFACE)));
    }
    printPackage(out, table, Mode.POJO);
    if (table instanceof TableDefinition)
      generatePojoClassJavadoc((TableDefinition) table, out);
    if (table instanceof TableDefinition)
      printTableAnnotations(out, (TableDefinition) table, isKeyMode);
    out.println("public class %s[[before= extends ][%s]][[before= implements ][%s]] {", className, list(superName),
        interfaces);
    out.println();
    int maxLength = 0;
    for (TypedElementDefinition<?> column : getTypedElements(table, isKeyMode))
    {
      maxLength = Math.max(maxLength, out.ref(getJavaType(column.getType(), Mode.POJO)).length());
    }
    for (TypedElementDefinition<?> column : getTypedElements(table, isKeyMode))
    {
      printColumnKeyAnnotations(out, table, (ColumnDefinition) column);
      out.tab(1).println("private %s%s %s;", generateImmutablePojos() ? "final " : "",
          StringUtils.rightPad(out.ref(getJavaType(column.getType(), Mode.POJO)), maxLength),
          getStrategy().getJavaMemberName(column, Mode.POJO));
    }
    // Constructors
    // ---------------------------------------------------------------------
    // Default constructor
    if (!generateImmutablePojos())
    {
      out.println();
      out.tab(1).print("public %s() {}", className);
      out.println();
    }
    // [#1363] copy constructor
    out.println();
    out.tab(1).println("public %s(%s value) {", className, className);
    for (TypedElementDefinition<?> column : getTypedElements(table, isKeyMode))
    {
      out.tab(2).println("this.%s = value.%s;", getStrategy().getJavaMemberName(column, Mode.POJO),
          getStrategy().getJavaMemberName(column, Mode.POJO));
    }
    out.tab(1).println("}");
    if (getTypedElements(table, isKeyMode).size() > 0 && getTypedElements(table, isKeyMode).size() < 256)
    {
      out.println();
      out.tab(1).print("public %s(", className);
      String separator1 = "";
      for (TypedElementDefinition<?> column : getTypedElements(table, isKeyMode))
      {
        out.println(separator1);
        out.tab(2).print("%s %s", StringUtils.rightPad(out.ref(getJavaType(column.getType(), Mode.POJO)), maxLength),
            getStrategy().getJavaMemberName(column, Mode.POJO));
        separator1 = ",";
      }
      out.println();
      out.tab(1).println(") {");
      for (TypedElementDefinition<?> column : getTypedElements(table, isKeyMode))
      {
        final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
        out.tab(2).println("this.%s = %s;", columnMember, columnMember);
      }
      out.tab(1).println("}");
    }
    for (TypedElementDefinition<?> column : getTypedElements(table, isKeyMode))
    {
      final String columnType = out.ref(getJavaType(column.getType(), Mode.POJO));
      final String columnSetterReturnType = fluentSetters() ? className : "void";
      final String columnSetter = getStrategy().getJavaSetterName(column, Mode.POJO);
      final String columnGetter = getStrategy().getJavaGetterName(column, Mode.POJO);
      final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
      final boolean isUDT = column.getType().isUDT();
      // Getter
      out.println();
      out.tab(1).overrideIf(generateInterfaces());
      out.tab(1).println("public %s %s() {", columnType, columnGetter);
      out.tab(2).println("return this.%s;", columnMember);
      out.tab(1).println("}");
      // Setter
      if (!generateImmutablePojos())
      {
        out.println();
        out.tab(1).overrideIf(generateInterfaces() && !isUDT);
        out.tab(1).println("public %s %s(%s %s) {", columnSetterReturnType, columnSetter, columnType, columnMember);
        out.tab(2).println("this.%s = %s;", columnMember, columnMember);
        if (fluentSetters())
          out.tab(2).println("return this;");
        out.tab(1).println("}");
        // setter overloads
        if (generateInterfaces() && isUDT)
        {
          final String columnTypeInterface = out.ref(getJavaType(column.getType(), Mode.INTERFACE));
          out.println();
          out.tab(1).override();
          out.tab(1).println("public %s %s(%s %s) {", columnSetterReturnType, columnSetter, columnTypeInterface,
              columnMember);
          out.tab(2).println("if (%s == null)", columnMember);
          out.tab(3).println("this.%s = null;", columnMember);
          out.tab(2).println("else");
          out.tab(3).println("this.%s = %s.into(new %s());", columnMember, columnMember, columnType);
          if (fluentSetters())
            out.tab(2).println("return this;");
          out.tab(1).println("}");
        }
      }
    }
    if (generatePojosEqualsAndHashCode())
    {
      generatePojoEqualsAndHashCode(table, out, isKeyMode);
    }
    generatePojoToString(table, out, isKeyMode);
    if (table instanceof TableDefinition)
      generatePojoClassFooter((TableDefinition) table, out);
    out.println("}");
    out.close();
  }
  
  protected void generatePojoEqualsAndHashCode(Definition table, JavaWriter out, boolean isKeyMode)
  {
    final String className = getStrategy().getJavaClassName(table, isKeyMode ? Mode.KEY : Mode.POJO);
    out.println();
    out.tab(1).println("@Override");
    out.tab(1).println("public boolean equals(%s obj) {", Object.class);
    out.tab(2).println("if (this == obj)");
    out.tab(3).println("return true;");
    out.tab(2).println("if (obj == null)");
    out.tab(3).println("return false;");
    out.tab(2).println("if (getClass() != obj.getClass())");
    out.tab(3).println("return false;");
    out.tab(2).println("final %s other = (%s) obj;", className, className);
    for (TypedElementDefinition<?> column : getTypedElements(table, isKeyMode))
    {
      final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
      out.tab(2).println("if (%s == null) {", columnMember);
      out.tab(3).println("if (other.%s != null)", columnMember);
      out.tab(4).println("return false;");
      out.tab(2).println("}");
      if (getJavaType(column.getType()).endsWith("[]"))
      {
        out.tab(2).println("else if (!%s.equals(%s, other.%s))", Arrays.class, columnMember, columnMember);
      }
      else
      {
        out.tab(2).println("else if (!%s.equals(other.%s))", columnMember, columnMember);
      }
      out.tab(3).println("return false;");
    }
    out.tab(2).println("return true;");
    out.tab(1).println("}");
    out.println();
    out.tab(1).println("@Override");
    out.tab(1).println("public int hashCode() {");
    out.tab(2).println("final int prime = 31;");
    out.tab(2).println("int result = 1;");
    for (TypedElementDefinition<?> column : getTypedElements(table, isKeyMode))
    {
      final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
      if (getJavaType(column.getType()).endsWith("[]"))
      {
        out.tab(2).println("result = prime * result + ((%s == null) ? 0 : %s.hashCode(%s));", columnMember,
            Arrays.class, columnMember);
      }
      else
      {
        out.tab(2).println("result = prime * result + ((%s == null) ? 0 : %s.hashCode());", columnMember, columnMember);
      }
    }
    out.tab(2).println("return result;");
    out.tab(1).println("}");
  }
  
  protected void generatePojoToString(Definition table, JavaWriter out, boolean isKeyMode)
  {
    final String className = getStrategy().getJavaClassName(table, isKeyMode ? Mode.KEY : Mode.POJO);
    out.println();
    out.tab(1).println("@Override");
    out.tab(1).println("public String toString() {");
    out.tab(2).println("StringBuilder builder = new StringBuilder();");
    out.tab(2).println("builder.append(\" %s [\");", className);
    // for (TypedElementDefinition<?> column : getTypedElements(table,
    // isKeyMode))
    @SuppressWarnings("rawtypes")
    List columnList = getTypedElements(table, isKeyMode);
    for (int i = 0; i < columnList.size(); i++)
    {
      TypedElementDefinition<?> column = (TypedElementDefinition<?>) columnList.get(i);
      final String columnMember = getStrategy().getJavaMemberName(column, Mode.POJO);
      if (i == 0)
        out.tab(2).println("builder.append(\"%s=\");", columnMember);
      else
        out.tab(2).println("builder.append(\", %s=\");", columnMember);
      out.tab(2).println("builder.append(%s);", columnMember);
    }
    out.tab(2).println("builder.append(\"]\");");
    out.tab(2).println("return builder.toString();");
    out.tab(1).println("}");
    out.println();
  }
  
  private List<? extends TypedElementDefinition<? extends Definition>> getTypedElements(Definition definition,
      boolean isKeyMode)
  {
    if (definition instanceof TableDefinition)
    {
      if (isKeyMode)
        return ((TableDefinition) definition).getPrimaryKey().getKeyColumns().size() > 1 ? ((TableDefinition) definition)
            .getPrimaryKey().getKeyColumns() : ((TableDefinition) definition).getUniqueKeys().get(0).getKeyColumns();
      else
        return ((TableDefinition) definition).getColumns();
    }
    else
    {
      throw new IllegalArgumentException("Unsupported type : " + definition);
    }
  }
  
  protected void generatePojoClassFooter(TableDefinition table, JavaWriter out)
  {
  }
  
  protected void generatePojoClassJavadoc(TableDefinition table, JavaWriter out)
  {
    printClassJavadoc(out, table);
  }
  
  protected void printTableAnnotations(JavaWriter out, TableDefinition table, boolean isKeyMode)
  {
    if (!isKeyMode)
    {
      out.print("@%s(\"", out.ref("gemlite.core.annotations.domain.Region"));
      out.print(table.getName().toLowerCase());
      out.print("\")\n");
      out.print("@%s(\"", out.ref("gemlite.core.annotations.domain.Table"));
      out.print(table.getName().toLowerCase());
      out.print("\")\n");
      if (table.getPrimaryKey().getKeyColumns().size() > 1
          || (table.getPrimaryKey().getKeyColumns().size() == 0 && table.getUniqueKeys().get(0).getKeyColumns().size() > 1))
      {
        out.print("@%s(", out.ref("gemlite.core.annotations.domain.Key"));
        out.print(getStrategy().getJavaClassName(table, Mode.KEY) + ".class");
        out.print(")\n");
        generateKey(table);
      }
    }
    out.println("@%s", out.ref("gemlite.core.annotations.domain.AutoSerialize"));
  }
  
  private void printColumnKeyAnnotations(JavaWriter out, Definition definition, ColumnDefinition column)
  {
    UniqueKeyDefinition pk = ((TableDefinition) definition).getPrimaryKey();
    if (pk.getKeyColumns().size() == 1 && pk.getKeyColumns().get(0).getName().equals(column.getName()))
    {
      out.tab(1).println("@%s", out.ref("gemlite.core.annotations.domain.Key"));
    }
    if (pk.getKeyColumns().size() == 0)
    {
      UniqueKeyDefinition uk = ((TableDefinition) definition).getUniqueKeys().get(0);
      if (uk.getKeyColumns().size() == 1 && uk.getKeyColumns().get(0).getName().equals(column.getName()))
      {
        out.tab(1).println("@%s", out.ref("gemlite.core.annotations.domain.Key"));
      }
    }
  }
  
  protected void printClassJavadoc(JavaWriter out, Definition definition)
  {
    printClassJavadoc(out, definition.getComment());
  }
  
  protected void printClassJavadoc(JavaWriter out, String comment)
  {
    
  }
  
  protected void printPackage(JavaWriter out, Definition definition)
  {
    printPackage(out, definition, Mode.DEFAULT);
  }
  
  protected void printPackage(JavaWriter out, Definition definition, Mode mode)
  {
    out.println("package %s;", getStrategy().getJavaPackageName(definition, mode));
    out.printImports();
  }
  
  protected String getSimpleJavaType(DataTypeDefinition type)
  {
    return GenerationUtil.getSimpleJavaType(getJavaType(type));
  }
  
  protected String getJavaType(DataTypeDefinition type)
  {
    return getJavaType(type, Mode.RECORD);
  }
  
  protected String getJavaType(DataTypeDefinition type, Mode udtMode)
  {
    return getType(type.getDatabase(), type.getSchema(), type.getType(), type.getPrecision(), type.getScale(),
        type.getUserType(), Object.class.getName(), udtMode);
  }
  
  protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, String u, String defaultType)
  {
    return getType(db, schema, t, p, s, u, defaultType, Mode.RECORD);
  }
  
  protected String getType(Database db, SchemaDefinition schema, String t, int p, int s, String u, String defaultType,
      Mode udtMode)
  {
    String type = defaultType;
    try
    {
      Class<?> clazz = DefaultDataType.getType(db.getDialect(), t, p, s);
      type = clazz.getCanonicalName();
      if (clazz.getTypeParameters().length > 0)
      {
        type += "<";
        String separator = "";
        for (TypeVariable<?> var : clazz.getTypeParameters())
        {
          type += separator;
          type += ((Class<?>) var.getBounds()[0]).getCanonicalName();
          separator = ", ";
        }
        type += ">";
      }
    }
    catch (SQLDialectNotSupportedException e)
    {
      if (defaultType == null)
      {
        throw e;
      }
    }
    return type;
  }
  
  @SafeVarargs
  private static final <T> List<T> list(T... objects)
  {
    List<T> result = new ArrayList<T>();
    if (objects != null)
      for (T object : objects)
        if (object != null && !"".equals(object))
          result.add(object);
    return result;
  }
  
  protected final JavaWriter newJavaWriter(File file)
  {
    files.add(file);
    return new JavaWriter(file, fullyQualifiedTypes());
  }
}
