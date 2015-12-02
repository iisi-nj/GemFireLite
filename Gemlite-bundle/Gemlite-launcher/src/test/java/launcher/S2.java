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
package launcher;

import gemlite.core.commands.DataStore;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.data.gemfire.RegionAttributesFactoryBean;

import com.gemstone.gemfire.cache.AttributesFactory;
import com.gemstone.gemfire.cache.CacheClosedException;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.DataPolicy;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolFactory;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;
import com.gemstone.gemfire.internal.cache.InternalRegionArguments;
import com.gemstone.gemfire.internal.cache.PoolManagerImpl;

public class S2
{
  public static void main(String[] args) throws Exception
  {
    DataStore.getInstance().basicStart(args);
    ClientCache cc = connectS1();
    System.out.println("cc:"+cc);
    DataStore.getInstance().waitForComplete();
  }
  public static ClientCache connectS1() throws Exception
  {
    GemFireCacheImpl gc=(GemFireCacheImpl)CacheFactory.getAnyInstance();
    InputStream in= S2.class.getResourceAsStream("f1.properties");
    Properties prop=new Properties();
    prop.load(in);
    String ip = ServerConfigHelper.getConfig(ITEMS.BINDIP);
    PoolFactory pf= PoolManagerImpl.getPMI().createFactory();
    pf.addLocator(ip, 12346);
    Pool p= pf.create("pool2");
    Object res = FunctionService.onServer(p).execute(new test1F()).getResult();
    String ss=ServerConfigHelper.getProperty("MEMBER_ID");
    System.out.println(p+" "+res+" from "+ss);
    
    
    gc.initializeClientRegionShortcuts(gc);
    ClientRegionFactory cf= gc.createClientRegionFactory(ClientRegionShortcut.PROXY);
    cf.setPoolName("pool2");
    Region s1=cf.create("s1");
    Region r1 = cf.createSubregion(s1,"product");
    Region r2 = cf.createSubregion(s1,"customer");
    Region s1product=cf.create("product");
    res = FunctionService.onRegion(s1product).execute(new test1F()).getResult();
    System.out.println(p+" "+res+" from "+ss);
    
//    ClientCacheFactory ccf = new ClientCacheFactory(prop);
//    ccf.addPoolLocator(ip, 12346);
//    ClientCache cc= ccf.create();
    return null;
  }
//  public Region createClientRegionOnServer(GemFireCacheImpl cc)
//  {
//    try
//    {
//      AttributesFactory rf = new AttributesFactory();
//      rf.setDataPolicy(DataPolicy.REPLICATE);
////      rf.setScope(Scope.DISTRIBUTED_NO_ACK);
//      InternalRegionArguments internalArgs = new InternalRegionArguments();
//      internalArgs.setIsUsedForMetaRegion(true);
//      cc.createVMRegion(MGM_REGION_NAME, rf.create(), internalArgs);
//    }
//    catch (CacheClosedException e)
//    {
//      LogUtil.getCoreLog().error("Cache closed,{}", e.getMessage());
//    }
//    catch (Exception e)
//    {
//      LogUtil.getCoreLog().warn("Management region cretae error.", e);
//    }
//    
//    LogUtil.getCoreLog().debug("Management region " + MGM_REGION_NAME + " created.");
//    return mgmRegion;
//  }
  
}
