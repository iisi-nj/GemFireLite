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

import gemlite.core.internal.mq.domain.ParseredValue;
import gemlite.core.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class AntlrTableParser implements ITableParser
{
  @Override
  public List<ParseredValue> doBatchParser(String sqlList)
  {
    List<ParseredValue> reList = new ArrayList<ParseredValue>();
    sqlList = sqlList.trim();
    try
    {
      ISQLParserDelegator parser = SqlParserFactory.createSqlParser(sqlList);
      reList = parser.parsed_value_list();
    }
    catch (Exception e)
    {
      LogUtil.getMqSyncLog().error("Parse SQL Exception with sql:" + sqlList, e);
    }
    
    return reList;
  }
  
  @Override
  public ParseredValue doParser(String sql)
  {
    ParseredValue pv = null;
    sql = sql.trim();
    try
    {
      ISQLParserDelegator parser = SqlParserFactory.createSqlParser(sql);
      pv = parser.sql_statement();
    }
    catch (Exception e)
    {
      LogUtil.getMqSyncLog().error("Parse SQL Exception with sql:" + sql, e);
    }
    
    return pv;
  }
  
  public static void main(String[] args)
  {
    String sqlList = "update tjsi_web.ac01 set aab001 = '10690499', aab060 = null, aac002 = 'can0qe520695', aac003 = 'o''connor timothy mic', aac004 = '1', aac005 = '01', aac006 = to_date('1961-11-02 00:00:00', 'yyyy-mm-dd hh24:mi:ss'), aac007 = to_date('2013-06-01 00:00:00', 'yyyy-mm-dd hh24:mi:ss'), cac100 = null, aac008 = '1', aac009 = '1', aac010 = null, aac011 = null, aac012 = null, aac013 = '1', aac014 = null, aac015 = null, aae005 = null, aac019 = null, aac020 = null, cac206 = null, ckc015 = null, ckc301 = null, aic001 = null, ajc001 = null, aae008 = null, aae010 = null, aae006 = null, aae007 = null, aae011 = '邓志扬', aae036 = to_date('2013-12-05 00:00:00', 'yyyy-mm-dd hh24:mi:ss'), aae013 = null, cac001 = null, cac303 = '0', cab001 = null, cac386 = '002506', cab004 = '0191', cac387 = '0' where aac001 = '1206765188' and aab001 = '10650678' and aab060 is null and aac002 = 'can0qe520695' and aac003 = 'o''connor timothy mic' and aac004 = '1' and aac005 = '01' and aac006 = to_date('1961-11-02 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and aac007 = to_date('2013-06-01 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and cac100 is null and aac008 = '1' and aac009 = '1' and aac010 is null and aac011 is null and aac012 is null and aac013 = '1' and aac014 is null and aac015 is null and aae005 is null and aac019 is null and aac020 is null and cac206 is null and ckc015 is null and ckc301 is null and aic001 is null and ajc001 is null and aae008 is null and aae010 is null and aae006 is null and aae007 is null and aae011 = '赵静秋' and aae036 = to_date('2013-05-29 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and aae013 is null and cac001 is null and cac303 = '0' and cab001 is null and cac386 = '002445' and cab004 = '0192' and cac387 = '0' and cab027 is null and cac110 is null and cac111 is null and cac018 is null and cac019 is null and ajc002 is null and cac112 is null and aae123 is null and cac305 is null and aic002 is null and cic200 is null and aae145 is null and cic100 is null and aac033 is null and aac034 is null and bac101 is null and aac021 is null and ckc451 is null and cic405 is null and cic406 is null and cac306 is null and cac307 is null and aae028 is null and aac035 is null and ffzs_bs is null";
    AntlrTableParser parser = new AntlrTableParser();
    List<ParseredValue> list = parser.doBatchParser(sqlList);
    for (int i = 0; i < list.size(); i++)
      System.out.println(list.get(i));
  }
}
