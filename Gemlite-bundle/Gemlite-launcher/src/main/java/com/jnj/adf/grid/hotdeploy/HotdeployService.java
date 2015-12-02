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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.stereotype.Component;

import com.jnj.adf.grid.hotdeploy.ADFJarDeploy;

import gemlite.core.internal.support.FunctionIds;
import gemlite.core.internal.support.hotdeploy.DeployParameter;
import gemlite.core.internal.support.hotdeploy.finderClass.JpaURLFinder;
import gemlite.core.util.LogUtil;

/**
 * @author dyang39
 *
 */
@Component
public class HotdeployService {

	@GemfireFunction(id=FunctionIds.DEPLOY_FUNCTION)
	public boolean deploy(List<DeployParameter> dps) {

		List<URL> list = new ArrayList<>();
		for (DeployParameter dp : dps) {
			try {
				URL url = createLocalJarFile(dp);
				list.add(url);
			} catch (IOException e) {
				LogUtil.getCoreLog().error("Deploy name:{0} url:{1} create local file error.", dp.getModuleName(),
						dp.getUrl());
			}
		}
		URL[] urls = new URL[list.size()];
		list.toArray(urls);
		ADFJarDeploy.getInstance().deploy(urls);
		return true;
	}

	private URL createLocalJarFile(DeployParameter dp) throws IOException {
		byte[] bytes = dp.getBytes();
		if (bytes == null) {
			BufferedInputStream bi = new BufferedInputStream(dp.getUrl().openStream());
			bytes = new byte[bi.available()];
			bi.read(bytes);
			bi.close();
		}
		URL localUrl = JpaURLFinder.createTempJarFile(dp.getModuleName(), bytes);
		return localUrl;
	}

}
