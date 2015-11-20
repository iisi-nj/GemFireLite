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
package gemlite.core.api.compare;

import gemlite.core.internal.testing.comparator.ResultComparator;
import gemlite.core.internal.testing.comparator.impl.MapComparator;
import gemlite.core.internal.testing.generator.SqlGenerator;
import gemlite.core.internal.testing.generator.action.OutputAction;
import gemlite.core.internal.testing.generator.pool.ReusePool;
import gemlite.core.internal.testing.generator.sequence.DelSqlSequence;
import gemlite.core.internal.testing.generator.sequence.InsUpdSqlSequence;
import gemlite.core.internal.testing.generator.template.OutputTemplate;
import gemlite.core.internal.testing.generator.template.impl.SqlTemplate;
import gemlite.core.internal.testing.generator.util.TableUtil;
import gemlite.core.internal.testing.generator.writer.OutputWriter;
import gemlite.core.internal.testing.generator.writer.impl.SqlWriter;
import gemlite.core.internal.testing.processor.ITestProcessor;
import gemlite.core.internal.testing.processor.benchmark.BenchmarkTestProcessor;
import gemlite.core.internal.testing.processor.mq.MqToGFTestProcessor;
import gemlite.core.internal.testing.processor.mq.RabbitMqSender;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class AutoCompareForMQ
{
	@Option(name = "-t", usage = "table name")
	public String tableName;
	
	@Option(name = "-n", usage = "package amount")
	public int count;
	
	@Option(name = "-h", usage = "rabbmitmq server host1:port1,[host2:port2]...")
	private String hostsAndPorts = "198.216.27.1:5672";
	  
	@Option(name = "-v", usage = "vhost ,default /")
	private String vhost = "/";
	  
	@Option(name = "-e", usage = "rabbitmq server exchange, default: amq.direct ")
	private String exchange = "amq.direct";
	
	@Option(name = "-r", usage = "rabbitmq server routekey")
	private String routeKey = "";
	
	@Option(name = "-q", usage = "queue name")
	private String queue = "";
	
	@Option(name = "-u", usage = "username:guest")
	private String userName = "guest";
	
	@Option(name = "-p", usage = "password:guest")
	private String password = "guest";
 
	@Option(name = "-encoding", usage = "default:gb18030")
	private String encoding = "gb18030";
	
	private boolean run(String[] args)
	{
		OutputTemplate template = new SqlTemplate(tableName);
		Iterable<OutputAction> actionSequence = new InsUpdSqlSequence(TableUtil.ACT_INS_UPD_DEL);
		ReusePool pool = new ReusePool();
		OutputWriter writer = new SqlWriter(tableName);
		SqlGenerator sg = new SqlGenerator(template, actionSequence, pool, writer);
		sg.generate(count);
		sg.closeWriter();
		
		Iterable<OutputAction> deleteSequence = new DelSqlSequence();
		SqlGenerator sg2 = new SqlGenerator(template, deleteSequence, pool, writer);
		sg2.generate(count);
		sg2.closeWriter();
		
		String[] hpArr = hostsAndPorts.split(",");
		SenderTask sender = new SenderTask(hpArr[0].split(":")[0], Integer.parseInt(hpArr[0].split(":")[1]), userName, password, vhost, exchange, routeKey,  writer.getFile(), encoding);
		sender.start();
		ITestProcessor srcResult = new BenchmarkTestProcessor(args);
		ITestProcessor desResult = new MqToGFTestProcessor(args);
		ResultComparator comp = new MapComparator(tableName);
		srcResult.process();
		desResult.process();
		return comp.compare();
	}
	
	public static void main(String[] args) 
	{
		AutoCompareForMQ tt = new AutoCompareForMQ();
		CmdLineParser parser = new CmdLineParser(tt);
		try {
			parser.parseArgument(args);
			tt.run(args);
		}
		catch (CmdLineException e) 
		{
			e.printStackTrace();
		}
	}
}

class SenderTask extends Thread
{
	private RabbitMqSender sender;
	public SenderTask(String host, int port, String user, String password, String vhost, String exchange, String routeKey, String fileName, String fileEnconding)
	{
		sender = new RabbitMqSender();
		sender.setInputSource(fileName, fileEnconding);
		sender.setExchange(exchange);
		sender.setHost(vhost);
		sender.setPort(port);
		sender.setRouteKey(routeKey);
	}

	@Override
	public void run()
	{
		sender.send();
	}
}
