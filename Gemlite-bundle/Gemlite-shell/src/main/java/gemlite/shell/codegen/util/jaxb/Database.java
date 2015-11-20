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
package gemlite.shell.codegen.util.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Database", propOrder = {})
@SuppressWarnings({ "all" })
public class Database implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(required = true)
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String name;
  @XmlList
  @XmlElement(defaultValue = "COMMENTS CASE_INSENSITIVE")
  protected List<RegexFlag> regexFlags;
  @XmlElement(defaultValue = ".*")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String includes = ".*";
  @XmlElement(defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String excludes = "";
  @XmlElement(defaultValue = "false")
  protected Boolean includeExcludeColumns = false;
  @XmlElement(defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String recordVersionFields = "";
  @XmlElement(defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String recordTimestampFields = "";
  @XmlElement(defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String syntheticPrimaryKeys = "";
  @XmlElement(defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String overridePrimaryKeys = "";
  @XmlElement(defaultValue = "false")
  protected Boolean dateAsTimestamp = false;
  @XmlElement(defaultValue = "true")
  protected Boolean unsignedTypes = true;
  @XmlElement(defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String inputSchema = "";
  @XmlElement(defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String schemaVersionProvider = "";
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String outputSchema;
  @XmlElement(defaultValue = "false")
  protected Boolean outputSchemaToDefault = false;
  @XmlElementWrapper(name = "properties")
  @XmlElement(name = "property")
  protected List<Property> properties;
  @XmlElementWrapper(name = "schemata")
  @XmlElement(name = "schema")
  protected List<Schema> schemata;
  @XmlElementWrapper(name = "customTypes")
  @XmlElement(name = "customType")
  protected List<CustomType> customTypes;
  @XmlElementWrapper(name = "enumTypes")
  @XmlElement(name = "enumType")
  protected List<EnumType> enumTypes;
  @XmlElementWrapper(name = "forcedTypes")
  @XmlElement(name = "forcedType")
  protected List<ForcedType> forcedTypes;
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String value)
  {
    this.name = value;
  }
  
  public List<RegexFlag> getRegexFlags()
  {
    if (regexFlags == null)
    {
      regexFlags = new ArrayList<RegexFlag>();
    }
    return this.regexFlags;
  }
  
  public String getIncludes()
  {
    return includes;
  }
  
  public void setIncludes(String value)
  {
    this.includes = value;
  }
  
  public String getExcludes()
  {
    return excludes;
  }
  
  public void setExcludes(String value)
  {
    this.excludes = value;
  }
  
  public Boolean isIncludeExcludeColumns()
  {
    return includeExcludeColumns;
  }
  
  public void setIncludeExcludeColumns(Boolean value)
  {
    this.includeExcludeColumns = value;
  }
  
  public String getRecordVersionFields()
  {
    return recordVersionFields;
  }
  
  public void setRecordVersionFields(String value)
  {
    this.recordVersionFields = value;
  }
  
  public String getRecordTimestampFields()
  {
    return recordTimestampFields;
  }
  
  public void setRecordTimestampFields(String value)
  {
    this.recordTimestampFields = value;
  }
  
  public String getSyntheticPrimaryKeys()
  {
    return syntheticPrimaryKeys;
  }
  
  public void setSyntheticPrimaryKeys(String value)
  {
    this.syntheticPrimaryKeys = value;
  }
  
  public String getOverridePrimaryKeys()
  {
    return overridePrimaryKeys;
  }
  
  public void setOverridePrimaryKeys(String value)
  {
    this.overridePrimaryKeys = value;
  }
  
  public Boolean isDateAsTimestamp()
  {
    return dateAsTimestamp;
  }
  
  public void setDateAsTimestamp(Boolean value)
  {
    this.dateAsTimestamp = value;
  }
  
  public Boolean isUnsignedTypes()
  {
    return unsignedTypes;
  }
  
  public void setUnsignedTypes(Boolean value)
  {
    this.unsignedTypes = value;
  }
  
  public String getInputSchema()
  {
    return inputSchema;
  }
  
  public void setInputSchema(String value)
  {
    this.inputSchema = value;
  }
  
  public String getSchemaVersionProvider()
  {
    return schemaVersionProvider;
  }
  
  public void setSchemaVersionProvider(String value)
  {
    this.schemaVersionProvider = value;
  }
  
  public String getOutputSchema()
  {
    return outputSchema;
  }
  
  public void setOutputSchema(String value)
  {
    this.outputSchema = value;
  }
  
  public Boolean isOutputSchemaToDefault()
  {
    return outputSchemaToDefault;
  }
  
  public void setOutputSchemaToDefault(Boolean value)
  {
    this.outputSchemaToDefault = value;
  }
  
  public List<Property> getProperties()
  {
    if (properties == null)
    {
      properties = new ArrayList<Property>();
    }
    return properties;
  }
  
  public void setProperties(List<Property> properties)
  {
    this.properties = properties;
  }
  
  public List<Schema> getSchemata()
  {
    if (schemata == null)
    {
      schemata = new ArrayList<Schema>();
    }
    return schemata;
  }
  
  public void setSchemata(List<Schema> schemata)
  {
    this.schemata = schemata;
  }
  
  public List<CustomType> getCustomTypes()
  {
    if (customTypes == null)
    {
      customTypes = new ArrayList<CustomType>();
    }
    return customTypes;
  }
  
  public void setCustomTypes(List<CustomType> customTypes)
  {
    this.customTypes = customTypes;
  }
  
  public List<EnumType> getEnumTypes()
  {
    if (enumTypes == null)
    {
      enumTypes = new ArrayList<EnumType>();
    }
    return enumTypes;
  }
  
  public void setEnumTypes(List<EnumType> enumTypes)
  {
    this.enumTypes = enumTypes;
  }
  
  public List<ForcedType> getForcedTypes()
  {
    if (forcedTypes == null)
    {
      forcedTypes = new ArrayList<ForcedType>();
    }
    return forcedTypes;
  }
  
  public void setForcedTypes(List<ForcedType> forcedTypes)
  {
    this.forcedTypes = forcedTypes;
  }
  
  public Database withName(String value)
  {
    setName(value);
    return this;
  }
  
  public Database withRegexFlags(RegexFlag... values)
  {
    if (values != null)
    {
      for (RegexFlag value : values)
      {
        getRegexFlags().add(value);
      }
    }
    return this;
  }
  
  public Database withRegexFlags(Collection<RegexFlag> values)
  {
    if (values != null)
    {
      getRegexFlags().addAll(values);
    }
    return this;
  }
  
  public Database withIncludes(String value)
  {
    setIncludes(value);
    return this;
  }
  
  public Database withExcludes(String value)
  {
    setExcludes(value);
    return this;
  }
  
  public Database withIncludeExcludeColumns(Boolean value)
  {
    setIncludeExcludeColumns(value);
    return this;
  }
  
  public Database withRecordVersionFields(String value)
  {
    setRecordVersionFields(value);
    return this;
  }
  
  public Database withRecordTimestampFields(String value)
  {
    setRecordTimestampFields(value);
    return this;
  }
  
  public Database withSyntheticPrimaryKeys(String value)
  {
    setSyntheticPrimaryKeys(value);
    return this;
  }
  
  public Database withOverridePrimaryKeys(String value)
  {
    setOverridePrimaryKeys(value);
    return this;
  }
  
  public Database withDateAsTimestamp(Boolean value)
  {
    setDateAsTimestamp(value);
    return this;
  }
  
  public Database withUnsignedTypes(Boolean value)
  {
    setUnsignedTypes(value);
    return this;
  }
  
  public Database withInputSchema(String value)
  {
    setInputSchema(value);
    return this;
  }
  
  public Database withSchemaVersionProvider(String value)
  {
    setSchemaVersionProvider(value);
    return this;
  }
  
  public Database withOutputSchema(String value)
  {
    setOutputSchema(value);
    return this;
  }
  
  public Database withOutputSchemaToDefault(Boolean value)
  {
    setOutputSchemaToDefault(value);
    return this;
  }
  
  public Database withProperties(Property... values)
  {
    if (values != null)
    {
      for (Property value : values)
      {
        getProperties().add(value);
      }
    }
    return this;
  }
  
  public Database withProperties(Collection<Property> values)
  {
    if (values != null)
    {
      getProperties().addAll(values);
    }
    return this;
  }
  
  public Database withProperties(List<Property> properties)
  {
    setProperties(properties);
    return this;
  }
  
  public Database withSchemata(Schema... values)
  {
    if (values != null)
    {
      for (Schema value : values)
      {
        getSchemata().add(value);
      }
    }
    return this;
  }
  
  public Database withSchemata(Collection<Schema> values)
  {
    if (values != null)
    {
      getSchemata().addAll(values);
    }
    return this;
  }
  
  public Database withSchemata(List<Schema> schemata)
  {
    setSchemata(schemata);
    return this;
  }
  
  public Database withCustomTypes(CustomType... values)
  {
    if (values != null)
    {
      for (CustomType value : values)
      {
        getCustomTypes().add(value);
      }
    }
    return this;
  }
  
  public Database withCustomTypes(Collection<CustomType> values)
  {
    if (values != null)
    {
      getCustomTypes().addAll(values);
    }
    return this;
  }
  
  public Database withCustomTypes(List<CustomType> customTypes)
  {
    setCustomTypes(customTypes);
    return this;
  }
  
  public Database withEnumTypes(EnumType... values)
  {
    if (values != null)
    {
      for (EnumType value : values)
      {
        getEnumTypes().add(value);
      }
    }
    return this;
  }
  
  public Database withEnumTypes(Collection<EnumType> values)
  {
    if (values != null)
    {
      getEnumTypes().addAll(values);
    }
    return this;
  }
  
  public Database withEnumTypes(List<EnumType> enumTypes)
  {
    setEnumTypes(enumTypes);
    return this;
  }
  
  public Database withForcedTypes(ForcedType... values)
  {
    if (values != null)
    {
      for (ForcedType value : values)
      {
        getForcedTypes().add(value);
      }
    }
    return this;
  }
  
  public Database withForcedTypes(Collection<ForcedType> values)
  {
    if (values != null)
    {
      getForcedTypes().addAll(values);
    }
    return this;
  }
  
  public Database withForcedTypes(List<ForcedType> forcedTypes)
  {
    setForcedTypes(forcedTypes);
    return this;
  }
}
