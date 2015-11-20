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
package gemlite.core.internal.mq;

import gemlite.core.internal.mq.parser.AntlrTableParser;
import gemlite.core.internal.mq.parser.ITableParser;
import gemlite.core.internal.mq.processor.AsyncSqlProcessor;
import gemlite.core.internal.mq.processor.IMapperDao;
import gemlite.core.internal.mq.processor.SimpleMapperDao;
import gemlite.core.internal.mq.receiver.HAMqReceiver;
import gemlite.core.internal.mq.receiver.file.FileReceiver;
import gemlite.core.internal.mq.receiver.file.FileShellParameter;
import gemlite.core.internal.mq.receiver.rabbit.RabbitMqReceiver;
import gemlite.core.internal.mq.sender.GFDomainSender;
import gemlite.core.internal.mq.sender.IDomainSender;
import gemlite.core.internal.testing.processor.file.TFileReceiver;

public class MqReceiverBuilder
{
  public final static HAMqReceiver createHAMqClient()
  {
    MqShellParameter param = new MqShellParameter();
    IMapperDao dao = new SimpleMapperDao();
    ITableParser parser = new AntlrTableParser();
    IDomainSender sender = new GFDomainSender();
    AsyncSqlProcessor processor = new AsyncSqlProcessor(parser, dao, sender);
    HAMqReceiver receiver = new RabbitMqReceiver(param, processor);
    return receiver;
  }
  
  public final static HAMqReceiver createFileTestClient()
  {
    FileShellParameter param = new FileShellParameter();
    IMapperDao dao = new SimpleMapperDao();
    ITableParser parser = new AntlrTableParser();
    IDomainSender sender = new GFDomainSender();
    AsyncSqlProcessor processor = new AsyncSqlProcessor(parser, dao, sender);
    HAMqReceiver receiver = new FileReceiver(param, processor);
    return receiver;
  }
  
  public final static HAMqReceiver createFileClientForTesting()
  {
    FileShellParameter param = new FileShellParameter();
    IMapperDao dao = new SimpleMapperDao();
    ITableParser parser = new AntlrTableParser();
    IDomainSender sender = new GFDomainSender();
    AsyncSqlProcessor processor = new AsyncSqlProcessor(parser, dao, sender);
    HAMqReceiver receiver = new TFileReceiver(param, processor);
    return receiver;
  }
}
