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
//import gemlite.core.internal.support.context.GemliteIndexContext;
//import gemlite.core.internal.support.context.IIndexContext;
//import gemlite.core.internal.support.context.IModuleContext;
//import gemlite.core.util.LogUtil;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.WeakHashMap;
//
//public class GemliteIndexLoader extends ClassLoader
//{
//	private Map<String, byte[]> classBytesMap = new WeakHashMap<String, byte[]>();
//	
//	private final static ThreadLocal<Boolean> alreadyScanned = new ThreadLocal<>();
//  
//	public GemliteIndexLoader()
//	{
//		super(null);
//		alreadyScanned.set(false);
//	}
//  
//	public void clean()
//	{
//		classBytesMap.clear();
//	    classBytesMap=null;
//	}
//	  
//	public void addIndexClasses(Map<String, byte[]> classes)
//	{
//		classBytesMap.putAll(classes);
//	}
//	
//	public Map<String, byte[]> getAllIndexClasses()
//	{
//		return classBytesMap;
//	}
//	
//	public void addIndexClass(String name, byte[] bytes)
//	{
//		classBytesMap.put(name, bytes);
//	}
//	  
//  @Override
//  protected Class<?> findClass(String className) throws ClassNotFoundException
//  {
//    if (classBytesMap.containsKey(className))
//    {
//      byte[] bytes = classBytesMap.get(className);
//      //classBytesMap.remove(className);
//      if (LogUtil.getCoreLog().isDebugEnabled())
//        LogUtil.getCoreLog().debug("Dynamic Index Class: " + className + " by " + this);
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
//  
//  private Class<?> loadSibings(String className, boolean resolve)throws ClassNotFoundException
//  {
//    Class<?> clazz = null;
//    GemliteIndexContext context = GemliteContext.getTopIndexContext();
//    Iterator<IIndexContext> it = context.getIndexContexts();
//    while (it.hasNext())
//    {
//    	IIndexContext idxC = it.next();
//    	GemliteIndexLoader loader = idxC.getIndexLoader();
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
//    				LogUtil.getCoreLog().debug(className+" loadSibings,"+clazz.getClassLoader());
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
//      clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
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
//    //load modules
//    clazz = loadModuleClasses(className, resolve);
//    if(clazz!=null)
//    	return clazz;
//    
//    //siblings
//    alreadyScanned.set(true);
//    clazz= loadSibings(className, resolve);
//    alreadyScanned.set(false);
//    
//    return clazz;
//  }
//  
//  private Class<?> loadModuleClasses(String className, boolean resolve)throws ClassNotFoundException
//  {
//    Class<?> clazz = null;
//    Iterator<IModuleContext> it = GemliteContext.getModuleContexts();
//    while (it.hasNext())
//    {
//      IModuleContext mod = it.next();
//      GemliteSibingsLoader jarLoader = mod.getLoader();
//      if (!jarLoader.isScanned())
//      {
////        try
////        {
////          clazz = jarLoader.loadClass(className, resolve, false);
////        }
////        catch (ClassNotFoundException |NoClassDefFoundError e)
////        {
////        }
//        if (clazz != null)
//        {
//          if(LogUtil.getCoreLog().isDebugEnabled())
//            LogUtil.getCoreLog().debug(className+" loadModules,"+clazz.getClassLoader());
//          return clazz;
//        }
//      }
//    }
//    return null;
//  }
//  
//  public Class<?> loadOwnerClass(String name, boolean resolve) throws ClassNotFoundException
//  {
//    
//    return super.loadClass(name, resolve);
//  }
//}
