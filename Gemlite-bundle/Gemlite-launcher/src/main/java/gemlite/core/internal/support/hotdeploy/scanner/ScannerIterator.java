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

import gemlite.core.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.io.FileUtils;

public class ScannerIterator implements Iterator<ScannerIteratorItem>
{
  
  //private static String INDEX_DEF_SUFFIX = System.getProperty("gemlite.core.index-def-suffix", ".def");
  
  private final static String class_suffix = ".class";
  private JarInputStream jarInputStream;
  private Iterator<File> classFileIterator;
  private boolean jarFile;// ture:jar false:path
  
  private int classpathLen;
  
  private Iterator<Map.Entry<String,byte[]>> dynClassesIterator;
  
  public ScannerIterator(Map<String, byte[]> byteMap) 
  {
	  jarFile = false;
	  dynClassesIterator = byteMap.entrySet().iterator();
  }
  
  public ScannerIterator(URL url) throws IOException, URISyntaxException
  {
    if ( url.getProtocol().equals("http") || url.getFile().endsWith(".jar"))
    {
      jarFile = true;
      jarInputStream = new JarInputStream(url.openStream());
    }
    else
    {
      Collection<File> colls = FileUtils.listFiles(new File(url.toURI()), new String[] { class_suffix.substring(1)}, true);
      classFileIterator = colls.iterator();
      classpathLen = url.getFile().length();
    }
    
  }
    
  public void close() throws IOException
  {
    if (jarInputStream != null)
      jarInputStream.close();
  }
  
  @Override
  public boolean hasNext()
  {
    if (jarInputStream != null)
    {
      try
      {
        return jarInputStream.available() == 1;
      }
      catch (IOException e)
      {
        return false;
      }
    }
    else if (classFileIterator != null)
      return classFileIterator.hasNext();
    else if(dynClassesIterator != null)
      return dynClassesIterator.hasNext();
    return false;
  }
  
  @Override
  public ScannerIteratorItem next()
  {
    ScannerIteratorItem sitem = null;
    if (jarInputStream != null)
    {
      try
      {
        JarEntry item = jarInputStream.getNextJarEntry();
        if (item != null)
          sitem = new ScannerIteratorItem(item.getName(), item,jarFile);
      }
      catch (IOException e)
      {
        LogUtil.getCoreLog().warn("JarInputStream is the reason,"+e.getMessage());
        return null;
      }
    }
    else if (classFileIterator != null)
    {
      File item = classFileIterator.next();
      if (item != null)
      {
        String filePath = item.getAbsolutePath();
        filePath = filePath.substring(classpathLen - 1);
        if (filePath.startsWith(".")||filePath.startsWith("/")||filePath.startsWith("\\"))
          filePath = filePath.substring(1);
        
        sitem = new ScannerIteratorItem(filePath, item,jarFile);
      }
    }
    else if(dynClassesIterator != null)
    {
    	Entry<String, byte[]> entry = dynClassesIterator.next();
    	sitem = new ScannerIteratorItem(entry.getKey(), entry.getValue(), false);
    }
    return sitem;
  }
  
  @Override
  public void remove()
  {
  }
}
