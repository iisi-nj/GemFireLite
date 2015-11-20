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
package shell;

import gemlite.core.api.SimpleClient;
import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.mq.MqReceiverBuilder;
import gemlite.core.internal.mq.receiver.HAMqReceiver;
import gemlite.core.internal.support.context.DomainMapperHelper;

public class MqClient
{

	public static void main(String[] args)
	{
		String[] params = new String[] { "-h", "192.168.120.54:5672", "-v","connfactory","-u",
				"admin", "-p", "golden","-q","order" };
		MqClient client = new MqClient();
		client.start(params);

	}

	private void start(String[] args)
	{
		SimpleClient.connect();
		DomainMapperHelper.scanMapperRegistryClass();
		String tableName = DomainRegistry.regionToTable("customer");
		System.out.println(tableName);
		HAMqReceiver receiver = MqReceiverBuilder.createHAMqClient();
		receiver.parseParameter(args);
		receiver.start();

		// SimpleClient.disconnect();

	}

}
