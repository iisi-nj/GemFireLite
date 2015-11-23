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
package gemlite.shell.commands.admin;

import gemlite.core.internal.jmx.manage.KeyConstants.Regions;
import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.common.GemliteStatus;
import gemlite.shell.service.admin.AdminService;
import gemlite.shell.service.admin.JMXService;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class RegionCommand extends AbstractAdminCommand
{
    @Autowired
    private AdminService service;
    
    @Autowired
    private JMXService jmxSrv;
    
    @CliAvailabilityIndicator({ "size -m","pr -b" })
    public boolean isCommandAvailable()
    {
      return GemliteStatus.isConnected();
    }
    
    @CliCommand(value = "size -m", help = "size -m")
    public String executeSizem(@CliOption(key="region",mandatory=true,optionContext="disable-string-converter param.context.region",help="Name/Path of the region to be described.") String name)
    {
      List<HashMap<String,Object>> rs = service.sizeM(name);
      put(CommandMeta.SIZEM,rs);
      //对结果进行解析
      StringBuilder sb = new StringBuilder();
      sb.append("Member Id\tRegion Size  ").append("\n");
      sb.append("----------\t-----------\t-----------  ").append("\n");
      for(HashMap<String,Object> map:rs)
      {
          sb.append(map.keySet().iterator().next() + "\t\t" + map.entrySet().iterator().next()).append("\n");
      }
      return sb.toString();
    }
    
    @CliCommand(value = "pr -b", help = "pr -b")
    public String executePrb(@CliOption(key="region",mandatory=true,optionContext="disable-string-converter param.context.region",help="Name/Path of the region to be described.") String name)
    {
      return service.prB(name);
    }
    
    //列出所有region详情
    @SuppressWarnings("unchecked")
    @CliCommand(value = "list regions", help = "list regions details")
    public String listRegionDetails()
    {
        String oname = MBeanHelper.createManagerMBeanName("RegionManager");
        Object obj = jmxSrv.invokeOperation(oname, "listRegionDetails");
        List<HashMap<String,Object>> set = (List<HashMap<String,Object>>)obj;
        
        //储存数据,供ws调用
        put(CommandMeta.LIST_REGIONS,set);
        
        //解析
        StringBuilder sb = new StringBuilder();
        if(set != null)
        for(HashMap<String,Object> map:set)
        {
            sb.append("RegionName:").append(map.get(Regions.regionName.name())).append("\n");
            sb.append("KeyClass:").append(map.get(Regions.keyClass.name())).append("\n");
            sb.append("keyFields:").append(map.get(Regions.keyFields.name())).append("\n");
            sb.append("valueClass:").append(map.get(Regions.valueClass.name())).append("\n");
            sb.append("valueFields:").append(map.get(Regions.valueFields.name())).append("\n");
            sb.append("Size:").append(map.get(Regions.size.name())).append("\n");
            sb.append("--------------------------------------------").append("\n");
        }
        return sb.toString();
    }
    
    
  //列出所有region详情
    @SuppressWarnings("unchecked")
    @CliCommand(value = "describe region", help = "describe region")
    public String describeRegion(@CliOption(key="region",mandatory=true,optionContext="disable-string-converter param.context.region",help="Name/Path of the region to be described.") String name)
    {
        String oname = MBeanHelper.createManagerMBeanName("RegionManager");
        Object obj = jmxSrv.invokeOperation(oname, "describeRegion",name);
        HashMap<String,Object> map;
        if(obj instanceof List<?>)
        {
            List<HashMap<String,Object>> list = (List<HashMap<String,Object>>)obj;
            map = list.get(0);
        }
        else
            map = (HashMap<String,Object>)obj;
        if(map == null)
            return "";
        //储存数据,供ws调用
        put(CommandMeta.DESCRIBE_REGION,map);
        
        //解析
        StringBuilder sb = new StringBuilder();
        sb.append("RegionName:").append(map.get(Regions.regionName.name())).append("\n");
        sb.append("KeyClass:").append(map.get(Regions.keyClass.name())).append("\n");
        sb.append("keyFields:").append(map.get(Regions.keyFields.name())).append("\n");
        sb.append("valueClass:").append(map.get(Regions.valueClass.name())).append("\n");
        sb.append("valueFields:").append(map.get(Regions.valueFields.name())).append("\n");
        sb.append("Size:").append(map.get(Regions.size.name())).append("\n");
        sb.append("StatisticsEnabled:").append(map.get(Regions.statisticsEnabled.name())).append("\n");
        sb.append("CloningEnabled:").append(map.get(Regions.cloningEnabled.name())).append("\n");
        sb.append("CacheListeners:").append(map.get(Regions.cacheListeners.name())).append("\n");
        sb.append("--------------------------------------------").append("\n");
        //判断是否存在partion属性
        if(map.get(Regions.partitionResolver.name())!=null)
        {
            sb.append("PartitionResolver:").append(map.get(Regions.partitionResolver.name())).append("\n");
            sb.append("RecoveryDelay:").append(map.get(Regions.recoveryDelay.name())).append("\n");
            sb.append("RedundantCopies:").append(map.get(Regions.redundantCopies.name())).append("\n");
            sb.append("ColocatedWith:").append(map.get(Regions.colocatedWith.name())).append("\n");
            sb.append("LocalMaxMemory:").append(map.get(Regions.localMaxMemory.name())).append("\n");
            sb.append("totalMaxMemory:").append(map.get(Regions.totalMaxMemory.name())).append("\n");
            sb.append("TotalNumBuckets:").append(map.get(Regions.totalNumBuckets.name())).append("\n");
        }
        return sb.toString();
    }
}
