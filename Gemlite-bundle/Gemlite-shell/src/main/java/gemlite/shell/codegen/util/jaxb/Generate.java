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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Generate", propOrder = {})
@SuppressWarnings({ "all" })
public class Generate implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(defaultValue = "true")
  protected Boolean relations = true;
  @XmlElement(defaultValue = "true")
  protected Boolean deprecated = true;
  @XmlElement(defaultValue = "true")
  protected Boolean instanceFields = true;
  @XmlElement(defaultValue = "true")
  protected Boolean generatedAnnotation = true;
  @XmlElement(defaultValue = "true")
  protected Boolean records = true;
  @XmlElement(defaultValue = "false")
  protected Boolean pojos = false;
  @XmlElement(defaultValue = "false")
  protected Boolean pojosEqualsAndHashCode = false;
  @XmlElement(defaultValue = "false")
  protected Boolean immutablePojos = false;
  @XmlElement(defaultValue = "false")
  protected Boolean interfaces = false;
  @XmlElement(defaultValue = "false")
  protected Boolean daos = false;
  @XmlElement(defaultValue = "false")
  protected Boolean jpaAnnotations = false;
  @XmlElement(defaultValue = "false")
  protected Boolean validationAnnotations = false;
  @XmlElement(defaultValue = "true")
  protected Boolean globalObjectReferences = true;
  @XmlElement(defaultValue = "true")
  protected Boolean globalTableReferences = true;
  @XmlElement(defaultValue = "true")
  protected Boolean globalSequenceReferences = true;
  @XmlElement(defaultValue = "true")
  protected Boolean globalUDTReferences = true;
  @XmlElement(defaultValue = "true")
  protected Boolean globalRoutineReferences = true;
  @XmlElement(defaultValue = "false")
  protected Boolean fluentSetters = false;
  @XmlElement(defaultValue = "")
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String fullyQualifiedTypes = "";
  
  public Boolean isRelations()
  {
    return relations;
  }
  
  public void setRelations(Boolean value)
  {
    this.relations = value;
  }
  
  public Boolean isDeprecated()
  {
    return deprecated;
  }
  
  public void setDeprecated(Boolean value)
  {
    this.deprecated = value;
  }
  
  public Boolean isInstanceFields()
  {
    return instanceFields;
  }
  
  public void setInstanceFields(Boolean value)
  {
    this.instanceFields = value;
  }
  
  public Boolean isGeneratedAnnotation()
  {
    return generatedAnnotation;
  }
  
  public void setGeneratedAnnotation(Boolean value)
  {
    this.generatedAnnotation = value;
  }
  
  public Boolean isRecords()
  {
    return records;
  }
  
  public void setRecords(Boolean value)
  {
    this.records = value;
  }
  
  public Boolean isPojos()
  {
    return pojos;
  }
  
  public void setPojos(Boolean value)
  {
    this.pojos = value;
  }
  
  public Boolean isPojosEqualsAndHashCode()
  {
    return pojosEqualsAndHashCode;
  }
  
  public void setPojosEqualsAndHashCode(Boolean value)
  {
    this.pojosEqualsAndHashCode = value;
  }
  
  public Boolean isImmutablePojos()
  {
    return immutablePojos;
  }
  
  public void setImmutablePojos(Boolean value)
  {
    this.immutablePojos = value;
  }
  
  public Boolean isInterfaces()
  {
    return interfaces;
  }
  
  public void setInterfaces(Boolean value)
  {
    this.interfaces = value;
  }
  
  public Boolean isDaos()
  {
    return daos;
  }
  
  public void setDaos(Boolean value)
  {
    this.daos = value;
  }
  
  public Boolean isJpaAnnotations()
  {
    return jpaAnnotations;
  }
  
  public void setJpaAnnotations(Boolean value)
  {
    this.jpaAnnotations = value;
  }
  
  public Boolean isValidationAnnotations()
  {
    return validationAnnotations;
  }
  
  public void setValidationAnnotations(Boolean value)
  {
    this.validationAnnotations = value;
  }
  
  public Boolean isGlobalObjectReferences()
  {
    return globalObjectReferences;
  }
  
  public void setGlobalObjectReferences(Boolean value)
  {
    this.globalObjectReferences = value;
  }
  
  public Boolean isGlobalTableReferences()
  {
    return globalTableReferences;
  }
  
  public void setGlobalTableReferences(Boolean value)
  {
    this.globalTableReferences = value;
  }
  
  public Boolean isGlobalSequenceReferences()
  {
    return globalSequenceReferences;
  }
  
  public void setGlobalSequenceReferences(Boolean value)
  {
    this.globalSequenceReferences = value;
  }
  
  public Boolean isGlobalUDTReferences()
  {
    return globalUDTReferences;
  }
  
  public void setGlobalUDTReferences(Boolean value)
  {
    this.globalUDTReferences = value;
  }
  
  public Boolean isGlobalRoutineReferences()
  {
    return globalRoutineReferences;
  }
  
  public void setGlobalRoutineReferences(Boolean value)
  {
    this.globalRoutineReferences = value;
  }
  
  public Boolean isFluentSetters()
  {
    return fluentSetters;
  }
  
  public void setFluentSetters(Boolean value)
  {
    this.fluentSetters = value;
  }
  
  public String getFullyQualifiedTypes()
  {
    return fullyQualifiedTypes;
  }
  
  public void setFullyQualifiedTypes(String value)
  {
    this.fullyQualifiedTypes = value;
  }
  
  public Generate withRelations(Boolean value)
  {
    setRelations(value);
    return this;
  }
  
  public Generate withDeprecated(Boolean value)
  {
    setDeprecated(value);
    return this;
  }
  
  public Generate withInstanceFields(Boolean value)
  {
    setInstanceFields(value);
    return this;
  }
  
  public Generate withGeneratedAnnotation(Boolean value)
  {
    setGeneratedAnnotation(value);
    return this;
  }
  
  public Generate withRecords(Boolean value)
  {
    setRecords(value);
    return this;
  }
  
  public Generate withPojos(Boolean value)
  {
    setPojos(value);
    return this;
  }
  
  public Generate withPojosEqualsAndHashCode(Boolean value)
  {
    setPojosEqualsAndHashCode(value);
    return this;
  }
  
  public Generate withImmutablePojos(Boolean value)
  {
    setImmutablePojos(value);
    return this;
  }
  
  public Generate withInterfaces(Boolean value)
  {
    setInterfaces(value);
    return this;
  }
  
  public Generate withDaos(Boolean value)
  {
    setDaos(value);
    return this;
  }
  
  public Generate withJpaAnnotations(Boolean value)
  {
    setJpaAnnotations(value);
    return this;
  }
  
  public Generate withValidationAnnotations(Boolean value)
  {
    setValidationAnnotations(value);
    return this;
  }
  
  public Generate withGlobalObjectReferences(Boolean value)
  {
    setGlobalObjectReferences(value);
    return this;
  }
  
  public Generate withGlobalTableReferences(Boolean value)
  {
    setGlobalTableReferences(value);
    return this;
  }
  
  public Generate withGlobalSequenceReferences(Boolean value)
  {
    setGlobalSequenceReferences(value);
    return this;
  }
  
  public Generate withGlobalUDTReferences(Boolean value)
  {
    setGlobalUDTReferences(value);
    return this;
  }
  
  public Generate withGlobalRoutineReferences(Boolean value)
  {
    setGlobalRoutineReferences(value);
    return this;
  }
  
  public Generate withFluentSetters(Boolean value)
  {
    setFluentSetters(value);
    return this;
  }
  
  public Generate withFullyQualifiedTypes(String value)
  {
    setFullyQualifiedTypes(value);
    return this;
  }
}
