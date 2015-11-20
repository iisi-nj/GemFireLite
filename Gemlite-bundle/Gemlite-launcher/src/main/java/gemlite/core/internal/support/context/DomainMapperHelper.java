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
package gemlite.core.internal.support.context;

import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class DomainMapperHelper
{
  public final static String DEFAULT_SUBFFIX = "$$$IMapperToolImpl";
  public final static String defaultMpperPackage = "gemlite.dynamic.asm.mapper.registry.";
  private static String mapperRegistryPackage = System.getProperty(defaultMpperPackage, defaultMpperPackage);
  
  public static void main(String[] args) throws Exception
  {
    ServerConfigHelper.initConfig();
    ServerConfigHelper.initLog4j("classpath:log4j2-server.xml");
    File f = new File("D:/Code/vmgemlite/vmsample/Sample-bundle/Sample-domain/target/Sample-domain-0.0.1-SNAPSHOT.jar");
    // File f2 = new File("D:/Code/vmgemlite/vmsample/Sample-bundle/Sample-domain/target/classes");
    URLClassLoader loader = new URLClassLoader(new URL[] { f.toURI().toURL() });
    scanMapperRegistryClass(loader);
  }
  
  public static void scanMapperRegistryClass()
  {
    scanMapperRegistryClass(Thread.currentThread().getContextClassLoader());
  }
  
  public static void scanMapperRegistryClass(ClassLoader loader)
  {
    try
    {
      URL pathURL = loader.getResource(mapperRegistryPackage.replaceAll("\\.", "\\/"));
      if (pathURL == null)
        return;
      PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(loader);
      String path = pathURL + "**/*.class";
      if (LogUtil.getCoreLog().isDebugEnabled())
        LogUtil.getCoreLog().debug("Domain registry scanning, path:" + path);
      Resource[] resArray = resolver.getResources(path);
      
      for (Resource res : resArray)
      {
        String fileName = res.getFilename();
        String className = fileName.substring(0, fileName.length() - ".class".length());
        className = className.replaceAll("///", "//.");
        className = defaultMpperPackage + className;
        
        LogUtil.getCoreLog().trace("Mapper registry class : ",className);
        IDomainRegistry reg = (IDomainRegistry) loader.loadClass(className).newInstance();
//        String s1= reg.getClass().getClassLoader().getResource(".").getPath();
//        String s2 = loader.getClass().getClassLoader().getResource(".").getPath();
//        System.err.println(s1+" >> "+s2);
        reg.doRegistry();
      }
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().error(e.getMessage(),e);
    }
  }
}
