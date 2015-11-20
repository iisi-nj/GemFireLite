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
package gemlite.core.internal.mq.receiver.file;

import gemlite.core.internal.mq.ShellParameter;

import org.kohsuke.args4j.Option;

public class FileShellParameter extends ShellParameter
{
  @Option(name = "-work", usage = "work path")
  public String workPath;
  
  @Option(name = "-base", usage = "base path")
  public String basePath;
  @Option(name = "-mode", usage = "default:tar ,tar/file")
  public String mode = "tar";
  
  @Option(name = "-fn", usage = "fname")
  public String fileName ;
  
  @Option(name = "-fe", usage = "file encoding,default GBK")
  public String fileEncoding ="GBK";
  
  
  
  public boolean isTarMode()
  {
    return "tar".equals(mode);
  }



  @Override
  public String toString()
  {
    return "workPath=" + workPath + "\nbasePath=" + basePath + "mode=" + mode + "\nfileName="
        + fileName + "\nfileEncoding=" + fileEncoding + "\n" + super.toString();
  }
}
