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
package gemlite.maven.plugin;

import gemlite.core.annotations.domain.AutoSerialize.Type;
import gemlite.maven.plugin.support.DomainMojoHelper;

import java.io.File;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.springframework.util.StringUtils;

/**
 * @goal domain-annotation
 * @phase compile
 * @configurator include-project-dependencies
 * @requiresDependencyResolution compile+runtime
 */
public class DomainAnnotationMojo extends AbstractMojo
{
  
  /**
   * @parameter expression="${project}"
   * @readonly
   */
  private MavenProject project;
  
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    DomainMojoHelper.setLog(getLog());
    Build build = project.getBuild();
    String outputDir = build.getOutputDirectory();
    System.setProperty("current-project-name", project.getName());
    Properties prop = project.getProperties();
    String domainSerializeType = prop.getProperty("AutoSerializeType");
    if (StringUtils.isEmpty(domainSerializeType))
    {
      domainSerializeType = Type.DS.name();
      DomainMojoHelper.log().info("AutoSerializeType -> default value " + domainSerializeType + "." );
    }
    else{
      DomainMojoHelper.log().info("AutoSerializeType -> " + domainSerializeType +"." );
    }
    
    System.setProperty("current-project-path", project.getBuild().getOutputDirectory());
    DomainMojoHelper.setLoader(Thread.currentThread().getContextClassLoader());
    DomainMojoExecutor executor = new DomainMojoExecutor(domainSerializeType);
    boolean success = executor.execute(outputDir,project.getName());
    if (!success)
    {
      File f = new File(build.getOutputDirectory());
      f.deleteOnExit();
      throw new MojoExecutionException("Domain class instrument failure.");
    }
    project.getProperties();
  }
  
}
