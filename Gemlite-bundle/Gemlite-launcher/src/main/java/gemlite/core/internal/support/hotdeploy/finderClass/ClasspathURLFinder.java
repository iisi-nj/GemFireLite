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

import gemlite.core.internal.support.hotdeploy.JarURLFinder;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ClasspathURLFinder implements JarURLFinder
{
  
  private URL core = null;
  private URL[] startup = null;
  
  
  
  @Override
  public void doFind()
  {
    core = findRuntimeURL();
    
    String userDir = System.getProperty("user.dir");
    String logicClassPath = userDir + "/target/classes";
    File f = new File(logicClassPath);
    try
    {
      URL logic = f.toURI().toURL();
      startup = new URL[]{logic};
    }
    catch (MalformedURLException e)
    {
    }
  }
  
  public URL findRuntimeURL()
  {
    try
    {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      URL pathURL = loader.getResource("gemlite/core/internal/GemliteHDModule.class");
      if (pathURL != null)
      {
       
        if("file".equals(pathURL.getProtocol()))
        {
          String str = pathURL.toString();
          int k=str.indexOf("/gemlite/core/internal/");
          str = str.substring(0,k);
          URL url = new URL(str);
          if(LogUtil.getCoreLog().isDebugEnabled())
            LogUtil.getCoreLog().debug("Found runtime jar:"+url);
          return url;
        }
        else if("jar".equals(pathURL.getProtocol()))
        {
          String str = pathURL.getPath();
          int k=str.indexOf("/gemlite/core/internal/");
          str = str.substring(0,k-1);
          URL url = new URL(str);
          if(LogUtil.getCoreLog().isDebugEnabled())
            LogUtil.getCoreLog().debug("Found runtime jar:"+url);
          return url;
        }
      }
    }
    catch (Exception e)
    {
    }
    return null;
    
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
