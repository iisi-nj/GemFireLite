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
import gemlite.core.util.LogUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;

@SuppressWarnings({"unchecked","rawtypes"})
@AdminService(name = "DeserializeService")
public class DeserializeService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    Cache cache = CacheFactory.getAnyInstance();
    String regionPath = (String)args.get("REGIONPATH");
    StringBuilder sb = new StringBuilder();
    if("ALL".equalsIgnoreCase(regionPath))
    {
      Set<Region<?,?>> regions = cache.rootRegions();
      for(Region region:regions)
      {
        sb.append(deserilze(region));
      }
      return sb.toString();
    }
    else
    {
      Region region = cache.getRegion(regionPath);
      return deserilze(region);
    }
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
  }
  
  private String deserilze(Region region)
  {
    if(region == null)
      return "region is null";
    LogUtil logUtil = LogUtil.newInstance();
    if (PartitionRegionHelper.isPartitionedRegion(region))
      region = PartitionRegionHelper.getLocalPrimaryData(region);
    Iterator it = region.values().iterator();
    int i = 0 ;
    while (it.hasNext())
    {
      it.next();
      i++;
    }
    long cost = logUtil.cost();
    LogUtil.getAppLog().info("deserilze "+ region.getFullPath() + " end,size:" + region.values().size() + " cost:" + cost);
    return region.getName()+":"+new Integer(i)+"cost:"+cost+"\n";
  }
}
