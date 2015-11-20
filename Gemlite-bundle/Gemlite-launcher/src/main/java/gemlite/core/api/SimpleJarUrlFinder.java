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
package gemlite.core.api;

import gemlite.core.internal.support.hotdeploy.JarURLFinderFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class SimpleJarUrlFinder
{
  public final static String SIMPLE_LOGIC_MODULE = "logic";
  
  public final static void search(URL coreUrl, URL... startupUrls)
  {
    JarURLFinderFactory.useSimpleJarURLFinder(coreUrl, startupUrls);
  }
  
  public final static void developMode()
  {
    String classpath = System.getProperty("java.class.path");
    String userDir = System.getProperty("user.dir");
    String logicClassPath = userDir + "/target/classes";
    File f = new File(logicClassPath);
    try
    {
      URL logic = f.toURI().toURL();
      String[] ps = classpath.split("\\;");
      for (String s : ps)
      {
        if (s.indexOf("Gemlite-runtime") > 0)
        {
          File f2 = new File(s);
          URL core = f2.toURI().toURL();
          SimpleJarUrlFinder.search(core, logic);
          return;
        }
      }
    }
    catch (MalformedURLException e)
    {
    }
  }
  
}
