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
package gemlite.shell.commands;

import gemlite.core.api.logic.LogicServices;
import gemlite.core.api.logic.RemoteResult;
import gemlite.shell.commands.admin.AbstractAdminCommand;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Shell端发起查询请求
 * @author GSONG
 * 2015年7月16日
 */
@Component
public class Query extends AbstractAdminCommand
{

    public static void main(String[] args)
    {

    }
    
    @CliCommand(value = "query", help = "query servcie ,such as query --module _tmprm_module_ --service ParameterService --type String[] --values IT0,key0,pc0")
    public Object doQuery(@CliOption(key = { "m", "module" }, mandatory = true) String module,
            @CliOption(key = { "s", "service" }, mandatory = true) String service,
            @CliOption(key = { "t", "type" }, mandatory = true) String type,
            @CliOption(key = { "v", "values" }, mandatory = true) String values)
    {
        //类型进行解析
        RemoteResult rr = LogicServices.createRequest(module, service, revert(type,values)); 
        Object rs = rr.getResult();
        put(CommandMeta.QUERY,rs);
        return rs;
    }
    
    public Object revert(String type,String values)
    {
        Assert.notNull(type);
        Assert.notNull(values);
        type = StringUtils.lowerCase(type);
        switch(type)
        {
          //字符数组
           case "string[]":
               //解析values
          String[] arr = values.split(",");
          return arr;
          
           case "string":
             return values;
        }
        return null;
    }

}
