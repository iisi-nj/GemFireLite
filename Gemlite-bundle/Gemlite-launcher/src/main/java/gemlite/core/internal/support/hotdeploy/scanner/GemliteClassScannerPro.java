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
package gemlite.core.internal.support.hotdeploy.scanner;

import gemlite.core.annotations.DeployConfigure;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.IIndexContextCreator;
import gemlite.core.internal.support.IModuleContextCreator;
import gemlite.core.internal.support.annotations.GemliteAnnotation;
import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.context.IGemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.internal.support.hotdeploy.GemliteSibingsLoader;
import gemlite.core.internal.support.hotdeploy.JarURLFinder;
import gemlite.core.internal.support.hotdeploy.JarURLFinderFactory;
import gemlite.core.util.GemliteHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/***
 * 
 * @author ynd
 * 
 */

/***
 * 需要保留一个入口，runtime加载时要替换，静态记录，用于core module的引入
 * 通过scan能够区分出core还是logic
 * 有的需要registry处理
 * 有的不需要？
 * 
 * core module 才会有registry
 * autoserialize
 * function/applicationlistener
 * 
 * autoserialize是附加，需要优先处理
 * 
 */
public class GemliteClassScannerPro
{
  private String REGISTRY_PKG = "gemlite.core.internal.context.registryClass";
  protected static RegistryDefineContext registryDefineContext = null;
  private static IModuleContextCreator moduleCreator = null;
  private static IIndexContextCreator indexContextCreator = null;
  
  public GemliteClassScannerPro()
  {
  }
  
  protected IIndexContextCreator getIdxContextCreator()
  {
    return indexContextCreator;
  }
  
  private final static String class_suffix = ".class";
  
  /***
   * 1.加载Runtime时扫描并记录Registry定义；其他情况使用已存在的定义
   * 2.记录形式： key=Annotation desc 或 interface desc
   * 例如：AdminService.class.getName() Function.class.getName()
   * 
   * 3.Registry 定义记录在哪？
   * scanner类及相关结构类在将来后续会被移至launcher模块，这样扫描时，就无需创建scanner类实例，这样会用到未确认的loader
   * 
   * @param loader
   * @return
   */
  private RegistryDefineContext findOrCreateRegistryDefinition(ClassLoader loader)
  {
    if (registryDefineContext != null)
      return registryDefineContext;
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(loader);
    String path = REGISTRY_PKG.replace('.', '/') + "/**/*.class";
    Resource[] urls = null;
    try
    {
      urls = resolver.getResources(path);
    }
    catch (IOException e)
    {
      JarURLFinder finder= JarURLFinderFactory.getFinderImpl();
      finder.doFind();
      URL coreURL= finder.getCoreJarUrl();
      GemliteSibingsLoader cloader= new GemliteSibingsLoader(coreURL);
      resolver = new PathMatchingResourcePatternResolver(cloader);
      path = REGISTRY_PKG.replace('.', '/') + "/**/*.class";
      try
      {
        urls = resolver.getResources(path);
      }
      catch (IOException e1)
      {
        LogUtil.getCoreLog().error("getResources for path :{}", path, e);
      }
    }
    if (urls == null || urls.length == 0)
    {
      // not core module
      return registryDefineContext;
    }
    
    registryDefineContext = new RegistryDefineContext();
    ScannedRegistryDefine def = new ScannedRegistryDefine(true);
    String key = Util.getInternalDesc(DeployConfigure.class);
    def.matchKey = key;
    registryDefineContext.addItem(key, def);
    
    // IModuleContext
    def = new ScannedRegistryDefine(true);
    key = Util.getInternalDesc(IModuleContextCreator.class);
    def.matchKey = key;
    registryDefineContext.addItem(key, def);
    
    def = new ScannedRegistryDefine(true);
    key = Util.getInternalDesc(IIndexContextCreator.class);
    def.matchKey = key;
    registryDefineContext.addItem(key, def);
    
    for (Resource res : urls)
    {
      ClassNode cn = GemliteHelper.toClassNode(res);
      if (cn == null)
        throw new GemliteException(res + " toClassNode failure.");
      GemliteAnnotation g = GemliteHelper.readAnnotations(cn);
      String desc = Util.getInternalDesc(GemliteRegistry.class);
      Map<String, Object> m = g.getAnnotation(desc);
      if (m != null)
      {
        Type t = (Type) m.get("value");
        String matchKey = t.getDescriptor();// annotation or interface desc Lgemlite.core...AdminService;
        Integer priority = (Integer) m.get("priority");
        ScannedRegistryDefine scannedDef = new ScannedRegistryDefine();
        scannedDef.matchKey = matchKey;
        scannedDef.registryClassName = cn.name.replace('/', '.'); // Lgemlite....AdminServiceRegistry;
        scannedDef.priority = priority != null ? priority : GemliteRegistry.DEFAULT_PRIORITY;
        registryDefineContext.addItem(matchKey, scannedDef);
        LogUtil.getCoreLog().trace("Registry define,key:{}  value:{}", matchKey, scannedDef.registryClassName);
      }
    }
    return registryDefineContext;
  }
  
  /***
   * 根据interface 或者 annotation 分类记录扫描的类
   * 
   * @param regDefMap
   * @param className
   * @param cn
   * @throws IOException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   */
  private void matchRegistryDefine(RegistryDefineContext defContext, RegistryMatchedContext mchContext,
      String className, ClassNode cn) throws IOException, InstantiationException, IllegalAccessException,
      ClassNotFoundException
  {
    // type:[classname]
    // type: registryClass,List
    LogUtil.getCoreLog().trace("Match registry define use class {}", className);
    if (cn.interfaces != null && cn.interfaces.size() > 0)
    {
      for (int i = 0; i < cn.interfaces.size(); i++)
      {
        String name = (String) cn.interfaces.get(i);
        ScannedRegistryDefine def = defContext.matchDefine("L" + name + ";");
        LogUtil.getCoreLog().trace("Match registry use interface {},result {}", name, def);
        if (def != null)
        {
          ScannedItem item = new ScannedItem(className, cn);
          mchContext.addItem(def, item);
        }
      }
    }
    if (cn.visibleAnnotations != null)
    {
      for (int i = 0; i < cn.visibleAnnotations.size(); i++)
      {
        AnnotationNode ann = (AnnotationNode) cn.visibleAnnotations.get(i);
        ScannedRegistryDefine def = defContext.matchDefine(ann.desc);
        LogUtil.getCoreLog().trace("Match registry use annotation {},result {}", ann.desc, def);
        if (def != null)
        {
          ScannedItem item = new ScannedItem(className, cn);
          GemliteHelper.readAnnotationValues(item, ann);
          mchContext.addItem(def, item);
        }
      }
    }
  }
  
  /***
   * Scan 过程不会加载任何属于loader的类
   * 
   * @param loader
   * @return
   */
  public RegistryMatchedContext scan(GemliteSibingsLoader loader, ScannerIterator scannerIterator)
  {
    RegistryDefineContext defContext = findOrCreateRegistryDefinition(loader);
    if (LogUtil.getCoreLog().isTraceEnabled())
    {
      Collection<ScannedRegistryDefine> coll = defContext.getScannedRegistryDefines();
      for (ScannedRegistryDefine def : coll)
      {
        LogUtil.getCoreLog().trace("Define key:{},registryClass:{},priority:{},flag:{}", def.matchKey,
            def.registryClassName, def.noninstance, def.priority);
      }
    }
    RegistryMatchedContext mchContext = scan(loader, defContext, scannerIterator);
    // by modname checkpoints
    
    return mchContext;
  }
  
  public IModuleContext createModuleContext(GemliteSibingsLoader loader, RegistryMatchedContext mcContext)
  {
    try
    {
      String moduleName = mcContext.getModuleName();
      ModuleType moduleType = mcContext.getModuleType();
      if (moduleType == ModuleType.RUNTIME)
      {
        moduleCreator = getModuleContextCreator(loader, registryDefineContext, mcContext);
        indexContextCreator = getIndexContextCreator(loader, registryDefineContext, mcContext);
      }
      IModuleContext module = moduleCreator.createModule(moduleName, moduleType, loader);
      return module;
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
    {
      throw new GemliteException("No @DeployConfigure define found.URL is " + loader.getURL());
    }
    
  }
  
  /***
   * 
   * @param loader
   * @param regDefMap
   * @return
   */
  protected RegistryMatchedContext scan(GemliteSibingsLoader loader, RegistryDefineContext defContext,
      ScannerIterator scannerIterator)
  {
    // URL url = loader.getURL();
    
    LogUtil lu = LogUtil.newInstance();
    // if (LogUtil.getCoreLog().isDebugEnabled())
    // LogUtil.getCoreLog().debug("Start scan classes in jar or path," + url);
    // ScannerIterator scannerIterator = null;
    RegistryMatchedContext mchContext = new RegistryMatchedContext();
    mchContext.setLoader(loader);
    try
    {
      // scannerIterator = new ScannerIterator(url);
      while (scannerIterator.hasNext())
      {
        ScannerIteratorItem item = scannerIterator.next();
        if (item == null)
          break;
        String name = item.getName();
        // 处理类文件
        // 1.检查接口，是否function
        // 2.检查类的annotation,是否需要实现序列化
        // 3.检查方法的annotation,是否需要处理统计
        byte[] content = null;
        if (name.endsWith(class_suffix))
        {
          content = item.getBytes(loader);
          
          ClassNode cn = GemliteHelper.toClassNode(content);
          String className = cn.name.replace('/', '.');
          className = className.replace('\\', '.');
          mchContext.getCachedClassNode().put(className, cn);
          // 跳过Registry,已扫描
          if (className.startsWith(REGISTRY_PKG))
            continue;
          
          matchRegistryDefine(defContext, mchContext, className, cn);
        }
        else
        {
          Object o = item.getItem();
          if (o instanceof byte[])
          {
            content = (byte[]) o;
            ClassNode cn = GemliteHelper.toClassNode(content);
            
            String className = cn.name.replace('/', '.');
            className = className.replace('\\', '.');
            // 跳过Registry,已扫描
            if (className.startsWith(REGISTRY_PKG))
              continue;
            
            matchRegistryDefine(defContext, mchContext, className, cn);
          }
        }
      }
      
      LogUtil.getCoreLog().debug("END scan classes in jar,cost=" + lu.cost() + " --------");
      
    }
    catch (Exception e)
    {
      throw new GemliteException("Scan error , loader url is {}" + loader.getURL(), e);
    }
    // finally
    // {
    // try
    // {
    // if (scannerIterator != null)
    // {
    // scannerIterator.close();
    // }
    // }
    // catch (IOException e)
    // {
    // LogUtil.getCoreLog().warn("Close jar inputstream error.");
    // }
    // }
    
    processDefineConfigure(loader, registryDefineContext, mchContext);
    return mchContext;
  }
  
  public void register(IModuleContext module, RegistryMatchedContext matched)
  {
    try
    {
      Map<String, IGemliteRegistry> registryCache = new ConcurrentHashMap<>();
      
      // others
      Map<ScannedRegistryDefine, List<RegistryParam>> result = matched.getScannedItems();
      
      for (ScannedRegistryDefine def : result.keySet())
      {
        List<RegistryParam> params = matched.getItems(def);
        if (def.noninstance)
          continue;
        // 如无匹配项，跳过
        if (params == null || params.size() == 0)
          continue;
        
        IGemliteRegistry reg = registryCache.get(def.matchKey);
        if (reg == null)
        {
          reg = (IGemliteRegistry) module.getLoader().loadClass(def.registryClassName).newInstance();
          registryCache.put(def.matchKey, reg);
        }
        reg.addParams(params);
        reg.register(module);
      }
      
      // if (scannedItem.getBytesTransformed() != null)
      // loader.addDynamicClass(scannedItem.getClassName(), scannedItem.getBytesTransformed());
      
      if (module != null)
        module.updateRegistryCache(registryCache);
    }
    catch (Exception e)
    {
      throw new GemliteException(e);
    }
  }
  
  public ScannedRegistryDefine getRegistryDefine(Class<?> cls)
  {
    if (registryDefineContext != null)
      return registryDefineContext.matchDefine(cls);
    else
      return null;
  }
  
  protected void processDefineConfigure(GemliteSibingsLoader loader, RegistryDefineContext defContext,
      RegistryMatchedContext mcContext)
  {
    ScannedRegistryDefine dcDefine = defContext.matchDefine(DeployConfigure.class);
    RegistryParam item = mcContext.getSingleItem(dcDefine);
    if (item == null)
    {
      LogUtil.getCoreLog().error("No @DeployConfigure defined,url:" + loader.getURL());
      throw new GemliteException("No @DeployConfigure defined,url:" + loader.getURL());
    }
    String v1 = (String) item.getAnnValue("value");
    String v2 = (String) item.getAnnValue("type");
    mcContext.setModuleInfo(v1, v2);
  }
  
  private IModuleContextCreator getModuleContextCreator(GemliteSibingsLoader loader, RegistryDefineContext defContext,
      RegistryMatchedContext mcContext) throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    ScannedRegistryDefine def = defContext.matchDefine(IModuleContextCreator.class);
    RegistryParam item = mcContext.getSingleItem(def);
    if (item == null)
    {
      LogUtil.getCoreLog().error("No IModuleContextCreator defined.");
      return null;
    }
    // regDefMap.remove(ANN_DEPLOY_CONFIGURE_DESC);
    // start registry
    // DeployConfigure define module name and type
    Class<?> cls0 = loader.loadClass(item.getClassName());
    IModuleContextCreator d = (IModuleContextCreator) cls0.newInstance();
    return d;
  }
  
  private IIndexContextCreator getIndexContextCreator(GemliteSibingsLoader loader, RegistryDefineContext defContext,
      RegistryMatchedContext mcContext) throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    ScannedRegistryDefine def = defContext.matchDefine(IIndexContextCreator.class);
    RegistryParam item = mcContext.getSingleItem(def);
    if (item == null)
    {
      LogUtil.getCoreLog().error("No IIndexContextCreator defined.");
      return null;
    }
    Class<?> cls0 = loader.loadClass(item.getClassName());
    IIndexContextCreator d = (IIndexContextCreator) cls0.newInstance();
    return d;
  }
}
