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
package gemlite.core.internal.support.hotdeploy;

import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.hotdeploy.finderClass.CompositeJarURLFinder;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.internal.support.jpa.files.service.JarFileService;
import gemlite.core.internal.support.system.ServerConfigHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import sun.misc.URLClassPath;

public class JarURLFinderFactory
{
  private static JarURLFinder finderInstance = new CompositeJarURLFinder();
  
  public static void main(String[] args)
  {
    try
    {
      ServerConfigHelper.initConfig();
      ServerConfigHelper.initLog4j("log4j-test.xml");
      JpaContext.init();
      JarFileService srv = JpaContext.getService(JarFileService.class);
      ReleasedJarFile ff= srv.getFileById(6l);
      URL url =new URL("mem", null, 0, ff.getFileName(), new BytesURLStreamHandler(ff.getContent()));
//      GemliteDeployer.getInstance().deploy(url);
      
      URLClassPath ucp = new URLClassPath(new URL[]{url});
      Object obj =ucp.getResource("gemlite.core.internal.GemliteBuilder");
      System.out.println(obj);
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    
    
  }
  
  public final static void setJarURLFinder(JarURLFinder finder)
  {
    finderInstance = finder;
  }
  
  public final static JarURLFinder getFinderImpl()
  {
    return finderInstance;
  }
  
  public final static void useSimpleJarURLFinder(URL coreUrl, URL... startupUrls)
  {
    finderInstance= new LocalJarURLFinder(coreUrl, startupUrls);
  }
  
  public final static URL createMemURL(String name,byte[] bytes)
  {
    try
    {
      URL url = new URL("mem:", null, 0, null, new BytesURLStreamHandler(bytes));
      return url;
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}

class LocalJarURLFinder implements JarURLFinder
{
  URL[] startup;
  URL core;
  
  public LocalJarURLFinder(URL coreUrl, URL... startup)
  {
    this.core=coreUrl;
    this.startup = startup;
  }
  
  @Override
  public URL[] getURLsOnStartup()
  {
    return startup;
  }
  
  @Override
  public URL getCoreJarUrl()
  {
    return core;
  }
  
  @Override
  public void doFind()
  {
    
  }
}

class BytesURLConnection extends URLConnection
{
  protected byte[] content;
  
  protected int offset;
  
  protected int length;
  
  public BytesURLConnection(URL url, byte[] content)
  {
    super(url);
    this.content = content;
  }
  
  public void connect()
  {
  }
  
  public InputStream getInputStream()
  {
    return new ByteArrayInputStream(content);
  }
  
}

class BytesURLStreamHandler extends URLStreamHandler
{
  protected byte[] content;
  
  public BytesURLStreamHandler(byte[] content)
  {
    this.content = content;
  }
  
  @Override
  protected URLConnection openConnection(URL u) throws IOException
  {
    return new BytesURLConnection(u, content);
  }
}
