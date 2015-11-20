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
package batch;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.Util;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.SimpleAsyncTaskExecutor;


public class TestSubContext
{
  public static void main(String[] args) 
  {
    try
    {
      ServerConfigHelper.initConfig();
      ServerConfigHelper.initLog4j("log4j-debug.xml");
      ClassPathXmlApplicationContext ctx = Util.initContext("batch/new-context.xml");
      JobLauncher launch = ctx.getBean(JobLauncher.class);
      JobExplorer epl=ctx.getBean(JobExplorer.class);
      JobRegistry reg =ctx.getBean(JobRegistry.class);
      JobOperator jop=ctx.getBean(JobOperator.class);
      ClassPathXmlApplicationContext ctx2 = Util.initContext(false,"batch/job-context.xml","batch-file-prod.xml");
      ctx2.setParent(ctx);
      ctx2.refresh();
      Job job = ctx2.getBean(Job.class);
      JobParametersBuilder build = new JobParametersBuilder();
      build.addLong("Id", System.currentTimeMillis());
        
      
      
      JobExecution exec= launch.run(job,build.toJobParameters());
      System.out.println(reg.getJobNames());
      Thread.sleep(Long.MAX_VALUE);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
   
  }
}
