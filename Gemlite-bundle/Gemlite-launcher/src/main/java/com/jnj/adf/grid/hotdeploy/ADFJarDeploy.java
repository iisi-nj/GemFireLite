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
package com.jnj.adf.grid.hotdeploy;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ResourceLoader;

/**
 * @author dyang39
 *
 */
public class ADFJarDeploy {
	
	private final static ADFJarDeploy instance=new ADFJarDeploy();
	private SingleClassLoader loader;
	private AnnotationConfigApplicationContext context;
	private Map<String, URL> localURLMap=new HashMap<>();
	private ADFJarDeploy() {
	}
	
	public final static ADFJarDeploy getInstance()
	{
		return instance;
	}


	/***
	 * simple hotdeploy
	 * 
	 * @param localUrl
	 */
	public void deploy(URL[] urls) {
		loader = new SingleClassLoader(urls);
		context = new AnnotationConfigApplicationContext();
		ResourceLoader res = new ClassLoaderResourceResolver(loader);
		context.setClassLoader(loader);
		context.setResourceLoader(res);
		context.register(GemfireContainer.class);
		context.scan("*");
		context.refresh();
	}
	
	private void clean()
	{
		
	}
}
