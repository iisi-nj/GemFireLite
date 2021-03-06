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
package gemlite.core.internal.support.jpa.files.service;

import gemlite.core.internal.admin.measurement.ScannedMethodItem;
import gemlite.core.internal.support.jpa.files.dao.GmCheckPointsDao;
import gemlite.core.internal.support.jpa.files.domain.GmCheckPoints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckPointsService
{
  @Autowired
  private GmCheckPointsDao dao;
  
  public List<GmCheckPoints> findAll()
  {
    return (List<GmCheckPoints>) dao.findAll();
  }
  
  public GmCheckPoints findOne(Long id)
  {
    return dao.findOne(id);
  }
  
  public void save(GmCheckPoints vo)
  {
    dao.save(vo);
  }
  
  public List<GmCheckPoints> findByModule(String module)
  {
    return dao.findByModule(module);
  }
  
  /***
   * 根据module名称取所有的需要添加checkpoints列表
   * [module:[class,[method]]]
   */
  public Map<String, Set<ScannedMethodItem>> findMapByModule(String module)
  {
    Map<String, Set<ScannedMethodItem>> m = new HashMap<>();
    List<GmCheckPoints> list = dao.findByModule(module);
    for (GmCheckPoints cp : list)
    {
      Set<ScannedMethodItem> s1 = m.get(cp.getClassname());
      if (s1 == null)
      {
        s1 = new HashSet<ScannedMethodItem>();
        m.put(cp.getClassname(), s1);
      }
      ScannedMethodItem item = new ScannedMethodItem(cp.getClassname(),cp.getMethod(),cp.getDesc());
      s1.add(item);
    }
    return m;
  }
  
  /***
   * 找到所有的需要添加checkpoints列表
   * [module:[class,[method]]]
   */
  public Map<String, Map<String, Set<String>>> findAllCheckPoints()
  {
    Map<String, Map<String, Set<String>>> m = new HashMap<String, Map<String, Set<String>>>();
    List<GmCheckPoints> list = (List<GmCheckPoints>) dao.findAll();
    for (int i = 0; i < list.size(); i++)
    {
      GmCheckPoints cp = list.get(i);
      if (m.containsKey(cp.getModule()))
      {
        Map<String, Set<String>> m1 = m.get(cp.getModule());
        if (m1.containsKey(cp.getClassname()))
        {
          Set<String> s = m1.get(cp.getClassname());
          s.add(cp.getMethod());
        }
        else
        {
          Set<String> s = new HashSet<String>();
          s.add(cp.getMethod());
          m1.put(cp.getClassname(), s);
        }
      }
      else
      {
        Map<String, Set<String>> m1 = new HashMap<String, Set<String>>();
        Set<String> s = new HashSet<String>();
        s.add(cp.getMethod());
        m1.put(cp.getClassname(), s);
        m.put(cp.getModule(), m1);
      }
    }
    return m;
  }
  
  public List<GmCheckPoints> findByModuleAndServicename(String module, String servicename)
  {
    return dao.findByModuleAndServicename(module, servicename);
  }
  
  public void delete(Long id)
  {
    dao.delete(id);
  }
  
  public void deleteByModuleAndServicename(String module, String servicename)
  {
    List<GmCheckPoints> list = dao.findByModuleAndServicename(module, servicename);
    dao.delete(list);
  }
}
