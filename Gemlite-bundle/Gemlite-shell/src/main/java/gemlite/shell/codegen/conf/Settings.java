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
package gemlite.shell.codegen.conf;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Settings", propOrder = {})
@SuppressWarnings({ "all" })
public class Settings extends SettingsBase implements Serializable, Cloneable
{
  private final static long serialVersionUID = 360L;
  @XmlElement(defaultValue = "true")
  protected Boolean renderSchema = true;
  protected RenderMapping renderMapping;
  @XmlElement(defaultValue = "QUOTED")
  protected RenderNameStyle renderNameStyle = RenderNameStyle.QUOTED;
  @XmlElement(defaultValue = "AS_IS")
  protected RenderKeywordStyle renderKeywordStyle = RenderKeywordStyle.AS_IS;
  @XmlElement(defaultValue = "false")
  protected Boolean renderFormatted = false;
  @XmlElement(defaultValue = "false")
  protected Boolean renderScalarSubqueriesForStoredFunctions = false;
  @XmlElement(defaultValue = "DEFAULT")
  protected BackslashEscaping backslashEscaping = BackslashEscaping.DEFAULT;
  @XmlElement(defaultValue = "INDEXED")
  protected ParamType paramType = ParamType.INDEXED;
  @XmlElement(defaultValue = "PREPARED_STATEMENT")
  protected StatementType statementType = StatementType.PREPARED_STATEMENT;
  @XmlElement(defaultValue = "true")
  protected Boolean executeLogging = true;
  @XmlElement(defaultValue = "false")
  protected Boolean executeWithOptimisticLocking = false;
  @XmlElement(defaultValue = "true")
  protected Boolean attachRecords = true;
  @XmlElement(defaultValue = "false")
  protected Boolean updatablePrimaryKeys = false;
  @XmlElement(defaultValue = "true")
  protected Boolean reflectionCaching = true;
  @XmlElement(defaultValue = "true")
  protected Boolean fetchWarnings = true;
  
  public Boolean isRenderSchema()
  {
    return renderSchema;
  }
  
  public void setRenderSchema(Boolean value)
  {
    this.renderSchema = value;
  }
  
  public RenderMapping getRenderMapping()
  {
    return renderMapping;
  }
  
  public void setRenderMapping(RenderMapping value)
  {
    this.renderMapping = value;
  }
  
  public RenderNameStyle getRenderNameStyle()
  {
    return renderNameStyle;
  }
  
  public void setRenderNameStyle(RenderNameStyle value)
  {
    this.renderNameStyle = value;
  }
  
  public RenderKeywordStyle getRenderKeywordStyle()
  {
    return renderKeywordStyle;
  }
  
  public void setRenderKeywordStyle(RenderKeywordStyle value)
  {
    this.renderKeywordStyle = value;
  }
  
  public Boolean isRenderFormatted()
  {
    return renderFormatted;
  }
  
  public void setRenderFormatted(Boolean value)
  {
    this.renderFormatted = value;
  }
  
  public Boolean isRenderScalarSubqueriesForStoredFunctions()
  {
    return renderScalarSubqueriesForStoredFunctions;
  }
  
  public void setRenderScalarSubqueriesForStoredFunctions(Boolean value)
  {
    this.renderScalarSubqueriesForStoredFunctions = value;
  }
  
  public BackslashEscaping getBackslashEscaping()
  {
    return backslashEscaping;
  }
  
  public void setBackslashEscaping(BackslashEscaping value)
  {
    this.backslashEscaping = value;
  }
  
  public ParamType getParamType()
  {
    return paramType;
  }
  
  public void setParamType(ParamType value)
  {
    this.paramType = value;
  }
  
  public StatementType getStatementType()
  {
    return statementType;
  }
  
  public void setStatementType(StatementType value)
  {
    this.statementType = value;
  }
  
  public Boolean isExecuteLogging()
  {
    return executeLogging;
  }
  
  public void setExecuteLogging(Boolean value)
  {
    this.executeLogging = value;
  }
  
  public Boolean isExecuteWithOptimisticLocking()
  {
    return executeWithOptimisticLocking;
  }
  
  public void setExecuteWithOptimisticLocking(Boolean value)
  {
    this.executeWithOptimisticLocking = value;
  }
  
  public Boolean isAttachRecords()
  {
    return attachRecords;
  }
  
  public void setAttachRecords(Boolean value)
  {
    this.attachRecords = value;
  }
  
  public Boolean isUpdatablePrimaryKeys()
  {
    return updatablePrimaryKeys;
  }
  
  public void setUpdatablePrimaryKeys(Boolean value)
  {
    this.updatablePrimaryKeys = value;
  }
  
  public Boolean isReflectionCaching()
  {
    return reflectionCaching;
  }
  
  public void setReflectionCaching(Boolean value)
  {
    this.reflectionCaching = value;
  }
  
  public Boolean isFetchWarnings()
  {
    return fetchWarnings;
  }
  
  public void setFetchWarnings(Boolean value)
  {
    this.fetchWarnings = value;
  }
  
  public Settings withRenderSchema(Boolean value)
  {
    setRenderSchema(value);
    return this;
  }
  
  public Settings withRenderMapping(RenderMapping value)
  {
    setRenderMapping(value);
    return this;
  }
  
  public Settings withRenderNameStyle(RenderNameStyle value)
  {
    setRenderNameStyle(value);
    return this;
  }
  
  public Settings withRenderKeywordStyle(RenderKeywordStyle value)
  {
    setRenderKeywordStyle(value);
    return this;
  }
  
  public Settings withRenderFormatted(Boolean value)
  {
    setRenderFormatted(value);
    return this;
  }
  
  public Settings withRenderScalarSubqueriesForStoredFunctions(Boolean value)
  {
    setRenderScalarSubqueriesForStoredFunctions(value);
    return this;
  }
  
  public Settings withBackslashEscaping(BackslashEscaping value)
  {
    setBackslashEscaping(value);
    return this;
  }
  
  public Settings withParamType(ParamType value)
  {
    setParamType(value);
    return this;
  }
  
  public Settings withStatementType(StatementType value)
  {
    setStatementType(value);
    return this;
  }
  
  public Settings withExecuteLogging(Boolean value)
  {
    setExecuteLogging(value);
    return this;
  }
  
  public Settings withExecuteWithOptimisticLocking(Boolean value)
  {
    setExecuteWithOptimisticLocking(value);
    return this;
  }
  
  public Settings withAttachRecords(Boolean value)
  {
    setAttachRecords(value);
    return this;
  }
  
  public Settings withUpdatablePrimaryKeys(Boolean value)
  {
    setUpdatablePrimaryKeys(value);
    return this;
  }
  
  public Settings withReflectionCaching(Boolean value)
  {
    setReflectionCaching(value);
    return this;
  }
  
  public Settings withFetchWarnings(Boolean value)
  {
    setFetchWarnings(value);
    return this;
  }
}
