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
package gemlite.shell.converters;

import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.core.util.LogUtil;
import gemlite.shell.service.admin.JMXService;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.Completion;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.MethodTarget;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;

@Component
public class RegionConverter implements Converter<String>
{
    @Autowired
    private JMXService jmxService;

    public boolean supports(Class<?> type, String optionContext)
    {
        return (String.class.equals(type)) && (StringUtils.contains(optionContext, "param.context.region"));
    }

    public String convertFromText(String value, Class<?> targetType, String optionContext)
    {
        return new String(value);
    }

    public boolean getAllPossibleValues(List<Completion> completions, Class<?> targetType, String existingData, String optionContext, MethodTarget target)
    {
        if ((String.class.equals(targetType)) && (StringUtils.contains(optionContext, "param.context.region")))
        {
            Set<String> regionPathSet = getAllRegionPaths();
            for (String regionPath : regionPathSet)
            {
                if (existingData != null)
                {
                    if (regionPath.startsWith(existingData))
                    {
                        completions.add(new Completion(regionPath));
                    }
                }
                else
                {
                    completions.add(new Completion(regionPath));
                }
            }
        }
        return !completions.isEmpty();
    }

    @SuppressWarnings("unchecked")
    private Set<String> getAllRegionPaths()
    {
        Set<String> regionPathSet = new LinkedHashSet<String>();
        Set<Region<?, ?>> set = ClientCacheFactory.getAnyInstance().rootRegions();
        if (set != null)
        {
            Iterator<Region<?, ?>> it = set.iterator();
            while (it.hasNext())
            {
                Region<?, ?> r = it.next();
                regionPathSet.add(r.getName());
            }
        }
        else
        {
            MBeanServerConnection connection = jmxService.getConnection();
            if (connection == null)
            {
                LogUtil.getCoreLog().error("Please connect to the Gemlite Mangaer first.");
            }
            else
            {
                String oname = MBeanHelper.createManagerMBeanName("RegionManager");
                Object obj = jmxService.invokeOperation(oname, "listRegions_one");
                Set<String> regionPaths;
                if(obj instanceof List<?>)
                    regionPaths = (Set<String>)((List<Set<String>>)obj).get(0);
                else
                 regionPaths = (Set<String>)obj; 
                if ((regionPaths != null) && (regionPaths.size() != 0))
                {
                    regionPathSet = new TreeSet<String>();
                    for (String regionPath : regionPaths)
                    {
                        if (regionPath != null)
                        {
                            regionPathSet.add(regionPath);
                        }
                    }
                }
            }
        }
        if(regionPathSet.isEmpty())
        {
            LogUtil.getCoreLog().info("No regions!");
        }
        return regionPathSet;
    }
}
