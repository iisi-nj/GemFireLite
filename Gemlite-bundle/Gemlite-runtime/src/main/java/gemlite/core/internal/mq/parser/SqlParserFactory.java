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
package gemlite.core.internal.mq.parser;

import gemlite.core.internal.mq.parser.impl.oracle.OracleParserDelegator;
import gemlite.core.internal.mq.parser.impl.oracle.OracleSQLLexer;
import gemlite.core.internal.mq.parser.impl.oracle.OracleSQLParser;
import gemlite.core.internal.mq.parser.impl.sybase.SybaseParserDelegator;
import gemlite.core.internal.mq.parser.impl.sybase.SybaseSQLLexer;
import gemlite.core.internal.mq.parser.impl.sybase.SybaseSQLParser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

public class SqlParserFactory
{
  public static String parserImplType = System.getProperty("gemlite.mq.parser.type", "oracle");
  
  public final static ISQLParserDelegator createSqlParser(String sqlList)
  {
    if ("sybase".equals(parserImplType))
    {
      SybaseSQLLexer lexer = new SybaseSQLLexer(new ANTLRStringStream(sqlList));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      SybaseSQLParser parser = new SybaseSQLParser(tokens);
      SybaseParserDelegator dg = new SybaseParserDelegator(parser);
      return dg;
    }
    else if ("oracle".equals(parserImplType))
    {
      OracleSQLLexer lexer = new OracleSQLLexer(new ANTLRStringStream(sqlList));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      OracleSQLParser parser = new OracleSQLParser(tokens);
      OracleParserDelegator dg = new OracleParserDelegator(parser);
      return dg;
    }
    
    return null;
  }
}
