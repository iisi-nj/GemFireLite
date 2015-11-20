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
package gemlite.core.internal.testing.generator.sequence;

import gemlite.core.internal.testing.generator.action.OutputAction;
import gemlite.core.internal.testing.generator.action.impl.DeleteNewKeyAction;
import gemlite.core.internal.testing.generator.action.impl.DeleteReuseKeyAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DelSqlSequence implements Iterable<OutputAction>
{
	private List<OutputAction> sqlList;
	
	public DelSqlSequence()
	{
		sqlList = new ArrayList<OutputAction>();
		sqlList.add(new DeleteReuseKeyAction());
		sqlList.add(new DeleteReuseKeyAction());
		sqlList.add(new DeleteNewKeyAction());
	}
	
	@Override
	public Iterator<OutputAction> iterator()
	{
		return sqlList.iterator();
	}

}
