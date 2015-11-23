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
package gemlite.core.internal.admin.service;

import gemlite.core.annotations.admin.AdminService;
import gemlite.core.internal.admin.AbstractRemoteAdminService;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.distributed.DistributedMember;

@AdminService(name = "ConfService")
public class ConfService extends AbstractRemoteAdminService<Map<String, Object>, Object>
{
  
  @Override
  public Object doExecute(Map<String, Object> args)
  {
    Cache cache = CacheFactory.getAnyInstance();
    // 获取配置参数
    String msg = getSystemInfo(cache);
    return msg;
  }

  @Override
  public void doExeception(Map<String, Object> args)
  {
    // TODO Auto-generated method stub
    
  }
  /**
   * 获取本机sever信息
   * 
   * @param cache
   * @return
   */
  public String getSystemInfo(Cache cache)
  {
    StringBuilder sb = new StringBuilder();
    String ip = System.getProperty(ITEMS.BINDIP.name());
    // 1.取得当前log级别
    sb.append("\n===================================================================\n");
    DistributedMember member = cache.getDistributedSystem().getDistributedMember();
    sb.append("IP:" + ip + " host:" + member.getHost() + "\n");
    sb.append("node:" + System.getProperty(ITEMS.NODE_NAME.name()) + "\n");
    sb.append("server ID:" + member.getId() + "\n");
    sb.append("server logLevel:" + cache.getLogger().getHandler().getLevel().getName() + "\n");
//    sb.append("log4j logLevel:" + Logger.get Logger.getRootLogger().getLevel().toString() + "\n");
    
    String location = System.getenv("GS_WORK");
    if (StringUtils.isEmpty(location))
    {
      try
      {
        ResourceBundle bundle = ResourceBundle.getBundle("env");
        location = bundle.getString("GS_WORK");
      }
      catch (Exception e)
      {
        LogUtil.getAppLog().error("env:" + e.getMessage());
      }
    }
    // 取得当前work目录
    sb.append("disk:" + location + "\n");
    return sb.toString();
  }
}
