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
import gemlite.core.internal.admin.AdminUtil;
import gemlite.core.internal.admin.CleanExpire;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.util.Map;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.CustomExpiry;
import com.gemstone.gemfire.cache.Region;

@SuppressWarnings({ "unchecked", "rawtypes" })
@AdminService(name = "CleanService")
public class CleanService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    String regionName = (String) args.get("REGIONPATH");
    Boolean cmd = (Boolean)args.get("COMMAND_CANCEL");
    if (regionName != null)
    {
      Cache cache = CacheFactory.getAnyInstance();
      Region<?, ?> region = cache.getRegion(regionName);
      if (cmd != null)
      {
        //判断数量是为0,如果数量为0了,再恢复过期
        int wait_times = 1;
        while(region.size()>0)
        {
           LogUtil.getCoreLog().info("total sleep "+(wait_times*2)+" s for waiting clean region :"+regionName + "region.size():"+region.size());
           wait_times++;
           try
           {
               Thread.sleep(2000);
           }
           catch(Exception e){}
        }
        region.getAttributesMutator().setCustomEntryIdleTimeout(null);
      }
      else
      {
        region.getAttributesMutator().setCustomEntryIdleTimeout((CustomExpiry) new CleanExpire());
      }
      StringBuilder sb = new StringBuilder();
      sb.append(AdminUtil.getIp());
      sb.append(":");
      sb.append(System.getProperty(ITEMS.NODE_NAME.name()));
      if(cmd!=null)
      sb.append(" Finish unClean");
      else
      sb.append(" Finish Clean");
      return  sb.toString();
    }
    else
    {
      return "regionPath is null";
    }
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
  }
}
