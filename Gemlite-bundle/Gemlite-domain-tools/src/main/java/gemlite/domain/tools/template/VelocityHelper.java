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
package gemlite.domain.tools.template;

import java.io.FileWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityHelper
{
  private static final VelocityEngine engine = new VelocityEngine();
  static
  {
    engine.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
    engine.setProperty("resource.loader", "class");
    engine.setProperty("class.resource.loader.class",
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
  }
  
  public static void mergeTemplate(Map<String, Object> paddingMap, String outputPath)
  {
    try
    {
      Template template = engine.getTemplate("gemlite/domain/tools/template/domain.vm");
      VelocityContext context = new VelocityContext(paddingMap);
      FileWriter writer = new FileWriter(outputPath);
      template.merge(context, writer);
      writer.close();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  
}
