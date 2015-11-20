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
package gemlite.shell.codegen.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import gemlite.shell.codegen.util.GenerationTool;
import gemlite.shell.codegen.util.jaxb.Configuration;
import gemlite.shell.codegen.util.jaxb.Database;
import gemlite.shell.codegen.util.jaxb.Generator;
import gemlite.shell.codegen.util.jaxb.Jdbc;
import gemlite.shell.codegen.util.jaxb.Target;

import org.springframework.stereotype.Component;

@Component
public class CodegenService
{
  public void execute()
  {
    try
    {
      String conf = "gemlite-codegen.xml";
      InputStream in = GenerationTool.class.getResourceAsStream(conf);
      if (in == null && !conf.startsWith("/"))
        in = GenerationTool.class.getResourceAsStream("/" + conf);
      if (in == null && new File(conf).exists())
        in = new FileInputStream(new File(conf));
      Configuration configuration = GenerationTool.load(in);
      GenerationTool.generate(configuration);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void webExecute(String driver, String url, String user, String pwd, String name, String inputSchema,
      String includes, String excludes, String packageName, String directory) throws Exception
  {
    
    Jdbc jdbc = new Jdbc();
    jdbc.setDriver(driver);
    jdbc.setUrl(url);
    jdbc.setUser(user);
    jdbc.setPassword(pwd);
    Database database = new Database();
    database.setName(name);
    database.setInputSchema(inputSchema);
    database.setIncludes(includes);
    database.setExcludes(excludes);
    Target target = new Target();
    target.setPackageName(packageName);
    target.setDirectory(directory);
    Generator generator = new Generator();
    generator.setDatabase(database);
    generator.setTarget(target);
    Configuration configuration = new Configuration();
    configuration.setJdbc(jdbc);
    configuration.setGenerator(generator);
    GenerationTool.generate(configuration);
  }
}
