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
//
//import gemlite.core.internal.support.context.GemliteContext;
//import gemlite.core.internal.support.context.IIndexContext;
//import gemlite.core.internal.support.context.IModuleContext;
//import gemlite.core.util.LogUtil;
//
//import java.io.IOException;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//import java.util.WeakHashMap;
//
//public class GemliteJarLoader extends URLClassLoader
//{
//  
//  private String jarLoaderName;
//  private Map<String, byte[]> asmClassMap = new WeakHashMap<>();
//  
//  private String desc;
//  
//  private Set<String> ownerClassNames;
//  private URL jarurl;
//  private final static ThreadLocal<Boolean> alreadyScanned = new ThreadLocal<>();
//  
//  public GemliteJarLoader(URL... urls)
//  {
//    this("", urls);
//  }
//  
//  public GemliteJarLoader( String name, URL... urls)
//  {
//    super(urls, null);
//    alreadyScanned.set(false);
//    this.jarurl = urls[0];
//    this.jarLoaderName = name;
//    this.desc = jarLoaderName;
//    for (URL url : urls)
//    {
//      desc += " " + url.toString();
//    }
//    ownerClassNames = new HashSet<>();
//  }
//  
//  public void recordAsOwner(String className)
//  {
//    ownerClassNames.add(className);
//  }
//  
//  public void addAsmClass(String name, byte[] bytes)
//  {
//    asmClassMap.put(name, bytes);
//  }
//  
//  @Override
//  protected Class<?> findClass(String className) throws ClassNotFoundException
//  {
//    if (asmClassMap.containsKey(className))
//    {
//      byte[] bytes = asmClassMap.get(className);
//      asmClassMap.remove(className);
//      if (LogUtil.getCoreLog().isDebugEnabled())
//        LogUtil.getCoreLog().debug("Transformed :" + className + " by " + this);
//      return defineClass(className, bytes, 0, bytes.length);
//    }
//    else
//    {
//      try
//      {
//        Class<?> cls = super.findClass(className);
//        return cls;
//      }
//      catch (ClassNotFoundException e)
//      {
//        throw e;
//      }
//    }
//  }
//  
//  @Override
//  public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException
//  {
//    Class<?> clazz= loadClass(className, resolve, true);
//    return clazz;
//  }
//  
//  public boolean isScanned()
//  {
//	  Boolean b = alreadyScanned.get();
//	  return b==null?false:b;
//  }
//  private Class<?> loadSibings(String className, boolean resolve)throws ClassNotFoundException
//  {
//    Class<?> clazz = null;
//    Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
//    while (it.hasNext())
//    {
//      IModuleContext mod = it.next();
//      GemliteJarLoader jarLoader = null;// mod.getJarLoader();
//      if (!jarLoader.isScanned())
//      {
//        try
//        {
//          clazz = jarLoader.loadClass(className, resolve, false);
//        }
//        catch (ClassNotFoundException |NoClassDefFoundError e)
//        {
//        }
//        if (clazz != null)
//        {
//          if(LogUtil.getCoreLog().isDebugEnabled())
//            LogUtil.getCoreLog().debug(className+" loadSibings,"+clazz.getClassLoader());
//          return clazz;
//        }
//      }
//    }
//    return null;
//  }
//  
//  private Class<?> loadIndexClasses(String className, boolean resolve) throws ClassNotFoundException
//  {
//    Class<?> clazz = null;
//    Iterator<IIndexContext> it = GemliteContext.getTopIndexContext().getIndexContexts();
//    while (it.hasNext())
//    {
//    	IIndexContext idc = it.next();
//    	GemliteIndexLoader loader = idc.getIndexLoader();
//    	if (!loader.isScanned())
//    	{
//    		try
//    		{	
//    			clazz = loader.loadClass(className, resolve, false);
//    		}
//    		catch (ClassNotFoundException |NoClassDefFoundError e)
//    		{
//    		}
//    		if (clazz != null)
//    		{
//    			if(LogUtil.getCoreLog().isDebugEnabled())
//    				LogUtil.getCoreLog().debug(className+" loadIndexClasses,"+clazz.getClassLoader());
//    			return clazz;
//    		}
//    	}
//    }
//    return null;
//  }
//  
//  public Class<?> loadClass(String className, boolean resolve, boolean findSibings) throws ClassNotFoundException
//  {
//    Class<?> clazz = null;
//    // classpath
//    try
//    {
//      clazz =Thread.currentThread().getContextClassLoader().loadClass(className);
//    }
//    catch (ClassNotFoundException |NoClassDefFoundError e)
//    {
//    }
//    if(clazz !=null)
//    {
//      if(LogUtil.getCoreLog().isDebugEnabled())
//        LogUtil.getCoreLog().debug(className+" loaded from parent,"+clazz.getClassLoader());
//      return clazz;
//    }
//    
//    //Current urlclassloader
//    clazz = findLoadedClass(className);
//    if (clazz != null)
//    {
//      if(LogUtil.getCoreLog().isDebugEnabled())
//        LogUtil.getCoreLog().debug(className+" findLoadedClass,"+clazz.getClassLoader());
//      return clazz;
//    }
//     
//    try
//    {
//      clazz = findClass(className);
//      if (resolve)
//        resolveClass(clazz);
//    }
//    catch (ClassNotFoundException |NoClassDefFoundError e)
//    {
//      if (!findSibings)
//        throw e;
//    }
//    if(clazz!=null)
//    {
//      if(LogUtil.getCoreLog().isDebugEnabled())
//        LogUtil.getCoreLog().debug(className+" findClass,"+clazz.getClassLoader());
//      return clazz;
//    }
//      
//    //siblings
//    alreadyScanned.set(true);
//    clazz= loadSibings(className, resolve);
//    alreadyScanned.set(false);
//    
//    if(clazz!=null)
//        return clazz;
//    
//    //indexClasses
//    return loadIndexClasses(className, resolve);
//  }
//  
//  public Class<?> loadOwnerClass(String name, boolean resolve) throws ClassNotFoundException
//  {
//    
//    return super.loadClass(name, resolve);
//  }
//  
////  @Override
////  public URL getResource(String name)
////  {
////    Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
////    while (it.hasNext())
////    {
////      IModuleContext mod = it.next();
////      GemliteJarLoader jarLoader = mod.getJarLoader();
////      URL url = jarLoader.getOwnerResource(name);
////      if (url != null)
////        return url;
////    }
////    return super.getResource(name);
////  }
////  
////  @Override
////  public Enumeration<URL> getResources(String name) throws IOException
////  {
////    Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
////    while (it.hasNext())
////    {
////      IModuleContext mod = it.next();
////      GemliteJarLoader jarLoader = mod.getJarLoader();
////      Enumeration<URL> urls = jarLoader.getOwnerResources(name);
////      if (urls != null)
////        return urls;
////    }
////    return super.getResources(name);
////  }
//  
////  public Enumeration<URL> getOwnerResources(String name) throws IOException
////  {
////    return findResources(name);
////  }
////  
////  public URL getOwnerResource(String name)
////  {
////    URL url = findResource(name);
////    return url;
////  }
//  
//  @Override
//  public boolean equals(Object obj)
//  {
//    if (this == obj)
//      return true;
//    if (obj == null)
//      return false;
//    if (getClass() != obj.getClass())
//      return false;
//    GemliteJarLoader other = (GemliteJarLoader) obj;
//    if (jarLoaderName == null)
//    {
//      if (other.jarLoaderName != null)
//        return false;
//    }
//    else if (!jarLoaderName.equals(other.jarLoaderName))
//      return false;
//    return true;
//  }
//  
//  public void clean()
//  {
//    try
//    {
//      close();
//    }
//    catch (IOException e)
//    {
//    }
//  }
//  
//  @Override
//  public String toString()
//  {
//    return desc;
//  }
//  
//  public URL getJarurl()
//  {
//    return jarurl;
//  }
//  
//}
