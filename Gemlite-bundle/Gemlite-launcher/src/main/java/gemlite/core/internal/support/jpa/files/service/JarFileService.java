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

import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;
import gemlite.core.internal.support.hotdeploy.scanner.RegistryMatchedContext;
import gemlite.core.internal.support.jpa.files.dao.ActiveFileDao;
import gemlite.core.internal.support.jpa.files.dao.JarFileDao;
import gemlite.core.internal.support.jpa.files.domain.ActiveFileId;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JarFileService
{
  @Autowired
  private JarFileDao dao;
  
  @Autowired
  private ActiveFileDao daoActive;
  
  @Transactional
  public void save(ReleasedJarFile jarFile)
  {
    dao.save(jarFile);
    // 如果数据充足,则保存数据
    if (StringUtils.isNotEmpty(jarFile.getModuleName()) && jarFile.getModuleType() != null)
    {
      ActiveFileId af = new ActiveFileId();
      af.setFileId(jarFile.getFileId());
      af.setModuleName(jarFile.getModuleName());
      af.setModuleType(jarFile.getModuleType());
      daoActive.save(af);
    }
  }
  
  @Transactional
  public void delete(ReleasedJarFile jarFile)
  {
    dao.delete(jarFile);
  }
  
  public void updateActiveFileId(ActiveFileId af)
  {
    daoActive.save(af);
  }
  
  /***
   * runtime 在第一个
   * 
   * @return
   */
  public List<ReleasedJarFile> findActiveFiles()
  {
    List<ActiveFileId> list = daoActive.findAll();
    List<Long> ids = new ArrayList<>();
    for (ActiveFileId item : list)
    {
      ids.add(item.getFileId());
      if (LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug("ActiveFileId:" + item.getFileId() + "," + item.getModuleName());
    }
    
    Iterator<ReleasedJarFile> it = dao.findAll(ids).iterator();
    List<ReleasedJarFile> result = new ArrayList<>();
    while (it.hasNext())
    {
      ReleasedJarFile f = it.next();
      result.add(f);
      if (LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug("ReleasedJarFile:" + f.getFileId() + "," + f.getModuleName());
    }
    return result;
  }
  
  public ActiveFileId findActiveByName(String moduleName)
  {
    return daoActive.findOne(moduleName);
  }
  
  public List<ReleasedJarFile> findAll()
  {
    return dao.findAll();
  }
  
  public boolean splitVersion(ReleasedJarFile jarFile, String fname)
  {
    String reg = "([-a-zA-Z]+)\\-([0-9].+)\\.jar";
    Pattern pattern = Pattern.compile(reg);
    Matcher matcher = pattern.matcher(fname);
    if (matcher.find() && matcher.groupCount() == 2)
    {
      jarFile.setFileName(matcher.group(1));
      jarFile.setVersion(matcher.group(2));
    }
    else
    {
      jarFile.setFileName(fname);
    }
    return true;
  }
  
  public boolean checkDuplicate(ReleasedJarFile jarFile)
  {
    ReleasedJarFile old = dao.getByFileNameAndVersion(jarFile.getFileName(), jarFile.getVersion());
    if (old != null)
    {
      if (old.getMd5_str().equals(jarFile.getMd5_str()))
        return false;
      jarFile.setFileId(old.getFileId());
      jarFile.setUpdate_count(old.getUpdate_count() + 1);
      if (StringUtils.isEmpty(jarFile.getDescription()) && StringUtils.isNotEmpty(old.getDescription()))
        jarFile.setDescription(old.getDescription());
    }
    return true;
  }
  
  public ReleasedJarFile deploy(URL url)
  {
    RegistryMatchedContext m = null;
    try
    {
      m = GemliteDeployer.getInstance().scan(url);
      if (m == null)
      {
        throw new GemliteException("No module define found.");
      }
      LogUtil.getCoreLog().info("Module " + m.getModuleName() + " type:" + m.getModuleType());
      ReleasedJarFile jarFile = new ReleasedJarFile();
      jarFile.setModuleName(m.getModuleName());
      jarFile.setModuleType(m.getModuleType());
      // Gemlite-runtime-0.0.1-SNAPSHOT.jar
      String fname = null;
      try
      {
        fname = url.toURI().toURL().getFile();
      }
      catch (MalformedURLException | URISyntaxException e)
      {
        throw new GemliteException("Not a vaild url:" + url, e);
      }
      if (!splitVersion(jarFile, fname))
      {
        LogUtil.getCoreLog().warn("Cann't find version no for file {}.", fname);
      }
      byte[] bytes = null;
      try
      {
        InputStream in = url.openStream();
        bytes = new byte[in.available()];
        in.read(bytes);
        in.close();
      }
      catch (IOException e)
      {
        throw new GemliteException(fname + " can not read bytes.", e);
      }
      jarFile.setFileName(fname);
      jarFile.setContent(bytes);
      jarFile.setMd5_str(Util.makeMD5String(bytes));
      jarFile.setUpdate_count(1);
      jarFile.setUpload_time(new Date());
      
      if (!checkDuplicate(jarFile))
      {
        LogUtil.getCoreLog().warn("{} has no change.", jarFile.getFileName());
        // throw new GemliteException(jarFile.getFileName() + " has no change.");
      }
      return jarFile;
    }
    finally
    {
      if (m != null && m.getLoader() != null)
        m.getLoader().clean();
    }
  }
  
  public ReleasedJarFile getFileById(Long id)
  {
    return dao.findOne(id);
  }
  
}
