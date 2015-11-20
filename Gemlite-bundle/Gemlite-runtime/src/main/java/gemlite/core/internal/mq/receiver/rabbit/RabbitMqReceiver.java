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
package gemlite.core.internal.mq.receiver.rabbit;

import gemlite.core.common.DateUtil;
import gemlite.core.internal.mq.MqConstant;
import gemlite.core.internal.mq.MqReceiveException;
import gemlite.core.internal.mq.MqShellParameter;
import gemlite.core.internal.mq.processor.IMessageProcessor;
import gemlite.core.internal.mq.receiver.HAMqReceiver;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author ynd
 * 
 */
public class RabbitMqReceiver extends HAMqReceiver
{

  private MqShellParameter param;
  private ConnectionFactory factory;
  private Connection connection;
  private Channel channel;
  
  private Address[] address;
  
  private QueueingConsumer consumer;
  private long deliveryTag = -1;
  private long reconnectTimeout = 10 * 1000;
  
  public RabbitMqReceiver(MqShellParameter param, IMessageProcessor processor)
  {
    super(processor);
    this.param = param;
  }
  
  public MqShellParameter getParam()
  {
    return  param;
  }
  
  private void checkWorkable()
  {
    Assert.notNull(getParam());
    Assert.notNull(getParam().getQueue());
  }
  
  protected void initRecv()
  {
    checkWorkable();
    
    String[] hpArr = getParam().getHostsAndPorts().split(",");
    address = new Address[hpArr.length];
    
    for (int i = 0; i < address.length; i++)
    {
      address[i] = new Address(hpArr[i].split(":")[0], Integer.parseInt(hpArr[i].split(":")[1]));
    }
    
    factory = new ConnectionFactory();
    factory.setUsername(getParam().getUserName());
    factory.setPassword(getParam().getPassward());
    factory.setVirtualHost(getParam().getVhost());
    try
    {
      createConnect();
    }
    catch (IOException e)
    {
      LogUtil.getMqSyncLog().error("Create connect failure.", e);
    }
    LogUtil.getMqSyncLog().info(" Connection and Channel Create Complete. ");
  }
  
  private void createConnect() throws IOException
  {
    connection = factory.newConnection(address);
    channel = connection.createChannel();
    channel.basicQos(getParam().getBatchSize());
    consumer = new QueueingConsumer(channel);
    channel.basicQos(getParam().getBatchSize());
  }
  
  @Override
  protected void doClose()
  {
    LogUtil.getMqSyncLog().info(" MQ Client closing ... ");
    if (channel.isOpen())
    {
      try
      {
        channel.close();
      }
      catch (IOException e)
      {
      }
    }
    if (connection.isOpen())
    {
      try
      {
        connection.close(5000);
      }
      catch (IOException e)
      {
      }
    }
    LogUtil.getMqSyncLog().info(" MQ Client closed. ");
  }
  
  
  @Override
  public void connect()
  {
    initRecv();
  }
  
  /***
   * getParam().getWaitTime() öào`:zˆÑ5^Öˆ
   * 
   */
  @Override
  public String readOneMessage()
  {
    try
    {
      channel.basicConsume(getParam().getQueue(), false, consumer);
      QueueingConsumer.Delivery delivery = consumer.nextDelivery(getParam().getWaitTime());
      if (delivery != null)
      {
        deliveryTag = delivery != null ? delivery.getEnvelope().getDeliveryTag() : deliveryTag;
        String msg = getMessageContent(delivery);
        return msg;
      }
      else
        return null;
    }
    catch (IOException | ShutdownSignalException | ConsumerCancelledException | InterruptedException e)
    {
      throw new MqReceiveException(e);
    }
  }
  
  /***
   * check if batchSize reached
   * 
   * @throws IOException
   */
  protected void doCommit() throws MqReceiveException
  {
    if (deliveryTag > 0)
    {
      try
      {
        channel.basicAck(deliveryTag, true);
      }
      catch (IOException e)
      {
        throw new MqReceiveException("deliveryTag:"+deliveryTag,e);
      }
      deliveryTag = -1;
    }
  }
  
  protected String getMessageContent(QueueingConsumer.Delivery delivery)
  {
    StringBuffer msgBuf = new StringBuffer();
    long timestamp = getTimestamp(delivery);
    if (timestamp > 0)
      msgBuf.append("\n" + MqConstant.TIMESTAMP + ":::" + timestamp + "\n");
    String message = getConent(delivery);
    msgBuf.append(message);
    // ∞Usql
    if (LogUtil.getSqllog().isInfoEnabled())
      LogUtil.getSqllog().info(msgBuf.toString());
    return msgBuf.toString();
  }
  
  /***
   * ÷àoÖπ
   * 
   * @getParam() delivery
   * @return
   */
  private String getConent(QueueingConsumer.Delivery delivery)
  {
    byte[] body = delivery.getBody();
    String encodedStr = "";
    String message = "";
    if (!getParam().isBase64Used())
    {
      try
      {
        message = new String(body,getParam().getEncoding());
      }
      catch (Exception e)
      {
        LogUtil.getMqSyncLog().error(
            "decodeFromBASE64 Error for the string: " + new String(body) + "with CharSet " + getParam().getEncoding(),
            e);
      }
    }
    else
    {
      encodedStr = Util.encodeToBASE64(body);
      message = Util.decodeFromBASE64(encodedStr, getParam().getEncoding());
    }
    
    message = StringUtils.lowerCase(message);
    message = message.replaceAll("\"|''", "");
    if (message.endsWith(";"))
      message = message.substring(0, message.length() - 1);
    return message;
  }
  
  /***
   * „ê timestamp
   * Âàoheader:,÷ótimestamp<
   * 
   * @getParam() delivery
   * @return
   */
  private long getTimestamp(QueueingConsumer.Delivery delivery)
  {
    BasicProperties prop = delivery.getProperties();
    Map<String, Object> headers = prop.getHeaders();
    long timestamp = -1;
    if (headers != null)
    {
      if (LogUtil.getMqSyncLog().isDebugEnabled())
        LogUtil.getMqSyncLog().debug(headers.toString());
      
      Object obj = headers.get(MqConstant.TIMESTAMP);
      
      if (obj != null)
      {
        if (LogUtil.getMqSyncLog().isDebugEnabled())
          LogUtil.getMqSyncLog().debug(obj.toString());
        
        Date dt = DateUtil.parse(obj.toString());
        if (dt != null)
          timestamp = dt.getTime();
        
        if (LogUtil.getMqSyncLog().isDebugEnabled())
          LogUtil.getMqSyncLog().debug("Timestamp:" + timestamp);
        timestamp = 0;
      }
    }
    return timestamp;
  }
  
  @Override
  public void reConnect()
  {
    LogUtil.getMqSyncLog().info(" MQ Client reConnecting ... ");
    while (true)
    {
      try
      {
        close();
        createConnect();
        LogUtil.getMqSyncLog().info(" MQ Client reConnected. ");
        return;
      }
      catch (IOException e)
      {
        connection.abort(5000);
        LogUtil.getMqSyncLog().info("Wait 10s to retry the connection");
        try
        {
          Thread.sleep(reconnectTimeout);
        }
        catch (InterruptedException e1)
        {
          LogUtil.getMqSyncLog().warn("Wake up less 10000 seconds!");
        }
      }
    }
  }
  
}
