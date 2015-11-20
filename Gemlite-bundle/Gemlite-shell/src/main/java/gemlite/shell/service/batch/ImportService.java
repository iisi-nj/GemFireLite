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
package gemlite.shell.service.batch;

import gemlite.core.common.DESUtil;
import gemlite.core.common.DateUtil;
import gemlite.core.internal.batch.BatchGenenator;
import gemlite.core.internal.batch.BatchParameter;
import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.domain.ConfigKeys;
import gemlite.core.internal.support.jpa.files.domain.ConfigTypes;
import gemlite.core.internal.support.jpa.files.domain.GmConfig;
import gemlite.core.internal.support.jpa.files.service.ConfigService;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;
import gemlite.shell.common.BatchTemplateTypes;
import gemlite.shell.common.converters.DB2Converter;
import gemlite.shell.common.converters.DerbyConverter;
import gemlite.shell.common.converters.H2Converter;
import gemlite.shell.common.converters.MySqlConverter;
import gemlite.shell.common.converters.OracleConverter;
import gemlite.shell.common.converters.PostgresConverter;
import gemlite.shell.common.converters.SqlServerConverter;
import gemlite.shell.common.converters.SqliteConverter;
import gemlite.shell.common.converters.SybaseConverter;
import gemlite.shell.common.converters.TableConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.support.DatabaseType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;

@Component
public class ImportService
{
  private Map<DatabaseType, TableConverter> converters = new HashMap<DatabaseType, TableConverter>();
  {
    converters.put(DatabaseType.DB2, new DB2Converter());
    converters.put(DatabaseType.DB2ZOS, new DB2Converter());
    converters.put(DatabaseType.DERBY, new DerbyConverter());
    converters.put(DatabaseType.H2, new H2Converter());
    converters.put(DatabaseType.MYSQL, new MySqlConverter());
    converters.put(DatabaseType.ORACLE, new OracleConverter());
    converters.put(DatabaseType.POSTGRES, new PostgresConverter());
    converters.put(DatabaseType.SQLITE, new SqliteConverter());
    converters.put(DatabaseType.SQLSERVER, new SqlServerConverter());
    converters.put(DatabaseType.SYBASE, new SybaseConverter());
  }
  
  class JobItem
  {
    protected String name;
    
    protected BatchParameter attributes;
    
    protected ApplicationContext jobContext;
    
    protected Job job;
    
    protected String jobContent;
  }
  
  private BatchGenenator generator = new BatchGenenator();
  
  private ClassPathXmlApplicationContext batchContext;
  
  private static AtomicBoolean initializedFlag = new AtomicBoolean(false);
  
  private JobLauncher jobLauncher;
  
  private Map<String, JobItem> jobItems = new HashMap<>();
  
  public static void main(String[] args)
  {
    ServerConfigHelper.initConfig();
    ServerConfigHelper.initLog4j("log4j-shell.xml");
    BatchGenenator gen = new BatchGenenator();
    gen.initializeEngine();
    BatchParameter param = new BatchParameter("file", "d", ",", "\"", false, "c1,c2", "r1", "t1", "UTF-8", 0, "", "",
        0, 0);
    String str = gen.generateFileJob("test", param);
    System.out.println(str);
  }
  
  private void checkContextInitialized()
  {
    if (initializedFlag.get() == false || batchContext == null || batchContext.isActive() == false)
    {
      try
      {
        generator.initializeEngine();
        
        batchContext = Util.initContext("batch/batch-context.xml");
        jobLauncher = batchContext.getBean(JobLauncher.class);
        if (LogUtil.getCoreLog().isDebugEnabled())
          LogUtil.getCoreLog().debug("Context initialized success:" + batchContext);
      }
      catch (Exception e)
      {
        if (e.getCause() instanceof DataAccessResourceFailureException)
        {
          LogUtil.getCoreLog().info("-> Spring batch reposity database error.");
        }
        if (LogUtil.getCoreLog().isErrorEnabled())
          LogUtil.getCoreLog().error("Context initailized failed", e);
        throw new GemliteException(e);
      }
      initializedFlag.set(true);
    }
    else if (LogUtil.getCoreLog().isDebugEnabled())
      LogUtil.getCoreLog().debug("Context already initialized:" + batchContext);
  }
  
  public boolean isTaskExist(String name, String tpl)
  {
    checkContextInitialized();
    return jobItems.containsKey(name + tpl);
  }
  
  public String listNames()
  {
    checkContextInitialized();
    List<String> names = new ArrayList<>(jobItems.keySet());
    Collections.sort(names);
    return names.toString();
  }
  
  public void showJob(String name, String tpl)
  {
    checkContextInitialized();
    JobItem item = jobItems.get(name + tpl);
    if (item == null)
      LogUtil.getCoreLog().info("Job " + name + " not defined.");
    else
      LogUtil.getCoreLog().info("Job " + name + " attr:" + item.attributes);
  }
  
  public void executeJob(String name, String tpl)
  {
    checkContextInitialized();
    JobItem item = jobItems.get(name + tpl);
    LogUtil.getAppLog().info("Job:" + name + " executing...");
    try
    {
      JobExecution exec = jobLauncher.run(item.job, new JobParametersBuilder().addDate("StartTime", new Date())
          .addString("name", name).toJobParameters());
      String s1 = DateUtil.format(exec.getCreateTime(), "HH:mm:ss.SSS");
      LogUtil.getAppLog().info("Job:" + name + " start asynchronous,start time:" + s1);
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().info("Job:" + name + " failed");
      if (LogUtil.getCoreLog().isErrorEnabled())
        LogUtil.getCoreLog().error(name, e);
    }
  }
  
  private void setDbParameter(String dbdriver, String dburl, String dbuser, String dbpsw)
  {
    // pn_ïåÎ-Ã“-ûÖ
    if (StringUtils.isNotEmpty(dbdriver))
      ServerConfigHelper.setProperty("db.jdbc.driver", dbdriver);
    if (StringUtils.isNotEmpty(dburl))
      ServerConfigHelper.setProperty("db.jdbc.url", dburl);
    if (StringUtils.isNotEmpty(dbdriver))
      ServerConfigHelper.setProperty("db.jdbc.user", dbuser);
    if (StringUtils.isNotEmpty(dbpsw))
      ServerConfigHelper.setProperty("db.jdbc.password", dbpsw);
  }
  
  /**
   * ÝXÎpn“üp„pn“Mn,ùè >
   * 
   * @param dbdriver
   * @param dburl
   * @param dbuser
   * @param dbpsw
   */
  public void saveDbConfig(String dbdriver, String dburl, String dbuser, String dbpsw)
  {
    ConfigService service = JpaContext.getService(ConfigService.class);
    
    if (StringUtils.isNotEmpty(dbdriver) && StringUtils.isNotEmpty(dbuser) && StringUtils.isNotEmpty(dbuser))
    {
      ConfigKeys keys[] = new ConfigKeys[] { ConfigKeys.import_dbdriver, ConfigKeys.import_dburl,
          ConfigKeys.import_dbuser, ConfigKeys.import_dbpsw };
      String values[] = new String[] { dbdriver, dburl, dbuser, dbpsw };
      
      for (int i = 0; i < 4; i++)
      {
        String key = keys[i].getValue();
        // Öpn,‚œòÏÝXÇ,ô°pn
        GmConfig conf = service.findConfigByKey(key);
        
        if (conf == null)
        {
          if (StringUtils.equals(ConfigKeys.import_dbpsw.getValue(), key))
            values[i] = DESUtil.encrypt(values[i]);
          service.saveConf(new GmConfig(key, values[i], ConfigTypes.importdbconfig.getValue()));
        }
        else
        {
          // ‚œÆ¡	9Ø, Æ
          if (StringUtils.equals(ConfigKeys.import_dbpsw.getValue(), key))
          {
            values[i] = DESUtil.encrypt(values[i]);
            // Æ¡	Ø,ÝX
            if (StringUtils.equals(conf.getValue(), values[i]))
              return;
          }
          conf.setValue(values[i]);
          service.saveConf(conf);
        }
      }
    }
  }
  
  /**
   * 
   * @param template
   * @param file
   * @param delimiter
   * @param quote
   * @param skipable
   * @param columns
   * @param region
   * @param table
   * @param encoding
   * @param linesToSkip
   * @param dbdriver
   *          //b„/üpeŽpn“„Mn
   * @param dburl
   * @param dbuser
   * @param dbpsw
   * @param sortKey
   * @param where
   * @param pageSize
   * @param fetchSize
   * @return
   */
  public boolean defineJob(String template, String file, String delimiter, String quote, boolean skipable,
      String columns, String region, String table, String encoding, int linesToSkip, String dbdriver, String dburl,
      String dbuser, String dbpsw, String sortKey, String where, int pageSize, int fetchSize)
  {
    BatchParameter param = new BatchParameter(template, file, delimiter, quote, skipable, columns, region, table,
        encoding, linesToSkip, sortKey, where, pageSize, fetchSize);
    if (!validParameters(param))
      return false;
    String cacheKey = region + template;
    try
    {
      // ‚œ/pn“!,dbØÏ
      if (StringUtils.equals(BatchTemplateTypes.jdbcPartition.getValue(), param.getTemplate())
          || StringUtils.equals(BatchTemplateTypes.jdbcpaging.getValue(), param.getTemplate()))
      {
        // ¾n¯ƒØÏ
        setDbParameter(dbdriver, dburl, dbuser, dbpsw);
        saveDbConfig(dbdriver, dburl, dbuser, dbpsw);
      }
      
      // ‚œ/partition„,table
      if (StringUtils.equals(BatchTemplateTypes.jdbcPartition.getValue(), param.getTemplate()))
      {
        DataSource dataSource = null;
        DatabaseType type = null;
        // 2b!Þ¥pn
        if (jobItems.containsKey(cacheKey))
        {
          dataSource = (DataSource) (jobItems.get(cacheKey).jobContext.getBean("jdbcDataSource"));
          type = DatabaseType.fromMetaData(dataSource);
        }
        else
        {
          // Ù*ö,·Öpn
          ClassPathXmlApplicationContext jdbc = Util.initContext(true, "batch/job-context.xml",
              "batch/import-db-jdbc.xml");
          dataSource = (DataSource) jdbc.getBean("jdbcDataSource");
          type = DatabaseType.fromMetaData(dataSource);
          jdbc.close();
        }
        if (converters.containsKey(type))
          param.setTable(converters.get(type).converte(table, sortKey));
      }
      
      String jobXMLFile = generator.generateFileJob(region, param);
      ClassPathXmlApplicationContext jobContext = null;
      if (StringUtils.equals("file", template))
      {
        jobContext = Util.initContext(false, "batch/job-context.xml", jobXMLFile);
      }
      else
      {
        //   }ûÖpn“pn„dbMn
        jobContext = Util.initContext(false, "batch/job-context.xml", "batch/import-db-jdbc.xml", jobXMLFile);
      }
      jobContext.setParent(batchContext);
      jobContext.refresh();
      if (LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug("Job:" + region + " define success.");
      
      JobItem item = new JobItem();
      item.attributes = param;
      item.job = jobContext.getBean(Job.class);
      item.jobContent = jobXMLFile;
      item.jobContext = jobContext;
      jobItems.put(cacheKey, item);
      return true;
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().info("Job define error.", e);
      throw new GemliteException(e);
    }
  }
  
  /***
   * valid parameter for define a job
   * 
   * @param param
   * @return
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private boolean validParameters(BatchParameter param)
  {
    // ‡ö{‹„!Œ‡ö
    if (StringUtils.equals("file", param.getTemplate()))
    {
      if (StringUtils.isEmpty(param.getFile()))
      {
        LogUtil.getCoreLog().info("'file' should not be null");
        return false;
      }
      else
      {
        // 1.file://classpath:// 4„ô¥(
        String str = param.getFile();
        if (!str.startsWith("file") && !str.startsWith("classpath"))
        {
          File f = new File(str);
          str = f.toURI().toString();
          param.setFile(str);
        }
      }
    }
    
    if (StringUtils.isEmpty(param.getTable()))
    {
      String table = DomainRegistry.regionToTable(param.getRegion());
      if (StringUtils.isEmpty(table))
      {
        LogUtil.getCoreLog().info("region to table failed.");
        return false;
      }
      LogUtil.getCoreLog().info("Set 'table'='region'= " + table);
      param.setTable(table);
    }
    if (StringUtils.isEmpty(param.getColumns()))
    {
      IMapperTool tool = DomainRegistry.getMapperTool(param.getRegion());
      if (tool != null)
      {
        List<String> names = tool.getValueFieldNames();
        String clsName = tool.getValueClass().getName();
        if (names != null)
        {
          String str = names.toString();
          str = str.substring(1, str.length() - 1);
          LogUtil.getCoreLog().info("Columns from class(" + clsName + "):" + str);
        }
      }
      return false;
    }
    return true;
  }
  
}
