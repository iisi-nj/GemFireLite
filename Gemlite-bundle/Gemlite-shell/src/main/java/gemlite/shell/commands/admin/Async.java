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

import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.shell.commands.CommandMeta;
import gemlite.shell.common.GemliteStatus;
import gemlite.shell.service.admin.JMXService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServerConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

@Component
public class Async extends AbstractAdminCommand
{
    @Autowired
    private JMXService jmxService;
    
    @CliAvailabilityIndicator({ "list async","add async","remove async" })
    public boolean isCommandAvailable()
    {
      return GemliteStatus.isConnected();
    }
    
    /**
     * ú@	e
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    @CliCommand(value = "list asyncqueue", help = "list async quenes")
    public String listSync()
    {
        String result = "";
        MBeanServerConnection connection = jmxService.getConnection();
        if(connection == null)
            return "Please connect to the Gemlite Mangaer first.";
        String oname = MBeanHelper.createManagerMBeanName("RegionManager");
        Object obj = jmxService.invokeOperation(oname, "listSync");
        List<HashMap<String,Object>> set;
        result += "===========AsyncEventQueue list==============\n";
        Set<String> queues = new LinkedHashSet<String>();
        List<HashMap<String,Object>> allQueues = new ArrayList<HashMap<String,Object>>();
        if(obj instanceof HashSet)
        {
            HashSet<List<HashMap<String,Object>>> list = (HashSet<List<HashMap<String,Object>>>)obj;
            for(List<HashMap<String,Object>> s:list)
            {
                allQueues.addAll(s);
                print(s,queues);
            }
        }
        else
        {
           set = (List<HashMap<String,Object>>)obj;
           allQueues.addAll(set);
           result+=print(set,queues);
        }
        //:ws pn
        put(CommandMeta.LIST_ASYNCQUEUE,allQueues);
        for(String s:queues)
        {
            result+=s+"\n";
        }
        result += "=============================================";
        return result;
    }
    
    private String print(List<HashMap<String,Object>> set,Set<String> queues)
    {
        StringBuilder result = new StringBuilder();
        for(HashMap<String,Object> sync:set)
        {
            queues.add((String)sync.get("queueid")); 
        }
        return result.toString();
    }
    
    @CliCommand(value = "desc asyncqueue", help = "list async quenes")
    public String listSync(@CliOption(key="queueId",mandatory=true) String queueId)
    {
        String result = "";
        MBeanServerConnection connection = jmxService.getConnection();
        if(connection == null)
            return "Please connect to the Gemlite Mangaer first.";
        String oname = MBeanHelper.createManagerMBeanName("RegionManager");
        Object list = jmxService.invokeOperation(oname, "descSync",queueId);
        result += "===========AsyncEventQueue info==============\n";
        result += list.toString();
        result += "=============================================";
        return result;
    }
    
    
    /**
     * û e:6
     * @param region
     * @param queuename
     * @param confFile
     * @return
     */
    @CliCommand(value = "add asyncqueue", help = "list async quenes")
    public String addAsync(@CliOption(key="region",mandatory=true,optionContext="disable-string-converter param.context.region") String region,
            @CliOption(key="queueId",mandatory=true) String queueId,
            @CliOption(key="persistent",unspecifiedDefaultValue="false") boolean persistent,
            //@CliOption(key="parallel",unspecifiedDefaultValue="false") boolean parallel,  dy:trueöúàH
            @CliOption(key="diskstorename",unspecifiedDefaultValue="async") String diskStoreName,
            @CliOption(key="driver",mandatory=true) String driver,
            @CliOption(key="url",mandatory=true) String url,
            @CliOption(key="user",mandatory=true) String user,
            @CliOption(key="password",mandatory=true) String password)
    {
        Object result = "";
        MBeanServerConnection connection = jmxService.getConnection();
        if(connection == null)
            return "Please connect to the Gemlite Mangaer first.";
        String oname = MBeanHelper.createManagerMBeanName("RegionManager");
        Object args [] = new Object[]{region,queueId,persistent,diskStoreName,driver,url,user,password};
        result = jmxService.invokeOperation(oname, "addSync",args);
        if(result!=null)
        return result.toString();
        return "";
    }
    
    /**
     *  de:6
     * @param region
     * @param queuename
     * @return
     */
    @CliCommand(value = "remove asyncqueue", help = "list async quenes")
    public String removeAsync(@CliOption(key="region",mandatory=true,optionContext="disable-string-converter param.context.region") String region,
            @CliOption(key="queueId",mandatory=true) String queueId)
    {
        Object result = "";
        MBeanServerConnection connection = jmxService.getConnection();
        if(connection == null)
            return "Please connect to the Gemlite Mangaer first.";
        String oname = MBeanHelper.createManagerMBeanName("RegionManager");
        result = jmxService.invokeOperation(oname, "removeSync",region,queueId);
        return result.toString();
    }
}
