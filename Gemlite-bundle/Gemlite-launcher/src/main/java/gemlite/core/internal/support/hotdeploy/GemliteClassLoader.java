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
import gemlite.core.internal.support.context.IIndexContext;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;

import org.springframework.util.ResourceUtils;

/***
 * 1.GemliteJarLoader之间为平行关系
 * 2.GemliteJarLoader负责载入各类可热部署的jar包
 * 3.GemliteJarLoader的父均为GemliteClassLoader
 * 4.
 * 
 * @author ynd
 * 
 */
public class GemliteClassLoader extends ClassLoader
{
  
  private final static GemliteClassLoader instance = new GemliteClassLoader(Thread.currentThread().getContextClassLoader());
  
  private GemliteClassLoader(ClassLoader loader)
  {
    super(loader);
  }
  
  public final static GemliteClassLoader getInstance()
  {
    return instance;
  }
  
  @SuppressWarnings({ "rawtypes", "unused" })
  public static void main(String[] args) throws Exception
  {
    
    ServerConfigHelper.initConfig();
    ServerConfigHelper.initLog4j("classpath:log4j2-server.xml");
    LogUtil.getCoreLog().trace("trace");
    LogUtil.getCoreLog().info("info");
    
    System.out.println(instance.getParent());
    File f = ResourceUtils.getFile("d:/tmp/g1.jar");
    System.out.println(f.toURI().toURL() + " " + f.exists());
    GemliteSibingsLoader loader = new GemliteSibingsLoader(f.toURI().toURL());
    GemliteContext.getInstance().putLodaer("ddd", loader);
    Class cls= loader.loadClass("gemlite.core.annotations.domain.Field");
    System.out.println(loader.getParent().getResource("."));
    
  }
  
  
//  public final static Class<?> newSystemClass(String className)
//  {
//    Class<?> cls = null;
//    try
//    {
//      cls = instance.loadClass(className);
//    }
//    catch (ClassNotFoundException e)
//    {
//      LogUtil.getCoreLog().error("Create system class error,name=" + className, e);
//    }
//    
//    return cls;
//  }
  
  
  private ClassLoader getFirstJarLoader()
  {
    if(GemliteContext.getModuleContexts().hasNext())
      return GemliteContext.getModuleContexts().next().getLoader();
    return null;
  }
  
  @Override
  public URL getResource(String name)
  {
    Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
    while (it.hasNext())
    {
      IModuleContext mod = it.next();
      ClassLoader jarLoader = mod.getLoader();
      URL url = jarLoader.getResource(name);
      if (url != null)
        return url;
    }
    
    Iterator<IIndexContext> it2 = GemliteContext.getTopIndexContext().getIndexContexts();
    while (it2.hasNext())
    {
    	IIndexContext idc = it2.next();
    	ClassLoader loader = idc.getLoader();
    	URL url = loader.getResource(name);
    	if (url != null)
    		return url;
    }
    return getParent().getResource(name);
  }
  
  @Override
  public Enumeration<URL> getResources(String name) throws IOException
  {
    Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
    while (it.hasNext())
    {
      IModuleContext mod = it.next();
      ClassLoader jarLoader = mod.getLoader();
      Enumeration<URL> urls = jarLoader.getResources(name);
      if (urls != null)
        return urls;
    }
    
    Iterator<IIndexContext> it2 = GemliteContext.getTopIndexContext().getIndexContexts();
    while (it2.hasNext())
    {
    	IIndexContext idc = it2.next();
    	ClassLoader loader = idc.getLoader();
    	Enumeration<URL> urls = loader.getResources(name);
    	if (urls != null)
    		return urls;
    }
    
    return getParent().getResources(name);
  }
  
  @Override
  public InputStream getResourceAsStream(String name)
  {
    Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
    while (it.hasNext())
    {
      IModuleContext mod = it.next();
      ClassLoader jarLoader = mod.getLoader();
      InputStream in = jarLoader.getResourceAsStream(name);
      if (in != null)
        return in;
    }
    
    Iterator<IIndexContext> it2 = GemliteContext.getTopIndexContext().getIndexContexts();
    while (it2.hasNext())
    {
    	IIndexContext idc = it2.next();
    	ClassLoader loader = idc.getLoader();
    	InputStream in = loader.getResourceAsStream(name);
    	if (in != null)
    		return in;
    }
    return getParent().getResourceAsStream(name);
  }
  
  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException
  {
    ClassLoader loader = getFirstJarLoader();
    if(loader==null)
        return super.loadClass(name);
    return loader.loadClass(name);
  }
  
  public String getFullClassPath()
  {
    StringBuilder sb = new StringBuilder();
    
    Iterator<GemliteSibingsLoader> it = GemliteContext.getLoaders();
    while (it.hasNext())
    {
      GemliteSibingsLoader loader = it.next();
      if(loader.getURL()==null)
        continue;
      URL url = loader.getURL();
      String p = url.getFile();
      sb.append(p).append(File.pathSeparator);
    }
    
    String cp = System.getProperty("java.class.path");
    sb.append(cp);
    
//    URLClassLoader parentClassLoader = (URLClassLoader)getFirstJarLoader().getParent();
//    for (URL u : parentClassLoader.getURLs())
//    {
//      String pf = u.getFile();
//      sb.append(pf).append(File.pathSeparator);
//    }
    return sb.toString();
   
  }
}
