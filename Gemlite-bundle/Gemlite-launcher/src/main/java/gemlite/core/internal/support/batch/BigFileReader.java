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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.channels.FileChannel;

public class BigFileReader extends BufferedReader
{
  public BigFileReader(Reader in, int sz, FileChannel channel, String lastLine, long limit) throws IOException
  {
    super(in, sz);
    this.channel = channel;
    this.lastLine = lastLine;
    this.limit = limit;
  }
  
  private FileChannel channel;
  private String lastLine;
  private long limit = 0;
  
  @Override
  public String readLine() throws IOException
  {
    String line = super.readLine();
//    LogUtil.getAppLog().info("channel.position ["+channel.position()+"] limit ["+limit+"]");
    if (line!=null && limit > 0 && channel.position() > limit)
    {
//      LogUtil.getAppLog().info("reach limit,check end ["+line+"]");
      if (line.equals(lastLine))
        return null;
    }
    return line;
  }
  
}
