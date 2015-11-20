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
package gemlite.shell.codegen.database;

import java.io.Serializable;

public interface DataType<T> extends Serializable
{
  DataType<T> getSQLDataType();
  
  DataType<T> getDataType(Configuration configuration);
  
  int getSQLType();
  
  Class<T> getType();
  
  Class<T[]> getArrayType();
  
  String getTypeName();
  
  String getTypeName(Configuration configuration);
  
  String getCastTypeName();
  
  String getCastTypeName(Configuration configuration);
  
  SQLDialect getDialect();
  
  DataType<T> nullable(boolean nullable);
  
  boolean nullable();
  
  DataType<T> defaulted(boolean defaulted);
  
  boolean defaulted();
  
  DataType<T> precision(int precision);
  
  DataType<T> precision(int precision, int scale);
  
  int precision();
  
  boolean hasPrecision();
  
  DataType<T> scale(int scale);
  
  int scale();
  
  boolean hasScale();
  
  DataType<T> length(int length);
  
  int length();
  
  boolean hasLength();
  
  boolean isNumeric();
  
  boolean isString();
  
  boolean isDateTime();
  
  boolean isTemporal();
  
  boolean isInterval();
  
  boolean isBinary();
  
  boolean isLob();
  
  boolean isArray();
}
