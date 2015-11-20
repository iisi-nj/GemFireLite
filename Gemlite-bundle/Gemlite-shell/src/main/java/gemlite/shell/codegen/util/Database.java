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

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import gemlite.shell.codegen.database.SQLDialect;
import gemlite.shell.codegen.util.jaxb.CustomType;
import gemlite.shell.codegen.util.jaxb.EnumType;
import gemlite.shell.codegen.util.jaxb.ForcedType;
import gemlite.shell.codegen.util.jaxb.RegexFlag;
import gemlite.shell.codegen.util.jaxb.Schema;

public interface Database
{
  List<SchemaDefinition> getSchemata();
  
  SchemaDefinition getSchema(String name);
  
  List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema);
  
  List<TableDefinition> getTables(SchemaDefinition schema);
  
  TableDefinition getTable(SchemaDefinition schema, String name);
  
  TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase);
  
  void setConnection(Connection connection);
  
  Connection getConnection();
  
  List<String> getInputSchemata();
  
  @Deprecated
  String getOutputSchema(String inputSchema);
  
  void setConfiguredSchemata(List<Schema> schemata);
  
  void setExcludes(String[] excludes);
  
  String[] getExcludes();
  
  void setIncludes(String[] includes);
  
  String[] getIncludes();
  
  void setIncludeExcludeColumns(boolean includeExcludeColumns);
  
  boolean getIncludeExcludeColumns();
  
  void addFilter(Filter filter);
  
  List<Filter> getFilters();
  
  <D extends Definition> List<D> filterExcludeInclude(List<D> definitions);
  
  void setRegexFlags(List<RegexFlag> regexFlags);
  
  List<RegexFlag> getRegexFlags();
  
  void setRecordVersionFields(String[] recordVersionFields);
  
  String[] getRecordVersionFields();
  
  void setRecordTimestampFields(String[] recordTimestampFields);
  
  String[] getRecordTimestampFields();
  
  void setSyntheticPrimaryKeys(String[] primaryKeys);
  
  String[] getSyntheticPrimaryKeys();
  
  void setOverridePrimaryKeys(String[] primaryKeys);
  
  String[] getOverridePrimaryKeys();
  
  void setConfiguredCustomTypes(List<CustomType> types);
  
  List<CustomType> getConfiguredCustomTypes();
  
  CustomType getConfiguredCustomType(String name);
  
  void setConfiguredEnumTypes(List<EnumType> types);
  
  List<EnumType> getConfiguredEnumTypes();
  
  void setConfiguredForcedTypes(List<ForcedType> types);
  
  SchemaVersionProvider getSchemaVersionProvider();
  
  void setSchemaVersionProvider(SchemaVersionProvider provider);
  
  List<ForcedType> getConfiguredForcedTypes();
  
  ForcedType getConfiguredForcedType(Definition definition);
  
  ForcedType getConfiguredForcedType(Definition definition, DataTypeDefinition definedType);
  
  SQLDialect getDialect();
  
  boolean isArrayType(String dataType);
  
  void setSupportsUnsignedTypes(boolean supportsUnsignedTypes);
  
  boolean supportsUnsignedTypes();
  
  void setDateAsTimestamp(boolean dateAsTimestamp);
  
  boolean dateAsTimestamp();
  
  void setIncludeRelations(boolean includeRelations);
  
  boolean includeRelations();
  
  void setProperties(Properties properties);
  
  public interface Filter
  {
    boolean exclude(Definition definition);
  }
}
