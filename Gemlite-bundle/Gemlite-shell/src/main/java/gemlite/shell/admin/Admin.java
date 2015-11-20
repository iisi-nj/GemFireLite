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
package gemlite.shell.admin;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.shell.admin.dao.AdminDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolManager;

public class Admin
{
  @Option(name = "-l", usage = "set cache loglevel:java.util.logging.Level support[OFF,SEVERE,WARNING,INFO,CONFIG,FINE,FINER]")
  private String cachelog = "";
  @Option(name = "-c", usage = "clean region with cancel,eg:-c regionName")
  private String clean = "";
  @Option(name = "-rebalance", usage = "rebalance,eg:-rebalance,without any parameter")
  private boolean rebalance = false;
  @Option(name = "-d", usage = "deserilze region,eg:-d,without any parameter")
  private boolean deserilzeFlag = false;
  @Option(name = "-rr", usage = "read region,regionName")
  private String rr =""; 
  @Option(name = "-conf", usage = "look one server config info")
  private boolean conf; 
  @Option(name = "-refreshlog", usage = "refresh log4j config")
  private boolean refreshlog = false; 
  @Option(name = "-listmissingdiskstores", usage = "list missing disk stores")
  private boolean listmiss = false; 
  @Option(name = "-sizem", usage = "size -m regionName")
  private String sizem = ""; 
  @Option(name = "-prb", usage = "pr -b regionName")
  private String prb = ""; 
  @Option(name = "-export", usage = "export data,without any parameter")
  private boolean export = false;
  @Option(name = "-import", usage = "import data,without any parameter")
  private boolean importdata = false;
  
  
  private AdminDao dao;
  public static void main(String[] args)
  {
    try
    {
      ServerConfigHelper.initConfig("env.properties");
      Admin bean = new Admin();
      bean.printMenu(args);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private void resetParam()
  {
    rebalance = false;
    cachelog = "";
    clean = "";
    conf = false;
    deserilzeFlag = false;
    rr = "";
    refreshlog = false;
    listmiss = false;
    sizem = "";
    prb = "";
    export = false;
    importdata = false;
  }
  
  public void printMenu(String[] args) throws IOException, CmdLineException
  {
    String resource = ("ds-client.xml");
    ClassPathXmlApplicationContext mainContext = new ClassPathXmlApplicationContext(new String[] { resource }, false);
    mainContext.setValidating(true);
    mainContext.refresh();
    //	(Set<String> keys= mainContext.getBeansOfType(Region.class).keySet();
    
    ClientCache clientCache = ClientCacheFactory.getAnyInstance();
    dao = new AdminDao();
    Map<String, Pool> pools = PoolManager.getAll();
    dao.setClientPool(pools.entrySet().iterator().next().getValue());
    CmdLineParser parser = new CmdLineParser(this);
    do
    {
      System.out.println("*************************************************************\n Administrator args:");
      parser.printExample(ExampleMode.ALL);
      parser.printUsage(System.out);
      System.out.println("Press \"X\" to exit. Any other key to continue.");
      System.out.println("Input your option: ");
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      String line = bufferedReader.readLine();
      if (line.equalsIgnoreCase("X"))
        break;
      resetParam();
      String[] inputArgs = line.split(" ");
      if (inputArgs.length > 0 && inputArgs.length <= 2)
      {
        try
        {
          parser.parseArgument(inputArgs);
        }
        catch (Exception e)
        {
          System.err.println("Option error!");
        }
        try
        {
        doCommand(dao, clientCache);
        }
        catch (Exception e) 
        {
          e.printStackTrace();
        }
      }
      else if (inputArgs.length > 2)
      {
        System.out.println("Please choose only one option each time.");
      }
    }
    while (true);
    mainContext.close();
  }
  
  private void doCommand(AdminDao dao,ClientCache clientCache) throws IOException
  {
    if (rebalance)
    {
      dao.doReblance();
    }
    else if (!StringUtils.isEmpty(cachelog))
    {
      dao.setLevel(cachelog);
    }
    else if (!StringUtils.isEmpty(clean))
    {
      dao.doClean(clean);
    }
    else if(deserilzeFlag)
    {
      dao.processRegion();
    }
    else if(!StringUtils.isEmpty(rr))
    {
      dao.processReadRegion(clientCache,rr);
    }
    else if(conf)
    {
      //åMnáo
      dao.lookConf();
    }
    else if(refreshlog)
    {
      dao.refreshLog4j();
    }
    else if(listmiss)
    {
      dao.listmissingdiskstores();
    }
    else if(!StringUtils.isEmpty(sizem))
    {
      dao.sizeM(sizem);
    }
    else if(!StringUtils.isEmpty(prb))
    {
      dao.prB(prb);
    }
  }
}
