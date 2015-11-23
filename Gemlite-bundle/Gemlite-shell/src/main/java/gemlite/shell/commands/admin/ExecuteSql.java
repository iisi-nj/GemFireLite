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

import gemlite.core.internal.mq.parser.AntlrTableParser;
import gemlite.core.internal.mq.processor.AsyncSqlProcessor;
import gemlite.core.internal.mq.processor.SimpleMapperDao;
import gemlite.core.internal.mq.sender.GFDomainSender;
import gemlite.core.util.LogUtil;
import gemlite.shell.common.GemliteStatus;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.shell.core.annotation.CliAvailabilityIndicator;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * 执行sql语句同步数据
 * @author GSONG
 * 2015年3月27日
 */
@Component
public class ExecuteSql extends AbstractAdminCommand
{
    @CliAvailabilityIndicator({ "execute sql" })
    public boolean isCommandAvailable()
    {
      return GemliteStatus.isConnected();
    }
    
    @CliCommand(value = "execute sql", help = "execute sql")
    public boolean execute(@CliOption(key="sql",mandatory=false) String sql,
            @CliOption(key="file",mandatory=false) String file)
    {
        if(StringUtils.isEmpty(sql)&&StringUtils.isEmpty(file))
            return false;
        AsyncSqlProcessor processor = new AsyncSqlProcessor(new AntlrTableParser(),new SimpleMapperDao(),new GFDomainSender());
        Date nowtime = new Date();
        StringBuffer msgBuf = new StringBuffer();
        msgBuf.append("timestamp:::" + nowtime.getTime()+"\n");
        if(StringUtils.isNotEmpty(sql))
        {
            msgBuf.append(sql);
            processor.parserOneMessage(msgBuf.toString());
            processor.remoteProcess(); 
            
            StringBuilder info = new StringBuilder("ExecuteSql :"+msgBuf.toString()+"Time is:" + nowtime.toString());
            LogUtil.getCoreLog().info(info.toString());
            System.out.println(info.toString());
        }
        else if(StringUtils.isNotEmpty(file))
        {
            try
            {
                LineNumberReader lr = new LineNumberReader(new FileReader(file));
                String sqlline = lr.readLine();
                int recvCount = 0;
                while (sqlline != null)
                {
                  sqlline = sqlline.trim();
                  if (StringUtils.isNotEmpty(sqlline))
                  {
                      if (sqlline.startsWith("insert") || sqlline.startsWith("update") || sqlline.startsWith("delete") || sqlline.startsWith("INSERT") || sqlline.startsWith("UPDATE") || sqlline.startsWith("DELETE"))
                      {
                        msgBuf.append("\n" + sqlline);
                        recvCount++;
                      }
                      else
                      {
                        msgBuf.append(" " + sqlline);
                      }
                  }
                  sqlline = lr.readLine();
                }
                lr.close();
                StringBuilder info = new StringBuilder("Read file ok. The Sql Number is " + recvCount);
                LogUtil.getCoreLog().info(info.toString());
                System.out.println(info.toString());
                processor.parserOneMessage(msgBuf.toString());
                processor.remoteProcess(); 
            }
            catch(IOException e)
            {
                LogUtil.getCoreLog().error("execute error, file:"+file,e);
                return false;
            }
        }
        return true;
    }
}
