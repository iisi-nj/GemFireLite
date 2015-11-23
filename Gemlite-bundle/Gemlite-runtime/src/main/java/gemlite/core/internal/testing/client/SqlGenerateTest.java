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
package gemlite.core.internal.testing.client;

import gemlite.core.api.SimpleClient;
import gemlite.core.internal.support.context.DomainMapperHelper;
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
import gemlite.core.internal.testing.processor.file.FileToGFTestProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


/**
 * 测试工具类
 * 
 * 根据参数传入表名称、测试数据数量 、测试类别，工具自动生成相应的sql数据，
 * 并通过mq工具类解析sql并写入节点的local和region中
 * 再通过RegionCompare工具类对写入的local和region数据进行校对
 * 
 *
 */
public class SqlGenerateTest
{
	@Option(name = "-n")
	private String name;

	@Option(name = "-c")
	private int count;
	
	@Option(name = "-a", usage="insert:0, insert+update:1, insert+delete:2, insert+update+delete:3")
	private int action;	

	public static void main(String[] args) throws Exception
	{
		SimpleClient.connect();
		SqlGenerateTest scd = new SqlGenerateTest();
		CmdLineParser parser = new CmdLineParser(scd);
		parser.parseArgument(args);

		String result = scd.sqlGenerator();
		System.out.println(result);

		System.out.println("Table[" + scd.name + "]已测试完毕.");
		SimpleClient.disconnect();
		System.exit(0);
	}

	private String sqlGenerator()
	{
		name = "customer";
		count = 10000;
		action = 0;
		if (name==null || name.equals(""))
		{
			System.out.println("Shell params [-n] is a invalidate value.");
			return "";
		}
		if (count<=0){
			System.out.println("Shell params [-c] is a invalidate value.");
			return "";			
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();

		Map<String, Object> tableMap = new HashMap<String, Object>();
		
		if (action < 0 || action > 3)
			action = 0;
		
		tableMap.put("tableName", name);
		tableMap.put("count", count);
		tableMap.put("action", action);
		tableList.add(tableMap);

		paramsMap.put("tableNames", tableList);
		
		DomainMapperHelper.scanMapperRegistryClass();
		StringBuffer resultBuf = new StringBuffer();
		String result = this.run(tableMap, resultBuf);

		//String result = FunctionUtil.onServer(new SqlGeneratorFunc().getId(),
		//		paramsMap, String.class);

		return result;
	}
	
	private String run(Map<String, Object> tableMap,StringBuffer resultBuf)
	{
		// table name
		String tableName = (String) tableMap.get("tableName");
		// test data count
		int count = (Integer) tableMap.get("count");
		// action
		int action = (Integer) tableMap.get("action");
		OutputTemplate template = new SqlTemplate(tableName);

		Iterable<OutputAction> actionSequence = new InsUpdSqlSequence(action);
		ReusePool pool = new ReusePool();
		OutputWriter writer = new SqlWriter(tableName);
		SqlGenerator sg = new SqlGenerator(template, actionSequence, pool,
				writer);
		sg.generate(count);

		//if generator delete sql
		if (action == TableUtil.ACT_INS_UPD_DEL)
		{
			Iterable<OutputAction> deleteSequence = new DelSqlSequence();
			sg = new SqlGenerator(template, deleteSequence, pool, writer);
			sg.generate(count);
		}
		sg.closeWriter();
		String[] param = new String[] { "-fn", writer.getFile(), "-fe", "gb18030"};
		ITestProcessor srcResult = new BenchmarkTestProcessor(param);
		ITestProcessor desResult = new FileToGFTestProcessor(param);
		desResult.process();
		srcResult.process();
	
		return tableName;
	}
}
