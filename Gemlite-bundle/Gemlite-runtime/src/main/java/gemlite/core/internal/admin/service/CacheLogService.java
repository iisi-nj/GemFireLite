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
package gemlite.core.internal.admin.service;

import gemlite.core.annotations.admin.AdminService;
import gemlite.core.internal.admin.AbstractRemoteAdminService;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;

import java.util.Map;
import java.util.logging.Level;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
/**
 * (о9До9log4jMnH
 * @author gsong
 */
@AdminService(name = "CacheLogService")
public class CacheLogService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    Cache cache = CacheFactory.getAnyInstance();    
    String level = (String)args.get("CACHE_LOGLEVEL");
    boolean sucess = true;
    //sн
    if (Level.OFF.getName().equals(level))
    {
      cache.getLogger().getHandler().setLevel(Level.OFF);
    }
    else if (Level.SEVERE.getName().equals(level))
    {
      cache.getLogger().getHandler().setLevel(Level.SEVERE);
    }
    else if (Level.WARNING.getName().equals(level))
    {
      cache.getLogger().getHandler().setLevel(Level.WARNING);
    }
    else if (Level.INFO.getName().equalsIgnoreCase(level))
    {
      cache.getLogger().getHandler().setLevel(Level.INFO);
    }
    else if (Level.FINE.getName().equals(level))
    {
      cache.getLogger().getHandler().setLevel(Level.FINE);
    }
    else if (Level.FINER.getName().equals(level))
    {
      cache.getLogger().getHandler().setLevel(Level.FINER);
    }
    else if (Level.FINEST.getName().equals(level))
    {
      cache.getLogger().getHandler().setLevel(Level.FINE);
    }
    else if(Level.CONFIG.getName().equalsIgnoreCase(level))
    {
      cache.getLogger().getHandler().setLevel(Level.CONFIG);
    }
    else
    {
      sucess = false;
    }
    return System.getProperty(ITEMS.BINDIP.name())+":"+System.getProperty(ITEMS.NODE_NAME.name())+" "+level+" "+sucess+"\n";
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
    // TODO Auto-generated method stub
    
  }
}


