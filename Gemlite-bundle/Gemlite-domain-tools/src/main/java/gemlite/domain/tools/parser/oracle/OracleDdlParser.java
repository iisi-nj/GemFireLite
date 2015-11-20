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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleCreateTableParser;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;

import gemlite.domain.tools.context.Column;
import gemlite.domain.tools.context.Table;
import gemlite.domain.tools.parser.IDdlParser;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class OracleDdlParser implements IDdlParser
{
  @Override
  public Map parseDdls(File file)
  {
    Map<Table, List<Column>> result = new HashMap<Table, List<Column>>();
    List<String> ddls = read(file);
    for (String ddl : ddls)
    {
      Map<Table, List<Column>> m = parseOneDdl(ddl);
      if (m != null && m.size() > 0)
        result.putAll(m);
    }
    return result;
  }
  
  private Map<Table, List<Column>> parseOneDdl(String ddl)
  {
    Map<Table, List<Column>> map = new HashMap<Table, List<Column>>();
    Table table = new Table();
    List<Column> columnList = new ArrayList<Column>();
    Map<String, Column> columnMap = new HashMap<String, Column>();
    OracleCreateTableParser parser = new OracleCreateTableParser(ddl);
    OracleCreateTableStatement statement = parser.parseCrateTable(true);
    String tableName = statement.getName().getSimleName().toLowerCase();
    table.setTableName(tableName);
    table.setRegionName(tableName);
    List<SQLTableElement> tableElementList = statement.getTableElementList();
    for (SQLTableElement element : tableElementList)
    {
      if (element instanceof SQLColumnDefinition)
      {
        SQLColumnDefinition def = (SQLColumnDefinition) element;
        Column column = new Column();
        column.setName(def.getName().toString().toLowerCase());
        column.setType(def.getDataType().getName().toLowerCase());
        column.setArguments(def.getDataType().getArguments());
        column.setComment(def.getComment());
        column.setJavaType(Oracle2JavaType.ORACLE_TO_JAVA.get(column.getType()) == null ? "UNKNOWN"
            : Oracle2JavaType.ORACLE_TO_JAVA.get(column.getType()));
        if ("number".equals(column.getType())
            && (null == column.getArguments() || column.getArguments().toString().length() == 0))
          column.setJavaType("int");
        column.setPrimitive(isPrimitive(column.getJavaType()));
        columnList.add(column);
        columnMap.put(column.getName(), column);
      }
      else if (element instanceof OraclePrimaryKey)
      {
        OraclePrimaryKey pk = (OraclePrimaryKey) element;
        List<SQLExpr> columns = pk.getColumns();
        List<Column> dbKey = new ArrayList<Column>();
        if (columns.size() == 1)
          table.setSingle_filed_pk(true);
        else if (columns.size() > 1)
          table.setHas_key_class(true);
        for (SQLExpr expr : columns)
        {
          SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) expr;
          Column column = columnMap.get(identifierExpr.getLowerName());
          column.setPrimary(true);
          dbKey.add(column);
        }
        table.setDbKey(dbKey);
      }
    }
    
    List<String> idxList = (List<String>) idxMap.get(tableName);
    if (idxList != null && idxList.size() > 0)
    {
      List<Column> dbKey = new ArrayList<Column>();
      if (idxList.size() == 1)
        table.setSingle_filed_pk(true);
      else if (idxList.size() > 1)
        table.setHas_key_class(true);
      for (String idx : idxList)
      {
        Column column = columnMap.get(idx);
        column.setPrimary(true);
        dbKey.add(column);
      }
      table.setDbKey(dbKey);
    }
    map.put(table, columnList);
    return map;
  }
  
  private List<String> read(File file)
  {
    List<String> ddls = new ArrayList<String>();
    List<String> subs = new ArrayList<String>();
    if (file.isDirectory())
    {
      String[] fileList = file.list();
      for (int i = 0; i < fileList.length; i++)
      {
        File tmp = new File(file.getAbsolutePath() + "\\" + fileList[i]);
        subs = processOneFile(tmp);
        if (subs.size() > 0)
          ddls.addAll(subs);
      }
    }
    else
    {
      subs = processOneFile(file);
      if (subs.size() > 0)
        ddls.addAll(subs);
    }
    return ddls;
  }
  
  private List<String> processOneFile(File file)
  {
    List<String> ddls = new ArrayList<String>();
    StringBuffer ddl = new StringBuffer();
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new FileReader(file));
      String temp = null;
      while ((temp = reader.readLine()) != null)
      {
        ddl.append(temp);
        if (temp.contains(";"))
        {
          ddls.add(ddl.toString());
          ddl = new StringBuffer();
        }
      }
      if (ddl.length() > 0)
        ddls.add(ddl.toString());
      reader.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return ddls;
  }
  
  private boolean isPrimitive(String typeName)
  {
    return PRIMITIVES.containsKey(typeName);
  }
  
  private static final Map PRIMITIVES = new HashMap();
  static
  {
    PRIMITIVES.put("char", "Character");
    PRIMITIVES.put("byte", "Byte");
    PRIMITIVES.put("short", "Short");
    PRIMITIVES.put("int", "Integer");
    PRIMITIVES.put("long", "Long");
    PRIMITIVES.put("boolean", "Boolean");
    PRIMITIVES.put("float", "Float");
    PRIMITIVES.put("double", "Double");
  }
  
  private Map idxMap = new HashMap();
  
  @Override
  public Map parseIdxs(File file)
  {
    Map result = new HashMap();
    List<String> idxs = read(file);
    for (String idx : idxs)
    {
      Map<Table, List<Column>> m = parseOneIdx(idx);
      if (m != null && m.size() > 0)
        result.putAll(m);
    }
    idxMap.putAll(result);
    return result;
  }
  
  private Map parseOneIdx(String idx)
  {
    Map map = new HashMap();
    OracleStatementParser parser = new OracleStatementParser(idx);
    OracleCreateIndexStatement statement = parser.parseCreateIndex(true);
    SQLExprTableSource tableSource = (SQLExprTableSource) statement.getTable();
    String tableName = ((SQLPropertyExpr) tableSource.getExpr()).getSimleName().toLowerCase();
    String idxType = statement.getType();
    List<SQLSelectOrderByItem> items = statement.getItems();
    List fields = new ArrayList();
    for (SQLSelectOrderByItem item : items)
    {
      SQLIdentifierExpr expr = (SQLIdentifierExpr) item.getExpr();
      fields.add(expr.getSimleName().toLowerCase());
    }
    if ("unique".equalsIgnoreCase(idxType))
      map.put(tableName, fields);
    return map;
  }
}
