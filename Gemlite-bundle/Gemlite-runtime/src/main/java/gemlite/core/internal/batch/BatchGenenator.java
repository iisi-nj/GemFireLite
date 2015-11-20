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
package gemlite.core.internal.batch;

import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;

/***
 * 1.ûÖMnáo
 * 2.ûÖ!H
 * 3.9naökE!H
 * 
 * @author ynd
 * 
 */
public class BatchGenenator
{
  private StringTemplateGroup fileGenerator;
  private String batchFilePath;
  
  public void initializeEngine()
  {
    initializeWorkPath();
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("batch/batch-template.stg");
    fileGenerator = new StringTemplateGroup(new InputStreamReader(in));
    try
    {
      in.close();
    }
    catch (IOException e)
    {
      LogUtil.getCoreLog().info("-> StringTemplate initailize error");
      if (LogUtil.getCoreLog().isErrorEnabled())
        LogUtil.getCoreLog().error("fileGenerator", e);
      throw new GemliteException(e);
    }
  }
  
  /***
   * return XML file path
   * 
   * @param name
   * @param file
   * @param delimiter
   * @param quote
   * @param skipable
   * @param columns
   * @param region
   * @param table
   * @param encoding
   * @param linesToSkip
   * @return
   */
  public String generateFileJob(String name, BatchParameter attributes)
  {
    // lineDelimiter, lineQuote, skipPolicy, jobName, lineNames, files, regionName, tableName, fileEncoding,
    // linesToSkipSize
    StringTemplate st = fileGenerator.getInstanceOf(attributes.getTemplate()+"Template");
    st.setAttribute("param", attributes);
    String content = st.toString();
    return writeOneFile("batch-" + attributes.getTemplate() + "-" + name + ".xml", content);
  }
  
  /***
   * GS_HOME/batchFile/
   */
  private void initializeWorkPath()
  {
    if (LogUtil.getCoreLog().isDebugEnabled())
      LogUtil.getCoreLog().debug("path initializing...");
    String path = ServerConfigHelper.getConfig(ITEMS.GS_HOME);
    if (StringUtils.isEmpty(path))
    {
      URL url = this.getClass().getResource("/");
      path = url.getPath();
    }
    if (!path.endsWith(File.separator))
      path += File.separator;
    path += "batchFile" + File.separator;
    
    if (LogUtil.getCoreLog().isDebugEnabled())
      LogUtil.getCoreLog().debug("path:" + path);
    File file = new File(path);
    if (!file.exists())
    {
      file.mkdirs();
    }
    batchFilePath = path;
    
    if (LogUtil.getCoreLog().isDebugEnabled())
      LogUtil.getCoreLog().debug("path initialized");
  }
  
  /***
   * create xml file at GS_HOME/batchFile
   * 
   * @param fileName
   * @param content
   * @return
   */
  private String writeOneFile(String fileName, String content)
  {
    try
    {
      File f = new File(batchFilePath, fileName);
      BufferedWriter bw = new BufferedWriter(new FileWriterWithEncoding(f, "utf-8"));
      bw.write(content);
      bw.close();
      LogUtil.getCoreLog().info("Batch file created at " +f.getAbsolutePath());
      return f.toURI().toString();
    }
    catch (Exception e)
    {
      throw new GemliteException(e);
    }
  }
}
