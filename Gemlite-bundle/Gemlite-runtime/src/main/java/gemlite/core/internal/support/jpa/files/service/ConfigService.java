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

import gemlite.core.internal.support.jpa.files.dao.GmConfigDao;
import gemlite.core.internal.support.jpa.files.domain.GmConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService
{
  @Autowired
  private GmConfigDao dao;
  
  public List<GmConfig> findAll()
  {
    return (List<GmConfig>)dao.findAll();
  }
  
  public GmConfig findOne(Long id)
  {
    return dao.findOne(id);
  }
  
  public void saveConf(GmConfig conf)
  {
    dao.save(conf);
  }
  
  public GmConfig findConfigByKey(String key)
  {
    GmConfig conf = new GmConfig();
    conf.setKey(key);
    GmConfig config = dao.getByKey(key);
    return config;
  }

  public List<GmConfig> findConfigByType(String type)
  {
    List<GmConfig> list =  dao.findByType(type);
    return list;
  }
  
  public Map<String, String> getConfig(String type)
  {
    List<GmConfig> configs = dao.findByType(type.toString());
    Map<String, String> map = new HashMap<String, String>();
    if(configs!=null && configs.size()>0)
    {
      for(GmConfig conf:configs)
      {
        map.put(conf.getKey(), conf.getValue());
      }
    }
    return map;
  }
}
