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

import gemlite.shell.service.admin.JMXService;

import java.util.List;
import java.util.Set;

import javax.management.ObjectInstance;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.Completion;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.MethodTarget;
import org.springframework.stereotype.Component;

@Component
public class ServiceNameConverter implements Converter<String>
{
    @Autowired
    private JMXService jmxService;

    public boolean supports(Class<?> type, String optionContext)
    {
        return (String.class.equals(type)) && (StringUtils.contains(optionContext, "param.context.service.name"));
    }

    public String convertFromText(String value, Class<?> targetType, String optionContext)
    {
        return value;
    }

    public boolean getAllPossibleValues(List<Completion> completions, Class<?> targetType, String existingData, String optionContext, MethodTarget target)
    {
        if ((String.class.equals(targetType)) && (StringUtils.contains(optionContext, "param.context.service.name")))
        {
            Set<ObjectInstance> beans = jmxService.listMBeans();
            for (ObjectInstance bean : beans)
            {
                String name = bean.getObjectName().getCanonicalName();
                if (existingData != null)
                {
                    if (name.startsWith(existingData))
                    {
                        completions.add(new Completion(name));
                    }
                }
                else
                {
                    completions.add(new Completion(name));
                }
            }
        }
        return !completions.isEmpty();
    }
}
