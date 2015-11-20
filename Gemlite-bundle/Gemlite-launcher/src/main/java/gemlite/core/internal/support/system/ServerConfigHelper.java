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

import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.internal.support.context.ManagementRegionHelper;
import gemlite.core.util.LogUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.OptionConverter;
import org.springframework.util.SystemPropertyUtils;

import com.gemstone.gemfire.internal.cache.GemFireCacheImpl;

/**
 * @author ynd
 * 
 */
public class ServerConfigHelper
{
  public enum TYPES
  {
    DATASTORE, LOCATOR, JMX, MQ, CLIENT
  }
  
  public enum ITEMS
  {
    GS_ENV, GS_HOME, GS_WORK, BINDIP, LOCATORS, GS_LOGIC_JAR_DIR, DEPLOY_WORKING_DIR, NODE_NAME, LOCATOR, PORT, NODE_TYPE
  };
  
  private static final Set<String> NAMES = new HashSet<String>();
  private static final AtomicBoolean initlized = new AtomicBoolean(false);
  
  private static int locatorCount=0;
  public static String ENV_FILE=System.getProperty("gemlite.env.file", "env.properties");
  
  static
  {
    NAMES.add(ITEMS.GS_ENV.name());
    NAMES.add(ITEMS.GS_HOME.name());
    NAMES.add(ITEMS.GS_WORK.name());
    NAMES.add(ITEMS.BINDIP.name());
    NAMES.add(ITEMS.LOCATORS.name());
    
    serverConfig = new Properties();
  }
  
  private static Properties serverConfig;
  
  public static String getConfig(ITEMS item)
  {
    if (serverConfig != null)
    {
      return serverConfig.getProperty(item.name());
    }
    return System.getProperty(item.name());
  }
  
  public static void setProperty(String name, String value)
  {
    System.setProperty(name, value);
    serverConfig.setProperty(name, value);
  }
  
  public static String getProperty(String name)
  {
    return System.getProperty(name);
  }
  
  public static Integer getInteger(String name)
  {
    Object val = System.getProperties().get(name);
    if (val instanceof Integer)
      return (Integer) System.getProperties().get(name);
    else
      return NumberUtils.toInt(val.toString());
  }
  
  public static void initLog4j()
  {
    initLog4j("log4j2-server.xml");
  }
  
  public static void initLog4j(String configFile)
  {
    configFile = "log4j2-server.xml";
    if (getProperty(ITEMS.NODE_TYPE.name()) == null)
      setProperty("NODE_TYPE", "");
    if (getProperty(ITEMS.NODE_NAME.name()) == null)
      setProperty("NODE_NAME", "");
    String resolvedLocation = SystemPropertyUtils.resolvePlaceholders("log4j2-server.xml");
    
    URL url;
    try
    {
      url = ServerConfigHelper.class.getClassLoader().getResource(configFile);
      LoggerContext context = (LoggerContext) LogManager.getContext(false);
      context.setConfigLocation(url.toURI());
      LogUtil.init();
    }
    catch (URISyntaxException e)
    {
      e.printStackTrace();
    }
    
    // System.setProperty("log4j.configurationFile", configFile);
    
    // try
    // {
    // if (!configFile.startsWith("classpath"))
    // configFile = "classpath:" + configFile;
    // Log4jConfigurer.initLogging(configFile);
    // }
    // catch (FileNotFoundException e)
    // {
    // System.err.println("File " + configFile + " not exists!");
    // }
  }
  
  public static void initConfig()
  {
    initConfig(TYPES.CLIENT, ENV_FILE);
  }
  
  public static void initConfig(String envFile)
  {
    initConfig(TYPES.CLIENT, envFile);
  }
  
  public static void initConfig(TYPES type)
  {
    initConfig(type,ENV_FILE);
  }
  public static void initConfig(TYPES type, String envFile)
  {
    if (initlized.get())
      return;
    initlized.set(true);
    initConfig0(envFile);
    GemliteContext.getRuntimeInfo().setNodeType(type);
    GemliteContext.getRuntimeInfo().setHomePath(getHomePath());
    GemliteContext.getRuntimeInfo().setWorkPath(getWorkPath());
    GemliteContext.getRuntimeInfo().setNodeName(getNodeName());
  }
  
  private synchronized static void initConfig0(String envFile)
  {
    try
    {
      Enumeration<URL> envs = ServerConfigHelper.class.getClassLoader().getResources(envFile);
      List<URL> list = new ArrayList<>();
      while (envs.hasMoreElements())
      {
        list.add(envs.nextElement());
      }
      for (int i = list.size() - 1; i >= 0; i--)
      {
        URL url = list.get(i);
        System.out.println("Load env from:" + url);
        Properties prop = new Properties();
        prop.load(url.openStream());
        serverConfig.putAll(prop);
      }
      // String url=ServerConfigHelper.class.getClassLoader().getResource(envFile).toString();
      // System.out.println("Load env from:"+url);
      // serverConfig.load(ServerConfigHelper.class.getClassLoader().getResourceAsStream(envFile));
    }
    catch (IOException e)
    {
      e.printStackTrace();
      initlized.set(false);
    }
    List<Object> names = new ArrayList<>(serverConfig.keySet());
    Collections.sort(names, new Comparator<Object>()
    {
      
      @Override
      public int compare(Object o1, Object o2)
      {
        return o1.toString().compareTo(o2.toString());
      }
    });
    Iterator<Object> it = names.iterator();
    while (it.hasNext())
    {
      String key = it.next().toString();
      // String value = serverConfig.getProperty(key);
      String value = OptionConverter.findAndSubst(key, serverConfig);
      if (NAMES.contains(key))
        processENV(key, value);
      else
        System.out.println(key + ":" + value);
      System.setProperty(key, value);
    }
    
    if (System.getProperty("BINDIP") == null && System.getenv("BINDIP") != null)
      setProperty(ITEMS.BINDIP.name(), System.getenv("BINDIP"));
    
    String bindip = getConfig(ITEMS.BINDIP);
    boolean localhost = bindip.equalsIgnoreCase("localhost");
    if (localhost)
    {
      try
      {
        InetAddress addr = InetAddress.getLocalHost();
        bindip = addr.getHostAddress();
        setProperty(ITEMS.BINDIP.name(), bindip);
        System.out.println("BINDIP changed to : " + bindip);
        String locators = getConfig(ITEMS.LOCATORS);
        locators = locators.replaceAll("localhost", bindip);
        setProperty(ITEMS.LOCATORS.name(), locators);
        System.out.println("LOCATORS changed to : " + locators);
      }
      catch (UnknownHostException e)
      {
        initlized.set(false);
        System.err.println("Unkonw host!");
      }
    }
    
    processLocators();
    // log
    // PropertyConfigurator.configure(ServerConfigHelper.class.getClassLoader().getResource(log4jFile));
    
    WorkPathHelper.verifyPath("log");
  }
  
  private static void processLocators()
  {
    String locators = System.getProperty(ITEMS.LOCATORS.name());
    if (!StringUtils.isEmpty(locators))
    {
      String[] grp = locators.split(",");
      locatorCount = grp.length;
      for (int i = 0; i < grp.length; i++)
      {
        String[] sub = grp[i].split("\\[");
        String ip = sub[0];
        String port = sub[1].substring(0, sub[1].length() - 1);
        String key1 = ITEMS.LOCATOR.name() + (i + 1);
        String key2 = ITEMS.PORT.name() + (i + 1);
        System.getProperties().put(key1, ip);
        System.getProperties().put(key2, Integer.valueOf(port));
        System.out.println(key1 + ":" + ip);
        System.out.println(key2 + ":" + port);
      }
    }
  }
  
  private static void f1()
  {
    Set<Object> set= System.getProperties().keySet();
    for(Object kk:set)
    {
      String key = kk.toString();
      if(key.startsWith("gemfire."))
      {
        
      }
    }
  }
  
  private static void processENV(String name, String valueInFile)
  {
    String value = System.getenv(name);
    if (StringUtils.isEmpty(value))
    {
      value = System.getProperty(name);
    }
    if (StringUtils.isEmpty(value))
    {
      value = valueInFile;
    }
    System.setProperty(name, value);
    if (StringUtils.isBlank(value))
    {
      throw new RuntimeException("ENV '" + name + "' not set!!");
    }
    System.out.println(name + ":" + value);
  }
  
  public static String getNodeName()
  {
    return getConfig(ITEMS.NODE_NAME);
  }
  
  public static String getWorkPath()
  {
    return getConfig(ITEMS.GS_WORK);
  }
  
  public static String getHomePath()
  {
    return getConfig(ITEMS.GS_HOME);
  }

  public static int getLocatorCount()
  {
    return locatorCount;
  }
  
}
