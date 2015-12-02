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
package gemlite.shell.service.admin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.gemfire.function.execution.GemfireOnServersFunctionTemplate;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.client.ClientCacheFactory;

import gemlite.core.internal.support.FunctionIds;
import gemlite.core.internal.support.GemliteException;
import gemlite.core.internal.support.context.JpaContext;
import gemlite.core.internal.support.hotdeploy.DeployParameter;
import gemlite.core.internal.support.jpa.files.domain.ReleasedJarFile;
import gemlite.core.internal.support.jpa.files.service.JarFileService;
import gemlite.core.util.LogUtil;

@Component
public class DeployService {
	public String deploy(String fileName) {

		try {
			File f = new File(fileName);
			URL url = null;
			if (!f.exists()) {
				LogUtil.getCoreLog().info("Search classpath ...");
				PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
				String path = "classpath:**/*" + fileName;
				Resource[] res = resolver.getResources(path);
				if (res.length > 0) {
					url = res[0].getURL();
				} else
					return path + " can not find in " + url;
			} else {
				url = f.toURI().toURL();
			}
			LogUtil.getCoreLog().info("Jar file path:" + url);
			JarFileService srv = JpaContext.getService(JarFileService.class);
			ReleasedJarFile jarFile = srv.deploy(url);

			String fname = null;
			try {
				fname = url.toURI().toURL().getFile();
			} catch (MalformedURLException | URISyntaxException e) {
				throw new GemliteException("Not a vaild url:" + url, e);
			}
			if (!srv.splitVersion(jarFile, fname)) {
				throw new GemliteException(fname + " not a valid jar file.");
			}

			// if (!srv.checkDuplicate(jarFile))
			// {
			// throw new GemliteException(jarFile.getFileName() + " has no
			// change.");
			// }
			// DeployParameter param = new
			// DeployParameter(jarFile.getModuleName(),jarFile.getModuleType(),jarFile.getContent());
			// FunctionUtil.deploy(param);
			srv.save(jarFile);
			List<ReleasedJarFile> list = srv.findActiveFiles();
			List<DeployParameter> dps = new ArrayList<>();
			for (ReleasedJarFile afile : list) {
				DeployParameter param = new DeployParameter(afile.getModuleName(), afile.getModuleType(),
						afile.getContent());
				dps.add(param);
			}
			GemfireOnServersFunctionTemplate t = new GemfireOnServersFunctionTemplate(
					ClientCacheFactory.getAnyInstance().getDefaultPool());
			t.executeAndExtract(FunctionIds.DEPLOY_FUNCTION, dps);

		} catch (IOException | GemliteException e) {
			e.printStackTrace();
			LogUtil.getCoreLog().error(fileName, e);
			return e.getMessage();
		}
		return "Deploy success";
	}
}
