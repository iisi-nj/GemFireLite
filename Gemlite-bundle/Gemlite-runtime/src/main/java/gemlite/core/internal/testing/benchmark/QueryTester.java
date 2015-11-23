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
package gemlite.core.internal.testing.benchmark;

import gemlite.core.util.LogUtil;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

public class QueryTester implements Callable<Long>
{
  private CyclicBarrier barrier;
  private long queryCount;
  private IQueryDao dao;
  private List<String[]> argsList;
  private boolean show;
  
  public IQueryDao getDao()
  {
    return dao;
  }
  
  public void setDao(IQueryDao dao)
  {
    this.dao = dao;
  }
  
  public QueryTester(IQueryDao dao,boolean show, CyclicBarrier barrier,long queryCount, List<String[]> argsList)
  {
    this.queryCount = queryCount;
    this.barrier = barrier;
    this.dao = dao;
    this.argsList = argsList;
    this.show = show;
  }
  
  @Override
  public Long call() throws Exception
  {
    barrier.await();
    LogUtil logUtil = LogUtil.newInstance();
    for (int i = 0; i < queryCount; i++)
    {
      try
      {
        //随机参数
        int n = argsList.size();
        int no = (int) (Math.random() * n);
        String[] args = argsList.get(no);
        dao.doQuery(show,args);
      }
      catch (Exception e)
      {
        LogUtil.getAppLog().error("query error:" + e.getMessage());
      }
    }
    return logUtil.cost();
  }
}
