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
package gemlite.core.api.func;

import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.GemliteIndexContext;
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.util.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;

public class DownloadServerClassFunction implements Function 
{
	private static final long serialVersionUID = -4504321381183556253L;

	@Override
	public boolean hasResult() 
	{
		return true;
	}

	@Override
	public void execute(FunctionContext functioncontext)
	{
		Map<String, byte[]> classBytesMapByModule = new HashMap<String, byte[]>();
		try
		{
			GemliteIndexContext idc = GemliteContext.getTopIndexContext();
			Iterator<IIndexContext> it = idc.getIndexContexts();
			while(it.hasNext())
			{
				IIndexContext context = it.next();
				if(context != null)
				{
					GemliteSibingsLoader loader = context.getLoader();
					if(loader != null)
					{
						Map<String, byte[]> classBytesMap = loader.getDynaimcClasses();
						if(classBytesMap != null)
							classBytesMapByModule.putAll(classBytesMap);
					}
				}
			}
		}
		catch (Exception e)
		{
			LogUtil.getCoreLog().error("", e);
		}
		
		functioncontext.getResultSender().lastResult(classBytesMapByModule);
	}

	@Override
	public String getId() 
	{
		return getClass().getName();
	}

	@Override
	public boolean optimizeForWrite() 
	{
		return false;
	}

	@Override
	public boolean isHA()
	{
		return false;
	}
}
