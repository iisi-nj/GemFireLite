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
package gemlite.core.internal.batch;


/***
 * 
 * @author ynd
 * 
 */
public class BatchParameter 
{
  public BatchParameter()
  {
  }
  
  
  public BatchParameter (String template, String file, String delimiter, String quote, boolean skipable,
      String columns, String region, String table, String encoding, int linesToSkip,String sortKey,String where,int pageSize,int fetchSize)
  {
    super();
    this.name = region;
    this.template = template;
    this.file = file;
    this.delimiter = delimiter;
    this.quote = quote;
    this.skipable = skipable;
    this.columns = columns;
    this.region = region;
    this.table = table;
    this.encoding = encoding;
    this.linesToSkip = linesToSkip;
    this.sortKey = sortKey;
    this.where = where;
    this.pageSize = pageSize;
    this.fetchSize = fetchSize;
  }

  private String name;
  private String template;
  private String file;
  private String delimiter;
  private String quote;
  private boolean skipable;
  private String columns;
  private String region;
  private String table;
  private String encoding;
  private int linesToSkip;
  
  
  //数据库导数相关
  private String sortKey;
  private String where;
  private int pageSize;
  private int fetchSize;
  
  public String getTemplate()
  {
    return template;
  }
  public void setTemplate(String template)
  {
    this.template = template;
  }
  public String getFile()
  {
    return file;
  }
  public void setFile(String file)
  {
    this.file = file;
  }
  public String getDelimiter()
  {
    return delimiter;
  }
  public void setDelimiter(String delimiter)
  {
    this.delimiter = delimiter;
  }
  public String getQuote()
  {
    return quote;
  }
  public void setQuote(String quote)
  {
    this.quote = quote;
  }
  public boolean isSkipable()
  {
    return skipable;
  }
  public void setSkipable(boolean skipable)
  {
    this.skipable = skipable;
  }
  public String getColumns()
  {
    return columns;
  }
  public void setColumns(String columns)
  {
    this.columns = columns;
  }
  public String getRegion()
  {
    return region;
  }
  public void setRegion(String region)
  {
    this.region = region;
  }
  public String getTable()
  {
    return table;
  }
  public void setTable(String table)
  {
    this.table = table;
  }
  public String getEncoding()
  {
    return encoding;
  }
  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
  }
  public int getLinesToSkip()
  {
    return linesToSkip;
  }
  public void setLinesToSkip(int linesToSkip)
  {
    this.linesToSkip = linesToSkip;
  }
  public String getName()
  {
	return name;
  }
  public void setName(String name)
  {
	this.name = name;
  }


public String getSortKey()
{
    return sortKey;
}


public void setSortKey(String sortKey)
{
    this.sortKey = sortKey;
}


public String getWhere()
{
    return where;
}


public void setWhere(String where)
{
    this.where = where;
}


public int getPageSize()
{
    return pageSize;
}


public void setPageSize(int pageSize)
{
    this.pageSize = pageSize;
}


public int getFetchSize()
{
    return fetchSize;
}


public void setFetchSize(int fetchSize)
{
    this.fetchSize = fetchSize;
}
}
