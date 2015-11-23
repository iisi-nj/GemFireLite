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
package gemlite.core.internal.index.antlr;
import gemlite.core.api.index.IndexManager;
import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.support.context.DomainMapperHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;
@SuppressWarnings({"all", "warnings", "unchecked"})
public class CreateIndexParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASC", "A_", "BY", "B_", "COLON", "COMMA", "CREATE", "C_", "DESC", "DOT", "D_", "E_", "FROM", "F_", "G_", "HASHINDEX", "H_", "ID", "INDEX", "INT", "I_", "J_", "K_", "LPAREN", "L_", "M_", "N_", "ON", "ORDER", "O_", "P_", "Q_", "RANGEINDEX", "RPAREN", "R_", "S_", "TO", "T_", "USING", "U_", "V_", "WS", "W_", "X_", "Y_", "Z_"
    };

    public static final int EOF=-1;
    public static final int ASC=4;
    public static final int A_=5;
    public static final int BY=6;
    public static final int B_=7;
    public static final int COLON=8;
    public static final int COMMA=9;
    public static final int CREATE=10;
    public static final int C_=11;
    public static final int DESC=12;
    public static final int DOT=13;
    public static final int D_=14;
    public static final int E_=15;
    public static final int FROM=16;
    public static final int F_=17;
    public static final int G_=18;
    public static final int HASHINDEX=19;
    public static final int H_=20;
    public static final int ID=21;
    public static final int INDEX=22;
    public static final int INT=23;
    public static final int I_=24;
    public static final int J_=25;
    public static final int K_=26;
    public static final int LPAREN=27;
    public static final int L_=28;
    public static final int M_=29;
    public static final int N_=30;
    public static final int ON=31;
    public static final int ORDER=32;
    public static final int O_=33;
    public static final int P_=34;
    public static final int Q_=35;
    public static final int RANGEINDEX=36;
    public static final int RPAREN=37;
    public static final int R_=38;
    public static final int S_=39;
    public static final int TO=40;
    public static final int T_=41;
    public static final int USING=42;
    public static final int U_=43;
    public static final int V_=44;
    public static final int WS=45;
    public static final int W_=46;
    public static final int X_=47;
    public static final int Y_=48;
    public static final int Z_=49;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public CreateIndexParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public CreateIndexParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected StringTemplateGroup templateLib =
  new StringTemplateGroup("CreateIndexParserTemplates", AngleBracketTemplateLexer.class);

public void setTemplateLib(StringTemplateGroup templateLib) {
  this.templateLib = templateLib;
}
public StringTemplateGroup getTemplateLib() {
  return templateLib;
}
/** allows convenient multi-value initialization:
 *  "new STAttrMap().put(...).put(...)"
 */
public static class STAttrMap extends HashMap {
  public STAttrMap put(String attrName, Object value) {
    super.put(attrName, value);
    return this;
  }
  public STAttrMap put(String attrName, int value) {
    super.put(attrName, new Integer(value));
    return this;
  }
}
    public String[] getTokenNames() { return CreateIndexParser.tokenNames; }
    public String getGrammarFileName() { return "D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g"; }


    public String getPackageName()
    {
    	return IndexManager.PACKAGE_NAME;
    }


    public static class keyword_return extends ParserRuleReturnScope {
        public String value;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "keyword"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:89:1: keyword returns [String value] : ( CREATE | INDEX | ON | USING | ORDER | BY | ASC | DESC );
    public final CreateIndexParser.keyword_return keyword() throws RecognitionException {
        CreateIndexParser.keyword_return retval = new CreateIndexParser.keyword_return();
        retval.start = input.LT(1);


        Token CREATE1=null;
        Token INDEX2=null;
        Token ON3=null;
        Token USING4=null;
        Token ORDER5=null;
        Token BY6=null;
        Token ASC7=null;
        Token DESC8=null;

        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:90:2: ( CREATE | INDEX | ON | USING | ORDER | BY | ASC | DESC )
            int alt1=8;
            switch ( input.LA(1) ) {
            case CREATE:
                {
                alt1=1;
                }
                break;
            case INDEX:
                {
                alt1=2;
                }
                break;
            case ON:
                {
                alt1=3;
                }
                break;
            case USING:
                {
                alt1=4;
                }
                break;
            case ORDER:
                {
                alt1=5;
                }
                break;
            case BY:
                {
                alt1=6;
                }
                break;
            case ASC:
                {
                alt1=7;
                }
                break;
            case DESC:
                {
                alt1=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }

            switch (alt1) {
                case 1 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:90:4: CREATE
                    {
                    CREATE1=(Token)match(input,CREATE,FOLLOW_CREATE_in_keyword724); 

                    retval.value = (CREATE1!=null?CREATE1.getText():null);

                    }
                    break;
                case 2 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:91:3: INDEX
                    {
                    INDEX2=(Token)match(input,INDEX,FOLLOW_INDEX_in_keyword730); 

                    retval.value = (INDEX2!=null?INDEX2.getText():null);

                    }
                    break;
                case 3 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:92:3: ON
                    {
                    ON3=(Token)match(input,ON,FOLLOW_ON_in_keyword736); 

                    retval.value = (ON3!=null?ON3.getText():null);

                    }
                    break;
                case 4 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:93:3: USING
                    {
                    USING4=(Token)match(input,USING,FOLLOW_USING_in_keyword742); 

                    retval.value = (USING4!=null?USING4.getText():null);

                    }
                    break;
                case 5 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:94:3: ORDER
                    {
                    ORDER5=(Token)match(input,ORDER,FOLLOW_ORDER_in_keyword748); 

                    retval.value = (ORDER5!=null?ORDER5.getText():null);

                    }
                    break;
                case 6 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:95:3: BY
                    {
                    BY6=(Token)match(input,BY,FOLLOW_BY_in_keyword754); 

                    retval.value = (BY6!=null?BY6.getText():null);

                    }
                    break;
                case 7 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:96:3: ASC
                    {
                    ASC7=(Token)match(input,ASC,FOLLOW_ASC_in_keyword760); 

                    retval.value = (ASC7!=null?ASC7.getText():null);

                    }
                    break;
                case 8 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:97:3: DESC
                    {
                    DESC8=(Token)match(input,DESC,FOLLOW_DESC_in_keyword766); 

                    retval.value = (DESC8!=null?DESC8.getText():null);

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "keyword"


    public static class identifier_return extends ParserRuleReturnScope {
        public String value;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "identifier"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:100:1: identifier returns [String value] : ( ID |s1= keyword );
    public final CreateIndexParser.identifier_return identifier() throws RecognitionException {
        CreateIndexParser.identifier_return retval = new CreateIndexParser.identifier_return();
        retval.start = input.LT(1);


        Token ID9=null;
        CreateIndexParser.keyword_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:101:2: ( ID |s1= keyword )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==ID) ) {
                alt2=1;
            }
            else if ( (LA2_0==ASC||LA2_0==BY||LA2_0==CREATE||LA2_0==DESC||LA2_0==INDEX||(LA2_0 >= ON && LA2_0 <= ORDER)||LA2_0==USING) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }
            switch (alt2) {
                case 1 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:101:4: ID
                    {
                    ID9=(Token)match(input,ID,FOLLOW_ID_in_identifier783); 

                    retval.value =(ID9!=null?ID9.getText():null);

                    }
                    break;
                case 2 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:102:4: s1= keyword
                    {
                    pushFollow(FOLLOW_keyword_in_identifier793);
                    s1=keyword();

                    state._fsp--;


                    retval.value = (s1!=null?s1.value:null);

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "identifier"


    public static class package_name_return extends ParserRuleReturnScope {
        public String packageName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "package_name"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:104:1: package_name returns [String packageName] : s1= identifier ( DOT s2= identifier )* ;
    public final CreateIndexParser.package_name_return package_name() throws RecognitionException {
        CreateIndexParser.package_name_return retval = new CreateIndexParser.package_name_return();
        retval.start = input.LT(1);


        CreateIndexParser.identifier_return s1 =null;

        CreateIndexParser.identifier_return s2 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:105:7: (s1= identifier ( DOT s2= identifier )* )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:105:10: s1= identifier ( DOT s2= identifier )*
            {
            pushFollow(FOLLOW_identifier_in_package_name817);
            s1=identifier();

            state._fsp--;


            retval.packageName = (s1!=null?s1.value:null); 

            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:106:3: ( DOT s2= identifier )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==DOT) ) {
                    switch ( input.LA(2) ) {
                    case ID:
                        {
                        int LA3_2 = input.LA(3);

                        if ( (LA3_2==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;
                    case CREATE:
                        {
                        int LA3_3 = input.LA(3);

                        if ( (LA3_3==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;
                    case INDEX:
                        {
                        int LA3_4 = input.LA(3);

                        if ( (LA3_4==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;
                    case ON:
                        {
                        int LA3_5 = input.LA(3);

                        if ( (LA3_5==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;
                    case USING:
                        {
                        int LA3_6 = input.LA(3);

                        if ( (LA3_6==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;
                    case ORDER:
                        {
                        int LA3_7 = input.LA(3);

                        if ( (LA3_7==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;
                    case BY:
                        {
                        int LA3_8 = input.LA(3);

                        if ( (LA3_8==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;
                    case ASC:
                        {
                        int LA3_9 = input.LA(3);

                        if ( (LA3_9==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;
                    case DESC:
                        {
                        int LA3_10 = input.LA(3);

                        if ( (LA3_10==DOT) ) {
                            switch ( input.LA(4) ) {
                            case ID:
                                {
                                int LA3_12 = input.LA(5);

                                if ( (LA3_12==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case CREATE:
                                {
                                int LA3_13 = input.LA(5);

                                if ( (LA3_13==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case INDEX:
                                {
                                int LA3_14 = input.LA(5);

                                if ( (LA3_14==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ON:
                                {
                                int LA3_15 = input.LA(5);

                                if ( (LA3_15==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case USING:
                                {
                                int LA3_16 = input.LA(5);

                                if ( (LA3_16==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ORDER:
                                {
                                int LA3_17 = input.LA(5);

                                if ( (LA3_17==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case BY:
                                {
                                int LA3_18 = input.LA(5);

                                if ( (LA3_18==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case ASC:
                                {
                                int LA3_19 = input.LA(5);

                                if ( (LA3_19==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;
                            case DESC:
                                {
                                int LA3_20 = input.LA(5);

                                if ( (LA3_20==DOT) ) {
                                    alt3=1;
                                }


                                }
                                break;

                            }

                        }


                        }
                        break;

                    }

                }


                switch (alt3) {
            	case 1 :
            	    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:107:3: DOT s2= identifier
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_package_name828); 

            	    retval.packageName += "."; 

            	    pushFollow(FOLLOW_identifier_in_package_name836);
            	    s2=identifier();

            	    state._fsp--;


            	    retval.packageName += (s2!=null?s2.value:null); 

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "package_name"


    public static class class_name_return extends ParserRuleReturnScope {
        public String className;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "class_name"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:112:1: class_name returns [String className] : s1= identifier ;
    public final CreateIndexParser.class_name_return class_name() throws RecognitionException {
        CreateIndexParser.class_name_return retval = new CreateIndexParser.class_name_return();
        retval.start = input.LT(1);


        CreateIndexParser.identifier_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:113:6: (s1= identifier )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:113:8: s1= identifier
            {
            pushFollow(FOLLOW_identifier_in_class_name866);
            s1=identifier();

            state._fsp--;



            	    	retval.className = (s1!=null?s1.value:null);
            	    

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "class_name"


    public static class method_name_return extends ParserRuleReturnScope {
        public String methodName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "method_name"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:119:1: method_name returns [String methodName] : s1= identifier ;
    public final CreateIndexParser.method_name_return method_name() throws RecognitionException {
        CreateIndexParser.method_name_return retval = new CreateIndexParser.method_name_return();
        retval.start = input.LT(1);


        CreateIndexParser.identifier_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:120:6: (s1= identifier )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:120:8: s1= identifier
            {
            pushFollow(FOLLOW_identifier_in_method_name893);
            s1=identifier();

            state._fsp--;



            	    	retval.methodName = (s1!=null?s1.value:null);
            	    

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "method_name"


    public static class index_name_return extends ParserRuleReturnScope {
        public String indexName;
        public String indexOrder;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "index_name"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:126:1: index_name returns [String indexName, String indexOrder] : s1= identifier ( COLON INT )? ;
    public final CreateIndexParser.index_name_return index_name() throws RecognitionException {
        CreateIndexParser.index_name_return retval = new CreateIndexParser.index_name_return();
        retval.start = input.LT(1);


        Token INT10=null;
        CreateIndexParser.identifier_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:131:5: (s1= identifier ( COLON INT )? )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:131:7: s1= identifier ( COLON INT )?
            {
            pushFollow(FOLLOW_identifier_in_index_name928);
            s1=identifier();

            state._fsp--;



            	   	retval.indexName = (s1!=null?s1.value:null);	
            	   

            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:135:5: ( COLON INT )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==COLON) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:135:6: COLON INT
                    {
                    match(input,COLON,FOLLOW_COLON_in_index_name942); 

                    INT10=(Token)match(input,INT,FOLLOW_INT_in_index_name944); 

                    }
                    break;

            }



            	   	retval.indexOrder = (INT10!=null?INT10.getText():null);
            	   

            }

            retval.stop = input.LT(-1);



            		if(retval.indexOrder == null)
            		  retval.indexOrder = "0"; 
                       
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "index_name"


    public static class region_name_return extends ParserRuleReturnScope {
        public String regionName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "region_name"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:141:1: region_name returns [String regionName] : s1= identifier ;
    public final CreateIndexParser.region_name_return region_name() throws RecognitionException {
        CreateIndexParser.region_name_return retval = new CreateIndexParser.region_name_return();
        retval.start = input.LT(1);


        CreateIndexParser.identifier_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:142:5: (s1= identifier )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:142:7: s1= identifier
            {
            pushFollow(FOLLOW_identifier_in_region_name973);
            s1=identifier();

            state._fsp--;



            	   	retval.regionName = (s1!=null?s1.value:null);
            	   

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "region_name"


    public static class user_define_function_return extends ParserRuleReturnScope {
        public String funcName;
        public List paramList;
        public String lastParam;
        public List oriParamList;
        public String oriLastParam;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "user_define_function"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:148:1: user_define_function returns [String funcName, List paramList, String lastParam, List oriParamList, String oriLastParam] : s1= function_name LPAREN s2= call_parameters RPAREN ;
    public final CreateIndexParser.user_define_function_return user_define_function() throws RecognitionException {
        CreateIndexParser.user_define_function_return retval = new CreateIndexParser.user_define_function_return();
        retval.start = input.LT(1);


        CreateIndexParser.function_name_return s1 =null;

        CreateIndexParser.call_parameters_return s2 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:149:3: (s1= function_name LPAREN s2= call_parameters RPAREN )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:149:5: s1= function_name LPAREN s2= call_parameters RPAREN
            {
            pushFollow(FOLLOW_function_name_in_user_define_function997);
            s1=function_name();

            state._fsp--;


            match(input,LPAREN,FOLLOW_LPAREN_in_user_define_function999); 

            pushFollow(FOLLOW_call_parameters_in_user_define_function1003);
            s2=call_parameters();

            state._fsp--;


            match(input,RPAREN,FOLLOW_RPAREN_in_user_define_function1005); 


            		   retval.funcName = (s1!=null?s1.funcName:null);
            		   retval.paramList = (s2!=null?s2.paramList:null);
            		   retval.lastParam = (s2!=null?s2.lastParam:null); 	
            		   retval.oriParamList = (s2!=null?s2.oriParamList:null);
            		   retval.oriLastParam = (s2!=null?s2.oriLastParam:null);
            		

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "user_define_function"


    public static class function_name_return extends ParserRuleReturnScope {
        public String funcName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "function_name"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:159:1: function_name returns [String funcName] : s1= package_name DOT s2= class_name DOT s3= method_name ;
    public final CreateIndexParser.function_name_return function_name() throws RecognitionException {
        CreateIndexParser.function_name_return retval = new CreateIndexParser.function_name_return();
        retval.start = input.LT(1);


        CreateIndexParser.package_name_return s1 =null;

        CreateIndexParser.class_name_return s2 =null;

        CreateIndexParser.method_name_return s3 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:160:3: (s1= package_name DOT s2= class_name DOT s3= method_name )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:160:5: s1= package_name DOT s2= class_name DOT s3= method_name
            {
            pushFollow(FOLLOW_package_name_in_function_name1026);
            s1=package_name();

            state._fsp--;


            match(input,DOT,FOLLOW_DOT_in_function_name1028); 

            pushFollow(FOLLOW_class_name_in_function_name1032);
            s2=class_name();

            state._fsp--;


            match(input,DOT,FOLLOW_DOT_in_function_name1034); 

            pushFollow(FOLLOW_method_name_in_function_name1038);
            s3=method_name();

            state._fsp--;



            		   retval.funcName = (s1!=null?s1.packageName:null) + "." + (s2!=null?s2.className:null) + "." + (s3!=null?s3.methodName:null);	
            		

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "function_name"


    public static class call_parameters_return extends ParserRuleReturnScope {
        public List paramList;
        public String lastParam;
        public List oriParamList;
        public String oriLastParam;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "call_parameters"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:166:1: call_parameters returns [List paramList, String lastParam, List oriParamList, String oriLastParam] : s1= identifier ( COMMA s2= identifier )* ;
    public final CreateIndexParser.call_parameters_return call_parameters() throws RecognitionException {
        CreateIndexParser.call_parameters_return retval = new CreateIndexParser.call_parameters_return();
        retval.start = input.LT(1);


        CreateIndexParser.identifier_return s1 =null;

        CreateIndexParser.identifier_return s2 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:174:3: (s1= identifier ( COMMA s2= identifier )* )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:174:5: s1= identifier ( COMMA s2= identifier )*
            {
            pushFollow(FOLLOW_identifier_in_call_parameters1067);
            s1=identifier();

            state._fsp--;



            		  retval.paramList =new ArrayList<String>(); 
            		  StringBuffer sb = new StringBuffer((s1!=null?s1.value:null));
            		  sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            		  retval.paramList.add(sb.toString());
            		  
            		  retval.oriParamList =new ArrayList<String>(); 
            		  retval.oriParamList.add((s1!=null?s1.value:null));
            		 

            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:184:4: ( COMMA s2= identifier )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==COMMA) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:185:5: COMMA s2= identifier
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_call_parameters1082); 

            	    pushFollow(FOLLOW_identifier_in_call_parameters1090);
            	    s2=identifier();

            	    state._fsp--;



            	    		   StringBuffer sb2 = new StringBuffer((s2!=null?s2.value:null));
            	    		   sb2.setCharAt(0, Character.toUpperCase(sb2.charAt(0)));
            	    		   retval.paramList.add(sb2.toString()); 
            	    		   
            	    		   retval.oriParamList.add((s2!=null?s2.value:null)); 
            	    		  

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);



            		  retval.lastParam = (String)retval.paramList.get(retval.paramList.size() - 1);
            		  retval.paramList.remove(retval.paramList.size() - 1);
            		  
            		  retval.oriLastParam = (String)retval.oriParamList.get(retval.oriParamList.size() - 1);
            		  retval.oriParamList.remove(retval.oriParamList.size() - 1);
            		
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "call_parameters"


    public static class attributes_list_return extends ParserRuleReturnScope {
        public List attrList;
        public String lastParam;
        public String udfName;
        public List oriParamList;
        public String oriLastParam;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "attributes_list"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:197:1: attributes_list returns [List attrList, String lastParam, String udfName, List oriParamList, String oriLastParam] : (s1= call_parameters |s2= user_define_function );
    public final CreateIndexParser.attributes_list_return attributes_list() throws RecognitionException {
        CreateIndexParser.attributes_list_return retval = new CreateIndexParser.attributes_list_return();
        retval.start = input.LT(1);


        CreateIndexParser.call_parameters_return s1 =null;

        CreateIndexParser.user_define_function_return s2 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:198:3: (s1= call_parameters |s2= user_define_function )
            int alt6=2;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==EOF||LA6_1==COMMA||LA6_1==ORDER||LA6_1==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_1==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
                }
                break;
            case CREATE:
                {
                int LA6_2 = input.LA(2);

                if ( (LA6_2==EOF||LA6_2==COMMA||LA6_2==ORDER||LA6_2==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_2==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 2, input);

                    throw nvae;

                }
                }
                break;
            case INDEX:
                {
                int LA6_3 = input.LA(2);

                if ( (LA6_3==EOF||LA6_3==COMMA||LA6_3==ORDER||LA6_3==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_3==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 3, input);

                    throw nvae;

                }
                }
                break;
            case ON:
                {
                int LA6_4 = input.LA(2);

                if ( (LA6_4==EOF||LA6_4==COMMA||LA6_4==ORDER||LA6_4==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_4==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 4, input);

                    throw nvae;

                }
                }
                break;
            case USING:
                {
                int LA6_5 = input.LA(2);

                if ( (LA6_5==EOF||LA6_5==COMMA||LA6_5==ORDER||LA6_5==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_5==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 5, input);

                    throw nvae;

                }
                }
                break;
            case ORDER:
                {
                int LA6_6 = input.LA(2);

                if ( (LA6_6==EOF||LA6_6==COMMA||LA6_6==ORDER||LA6_6==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_6==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 6, input);

                    throw nvae;

                }
                }
                break;
            case BY:
                {
                int LA6_7 = input.LA(2);

                if ( (LA6_7==EOF||LA6_7==COMMA||LA6_7==ORDER||LA6_7==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_7==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 7, input);

                    throw nvae;

                }
                }
                break;
            case ASC:
                {
                int LA6_8 = input.LA(2);

                if ( (LA6_8==EOF||LA6_8==COMMA||LA6_8==ORDER||LA6_8==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_8==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 8, input);

                    throw nvae;

                }
                }
                break;
            case DESC:
                {
                int LA6_9 = input.LA(2);

                if ( (LA6_9==EOF||LA6_9==COMMA||LA6_9==ORDER||LA6_9==TO) ) {
                    alt6=1;
                }
                else if ( (LA6_9==DOT) ) {
                    alt6=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 9, input);

                    throw nvae;

                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }

            switch (alt6) {
                case 1 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:198:5: s1= call_parameters
                    {
                    pushFollow(FOLLOW_call_parameters_in_attributes_list1121);
                    s1=call_parameters();

                    state._fsp--;


                     retval.attrList = (s1!=null?s1.paramList:null); retval.lastParam = (s1!=null?s1.lastParam:null); retval.oriParamList =(s1!=null?s1.oriParamList:null); retval.oriLastParam =(s1!=null?s1.oriLastParam:null);

                    }
                    break;
                case 2 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:199:6: s2= user_define_function
                    {
                    pushFollow(FOLLOW_user_define_function_in_attributes_list1132);
                    s2=user_define_function();

                    state._fsp--;


                     retval.attrList = (s2!=null?s2.paramList:null); retval.lastParam = (s2!=null?s2.lastParam:null);retval.udfName = (s2!=null?s2.funcName:null); retval.oriParamList =(s2!=null?s2.oriParamList:null); retval.oriLastParam =(s2!=null?s2.oriLastParam:null);

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attributes_list"


    public static class order_by_clause_return extends ParserRuleReturnScope {
        public Map value;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "order_by_clause"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:202:1: order_by_clause returns [Map value] : (s1= identifier ( ASC | DESC )? |t1= user_define_function ( ASC | DESC )? );
    public final CreateIndexParser.order_by_clause_return order_by_clause() throws RecognitionException {
        CreateIndexParser.order_by_clause_return retval = new CreateIndexParser.order_by_clause_return();
        retval.start = input.LT(1);


        CreateIndexParser.identifier_return s1 =null;

        CreateIndexParser.user_define_function_return t1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:203:3: (s1= identifier ( ASC | DESC )? |t1= user_define_function ( ASC | DESC )? )
            int alt9=2;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==EOF||LA9_1==ASC||LA9_1==COMMA||LA9_1==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_1==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;

                }
                }
                break;
            case CREATE:
                {
                int LA9_2 = input.LA(2);

                if ( (LA9_2==EOF||LA9_2==ASC||LA9_2==COMMA||LA9_2==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_2==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 2, input);

                    throw nvae;

                }
                }
                break;
            case INDEX:
                {
                int LA9_3 = input.LA(2);

                if ( (LA9_3==EOF||LA9_3==ASC||LA9_3==COMMA||LA9_3==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_3==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 3, input);

                    throw nvae;

                }
                }
                break;
            case ON:
                {
                int LA9_4 = input.LA(2);

                if ( (LA9_4==EOF||LA9_4==ASC||LA9_4==COMMA||LA9_4==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_4==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 4, input);

                    throw nvae;

                }
                }
                break;
            case USING:
                {
                int LA9_5 = input.LA(2);

                if ( (LA9_5==EOF||LA9_5==ASC||LA9_5==COMMA||LA9_5==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_5==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 5, input);

                    throw nvae;

                }
                }
                break;
            case ORDER:
                {
                int LA9_6 = input.LA(2);

                if ( (LA9_6==EOF||LA9_6==ASC||LA9_6==COMMA||LA9_6==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_6==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 6, input);

                    throw nvae;

                }
                }
                break;
            case BY:
                {
                int LA9_7 = input.LA(2);

                if ( (LA9_7==EOF||LA9_7==ASC||LA9_7==COMMA||LA9_7==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_7==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 7, input);

                    throw nvae;

                }
                }
                break;
            case ASC:
                {
                int LA9_8 = input.LA(2);

                if ( (LA9_8==EOF||LA9_8==ASC||LA9_8==COMMA||LA9_8==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_8==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 8, input);

                    throw nvae;

                }
                }
                break;
            case DESC:
                {
                int LA9_9 = input.LA(2);

                if ( (LA9_9==EOF||LA9_9==ASC||LA9_9==COMMA||LA9_9==DESC) ) {
                    alt9=1;
                }
                else if ( (LA9_9==DOT) ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 9, input);

                    throw nvae;

                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }

            switch (alt9) {
                case 1 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:203:5: s1= identifier ( ASC | DESC )?
                    {
                    pushFollow(FOLLOW_identifier_in_order_by_clause1152);
                    s1=identifier();

                    state._fsp--;



                    	            StringBuffer sb = new StringBuffer((s1!=null?s1.value:null));
                    		    sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                    		    
                    		    retval.value = new HashMap();
                    		    retval.value.put("funcName", null);
                    	  	    retval.value.put("paramList", null);
                    	  	    retval.value.put("lastParam", sb.toString());
                    	  	    retval.value.put("oriParamList", null);
                    	  	    retval.value.put("oriLastParam", (s1!=null?s1.value:null)); 
                    	  	    retval.value.put("sort", true);
                    		  

                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:216:5: ( ASC | DESC )?
                    int alt7=3;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==ASC) ) {
                        alt7=1;
                    }
                    else if ( (LA7_0==DESC) ) {
                        alt7=2;
                    }
                    switch (alt7) {
                        case 1 :
                            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:216:7: ASC
                            {
                            match(input,ASC,FOLLOW_ASC_in_order_by_clause1167); 

                            }
                            break;
                        case 2 :
                            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:216:13: DESC
                            {
                            match(input,DESC,FOLLOW_DESC_in_order_by_clause1171); 

                            retval.value.put("sort", false);

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:218:7: t1= user_define_function ( ASC | DESC )?
                    {
                    pushFollow(FOLLOW_user_define_function_in_order_by_clause1195);
                    t1=user_define_function();

                    state._fsp--;



                    		    retval.value = new HashMap();
                    	  	    retval.value.put("funcName", (t1!=null?t1.funcName:null));
                    	  	    retval.value.put("paramList", (t1!=null?t1.paramList:null));
                    	  	    retval.value.put("lastParam", (t1!=null?t1.lastParam:null));
                    	  	    retval.value.put("oriParamList", (t1!=null?t1.oriParamList:null));
                    	  	    retval.value.put("oriLastParam", (t1!=null?t1.oriLastParam:null));
                    	  	    retval.value.put("sort", true);
                    	  	  

                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:228:7: ( ASC | DESC )?
                    int alt8=3;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==ASC) ) {
                        alt8=1;
                    }
                    else if ( (LA8_0==DESC) ) {
                        alt8=2;
                    }
                    switch (alt8) {
                        case 1 :
                            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:228:9: ASC
                            {
                            match(input,ASC,FOLLOW_ASC_in_order_by_clause1214); 

                            }
                            break;
                        case 2 :
                            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:228:15: DESC
                            {
                            match(input,DESC,FOLLOW_DESC_in_order_by_clause1218); 

                            retval.value.put("sort",false);

                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "order_by_clause"


    public static class multi_order_by_clause_return extends ParserRuleReturnScope {
        public List itemList;
        public Set oriParamList;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "multi_order_by_clause"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:231:1: multi_order_by_clause returns [List itemList, Set oriParamList] : ORDER BY t1= order_by_clause ( COMMA t2= order_by_clause )* ;
    public final CreateIndexParser.multi_order_by_clause_return multi_order_by_clause() throws RecognitionException {
        CreateIndexParser.multi_order_by_clause_return retval = new CreateIndexParser.multi_order_by_clause_return();
        retval.start = input.LT(1);


        CreateIndexParser.order_by_clause_return t1 =null;

        CreateIndexParser.order_by_clause_return t2 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:232:3: ( ORDER BY t1= order_by_clause ( COMMA t2= order_by_clause )* )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:232:5: ORDER BY t1= order_by_clause ( COMMA t2= order_by_clause )*
            {
            match(input,ORDER,FOLLOW_ORDER_in_multi_order_by_clause1238); 

            match(input,BY,FOLLOW_BY_in_multi_order_by_clause1240); 

            pushFollow(FOLLOW_order_by_clause_in_multi_order_by_clause1244);
            t1=order_by_clause();

            state._fsp--;



            		  retval.itemList =new ArrayList(); 
            		  retval.itemList.add((t1!=null?t1.value:null));
            		  
            		  retval.oriParamList =new HashSet<String>(); 
            		  Map item = (t1!=null?t1.value:null);
            		  List list = (List)item.get("oriParamList");
            		  if(list != null)
            		    retval.oriParamList.addAll(list);
            		  retval.oriParamList.add(item.get("oriLastParam"));
            	  	

            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:244:3: ( COMMA t2= order_by_clause )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==COMMA) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:244:4: COMMA t2= order_by_clause
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_multi_order_by_clause1253); 

            	    pushFollow(FOLLOW_order_by_clause_in_multi_order_by_clause1259);
            	    t2=order_by_clause();

            	    state._fsp--;



            	    	          retval.itemList.add((t2!=null?t2.value:null));
            	    		  Map item2 = (t2!=null?t2.value:null);
            	    		  List list2 = (List)item2.get("oriParamList");
            	    		  if(list2 != null)
            	    		    retval.oriParamList.addAll(list2);
            	    		  retval.oriParamList.add(item2.get("oriLastParam"));
            	    		

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "multi_order_by_clause"


    public static class create_rangeindex_clause_return extends ParserRuleReturnScope {
        public String indexName;
        public String indexOrder;
        public String packageName;
        public String regionName;
        public String keyClass;
        public String valueClass;
        public String attr;
        public String oriAttr;
        public List itemList;
        public boolean keyOnly;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "create_rangeindex_clause"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:257:1: create_rangeindex_clause returns [String indexName, String indexOrder, String packageName, String regionName,\r\n String keyClass, String valueClass, String attr, String oriAttr,\r\n List itemList, boolean keyOnly] : CREATE RANGEINDEX s1= index_name ON s2= region_name USING s3= identifier (s4= multi_order_by_clause )? -> rangeKeyFile(packageName=getPackageName()indexName=$s1.indexNameattr=$s3.value);
    public final CreateIndexParser.create_rangeindex_clause_return create_rangeindex_clause() throws RecognitionException {
        CreateIndexParser.create_rangeindex_clause_return retval = new CreateIndexParser.create_rangeindex_clause_return();
        retval.start = input.LT(1);


        CreateIndexParser.index_name_return s1 =null;

        CreateIndexParser.region_name_return s2 =null;

        CreateIndexParser.identifier_return s3 =null;

        CreateIndexParser.multi_order_by_clause_return s4 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:260:2: ( CREATE RANGEINDEX s1= index_name ON s2= region_name USING s3= identifier (s4= multi_order_by_clause )? -> rangeKeyFile(packageName=getPackageName()indexName=$s1.indexNameattr=$s3.value))
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:260:4: CREATE RANGEINDEX s1= index_name ON s2= region_name USING s3= identifier (s4= multi_order_by_clause )?
            {
            match(input,CREATE,FOLLOW_CREATE_in_create_rangeindex_clause1286); 

            match(input,RANGEINDEX,FOLLOW_RANGEINDEX_in_create_rangeindex_clause1288); 

            pushFollow(FOLLOW_index_name_in_create_rangeindex_clause1292);
            s1=index_name();

            state._fsp--;


            match(input,ON,FOLLOW_ON_in_create_rangeindex_clause1294); 

            pushFollow(FOLLOW_region_name_in_create_rangeindex_clause1298);
            s2=region_name();

            state._fsp--;


            match(input,USING,FOLLOW_USING_in_create_rangeindex_clause1300); 

            pushFollow(FOLLOW_identifier_in_create_rangeindex_clause1304);
            s3=identifier();

            state._fsp--;


            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:260:76: (s4= multi_order_by_clause )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==ORDER) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:260:76: s4= multi_order_by_clause
                    {
                    pushFollow(FOLLOW_multi_order_by_clause_in_create_rangeindex_clause1308);
                    s4=multi_order_by_clause();

                    state._fsp--;


                    }
                    break;

            }



            		retval.indexName = (s1!=null?s1.indexName:null);	
            		retval.indexOrder = (s1!=null?s1.indexOrder:null);
            		retval.packageName = getPackageName();
            		retval.regionName = (s2!=null?s2.regionName:null);
            		DomainMapperHelper.scanMapperRegistryClass();
            	   	IMapperTool tool = DomainRegistry.getMapperTool((s2!=null?s2.regionName:null));
            	   	retval.keyClass = tool.getKeyClass().getName();
            	   	retval.valueClass = tool.getValueClass().getName();
            	   	Collection<String> kfs = tool.getKeyFieldNames();
            	   	retval.attr = (s3!=null?s3.value:null);
            	   	StringBuffer sb = new StringBuffer((s3!=null?s3.value:null));
            		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            		retval.oriAttr = sb.toString();
            		retval.itemList = (s4!=null?s4.itemList:null);
            		if((s4!=null?s4.oriParamList:null)!=null && kfs.containsAll((s4!=null?s4.oriParamList:null)))
            		    retval.keyOnly = true;
            		else
            		    retval.keyOnly = false; 
            	

            // TEMPLATE REWRITE
            // 281:2: -> rangeKeyFile(packageName=getPackageName()indexName=$s1.indexNameattr=$s3.value)
            {
                retval.st = templateLib.getInstanceOf("rangeKeyFile",new STAttrMap().put("packageName", getPackageName()).put("indexName", (s1!=null?s1.indexName:null)).put("attr", (s3!=null?s3.value:null)));
            }



            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "create_rangeindex_clause"


    public static class produce_rangeindex_code_return extends ParserRuleReturnScope {
        public String indexName;
        public String packageName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "produce_rangeindex_code"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:284:1: produce_rangeindex_code returns [String indexName, String packageName] : s1= create_rangeindex_clause -> rangeClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattr=$s1.attroriAttr=$s1.oriAttrorderItemList=$s1.itemListkeyOnly=$s1.keyOnly);
    public final CreateIndexParser.produce_rangeindex_code_return produce_rangeindex_code() throws RecognitionException {
        CreateIndexParser.produce_rangeindex_code_return retval = new CreateIndexParser.produce_rangeindex_code_return();
        retval.start = input.LT(1);


        CreateIndexParser.create_rangeindex_clause_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:285:3: (s1= create_rangeindex_clause -> rangeClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattr=$s1.attroriAttr=$s1.oriAttrorderItemList=$s1.itemListkeyOnly=$s1.keyOnly))
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:285:5: s1= create_rangeindex_clause
            {
            pushFollow(FOLLOW_create_rangeindex_clause_in_produce_rangeindex_code1348);
            s1=create_rangeindex_clause();

            state._fsp--;



            		  retval.indexName = (s1!=null?s1.indexName:null);
            		  retval.packageName = (s1!=null?s1.packageName:null);
            		

            // TEMPLATE REWRITE
            // 290:3: -> rangeClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattr=$s1.attroriAttr=$s1.oriAttrorderItemList=$s1.itemListkeyOnly=$s1.keyOnly)
            {
                retval.st = templateLib.getInstanceOf("rangeClassFile",new STAttrMap().put("packageName", (s1!=null?s1.packageName:null)).put("indexName", (s1!=null?s1.indexName:null)).put("indexOrder", (s1!=null?s1.indexOrder:null)).put("regionName", (s1!=null?s1.regionName:null)).put("keyClass", (s1!=null?s1.keyClass:null)).put("valueClass", (s1!=null?s1.valueClass:null)).put("attr", (s1!=null?s1.attr:null)).put("oriAttr", (s1!=null?s1.oriAttr:null)).put("orderItemList", (s1!=null?s1.itemList:null)).put("keyOnly", (s1!=null?s1.keyOnly:false)));
            }



            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "produce_rangeindex_code"


    public static class create_index_clause_return extends ParserRuleReturnScope {
        public String indexName;
        public String indexOrder;
        public String packageName;
        public String regionName;
        public String keyClass;
        public String valueClass;
        public List attrList;
        public String lastParam;
        public String udfName;
        public List oriParamList;
        public String oriLastParam;
        public List itemList;
        public boolean keyOnly;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "create_index_clause"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:297:1: create_index_clause returns [String indexName, String indexOrder, String packageName, String regionName,\r\n\t\t\t String keyClass, String valueClass, List attrList,\r\n\t\t\t String lastParam, String udfName, List oriParamList, String oriLastParam, \r\n\t\t\t List itemList, boolean keyOnly] : CREATE INDEX s1= index_name ON s2= region_name USING s3= attributes_list (s4= multi_order_by_clause )? -> keyFile(packageName=getPackageName()indexName=$s1.indexNameattrList=$s3.oriParamListlastParam=$s3.oriLastParamudfName=$s3.udfName);
    public final CreateIndexParser.create_index_clause_return create_index_clause() throws RecognitionException {
        CreateIndexParser.create_index_clause_return retval = new CreateIndexParser.create_index_clause_return();
        retval.start = input.LT(1);


        CreateIndexParser.index_name_return s1 =null;

        CreateIndexParser.region_name_return s2 =null;

        CreateIndexParser.attributes_list_return s3 =null;

        CreateIndexParser.multi_order_by_clause_return s4 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:301:3: ( CREATE INDEX s1= index_name ON s2= region_name USING s3= attributes_list (s4= multi_order_by_clause )? -> keyFile(packageName=getPackageName()indexName=$s1.indexNameattrList=$s3.oriParamListlastParam=$s3.oriLastParamudfName=$s3.udfName))
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:301:5: CREATE INDEX s1= index_name ON s2= region_name USING s3= attributes_list (s4= multi_order_by_clause )?
            {
            match(input,CREATE,FOLLOW_CREATE_in_create_index_clause1485); 

            match(input,INDEX,FOLLOW_INDEX_in_create_index_clause1487); 

            pushFollow(FOLLOW_index_name_in_create_index_clause1491);
            s1=index_name();

            state._fsp--;


            match(input,ON,FOLLOW_ON_in_create_index_clause1493); 

            pushFollow(FOLLOW_region_name_in_create_index_clause1497);
            s2=region_name();

            state._fsp--;


            match(input,USING,FOLLOW_USING_in_create_index_clause1499); 

            pushFollow(FOLLOW_attributes_list_in_create_index_clause1503);
            s3=attributes_list();

            state._fsp--;


            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:301:77: (s4= multi_order_by_clause )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ORDER) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:301:77: s4= multi_order_by_clause
                    {
                    pushFollow(FOLLOW_multi_order_by_clause_in_create_index_clause1507);
                    s4=multi_order_by_clause();

                    state._fsp--;


                    }
                    break;

            }



            		  retval.indexName = (s1!=null?s1.indexName:null);	
            		  retval.indexOrder = (s1!=null?s1.indexOrder:null);
            	   	  retval.packageName = getPackageName();
            	   	  retval.regionName = (s2!=null?s2.regionName:null);
            	   	  DomainMapperHelper.scanMapperRegistryClass();
            	   	  IMapperTool tool = DomainRegistry.getMapperTool((s2!=null?s2.regionName:null));
            	   	  retval.keyClass = tool.getKeyClass().getName();
            	   	  retval.valueClass = tool.getValueClass().getName();
            	   	  Collection<String> kfs = tool.getKeyFieldNames();
            	   	  retval.attrList = (s3!=null?s3.attrList:null);
            	   	  retval.lastParam = (s3!=null?s3.lastParam:null);
            	   	  retval.udfName = (s3!=null?s3.udfName:null);
            	   	  retval.oriParamList = (s3!=null?s3.oriParamList:null);
            	   	  retval.oriLastParam = (s3!=null?s3.oriLastParam:null);
            	   	  retval.itemList = (s4!=null?s4.itemList:null);
            		  if((s4!=null?s4.oriParamList:null)!=null && kfs.containsAll((s4!=null?s4.oriParamList:null)))
            		    retval.keyOnly = true;
            		  else
            		    retval.keyOnly = false;  
            		

            // TEMPLATE REWRITE
            // 323:3: -> keyFile(packageName=getPackageName()indexName=$s1.indexNameattrList=$s3.oriParamListlastParam=$s3.oriLastParamudfName=$s3.udfName)
            {
                retval.st = templateLib.getInstanceOf("keyFile",new STAttrMap().put("packageName", getPackageName()).put("indexName", (s1!=null?s1.indexName:null)).put("attrList", (s3!=null?s3.oriParamList:null)).put("lastParam", (s3!=null?s3.oriLastParam:null)).put("udfName", (s3!=null?s3.udfName:null)));
            }



            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "create_index_clause"


    public static class produce_index_code_return extends ParserRuleReturnScope {
        public String indexName;
        public String packageName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "produce_index_code"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:327:1: produce_index_code returns [String indexName, String packageName] : s1= create_index_clause -> classFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattrList=$s1.attrListlastParam=$s1.lastParamudfName=$s1.udfNameoriParamList=$s1.oriParamListoriLastParam=$s1.oriLastParamorderItemList=$s1.itemListkeyOnly=$s1.keyOnly);
    public final CreateIndexParser.produce_index_code_return produce_index_code() throws RecognitionException {
        CreateIndexParser.produce_index_code_return retval = new CreateIndexParser.produce_index_code_return();
        retval.start = input.LT(1);


        CreateIndexParser.create_index_clause_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:328:3: (s1= create_index_clause -> classFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattrList=$s1.attrListlastParam=$s1.lastParamudfName=$s1.udfNameoriParamList=$s1.oriParamListoriLastParam=$s1.oriLastParamorderItemList=$s1.itemListkeyOnly=$s1.keyOnly))
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:328:5: s1= create_index_clause
            {
            pushFollow(FOLLOW_create_index_clause_in_produce_index_code1570);
            s1=create_index_clause();

            state._fsp--;



            		  retval.indexName = (s1!=null?s1.indexName:null);
            		  retval.packageName = (s1!=null?s1.packageName:null);
            		

            // TEMPLATE REWRITE
            // 333:3: -> classFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattrList=$s1.attrListlastParam=$s1.lastParamudfName=$s1.udfNameoriParamList=$s1.oriParamListoriLastParam=$s1.oriLastParamorderItemList=$s1.itemListkeyOnly=$s1.keyOnly)
            {
                retval.st = templateLib.getInstanceOf("classFile",new STAttrMap().put("packageName", (s1!=null?s1.packageName:null)).put("indexName", (s1!=null?s1.indexName:null)).put("indexOrder", (s1!=null?s1.indexOrder:null)).put("regionName", (s1!=null?s1.regionName:null)).put("keyClass", (s1!=null?s1.keyClass:null)).put("valueClass", (s1!=null?s1.valueClass:null)).put("attrList", (s1!=null?s1.attrList:null)).put("lastParam", (s1!=null?s1.lastParam:null)).put("udfName", (s1!=null?s1.udfName:null)).put("oriParamList", (s1!=null?s1.oriParamList:null)).put("oriLastParam", (s1!=null?s1.oriLastParam:null)).put("orderItemList", (s1!=null?s1.itemList:null)).put("keyOnly", (s1!=null?s1.keyOnly:false)));
            }



            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "produce_index_code"


    public static class produce_test_index_code_return extends ParserRuleReturnScope {
        public String indexName;
        public String packageName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "produce_test_index_code"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:340:1: produce_test_index_code returns [String indexName, String packageName] : s1= create_index_clause -> testClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattrList=$s1.attrListlastParam=$s1.lastParamudfName=$s1.udfNameoriParamList=$s1.oriParamListoriLastParam=$s1.oriLastParamorderItemList=$s1.itemListkeyOnly=$s1.keyOnly);
    public final CreateIndexParser.produce_test_index_code_return produce_test_index_code() throws RecognitionException {
        CreateIndexParser.produce_test_index_code_return retval = new CreateIndexParser.produce_test_index_code_return();
        retval.start = input.LT(1);


        CreateIndexParser.create_index_clause_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:341:3: (s1= create_index_clause -> testClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattrList=$s1.attrListlastParam=$s1.lastParamudfName=$s1.udfNameoriParamList=$s1.oriParamListoriLastParam=$s1.oriLastParamorderItemList=$s1.itemListkeyOnly=$s1.keyOnly))
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:341:5: s1= create_index_clause
            {
            pushFollow(FOLLOW_create_index_clause_in_produce_test_index_code1726);
            s1=create_index_clause();

            state._fsp--;



            		  retval.indexName = (s1!=null?s1.indexName:null);
            		  retval.packageName = (s1!=null?s1.packageName:null);
            		

            // TEMPLATE REWRITE
            // 346:3: -> testClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassattrList=$s1.attrListlastParam=$s1.lastParamudfName=$s1.udfNameoriParamList=$s1.oriParamListoriLastParam=$s1.oriLastParamorderItemList=$s1.itemListkeyOnly=$s1.keyOnly)
            {
                retval.st = templateLib.getInstanceOf("testClassFile",new STAttrMap().put("packageName", (s1!=null?s1.packageName:null)).put("indexName", (s1!=null?s1.indexName:null)).put("indexOrder", (s1!=null?s1.indexOrder:null)).put("regionName", (s1!=null?s1.regionName:null)).put("keyClass", (s1!=null?s1.keyClass:null)).put("valueClass", (s1!=null?s1.valueClass:null)).put("attrList", (s1!=null?s1.attrList:null)).put("lastParam", (s1!=null?s1.lastParam:null)).put("udfName", (s1!=null?s1.udfName:null)).put("oriParamList", (s1!=null?s1.oriParamList:null)).put("oriLastParam", (s1!=null?s1.oriLastParam:null)).put("orderItemList", (s1!=null?s1.itemList:null)).put("keyOnly", (s1!=null?s1.keyOnly:false)));
            }



            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "produce_test_index_code"


    public static class create_mappingregion_clause_return extends ParserRuleReturnScope {
        public String indexName;
        public String indexOrder;
        public String packageName;
        public String regionName;
        public String keyClass;
        public String valueClass;
        public List oriNameList;
        public List nameList;
        public List fromAttrList;
        public String fromLastParam;
        public List fromOriParamList;
        public String fromOriLastParam;
        public List toAttrList;
        public String toLastParam;
        public List toOriParamList;
        public String toOriLastParam;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "create_mappingregion_clause"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:353:1: create_mappingregion_clause returns [String indexName, String indexOrder, String packageName, String regionName,\r\n\t\t\t String keyClass, String valueClass, List oriNameList, List nameList, \r\n\t\t\t List fromAttrList, String fromLastParam, List fromOriParamList, String fromOriLastParam, \r\n\t\t\t List toAttrList, String toLastParam, List toOriParamList, String toOriLastParam] : CREATE HASHINDEX s1= index_name ON s2= region_name FROM s3= attributes_list TO s4= attributes_list ;
    public final CreateIndexParser.create_mappingregion_clause_return create_mappingregion_clause() throws RecognitionException {
        CreateIndexParser.create_mappingregion_clause_return retval = new CreateIndexParser.create_mappingregion_clause_return();
        retval.start = input.LT(1);


        CreateIndexParser.index_name_return s1 =null;

        CreateIndexParser.region_name_return s2 =null;

        CreateIndexParser.attributes_list_return s3 =null;

        CreateIndexParser.attributes_list_return s4 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:357:2: ( CREATE HASHINDEX s1= index_name ON s2= region_name FROM s3= attributes_list TO s4= attributes_list )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:357:4: CREATE HASHINDEX s1= index_name ON s2= region_name FROM s3= attributes_list TO s4= attributes_list
            {
            match(input,CREATE,FOLLOW_CREATE_in_create_mappingregion_clause1877); 

            match(input,HASHINDEX,FOLLOW_HASHINDEX_in_create_mappingregion_clause1879); 

            pushFollow(FOLLOW_index_name_in_create_mappingregion_clause1883);
            s1=index_name();

            state._fsp--;


            match(input,ON,FOLLOW_ON_in_create_mappingregion_clause1885); 

            pushFollow(FOLLOW_region_name_in_create_mappingregion_clause1889);
            s2=region_name();

            state._fsp--;


            match(input,FROM,FOLLOW_FROM_in_create_mappingregion_clause1891); 

            pushFollow(FOLLOW_attributes_list_in_create_mappingregion_clause1895);
            s3=attributes_list();

            state._fsp--;


            match(input,TO,FOLLOW_TO_in_create_mappingregion_clause1897); 

            pushFollow(FOLLOW_attributes_list_in_create_mappingregion_clause1901);
            s4=attributes_list();

            state._fsp--;



            		retval.indexName = (s1!=null?s1.indexName:null);	
            		retval.indexOrder = (s1!=null?s1.indexOrder:null);
            	   	retval.packageName = getPackageName();
            	   	retval.regionName = (s2!=null?s2.regionName:null);
            	   	DomainMapperHelper.scanMapperRegistryClass();
            	   	IMapperTool tool = DomainRegistry.getMapperTool((s2!=null?s2.regionName:null));
            	   	retval.keyClass = tool.getKeyClass().getName();
            	   	retval.valueClass = tool.getValueClass().getName();
            	   	Collection<String> set = tool.getKeyFieldNames();
            		retval.oriNameList = new ArrayList<String>();
            		retval.nameList = new ArrayList<String>();
            		for(Iterator<String> it=set.iterator(); it.hasNext();)
            		{
            			String name = it.next();
            			retval.oriNameList.add(name);
            			StringBuffer sb = new StringBuffer(name);
            			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            			retval.nameList.add(sb.toString());
            		}
            	   		   	
            		retval.fromAttrList = (s3!=null?s3.attrList:null);
            	   	retval.fromLastParam = (s3!=null?s3.lastParam:null);
            	   	retval.fromOriParamList = (s3!=null?s3.oriParamList:null);
            	   	retval.fromOriLastParam = (s3!=null?s3.oriLastParam:null);
            	   	retval.toAttrList = (s4!=null?s4.attrList:null);
            	   	retval.toLastParam = (s4!=null?s4.lastParam:null);
            	   	retval.toOriParamList = (s4!=null?s4.oriParamList:null);
            	   	retval.toOriLastParam = (s4!=null?s4.oriLastParam:null);
            	

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "create_mappingregion_clause"


    public static class produce_mappingregion_code_return extends ParserRuleReturnScope {
        public String indexName;
        public String packageName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "produce_mappingregion_code"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:391:1: produce_mappingregion_code returns [String indexName, String packageName] : s1= create_mappingregion_clause -> mappingRegionClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassoriNameList=$s1.oriNameListnameList=$s1.nameListfromAttrList=$s1.fromAttrListfromLastParam=$s1.fromLastParamfromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParamtoAttrList=$s1.toAttrListtoLastParam=$s1.toLastParamtoOriParamList=$s1.toOriParamListtoOriLastParam=$s1.toOriLastParam);
    public final CreateIndexParser.produce_mappingregion_code_return produce_mappingregion_code() throws RecognitionException {
        CreateIndexParser.produce_mappingregion_code_return retval = new CreateIndexParser.produce_mappingregion_code_return();
        retval.start = input.LT(1);


        CreateIndexParser.create_mappingregion_clause_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:392:2: (s1= create_mappingregion_clause -> mappingRegionClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassoriNameList=$s1.oriNameListnameList=$s1.nameListfromAttrList=$s1.fromAttrListfromLastParam=$s1.fromLastParamfromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParamtoAttrList=$s1.toAttrListtoLastParam=$s1.toLastParamtoOriParamList=$s1.toOriParamListtoOriLastParam=$s1.toOriLastParam))
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:392:4: s1= create_mappingregion_clause
            {
            pushFollow(FOLLOW_create_mappingregion_clause_in_produce_mappingregion_code1922);
            s1=create_mappingregion_clause();

            state._fsp--;



            		retval.indexName = (s1!=null?s1.indexName:null);
            		retval.packageName = (s1!=null?s1.packageName:null);
            	

            // TEMPLATE REWRITE
            // 397:2: -> mappingRegionClassFile(packageName=$s1.packageNameindexName=$s1.indexNameindexOrder=$s1.indexOrderregionName=$s1.regionNamekeyClass=$s1.keyClassvalueClass=$s1.valueClassoriNameList=$s1.oriNameListnameList=$s1.nameListfromAttrList=$s1.fromAttrListfromLastParam=$s1.fromLastParamfromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParamtoAttrList=$s1.toAttrListtoLastParam=$s1.toLastParamtoOriParamList=$s1.toOriParamListtoOriLastParam=$s1.toOriLastParam)
            {
                retval.st = templateLib.getInstanceOf("mappingRegionClassFile",new STAttrMap().put("packageName", (s1!=null?s1.packageName:null)).put("indexName", (s1!=null?s1.indexName:null)).put("indexOrder", (s1!=null?s1.indexOrder:null)).put("regionName", (s1!=null?s1.regionName:null)).put("keyClass", (s1!=null?s1.keyClass:null)).put("valueClass", (s1!=null?s1.valueClass:null)).put("oriNameList", (s1!=null?s1.oriNameList:null)).put("nameList", (s1!=null?s1.nameList:null)).put("fromAttrList", (s1!=null?s1.fromAttrList:null)).put("fromLastParam", (s1!=null?s1.fromLastParam:null)).put("fromOriParamList", (s1!=null?s1.fromOriParamList:null)).put("fromOriLastParam", (s1!=null?s1.fromOriLastParam:null)).put("toAttrList", (s1!=null?s1.toAttrList:null)).put("toLastParam", (s1!=null?s1.toLastParam:null)).put("toOriParamList", (s1!=null?s1.toOriParamList:null)).put("toOriLastParam", (s1!=null?s1.toOriLastParam:null)));
            }



            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "produce_mappingregion_code"


    public static class produce_mappinglocalcache_code_return extends ParserRuleReturnScope {
        public String indexName;
        public String packageName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "produce_mappinglocalcache_code"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:404:1: produce_mappinglocalcache_code returns [String indexName, String packageName] : s1= create_mappingregion_clause -> mappingLocalCacheClassFile(packageName=$s1.packageNameindexName=$s1.indexNamefromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParam);
    public final CreateIndexParser.produce_mappinglocalcache_code_return produce_mappinglocalcache_code() throws RecognitionException {
        CreateIndexParser.produce_mappinglocalcache_code_return retval = new CreateIndexParser.produce_mappinglocalcache_code_return();
        retval.start = input.LT(1);


        CreateIndexParser.create_mappingregion_clause_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:405:2: (s1= create_mappingregion_clause -> mappingLocalCacheClassFile(packageName=$s1.packageNameindexName=$s1.indexNamefromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParam))
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:405:4: s1= create_mappingregion_clause
            {
            pushFollow(FOLLOW_create_mappingregion_clause_in_produce_mappinglocalcache_code2082);
            s1=create_mappingregion_clause();

            state._fsp--;



            		retval.indexName = (s1!=null?s1.indexName:null);
            		retval.packageName = (s1!=null?s1.packageName:null);
            	

            // TEMPLATE REWRITE
            // 410:2: -> mappingLocalCacheClassFile(packageName=$s1.packageNameindexName=$s1.indexNamefromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParam)
            {
                retval.st = templateLib.getInstanceOf("mappingLocalCacheClassFile",new STAttrMap().put("packageName", (s1!=null?s1.packageName:null)).put("indexName", (s1!=null?s1.indexName:null)).put("fromOriParamList", (s1!=null?s1.fromOriParamList:null)).put("fromOriLastParam", (s1!=null?s1.fromOriLastParam:null)));
            }



            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "produce_mappinglocalcache_code"


    public static class produce_mappingRegionResolver_code_return extends ParserRuleReturnScope {
        public String indexName;
        public String packageName;
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "produce_mappingRegionResolver_code"
    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:413:1: produce_mappingRegionResolver_code returns [String indexName, String packageName] : s1= create_mappingregion_clause -> mappingRegionResolverClassFile(packageName=$s1.packageNameindexName=$s1.indexNamefromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParam);
    public final CreateIndexParser.produce_mappingRegionResolver_code_return produce_mappingRegionResolver_code() throws RecognitionException {
        CreateIndexParser.produce_mappingRegionResolver_code_return retval = new CreateIndexParser.produce_mappingRegionResolver_code_return();
        retval.start = input.LT(1);


        CreateIndexParser.create_mappingregion_clause_return s1 =null;


        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:414:2: (s1= create_mappingregion_clause -> mappingRegionResolverClassFile(packageName=$s1.packageNameindexName=$s1.indexNamefromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParam))
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:414:4: s1= create_mappingregion_clause
            {
            pushFollow(FOLLOW_create_mappingregion_clause_in_produce_mappingRegionResolver_code2126);
            s1=create_mappingregion_clause();

            state._fsp--;



            		retval.indexName = (s1!=null?s1.indexName:null);
            		retval.packageName = (s1!=null?s1.packageName:null);
            	

            // TEMPLATE REWRITE
            // 419:2: -> mappingRegionResolverClassFile(packageName=$s1.packageNameindexName=$s1.indexNamefromOriParamList=$s1.fromOriParamListfromOriLastParam=$s1.fromOriLastParam)
            {
                retval.st = templateLib.getInstanceOf("mappingRegionResolverClassFile",new STAttrMap().put("packageName", (s1!=null?s1.packageName:null)).put("indexName", (s1!=null?s1.indexName:null)).put("fromOriParamList", (s1!=null?s1.fromOriParamList:null)).put("fromOriLastParam", (s1!=null?s1.fromOriLastParam:null)));
            }



            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "produce_mappingRegionResolver_code"

    // Delegated rules


 

    public static final BitSet FOLLOW_CREATE_in_keyword724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INDEX_in_keyword730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ON_in_keyword736 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_USING_in_keyword742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_keyword748 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BY_in_keyword754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASC_in_keyword760 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DESC_in_keyword766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_identifier783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_keyword_in_identifier793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_package_name817 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_DOT_in_package_name828 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_identifier_in_package_name836 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_identifier_in_class_name866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_method_name893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_index_name928 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_COLON_in_index_name942 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_INT_in_index_name944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_region_name973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_name_in_user_define_function997 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_LPAREN_in_user_define_function999 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_call_parameters_in_user_define_function1003 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_RPAREN_in_user_define_function1005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_package_name_in_function_name1026 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_DOT_in_function_name1028 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_class_name_in_function_name1032 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_DOT_in_function_name1034 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_method_name_in_function_name1038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_call_parameters1067 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_call_parameters1082 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_identifier_in_call_parameters1090 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_call_parameters_in_attributes_list1121 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_user_define_function_in_attributes_list1132 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_order_by_clause1152 = new BitSet(new long[]{0x0000000000001012L});
    public static final BitSet FOLLOW_ASC_in_order_by_clause1167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DESC_in_order_by_clause1171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_user_define_function_in_order_by_clause1195 = new BitSet(new long[]{0x0000000000001012L});
    public static final BitSet FOLLOW_ASC_in_order_by_clause1214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DESC_in_order_by_clause1218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ORDER_in_multi_order_by_clause1238 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_BY_in_multi_order_by_clause1240 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_order_by_clause_in_multi_order_by_clause1244 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_multi_order_by_clause1253 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_order_by_clause_in_multi_order_by_clause1259 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_CREATE_in_create_rangeindex_clause1286 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_RANGEINDEX_in_create_rangeindex_clause1288 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_index_name_in_create_rangeindex_clause1292 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ON_in_create_rangeindex_clause1294 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_region_name_in_create_rangeindex_clause1298 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_USING_in_create_rangeindex_clause1300 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_identifier_in_create_rangeindex_clause1304 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_multi_order_by_clause_in_create_rangeindex_clause1308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_rangeindex_clause_in_produce_rangeindex_code1348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_create_index_clause1485 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_INDEX_in_create_index_clause1487 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_index_name_in_create_index_clause1491 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ON_in_create_index_clause1493 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_region_name_in_create_index_clause1497 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_USING_in_create_index_clause1499 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_attributes_list_in_create_index_clause1503 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_multi_order_by_clause_in_create_index_clause1507 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_index_clause_in_produce_index_code1570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_index_clause_in_produce_test_index_code1726 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CREATE_in_create_mappingregion_clause1877 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_HASHINDEX_in_create_mappingregion_clause1879 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_index_name_in_create_mappingregion_clause1883 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ON_in_create_mappingregion_clause1885 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_region_name_in_create_mappingregion_clause1889 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_FROM_in_create_mappingregion_clause1891 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_attributes_list_in_create_mappingregion_clause1895 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_TO_in_create_mappingregion_clause1897 = new BitSet(new long[]{0x0000040180601450L});
    public static final BitSet FOLLOW_attributes_list_in_create_mappingregion_clause1901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_mappingregion_clause_in_produce_mappingregion_code1922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_mappingregion_clause_in_produce_mappinglocalcache_code2082 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_create_mappingregion_clause_in_produce_mappingRegionResolver_code2126 = new BitSet(new long[]{0x0000000000000002L});

}
