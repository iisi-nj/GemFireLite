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
package gemlite.core.internal.support.system;

import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class WorkPathHelper
{
  
  public void unlock(FileLock lock)
  {
    if (lock != null)
    {
      try
      {
        lock.release();
      }
      catch (IOException x)
      {
        throw new RuntimeException(x);
      }
      lock = null;
    }
  }
  /***
   * GS_HOME
   * @param path
   * @return
   */
  public static File verifyPath(String path)
  {
    return verifyPath(path, false);
  }
  
  /***
   * from GS_HOME
   * @param path
   * @param clean
   * @return
   */
  public static File verifyPath(String path, boolean clean)
  {
    String home = ServerConfigHelper.getConfig(ITEMS.GS_HOME);
    return verifyPath(home, path, clean);
  }
  
  /**
   * type:server,locator
   * 
   * @param type
   * @return
   */
  public static File verifyPath(String home, String path, boolean clean)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(home).append("/");
    String parent = builder.toString();
    File dir = new File(parent, path);
    if (clean && dir.exists())
    {
      FileUtils.deleteQuietly(dir);
    }
    if (!dir.exists())
    {
      boolean bl = dir.mkdirs();
      if (!bl)
      {
        System.err.println("Directory create failure:" + dir.getAbsolutePath());
        return null;
      }
    }
    return dir;
  }
  
  public static String verifyServerName(ITEMS item, String type, String name)
  {
    String home = ServerConfigHelper.getConfig(item);
    StringBuilder builder = new StringBuilder();
    builder.append(home).append("/").append(type);
    String parent = builder.toString();
    if (StringUtils.isBlank(name))
      return randomServerName(parent);
    else
    {
      File f = WorkPathHelper.findDirectory(parent, name);
      if (f != null)
      {
        if (lock(f))
        {
          System.out.println("Check directory ok:" + f);
          return name;
        }
      }
      
    }
    throw new RuntimeException("Error create work path:" + parent + "/" + name);
  }
  
  public static String randomServerName(String parent)
  {
    int i = 0;
    for (; i < 100; i++)
    {
      File f = findDirectory(parent, "node" + i);
      if (f != null)
      {
        if (lock(f))
        {
          return "node" + i;
        }
      }
      
    }
    
    return null;
  }
  
  /***
   * 
   * @return
   */
  public static synchronized File findDirectory(String parent, String serverName)
  {
    if (serverName != null)
    {
      File dir = new File(parent, serverName);
      if (!dir.exists())
      {
        boolean bl = dir.mkdirs();
        if (!bl)
        {
          System.err.println("Directory create failure:" + dir.getAbsolutePath());
          return null;
        }
        else
          return dir;
      }
      else
        return dir;
    }
    return null;
  }
  
  private static FileLock lock;
  
  private static boolean lock(File directory)
  {
    String name = "node.lock";
    File lockfile = new File(directory, name);
    lockfile.deleteOnExit();
    try
    {
      FileChannel fc = (FileChannel) Channels.newChannel(new FileOutputStream(lockfile));
      lock = fc.tryLock();
      if (lock != null)
      {
        return true;
      }
    }
    catch (IOException x)
    {
      System.err.println(x.toString());
    }
    catch (OverlappingFileLockException e)
    {
      System.err.println(e.toString());
    }
    return false;
  }
  
  public static String diskStore(String diskStoreName)
  {
    String parent = ServerConfigHelper.getConfig(ITEMS.GS_WORK);
    String serverName = System.getProperty(ServerConfigHelper.ITEMS.NODE_NAME.name());
    File diskStore = findDirectory(parent + "/server/" + serverName, diskStoreName);
    return diskStore.getAbsolutePath();
  }
  
  
}
