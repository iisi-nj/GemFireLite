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

import gemlite.core.internal.support.context.IGemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.hotdeploy.GemliteDeployer;
import gemlite.core.util.Util;

import java.net.MalformedURLException;
import java.net.URL;

public class JarFileDeploy
{
  public static void main(String[] args)
  {
    try
    {
      JarFileDeploy dl = new JarFileDeploy();
      boolean bl = false;
      URL url = new URL(
          "file:/D:/Code/vmgemlite/vmgemlite/Gemlite-bundle/Gemlite-runtime/target/Gemlite-runtime-0.0.1-SNAPSHOT.jar");
      URL url2 = Util.strToURL("file:/D:/Code/vmgemlite/vmgemlite/Gemlite-bundle/Gemlite-runtime/target/classes/");
      dl.f(url2);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
  }
  
  private void f(URL url)
  {
    IModuleContext m = GemliteDeployer.getInstance().deploy(url);
    for(String k:m.getRegistryCache().keySet())
    {
//      IGemliteRegistry v = m.getRegistry(k);
//      System.out.println(k+" "+v.getItems());
    }
  }
}
