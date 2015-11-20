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
package gemlite.core.internal.admin;

import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheClosedException;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.distributed.DistributedMember;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AdminUtil
{
  public static String IPSUFFIX = "-";
  /**
   * 9nmemberId�DistributedMember,(ip��c
   * 
   * @param cache
   * @param memberNameOrId
   * @return
   */
  public static DistributedMember getDistributedMemberById(Cache cache, String memberId, String ip)
  {
    DistributedMember memberFound = null;
    if (memberId != null)
    {
      if (ip != null)
      {
        ip = ip.replace(IPSUFFIX, "");
        int index = ip.indexOf(" ");
        ip = ip.substring(0, index);
        ip = ip.trim();
      }
      String ipMemberId = replaceIP(memberId, ip);
      Set<DistributedMember> memberSet = new HashSet(((InternalDistributedSystem) cache.getDistributedSystem())
          .getDistributionManager().getDistributionManagerIds());
      for (DistributedMember member : memberSet)
      {
        if (member.getId().equals(memberId) || member.getId().equals(ipMemberId))
        {
          memberFound = member;
          break;
        }
      }
    }
    return memberFound;
  }
  
  /**
   * �memberId,��hostname,v(ip�b���memberId,/b$�<
   * dp64-12(8521)<v56>:26740/39191 gsong-PC<v1>:48521/1123
   * 
   * @param member
   * @param ip
   * @return
   */
  private static String replaceIP(String memberId, String ip)
  {
    if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(ip))
      return memberId;
    // , e��M  
    int indexLeft = memberId.indexOf("<");
    if (indexLeft < 0)
      return memberId;
    String suf = memberId.substring(indexLeft);
    String pre = memberId.substring(0, indexLeft);
    // ��M �b/&+��(),��e
    int index = pre.indexOf("(");
    String center = "";
    if (index > 0)
    {
      center = pre.substring(index);
    }
    return ip + center + suf;
  }
  
  /**
   * �@	RegionNames,Set<String>
   * 
   * @param cache
   * @return
   */
  public static TreeSet<String> getRegionNames(Cache cache)
  {
    try
    {
      Set<Region<?, ?>> regions = cache.rootRegions();
      if ((regions.isEmpty()) || (regions == null))
      {
        return null;
      }
      else
      {
        TreeSet regionInformationSet = new TreeSet();
        for (Region region : regions)
        {
          regionInformationSet.add(region.getFullPath().substring(1));
        }
        return regionInformationSet;
      }
    }
    catch (CacheClosedException e)
    {
      LogUtil.getAppLog().error("error CacheClosedException:", e);
    }
    catch (Exception e)
    {
      LogUtil.getAppLog().error("error:", e);
    }
    return null;
  }
  
  /**
   * �ip
   * 
   * @return
   */
  public static String getIp()
  {
    String ip = System.getProperty(ITEMS.BINDIP.name());
    if (StringUtils.isEmpty(ip))
    {
      try
      {
        InetAddress addr = InetAddress.getLocalHost();
        ip = addr.getHostAddress().toString();
        ip += IPSUFFIX; // �d͹���
      }
      catch (Exception e)
      {
        ip = e.getMessage();
        LogUtil.getAppLog().error("get Ip error:", e);
      }
    }
    return ip;
  }
  
  /**
   * �@	�memberIds
   * @param cache
   * @return
   */
  public static Set getAllMemberIds(Cache cache)
  {
    TreeSet<String> set = new TreeSet<String>();
    Set<DistributedMember> memberSet = new HashSet(((InternalDistributedSystem) cache.getDistributedSystem())
        .getDistributionManager().getDistributionManagerIds());
    for (DistributedMember member : memberSet)
    {
      set.add(member.getId());
    }
    return set;
  }
}
