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
package gemlite.core.internal.mq.receiver;

import gemlite.core.internal.mq.MqReceiveException;
import gemlite.core.internal.mq.ShellParameter;
import gemlite.core.internal.mq.processor.IMessageProcessor;
import gemlite.core.util.LogUtil;

import java.util.Scanner;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.springframework.util.Assert;

/**
 * @author ynd
 * 
 */
public abstract class HAMqReceiver implements IMqReceiver
{
  protected boolean stop = false;
  protected IMessageProcessor processor;
  
  public HAMqReceiver(IMessageProcessor processor)
  {
    this.processor = processor;
  }
  
  protected void open()
  {
    Assert.notNull(getParam());
    connect();
    LogUtil.getAppLog().info("Mq connection opened.");
  }
  
  protected abstract void doClose();
  
  protected abstract String readOneMessage();
  
  protected abstract void doCommit();
  
  public abstract ShellParameter getParam();
  
  public void close()
  {
    LogUtil.getAppLog().info(" MQ Client closing ... ");
    doClose();
    LogUtil.getAppLog().info(" MQ Client closed. ");
  }
  
  public boolean parseParameter(String[] args)
  {
    if (LogUtil.getMqSyncLog().isInfoEnabled())
      LogUtil.getMqSyncLog().info("Parse input args to ShellParameter...");
    CmdLineParser parser = new CmdLineParser(getParam());
    try
    {
      parser.parseArgument(args);
      boolean valid = getParam().checkValid();
      if (LogUtil.getMqSyncLog().isInfoEnabled())
        LogUtil.getMqSyncLog().info("Parameter valid?" + valid + ",parameter is:\n" + getParam());
      return valid;
    }
    catch (CmdLineException e)
    {
      e.printStackTrace();
    }
    return false;
    
  }
  
  public void start()
  {
    open();
    try
    {
      while (true)
      {
        try
        {
          startReceiveMessages();
        }
        catch (MqReceiveException e)
        {
          LogUtil.getMqSyncLog().error("IOException and Reason is ", e);
          LogUtil.getMqSyncLog().info("Reconnecting ...");
          if (e.isShouldStop())
          {
            
          }
          else if (e.isNeedReconnect())
          {
            reConnect();
          }
        }
      }
    }
    finally
    {
      close();
    }
  }
  
  /***
   * d^�s��60\b��d����Mq server�ֈo
   * lb��GemfireƤ
   * �o���n batchSize��n�:ay�
   * �batchSize>1�(y!�Фa�
   * 1.�6pϾ0y!p
   * 2.����0
   * 
   * @throws MqReceiveException
   */
  protected void startReceiveMessages() throws MqReceiveException
  {
    Scanner scan = new Scanner(System.in);
    int recvCount = 0;
    //   !�60�o���
    long lastMsgTimestamp = System.currentTimeMillis();
    while (!stop)
    {
      long t1 = System.currentTimeMillis();
      String msg = readOneMessage();
      long t2 = System.currentTimeMillis();
      if (LogUtil.getMqSyncLog().isDebugEnabled())
        LogUtil.getMqSyncLog().debug("read one message,cost:" + (t2 - t1)  + " recvCount:" + recvCount);
      // ��waitTime
      long waitTime = t2 - lastMsgTimestamp;
      if (msg != null)
      {
        lastMsgTimestamp = t2;
        // �o
        processor.parserOneMessage(msg);
        recvCount++;
        long t3 = System.currentTimeMillis();
        
        if (LogUtil.getMqSyncLog().isDebugEnabled())
          LogUtil.getMqSyncLog().debug("parsed received message,cost:" + (t3 - t2) + " recvCount:" + recvCount);
        if (getParam().isDebug())// debug must be single step
        {
          System.out.println("Press Any Key to continue");
          scan.nextLine();
        }
      }
      // 1.recvCount>0 �6�ra0�o
      // 2.cost >= param.getWaitTime() ,��
      // 3.recvCount == param.getBatchSize() �0y!p�
      if (LogUtil.getMqSyncLog().isDebugEnabled())
      {
        LogUtil.getMqSyncLog().debug(
            "Threshold check, batchSize vs recvCount: (" + getParam().getBatchSize() + " vs " + recvCount
                + ") waitTime vs used: (" + getParam().getWaitTime() + " vs " + waitTime+")");
      }
      if (recvCount > 0 && ((waitTime >= getParam().getWaitTime()) || recvCount == getParam().getBatchSize()))
      {
        processor.remoteProcess();
        doCommit();
        recvCount = 0;
      }
    }
    scan.close();
  }
  
}
