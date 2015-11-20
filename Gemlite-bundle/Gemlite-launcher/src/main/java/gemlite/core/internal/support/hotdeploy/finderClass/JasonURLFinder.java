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

import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.annotations.ModuleType;
import gemlite.core.internal.support.hotdeploy.DeployParameter;
import gemlite.core.internal.support.hotdeploy.JarURLFinder;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class JasonURLFinder implements JarURLFinder
{
  private static final TypeReference<List<DeployParameter>> dpListRef = new TypeReference<List<DeployParameter>>()
  {
  };
  
  private URL runtime;
  private URL[] others;
  
  /***
   * 1.Ö—À;„jar0@
   */
  public void doFind()
  {
    String config_server = ServerConfigHelper.getProperty("CONFIG_SERVER");
    ObjectMapper mapper = new ObjectMapper();
    URL url=Util.makeFullURL(config_server, "/jars/api/active");
    try
    {
      List<DeployParameter> params = mapper.readValue(url, dpListRef);
      List<URL> list = new ArrayList<>();
      for (DeployParameter param : params)
      {
        if (param.getModuleType()==ModuleType.RUNTIME)
          runtime = param.getUrl();
        else
          list.add(param.getUrl());
        
        LogUtil.getCoreLog().info("Module "+param.getModuleType()+" url:"+param.getUrl());
      }
      others = new URL[list.size()];
      others = list.toArray(others);
    }
    catch (IOException e)
    {
      throw new GemliteException("Failure on config-server",e);
    }
  }
  
  public URL[] getURLsOnStartup()
  {
    return others;
  }
  
  public URL getCoreJarUrl()
  {
    return runtime;
  }
  
}
