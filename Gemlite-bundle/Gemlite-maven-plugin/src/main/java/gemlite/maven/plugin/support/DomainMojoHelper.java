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
package gemlite.maven.plugin.support;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.objectweb.asm.tree.FieldNode;

public class DomainMojoHelper
{
  private static Log log = new DefaultLog(new ConsoleLogger(Logger.LEVEL_DEBUG, "testing"));
  
  public static boolean FixNullValue = false;
  
  public final static void setLog(Log log)
  {
    DomainMojoHelper.log = log;
  }
  
  public final static Log log()
  {
    return log;
  }
  
  private static ClassLoader projLoader;
  
  public final static void setLoader(ClassLoader loader)
  {
    projLoader = loader;
  }
  
  public final static ClassLoader getLoader()
  {
    return projLoader;
  }
  
  
  public final static boolean isValidField(FieldNode fn)
  {
    if(fn.name.equals("serialVersionUID"))
      return false;
    return true;
  }
 
}
