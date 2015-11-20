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
package gemlite.maven.plugin.support.serialize;

import gemlite.core.internal.asm.serialize.DataSerializeHelper;
import gemlite.core.util.GemliteHelper;
import gemlite.maven.plugin.support.ClassProcessor;
import gemlite.maven.plugin.support.DomainMojoHelper;
import gemlite.maven.plugin.support.ProcessResult;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class DataSerializeProcessor implements ClassProcessor, Opcodes
{
  
  @Override
  public ProcessResult process(File originFile, byte[] bytes, ClassNode cn) throws IOException
  {
    ProcessResult result = new ProcessResult();
    DataSerializeHelper.getInstance().process(cn);
    byte[] newBytes= GemliteHelper.classNodeToBytes(cn);
    if(newBytes==null)
    {
      result.success=false;
      return result;
    }
    if (DomainMojoHelper.log().isDebugEnabled())
      DomainMojoHelper.log().debug(cn.name + " prepare to overwrite class file.");
    writeClassFile(originFile, newBytes);
    if (DomainMojoHelper.log().isDebugEnabled())
      DomainMojoHelper.log().debug(cn.name + " write file done.");
    return result;
  }
  
  private void writeClassFile(File originFile, byte[] processedBytes) throws IOException
  {
    originFile.delete();
    BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(originFile));
    bo.write(processedBytes);
    bo.close();
  }
  
  
  
  public void endProcess()
  {
    
  }
  
}
