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

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @author dyang39
 *
 */
public class ClassLoaderResourceResolver implements ResourcePatternResolver {

	PathMatchingResourcePatternResolver resolver;

	public ClassLoaderResourceResolver(URLClassLoader classLoader) {
		resolver = new PathMatchingResourcePatternResolver(classLoader);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.core.io.support.PathMatchingResourcePatternResolver#
	 * getResources(java.lang.String)
	 */
	@Override
	public Resource[] getResources(String locationPattern) throws IOException {
		
		URLClassLoader loader = (URLClassLoader)getClassLoader();
		URL[] urls = loader.getURLs();
		List<Resource> list =new ArrayList<>();
		for(URL url:urls)
		{
			String path = url + "/**/*.class";
			Resource[] resources = resolver.getResources(path);
			list.addAll(Arrays.asList(resources));
		}
		Resource[] result = new Resource[list.size()];
		list.toArray(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.core.io.ResourceLoader#getResource(java.lang.String)
	 */
	@Override
	public Resource getResource(String location) {
		return resolver.getResource(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.core.io.ResourceLoader#getClassLoader()
	 */
	@Override
	public ClassLoader getClassLoader() {
		return resolver.getClassLoader();
	}
}
