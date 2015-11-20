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


import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class SybaseSQLLexer extends Lexer {
    public static final int EOF=-1;
    public static final int AND_SYM=4;
    public static final int A_=5;
    public static final int BIT_NUM=6;
    public static final int B_=7;
    public static final int COMMA=8;
    public static final int C_=9;
    public static final int DELETE_SYM=10;
    public static final int DOT=11;
    public static final int D_=12;
    public static final int EQ_SYM=13;
    public static final int EXPONENT=14;
    public static final int E_=15;
    public static final int FALSE_SYM=16;
    public static final int FLOAT=17;
    public static final int FROM=18;
    public static final int F_=19;
    public static final int G_=20;
    public static final int HEX_DIGIT=21;
    public static final int HEX_DIGIT_FRAGMENT=22;
    public static final int H_=23;
    public static final int ID=24;
    public static final int INSERT=25;
    public static final int INT=26;
    public static final int INTO=27;
    public static final int IS_SYM=28;
    public static final int I_=29;
    public static final int J_=30;
    public static final int K_=31;
    public static final int LPAREN=32;
    public static final int L_=33;
    public static final int M_=34;
    public static final int NULL_SYM=35;
    public static final int N_=36;
    public static final int O_=37;
    public static final int P_=38;
    public static final int Q_=39;
    public static final int RPAREN=40;
    public static final int R_=41;
    public static final int SET_SYM=42;
    public static final int STRING=43;
    public static final int S_=44;
    public static final int TIMESTAMP=45;
    public static final int TRI_COLON=46;
    public static final int TRUE_SYM=47;
    public static final int T_=48;
    public static final int UPDATE=49;
    public static final int U_=50;
    public static final int VALUES=51;
    public static final int VALUE_SYM=52;
    public static final int V_=53;
    public static final int WHERE=54;
    public static final int WS=55;
    public static final int W_=56;
    public static final int X_=57;
    public static final int Y_=58;
    public static final int Z_=59;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public SybaseSQLLexer() {} 
    public SybaseSQLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SybaseSQLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g"; }

    // $ANTLR start "A_"
    public final void mA_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:22:13: ( 'a' | 'A' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "A_"

    // $ANTLR start "B_"
    public final void mB_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:23:13: ( 'b' | 'B' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "B_"

    // $ANTLR start "C_"
    public final void mC_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:24:13: ( 'c' | 'C' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "C_"

    // $ANTLR start "D_"
    public final void mD_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:25:13: ( 'd' | 'D' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "D_"

    // $ANTLR start "E_"
    public final void mE_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:26:13: ( 'e' | 'E' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "E_"

    // $ANTLR start "F_"
    public final void mF_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:27:13: ( 'f' | 'F' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "F_"

    // $ANTLR start "G_"
    public final void mG_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:28:13: ( 'g' | 'G' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "G_"

    // $ANTLR start "H_"
    public final void mH_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:29:13: ( 'h' | 'H' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "H_"

    // $ANTLR start "I_"
    public final void mI_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:30:13: ( 'i' | 'I' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "I_"

    // $ANTLR start "J_"
    public final void mJ_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:31:13: ( 'j' | 'J' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "J_"

    // $ANTLR start "K_"
    public final void mK_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:32:13: ( 'k' | 'K' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "K_"

    // $ANTLR start "L_"
    public final void mL_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:33:13: ( 'l' | 'L' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "L_"

    // $ANTLR start "M_"
    public final void mM_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:34:13: ( 'm' | 'M' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "M_"

    // $ANTLR start "N_"
    public final void mN_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:35:13: ( 'n' | 'N' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "N_"

    // $ANTLR start "O_"
    public final void mO_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:36:13: ( 'o' | 'O' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "O_"

    // $ANTLR start "P_"
    public final void mP_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:37:13: ( 'p' | 'P' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "P_"

    // $ANTLR start "Q_"
    public final void mQ_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:38:13: ( 'q' | 'Q' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Q_"

    // $ANTLR start "R_"
    public final void mR_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:39:13: ( 'r' | 'R' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "R_"

    // $ANTLR start "S_"
    public final void mS_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:40:13: ( 's' | 'S' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "S_"

    // $ANTLR start "T_"
    public final void mT_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:41:13: ( 't' | 'T' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T_"

    // $ANTLR start "U_"
    public final void mU_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:42:13: ( 'u' | 'U' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "U_"

    // $ANTLR start "V_"
    public final void mV_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:43:13: ( 'v' | 'V' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "V_"

    // $ANTLR start "W_"
    public final void mW_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:44:13: ( 'w' | 'W' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "W_"

    // $ANTLR start "X_"
    public final void mX_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:45:13: ( 'x' | 'X' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "X_"

    // $ANTLR start "Y_"
    public final void mY_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:46:13: ( 'y' | 'Y' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Y_"

    // $ANTLR start "Z_"
    public final void mZ_() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:47:13: ( 'z' | 'Z' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Z_"

    // $ANTLR start "HEX_DIGIT_FRAGMENT"
    public final void mHEX_DIGIT_FRAGMENT() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:51:20: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HEX_DIGIT_FRAGMENT"

    // $ANTLR start "EXPONENT"
    public final void mEXPONENT() throws RecognitionException {
        try {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:54:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:54:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:54:22: ( '+' | '-' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='+'||LA1_0=='-') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:54:33: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EXPONENT"

    // $ANTLR start "INSERT"
    public final void mINSERT() throws RecognitionException {
        try {
            int _type = INSERT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:55:8: ( I_ N_ S_ E_ R_ T_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:55:10: I_ N_ S_ E_ R_ T_
            {
            mI_(); 


            mN_(); 


            mS_(); 


            mE_(); 


            mR_(); 


            mT_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INSERT"

    // $ANTLR start "INTO"
    public final void mINTO() throws RecognitionException {
        try {
            int _type = INTO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:56:8: ( I_ N_ T_ O_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:56:10: I_ N_ T_ O_
            {
            mI_(); 


            mN_(); 


            mT_(); 


            mO_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INTO"

    // $ANTLR start "VALUE_SYM"
    public final void mVALUE_SYM() throws RecognitionException {
        try {
            int _type = VALUE_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:57:11: ( V_ A_ L_ U_ E_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:57:13: V_ A_ L_ U_ E_
            {
            mV_(); 


            mA_(); 


            mL_(); 


            mU_(); 


            mE_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VALUE_SYM"

    // $ANTLR start "VALUES"
    public final void mVALUES() throws RecognitionException {
        try {
            int _type = VALUES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:58:8: ( V_ A_ L_ U_ E_ S_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:58:10: V_ A_ L_ U_ E_ S_
            {
            mV_(); 


            mA_(); 


            mL_(); 


            mU_(); 


            mE_(); 


            mS_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "VALUES"

    // $ANTLR start "TRUE_SYM"
    public final void mTRUE_SYM() throws RecognitionException {
        try {
            int _type = TRUE_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:59:10: ( T_ R_ U_ E_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:59:12: T_ R_ U_ E_
            {
            mT_(); 


            mR_(); 


            mU_(); 


            mE_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TRUE_SYM"

    // $ANTLR start "FALSE_SYM"
    public final void mFALSE_SYM() throws RecognitionException {
        try {
            int _type = FALSE_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:60:11: ( F_ A_ L_ S_ E_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:60:13: F_ A_ L_ S_ E_
            {
            mF_(); 


            mA_(); 


            mL_(); 


            mS_(); 


            mE_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FALSE_SYM"

    // $ANTLR start "NULL_SYM"
    public final void mNULL_SYM() throws RecognitionException {
        try {
            int _type = NULL_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:61:11: ( N_ U_ L_ L_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:61:13: N_ U_ L_ L_
            {
            mN_(); 


            mU_(); 


            mL_(); 


            mL_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NULL_SYM"

    // $ANTLR start "UPDATE"
    public final void mUPDATE() throws RecognitionException {
        try {
            int _type = UPDATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:62:8: ( U_ P_ D_ A_ T_ E_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:62:10: U_ P_ D_ A_ T_ E_
            {
            mU_(); 


            mP_(); 


            mD_(); 


            mA_(); 


            mT_(); 


            mE_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UPDATE"

    // $ANTLR start "SET_SYM"
    public final void mSET_SYM() throws RecognitionException {
        try {
            int _type = SET_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:63:9: ( S_ E_ T_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:63:11: S_ E_ T_
            {
            mS_(); 


            mE_(); 


            mT_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SET_SYM"

    // $ANTLR start "WHERE"
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:64:7: ( W_ H_ E_ R_ E_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:64:9: W_ H_ E_ R_ E_
            {
            mW_(); 


            mH_(); 


            mE_(); 


            mR_(); 


            mE_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WHERE"

    // $ANTLR start "IS_SYM"
    public final void mIS_SYM() throws RecognitionException {
        try {
            int _type = IS_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:65:8: ( I_ S_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:65:10: I_ S_
            {
            mI_(); 


            mS_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "IS_SYM"

    // $ANTLR start "AND_SYM"
    public final void mAND_SYM() throws RecognitionException {
        try {
            int _type = AND_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:66:9: ( A_ N_ D_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:66:11: A_ N_ D_
            {
            mA_(); 


            mN_(); 


            mD_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AND_SYM"

    // $ANTLR start "DELETE_SYM"
    public final void mDELETE_SYM() throws RecognitionException {
        try {
            int _type = DELETE_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:67:12: ( D_ E_ L_ E_ T_ E_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:67:14: D_ E_ L_ E_ T_ E_
            {
            mD_(); 


            mE_(); 


            mL_(); 


            mE_(); 


            mT_(); 


            mE_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DELETE_SYM"

    // $ANTLR start "FROM"
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:68:6: ( F_ R_ O_ M_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:68:8: F_ R_ O_ M_
            {
            mF_(); 


            mR_(); 


            mO_(); 


            mM_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FROM"

    // $ANTLR start "TIMESTAMP"
    public final void mTIMESTAMP() throws RecognitionException {
        try {
            int _type = TIMESTAMP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:69:11: ( T_ I_ M_ E_ S_ T_ A_ M_ P_ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:69:13: T_ I_ M_ E_ S_ T_ A_ M_ P_
            {
            mT_(); 


            mI_(); 


            mM_(); 


            mE_(); 


            mS_(); 


            mT_(); 


            mA_(); 


            mM_(); 


            mP_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TIMESTAMP"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:72:8: ( '\\'' (~ ( '\\'' ) )* '\\'' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:72:11: '\\'' (~ ( '\\'' ) )* '\\''
            {
            match('\''); 

            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:72:16: (~ ( '\\'' ) )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0 >= '\u0000' && LA3_0 <= '&')||(LA3_0 >= '(' && LA3_0 <= '\uFFFF')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:75:5: ( ( '+' | '-' )? ( '0' .. '9' )+ )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:75:7: ( '+' | '-' )? ( '0' .. '9' )+
            {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:75:7: ( '+' | '-' )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='+'||LA4_0=='-') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:75:18: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '0' && LA5_0 <= '9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:78:7: ( ( '+' | '-' )? ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT ) )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:78:9: ( '+' | '-' )? ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
            {
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:78:9: ( '+' | '-' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='+'||LA6_0=='-') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:79:2: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
            int alt13=3;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:79:3: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
                    {
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:79:3: ( '0' .. '9' )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0 >= '0' && LA7_0 <= '9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    match('.'); 

                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:79:19: ( '0' .. '9' )*
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( ((LA8_0 >= '0' && LA8_0 <= '9')) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                    	case 1 :
                    	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);


                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:79:31: ( EXPONENT )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0=='E'||LA9_0=='e') ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:79:31: EXPONENT
                            {
                            mEXPONENT(); 


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:80:6: '.' ( '0' .. '9' )+ ( EXPONENT )?
                    {
                    match('.'); 

                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:80:10: ( '0' .. '9' )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0 >= '0' && LA10_0 <= '9')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);


                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:80:22: ( EXPONENT )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0=='E'||LA11_0=='e') ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:80:22: EXPONENT
                            {
                            mEXPONENT(); 


                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:81:6: ( '0' .. '9' )+ EXPONENT
                    {
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:81:6: ( '0' .. '9' )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0 >= '0' && LA12_0 <= '9')) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt12 >= 1 ) break loop12;
                                EarlyExitException eee =
                                    new EarlyExitException(12, input);
                                throw eee;
                        }
                        cnt12++;
                    } while (true);


                    mEXPONENT(); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "BIT_NUM"
    public final void mBIT_NUM() throws RecognitionException {
        try {
            int _type = BIT_NUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:84:9: ( ( '0b' ( '0' | '1' )+ ) | ( B_ '\\'' ( '0' | '1' )+ '\\'' ) )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0=='0') ) {
                alt16=1;
            }
            else if ( (LA16_0=='B'||LA16_0=='b') ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }
            switch (alt16) {
                case 1 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:85:2: ( '0b' ( '0' | '1' )+ )
                    {
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:85:2: ( '0b' ( '0' | '1' )+ )
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:85:5: '0b' ( '0' | '1' )+
                    {
                    match("0b"); 



                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:85:13: ( '0' | '1' )+
                    int cnt14=0;
                    loop14:
                    do {
                        int alt14=2;
                        int LA14_0 = input.LA(1);

                        if ( ((LA14_0 >= '0' && LA14_0 <= '1')) ) {
                            alt14=1;
                        }


                        switch (alt14) {
                    	case 1 :
                    	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '1') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt14 >= 1 ) break loop14;
                                EarlyExitException eee =
                                    new EarlyExitException(14, input);
                                throw eee;
                        }
                        cnt14++;
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:87:2: ( B_ '\\'' ( '0' | '1' )+ '\\'' )
                    {
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:87:2: ( B_ '\\'' ( '0' | '1' )+ '\\'' )
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:87:5: B_ '\\'' ( '0' | '1' )+ '\\''
                    {
                    mB_(); 


                    match('\''); 

                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:87:13: ( '0' | '1' )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( ((LA15_0 >= '0' && LA15_0 <= '1')) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '1') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt15 >= 1 ) break loop15;
                                EarlyExitException eee =
                                    new EarlyExitException(15, input);
                                throw eee;
                        }
                        cnt15++;
                    } while (true);


                    match('\''); 

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BIT_NUM"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            int _type = HEX_DIGIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:90:10: ( ( '0x' ( HEX_DIGIT_FRAGMENT )+ ) | ( 'X' '\\'' ( HEX_DIGIT_FRAGMENT )+ '\\'' ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0=='0') ) {
                alt19=1;
            }
            else if ( (LA19_0=='X') ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }
            switch (alt19) {
                case 1 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:91:2: ( '0x' ( HEX_DIGIT_FRAGMENT )+ )
                    {
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:91:2: ( '0x' ( HEX_DIGIT_FRAGMENT )+ )
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:91:5: '0x' ( HEX_DIGIT_FRAGMENT )+
                    {
                    match("0x"); 



                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:91:10: ( HEX_DIGIT_FRAGMENT )+
                    int cnt17=0;
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( ((LA17_0 >= '0' && LA17_0 <= '9')||(LA17_0 >= 'A' && LA17_0 <= 'F')||(LA17_0 >= 'a' && LA17_0 <= 'f')) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt17 >= 1 ) break loop17;
                                EarlyExitException eee =
                                    new EarlyExitException(17, input);
                                throw eee;
                        }
                        cnt17++;
                    } while (true);


                    }


                    }
                    break;
                case 2 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:93:2: ( 'X' '\\'' ( HEX_DIGIT_FRAGMENT )+ '\\'' )
                    {
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:93:2: ( 'X' '\\'' ( HEX_DIGIT_FRAGMENT )+ '\\'' )
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:93:5: 'X' '\\'' ( HEX_DIGIT_FRAGMENT )+ '\\''
                    {
                    match('X'); 

                    match('\''); 

                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:93:14: ( HEX_DIGIT_FRAGMENT )+
                    int cnt18=0;
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( ((LA18_0 >= '0' && LA18_0 <= '9')||(LA18_0 >= 'A' && LA18_0 <= 'F')||(LA18_0 >= 'a' && LA18_0 <= 'f')) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
                    	    {
                    	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt18 >= 1 ) break loop18;
                                EarlyExitException eee =
                                    new EarlyExitException(18, input);
                                throw eee;
                        }
                        cnt18++;
                    } while (true);


                    match('\''); 

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:96:5: ( '.' | '..' )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0=='.') ) {
                int LA20_1 = input.LA(2);

                if ( (LA20_1=='.') ) {
                    alt20=2;
                }
                else {
                    alt20=1;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }
            switch (alt20) {
                case 1 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:96:7: '.'
                    {
                    match('.'); 

                    }
                    break;
                case 2 :
                    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:96:11: '..'
                    {
                    match(".."); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:97:8: ( ')' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:97:10: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RPAREN"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:98:8: ( '(' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:98:10: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LPAREN"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:99:7: ( ',' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:99:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "EQ_SYM"
    public final void mEQ_SYM() throws RecognitionException {
        try {
            int _type = EQ_SYM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:100:8: ( '=' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:100:10: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EQ_SYM"

    // $ANTLR start "TRI_COLON"
    public final void mTRI_COLON() throws RecognitionException {
        try {
            int _type = TRI_COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:101:11: ( ':::' )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:101:13: ':::'
            {
            match(":::"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TRI_COLON"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:103:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:103:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:103:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( ((LA21_0 >= '0' && LA21_0 <= '9')||(LA21_0 >= 'A' && LA21_0 <= 'Z')||LA21_0=='_'||(LA21_0 >= 'a' && LA21_0 <= 'z')) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:106:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:106:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:8: ( INSERT | INTO | VALUE_SYM | VALUES | TRUE_SYM | FALSE_SYM | NULL_SYM | UPDATE | SET_SYM | WHERE | IS_SYM | AND_SYM | DELETE_SYM | FROM | TIMESTAMP | STRING | INT | FLOAT | BIT_NUM | HEX_DIGIT | DOT | RPAREN | LPAREN | COMMA | EQ_SYM | TRI_COLON | ID | WS )
        int alt22=28;
        alt22 = dfa22.predict(input);
        switch (alt22) {
            case 1 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:10: INSERT
                {
                mINSERT(); 


                }
                break;
            case 2 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:17: INTO
                {
                mINTO(); 


                }
                break;
            case 3 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:22: VALUE_SYM
                {
                mVALUE_SYM(); 


                }
                break;
            case 4 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:32: VALUES
                {
                mVALUES(); 


                }
                break;
            case 5 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:39: TRUE_SYM
                {
                mTRUE_SYM(); 


                }
                break;
            case 6 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:48: FALSE_SYM
                {
                mFALSE_SYM(); 


                }
                break;
            case 7 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:58: NULL_SYM
                {
                mNULL_SYM(); 


                }
                break;
            case 8 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:67: UPDATE
                {
                mUPDATE(); 


                }
                break;
            case 9 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:74: SET_SYM
                {
                mSET_SYM(); 


                }
                break;
            case 10 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:82: WHERE
                {
                mWHERE(); 


                }
                break;
            case 11 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:88: IS_SYM
                {
                mIS_SYM(); 


                }
                break;
            case 12 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:95: AND_SYM
                {
                mAND_SYM(); 


                }
                break;
            case 13 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:103: DELETE_SYM
                {
                mDELETE_SYM(); 


                }
                break;
            case 14 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:114: FROM
                {
                mFROM(); 


                }
                break;
            case 15 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:119: TIMESTAMP
                {
                mTIMESTAMP(); 


                }
                break;
            case 16 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:129: STRING
                {
                mSTRING(); 


                }
                break;
            case 17 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:136: INT
                {
                mINT(); 


                }
                break;
            case 18 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:140: FLOAT
                {
                mFLOAT(); 


                }
                break;
            case 19 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:146: BIT_NUM
                {
                mBIT_NUM(); 


                }
                break;
            case 20 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:154: HEX_DIGIT
                {
                mHEX_DIGIT(); 


                }
                break;
            case 21 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:164: DOT
                {
                mDOT(); 


                }
                break;
            case 22 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:168: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 23 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:175: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 24 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:182: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 25 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:188: EQ_SYM
                {
                mEQ_SYM(); 


                }
                break;
            case 26 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:195: TRI_COLON
                {
                mTRI_COLON(); 


                }
                break;
            case 27 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:205: ID
                {
                mID(); 


                }
                break;
            case 28 :
                // D:\\MOR\\CrorderMain\\CrorderMq\\src\\main\\resources\\SQL.g:1:208: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA13 dfa13 = new DFA13(this);
    protected DFA22 dfa22 = new DFA22(this);
    static final String DFA13_eotS =
        "\5\uffff";
    static final String DFA13_eofS =
        "\5\uffff";
    static final String DFA13_minS =
        "\2\56\3\uffff";
    static final String DFA13_maxS =
        "\1\71\1\145\3\uffff";
    static final String DFA13_acceptS =
        "\2\uffff\1\2\1\1\1\3";
    static final String DFA13_specialS =
        "\5\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\2\1\uffff\12\1",
            "\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
            "",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "79:2: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )";
        }
    }
    static final String DFA22_eotS =
        "\1\uffff\12\27\2\uffff\1\51\1\52\1\51\2\27\7\uffff\1\27\1\55\13"+
        "\27\5\uffff\2\27\1\uffff\7\27\1\102\1\27\1\104\2\27\1\107\1\27\1"+
        "\111\2\27\1\114\1\115\1\27\1\uffff\1\27\1\uffff\2\27\1\uffff\1\122"+
        "\1\uffff\1\27\1\125\2\uffff\1\27\1\127\1\27\1\131\1\uffff\1\132"+
        "\1\27\1\uffff\1\134\1\uffff\1\135\2\uffff\1\27\2\uffff\1\27\1\140"+
        "\1\uffff";
    static final String DFA22_eofS =
        "\141\uffff";
    static final String DFA22_minS =
        "\1\11\1\116\1\101\1\111\1\101\1\125\1\120\1\105\1\110\1\116\1\105"+
        "\1\uffff\2\56\1\60\1\56\2\47\7\uffff\1\123\1\60\1\114\1\125\1\115"+
        "\1\114\1\117\1\114\1\104\1\124\1\105\1\104\1\114\5\uffff\1\105\1"+
        "\117\1\uffff\1\125\2\105\1\123\1\115\1\114\1\101\1\60\1\122\1\60"+
        "\1\105\1\122\1\60\1\105\1\60\1\123\1\105\2\60\1\124\1\uffff\1\105"+
        "\1\uffff\2\124\1\uffff\1\60\1\uffff\1\124\1\60\2\uffff\1\105\1\60"+
        "\1\105\1\60\1\uffff\1\60\1\101\1\uffff\1\60\1\uffff\1\60\2\uffff"+
        "\1\115\2\uffff\1\120\1\60\1\uffff";
    static final String DFA22_maxS =
        "\1\172\1\163\1\141\2\162\1\165\1\160\1\145\1\150\1\156\1\145\1\uffff"+
        "\1\71\1\170\1\71\1\145\2\47\7\uffff\1\164\1\172\1\154\1\165\1\155"+
        "\1\154\1\157\1\154\1\144\1\164\1\145\1\144\1\154\5\uffff\1\145\1"+
        "\157\1\uffff\1\165\2\145\1\163\1\155\1\154\1\141\1\172\1\162\1\172"+
        "\1\145\1\162\1\172\1\145\1\172\1\163\1\145\2\172\1\164\1\uffff\1"+
        "\145\1\uffff\2\164\1\uffff\1\172\1\uffff\1\164\1\172\2\uffff\1\145"+
        "\1\172\1\145\1\172\1\uffff\1\172\1\141\1\uffff\1\172\1\uffff\1\172"+
        "\2\uffff\1\155\2\uffff\1\160\1\172\1\uffff";
    static final String DFA22_acceptS =
        "\13\uffff\1\20\6\uffff\1\26\1\27\1\30\1\31\1\32\1\33\1\34\15\uffff"+
        "\1\22\1\23\1\24\1\21\1\25\2\uffff\1\13\24\uffff\1\11\1\uffff\1\14"+
        "\2\uffff\1\2\1\uffff\1\5\2\uffff\1\16\1\7\4\uffff\1\3\2\uffff\1"+
        "\6\1\uffff\1\12\1\uffff\1\1\1\4\1\uffff\1\10\1\15\2\uffff\1\17";
    static final String DFA22_specialS =
        "\141\uffff}>";
    static final String[] DFA22_transitionS = {
            "\2\30\2\uffff\1\30\22\uffff\1\30\6\uffff\1\13\1\23\1\22\1\uffff"+
            "\1\14\1\24\1\14\1\16\1\uffff\1\15\11\17\1\26\2\uffff\1\25\3"+
            "\uffff\1\11\1\20\1\27\1\12\1\27\1\4\2\27\1\1\4\27\1\5\4\27\1"+
            "\7\1\3\1\6\1\2\1\10\1\21\2\27\4\uffff\1\27\1\uffff\1\11\1\20"+
            "\1\27\1\12\1\27\1\4\2\27\1\1\4\27\1\5\4\27\1\7\1\3\1\6\1\2\1"+
            "\10\3\27",
            "\1\31\4\uffff\1\32\32\uffff\1\31\4\uffff\1\32",
            "\1\33\37\uffff\1\33",
            "\1\35\10\uffff\1\34\26\uffff\1\35\10\uffff\1\34",
            "\1\36\20\uffff\1\37\16\uffff\1\36\20\uffff\1\37",
            "\1\40\37\uffff\1\40",
            "\1\41\37\uffff\1\41",
            "\1\42\37\uffff\1\42",
            "\1\43\37\uffff\1\43",
            "\1\44\37\uffff\1\44",
            "\1\45\37\uffff\1\45",
            "",
            "\1\46\1\uffff\12\17",
            "\1\46\1\uffff\12\17\13\uffff\1\46\34\uffff\1\47\2\uffff\1\46"+
            "\22\uffff\1\50",
            "\12\46",
            "\1\46\1\uffff\12\17\13\uffff\1\46\37\uffff\1\46",
            "\1\47",
            "\1\50",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\53\1\54\36\uffff\1\53\1\54",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\56\37\uffff\1\56",
            "\1\57\37\uffff\1\57",
            "\1\60\37\uffff\1\60",
            "\1\61\37\uffff\1\61",
            "\1\62\37\uffff\1\62",
            "\1\63\37\uffff\1\63",
            "\1\64\37\uffff\1\64",
            "\1\65\37\uffff\1\65",
            "\1\66\37\uffff\1\66",
            "\1\67\37\uffff\1\67",
            "\1\70\37\uffff\1\70",
            "",
            "",
            "",
            "",
            "",
            "\1\71\37\uffff\1\71",
            "\1\72\37\uffff\1\72",
            "",
            "\1\73\37\uffff\1\73",
            "\1\74\37\uffff\1\74",
            "\1\75\37\uffff\1\75",
            "\1\76\37\uffff\1\76",
            "\1\77\37\uffff\1\77",
            "\1\100\37\uffff\1\100",
            "\1\101\37\uffff\1\101",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\103\37\uffff\1\103",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\105\37\uffff\1\105",
            "\1\106\37\uffff\1\106",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\110\37\uffff\1\110",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\112\37\uffff\1\112",
            "\1\113\37\uffff\1\113",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\116\37\uffff\1\116",
            "",
            "\1\117\37\uffff\1\117",
            "",
            "\1\120\37\uffff\1\120",
            "\1\121\37\uffff\1\121",
            "",
            "\12\27\7\uffff\22\27\1\123\7\27\4\uffff\1\27\1\uffff\22\27"+
            "\1\123\7\27",
            "",
            "\1\124\37\uffff\1\124",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "",
            "\1\126\37\uffff\1\126",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\130\37\uffff\1\130",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "\1\133\37\uffff\1\133",
            "",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            "",
            "",
            "\1\136\37\uffff\1\136",
            "",
            "",
            "\1\137\37\uffff\1\137",
            "\12\27\7\uffff\32\27\4\uffff\1\27\1\uffff\32\27",
            ""
    };

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( INSERT | INTO | VALUE_SYM | VALUES | TRUE_SYM | FALSE_SYM | NULL_SYM | UPDATE | SET_SYM | WHERE | IS_SYM | AND_SYM | DELETE_SYM | FROM | TIMESTAMP | STRING | INT | FLOAT | BIT_NUM | HEX_DIGIT | DOT | RPAREN | LPAREN | COMMA | EQ_SYM | TRI_COLON | ID | WS );";
        }
    }
 

}