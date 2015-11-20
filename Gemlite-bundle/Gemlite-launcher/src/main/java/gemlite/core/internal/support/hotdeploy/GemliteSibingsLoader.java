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
package gemlite.core.internal.support.hotdeploy;

import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.system.WorkPathHelper;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GemliteSibingsLoader extends URLClassLoader
{
  private Map<String, byte[]> classBytesMap = new ConcurrentHashMap<String, byte[]>();
  
  private final ThreadLocal<Boolean> alreadyScanned = new ThreadLocal<>();
  
  private URL url;
  
  private static URL blankLoaderPath()
  {
    File f = WorkPathHelper.verifyPath(".loader");
    try
    {
      return f.toURI().toURL();
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public GemliteSibingsLoader()
  {
    this(blankLoaderPath());
  }
  
  public GemliteSibingsLoader(URL url)
  {
    this(url, getSystemClassLoader());
  }
  
  public GemliteSibingsLoader(ClassLoader parent)
  {
    this(blankLoaderPath(), parent);
  }
  
  public GemliteSibingsLoader(URL url, ClassLoader parent)
  {
    super(new URL[] { url }, parent);
    alreadyScanned.set(false);
    this.url = url;
  }
  
  public URL getURL()
  {
    return url;
  }
  
  public void clean()
  {
    classBytesMap.clear();
    classBytesMap = null;
    try
    {
      super.close();
    }
    catch (IOException e)
    {
      LogUtil.getCoreLog().info("URLClassLoader can not close successful", e);
    }
    
  }
  
  public void addDynamicClasses(Map<String, byte[]> classes)
  {
    classBytesMap.putAll(classes);
  }
  
  public Map<String, byte[]> getDynaimcClasses()
  {
    return classBytesMap;
  }
  
  public void addDynamicClass(String name, byte[] bytes)
  {
    classBytesMap.put(name, bytes);
  }
  
  @Override
  protected Class<?> findClass(String className) throws ClassNotFoundException
  {
    if (classBytesMap.containsKey(className))
    {
      byte[] bytes = classBytesMap.get(className);
      // classBytesMap.remove(className);
      if (LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug("Dynamic Class: " + className + " by " + this);
      Class<?> clazz = defineClass(className, bytes, 0, bytes.length);
//      classBytesMap.remove(className);
      return clazz;
    }
    else
    {
      try
      {
        Class<?> cls = super.findClass(className);
        return cls;
      }
      catch (ClassNotFoundException e)
      {
        throw e;
      }
    }
  }
  
  @Override
  public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException
  {
    Class<?> clazz = loadClassWithSibings(className, resolve, true);
    return clazz;
  }
  
  public boolean isScanned()
  {
    Boolean b = alreadyScanned.get();
    return b == null ? false : b;
  }
  
  private Class<?> loadSibings(String className, boolean resolve) throws ClassNotFoundException
  {
    Class<?> clazz = null;
    Iterator<GemliteSibingsLoader> it = GemliteContext.getLoaders();
    while (it.hasNext())
    {
      GemliteSibingsLoader loader = it.next();
      if (!loader.isScanned())
      {
        try
        {
          clazz = loader.loadClassWithSibings(className, resolve, false);
        }
        catch (ClassNotFoundException | NoClassDefFoundError e)
        {
        }
        if (clazz != null)
        {
          if (LogUtil.getCoreLog().isDebugEnabled())
            LogUtil.getCoreLog().debug(className + " loadSibings," + clazz.getClassLoader());
          return clazz;
        }
      }
    }
    return null;
  }
  
  public Class<?> loadClassWithSibings(String className, boolean resolve, boolean findSibings)
      throws ClassNotFoundException
  {
    Class<?> clazz = null;
    // first check cache
    clazz = findLoadedClass(className);
    if (clazz != null)
    {
      LogUtil.getCoreLog().trace("Hit cache, class:{} loader:{}", className, clazz.getClassLoader());
      return clazz;
    }
    // then parent
    try
    {
      ClassLoader ploader = getParent();
      if (ploader != null)
      {
        clazz = ploader.loadClass(className);
        if (clazz != null)
        {
          LogUtil.getCoreLog().trace("Parent loader, class:{} loader:{}", className, ploader);
          return clazz;
        }
      }
      else
      {
        // then bootstrap
        clazz = super.loadClass(className, false);
        if (clazz != null)
        {
          LogUtil.getCoreLog().trace("BootstrapClass:{}", className);
          return clazz;
        }
      }
    }
    catch (ClassNotFoundException e)
    {
    }
    
    // native
    try
    {
      clazz = findClass(className);
      if (resolve)
        resolveClass(clazz);
    }
    catch (ClassNotFoundException | NoClassDefFoundError e)
    {
      if (!findSibings)
        throw e;
    }
    if (clazz != null)
    {
      if (LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug(className + " findClass," + clazz.getClassLoader());
      return clazz;
    }
    
    // siblings
    alreadyScanned.set(true);
    clazz = loadSibings(className, resolve);
    alreadyScanned.set(false);
    
    return clazz;
  }
  
}
