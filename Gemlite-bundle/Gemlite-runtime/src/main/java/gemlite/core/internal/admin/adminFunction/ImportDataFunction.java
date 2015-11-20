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
package gemlite.core.internal.admin.adminFunction;

import gemlite.core.internal.admin.snapshot.RegionSnapshotService;
import gemlite.core.internal.admin.snapshot.RegionSnapshotServiceImpl;
import gemlite.core.internal.admin.snapshot.SnapshotOptions;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.ResultSender;

@SuppressWarnings({ "rawtypes", "unchecked"})
public class ImportDataFunction implements Function
{
  private static final long serialVersionUID = -8222186085474388512L;
  
  @Override
  public void execute(FunctionContext fc)
  {
    Map param = (HashMap) fc.getArguments();
    String regionName = (String) param.get("REGIONPATH");
    String importFileName = (String) param.get("FILEPATH");
    final ResultSender sender = fc.getResultSender(); 
    try
    {
      Cache cache = CacheFactory.getAnyInstance();
      Region region = cache.getRegion(regionName);
      String hostName = cache.getDistributedSystem().getDistributedMember().getHost();
      if (region != null)
      {
        LogUtil logUtil = LogUtil.newInstance();
        RegionSnapshotService snapshotService = new RegionSnapshotServiceImpl(region);
        File importFile = new File(importFileName);
        snapshotService.load(new File(importFileName), SnapshotOptions.SnapshotFormat.GEMFIRE,sender);
        String node = System.getProperty(ITEMS.NODE_NAME.name());
        String bindIp = System.getProperty(ITEMS.BINDIP.name());
        String successMessage = MessageFormat.format("Finish Data imported from file : {0} on host:{1} node:{2} to region : {3} cost :{4}",
            new Object[] { importFile.getCanonicalPath(), hostName+bindIp,node, regionName,logUtil.cost() });
        sender.lastResult(successMessage);
      }
      else
      {
        throw new IllegalArgumentException(MessageFormat.format("Region : {0} not found", regionName));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fc.getResultSender().sendException(e);
    }
  }
  
  @Override
  public boolean hasResult()
  {
    return true;
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
    return true;
  }
  
}
