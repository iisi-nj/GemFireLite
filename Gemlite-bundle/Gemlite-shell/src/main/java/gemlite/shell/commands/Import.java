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
import gemlite.shell.commands.admin.AbstractAdminCommand;
import gemlite.shell.service.batch.ImportService;

import java.io.File;
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
public class Import extends AbstractAdminCommand
{
    @Autowired
    ImportService importService;
    
    @CliAvailabilityIndicator({ "import" })
    public boolean offlineCommand()
    {
        return true;
    }

    @CliCommand(value = "import", help = "import from file(default),db to gemfire\n" + "1.has predefined property:import --t file --p import-file.properties\n"
            + "2.no predefined property:import --t file --f ac01.csv --c \"aac001,aab001...\" --region ac01 --table ac01\n")
    public void runJob(@CliOption(key = { "r", "region" }, mandatory = true, optionContext = "disable-string-converter param.context.region") String region, @CliOption(key = { "U",
            "update" }, unspecifiedDefaultValue = "false", help = "if job exists,update job define and execute it") boolean forceUpdate, @CliOption(key = {
            "T", "template" }, optionContext = "file,jdbc-paging,jdbc-partition", unspecifiedDefaultValue = "file") String template, @CliOption(key = { "f",
            "file" }, help = "csv file,e.g. /home/user/csv/table01.*.csv") String file,
            @CliOption(key = { "d", "delimiter" }, unspecifiedDefaultValue = ",", help = "default: ,") String delimiter,
            @CliOption(key = { "q", "quote" }, unspecifiedDefaultValue = "\"", help = "default: \"") String quote,
            @CliOption(key = { "s", "skipable" }, unspecifiedDefaultValue = "true", help = "sikp error? default is true") boolean skipable, @CliOption(key = {
                    "c", "columns" }, help = "1.user specify 2.csv head line 3.Domain property name 4.error") String columns,
            @CliOption(key = { "t", "table" }) String table, @CliOption(key = { "e", "encoding" }, unspecifiedDefaultValue = "UTF-8") String encoding,
            @CliOption(key = { "L", "linesToSkip" }, unspecifiedDefaultValue = "0") int linesToSkip,
            @CliOption(key = { "l", "list" }, unspecifiedDefaultValue = "false", specifiedDefaultValue = "true") boolean list,
            @CliOption(key = { "S", "show" }, unspecifiedDefaultValue = "false", specifiedDefaultValue = "true") boolean showDefine,
            @CliOption(key = { "driver", "dbdriver" },unspecifiedDefaultValue = "") String dbdriver,
            @CliOption(key = { "url", "dburl" },unspecifiedDefaultValue = "") String dburl,
            @CliOption(key = { "user", "dbuser" },unspecifiedDefaultValue = "") String dbuser,
            @CliOption(key = { "psw", "dbpsw" },unspecifiedDefaultValue = "") String dbpsw,
            @CliOption(key = { "k", "sortKey" },unspecifiedDefaultValue = "") String sortKey,
            @CliOption(key = { "w", "where" },unspecifiedDefaultValue = "") String where,
            @CliOption(key = { "p", "pageSize" },unspecifiedDefaultValue = "1000") int pageSize,
            @CliOption(key = { "F", "fetchSize" },unspecifiedDefaultValue = "-1") int fetchSize)
    {
        try
        {
            //ˇbpn
            if(quote!=null && quote.contains("\\t"))
                quote = "\t";
            if (list)
            {
                showNames();
                return;
            }
            else if (showDefine)
            {
                importService.showJob(region,template);
                return;
            }

            // Task not exists or force update
            if (!importService.isTaskExist(region,template) || forceUpdate)
            {
                // define new task
                boolean success = importService.defineJob(template, file, delimiter, quote, skipable, columns, region, table, encoding, linesToSkip,dbdriver,dburl,dbuser,dbpsw,sortKey,where,pageSize,fetchSize);
                if (success)
                {
                    BatchService batchService = JpaContext.getService(BatchService.class);
                    //›XjobÛpnì
                    batchService.saveJob(template, file, delimiter, quote, skipable, columns, region, table, encoding, linesToSkip,dbdriver,dburl,dbuser,dbpsw, sortKey, where, pageSize, fetchSize,forceUpdate);
                    LogUtil.getCheckLog().info("job " + region + " defined");
                    importService.executeJob(region,template);
                    
                    //gLü°	õ8Ñ›,pnêÑMnôepnì
                    importService.saveDbConfig(dbdriver, dburl, dbuser, dbpsw);
                }
            }
            else
                importService.executeJob(region,template);

        }
        catch(GemliteException e)
        {
            if (LogUtil.getCoreLog().isErrorEnabled())
                LogUtil.getCoreLog().error("import error", e);
            LogUtil.getCoreLog().info("Command failed,see debug log for more information");
        }

    }

    //*********************************************
    //{exit_code=COMPLETED, job_execution_id=4, duration=08:00:00, status=COMPLETED, end_time=2015-01-22 13:32:04.589, job_name=detailJob, create_time=2015-01-22 13:32:03.917, start_time=2015-01-22 13:32:03.933, last_updated=2015-01-22 13:32:04.59, exit_message=, job_instance_id=4, version=2}
    //{exit_code=COMPLETED, job_execution_id=3, duration=08:00:00, status=COMPLETED, end_time=2015-01-22 13:31:53.872, job_name=ordermainJob, create_time=2015-01-22 13:31:53.292, start_time=2015-01-22 13:31:53.317, last_updated=2015-01-22 13:31:53.873, exit_message=, job_instance_id=3, version=2}
    //{exit_code=COMPLETED, job_execution_id=2, duration=08:00:00, status=COMPLETED, end_time=2015-01-22 13:31:46.487, job_name=productJob, create_time=2015-01-22 13:31:46.22, start_time=2015-01-22 13:31:46.236, last_updated=2015-01-22 13:31:46.488, exit_message=, job_instance_id=2, version=2}
    //{exit_code=COMPLETED, job_execution_id=1, duration=08:00:00, status=COMPLETED, end_time=2015-01-22 13:31:37.88, job_name=productJob, create_time=2015-01-22 13:31:37.298, start_time=2015-01-22 13:31:37.36, last_updated=2015-01-22 13:31:37.881, exit_message=, job_instance_id=1, version=2}
    //****************************************
    @SuppressWarnings("rawtypes")
    @CliCommand(value = "list jobs", help = "list jobs")
    public String listJobs(@CliOption(key = { "status" }, mandatory = false, optionContext = "disable-string-converter param.context.job.status") String status)
    {
        BatchService batchService = JpaContext.getService(BatchService.class);
        Collection<Map> jobs;
        try
        {
          jobs = batchService.listJobExecutions(status);
           //:ws pn
          put(CommandMeta.LIST_JOBS,jobs);
          
          StringBuilder sb = new StringBuilder();
          for (Map job : jobs)
          {
              sb.append("ID:"+job.get("job_execution_id")+" JobName:"+job.get("job_name")+" status:"+job.get("status")+" startTime:"+job.get("start_time")+" endTime:"+job.get("end_time")+"\n");
          }
          return sb.toString();
        }
        catch(Exception e)
        {
          LogUtil.getCoreLog().warn("No Job has Executed.{}",e.getMessage());
        }
        
        return "no jobs";      
    }
    
    //************************************************************
    //{exit_code=COMPLETED, job_execution_id=5, status=COMPLETED, end_time=2015-01-22 16:12:05.037, job_name=detailJob, job_configuration_location=null, create_time=2015-01-22 16:12:04.44, start_time=2015-01-22 16:12:04.457, last_updated=2015-01-22 16:12:05.039, exit_message=, job_instance_id=5, version=2}
    //[{rollback_count=0, exit_code=COMPLETED, write_count=500, commit_count=5, status=COMPLETED, write_skip_count=0, read_skip_count=0, exit_message=, version=2, read_count=500, filter_count=0, end_time=2015-01-22 16:12:05.013, step_execution_id=9, process_skip_count=0, start_time=2015-01-22 16:12:04.528, last_updated=2015-01-22 16:12:05.015, step_name=detailStep0}, 
    //{rollback_count=0, exit_code=COMPLETED, write_count=500, commit_count=5, status=COMPLETED, write_skip_count=0, read_skip_count=0, exit_message=, version=7, read_count=500, filter_count=0, end_time=2015-01-22 16:12:04.996, step_execution_id=10, process_skip_count=0, start_time=2015-01-22 16:12:04.567, last_updated=2015-01-22 16:12:04.997, step_name=detailStep1:partition0}]
    //************************************************************
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @CliCommand(value = "describe job", help = "describe job by id")
    public String describeJob(@CliOption(key = { "id"}, mandatory = true,optionContext="disable-string-converter param.context.job.id") String jobExecutionId)
    {
        BatchService batchService = JpaContext.getService(BatchService.class);
        Map jobExecution = batchService.getJobExecution(NumberUtils.createLong(jobExecutionId));
        //°óduration
        Timestamp t1 = (Timestamp)jobExecution.get("start_time");
        Timestamp t2 = (Timestamp)jobExecution.get("last_updated");
        String duration = "";
        if(t2!=null)
        duration = DateUtil.format(new Date(t2.getTime()-t1.getTime()), "mm:ss.SSS") ;
        jobExecution.put("duration", duration);
        List<Map> steps = batchService.getStepExecutions(NumberUtils.createLong(jobExecutionId));
        jobExecution.put("steps", steps);
        
        //pn Ÿws
        put(CommandMeta.DESCRIBE_JOB,jobExecution);
        
        //pnU:
        StringBuilder sb = new StringBuilder();
        sb.append("JobName:").append(jobExecution.get("job_name")).append("\n");
        sb.append("Status:").append(jobExecution.get("status")).append("\n");
        sb.append("StartTime:").append(jobExecution.get("start_time")).append("\n");
        sb.append("LastUpdateTime:").append(jobExecution.get("last_updated")).append("\n");
        sb.append("Duration:").append(jobExecution.get("duration")).append("\n");
        sb.append("StepDetails:\n");
        for(Map step:steps)
        {
            sb.append("StepName:").append(step.get("step_name"));
            sb.append(" Status:").append(step.get("status"));
            sb.append(" ReadCount:").append(step.get("read_count"));
            sb.append(" WriteCount:").append(step.get("write_count"));
            sb.append(" CommitCount:").append(step.get("commit_count"));
            sb.append("\n");
            
            String exit_message = (String)step.get("exit_message"); 
            if(!StringUtils.isEmpty(exit_message))
            {
                sb.append("EXIT_MESSAGE:\n");
                sb.append(exit_message);
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }

    /***
     * user input only import 1.check context 2.list exists jobNames 3.end
     */
    private void showNames()
    {
        String names = importService.listNames();
        LogUtil.getCoreLog().info(names);
    }

}