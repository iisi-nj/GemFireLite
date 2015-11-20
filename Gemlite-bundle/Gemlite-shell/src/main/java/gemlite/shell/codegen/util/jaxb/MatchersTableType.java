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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersTableType", propOrder = {})
@SuppressWarnings({ "all" })
public class MatchersTableType implements Serializable
{
  private final static long serialVersionUID = 360L;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String expression;
  protected MatcherRule tableClass;
  protected MatcherRule tableIdentifier;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String tableImplements;
  protected MatcherRule recordClass;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String recordImplements;
  protected MatcherRule interfaceClass;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String interfaceImplements;
  protected MatcherRule daoClass;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String daoImplements;
  protected MatcherRule pojoClass;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String pojoExtends;
  @XmlJavaTypeAdapter(TrimAdapter.class)
  protected String pojoImplements;
  
  public String getExpression()
  {
    return expression;
  }
  
  public void setExpression(String value)
  {
    this.expression = value;
  }
  
  public MatcherRule getTableClass()
  {
    return tableClass;
  }
  
  public void setTableClass(MatcherRule value)
  {
    this.tableClass = value;
  }
  
  public MatcherRule getTableIdentifier()
  {
    return tableIdentifier;
  }
  
  public void setTableIdentifier(MatcherRule value)
  {
    this.tableIdentifier = value;
  }
  
  public String getTableImplements()
  {
    return tableImplements;
  }
  
  public void setTableImplements(String value)
  {
    this.tableImplements = value;
  }
  
  public MatcherRule getRecordClass()
  {
    return recordClass;
  }
  
  public void setRecordClass(MatcherRule value)
  {
    this.recordClass = value;
  }
  
  public String getRecordImplements()
  {
    return recordImplements;
  }
  
  public void setRecordImplements(String value)
  {
    this.recordImplements = value;
  }
  
  public MatcherRule getInterfaceClass()
  {
    return interfaceClass;
  }
  
  public void setInterfaceClass(MatcherRule value)
  {
    this.interfaceClass = value;
  }
  
  public String getInterfaceImplements()
  {
    return interfaceImplements;
  }
  
  public void setInterfaceImplements(String value)
  {
    this.interfaceImplements = value;
  }
  
  public MatcherRule getDaoClass()
  {
    return daoClass;
  }
  
  public void setDaoClass(MatcherRule value)
  {
    this.daoClass = value;
  }
  
  public String getDaoImplements()
  {
    return daoImplements;
  }
  
  public void setDaoImplements(String value)
  {
    this.daoImplements = value;
  }
  
  public MatcherRule getPojoClass()
  {
    return pojoClass;
  }
  
  public void setPojoClass(MatcherRule value)
  {
    this.pojoClass = value;
  }
  
  public String getPojoExtends()
  {
    return pojoExtends;
  }
  
  public void setPojoExtends(String value)
  {
    this.pojoExtends = value;
  }
  
  public String getPojoImplements()
  {
    return pojoImplements;
  }
  
  public void setPojoImplements(String value)
  {
    this.pojoImplements = value;
  }
  
  public MatchersTableType withExpression(String value)
  {
    setExpression(value);
    return this;
  }
  
  public MatchersTableType withTableClass(MatcherRule value)
  {
    setTableClass(value);
    return this;
  }
  
  public MatchersTableType withTableIdentifier(MatcherRule value)
  {
    setTableIdentifier(value);
    return this;
  }
  
  public MatchersTableType withTableImplements(String value)
  {
    setTableImplements(value);
    return this;
  }
  
  public MatchersTableType withRecordClass(MatcherRule value)
  {
    setRecordClass(value);
    return this;
  }
  
  public MatchersTableType withRecordImplements(String value)
  {
    setRecordImplements(value);
    return this;
  }
  
  public MatchersTableType withInterfaceClass(MatcherRule value)
  {
    setInterfaceClass(value);
    return this;
  }
  
  public MatchersTableType withInterfaceImplements(String value)
  {
    setInterfaceImplements(value);
    return this;
  }
  
  public MatchersTableType withDaoClass(MatcherRule value)
  {
    setDaoClass(value);
    return this;
  }
  
  public MatchersTableType withDaoImplements(String value)
  {
    setDaoImplements(value);
    return this;
  }
  
  public MatchersTableType withPojoClass(MatcherRule value)
  {
    setPojoClass(value);
    return this;
  }
  
  public MatchersTableType withPojoExtends(String value)
  {
    setPojoExtends(value);
    return this;
  }
  
  public MatchersTableType withPojoImplements(String value)
  {
    setPojoImplements(value);
    return this;
  }
}
