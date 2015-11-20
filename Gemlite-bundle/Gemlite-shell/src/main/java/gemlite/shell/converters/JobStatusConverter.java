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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.shell.core.Completion;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.MethodTarget;
import org.springframework.stereotype.Component;

@Component
public class JobStatusConverter implements Converter<String>
{

    @Override
    public boolean supports(Class<?> type, String optionContext)
    {
        return (String.class.equals(type)) && (StringUtils.contains(optionContext, "param.context.job.status"));
    }

    @Override
    public String convertFromText(String value, Class<?> targetType, String optionContext)
    {
        return value;
    }

    @Override
    public boolean getAllPossibleValues(List<Completion> completions, Class<?> targetType, String existingData, String optionContext, MethodTarget target)
    {
        if ((String.class.equals(targetType)) && (StringUtils.contains(optionContext, "param.context.job.status")))
        {
            Set<String> status = new HashSet<String>();
            status.add("STARTED");
            status.add("COMPLETED");
            for (String regionPath : status)
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

}
