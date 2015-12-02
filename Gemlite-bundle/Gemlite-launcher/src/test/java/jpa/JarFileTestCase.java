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
package jpa;

import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.internal.support.jpa.files.service.JarFileService;
import gemlite.core.internal.support.system.ServerConfigHelper;

import java.io.File;
import java.net.MalformedURLException;

public class JarFileTestCase
{
  public static void init()
  {
    
  }
  public static void main(String[] args) throws MalformedURLException
  {
    ServerConfigHelper.initConfig();
    ServerConfigHelper.initLog4j("log4j-test.xml");
    File f = new File("D:/Code/vmgemlite/vmgemlite/Gemlite-bundle/Gemlite-runtime/target/Gemlite-runtime-0.0.1-SNAPSHOT.jar");
    JpaContext.init();
    JarFileService srv = JpaContext.getService(JarFileService.class);
    ReleasedJarFile file= srv.deploy(f.toURI().toURL());
    ReleasedJarFile f2 = srv.getFileById(file.getFileId());
    System.out.println(f2);
    
  }
}
