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
package gemlite.core.internal.support.jpa.files.dao;


import gemlite.core.util.LogUtil;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.h2.jdbc.JdbcSQLException;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GmBatchDaoImpl implements GmBatchExtDao
{
  @PersistenceContext
  protected EntityManager em;
  
  @Override
  public int countJobExecutions()
  {
    Query q = em.createNativeQuery("SELECT COUNT(1) from BATCH_JOB_EXECUTION");
    BigInteger count =  (BigInteger) q.getSingleResult();
    return count.intValue();
  }
  
  @Override
  public List<Map> queryJobExecutions(String status)
  {
    String where  = "";
    if(!StringUtils.isEmpty(status))
    {
        where = " where e.status='"+status+"'";
    }
    String getjobs = "select e.job_execution_id as job_execution_id, e.start_time as start_time, e.end_time, e.status, e.exit_code, e.exit_message, e.create_time, e.last_updated, e.version, i.job_instance_id, i.job_name from batch_job_execution e join batch_job_instance i on e.job_instance_id=i.job_instance_id "+where+" order by job_execution_id desc";
    Query query=  em.createNativeQuery(getjobs);
    SQLQuery nativeQuery= query.unwrap(SQLQuery.class);
    nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
    List<Map> list = null;
    try
    {
      list = nativeQuery.list();
    }
    catch(Exception e)
    {
      LogUtil.getCoreLog().warn("SQL {} Error {}",getjobs,e.getMessage());
    }
    //对结果集进行处理
    List<Map> newlist = new ArrayList<Map>();
    if(list!=null)
    {
      for(Map map :list)
      {
        //计算duration
        Timestamp t1 = (Timestamp)map.get("START_TIME");
        Timestamp t2 = (Timestamp)map.get("END_TIME");
        String duration = "";
        if(t2!=null)
        duration = format(new Date(t2.getTime()-t1.getTime()), "HH:mm:ss") ;
        
        Map newmap = new HashMap<String, Object>();
        newmap.put("duration", duration);
        
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while(it.hasNext())
        {
          Entry<String, Object> entry = it.next();
          newmap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        newlist.add(newmap);
      }
    }
      
    return newlist;
  }
  
  
  @Override
  public int countRunningJob()
  {
    String countRunningJob = "select count(1) from batch_job_execution where status = 'STARTED'";
    Query query=  em.createNativeQuery(countRunningJob);
    BigInteger count = (BigInteger)query.getSingleResult();
    return count.intValue();
  }
  
  @Override
  public Map queryJobExecutionById(Long executionId)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT a.*,b.job_name");
    sb.append(" from BATCH_JOB_EXECUTION a left join batch_job_instance b on a.job_instance_id = b.job_instance_id where a.JOB_EXECUTION_ID = ?");
    
    Query query=  em.createNativeQuery(sb.toString());
    SQLQuery nativeQuery= query.unwrap(SQLQuery.class);
    nativeQuery.setParameter(0, executionId);
    nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
    List<Map> list = nativeQuery.list();
    
    return (list == null || list.isEmpty()) ? null : toLowerCaseKey(list).get(0);
  }
  
  
  private List<Map> toLowerCaseKey(List<Map> oldList)
  {
    List<Map> newlist = new ArrayList<Map>();
    if(oldList!=null)
      for(Map map :oldList)
      {
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        Map newmap = new HashMap<String, Object>();
        while(it.hasNext())
        {
          Entry<String, Object> entry = it.next();
          newmap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        newlist.add(newmap);
      }
    return newlist;
  }
  
  @Override
  public List<Map> queryStepNamesForJob(String jobName)
  {
    String sql = "SELECT E.JOB_EXECUTION_ID, E.START_TIME, E.END_TIME, E.STATUS, E.EXIT_CODE, E.EXIT_MESSAGE, E.CREATE_TIME, E.LAST_UPDATED, E.VERSION, I.JOB_INSTANCE_ID, I.JOB_NAME"
        + " FROM BATCH_JOB_EXECUTION E, BATCH_JOB_INSTANCE I WHERE E.JOB_INSTANCE_ID=I.JOB_INSTANCE_ID and I.JOB_NAME=? ORDER BY JOB_EXECUTION_ID DESC";
    
    Query query=  em.createNativeQuery(sql);
    SQLQuery nativeQuery= query.unwrap(SQLQuery.class);
    nativeQuery.setParameter(0, jobName);
    nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
    List<Map> list = nativeQuery.list();
    
    return toLowerCaseKey(list);
  }
  
  @Override
  public List<Map> queryStepExecutionsById(Long jobExecutionId)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT STEP_EXECUTION_ID, STEP_NAME, START_TIME, END_TIME, STATUS, COMMIT_COUNT,");
    sb.append(" READ_COUNT, FILTER_COUNT, WRITE_COUNT, EXIT_CODE, EXIT_MESSAGE, READ_SKIP_COUNT,");
    sb.append(" WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, LAST_UPDATED, VERSION from ");
    sb.append(" BATCH_STEP_EXECUTION where JOB_EXECUTION_ID = ?");
    sb.append(" order by STEP_EXECUTION_ID");
    
    Query query=  em.createNativeQuery(sb.toString());
    SQLQuery nativeQuery= query.unwrap(SQLQuery.class);
    nativeQuery.setParameter(0, jobExecutionId);
    nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
    
    List<Map> list = nativeQuery.list();
    return toLowerCaseKey(list);
  }
  
  private String format(Date dt, String fmtStr)
  {
    if (dt == null)
      return "";
    String str = DateFormatUtils.format(dt, fmtStr);
    return str;
  }
}
