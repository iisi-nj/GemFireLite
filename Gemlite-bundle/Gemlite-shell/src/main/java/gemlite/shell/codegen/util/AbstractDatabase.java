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
import gemlite.shell.codegen.util.jaxb.CustomType;
import gemlite.shell.codegen.util.jaxb.EnumType;
import gemlite.shell.codegen.util.jaxb.ForcedType;
import gemlite.shell.codegen.util.jaxb.RegexFlag;
import gemlite.shell.codegen.util.jaxb.Schema;
import gemlite.core.util.LogUtil;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.xml.bind.JAXB;

public abstract class AbstractDatabase implements Database
{
  // -------------------------------------------------------------------------
  // Configuration elements
  // -------------------------------------------------------------------------
  private Properties properties;
  private Connection connection;
  private List<RegexFlag> regexFlags;
  private List<Filter> filters;
  private String[] excludes;
  private String[] includes;
  private boolean includeExcludeColumns;
  private String[] recordVersionFields;
  private String[] recordTimestampFields;
  private String[] syntheticPrimaryKeys;
  private String[] overridePrimaryKeys;
  private boolean supportsUnsignedTypes;
  private boolean dateAsTimestamp;
  private List<Schema> configuredSchemata;
  private List<CustomType> configuredCustomTypes;
  private List<EnumType> configuredEnumTypes;
  private List<ForcedType> configuredForcedTypes;
  private SchemaVersionProvider schemaVersionProvider;
  // -------------------------------------------------------------------------
  // Loaded definitions
  // -------------------------------------------------------------------------
  private List<String> inputSchemata;
  private List<SchemaDefinition> schemata;
  private List<UniqueKeyDefinition> uniqueKeys;
  private List<TableDefinition> tables;
  private boolean includeRelations = true;
  private transient Map<SchemaDefinition, List<UniqueKeyDefinition>> uniqueKeysBySchema;
  private transient Map<SchemaDefinition, List<TableDefinition>> tablesBySchema;
  // Other caches
  // private final Map<Table<?>, Boolean> exists;
  private final Map<String, Pattern> patterns;
  
  protected AbstractDatabase()
  {
    // exists = new HashMap<Table<?>, Boolean>();
    patterns = new HashMap<String, Pattern>();
    filters = new ArrayList<Database.Filter>();
  }
  
  @Override
  public final void setConnection(Connection connection)
  {
    this.connection = connection;
  }
  
  @Override
  public final Connection getConnection()
  {
    return connection;
  }
  
  final Pattern pattern(String regex)
  {
    Pattern pattern = patterns.get(regex);
    if (pattern == null)
    {
      int flags = 0;
      List<RegexFlag> list = new ArrayList<RegexFlag>(getRegexFlags());
      // [#3860] This should really be handled by JAXB, but apparently, @XmlList
      // and @XmlElement(defaultValue=...)
      // cannot be combined: http://stackoverflow.com/q/27528698/521799
      if (list.isEmpty())
      {
        list.add(RegexFlag.COMMENTS);
        list.add(RegexFlag.CASE_INSENSITIVE);
      }
      for (RegexFlag flag : list)
      {
        switch (flag)
        {
          case CANON_EQ:
            flags |= Pattern.CANON_EQ;
            break;
          case CASE_INSENSITIVE:
            flags |= Pattern.CASE_INSENSITIVE;
            break;
          case COMMENTS:
            flags |= Pattern.COMMENTS;
            break;
          case DOTALL:
            flags |= Pattern.DOTALL;
            break;
          case LITERAL:
            flags |= Pattern.LITERAL;
            break;
          case MULTILINE:
            flags |= Pattern.MULTILINE;
            break;
          case UNICODE_CASE:
            flags |= Pattern.UNICODE_CASE;
            break;
          case UNICODE_CHARACTER_CLASS:
            flags |= 0x100;
            break; // Pattern.UNICODE_CHARACTER_CLASS: Java 1.7 only
          case UNIX_LINES:
            flags |= Pattern.UNIX_LINES;
            break;
        }
      }
      pattern = Pattern.compile(regex, flags);
      patterns.put(regex, pattern);
    }
    return pattern;
  }
  
  @Override
  public final List<SchemaDefinition> getSchemata()
  {
    if (schemata == null)
    {
      schemata = new ArrayList<SchemaDefinition>();
      try
      {
        schemata = getSchemata0();
      }
      catch (SQLException e)
      {
        LogUtil.getCoreLog().error("Could not load schemata", e);
      }
      Iterator<SchemaDefinition> it = schemata.iterator();
      while (it.hasNext())
      {
        if (!getInputSchemata().contains(it.next().getName()))
        {
          it.remove();
        }
      }
      if (schemata.isEmpty())
      {
        LogUtil
            .getCoreLog()
            .warn(
                "No schemata were loaded",
                "Please check your connection settings, and whether your database (and your database version!) is really supported by gemlite. Also, check the case-sensitivity in your configured <inputSchema/> elements : "
                    + inputSchemata);
      }
    }
    return schemata;
  }
  
  @Override
  public final SchemaDefinition getSchema(String inputName)
  {
    for (SchemaDefinition schema : getSchemata())
    {
      if (schema.getName().equals(inputName))
      {
        return schema;
      }
    }
    return null;
  }
  
  @Override
  public final List<String> getInputSchemata()
  {
    if (inputSchemata == null)
    {
      inputSchemata = new ArrayList<String>();
      // [#1312] Allow for ommitting inputSchema configuration. Generate
      // All schemata instead
      if (configuredSchemata.size() == 1 && StringUtils.isBlank(configuredSchemata.get(0).getInputSchema()))
      {
        try
        {
          for (SchemaDefinition schema : getSchemata0())
          {
            inputSchemata.add(schema.getName());
          }
        }
        catch (SQLException e)
        {
          LogUtil.getCoreLog().error("Could not load schemata", e);
        }
      }
      else
      {
        for (Schema schema : configuredSchemata)
        {
          {
            inputSchemata.add(schema.getInputSchema());
          }
        }
      }
    }
    return inputSchemata;
  }
  
  @Override
  @Deprecated
  public String getOutputSchema(String inputSchema)
  {
    for (Schema schema : configuredSchemata)
    {
      if (inputSchema.equals(schema.getInputSchema()))
      {
        return schema.getOutputSchema();
      }
    }
    return inputSchema;
  }
  
  @Override
  public final void setConfiguredSchemata(List<Schema> schemata)
  {
    this.configuredSchemata = schemata;
  }
  
  @Override
  public final void setProperties(Properties properties)
  {
    this.properties = properties;
  }
  
  public final Properties getProperties()
  {
    return properties;
  }
  
  @Override
  public final List<Filter> getFilters()
  {
    return Collections.unmodifiableList(filters);
  }
  
  @Override
  public final void addFilter(Filter filter)
  {
    filters.add(filter);
  }
  
  @Override
  public final void setExcludes(String[] excludes)
  {
    this.excludes = excludes;
  }
  
  @Override
  public final String[] getExcludes()
  {
    return excludes;
  }
  
  @Override
  public final void setIncludes(String[] includes)
  {
    this.includes = includes;
  }
  
  @Override
  public final String[] getIncludes()
  {
    return includes;
  }
  
  @Override
  public final void setIncludeExcludeColumns(boolean includeExcludeColumns)
  {
    this.includeExcludeColumns = includeExcludeColumns;
  }
  
  @Override
  public final boolean getIncludeExcludeColumns()
  {
    return includeExcludeColumns;
  }
  
  @Override
  public final void setRegexFlags(List<RegexFlag> regexFlags)
  {
    this.regexFlags = regexFlags;
  }
  
  @Override
  public final List<RegexFlag> getRegexFlags()
  {
    return regexFlags;
  }
  
  @Override
  public void setRecordVersionFields(String[] recordVersionFields)
  {
    this.recordVersionFields = recordVersionFields;
  }
  
  @Override
  public String[] getRecordVersionFields()
  {
    return recordVersionFields;
  }
  
  @Override
  public void setRecordTimestampFields(String[] recordTimestampFields)
  {
    this.recordTimestampFields = recordTimestampFields;
  }
  
  @Override
  public String[] getRecordTimestampFields()
  {
    return recordTimestampFields;
  }
  
  @Override
  public void setSyntheticPrimaryKeys(String[] syntheticPrimaryKeys)
  {
    this.syntheticPrimaryKeys = syntheticPrimaryKeys;
  }
  
  @Override
  public String[] getSyntheticPrimaryKeys()
  {
    return syntheticPrimaryKeys;
  }
  
  @Override
  public void setOverridePrimaryKeys(String[] overridePrimaryKeys)
  {
    this.overridePrimaryKeys = overridePrimaryKeys;
  }
  
  @Override
  public String[] getOverridePrimaryKeys()
  {
    return overridePrimaryKeys;
  }
  
  @Override
  public final void setConfiguredEnumTypes(List<EnumType> configuredEnumTypes)
  {
    this.configuredEnumTypes = configuredEnumTypes;
  }
  
  @Override
  public final List<EnumType> getConfiguredEnumTypes()
  {
    return configuredEnumTypes;
  }
  
  @Override
  public final void setConfiguredCustomTypes(List<CustomType> configuredCustomTypes)
  {
    this.configuredCustomTypes = configuredCustomTypes;
  }
  
  @Override
  public final List<CustomType> getConfiguredCustomTypes()
  {
    return configuredCustomTypes;
  }
  
  @Override
  public final CustomType getConfiguredCustomType(String typeName)
  {
    Iterator<CustomType> it = configuredCustomTypes.iterator();
    while (it.hasNext())
    {
      CustomType type = it.next();
      if (type == null || (type.getName() == null && type.getType() == null))
      {
        try
        {
          StringWriter writer = new StringWriter();
          JAXB.marshal(type, writer);
          LogUtil.getCoreLog().warn("Invalid custom type encountered: " + writer.toString());
        }
        catch (Exception e)
        {
          LogUtil.getCoreLog().warn("Invalid custom type encountered: " + type);
        }
        it.remove();
        continue;
      }
      if (StringUtils.equals(type.getType() != null ? type.getType() : type.getName(), typeName))
      {
        return type;
      }
    }
    return null;
  }
  
  @Override
  public final void setConfiguredForcedTypes(List<ForcedType> configuredForcedTypes)
  {
    this.configuredForcedTypes = configuredForcedTypes;
  }
  
  @Override
  public final List<ForcedType> getConfiguredForcedTypes()
  {
    return configuredForcedTypes;
  }
  
  @Override
  public final SchemaVersionProvider getSchemaVersionProvider()
  {
    return schemaVersionProvider;
  }
  
  @Override
  public final void setSchemaVersionProvider(SchemaVersionProvider schemaVersionProvider)
  {
    this.schemaVersionProvider = schemaVersionProvider;
  }
  
  @Override
  public final void setSupportsUnsignedTypes(boolean supportsUnsignedTypes)
  {
    this.supportsUnsignedTypes = supportsUnsignedTypes;
  }
  
  @Override
  public final boolean supportsUnsignedTypes()
  {
    return supportsUnsignedTypes;
  }
  
  @Override
  public final void setDateAsTimestamp(boolean dateAsTimestamp)
  {
    this.dateAsTimestamp = dateAsTimestamp;
  }
  
  @Override
  public final boolean dateAsTimestamp()
  {
    return dateAsTimestamp;
  }
  
  @Override
  public final void setIncludeRelations(boolean includeRelations)
  {
    this.includeRelations = includeRelations;
  }
  
  @Override
  public final boolean includeRelations()
  {
    return includeRelations;
  }
  
  @Override
  public final List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema)
  {
    if (uniqueKeys == null)
    {
      uniqueKeys = new ArrayList<UniqueKeyDefinition>();
      for (SchemaDefinition s : getSchemata())
      {
        for (TableDefinition table : getTables(s))
        {
          for (UniqueKeyDefinition uniqueKey : table.getUniqueKeys())
          {
            uniqueKeys.add(uniqueKey);
          }
        }
      }
    }
    if (uniqueKeysBySchema == null)
    {
      uniqueKeysBySchema = new LinkedHashMap<SchemaDefinition, List<UniqueKeyDefinition>>();
    }
    return filterSchema(uniqueKeys, schema, uniqueKeysBySchema);
  }
  
  @Override
  public final List<TableDefinition> getTables(SchemaDefinition schema)
  {
    if (tables == null)
    {
      tables = new ArrayList<TableDefinition>();
      try
      {
        List<TableDefinition> t = getTables0();
        tables = filterExcludeInclude(t);
        LogUtil.getCoreLog().info("Tables fetched", fetchedSize(t, tables));
      }
      catch (Exception e)
      {
        LogUtil.getCoreLog().error("Error while fetching tables", e);
      }
    }
    if (tablesBySchema == null)
    {
      tablesBySchema = new LinkedHashMap<SchemaDefinition, List<TableDefinition>>();
    }
    return filterSchema(tables, schema, tablesBySchema);
  }
  
  @Override
  public final TableDefinition getTable(SchemaDefinition schema, String name)
  {
    return getTable(schema, name, false);
  }
  
  @Override
  public final TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase)
  {
    return getDefinition(getTables(schema), name, ignoreCase);
  }
  
  @Override
  public final ForcedType getConfiguredForcedType(Definition definition)
  {
    return getConfiguredForcedType(definition, null);
  }
  
  @Override
  public final ForcedType getConfiguredForcedType(Definition definition, DataTypeDefinition definedType)
  {
    forcedTypeLoop: for (ForcedType forcedType : getConfiguredForcedTypes())
    {
      String expression = forcedType.getExpression();
      if (forcedType.getExpressions() != null)
      {
        expression = forcedType.getExpressions();
        LogUtil.getCoreLog().warn("DEPRECATED",
            "The <expressions/> element in <forcedType/> is deprecated. Use <expression/> instead");
      }
      String types = forcedType.getTypes();
      if (expression != null)
      {
        Pattern p = pattern(expression);
        if (!p.matcher(definition.getName()).matches() && !p.matcher(definition.getQualifiedName()).matches())
        {
          continue forcedTypeLoop;
        }
      }
      if (types != null && definedType != null)
      {
        Pattern p = pattern(types);
        if ((!p.matcher(definedType.getType()).matches())
            && (definedType.getLength() == 0 || !p.matcher(definedType.getType() + "(" + definedType.getLength() + ")")
                .matches())
            && (definedType.getScale() != 0 || !p.matcher(
                definedType.getType() + "(" + definedType.getPrecision() + ")").matches())
            && (!p.matcher(
                definedType.getType() + "(" + definedType.getPrecision() + "," + definedType.getScale() + ")")
                .matches())
            && (!p.matcher(
                definedType.getType() + "(" + definedType.getPrecision() + ", " + definedType.getScale() + ")")
                .matches()))
        {
          continue forcedTypeLoop;
        }
      }
      return forcedType;
    }
    return null;
  }
  
  protected static final <D extends Definition> D getDefinition(List<D> definitions, String name, boolean ignoreCase)
  {
    for (D definition : definitions)
    {
      if ((ignoreCase && definition.getName().equalsIgnoreCase(name))
          || (!ignoreCase && definition.getName().equals(name)))
      {
        return definition;
      }
    }
    return null;
  }
  
  protected final <T extends Definition> List<T> filterSchema(List<T> definitions, SchemaDefinition schema,
      Map<SchemaDefinition, List<T>> cache)
  {
    List<T> result = cache.get(schema);
    if (result == null)
    {
      result = filterSchema(definitions, schema);
      cache.put(schema, result);
    }
    return result;
  }
  
  protected final <T extends Definition> List<T> filterSchema(List<T> definitions, SchemaDefinition schema)
  {
    if (schema == null)
    {
      return definitions;
    }
    else
    {
      List<T> result = new ArrayList<T>();
      for (T definition : definitions)
      {
        if (definition.getSchema().equals(schema))
        {
          result.add(definition);
        }
      }
      return result;
    }
  }
  
  @Override
  public final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions)
  {
    return filterExcludeInclude(definitions, excludes, includes, filters);
  }
  
  protected final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions, String[] e, String[] i,
      List<Filter> f)
  {
    List<T> result = new ArrayList<T>();
    definitionsLoop: for (T definition : definitions)
    {
      if (e != null)
      {
        for (String exclude : e)
        {
          Pattern p = pattern(exclude);
          if (exclude != null
              && (p.matcher(definition.getName()).matches() || p.matcher(definition.getQualifiedName()).matches()))
          {
            if (LogUtil.getCoreLog().isDebugEnabled())
              LogUtil.getCoreLog().debug("Exclude",
                  "Excluding " + definition.getQualifiedName() + " because of pattern " + exclude);
            continue definitionsLoop;
          }
        }
      }
      if (i != null)
      {
        for (String include : i)
        {
          Pattern p = pattern(include);
          if (include != null
              && (p.matcher(definition.getName()).matches() || p.matcher(definition.getQualifiedName()).matches()))
          {
            // [#3488] This allows for filtering out additional objects, in case
            // the applicable
            // code generation configuration might cause conflicts in resulting
            // code
            // [#3526] Filters should be applied last, after <exclude/> and
            // <include/>
            for (Filter filter : f)
            {
              if (filter.exclude(definition))
              {
                if (LogUtil.getCoreLog().isDebugEnabled())
                  LogUtil.getCoreLog().debug("Exclude",
                      "Excluding " + definition.getQualifiedName() + " because of filter " + filter);
                continue definitionsLoop;
              }
            }
            result.add(definition);
            if (LogUtil.getCoreLog().isDebugEnabled())
              LogUtil.getCoreLog().debug("Include",
                  "Including " + definition.getQualifiedName() + " because of pattern " + include);
            continue definitionsLoop;
          }
        }
      }
    }
    return result;
  }
  
  @Override
  public final boolean isArrayType(String dataType)
  {
    return false;
  }
  
  protected static final String fetchedSize(List<?> fetched, List<?> included)
  {
    return fetched.size() + " (" + included.size() + " included, " + (fetched.size() - included.size()) + " excluded)";
  }
  
  protected abstract List<SchemaDefinition> getSchemata0() throws SQLException;
  
  protected abstract List<TableDefinition> getTables0() throws SQLException;
}
