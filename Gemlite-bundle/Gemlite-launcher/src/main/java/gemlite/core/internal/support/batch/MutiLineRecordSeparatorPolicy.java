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
package gemlite.core.internal.support.batch;

import gemlite.core.util.LogUtil;

import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;

public class MutiLineRecordSeparatorPolicy implements RecordSeparatorPolicy
{

  private char separator;
  private char quotechar;
  private int columns;

  @Override
  public boolean isEndOfRecord(String line)
  {
    CSVParser parser = new CSVParser(separator, quotechar);
    String arr[] = new String[]{};
    try
    {
      arr = parser.parseLineMulti(line);
    }
    catch (Exception e)
    {
      LogUtil.getAppLog().error("isEndOfRecord error", e);
      return true;
    }
    return (!parser.isPending() || arr.length>=columns);
  }
  
  @Override
  public String postProcess(String record)
  {
    return "'"+record+"'";
  }
  
  @Override
  public String preProcess(String record)
  {
    return record;
  }

  public char getSeparator()
  {
    return separator;
  }

  public void setSeparator(char separator)
  {
    this.separator = separator;
  }

  public char getQuotechar()
  {
    return quotechar;
  }

  public void setQuotechar(char quotechar)
  {
    this.quotechar = quotechar;
  }

  public int getColumns()
  {
    return columns;
  }

  public void setColumns(int columns)
  {
    this.columns = columns;
  }
}
