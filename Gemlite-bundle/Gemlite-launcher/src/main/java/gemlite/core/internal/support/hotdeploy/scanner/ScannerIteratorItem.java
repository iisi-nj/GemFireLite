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
package gemlite.core.internal.support.hotdeploy.scanner;

import gemlite.core.internal.support.hotdeploy.GemliteClassLoader;
import gemlite.core.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;

import org.apache.commons.io.FileUtils;

public class ScannerIteratorItem
{
  public ScannerIteratorItem(String name, Object item,boolean jarFile)
  {
    this.name = name;
    this.item = item;
    this.jarFile = jarFile;
  }
  
  private Object item;
  private String name;
  private boolean jarFile;
  
  public String getName()
  {
    return name;
  }
  
  public Object getItem()
  {
    return item;
  }
  
  public byte[] getBytes() throws IOException
  {
    return getBytes(GemliteClassLoader.getInstance());
  }
  
  public byte[] getBytes(ClassLoader loader) throws IOException
  {
    byte[] bytes = null;
    if (jarFile)
      bytes = readContent(loader, (JarEntry) item);
    else
      bytes = FileUtils.readFileToByteArray((File) item);
    return bytes;
  }
  
  private byte[] readContent(ClassLoader loader, JarEntry entry) throws IOException
  {
    URL url =  loader.getResource(entry.getName());
    URLConnection ulc =  url.openConnection();
    InputStream in3 = ulc.getInputStream();
    InputStream in2 = url.openStream();
    
    InputStream in = loader.getResourceAsStream(entry.getName());
    if(in == null)
    {
      LogUtil.getCoreLog().trace("ReadContent inputStream is null entry.name={} , loader={}",entry.getName(),loader);
    }
    
    BufferedInputStream bi = new BufferedInputStream(in);
    byte[] bt = new byte[in.available()];
    bi.read(bt);
    bi.close();
    in.close();
    return bt;
  }
}
