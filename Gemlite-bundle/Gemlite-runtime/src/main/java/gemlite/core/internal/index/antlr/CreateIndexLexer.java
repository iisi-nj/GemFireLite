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


import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CreateIndexLexer extends Lexer {
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
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public CreateIndexLexer() {} 
    public CreateIndexLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CreateIndexLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g"; }

    // $ANTLR start "A_"
    public final void mA_() throws RecognitionException {
        try {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:29:13: ( 'a' | 'A' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:30:13: ( 'b' | 'B' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:31:13: ( 'c' | 'C' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:32:13: ( 'd' | 'D' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:33:13: ( 'e' | 'E' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:34:13: ( 'f' | 'F' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:35:13: ( 'g' | 'G' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:36:13: ( 'h' | 'H' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:37:13: ( 'i' | 'I' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:38:13: ( 'j' | 'J' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:39:13: ( 'k' | 'K' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:40:13: ( 'l' | 'L' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:41:13: ( 'm' | 'M' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:42:13: ( 'n' | 'N' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:43:13: ( 'o' | 'O' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:44:13: ( 'p' | 'P' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:45:13: ( 'q' | 'Q' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:46:13: ( 'r' | 'R' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:47:13: ( 's' | 'S' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:48:13: ( 't' | 'T' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:49:13: ( 'u' | 'U' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:50:13: ( 'v' | 'V' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:51:13: ( 'w' | 'W' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:52:13: ( 'x' | 'X' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:53:13: ( 'y' | 'Y' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:54:13: ( 'z' | 'Z' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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

    // $ANTLR start "CREATE"
    public final void mCREATE() throws RecognitionException {
        try {
            int _type = CREATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:56:8: ( C_ R_ E_ A_ T_ E_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:56:10: C_ R_ E_ A_ T_ E_
            {
            mC_(); 


            mR_(); 


            mE_(); 


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
    // $ANTLR end "CREATE"

    // $ANTLR start "INDEX"
    public final void mINDEX() throws RecognitionException {
        try {
            int _type = INDEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:57:7: ( I_ N_ D_ E_ X_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:57:9: I_ N_ D_ E_ X_
            {
            mI_(); 


            mN_(); 


            mD_(); 


            mE_(); 


            mX_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INDEX"

    // $ANTLR start "HASHINDEX"
    public final void mHASHINDEX() throws RecognitionException {
        try {
            int _type = HASHINDEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:58:11: ( H_ A_ S_ H_ I_ N_ D_ E_ X_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:58:13: H_ A_ S_ H_ I_ N_ D_ E_ X_
            {
            mH_(); 


            mA_(); 


            mS_(); 


            mH_(); 


            mI_(); 


            mN_(); 


            mD_(); 


            mE_(); 


            mX_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HASHINDEX"

    // $ANTLR start "RANGEINDEX"
    public final void mRANGEINDEX() throws RecognitionException {
        try {
            int _type = RANGEINDEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:59:12: ( R_ A_ N_ G_ E_ I_ N_ D_ E_ X_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:59:14: R_ A_ N_ G_ E_ I_ N_ D_ E_ X_
            {
            mR_(); 


            mA_(); 


            mN_(); 


            mG_(); 


            mE_(); 


            mI_(); 


            mN_(); 


            mD_(); 


            mE_(); 


            mX_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RANGEINDEX"

    // $ANTLR start "ON"
    public final void mON() throws RecognitionException {
        try {
            int _type = ON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:60:4: ( O_ N_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:60:6: O_ N_
            {
            mO_(); 


            mN_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ON"

    // $ANTLR start "FROM"
    public final void mFROM() throws RecognitionException {
        try {
            int _type = FROM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:61:6: ( F_ R_ O_ M_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:61:8: F_ R_ O_ M_
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

    // $ANTLR start "TO"
    public final void mTO() throws RecognitionException {
        try {
            int _type = TO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:62:4: ( T_ O_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:62:6: T_ O_
            {
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
    // $ANTLR end "TO"

    // $ANTLR start "USING"
    public final void mUSING() throws RecognitionException {
        try {
            int _type = USING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:63:7: ( U_ S_ I_ N_ G_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:63:9: U_ S_ I_ N_ G_
            {
            mU_(); 


            mS_(); 


            mI_(); 


            mN_(); 


            mG_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "USING"

    // $ANTLR start "ORDER"
    public final void mORDER() throws RecognitionException {
        try {
            int _type = ORDER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:64:8: ( O_ R_ D_ E_ R_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:64:10: O_ R_ D_ E_ R_
            {
            mO_(); 


            mR_(); 


            mD_(); 


            mE_(); 


            mR_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ORDER"

    // $ANTLR start "BY"
    public final void mBY() throws RecognitionException {
        try {
            int _type = BY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:65:4: ( B_ Y_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:65:6: B_ Y_
            {
            mB_(); 


            mY_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BY"

    // $ANTLR start "ASC"
    public final void mASC() throws RecognitionException {
        try {
            int _type = ASC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:66:5: ( A_ S_ C_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:66:7: A_ S_ C_
            {
            mA_(); 


            mS_(); 


            mC_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ASC"

    // $ANTLR start "DESC"
    public final void mDESC() throws RecognitionException {
        try {
            int _type = DESC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:67:6: ( D_ E_ S_ C_ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:67:8: D_ E_ S_ C_
            {
            mD_(); 


            mE_(); 


            mS_(); 


            mC_(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DESC"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:70:8: ( '(' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:70:10: '('
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

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:71:8: ( ')' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:71:10: ')'
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

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:72:5: ( '.' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:72:7: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:73:7: ( ',' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:73:9: ','
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

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:74:7: ( ':' )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:74:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COLON"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:76:5: ( ( '+' | '-' )? ( '0' .. '9' )+ )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:76:7: ( '+' | '-' )? ( '0' .. '9' )+
            {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:76:7: ( '+' | '-' )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='+'||LA1_0=='-') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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


            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:76:18: ( '0' .. '9' )+
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
            	    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:79:5: ( ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* ) )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:79:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            {
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:79:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:79:8: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:79:32: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0 >= '0' && LA3_0 <= '9')||(LA3_0 >= 'A' && LA3_0 <= 'Z')||LA3_0=='_'||(LA3_0 >= 'a' && LA3_0 <= 'z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:
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
            	    break loop3;
                }
            } while (true);


            }


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
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:82:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:82:9: ( ' ' | '\\t' | '\\r' | '\\n' )
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
        // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:8: ( CREATE | INDEX | HASHINDEX | RANGEINDEX | ON | FROM | TO | USING | ORDER | BY | ASC | DESC | LPAREN | RPAREN | DOT | COMMA | COLON | INT | ID | WS )
        int alt4=20;
        alt4 = dfa4.predict(input);
        switch (alt4) {
            case 1 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:10: CREATE
                {
                mCREATE(); 


                }
                break;
            case 2 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:17: INDEX
                {
                mINDEX(); 


                }
                break;
            case 3 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:23: HASHINDEX
                {
                mHASHINDEX(); 


                }
                break;
            case 4 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:33: RANGEINDEX
                {
                mRANGEINDEX(); 


                }
                break;
            case 5 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:44: ON
                {
                mON(); 


                }
                break;
            case 6 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:47: FROM
                {
                mFROM(); 


                }
                break;
            case 7 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:52: TO
                {
                mTO(); 


                }
                break;
            case 8 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:55: USING
                {
                mUSING(); 


                }
                break;
            case 9 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:61: ORDER
                {
                mORDER(); 


                }
                break;
            case 10 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:67: BY
                {
                mBY(); 


                }
                break;
            case 11 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:70: ASC
                {
                mASC(); 


                }
                break;
            case 12 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:74: DESC
                {
                mDESC(); 


                }
                break;
            case 13 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:79: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 14 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:86: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 15 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:93: DOT
                {
                mDOT(); 


                }
                break;
            case 16 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:97: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 17 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:103: COLON
                {
                mCOLON(); 


                }
                break;
            case 18 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:109: INT
                {
                mINT(); 


                }
                break;
            case 19 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:113: ID
                {
                mID(); 


                }
                break;
            case 20 :
                // D:\\Gemlite\\vmgemlite\\Gemlite-bundle\\Gemlite-runtime\\src\\main\\resources\\CreateIndex.g:1:116: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA4 dfa4 = new DFA4(this);
    static final String DFA4_eotS =
        "\1\uffff\13\22\10\uffff\4\22\1\44\2\22\1\47\1\22\1\51\6\22\1\uffff"+
        "\2\22\1\uffff\1\22\1\uffff\1\63\6\22\1\72\1\22\1\uffff\1\74\1\22"+
        "\1\76\2\22\1\101\1\uffff\1\102\1\uffff\1\103\1\uffff\2\22\3\uffff"+
        "\4\22\1\112\1\22\1\uffff\1\114\1\uffff";
    static final String DFA4_eofS =
        "\115\uffff";
    static final String DFA4_minS =
        "\1\11\1\122\1\116\2\101\1\116\1\122\1\117\1\123\1\131\1\123\1\105"+
        "\10\uffff\1\105\1\104\1\123\1\116\1\60\1\104\1\117\1\60\1\111\1"+
        "\60\1\103\1\123\1\101\1\105\1\110\1\107\1\uffff\1\105\1\115\1\uffff"+
        "\1\116\1\uffff\1\60\1\103\1\124\1\130\1\111\1\105\1\122\1\60\1\107"+
        "\1\uffff\1\60\1\105\1\60\1\116\1\111\1\60\1\uffff\1\60\1\uffff\1"+
        "\60\1\uffff\1\104\1\116\3\uffff\1\105\1\104\1\130\1\105\1\60\1\130"+
        "\1\uffff\1\60\1\uffff";
    static final String DFA4_maxS =
        "\1\172\1\162\1\156\2\141\2\162\1\157\1\163\1\171\1\163\1\145\10"+
        "\uffff\1\145\1\144\1\163\1\156\1\172\1\144\1\157\1\172\1\151\1\172"+
        "\1\143\1\163\1\141\1\145\1\150\1\147\1\uffff\1\145\1\155\1\uffff"+
        "\1\156\1\uffff\1\172\1\143\1\164\1\170\1\151\1\145\1\162\1\172\1"+
        "\147\1\uffff\1\172\1\145\1\172\1\156\1\151\1\172\1\uffff\1\172\1"+
        "\uffff\1\172\1\uffff\1\144\1\156\3\uffff\1\145\1\144\1\170\1\145"+
        "\1\172\1\170\1\uffff\1\172\1\uffff";
    static final String DFA4_acceptS =
        "\14\uffff\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\20\uffff\1\5\2"+
        "\uffff\1\7\1\uffff\1\12\11\uffff\1\13\6\uffff\1\6\1\uffff\1\14\1"+
        "\uffff\1\2\2\uffff\1\11\1\10\1\1\6\uffff\1\3\1\uffff\1\4";
    static final String DFA4_specialS =
        "\115\uffff}>";
    static final String[] DFA4_transitionS = {
            "\2\23\2\uffff\1\23\22\uffff\1\23\7\uffff\1\14\1\15\1\uffff\1"+
            "\21\1\17\1\21\1\16\1\uffff\12\21\1\20\6\uffff\1\12\1\11\1\1"+
            "\1\13\1\22\1\6\1\22\1\3\1\2\5\22\1\5\2\22\1\4\1\22\1\7\1\10"+
            "\5\22\4\uffff\1\22\1\uffff\1\12\1\11\1\1\1\13\1\22\1\6\1\22"+
            "\1\3\1\2\5\22\1\5\2\22\1\4\1\22\1\7\1\10\5\22",
            "\1\24\37\uffff\1\24",
            "\1\25\37\uffff\1\25",
            "\1\26\37\uffff\1\26",
            "\1\27\37\uffff\1\27",
            "\1\30\3\uffff\1\31\33\uffff\1\30\3\uffff\1\31",
            "\1\32\37\uffff\1\32",
            "\1\33\37\uffff\1\33",
            "\1\34\37\uffff\1\34",
            "\1\35\37\uffff\1\35",
            "\1\36\37\uffff\1\36",
            "\1\37\37\uffff\1\37",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\40\37\uffff\1\40",
            "\1\41\37\uffff\1\41",
            "\1\42\37\uffff\1\42",
            "\1\43\37\uffff\1\43",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\45\37\uffff\1\45",
            "\1\46\37\uffff\1\46",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\50\37\uffff\1\50",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\52\37\uffff\1\52",
            "\1\53\37\uffff\1\53",
            "\1\54\37\uffff\1\54",
            "\1\55\37\uffff\1\55",
            "\1\56\37\uffff\1\56",
            "\1\57\37\uffff\1\57",
            "",
            "\1\60\37\uffff\1\60",
            "\1\61\37\uffff\1\61",
            "",
            "\1\62\37\uffff\1\62",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\64\37\uffff\1\64",
            "\1\65\37\uffff\1\65",
            "\1\66\37\uffff\1\66",
            "\1\67\37\uffff\1\67",
            "\1\70\37\uffff\1\70",
            "\1\71\37\uffff\1\71",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\73\37\uffff\1\73",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\75\37\uffff\1\75",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\77\37\uffff\1\77",
            "\1\100\37\uffff\1\100",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\104\37\uffff\1\104",
            "\1\105\37\uffff\1\105",
            "",
            "",
            "",
            "\1\106\37\uffff\1\106",
            "\1\107\37\uffff\1\107",
            "\1\110\37\uffff\1\110",
            "\1\111\37\uffff\1\111",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\113\37\uffff\1\113",
            "",
            "\12\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( CREATE | INDEX | HASHINDEX | RANGEINDEX | ON | FROM | TO | USING | ORDER | BY | ASC | DESC | LPAREN | RPAREN | DOT | COMMA | COLON | INT | ID | WS );";
        }
    }
 

}
