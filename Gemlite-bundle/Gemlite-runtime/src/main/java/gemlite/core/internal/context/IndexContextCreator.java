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
package gemlite.core.internal.context;
import gemlite.core.internal.support.IIndexContextCreator;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;

public class IndexContextCreator implements IIndexContextCreator
{

	@Override
	public IIndexContext createIndexContext(String indexName, String indexDef,
			GemliteSibingsLoader loader)
	{
		return new IndexContextImpl(indexName, indexDef, loader);
	}

}
