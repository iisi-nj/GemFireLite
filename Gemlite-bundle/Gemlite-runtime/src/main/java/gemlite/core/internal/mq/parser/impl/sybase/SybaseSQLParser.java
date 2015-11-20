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
package gemlite.core.internal.mq.parser.impl.sybase;

import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.mq.MqConstant;
import gemlite.core.internal.mq.domain.ParseredValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;

@SuppressWarnings({ "all", "warnings", "unchecked" })
public class SybaseSQLParser extends Parser
{
  public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND_SYM", "A_",
      "BIT_NUM", "B_", "COMMA", "C_", "DELETE_SYM", "DOT", "D_", "EQ_SYM", "EXPONENT", "E_", "FALSE_SYM", "FLOAT",
      "FROM", "F_", "G_", "HEX_DIGIT", "HEX_DIGIT_FRAGMENT", "H_", "ID", "INSERT", "INT", "INTO", "IS_SYM", "I_", "J_",
      "K_", "LPAREN", "L_", "M_", "NULL_SYM", "N_", "O_", "P_", "Q_", "RPAREN", "R_", "SET_SYM", "STRING", "S_",
      "TIMESTAMP", "TRI_COLON", "TRUE_SYM", "T_", "UPDATE", "U_", "VALUES", "VALUE_SYM", "V_", "WHERE", "WS", "W_",
      "X_", "Y_", "Z_" };
  
  public static final int EOF = -1;
  public static final int AND_SYM = 4;
  public static final int A_ = 5;
  public static final int BIT_NUM = 6;
  public static final int B_ = 7;
  public static final int COMMA = 8;
  public static final int C_ = 9;
  public static final int DELETE_SYM = 10;
  public static final int DOT = 11;
  public static final int D_ = 12;
  public static final int EQ_SYM = 13;
  public static final int EXPONENT = 14;
  public static final int E_ = 15;
  public static final int FALSE_SYM = 16;
  public static final int FLOAT = 17;
  public static final int FROM = 18;
  public static final int F_ = 19;
  public static final int G_ = 20;
  public static final int HEX_DIGIT = 21;
  public static final int HEX_DIGIT_FRAGMENT = 22;
  public static final int H_ = 23;
  public static final int ID = 24;
  public static final int INSERT = 25;
  public static final int INT = 26;
  public static final int INTO = 27;
  public static final int IS_SYM = 28;
  public static final int I_ = 29;
  public static final int J_ = 30;
  public static final int K_ = 31;
  public static final int LPAREN = 32;
  public static final int L_ = 33;
  public static final int M_ = 34;
  public static final int NULL_SYM = 35;
  public static final int N_ = 36;
  public static final int O_ = 37;
  public static final int P_ = 38;
  public static final int Q_ = 39;
  public static final int RPAREN = 40;
  public static final int R_ = 41;
  public static final int SET_SYM = 42;
  public static final int STRING = 43;
  public static final int S_ = 44;
  public static final int TIMESTAMP = 45;
  public static final int TRI_COLON = 46;
  public static final int TRUE_SYM = 47;
  public static final int T_ = 48;
  public static final int UPDATE = 49;
  public static final int U_ = 50;
  public static final int VALUES = 51;
  public static final int VALUE_SYM = 52;
  public static final int V_ = 53;
  public static final int WHERE = 54;
  public static final int WS = 55;
  public static final int W_ = 56;
  public static final int X_ = 57;
  public static final int Y_ = 58;
  public static final int Z_ = 59;
  
  // delegates
  public Parser[] getDelegates()
  {
    return new Parser[] {};
  }
  
  // delegators
  
  public SybaseSQLParser(TokenStream input)
  {
    this(input, new RecognizerSharedState());
  }
  
  public SybaseSQLParser(TokenStream input, RecognizerSharedState state)
  {
    super(input, state);
  }
  
  public String[] getTokenNames()
  {
    return SybaseSQLParser.tokenNames;
  }
  
  public String getGrammarFileName()
  {
    return "D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g";
  }
  
  // TODO:grammar fileåÊÕ„h
  public boolean isValidTableName(String tableName)
  {
	  String regionName = DomainRegistry.tableToRegion(tableName);
	  if(regionName == null || regionName.equals(""))
		  return false;
	  else
		  return true;
  }
  
  // $ANTLR start "parsed_value_list"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:113:
  // 1: parsed_value_list returns [List<ParseredValue> result] : package1=
  // parsed_one_package (package2= parsed_one_package )* ;
  public final List<ParseredValue> parsed_value_list() throws RecognitionException
  {
    List<ParseredValue> result = null;
    
    List<ParseredValue> package1 = null;
    
    List<ParseredValue> package2 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:114:2:
      // (package1= parsed_one_package (package2= parsed_one_package )* )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:114:4:
      // package1= parsed_one_package (package2= parsed_one_package )*
      {
        pushFollow(FOLLOW_parsed_one_package_in_parsed_value_list1040);
        package1 = parsed_one_package();
        
        state._fsp--;
        
        result = new ArrayList<ParseredValue>();
        result.addAll(package1);
        
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:119:2:
        // (package2= parsed_one_package )*
        loop1: do
        {
          int alt1 = 2;
          int LA1_0 = input.LA(1);
          
          if ((LA1_0 == TIMESTAMP))
          {
            alt1 = 1;
          }
          
          switch (alt1)
          {
            case 1:
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:120:4:
            // package2= parsed_one_package
            {
              pushFollow(FOLLOW_parsed_one_package_in_parsed_value_list1055);
              package2 = parsed_one_package();
              
              state._fsp--;
              
              result.addAll(package2);
              
            }
              break;
            
            default:
              break loop1;
          }
        }
        while (true);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "parsed_value_list"
  
  // $ANTLR start "parsed_one_package"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:127:1:
  // parsed_one_package returns [List<ParseredValue> result] : timestamp s1=
  // sql_statement (s2= sql_statement )* ;
  public final List<ParseredValue> parsed_one_package() throws RecognitionException
  {
    List<ParseredValue> result = null;
    
    ParseredValue s1 = null;
    
    ParseredValue s2 = null;
    
    long timestamp1 = 0;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:128:2: (
      // timestamp s1= sql_statement (s2= sql_statement )* )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:128:4:
      // timestamp s1= sql_statement (s2= sql_statement )*
      {
        pushFollow(FOLLOW_timestamp_in_parsed_one_package1080);
        timestamp1 = timestamp();
        
        state._fsp--;
        
        result = new ArrayList<ParseredValue>();
        
        pushFollow(FOLLOW_sql_statement_in_parsed_one_package1089);
        s1 = sql_statement();
        
        state._fsp--;
        
        if (s1 != null)
        {
          s1.setTimestamp(timestamp1);
          result.add(s1);
        }
        
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:140:2:
        // (s2= sql_statement )*
        loop2: do
        {
          int alt2 = 2;
          int LA2_0 = input.LA(1);
          
          if ((LA2_0 == DELETE_SYM || LA2_0 == INSERT || LA2_0 == UPDATE))
          {
            alt2 = 1;
          }
          
          switch (alt2)
          {
            case 1:
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:140:3:
            // s2= sql_statement
            {
              pushFollow(FOLLOW_sql_statement_in_parsed_one_package1098);
              s2 = sql_statement();
              
              state._fsp--;
              
              if (s2 != null)
              {
                s2.setTimestamp(timestamp1);
                result.add(s2);
              }
              
            }
              break;
            
            default:
              break loop2;
          }
        }
        while (true);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "parsed_one_package"
  
  // $ANTLR start "timestamp"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:150:1:
  // timestamp returns [long result] : TIMESTAMP TRI_COLON INT ;
  public final long timestamp() throws RecognitionException
  {
    long result = 0;
    
    Token INT2 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:151:2: (
      // TIMESTAMP TRI_COLON INT )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:151:4:
      // TIMESTAMP TRI_COLON INT
      {
        match(input, TIMESTAMP, FOLLOW_TIMESTAMP_in_timestamp1117);
        
        match(input, TRI_COLON, FOLLOW_TRI_COLON_in_timestamp1119);
        
        INT2 = (Token) match(input, INT, FOLLOW_INT_in_timestamp1121);
        
        result = Long.parseLong((INT2 != null ? INT2.getText() : null));
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "timestamp"
  
  // $ANTLR start "sql_statement"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:157:1:
  // sql_statement returns [ParseredValue pv] : ( insert_statement |
  // update_statement | delete_statement );
  public final ParseredValue sql_statement() throws RecognitionException
  {
    ParseredValue pv = null;
    
    ParseredValue insert_statement3 = null;
    
    ParseredValue update_statement4 = null;
    
    ParseredValue delete_statement5 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:158:2: (
      // insert_statement | update_statement | delete_statement )
      int alt3 = 3;
      switch (input.LA(1))
      {
        case INSERT:
        {
          alt3 = 1;
        }
          break;
        case UPDATE:
        {
          alt3 = 2;
        }
          break;
        case DELETE_SYM:
        {
          alt3 = 3;
        }
          break;
        default:
          NoViableAltException nvae = new NoViableAltException("", 3, 0, input);
          
          throw nvae;
          
      }
      
      switch (alt3)
      {
        case 1:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:158:4:
        // insert_statement
        {
          pushFollow(FOLLOW_insert_statement_in_sql_statement1139);
          insert_statement3 = insert_statement();
          
          state._fsp--;
          
          pv = insert_statement3;
          
        }
          break;
        case 2:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:159:4:
        // update_statement
        {
          pushFollow(FOLLOW_update_statement_in_sql_statement1146);
          update_statement4 = update_statement();
          
          state._fsp--;
          
          pv = update_statement4;
          
        }
          break;
        case 3:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:160:4:
        // delete_statement
        {
          pushFollow(FOLLOW_delete_statement_in_sql_statement1153);
          delete_statement5 = delete_statement();
          
          state._fsp--;
          
          pv = delete_statement5;
          
        }
          break;
      
      }
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return pv;
  }
  
  // $ANTLR end "sql_statement"
  
  // $ANTLR start "delete_statement"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:163:1:
  // delete_statement returns [ParseredValue pv] : DELETE_SYM FROM table_spec
  // where_clause ;
  public final ParseredValue delete_statement() throws RecognitionException
  {
    ParseredValue pv = null;
    
    String table_spec6 = null;
    
    HashMap where_clause7 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:164:2: (
      // DELETE_SYM FROM table_spec where_clause )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:165:2:
      // DELETE_SYM FROM table_spec where_clause
      {
        match(input, DELETE_SYM, FOLLOW_DELETE_SYM_in_delete_statement1170);
        
        match(input, FROM, FOLLOW_FROM_in_delete_statement1172);
        
        pushFollow(FOLLOW_table_spec_in_delete_statement1174);
        table_spec6 = table_spec();
        
        state._fsp--;
        
        pushFollow(FOLLOW_where_clause_in_delete_statement1177);
        where_clause7 = where_clause();
        
        state._fsp--;
        
        pv = new ParseredValue();
        pv.setOp(MqConstant.DELETE);
        String tableName = table_spec6;
        if (!isValidTableName(tableName))
          return null;
        pv.setTableName(tableName);
        
        pv.getValueMap().putAll(where_clause7);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return pv;
  }
  
  // $ANTLR end "delete_statement"
  
  // $ANTLR start "update_statement"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:179:1:
  // update_statement returns [ParseredValue pv] : UPDATE table_spec
  // set_columns_clause where_clause ;
  public final ParseredValue update_statement() throws RecognitionException
  {
    ParseredValue pv = null;
    
    String table_spec8 = null;
    
    HashMap set_columns_clause9 = null;
    
    HashMap where_clause10 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:180:2: (
      // UPDATE table_spec set_columns_clause where_clause )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:181:2:
      // UPDATE table_spec set_columns_clause where_clause
      {
        match(input, UPDATE, FOLLOW_UPDATE_in_update_statement1196);
        
        pushFollow(FOLLOW_table_spec_in_update_statement1198);
        table_spec8 = table_spec();
        
        state._fsp--;
        
        pushFollow(FOLLOW_set_columns_clause_in_update_statement1201);
        set_columns_clause9 = set_columns_clause();
        
        state._fsp--;
        
        pushFollow(FOLLOW_where_clause_in_update_statement1204);
        where_clause10 = where_clause();
        
        state._fsp--;
        
        pv = new ParseredValue();
        pv.setOp(MqConstant.UPDATE);
        String tableName = table_spec8;
        
        if (!isValidTableName(tableName))
          return null;
        pv.setTableName(tableName);
        pv.getUpdateMap().putAll(set_columns_clause9);
        pv.getValueMap().putAll(set_columns_clause9);
        
        pv.getWhereMap().putAll(where_clause10);
        for (Iterator it = where_clause10.keySet().iterator(); it.hasNext();)
        {
          String k = (String) it.next();
          String v = (String) where_clause10.get(k);
          if (!pv.getValueMap().containsKey(k))
            pv.getValueMap().put(k, v);
        }
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return pv;
  }
  
  // $ANTLR end "update_statement"
  
  // $ANTLR start "set_columns_clause"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:206:1:
  // set_columns_clause returns [HashMap result] : SET_SYM name=
  // set_column_clause ( COMMA name2= set_column_clause )* ;
  public final HashMap set_columns_clause() throws RecognitionException
  {
    HashMap result = null;
    
    SybaseSQLParser.set_column_clause_return name = null;
    
    SybaseSQLParser.set_column_clause_return name2 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:207:2: (
      // SET_SYM name= set_column_clause ( COMMA name2= set_column_clause )* )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:207:4:
      // SET_SYM name= set_column_clause ( COMMA name2= set_column_clause )*
      {
        match(input, SET_SYM, FOLLOW_SET_SYM_in_set_columns_clause1221);
        
        pushFollow(FOLLOW_set_column_clause_in_set_columns_clause1225);
        name = set_column_clause();
        
        state._fsp--;
        
        result = new HashMap();
        result.put((name != null ? name.key : null), (name != null ? name.value : null));
        
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:208:2: (
        // COMMA name2= set_column_clause )*
        loop4: do
        {
          int alt4 = 2;
          int LA4_0 = input.LA(1);
          
          if ((LA4_0 == COMMA))
          {
            alt4 = 1;
          }
          
          switch (alt4)
          {
            case 1:
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:208:4:
            // COMMA name2= set_column_clause
            {
              match(input, COMMA, FOLLOW_COMMA_in_set_columns_clause1232);
              
              pushFollow(FOLLOW_set_column_clause_in_set_columns_clause1238);
              name2 = set_column_clause();
              
              state._fsp--;
              
              result.put((name2 != null ? name2.key : null), (name2 != null ? name2.value : null));
              
            }
              break;
            
            default:
              break loop4;
          }
        }
        while (true);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "set_columns_clause"
  
  public static class set_column_clause_return extends ParserRuleReturnScope
  {
    public String key;
    public String value;
  };
  
  // $ANTLR start "set_column_clause"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:213:1:
  // set_column_clause returns [String key, String value] : ( ( column_name
  // EQ_SYM literal_value ) | ( column_name IS_SYM NULL_SYM ) );
  public final SybaseSQLParser.set_column_clause_return set_column_clause() throws RecognitionException
  {
    SybaseSQLParser.set_column_clause_return retval = new SybaseSQLParser.set_column_clause_return();
    retval.start = input.LT(1);
    
    String column_name11 = null;
    
    String literal_value12 = null;
    
    String column_name13 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:214:2: ( (
      // column_name EQ_SYM literal_value ) | ( column_name IS_SYM NULL_SYM ) )
      int alt5 = 2;
      int LA5_0 = input.LA(1);
      
      if ((LA5_0 == ID))
      {
        int LA5_1 = input.LA(2);
        
        if ((LA5_1 == EQ_SYM))
        {
          alt5 = 1;
        }
        else if ((LA5_1 == IS_SYM))
        {
          alt5 = 2;
        }
        else
        {
          NoViableAltException nvae = new NoViableAltException("", 5, 1, input);
          
          throw nvae;
          
        }
      }
      else
      {
        NoViableAltException nvae = new NoViableAltException("", 5, 0, input);
        
        throw nvae;
        
      }
      switch (alt5)
      {
        case 1:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:214:4: (
        // column_name EQ_SYM literal_value )
        {
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:214:4:
          // ( column_name EQ_SYM literal_value )
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:214:5:
          // column_name EQ_SYM literal_value
          {
            pushFollow(FOLLOW_column_name_in_set_column_clause1260);
            column_name11 = column_name();
            
            state._fsp--;
            
            match(input, EQ_SYM, FOLLOW_EQ_SYM_in_set_column_clause1262);
            
            pushFollow(FOLLOW_literal_value_in_set_column_clause1264);
            literal_value12 = literal_value();
            
            state._fsp--;
            
          }
          
          retval.key = column_name11;
          retval.value = literal_value12.trim().replaceAll("\'", "");
          
        }
          break;
        case 2:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:219:4: (
        // column_name IS_SYM NULL_SYM )
        {
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:219:4:
          // ( column_name IS_SYM NULL_SYM )
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:219:5:
          // column_name IS_SYM NULL_SYM
          {
            pushFollow(FOLLOW_column_name_in_set_column_clause1274);
            column_name13 = column_name();
            
            state._fsp--;
            
            match(input, IS_SYM, FOLLOW_IS_SYM_in_set_column_clause1276);
            
            match(input, NULL_SYM, FOLLOW_NULL_SYM_in_set_column_clause1278);
            
          }
          
          retval.key = column_name13;
          retval.value = "";
          
        }
          break;
      
      }
      retval.stop = input.LT(-1);
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return retval;
  }
  
  // $ANTLR end "set_column_clause"
  
  // $ANTLR start "where_clause"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:222:1:
  // where_clause returns [HashMap result] : WHERE name= set_column_clause (
  // AND_SYM name2= set_column_clause )* ;
  public final HashMap where_clause() throws RecognitionException
  {
    HashMap result = null;
    
    SybaseSQLParser.set_column_clause_return name = null;
    
    SybaseSQLParser.set_column_clause_return name2 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:223:2: (
      // WHERE name= set_column_clause ( AND_SYM name2= set_column_clause )* )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:224:2:
      // WHERE name= set_column_clause ( AND_SYM name2= set_column_clause )*
      {
        match(input, WHERE, FOLLOW_WHERE_in_where_clause1295);
        
        pushFollow(FOLLOW_set_column_clause_in_where_clause1299);
        name = set_column_clause();
        
        state._fsp--;
        
        result = new HashMap();
        result.put((name != null ? name.key : null), (name != null ? name.value : null));
        
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:225:2: (
        // AND_SYM name2= set_column_clause )*
        loop6: do
        {
          int alt6 = 2;
          int LA6_0 = input.LA(1);
          
          if ((LA6_0 == AND_SYM))
          {
            alt6 = 1;
          }
          
          switch (alt6)
          {
            case 1:
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:225:4:
            // AND_SYM name2= set_column_clause
            {
              match(input, AND_SYM, FOLLOW_AND_SYM_in_where_clause1306);
              
              pushFollow(FOLLOW_set_column_clause_in_where_clause1311);
              name2 = set_column_clause();
              
              state._fsp--;
              
              result.put((name2 != null ? name2.key : null), (name2 != null ? name2.value : null));
              
            }
              break;
            
            default:
              break loop6;
          }
        }
        while (true);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "where_clause"
  
  // $ANTLR start "insert_statement"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:230:1:
  // insert_statement returns [ParseredValue pv] : insert_header column_list
  // value_list_clause ;
  public final ParseredValue insert_statement() throws RecognitionException
  {
    ParseredValue pv = null;
    
    String insert_header14 = null;
    
    ArrayList column_list15 = null;
    
    ArrayList value_list_clause16 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:231:2: (
      // insert_header column_list value_list_clause )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:232:2:
      // insert_header column_list value_list_clause
      {
        pushFollow(FOLLOW_insert_header_in_insert_statement1333);
        insert_header14 = insert_header();
        
        state._fsp--;
        
        pushFollow(FOLLOW_column_list_in_insert_statement1336);
        column_list15 = column_list();
        
        state._fsp--;
        
        pushFollow(FOLLOW_value_list_clause_in_insert_statement1339);
        value_list_clause16 = value_list_clause();
        
        state._fsp--;
        
        pv = new ParseredValue();
        pv.setOp(MqConstant.INSERT);
        String tableName = insert_header14;
        
        if (!isValidTableName(tableName))
          return null;
        
        pv.setTableName(tableName);
        
        ArrayList keyArr = column_list15;
        ArrayList valueArr = value_list_clause16;
        
        for (int i = 0; i < keyArr.size(); i++)
        {
          String key = keyArr.get(i).toString().trim();
          String value = valueArr.get(i).toString().trim().replaceAll("\'", "");
          pv.getValueMap().put(key, value);
        }
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return pv;
  }
  
  // $ANTLR end "insert_statement"
  
  // $ANTLR start "insert_header"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:257:1:
  // insert_header returns [String result] : INSERT INTO table_spec ;
  public final String insert_header() throws RecognitionException
  {
    String result = null;
    
    String table_spec17 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:258:2: (
      // INSERT INTO table_spec )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:258:4:
      // INSERT INTO table_spec
      {
        match(input, INSERT, FOLLOW_INSERT_in_insert_header1356);
        
        match(input, INTO, FOLLOW_INTO_in_insert_header1358);
        
        pushFollow(FOLLOW_table_spec_in_insert_header1360);
        table_spec17 = table_spec();
        
        state._fsp--;
        
        result = table_spec17;
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "insert_header"
  
  // $ANTLR start "table_spec"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:264:1:
  // table_spec returns [String result] : ( schema_name DOT )? table_name ;
  public final String table_spec() throws RecognitionException
  {
    String result = null;
    
    String table_name18 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:265:2: ( (
      // schema_name DOT )? table_name )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:265:4: (
      // schema_name DOT )? table_name
      {
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:265:4: (
        // schema_name DOT )?
        int alt7 = 2;
        int LA7_0 = input.LA(1);
        
        if ((LA7_0 == ID))
        {
          int LA7_1 = input.LA(2);
          
          if ((LA7_1 == DOT))
          {
            alt7 = 1;
          }
        }
        switch (alt7)
        {
          case 1:
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:265:6:
          // schema_name DOT
          {
            pushFollow(FOLLOW_schema_name_in_table_spec1379);
            schema_name();
            
            state._fsp--;
            
            match(input, DOT, FOLLOW_DOT_in_table_spec1381);
            
          }
            break;
        
        }
        
        pushFollow(FOLLOW_table_name_in_table_spec1386);
        table_name18 = table_name();
        
        state._fsp--;
        
        result = table_name18;
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "table_spec"
  
  // $ANTLR start "column_list"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:271:1:
  // column_list returns [ArrayList result] : LPAREN name= column_name ( COMMA
  // name2= column_name )* RPAREN ;
  public final ArrayList column_list() throws RecognitionException
  {
    ArrayList result = null;
    
    String name = null;
    
    String name2 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:272:2: (
      // LPAREN name= column_name ( COMMA name2= column_name )* RPAREN )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:273:2:
      // LPAREN name= column_name ( COMMA name2= column_name )* RPAREN
      {
        match(input, LPAREN, FOLLOW_LPAREN_in_column_list1404);
        
        pushFollow(FOLLOW_column_name_in_column_list1411);
        name = column_name();
        
        state._fsp--;
        
        result = new ArrayList();
        result.add(name);
        
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:275:4: (
        // COMMA name2= column_name )*
        loop8: do
        {
          int alt8 = 2;
          int LA8_0 = input.LA(1);
          
          if ((LA8_0 == COMMA))
          {
            alt8 = 1;
          }
          
          switch (alt8)
          {
            case 1:
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:275:5:
            // COMMA name2= column_name
            {
              match(input, COMMA, FOLLOW_COMMA_in_column_list1419);
              
              pushFollow(FOLLOW_column_name_in_column_list1423);
              name2 = column_name();
              
              state._fsp--;
              
              result.add(name2);
              
            }
              break;
            
            default:
              break loop8;
          }
        }
        while (true);
        
        match(input, RPAREN, FOLLOW_RPAREN_in_column_list1430);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "column_list"
  
  // $ANTLR start "value_list_clause"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:279:1:
  // value_list_clause returns [ArrayList result] : ( VALUES | VALUE_SYM )
  // column_value_list ;
  public final ArrayList value_list_clause() throws RecognitionException
  {
    ArrayList result = null;
    
    ArrayList column_value_list19 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:279:45: (
      // ( VALUES | VALUE_SYM ) column_value_list )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:280:2: (
      // VALUES | VALUE_SYM ) column_value_list
      {
        if ((input.LA(1) >= VALUES && input.LA(1) <= VALUE_SYM))
        {
          input.consume();
          state.errorRecovery = false;
        }
        else
        {
          MismatchedSetException mse = new MismatchedSetException(null, input);
          throw mse;
        }
        
        pushFollow(FOLLOW_column_value_list_in_value_list_clause1451);
        column_value_list19 = column_value_list();
        
        state._fsp--;
        
        result = column_value_list19;
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "value_list_clause"
  
  // $ANTLR start "column_value_list"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:286:1:
  // column_value_list returns [ArrayList result] : LPAREN value= literal_value
  // ( COMMA value2= literal_value )* RPAREN ;
  public final ArrayList column_value_list() throws RecognitionException
  {
    ArrayList result = null;
    
    String value = null;
    
    String value2 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:287:2: (
      // LPAREN value= literal_value ( COMMA value2= literal_value )* RPAREN )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:288:2:
      // LPAREN value= literal_value ( COMMA value2= literal_value )* RPAREN
      {
        match(input, LPAREN, FOLLOW_LPAREN_in_column_value_list1469);
        
        pushFollow(FOLLOW_literal_value_in_column_value_list1475);
        value = literal_value();
        
        state._fsp--;
        
        result = new ArrayList();
        result.add(value);
        
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:290:3: (
        // COMMA value2= literal_value )*
        loop9: do
        {
          int alt9 = 2;
          int LA9_0 = input.LA(1);
          
          if ((LA9_0 == COMMA))
          {
            alt9 = 1;
          }
          
          switch (alt9)
          {
            case 1:
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:290:4:
            // COMMA value2= literal_value
            {
              match(input, COMMA, FOLLOW_COMMA_in_column_value_list1482);
              
              pushFollow(FOLLOW_literal_value_in_column_value_list1486);
              value2 = literal_value();
              
              state._fsp--;
              
              result.add(value2);
              
            }
              break;
            
            default:
              break loop9;
          }
        }
        while (true);
        
        match(input, RPAREN, FOLLOW_RPAREN_in_column_value_list1494);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "column_value_list"
  
  // $ANTLR start "literal_value"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:294:1:
  // literal_value returns [String result] : ( STRING | number_literal |
  // HEX_DIGIT | boolean_literal | BIT_NUM | NULL_SYM ) ;
  public final String literal_value() throws RecognitionException
  {
    String result = null;
    
    Token STRING20 = null;
    Token HEX_DIGIT22 = null;
    Token BIT_NUM24 = null;
    String number_literal21 = null;
    
    String boolean_literal23 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:295:2: ( (
      // STRING | number_literal | HEX_DIGIT | boolean_literal | BIT_NUM |
      // NULL_SYM ) )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:296:9: (
      // STRING | number_literal | HEX_DIGIT | boolean_literal | BIT_NUM |
      // NULL_SYM )
      {
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:296:9: (
        // STRING | number_literal | HEX_DIGIT | boolean_literal | BIT_NUM |
        // NULL_SYM )
        int alt10 = 6;
        switch (input.LA(1))
        {
          case STRING:
          {
            alt10 = 1;
          }
            break;
          case FLOAT:
          case INT:
          {
            alt10 = 2;
          }
            break;
          case HEX_DIGIT:
          {
            alt10 = 3;
          }
            break;
          case FALSE_SYM:
          case TRUE_SYM:
          {
            alt10 = 4;
          }
            break;
          case BIT_NUM:
          {
            alt10 = 5;
          }
            break;
          case NULL_SYM:
          {
            alt10 = 6;
          }
            break;
          default:
            NoViableAltException nvae = new NoViableAltException("", 10, 0, input);
            
            throw nvae;
            
        }
        
        switch (alt10)
        {
          case 1:
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:296:11:
          // STRING
          {
            STRING20 = (Token) match(input, STRING, FOLLOW_STRING_in_literal_value1518);
            
            result = (STRING20 != null ? STRING20.getText() : null);
            
          }
            break;
          case 2:
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:297:12:
          // number_literal
          {
            pushFollow(FOLLOW_number_literal_in_literal_value1533);
            number_literal21 = number_literal();
            
            state._fsp--;
            
            result = number_literal21;
            
          }
            break;
          case 3:
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:298:12:
          // HEX_DIGIT
          {
            HEX_DIGIT22 = (Token) match(input, HEX_DIGIT, FOLLOW_HEX_DIGIT_in_literal_value1548);
            
            result = (HEX_DIGIT22 != null ? HEX_DIGIT22.getText() : null);
            
          }
            break;
          case 4:
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:299:12:
          // boolean_literal
          {
            pushFollow(FOLLOW_boolean_literal_in_literal_value1563);
            boolean_literal23 = boolean_literal();
            
            state._fsp--;
            
            result = boolean_literal23;
            
          }
            break;
          case 5:
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:300:12:
          // BIT_NUM
          {
            BIT_NUM24 = (Token) match(input, BIT_NUM, FOLLOW_BIT_NUM_in_literal_value1578);
            
            result = (BIT_NUM24 != null ? BIT_NUM24.getText() : null);
            
          }
            break;
          case 6:
          // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:301:12:
          // NULL_SYM
          {
            match(input, NULL_SYM, FOLLOW_NULL_SYM_in_literal_value1593);
            
            result = "";
            
          }
            break;
        
        }
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "literal_value"
  
  // $ANTLR start "number_literal"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:305:1:
  // number_literal returns [String result] : ( INT | FLOAT );
  public final String number_literal() throws RecognitionException
  {
    String result = null;
    
    Token INT25 = null;
    Token FLOAT26 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:306:2: (
      // INT | FLOAT )
      int alt11 = 2;
      int LA11_0 = input.LA(1);
      
      if ((LA11_0 == INT))
      {
        alt11 = 1;
      }
      else if ((LA11_0 == FLOAT))
      {
        alt11 = 2;
      }
      else
      {
        NoViableAltException nvae = new NoViableAltException("", 11, 0, input);
        
        throw nvae;
        
      }
      switch (alt11)
      {
        case 1:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:306:4:
        // INT
        {
          INT25 = (Token) match(input, INT, FOLLOW_INT_in_number_literal1619);
          
          result = (INT25 != null ? INT25.getText() : null);
          
        }
          break;
        case 2:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:307:4:
        // FLOAT
        {
          FLOAT26 = (Token) match(input, FLOAT, FOLLOW_FLOAT_in_number_literal1626);
          
          result = (FLOAT26 != null ? FLOAT26.getText() : null);
          
        }
          break;
      
      }
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "number_literal"
  
  // $ANTLR start "boolean_literal"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:310:1:
  // boolean_literal returns [String result] : ( TRUE_SYM | FALSE_SYM );
  public final String boolean_literal() throws RecognitionException
  {
    String result = null;
    
    Token TRUE_SYM27 = null;
    Token FALSE_SYM28 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:311:2: (
      // TRUE_SYM | FALSE_SYM )
      int alt12 = 2;
      int LA12_0 = input.LA(1);
      
      if ((LA12_0 == TRUE_SYM))
      {
        alt12 = 1;
      }
      else if ((LA12_0 == FALSE_SYM))
      {
        alt12 = 2;
      }
      else
      {
        NoViableAltException nvae = new NoViableAltException("", 12, 0, input);
        
        throw nvae;
        
      }
      switch (alt12)
      {
        case 1:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:311:4:
        // TRUE_SYM
        {
          TRUE_SYM27 = (Token) match(input, TRUE_SYM, FOLLOW_TRUE_SYM_in_boolean_literal1642);
          
          result = (TRUE_SYM27 != null ? TRUE_SYM27.getText() : null);
          
        }
          break;
        case 2:
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:312:4:
        // FALSE_SYM
        {
          FALSE_SYM28 = (Token) match(input, FALSE_SYM, FOLLOW_FALSE_SYM_in_boolean_literal1649);
          
          result = (FALSE_SYM28 != null ? FALSE_SYM28.getText() : null);
          
        }
          break;
      
      }
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "boolean_literal"
  
  // $ANTLR start "schema_name"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:315:1:
  // schema_name returns [String result] : ID ;
  public final String schema_name() throws RecognitionException
  {
    String result = null;
    
    Token ID29 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:316:3: (
      // ID )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:316:5: ID
      {
        ID29 = (Token) match(input, ID, FOLLOW_ID_in_schema_name1666);
        
        result = (ID29 != null ? ID29.getText() : null);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "schema_name"
  
  // $ANTLR start "table_name"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:322:1:
  // table_name returns [String result] : ID ;
  public final String table_name() throws RecognitionException
  {
    String result = null;
    
    Token ID30 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:323:2: (
      // ID )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:323:4: ID
      {
        ID30 = (Token) match(input, ID, FOLLOW_ID_in_table_name1686);
        
        result = (ID30 != null ? ID30.getText() : null);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "table_name"
  
  // $ANTLR start "column_name"
  // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:329:1:
  // column_name returns [String result] : ID ;
  public final String column_name() throws RecognitionException
  {
    String result = null;
    
    Token ID31 = null;
    
    try
    {
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:330:2: (
      // ID )
      // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:330:4: ID
      {
        ID31 = (Token) match(input, ID, FOLLOW_ID_in_column_name1703);
        
        result = (ID31 != null ? ID31.getText() : null);
        
      }
      
    }
    catch (RecognitionException re)
    {
      reportError(re);
      recover(input, re);
    }
    
    finally
    {
      // do for sure before leaving
    }
    return result;
  }
  
  // $ANTLR end "column_name"
  
  // Delegated rules
  
  public static final BitSet FOLLOW_parsed_one_package_in_parsed_value_list1040 = new BitSet(
      new long[] { 0x0000200000000002L });
  public static final BitSet FOLLOW_parsed_one_package_in_parsed_value_list1055 = new BitSet(
      new long[] { 0x0000200000000002L });
  public static final BitSet FOLLOW_timestamp_in_parsed_one_package1080 = new BitSet(new long[] { 0x0002000002000400L });
  public static final BitSet FOLLOW_sql_statement_in_parsed_one_package1089 = new BitSet(
      new long[] { 0x0002000002000402L });
  public static final BitSet FOLLOW_sql_statement_in_parsed_one_package1098 = new BitSet(
      new long[] { 0x0002000002000402L });
  public static final BitSet FOLLOW_TIMESTAMP_in_timestamp1117 = new BitSet(new long[] { 0x0000400000000000L });
  public static final BitSet FOLLOW_TRI_COLON_in_timestamp1119 = new BitSet(new long[] { 0x0000000004000000L });
  public static final BitSet FOLLOW_INT_in_timestamp1121 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_insert_statement_in_sql_statement1139 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_update_statement_in_sql_statement1146 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_delete_statement_in_sql_statement1153 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_DELETE_SYM_in_delete_statement1170 = new BitSet(new long[] { 0x0000000000040000L });
  public static final BitSet FOLLOW_FROM_in_delete_statement1172 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_table_spec_in_delete_statement1174 = new BitSet(new long[] { 0x0040000000000000L });
  public static final BitSet FOLLOW_where_clause_in_delete_statement1177 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_UPDATE_in_update_statement1196 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_table_spec_in_update_statement1198 = new BitSet(new long[] { 0x0000040000000000L });
  public static final BitSet FOLLOW_set_columns_clause_in_update_statement1201 = new BitSet(
      new long[] { 0x0040000000000000L });
  public static final BitSet FOLLOW_where_clause_in_update_statement1204 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_SET_SYM_in_set_columns_clause1221 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_set_column_clause_in_set_columns_clause1225 = new BitSet(
      new long[] { 0x0000000000000102L });
  public static final BitSet FOLLOW_COMMA_in_set_columns_clause1232 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_set_column_clause_in_set_columns_clause1238 = new BitSet(
      new long[] { 0x0000000000000102L });
  public static final BitSet FOLLOW_column_name_in_set_column_clause1260 = new BitSet(
      new long[] { 0x0000000000002000L });
  public static final BitSet FOLLOW_EQ_SYM_in_set_column_clause1262 = new BitSet(new long[] { 0x0000880804230040L });
  public static final BitSet FOLLOW_literal_value_in_set_column_clause1264 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_column_name_in_set_column_clause1274 = new BitSet(
      new long[] { 0x0000000010000000L });
  public static final BitSet FOLLOW_IS_SYM_in_set_column_clause1276 = new BitSet(new long[] { 0x0000000800000000L });
  public static final BitSet FOLLOW_NULL_SYM_in_set_column_clause1278 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_WHERE_in_where_clause1295 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_set_column_clause_in_where_clause1299 = new BitSet(
      new long[] { 0x0000000000000012L });
  public static final BitSet FOLLOW_AND_SYM_in_where_clause1306 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_set_column_clause_in_where_clause1311 = new BitSet(
      new long[] { 0x0000000000000012L });
  public static final BitSet FOLLOW_insert_header_in_insert_statement1333 = new BitSet(
      new long[] { 0x0000000100000000L });
  public static final BitSet FOLLOW_column_list_in_insert_statement1336 = new BitSet(new long[] { 0x0018000000000000L });
  public static final BitSet FOLLOW_value_list_clause_in_insert_statement1339 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_INSERT_in_insert_header1356 = new BitSet(new long[] { 0x0000000008000000L });
  public static final BitSet FOLLOW_INTO_in_insert_header1358 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_table_spec_in_insert_header1360 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_schema_name_in_table_spec1379 = new BitSet(new long[] { 0x0000000000000800L });
  public static final BitSet FOLLOW_DOT_in_table_spec1381 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_table_name_in_table_spec1386 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_LPAREN_in_column_list1404 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_column_name_in_column_list1411 = new BitSet(new long[] { 0x0000010000000100L });
  public static final BitSet FOLLOW_COMMA_in_column_list1419 = new BitSet(new long[] { 0x0000000001000000L });
  public static final BitSet FOLLOW_column_name_in_column_list1423 = new BitSet(new long[] { 0x0000010000000100L });
  public static final BitSet FOLLOW_RPAREN_in_column_list1430 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_set_in_value_list_clause1443 = new BitSet(new long[] { 0x0000000100000000L });
  public static final BitSet FOLLOW_column_value_list_in_value_list_clause1451 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_LPAREN_in_column_value_list1469 = new BitSet(new long[] { 0x0000880804230040L });
  public static final BitSet FOLLOW_literal_value_in_column_value_list1475 = new BitSet(
      new long[] { 0x0000010000000100L });
  public static final BitSet FOLLOW_COMMA_in_column_value_list1482 = new BitSet(new long[] { 0x0000880804230040L });
  public static final BitSet FOLLOW_literal_value_in_column_value_list1486 = new BitSet(
      new long[] { 0x0000010000000100L });
  public static final BitSet FOLLOW_RPAREN_in_column_value_list1494 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_STRING_in_literal_value1518 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_number_literal_in_literal_value1533 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_HEX_DIGIT_in_literal_value1548 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_boolean_literal_in_literal_value1563 = new BitSet(
      new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_BIT_NUM_in_literal_value1578 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_NULL_SYM_in_literal_value1593 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_INT_in_number_literal1619 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_FLOAT_in_number_literal1626 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_TRUE_SYM_in_boolean_literal1642 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_FALSE_SYM_in_boolean_literal1649 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_ID_in_schema_name1666 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_ID_in_table_name1686 = new BitSet(new long[] { 0x0000000000000002L });
  public static final BitSet FOLLOW_ID_in_column_name1703 = new BitSet(new long[] { 0x0000000000000002L });
  
}