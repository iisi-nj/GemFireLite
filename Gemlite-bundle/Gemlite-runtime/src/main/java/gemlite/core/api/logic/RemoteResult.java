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
package gemlite.core.api.logic;

import gemlite.core.api.ApiConstant;
import gemlite.core.internal.logic.collectors.ListResultCollector;
import gemlite.core.internal.logic.collectors.MapResultCollector;
import gemlite.core.internal.logic.collectors.TypeResultCollector;

import java.util.List;
import java.util.Map;

import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.ResultCollector;

@SuppressWarnings("rawtypes")
public class RemoteResult {
	private Execution ex;

	public RemoteResult(Execution ex) {
		super();
		this.ex = ex;
	}

	public <T> T getResult(Class<T> T) {
		Object result = null;
		if (T.equals(List.class))
			result = getList();
		else if (T.equals(Map.class))
			result = getMap();
		else
			result = getTypedResult(T);

		if (result != null)
			return T.cast(result);

		return null;
	}

	public <T> T getTypedResult(Class<T> T) {

		ResultCollector rc= ex.withCollector(new TypeResultCollector<>()).execute(ApiConstant.REMOTE_SERVICE_FUNCTION);
		return T.cast(rc.getResult());
	}

	public Object getResult() {
		ResultCollector rc = ex.execute(ApiConstant.REMOTE_SERVICE_FUNCTION);
		return rc.getResult();
	}

	private List getList() {
		ResultCollector rc= ex.withCollector(new ListResultCollector()).execute(ApiConstant.REMOTE_SERVICE_FUNCTION);
		return (List)rc.getResult();
	}

	private Map getMap() {
		ResultCollector rc= ex.withCollector(new MapResultCollector()).execute(ApiConstant.REMOTE_SERVICE_FUNCTION);
		return (Map)rc.getResult();
	}

}
