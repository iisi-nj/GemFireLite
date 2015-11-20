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
package gemlite.core.internal.testing.generator;

import gemlite.core.internal.testing.generator.action.OutputAction;
import gemlite.core.internal.testing.generator.pool.ReusePool;
import gemlite.core.internal.testing.generator.template.OutputTemplate;
import gemlite.core.internal.testing.generator.writer.OutputWriter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SqlGenerator
{
	private String tableName;
	private OutputTemplate template;
	private Iterable<OutputAction> actionSequence;
	private ReusePool pool;
	private OutputWriter writer;
	private Map<String, Integer> statistics;
	
	public SqlGenerator(OutputTemplate template, Iterable<OutputAction> actionSequence, ReusePool pool, OutputWriter writer)
	{
		this.template = template;
		this.actionSequence = actionSequence;
		this.pool = pool;
		this.writer = writer;
		statistics =  new HashMap<String, Integer>();
	}
	
	public boolean generate(int count)
	{
		for(int i=0; i<count; i++)
		{
			Iterator<OutputAction> it = actionSequence.iterator();
			while(it.hasNext())
			{
				OutputAction action = it.next();
				computeStats(action);
				String sql = (String) action.doAct(template, pool);
				writer.write(sql);
			}
		}
		writer.write("\n");
		//writer.close();
		
		return true;
	}
	
	public void closeWriter()
	{
		if (writer != null)
			writer.close();
	}
	
	private void computeStats(OutputAction action)
	{
		String type = action.getType();
		Integer count = statistics.get(type);
		if(count == null)
		{
			statistics.put(type, 1);
		}
		else
		{
			statistics.put(type, count++);
		}
	}
	
	public Map<String, Integer> getStats()
	{
		return statistics;
	}
}
