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

import gemlite.core.internal.domain.DomainRegistry;
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
import gemlite.core.internal.testing.processor.file.FileToGFTestProcessor;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class AutoCompareForFile
{
	@Option(name = "-t", usage = "table name")
	public String tableName;
	
	@Option(name = "-n", usage = "package amount")
	public int count;
	
	private boolean run()
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
		
		
		String[] param = new String[] { "-fn", writer.getFile(), "-fe", "gb18030"};
		ITestProcessor srcResult = new BenchmarkTestProcessor(param);
		ITestProcessor desResult = new FileToGFTestProcessor(param);
		ResultComparator comp = new MapComparator(DomainRegistry.tableToRegion(tableName));
		srcResult.process();
		desResult.process();
		return comp.compare();
	}
	
	public static void main(String[] args) 
	{
		String[] param = new String[] { "-t", "Order", "-n", "1"};
		AutoCompareForFile tt = new AutoCompareForFile();
		CmdLineParser parser = new CmdLineParser(tt);
		try {
			parser.parseArgument(args==null?param:args);
			tt.run();
		}
		catch (CmdLineException e) 
		{
			e.printStackTrace();
		}
	}
}
