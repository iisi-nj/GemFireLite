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

import javax.transaction.TransactionManager;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.Util;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilderHelper;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;


public class Test1
{
  public static void main(String[] args) throws Exception
  {
    ServerConfigHelper.initLog4j("log4j-shell.xml");
    ClassPathXmlApplicationContext ctx = Util.initContext("batch/new-context.xml","batch-file-ac01.xml");
    JobLauncher launch = ctx.getBean(JobLauncher.class);
    JobExplorer epl=ctx.getBean(JobExplorer.class);
    JobRegistry reg =ctx.getBean(JobRegistry.class);
    JobOperator jop=ctx.getBean(JobOperator.class);
    System.out.println(epl.getJobNames()+" "+reg.getJobNames()+" "+jop.toString());
    
    
    
    for(String bn: ctx.getBeanFactory().getBeanDefinitionNames())
    {
      System.out.println(bn);
    }
    
    ctx.close();
  }
}
