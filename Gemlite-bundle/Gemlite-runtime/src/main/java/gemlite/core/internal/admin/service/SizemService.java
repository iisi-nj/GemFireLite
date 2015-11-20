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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.internal.cache.BucketRegion;
import com.gemstone.gemfire.internal.cache.ForceReattemptException;
import com.gemstone.gemfire.internal.cache.PartitionedRegion;

@SuppressWarnings({ "rawtypes", "unchecked" })
@AdminService(name = "SizemService")
public class SizemService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    Cache cache = CacheFactory.getAnyInstance();
    String regionPath = (String) args.get("REGIONPATH");
    Region region = cache.getRegion(regionPath);
    HashMap<String, Object> resultMap = new HashMap<String, Object>();
    HashMap<DistributedMember, Integer> memberMap = new HashMap<DistributedMember, Integer>();
    if (region == null)
    {
      resultMap.put("code", "-1");
      return resultMap;
    }
    else
    {
      boolean isPR = region instanceof PartitionedRegion;
      resultMap.put("isPR", isPR);
      if (isPR)
      {
        DistributedMember member = cache.getDistributedSystem().getDistributedMember();
        PartitionedRegion pr = (PartitionedRegion) region;
        int count = 0;
        if (pr.getDataStore() == null)
        {
        }
        else
        {
          List<Integer> bucketIdList = pr.getDataStore().getLocalPrimaryBucketsListTestOnly();
          for (Integer bucketId : bucketIdList)
          {
            try
            {
              BucketRegion bucketRegion = pr.getDataStore().getInitializedBucketForId(null, bucketId);
              count += bucketRegion.size();
            }
            catch (ForceReattemptException e)
            {
            }
          }
        }
        memberMap.put(member, count);
      }
      else
      {
        memberMap.put(cache.getDistributedSystem().getDistributedMember(), region.keySet().size());
      }
      resultMap.put("code", "1");
    }
    resultMap.put("memberMap", memberMap);
    return resultMap;
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
    // TODO Auto-generated method stub
    
  }
}
