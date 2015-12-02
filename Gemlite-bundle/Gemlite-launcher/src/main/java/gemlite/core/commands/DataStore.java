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
package gemlite.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.distributed.DistributedMember;

import gemlite.core.internal.support.context.DomainMapperHelper;
import gemlite.core.internal.support.events.GemliteEvent;
import gemlite.core.internal.support.events.IEventDispatcher;
import gemlite.core.internal.support.hotdeploy.JarURLFinderFactory;
import gemlite.core.internal.support.hotdeploy.finderClass.ClasspathURLFinder;
import gemlite.core.internal.support.system.GemliteAgent;
import gemlite.core.internal.support.system.ServerConfigHelper;
import gemlite.core.internal.support.system.ServerConfigHelper.ITEMS;
import gemlite.core.internal.support.system.ServerConfigHelper.TYPES;
import gemlite.core.internal.support.system.WorkPathHelper;
import gemlite.core.util.LogUtil;
import gemlite.core.util.Util;

public class DataStore implements IEventDispatcher {
	@Option(name = "-name", usage = "work path")
	private String serverName;
	@Option(name = "-configXml", usage = "work path")
	private String[] configXml = null;

	@Option(name = "-developMode", usage = "")
	private boolean developMode;

	private static DataStore dsInstance = new DataStore();

	private ClassPathXmlApplicationContext mainContext;

	private DataStore() {
	}

	public final static DataStore getInstance() {
		return dsInstance;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		DataStore.getInstance().start(args);
	}

	public void stop() {
		LogUtil.getCoreLog().info("cacheserver stopping ...");
	}

	public void waitForComplete() {
		while (true) {

			if (!CacheFactory.getAnyInstance().isClosed()) {
				try {
					Thread.sleep(500L);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				continue;
			}
			break;
		}
		mainContext.publishEvent(new GemliteEvent(GemliteEvent.STOPPED));
		mainContext.close();
		System.out.println("cacheserver stopped.\n");
	}

	public void basicStart(String[] args) {

		System.out.println("=======   Gemlite initialize environment & log4j     =======");
		ServerConfigHelper.initConfig(TYPES.DATASTORE);
		CmdLineParser.registerHandler(String[].class, StringArrayOptionHandler.class);
		CmdLineParser cp = new CmdLineParser(dsInstance);
		
		try {
			cp.parseArgument(args);
		} catch (CmdLineException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if (developMode)
			JarURLFinderFactory.setJarURLFinder(new ClasspathURLFinder());
		if (StringUtils.isBlank(serverName)) {
			serverName = WorkPathHelper.verifyServerName(ITEMS.GS_WORK, "server", serverName);
		}
		WorkPathHelper.verifyPath("deploy");
		ServerConfigHelper.setProperty(ServerConfigHelper.ITEMS.NODE_NAME.name(), serverName);
		ServerConfigHelper.setProperty(ServerConfigHelper.ITEMS.NODE_TYPE.name(), "server");
		ServerConfigHelper.setProperty("gemfire.BucketRegion.alwaysFireLocalListeners", "true");

		ServerConfigHelper.initLog4j("classpath:log4j2-server.xml");
		LogUtil.getCoreLog().info("NODE_NAME:" + serverName);
		System.out.println("=======     Initialize environment & log4j done    =======");
		if (configXml==null)
			mainContext = Util.initContext("ds-launcher.xml");
		else
		{
			List<String> list = new ArrayList<>();
			list.add("ds-launcher.xml");
			list.addAll(Arrays.asList(configXml));
			String[] configs = new String[list.size()];
			list.toArray(configs);
			mainContext = Util.initContext(configs);
		}
//		mainContext.setClassLoader(GemliteClassLoader.getInstance());
		Cache c = CacheFactory.getAnyInstance();
		DistributedMember m = c.getDistributedSystem().getDistributedMember();
		String memberId = m.getId();
		ServerConfigHelper.setProperty("MEMBER_ID", memberId);
		GemliteAgent.getInstance().startRMIConnector();
		GemliteAgent.getInstance().startHtmlAdapter();
		LogUtil.getCoreLog().info("Cacheserver " + serverName + "(" + memberId + "/" + Util.getPID() + ") started.");
		LogUtil.getCoreLog().info("---------------------------------------------------");
		DomainMapperHelper.scanMapperRegistryClass();
		mainContext.publishEvent(new GemliteEvent(GemliteEvent.STARTED));
	}

	public void start(String[] args) {
		try {
			basicStart(args);
			waitForComplete();
		} catch (Exception e) {
			LogUtil.getCoreLog().error("Error occur,DataStore exit", e);
			if (mainContext != null)
				mainContext.close();
			System.exit(-1);
		}

	}

	@Override
	public void sendEvent(ApplicationEvent e) {
		if (mainContext != null)
			mainContext.publishEvent(e);
	}


}
