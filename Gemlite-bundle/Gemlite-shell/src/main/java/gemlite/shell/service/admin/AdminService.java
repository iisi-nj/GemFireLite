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
package gemlite.shell.service.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import gemlite.shell.admin.dao.AdminDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.Pool;

@Component
public class AdminService
{
  @Autowired
  private AdminDao dao;

  public String doReblance()
  {
    return dao.doReblance();
  }

  public String doClean(String regionName)
  {
    return dao.doClean(regionName);
  }

  public void setLevel(String level)
  {
    dao.setLevel(level);
  }

  public void lookConf()
  {
    dao.lookConf();
  }

  public void processRegion()
  {
    dao.processRegion();
  }

  public void processReadRegion(ClientCache clientCache, String rr)
  {
    dao.processReadRegion(clientCache, rr);
  }

  public void refreshLog4j()
  {
    dao.refreshLog4j();
  }

  public void listmissingdiskstores()
  {
    dao.listmissingdiskstores();
  }

  public List<HashMap<String,Object>> sizeM(String regionName)
  {
    return dao.sizeM(regionName);
  }

  public String prB(String regionName)
  {
    return dao.prB(regionName);
  }

  public Pool getClientPool()
  {
    return dao.getClientPool();
  }
}
