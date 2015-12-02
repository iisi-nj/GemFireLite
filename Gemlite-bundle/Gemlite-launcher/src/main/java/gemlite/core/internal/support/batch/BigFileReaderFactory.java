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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.springframework.batch.item.file.DefaultBufferedReaderFactory;
import org.springframework.core.io.Resource;

public class BigFileReaderFactory extends DefaultBufferedReaderFactory
{
  private int bufferSize = 1024 * 512;
  private long position = 0;
  private long limit = 0;
  private int lineSize=1024*100;
  //指定列数
  private int columns = 0;
  
  @Override
  public BufferedReader create(Resource resource, String encoding) throws UnsupportedEncodingException, IOException
  {
    if(columns == 0)
      throw new IOException("please set columns !");
    FileInputStream fi = new FileInputStream(resource.getFile());
    Charset charset = Charset.forName(encoding);
    CharsetDecoder decoder = charset.newDecoder();
    FileChannel fc = (FileChannel) Channels.newChannel(fi);
    fc.position(limit);
    Reader reader = Channels.newReader(fc, decoder, lineSize);
    BufferedReader br = new BufferedReader(reader);
    
    //处理跳过行
    br = processLastLineSkip(fc, reader, decoder, br);
    
    //确定街接的行,测试数据按2个字段来算的,这个也不行啊
    String lastLine = br.readLine();
    CSVParser parser = new CSVParser();
    String arr[] = parser.parseLineMulti(lastLine);
    while(parser.isPending() || arr.length < columns)
    {
        lastLine = br.readLine();
        parser = new CSVParser();
        arr = parser.parseLineMulti(lastLine);
    }
    
    FileChannel channel = (FileChannel) Channels.newChannel(fi);
    channel.position(position);
    Reader ioReader = Channels.newReader(channel, decoder, bufferSize);
    BigFileReader bfreader = new BigFileReader(ioReader, bufferSize, channel, lastLine, limit);
    String str ="";
    if(position>0)
    {
      BigFileReader bfreader1 = processFirstLineSkip(bfreader, channel, ioReader, fi, lastLine, decoder);
      bfreader = bfreader1;
    }
    else
    LogUtil.getAppLog().info("Create reader,start batchfile. postion="+position+" skip str:"+str+" thread:"+Thread.currentThread().getName()+" lastline["+lastLine+"] limit["+limit+"]");
    return bfreader;
  }
  
  private BufferedReader processLastLineSkip(FileChannel fc,Reader reader,CharsetDecoder decoder,BufferedReader br) throws IOException
  {
    boolean stop = false;
    int times = 0;
    while(!stop)
    {
      times++;
      try
      {
        String str = br.readLine();
        LogUtil.getAppLog().info("skip line:"+str);
      }
      catch(Exception e)
      {
        fc.position(limit+times);
        reader = Channels.newReader(fc, decoder, lineSize);
        br = new BufferedReader(reader);
        LogUtil.getAppLog().warn("skip error line:"+e.getMessage());
        continue;
      }
      stop = true;
    }
    return br;
  }
  
  /**
   * 将读写头调至准确的第一行
   * @param bfreader
   */
  private BigFileReader processFirstLineSkip(BigFileReader bfreader,FileChannel channel,Reader ioReader,FileInputStream fi,String lastLine,CharsetDecoder decoder) throws IOException
  {
    int i = 1;
    String str = "";
    //抛弃的字段,对这些字段进行验证,如果是完整的则直接抛弃,否则接着读下面的
    boolean stop = false;
    int times = 0;
    while(!stop)
    {
      times++;
      try
      {
        str =bfreader.readLine();
        LogUtil.getAppLog().info("skip line:"+str);
      }
      catch(Exception e)
      {
        channel.position(position+times);
        ioReader = Channels.newReader(channel, decoder, lineSize);
        bfreader = new BigFileReader(ioReader, bufferSize, channel, lastLine, limit);
        LogUtil.getAppLog().warn("skip error line:"+e.getMessage());
        continue;
      }
      stop = true;
    }
    
    CSVParser parser = new CSVParser();
    String arr[] = parser.parseLineMulti(str);
    //这里面要判断一下字段数量
    while(parser.isPending() || arr.length < columns)
    {
      str =bfreader.readLine();
      //TODO 这里为什么还要重新new一个? add 2014.07.07 by gsong 注意测试时去掉下面一行试试
      parser = new CSVParser();
      arr = parser.parseLineMulti(str);
      i++;
    }
    
    //找到完整的skip行之后,重新回滚
    if(i>1)
    {
      channel = (FileChannel) Channels.newChannel(fi);
      channel.position(position);
      ioReader = Channels.newReader(channel, decoder, bufferSize);
      bfreader = new BigFileReader(ioReader, bufferSize, channel, lastLine, limit);
      //需要读i-1次
      str="";
      while(i>1)
      {
        stop = false;
        times = 0;
        while(!stop)
        {
          times++;
          try
          {
            str+=bfreader.readLine();
          }
          catch(Exception e)
          {
            channel.position(position+times);
            ioReader = Channels.newReader(channel, decoder, lineSize);
            bfreader = new BigFileReader(ioReader, bufferSize, channel, lastLine, limit);
            LogUtil.getAppLog().warn("skip error line:"+e.getMessage());
            continue;
          }
          stop = true;
        }
        i--;
      }
    }
    LogUtil.getAppLog().info("Create reader,start batchfile. postion="+position+" skip str:"+str+" thread:"+Thread.currentThread().getName()+" lastline["+lastLine+"] limit["+limit+"]");
    return bfreader;
  }
  
  public int getBufferSize()
  {
    return bufferSize;
  }
  
  public void setBufferSize(int bufferSize)
  {
    this.bufferSize = bufferSize;
  }
  
  public long getPosition()
  {
    return position;
  }
  
  public void setPosition(long position)
  {
    this.position = position;
  }
  
  public long getLimit()
  {
    return limit;
  }
  
  public void setLimit(long limit)
  {
    this.limit = limit;
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
