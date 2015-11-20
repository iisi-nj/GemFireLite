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
package gemlite.core.internal.support.hotdeploy.scanner;

import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;

public class GemliteIndexClassScanner extends GemliteClassScannerPro
{
	@Override
	public IModuleContext createModuleContext(GemliteSibingsLoader loader, RegistryMatchedContext mcContext)
	{
		String tuple = mcContext.getModuleName();
		String indexName = tuple.split(":")[0];
	    String indexDef =	tuple.split(":")[1];
	    IModuleContext module = super.getIdxContextCreator().createIndexContext(indexName, indexDef, loader);
	    return module;
	}

	@Override
	protected void processDefineConfigure(GemliteSibingsLoader loader, RegistryDefineContext defContext,
		      RegistryMatchedContext mcContext)
	{
		
	}
}
