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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import java.util.Set;

class GenerationUtil
{
  private static Set<String> JAVA_KEYWORDS = unmodifiableSet(new HashSet<String>(asList("abstract", "assert",
      "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "double", "do",
      "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import",
      "instanceof", "interface", "int", "long", "native", "new", "package", "private", "protected", "public", "return",
      "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true",
      "try", "void", "volatile", "while")));
  
  public static String convertToJavaIdentifier(String literal)
  {
    if (JAVA_KEYWORDS.contains(literal))
    {
      return literal + "_";
    }
    StringBuilder sb = new StringBuilder();
    if ("".equals(literal))
    {
      return "_";
    }
    for (int i = 0; i < literal.length(); i++)
    {
      char c = literal.charAt(i);
      if (!Character.isJavaIdentifierPart(c))
      {
        sb.append(escape(c));
      }
      else if (i == 0 && !Character.isJavaIdentifierStart(literal.charAt(0)))
      {
        sb.append("_");
        sb.append(c);
      }
      else
      {
        sb.append(c);
      }
    }
    return sb.toString();
  }
  
  private static String escape(char c)
  {
    if (c == ' ' || c == '-')
      return "_";
    else
      return "_" + Integer.toHexString(c);
  }
  
  static String getSimpleJavaType(String qualifiedJavaType)
  {
    if (qualifiedJavaType == null)
    {
      return null;
    }
    return qualifiedJavaType.replaceAll(".*\\.", "");
  }
  
  public static Integer[] range(Integer from, Integer to)
  {
    Integer[] result = new Integer[to - from + 1];
    for (int i = from; i <= to; i++)
    {
      result[i - from] = i;
    }
    return result;
  }
}
