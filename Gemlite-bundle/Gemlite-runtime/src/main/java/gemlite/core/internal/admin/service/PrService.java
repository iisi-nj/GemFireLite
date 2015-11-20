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
import gemlite.core.api.ApiConstant;
import gemlite.core.internal.admin.AbstractRemoteAdminService;
import gemlite.core.util.LogUtil;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.distributed.internal.membership.InternalDistributedMember;
import com.gemstone.gemfire.internal.cache.BucketRegion;
import com.gemstone.gemfire.internal.cache.PartitionedRegion;

@SuppressWarnings({"unchecked","rawtypes"})
@AdminService(name = "PrService")
public class PrService extends AbstractRemoteAdminService<Map<String, Object>, ArrayList<HashMap>>
{
  @Override
  public ArrayList<HashMap> doExecute(Map<String, Object> args)
  {
    Cache cache = CacheFactory.getAnyInstance();
    String regionPath = (String)args.get(ApiConstant.REGIONPATH);
    Region region = cache.getRegion(regionPath);
    ArrayList<HashMap> list = new ArrayList<HashMap>();
    if (region != null && region instanceof PartitionedRegion)
    {
      DistributedMember member = cache.getDistributedSystem().getDistributedMember();
      PartitionedRegion pr = (PartitionedRegion) region;
      if (pr.getDataStore() != null)
      {
        Set<BucketRegion> set2 = pr.getDataStore().getAllLocalBucketRegions();
        for (BucketRegion br : set2)
        {
          HashMap map = new HashMap();
          map.put("BucketId", br.getId());
          map.put("Size", br.size());
          map.put("Bytes", br.getTotalBytes());
          map.put("host", getHost());
          map.put("node",System.getProperty("NODE_NAME"));
          InternalDistributedMember m = pr.getBucketPrimary(br.getId());
          if (m!=null && member.getId().equals(m.getId()))
          {
            map.put("type","primary");
          }
          else
          {
            map.put("type","redundant");
          }
          map.put("TotalNumBuckets", pr.getPartitionAttributes().getTotalNumBuckets());
          list.add(map);
        }
      }
    }
    return list;
  }
  
  /**
   * Ö,:„ip
   * @return
   */
  private String getHost()
  {
    String ip = "";
    try
    {
      InetAddress addr = InetAddress.getLocalHost();
      ip = addr.getHostAddress().toString();
    }
    catch (Exception e)
    {
      ip = e.getMessage();
      LogUtil.getAppLog().error("get Ip error:", e);
    }
    return ip;
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
    
  }
}
