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
package gemlite.domain.tools.parser.oracle;

import java.util.HashMap;
import java.util.Map;

public class Oracle2JavaType
{
  public static final Map<String, String> ORACLE_TO_JAVA = new HashMap<String, String>();
  static
  {
    ORACLE_TO_JAVA.put("CHAR", "String");
    ORACLE_TO_JAVA.put("char", "String");
    ORACLE_TO_JAVA.put("VARCHAR", "String");
    ORACLE_TO_JAVA.put("varchar", "String");
    ORACLE_TO_JAVA.put("LONGVARCHAR", "String");
    ORACLE_TO_JAVA.put("longvarchar", "String");
    ORACLE_TO_JAVA.put("NUMERIC", "java.math.BigDecimal");
    ORACLE_TO_JAVA.put("numeric", "java.math.BigDecimal");
    ORACLE_TO_JAVA.put("DECIMAL", "java.math.BigDecimal");
    ORACLE_TO_JAVA.put("decimal", "java.math.BigDecimal");
    ORACLE_TO_JAVA.put("NUMBER", "double");
    ORACLE_TO_JAVA.put("number", "double");
    ORACLE_TO_JAVA.put("VARCHAR2", "String");
    ORACLE_TO_JAVA.put("varchar2", "String");
    ORACLE_TO_JAVA.put("NVARCHAR2", "String");
    ORACLE_TO_JAVA.put("nvarchar2", "String");
    ORACLE_TO_JAVA.put("DATE", "java.util.Date");
    ORACLE_TO_JAVA.put("date", "java.util.Date");
    ORACLE_TO_JAVA.put("CLOB", "java.lang.Object");
    ORACLE_TO_JAVA.put("clob", "java.lang.Object");
    ORACLE_TO_JAVA.put("RAW", "String");
    ORACLE_TO_JAVA.put("raw", "String");
    ORACLE_TO_JAVA.put("FLOAT", "float");
    ORACLE_TO_JAVA.put("float", "float");
    ORACLE_TO_JAVA.put("INTEGER", "int");
    ORACLE_TO_JAVA.put("integer", "int");
    ORACLE_TO_JAVA.put("TIMESTAMP", "long");
    ORACLE_TO_JAVA.put("timestamp", "long");
    ORACLE_TO_JAVA.put("SMALLINT", "int");
    ORACLE_TO_JAVA.put("smallint", "int");
  }
}
