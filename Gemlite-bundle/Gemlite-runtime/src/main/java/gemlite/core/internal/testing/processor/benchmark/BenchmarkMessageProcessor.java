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
package gemlite.core.internal.testing.processor.benchmark;

import gemlite.core.internal.mq.domain.MqSyncDomain;
import gemlite.core.internal.mq.domain.ParseredValue;
import gemlite.core.internal.mq.parser.ITableParser;
import gemlite.core.internal.mq.processor.IMapperDao;
import gemlite.core.internal.mq.processor.IMessageProcessor;
import gemlite.core.internal.mq.sender.IDomainSender;

import org.apache.commons.lang.StringUtils;

public class BenchmarkMessageProcessor implements IMessageProcessor
{
	private ITableParser parser;
	private IMapperDao dao;
	private IDomainSender sender;

	
	public BenchmarkMessageProcessor(ITableParser parser, IMapperDao dao, IDomainSender sender)
	{
	    this.parser = parser;
	    this.dao = dao;
	    this.sender = sender;
	}
	
	@Override
	public void remoteProcess()
	{

	}

	@Override
	public void parserOneMessage(String msg)
	{
		ParseredValue pv = parser.doParser(msg);
		if (pv == null)
		{
			if (!StringUtils.isBlank(msg))
			{
				System.out.println("Parse sql errror:" + msg);
			}
			return;
		}
		MqSyncDomain md = dao.mapperPV(pv);
		sender.doSend(md);
	}

}
