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

import java.net.URL;

public class CompositeJarURLFinder implements JarURLFinder
{
  ClasspathURLFinder cpfinder;
  JpaURLFinder jpaFinder;
  JasonURLFinder finder;
  URL[] startup;
  URL core;
  
  public CompositeJarURLFinder()
  {
    jpaFinder = new JpaURLFinder();
    finder = new JasonURLFinder();
    cpfinder = new ClasspathURLFinder();
  }
  
  @Override
  public void doFind()
  {
    cpfinder.doFind();
    core = cpfinder.getCoreJarUrl();
    try
    {
      finder.doFind();
      startup = finder.getURLsOnStartup();
      if (core == null)
        core = finder.getCoreJarUrl();
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().info("No jar found on ws");
      try
      {
        jpaFinder.doFind();
        startup = jpaFinder.getURLsOnStartup();
        if (core == null)
          core = jpaFinder.getCoreJarUrl();
      }
      catch (Exception e2)
      {
        LogUtil.getCoreLog().info("No jar found on jpa");
      }
    }
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
