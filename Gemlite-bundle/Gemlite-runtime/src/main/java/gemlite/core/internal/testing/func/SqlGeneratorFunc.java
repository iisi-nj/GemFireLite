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
package gemlite.core.internal.testing.func;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
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

import java.util.List;
import java.util.Map;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class SqlGeneratorFunc implements Function
{
	private static final long serialVersionUID = 7197258727410838188L;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(FunctionContext context)
	{
		if (context.getArguments() instanceof Map)
		{
			Map<String, Object> paramMap = (Map<String, Object>) context.getArguments();
			if (paramMap == null || paramMap.keySet().size() == 0)
			{
				context.getResultSender().lastResult(
						"Service params is null or is Empty, execute failed.");
				return;
			}
			// table's list
			StringBuffer resultBuf = new StringBuffer();
			List<Map<String, Object>> tablesList =(List<Map<String, Object>>) paramMap.get("tableNames");
			for (Map<String, Object> tableMap : tablesList)
			{
				String tableName = sqlGenerator(tableMap, resultBuf);
				//resultBuf.append("Table [").append(tableName).append("] generator sql success. /n");
			}
			context.getResultSender().lastResult(resultBuf.toString());
			return;

		} else
		{
			context.getResultSender().lastResult(
					"Service params is null or is Empty, execute failed.");
			return;
		}

	}

	private String sqlGenerator(Map<String, Object> tableMap,StringBuffer resultBuf)
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
		//ResultComparator comp = new MapComparator(DomainRegistry.tableToRegion(tableName));
		desResult.process();
		srcResult.process();
		
		//boolean compareResult = comp.compare();
		//if (compareResult)
		//{
		resultBuf
				.append(getHostIPAndName())
				.append(" Table [")
				.append(tableName)
				.append("] The local data and the region data has been generated. \n");
		//}else{
		//	resultBuf.append("Table [").append(tableName).
		//	append("] The local data and the region data is not matched. /n");		
		//}
		

		return tableName;
	}

	@Override
	public String getId()
	{
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean hasResult()
	{
		return true;
	}

	@Override
	public boolean isHA()
	{
		return false;
	}

	@Override
	public boolean optimizeForWrite()
	{
		return false;
	}
	
	private String getHostIPAndName()
	{
		String memberId = ServerConfigHelper.getProperty("MEMBER_ID");
		String ip = ServerConfigHelper.getConfig(ITEMS.BINDIP);
		String serverName = ServerConfigHelper.getConfig(ITEMS.NODE_NAME);
		String port = ServerConfigHelper.getProperty("JMX_RMI_PORT");
		return serverName + "/" + ip + "(" + memberId + "/" + port + ") ";
	}

}
