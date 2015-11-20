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
package gemlite.domain.tools;

import gemlite.domain.tools.parser.DdlParserFactory;
import gemlite.domain.tools.parser.IDdlParser;

import java.io.File;
import java.util.Map;

import org.kohsuke.args4j.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExporterTask
{
  @Option(name = "-fp", usage = "ddl file path")
  private String filePath = "D:/test";
  
  @Option(name = "-pn", usage = "package name")
  private String packageName = "gemlite.domain.tools.domain";
  
  @Option(name = "-db", usage = "type of database")
  private String database = "oracle";
  
  @Option(name = "-ds", usage = "db key source")
  private String dbKeySource = "index";
  
  @Option(name = "-ip", usage = "index file path")
  private String indexPath = "D:/idx";
  
  @Option(name = "-op", usage = "output path")
  private String outputPath = "D:/sts_workspace/vmgemlite/Gemlite-bundle/Gemlite-domain-tools/src/main/java/gemlite/domain/tools/domain";
  
  public void execute()
  {
    IDdlParser parser = DdlParserFactory.createDdlParser(database);
    if ("index".equalsIgnoreCase(dbKeySource))
      parser.parseIdxs(new File(indexPath));
    File file = new File(this.filePath);
    Map parsedDdls = parser.parseDdls(file);
    Exporter exporter = new Exporter(parsedDdls);
    exporter.generateJava(this.packageName, this.outputPath);
    System.out.println("done!");
  }
  
  public static void main(String[] args)
  {
    ExporterTask task = new ExporterTask();
    task.execute();
  }
}
