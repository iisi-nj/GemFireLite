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
package gemlite.shell.codegen.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils
{
  public static final String EMPTY = "";
  public static final int INDEX_NOT_FOUND = -1;
  private static final int PAD_LIMIT = 8192;
  
  // Defaults
  // -----------------------------------------------------------------------
  public static String defaultString(String str)
  {
    return str == null ? "" : str;
  }
  
  public static String defaultString(String str, String defaultStr)
  {
    return str == null ? defaultStr : str;
  }
  
  public static String defaultIfEmpty(String str, String defaultStr)
  {
    return StringUtils.isEmpty(str) ? defaultStr : str;
  }
  
  public static String defaultIfBlank(String str, String defaultStr)
  {
    return StringUtils.isBlank(str) ? defaultStr : str;
  }
  
  // Empty checks
  // -----------------------------------------------------------------------
  public static boolean isEmpty(String str)
  {
    return str == null || str.length() == 0;
  }
  
  public static boolean isBlank(String str)
  {
    int strLen;
    if (str == null || (strLen = str.length()) == 0)
    {
      return true;
    }
    for (int i = 0; i < strLen; i++)
    {
      if ((Character.isWhitespace(str.charAt(i)) == false))
      {
        return false;
      }
    }
    return true;
  }
  
  // Count matches
  // -----------------------------------------------------------------------
  public static int countMatches(String str, String sub)
  {
    if (isEmpty(str) || isEmpty(sub))
    {
      return 0;
    }
    int count = 0;
    int idx = 0;
    while ((idx = str.indexOf(sub, idx)) != -1)
    {
      count++;
      idx += sub.length();
    }
    return count;
  }
  
  // Padding
  // -----------------------------------------------------------------------
  public static String rightPad(String str, int size)
  {
    return rightPad(str, size, ' ');
  }
  
  public static String rightPad(String str, int size, char padChar)
  {
    if (str == null)
    {
      return null;
    }
    int pads = size - str.length();
    if (pads <= 0)
    {
      return str; // returns original String when possible
    }
    if (pads > PAD_LIMIT)
    {
      return rightPad(str, size, String.valueOf(padChar));
    }
    return str.concat(padding(pads, padChar));
  }
  
  public static String rightPad(String str, int size, String padStr)
  {
    if (str == null)
    {
      return null;
    }
    if (isEmpty(padStr))
    {
      padStr = " ";
    }
    int padLen = padStr.length();
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0)
    {
      return str; // returns original String when possible
    }
    if (padLen == 1 && pads <= PAD_LIMIT)
    {
      return rightPad(str, size, padStr.charAt(0));
    }
    if (pads == padLen)
    {
      return str.concat(padStr);
    }
    else if (pads < padLen)
    {
      return str.concat(padStr.substring(0, pads));
    }
    else
    {
      char[] padding = new char[pads];
      char[] padChars = padStr.toCharArray();
      for (int i = 0; i < pads; i++)
      {
        padding[i] = padChars[i % padLen];
      }
      return str.concat(new String(padding));
    }
  }
  
  public static String leftPad(String str, int size)
  {
    return leftPad(str, size, ' ');
  }
  
  public static String leftPad(String str, int size, char padChar)
  {
    if (str == null)
    {
      return null;
    }
    int pads = size - str.length();
    if (pads <= 0)
    {
      return str; // returns original String when possible
    }
    if (pads > PAD_LIMIT)
    {
      return leftPad(str, size, String.valueOf(padChar));
    }
    return padding(pads, padChar).concat(str);
  }
  
  public static String leftPad(String str, int size, String padStr)
  {
    if (str == null)
    {
      return null;
    }
    if (isEmpty(padStr))
    {
      padStr = " ";
    }
    int padLen = padStr.length();
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0)
    {
      return str; // returns original String when possible
    }
    if (padLen == 1 && pads <= PAD_LIMIT)
    {
      return leftPad(str, size, padStr.charAt(0));
    }
    if (pads == padLen)
    {
      return padStr.concat(str);
    }
    else if (pads < padLen)
    {
      return padStr.substring(0, pads).concat(str);
    }
    else
    {
      char[] padding = new char[pads];
      char[] padChars = padStr.toCharArray();
      for (int i = 0; i < pads; i++)
      {
        padding[i] = padChars[i % padLen];
      }
      return new String(padding).concat(str);
    }
  }
  
  private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException
  {
    if (repeat < 0)
    {
      throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
    }
    final char[] buf = new char[repeat];
    for (int i = 0; i < buf.length; i++)
    {
      buf[i] = padChar;
    }
    return new String(buf);
  }
  
  // Abbreviating
  // -----------------------------------------------------------------------
  public static String abbreviate(String str, int maxWidth)
  {
    return abbreviate(str, 0, maxWidth);
  }
  
  public static String abbreviate(String str, int offset, int maxWidth)
  {
    if (str == null)
    {
      return null;
    }
    if (maxWidth < 4)
    {
      throw new IllegalArgumentException("Minimum abbreviation width is 4");
    }
    if (str.length() <= maxWidth)
    {
      return str;
    }
    if (offset > str.length())
    {
      offset = str.length();
    }
    if ((str.length() - offset) < (maxWidth - 3))
    {
      offset = str.length() - (maxWidth - 3);
    }
    if (offset <= 4)
    {
      return str.substring(0, maxWidth - 3) + "...";
    }
    if (maxWidth < 7)
    {
      throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
    }
    if ((offset + (maxWidth - 3)) < str.length())
    {
      return "..." + abbreviate(str.substring(offset), maxWidth - 3);
    }
    return "..." + str.substring(str.length() - (maxWidth - 3));
  }
  
  // ContainsAny
  // -----------------------------------------------------------------------
  public static boolean containsAny(String str, char... searchChars)
  {
    if (str == null || str.length() == 0 || searchChars == null || searchChars.length == 0)
    {
      return false;
    }
    for (int i = 0; i < str.length(); i++)
    {
      char ch = str.charAt(i);
      for (int j = 0; j < searchChars.length; j++)
      {
        if (searchChars[j] == ch)
        {
          return true;
        }
      }
    }
    return false;
  }
  
  public static String replace(String text, String searchString, String replacement)
  {
    return replace(text, searchString, replacement, -1);
  }
  
  public static String replace(String text, String searchString, String replacement, int max)
  {
    if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0)
    {
      return text;
    }
    int start = 0;
    int end = text.indexOf(searchString, start);
    if (end == INDEX_NOT_FOUND)
    {
      return text;
    }
    int replLength = searchString.length();
    int increase = replacement.length() - replLength;
    increase = (increase < 0 ? 0 : increase);
    increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
    StringBuilder buf = new StringBuilder(text.length() + increase);
    while (end != INDEX_NOT_FOUND)
    {
      buf.append(text.substring(start, end)).append(replacement);
      start = end + replLength;
      if (--max == 0)
      {
        break;
      }
      end = text.indexOf(searchString, start);
    }
    buf.append(text.substring(start));
    return buf.toString();
  }
  
  public static String replaceEach(String text, String[] searchList, String[] replacementList)
  {
    return replaceEach(text, searchList, replacementList, false, 0);
  }
  
  private static String replaceEach(String text, String[] searchList, String[] replacementList, boolean repeat,
      int timeToLive)
  {
    // mchyzer Performance note: This creates very few new objects (one major
    // goal)
    // let me know if there are performance requests, we can create a harness to
    // measure
    if (text == null || text.length() == 0 || searchList == null || searchList.length == 0 || replacementList == null
        || replacementList.length == 0)
    {
      return text;
    }
    // if recursing, this shouldnt be less than 0
    if (timeToLive < 0)
    {
      throw new IllegalStateException("TimeToLive of " + timeToLive + " is less than 0: " + text);
    }
    int searchLength = searchList.length;
    int replacementLength = replacementList.length;
    // make sure lengths are ok, these need to be equal
    if (searchLength != replacementLength)
    {
      throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs "
          + replacementLength);
    }
    // keep track of which still have matches
    boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
    // index on index that the match was found
    int textIndex = -1;
    int replaceIndex = -1;
    int tempIndex = -1;
    // index of replace array that will replace the search string found
    // NOTE: logic duplicated below START
    for (int i = 0; i < searchLength; i++)
    {
      if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
          || replacementList[i] == null)
      {
        continue;
      }
      tempIndex = text.indexOf(searchList[i]);
      // see if we need to keep searching for this
      if (tempIndex == -1)
      {
        noMoreMatchesForReplIndex[i] = true;
      }
      else
      {
        if (textIndex == -1 || tempIndex < textIndex)
        {
          textIndex = tempIndex;
          replaceIndex = i;
        }
      }
    }
    // NOTE: logic mostly below END
    // no search strings found, we are done
    if (textIndex == -1)
    {
      return text;
    }
    int start = 0;
    // get a good guess on the size of the result buffer so it doesnt have to
    // double if it goes over a bit
    int increase = 0;
    // count the replacement text elements that are larger than their
    // corresponding text being replaced
    for (int i = 0; i < searchList.length; i++)
    {
      int greater = replacementList[i].length() - searchList[i].length();
      if (greater > 0)
      {
        increase += 3 * greater; // assume 3 matches
      }
    }
    // have upper-bound at 20% increase, then let Java take over
    increase = Math.min(increase, text.length() / 5);
    StringBuffer buf = new StringBuffer(text.length() + increase);
    while (textIndex != -1)
    {
      for (int i = start; i < textIndex; i++)
      {
        buf.append(text.charAt(i));
      }
      buf.append(replacementList[replaceIndex]);
      start = textIndex + searchList[replaceIndex].length();
      textIndex = -1;
      replaceIndex = -1;
      tempIndex = -1;
      // find the next earliest match
      // NOTE: logic mostly duplicated above START
      for (int i = 0; i < searchLength; i++)
      {
        if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length() == 0
            || replacementList[i] == null)
        {
          continue;
        }
        tempIndex = text.indexOf(searchList[i], start);
        // see if we need to keep searching for this
        if (tempIndex == -1)
        {
          noMoreMatchesForReplIndex[i] = true;
        }
        else
        {
          if (textIndex == -1 || tempIndex < textIndex)
          {
            textIndex = tempIndex;
            replaceIndex = i;
          }
        }
      }
      // NOTE: logic duplicated above END
    }
    int textLength = text.length();
    for (int i = start; i < textLength; i++)
    {
      buf.append(text.charAt(i));
    }
    String result = buf.toString();
    if (!repeat)
    {
      return result;
    }
    return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
  }
  
  // Joining
  // -----------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  public static <T> String join(T... elements)
  {
    return join(elements, null);
  }
  
  public static String join(Object[] array, char separator)
  {
    if (array == null)
    {
      return null;
    }
    return join(array, separator, 0, array.length);
  }
  
  public static String join(Object[] array, char separator, int startIndex, int endIndex)
  {
    if (array == null)
    {
      return null;
    }
    int noOfItems = (endIndex - startIndex);
    if (noOfItems <= 0)
    {
      return EMPTY;
    }
    StringBuilder buf = new StringBuilder(noOfItems * 16);
    for (int i = startIndex; i < endIndex; i++)
    {
      if (i > startIndex)
      {
        buf.append(separator);
      }
      if (array[i] != null)
      {
        buf.append(array[i]);
      }
    }
    return buf.toString();
  }
  
  public static String join(Object[] array, String separator)
  {
    if (array == null)
    {
      return null;
    }
    return join(array, separator, 0, array.length);
  }
  
  public static String join(Object[] array, String separator, int startIndex, int endIndex)
  {
    if (array == null)
    {
      return null;
    }
    if (separator == null)
    {
      separator = EMPTY;
    }
    // endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
    // len(separator))
    // (Assuming that all Strings are roughly equally long)
    int noOfItems = (endIndex - startIndex);
    if (noOfItems <= 0)
    {
      return EMPTY;
    }
    StringBuilder buf = new StringBuilder(noOfItems * 16);
    for (int i = startIndex; i < endIndex; i++)
    {
      if (i > startIndex)
      {
        buf.append(separator);
      }
      if (array[i] != null)
      {
        buf.append(array[i]);
      }
    }
    return buf.toString();
  }
  
  private StringUtils()
  {
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  public static boolean equals(Object object1, Object object2)
  {
    if (object1 == object2)
    {
      return true;
    }
    if ((object1 == null) || (object2 == null))
    {
      return false;
    }
    return object1.equals(object2);
  }
  
  public static <T> T defaultIfNull(T object, T defaultValue)
  {
    return object != null ? object : defaultValue;
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  public static String toCamelCase(String string)
  {
    StringBuilder result = new StringBuilder();
    // [#2515] - Keep trailing underscores
    for (String word : string.split("_", -1))
    {
      // Uppercase first letter of a word
      if (word.length() > 0)
      {
        // [#82] - If a word starts with a digit, prevail the
        // underscore to prevent naming clashes
        if (Character.isDigit(word.charAt(0)))
        {
          result.append("_");
        }
        result.append(word.substring(0, 1).toUpperCase());
        result.append(word.substring(1).toLowerCase());
      }
      // If no letter exists, prevail the underscore (e.g. leading
      // underscores)
      else
      {
        result.append("_");
      }
    }
    return result.toString();
  }
  
  public static String toCamelCaseLC(String string)
  {
    return toLC(toCamelCase(string));
  }
  
  public static String toLC(String string)
  {
    if (string == null || string.isEmpty())
    {
      return string;
    }
    return Character.toLowerCase(string.charAt(0)) + string.substring(1);
  }
  
  public static String[] split(String regex, CharSequence input)
  {
    int index = 0;
    ArrayList<String> matchList = new ArrayList<String>();
    Matcher m = Pattern.compile(regex).matcher(input);
    // Add segments before each match found
    while (m.find())
    {
      matchList.add(input.subSequence(index, m.start()).toString());
      matchList.add(input.subSequence(m.start(), m.end()).toString());
      index = m.end();
    }
    // If no match was found, return this
    if (index == 0)
      return new String[] { input.toString() };
    // Add remaining segment
    matchList.add(input.subSequence(index, input.length()).toString());
    // Construct result
    Iterator<String> it = matchList.iterator();
    while (it.hasNext())
    {
      if ("".equals(it.next()))
      {
        it.remove();
      }
    }
    String[] result = new String[matchList.size()];
    return matchList.toArray(result);
  }
}
