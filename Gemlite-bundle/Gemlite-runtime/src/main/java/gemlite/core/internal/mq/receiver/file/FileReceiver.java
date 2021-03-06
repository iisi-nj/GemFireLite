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
package gemlite.core.internal.mq.receiver.file;

import gemlite.core.common.DateUtil;
import gemlite.core.internal.mq.MqConstant;
import gemlite.core.internal.mq.MqReceiveException;
import gemlite.core.internal.mq.ShellParameter;
import gemlite.core.internal.mq.processor.IMessageProcessor;
import gemlite.core.internal.mq.receiver.HAMqReceiver;
import gemlite.core.util.LogUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

/**
 * @author ynd
 * 
 */
public class FileReceiver extends HAMqReceiver
{
  private FileShellParameter param;
  
  private LineNumberReader lr;
  
  public FileReceiver(FileShellParameter param, IMessageProcessor processor)
  {
    super(processor);
    this.param = param;
  }
  
  @Override
  protected void doClose()
  {
    LogUtil.getMqSyncLog().info(" MQ Client closing ... ");
    LogUtil.getMqSyncLog().info(" MQ Client closed. ");
  }
  
  private String line;
  
  @Override
  public void connect()
  {
    try
    {
      lr = new LineNumberReader(new InputStreamReader(new FileInputStream(param.fileName), param.fileEncoding));
      line = readLine();
    }
    catch (UnsupportedEncodingException | FileNotFoundException e)
    {
      LogUtil.getMqSyncLog().error(param.fileName, e);
      throw new MqReceiveException(true);
    }
  }
  
  private String readLine()
  {
    try
    {
      return lr.readLine();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  /***
   * 一次读一个消息包
   * 一个消息包由一条或多条sql组成
   */
  @Override
  public String readOneMessage()
  {
    String sqlDate = "";
    StringBuffer buffer = new StringBuffer();
    // [16:34:32.781-16:34:32.781]update left_base_center set ticke
    // 解析时戳
    int i = line.indexOf("]");
    String s0 = line.substring(1, i);
    int k = s0.indexOf("-");
    String s1 = sqlDate + " " + s0.substring(0, k);
    long timestamp = DateUtil.parse(s1).getTime();
    
    buffer.append("\n" + MqConstant.TIMESTAMP + ":::" + timestamp + "\n");
    line = line.substring(i + 1);
    buffer.append(line).append("\n");
    line = readLine();
    while (line != null)
    {
      if (!StringUtils.isBlank(line))
      {
        if (line.startsWith("["))
          break;
        buffer.append(line).append("\n");
      }
      line = readLine();
    }
    
    return buffer.toString();
  }
  
  /***
   * check if batchSize reached
   * 
   * @throws IOException
   */
  protected void doCommit() throws MqReceiveException
  {
  }
  
  @Override
  public void reConnect()
  {
    
  }
  
  @Override
  public ShellParameter getParam()
  {
    return param;
  }
  
}
