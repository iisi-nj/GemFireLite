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
package gemlite.core.internal.testing.processor.mq;

import gemlite.core.internal.mq.MqReceiverBuilder;
import gemlite.core.internal.mq.receiver.HAMqReceiver;
import gemlite.core.internal.support.context.DomainMapperHelper;
import gemlite.core.internal.testing.processor.ITestProcessor;
import gemlite.core.util.LogUtil;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MqToGFTestProcessor extends ITestProcessor
{
	private String[] param;
	public MqToGFTestProcessor(String[] args)
	{
		param = args;
	}
	
	@Override
	public void open()
	{

	}

	@Override
	public void process()
	{
	    try
	    {
	      //ServerConfigHelper.initConfig("env.properties");
	      //ServerConfigHelper.initLog4j("classpath:log4j-mq.xml");
	      HAMqReceiver receiver = MqReceiverBuilder.createHAMqClient();
	      if(LogUtil.getMqSyncLog().isInfoEnabled())
	        LogUtil.getMqSyncLog().info("Created receiver:"+receiver);
	      boolean bl = receiver.parseParameter(param);
	      if (!bl)
	      {
	        System.err.println("error param:" + param);
	        return;
	      }
	      String[] resources = {receiver.getParam().getConfigXml() };
	      ClassPathXmlApplicationContext mainContext = new ClassPathXmlApplicationContext(resources, false);
	      mainContext.setValidating(true);
	      mainContext.refresh();
	      
	      DomainMapperHelper.scanMapperRegistryClass(Thread.currentThread().getContextClassLoader());
	      receiver.start();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      System.exit(-1);
	    }
	}

	@Override
	public void close()
	{

	}

}
