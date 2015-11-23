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

import gemlite.core.internal.support.system.ServerConfigHelper;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Run
{
  private int threadsNum = 1;
  private int queryCount = 1;
  private String testDataFile = "";
  private IQueryDao queryDao;
  private boolean show;
  
  public static void execute(boolean show,String[] args)
  {
    ServerConfigHelper.initConfig("env.properties");
    try
    {
      Run instance = new Run(Integer.parseInt(args[0]),Integer.parseInt(args[1]),(String)args[2],(String)args[3],show);
      instance.runTesting();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
  
  public Run(int threadsNum, int queryCount, String testDataFile, String queryClass,boolean show)
  {
      super();
      this.threadsNum = threadsNum;
      this.queryCount = queryCount;
      this.testDataFile = testDataFile;
      this.show = show;
      //根据class名称实例化dao
      try
      {
          Class<?> dao = Class.forName(queryClass);
          queryDao = (IQueryDao)dao.newInstance();
      }
      catch(ClassNotFoundException | InstantiationException | IllegalAccessException e)
      {
          e.printStackTrace();
      }
  }

  public void runTesting() throws Exception
  {
    System.out.println("Testing start ...");
    CyclicBarrier barrier = new CyclicBarrier(threadsNum);
    ExecutorService executor = Executors.newFixedThreadPool(threadsNum);
    List<Future<Long>> futureList = new ArrayList<Future<Long>>(threadsNum);
    
    //根据文件读取参数内容
    List<String[]> argsList = prepareTestData(this.testDataFile);
    
    try
    {
      for (int n = 0; n < threadsNum; n++)
      {
        QueryTester tester = new QueryTester(queryDao,show,barrier,queryCount,argsList);
        Future<Long> future = executor.submit(tester);
        futureList.add(future);
      }
      long totalRuntime = 0;
      List<Long> runtimes = new ArrayList<Long>();
      for (Future<Long> future : futureList)
      {
        long runtime = future.get();
        runtimes.add(runtime);
        totalRuntime += runtime;
      }
      long runtime = getMaxRuntime(runtimes);
      float tps = threadsNum * queryCount / (runtime * 1.0f) * 1000;
      System.out.println("total runtime:" + runtime + "ms   per get runtime:"
          + (totalRuntime / (threadsNum * queryCount * 1f)) + "ms, tps:" + tps);
    }
    finally
    {
      executor.shutdown();
    }
  }
  
  
  public List<String[]> prepareTestData(String testDataFile) throws IOException
  {
    System.out.println("Read file:" + testDataFile);
    LineNumberReader lr = new LineNumberReader(new FileReader(testDataFile));
    String line = lr.readLine();
    List<String[]> argsList = new ArrayList<String[]>();
    while (line != null)
    {
      String[] args = line.split(",");
      if (args.length>0)
      {
        argsList.add(args);
        line = lr.readLine();
      }
      else
        line = lr.readLine();
    }
    lr.close();
    System.out.println("Read file ok.");
    return argsList;
  }
  
  public long getMaxRuntime(List<Long> runtimes)
  {
    long maxRuntime = 0;
    for (long runtime : runtimes)
    {
      if (maxRuntime <= runtime)
        maxRuntime = runtime;
    }
    return maxRuntime;
  }
  
}
