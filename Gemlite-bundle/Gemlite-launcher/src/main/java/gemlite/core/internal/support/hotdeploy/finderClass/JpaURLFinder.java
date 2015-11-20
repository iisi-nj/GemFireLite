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
package gemlite.core.internal.support.hotdeploy.finderClass;

import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.hotdeploy.JarURLFinder;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.internal.support.jpa.files.service.JarFileService;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.support.system.WorkPathHelper;
import gemlite.core.util.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JpaURLFinder implements JarURLFinder
{
  
  private URL core = null;
  private URL[] startup = null;
  
  public static void main(String[] args)
  {
    ServerConfigHelper.initConfig();
    File ff = WorkPathHelper.verifyPath("/.jarCache/s1");
    try
    {
      JpaURLFinder f = new JpaURLFinder();
      URL url  =f.createTempJarFile("",null);
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public final static URL createTempJarFile(String moduleName, byte[] bytes)
  {
    try
    {
      String node = ServerConfigHelper.getConfig(ITEMS.NODE_NAME);
      File tempPath = WorkPathHelper.verifyPath("/temp/" + node);
      File tempFile = File.createTempFile(moduleName, ".jar", tempPath);
      tempFile.deleteOnExit();
      
      BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(tempFile));
      bo.write(bytes);
      bo.close();
      return tempFile.toURI().toURL();
    }
    catch (Exception e)
    {
      throw new GemliteException(moduleName,e);
    }
  }
  
  @Override
  public void doFind()
  {
    JarFileService srv = JpaContext.getService(JarFileService.class);
    List<ReleasedJarFile> files = srv.findActiveFiles();
    String node = ServerConfigHelper.getConfig(ITEMS.NODE_NAME);
    WorkPathHelper.verifyPath("/temp/" + node, true);
    
    List<URL> urls = new ArrayList<>();
    for (ReleasedJarFile file : files)
    {
      URL url = createTempJarFile(file.getModuleName(), file.getContent());
      if(LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug("Create temp file:"+url);
      if (url == null)
        continue;
      if (file.getModuleType() == ModuleType.RUNTIME)
        core = url;
      else
        urls.add(url);
    }
    if (urls != null && urls.size() > 0)
    {
      startup = new URL[urls.size()];
      startup = urls.toArray(startup);
    }
  }
  
  @Override
  public URL[] getURLsOnStartup()
  {
    return startup;
  }
  
  @Override
  public URL getCoreJarUrl()
  {
    return core;
  }
  
}
