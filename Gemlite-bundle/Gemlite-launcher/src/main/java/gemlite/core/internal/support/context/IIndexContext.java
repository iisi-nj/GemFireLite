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
package gemlite.core.internal.support.context;

import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;

import java.net.URL;
import java.util.Map;


/***
 * 
 * @author johnson
 * 
 */
public abstract class IIndexContext implements IModuleContext
{
  public abstract String getIndexName();
  
  public abstract String getIndexDef();
  
  public abstract Object getIndexInstance(boolean test);
  
  public abstract Object getIndexData(boolean test);
  
  public abstract void setIndexData(Object data, boolean test);
  
  public abstract Object getIndexInstance();
  
  public abstract void setIndexInstance(Object instance, boolean test);
  
  public abstract Object getIndexData();
  
  public abstract void setIndexData(Object data);
  
  public abstract String getRegionName();
  
  public abstract void setRegionName(String regionName);
  
  public abstract boolean isRegion();
  
  public abstract void setRegionType(boolean isRegion);
  
  public abstract Object getMbean();
  
  public abstract void setMbean(Object mbean);
  
  public abstract void close();
  

  @Override
  public URL getJarURL()
  {
		return null;
  }

  @Override
  public GemliteSibingsLoader getLoader()
  {
	  return null;
  }

  @Override
  public String getModuleName()
  {
		return null;
  }

  @Override
  public ModuleType getModuleType()
  {
		return null;
  }

  @Override
  public boolean isCoreModule()
  {
		return false;
  }

  @Override
  public Map<String, IGemliteRegistry> getRegistryCache()
  {
		return null;
  }

  @Override
  public void updateRegistryCache(Map<String, IGemliteRegistry> cache)
  {
		
  }

  @Override
  public IGemliteRegistry getRegistry(Class<?> cls)
  {
		return null;
  }

  @Override
  public void clean()
  {
		
  }
  
}
