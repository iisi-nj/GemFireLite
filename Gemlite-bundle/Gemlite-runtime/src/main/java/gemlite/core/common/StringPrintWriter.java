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
package gemlite.core.common;
 
 import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
 
 public class StringPrintWriter
   extends PrintWriter
 {
   private final StringBuilder sb;
   private static final Writer dummyLock = new StringWriter();
   private final String lineSep;
   
   public StringPrintWriter()
   {
     this(new StringBuilder(), null);
   }
   
   public StringPrintWriter(StringBuilder sb)
   {
     this(sb, null);
   }
   
   public StringPrintWriter(StringBuilder sb, String lineSep)
   {
     super(dummyLock, false);
     this.sb = sb;
     this.lineSep = (lineSep != null ? lineSep : "\n");
   }
   
   public void write(int c)
   {
     this.sb.append((char)c);
   }
   
   public void write(char[] cbuf)
   {
     this.sb.append(cbuf);
   }
   
   public void write(char[] cbuf, int off, int len)
   {
     if ((off < 0) || (off > cbuf.length) || (len < 0) || (off + len > cbuf.length) || (off + len < 0)) {
       throw new IndexOutOfBoundsException();
     }
     if (len == 0) {
       return;
     }
     this.sb.append(cbuf, off, len);
   }
   
   public void write(String str)
   {
     this.sb.append(str);
   }
   
   public void write(String str, int off, int len)
   {
     this.sb.append(str.substring(off, off + len));
   }
   
   public void print(boolean b)
   {
     this.sb.append(b);
   }
   
   public void print(int i)
   {
     this.sb.append(i);
   }
   
   public void print(long l)
   {
     this.sb.append(l);
   }
   
   public void print(float f)
   {
     this.sb.append(f);
   }
   
   public void print(double d)
   {
     this.sb.append(d);
   }
   
   public void print(char[] s)
   {
     this.sb.append(s);
   }
   
   public void print(String s)
   {
     this.sb.append(s);
   }
   
   public void print(Object obj)
   {
     this.sb.append(obj);
   }
   
   public void println()
   {
     this.sb.append(this.lineSep);
   }
   
   public void println(boolean b)
   {
     this.sb.append(b).append(this.lineSep);
   }
   
   public void println(int i)
   {
     this.sb.append(i).append(this.lineSep);
   }
   
   public void println(long l)
   {
     this.sb.append(l).append(this.lineSep);
   }
   
   public void println(float f)
   {
     this.sb.append(f).append(this.lineSep);
   }
   
   public void println(double d)
   {
     this.sb.append(d).append(this.lineSep);
   }
   
   public void println(char[] s)
   {
     this.sb.append(s).append(this.lineSep);
   }
   
   public void println(String s)
   {
     this.sb.append(s).append(this.lineSep);
   }
   
   public void println(Object obj)
   {
     this.sb.append(obj).append(this.lineSep);
   }
   
   public StringPrintWriter append(char c)
   {
     write(c);
     return this;
   }
   
   public StringPrintWriter append(CharSequence csq)
   {
     write(csq != null ? csq.toString() : "null");
     return this;
   }
   
   public StringPrintWriter append(CharSequence csq, int start, int end)
   {
     write(csq != null ? csq.subSequence(start, end).toString() : "null");
     return this;
   }
   
   public String toString()
   {
     return this.sb.toString();
   }
   
   public StringBuilder getBuilder()
   {
     return this.sb;
   }
   
   public void flush() {}
   
   public void close() {}
 }
