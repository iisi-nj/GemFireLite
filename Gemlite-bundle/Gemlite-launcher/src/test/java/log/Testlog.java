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
package log;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

public class Testlog
{
  public static void main(String[] args) throws URISyntaxException
  {
    File f = new File("d:/tmp/x/log4j2_jnj.xml");
    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    context.setConfigLocation(f.toURI());
    
    Logger log = LogManager.getLogger("com.gemstone.xx");
    log.info("inof");
    log.debug("inof");
    log.info("inof");
    
  }
}
