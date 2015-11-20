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
import gemlite.core.internal.admin.adminFunction.ImportDataFunction;
import gemlite.core.internal.admin.collector.MsgCollector;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheClosedException;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionInvocationTargetException;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.distributed.DistributedMember;

@SuppressWarnings({ "rawtypes" })
@AdminService(name = "ImportDataService")
public class ImportDataService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    Cache cache = CacheFactory.getAnyInstance();
    String memberId = (String) args.get("MEMBERID");
    String ip = (String)args.get("IP");
    String regionName = (String) args.get("REGIONPATH");
    String filePath = (String) args.get("FILEPATH");
    String showLog = (String) args.get("showLog");
    if (StringUtils.isEmpty(filePath))
    {
      filePath = System.getProperty(ITEMS.GS_WORK.name());
    }
    if ("ALL".equalsIgnoreCase(regionName))
    {
      // ���@	region�,�&gfd 
      if (filePath.endsWith(".gfd"))
      {
        int loc1 = filePath.lastIndexOf("\\");
        int loc2 = filePath.lastIndexOf("/");
        filePath = filePath.substring(0, loc1 > loc2 ? loc1 : loc2);
      }
    }
    if (!filePath.endsWith(".gfd") && !filePath.endsWith("\\") && !filePath.endsWith("/"))
      filePath += File.separator;
    
    args.put("FILEPATH", filePath);
    DistributedMember targetMember = AdminUtil.getDistributedMemberById(cache, memberId,ip);
    try
    {
      Set<String> regions = new HashSet<String>();
      if ("ALL".equals(regionName))
      {
        regions = AdminUtil.getRegionNames(cache);
      }
      else
      {
        regions.add(regionName);
      }
      if (targetMember == null)
      {
        LogUtil.getAppLog().error("error:targetMember is null!memberId is:" + memberId+" and total members:"+AdminUtil.getAllMemberIds(cache));
        return "error:targetMember is null!memberId is:" + memberId+" and total members:"+AdminUtil.getAllMemberIds(cache);
      }
      if (regions == null || regions.isEmpty())
      {
        LogUtil.getAppLog().error("error:regions is empty!");
        return "error:regions is empty!";
      }
        // *region * *
        for (String r : regions)
        {
          Execution execution = null;
          String path = filePath;
          if (!filePath.endsWith(".gfd"))
            path += (r + ".gfd");
          args.put("REGIONPATH", r);
          args.put("FILEPATH", path);
          String last = "last";
          MsgCollector msgCollector = new MsgCollector(last);
          execution = FunctionService.onMember(targetMember).withArgs(args).withCollector(msgCollector);
          ResultCollector rc = execution.execute(new ImportDataFunction().getId());
          if ("Y".equals(showLog))
          {
            BlockingQueue queue = msgCollector.getResult();
            String msg = "";
            try
            {
              // �-��o
              while ((msg = (String) queue.take()) != last)
              {
                if (LogUtil.getAppLog().isDebugEnabled())
                  LogUtil.getAppLog().debug(" send to client:" + msg);
                return msg;
              }
            }
            catch (Exception e)
            {
              e.printStackTrace();
              LogUtil.getAppLog().error("send msg error:", e);
              return e.getMessage();
            }
          }
          else
          {
            return rc.getResult();
          }
        }
    }
    catch (CacheClosedException e)
    {
      e.printStackTrace();
      LogUtil.getAppLog().error("error:CacheClosedException", e);
      return e.getMessage();
    }
    catch (FunctionInvocationTargetException e)
    {
      e.printStackTrace();
      LogUtil.getAppLog().error("error:FunctionInvocationTargetException", e);
      return e.getMessage();
    }
    return true;
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
    // TODO Auto-generated method stub
    
  }
}
