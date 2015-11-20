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
package test.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ReadJar
{
  public static void main(String[] args) throws Exception, IOException
  {
    File f = new File( "D:/Code/vmgemlite/vmsample/Sample-bundle/Sample-logic/target/Sample-logic-0.0.1-SNAPSHOT.jar");
    URLClassLoader loader = new URLClassLoader(new URL[]{f.toURI().toURL()});
    JarInputStream ji = new JarInputStream(new FileInputStream(f));
    JarEntry entry =  ji.getNextJarEntry();
    while(entry!=null)
    {
      System.out.println(entry.getName());
      InputStream in= loader.getResourceAsStream(entry.getName());
      BufferedInputStream bi = new BufferedInputStream(in);
      byte[] bt = new byte[in.available()];
      bi.read(bt);
      bi.close();
      System.out.println(new String(bt));
      entry =  ji.getNextJarEntry();
    }
  }
}
