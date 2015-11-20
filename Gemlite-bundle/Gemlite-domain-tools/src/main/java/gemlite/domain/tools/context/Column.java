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
package gemlite.domain.tools.context;

public class Column
{
  private String name;
  private String type;
  private Object arguments;
  private Object comment;
  private String firstLetterUpper;
  private String modifiers;
  private String javaType;
  private boolean primitive;
  private boolean primary;
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
    this.firstLetterUpper = (Character.toUpperCase(name.charAt(0)) + name.substring(1));
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  public Object getComment()
  {
    return this.comment;
  }
  
  public void setComment(Object comment)
  {
    this.comment = comment;
  }
  
  public String getFirstLetterUpper()
  {
    return this.firstLetterUpper;
  }
  
  public String getModifiers()
  {
    return this.modifiers;
  }
  
  public void setModifiers(String modifiers)
  {
    this.modifiers = modifiers;
  }
  
  public String getJavaType()
  {
    return this.javaType;
  }
  
  public void setJavaType(String javaType)
  {
    this.javaType = javaType;
  }
  
  public Object getArguments()
  {
    return this.arguments;
  }
  
  public void setArguments(Object arguments)
  {
    this.arguments = arguments;
  }
  
  public boolean isPrimitive()
  {
    return this.primitive;
  }
  
  public void setPrimitive(boolean primitive)
  {
    this.primitive = primitive;
  }
  
  public boolean isPrimary()
  {
    return primary;
  }
  
  public void setPrimary(boolean primary)
  {
    this.primary = primary;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("Column [name=");
    builder.append(name);
    builder.append(", type=");
    builder.append(type);
    builder.append(", arguments=");
    builder.append(arguments);
    builder.append(", comment=");
    builder.append(comment);
    builder.append(", firstLetterUpper=");
    builder.append(firstLetterUpper);
    builder.append(", modifiers=");
    builder.append(modifiers);
    builder.append(", javaType=");
    builder.append(javaType);
    builder.append(", primitive=");
    builder.append(primitive);
    builder.append(", primary=");
    builder.append(primary);
    builder.append("]");
    return builder.toString();
  }
}
