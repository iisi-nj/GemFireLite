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

import gemlite.core.common.DateUtil;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.service.BatchService;
import gemlite.core.util.LogUtil;
import gemlite.shell.admin.dao.AdminDao;
import gemlite.shell.commands.admin.AbstractAdminCommand;
import gemlite.shell.service.batch.ImportService;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/***
 * 1.import by properties file 2.import list: list exist task 3.import
 * taskId->{taskName,delimiter,quote,skipable,fields,file,region,table}
 * 
 * @author ynd
 * 
 */
@Component
public class DataProcesser extends AbstractAdminCommand
{
    @Autowired
    AdminDao dao;
    
    @CliAvailabilityIndicator({ "exportEach" })
    public boolean offlineCommand()
    {
        return true;
    }

    /**
     * üú‡öpn
     */
    @CliCommand(value = "exportEach", help = "export from region")
    public void exportDataExch(
            @CliOption(key = { "r", "region" }, mandatory = true, optionContext = "disable-string-converter param.context.region") String regionName, 
            @CliOption(key = { "f", "filePath" }, mandatory = true) String filePath,
            @CliOption(key = { "m", "memberId" }, mandatory = true) String memberId,
            @CliOption(key = { "i", "ip" }, mandatory = true) String ip,
            @CliOption(key = { "s", "show" }, mandatory = true) String showLog
           )
    {
        dao.export(regionName, filePath, memberId, ip, showLog);
    }
}