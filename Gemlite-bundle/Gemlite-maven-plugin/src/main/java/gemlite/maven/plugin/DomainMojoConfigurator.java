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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.configurator.AbstractComponentConfigurator;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.composite.ObjectWithFieldsConverter;
import org.codehaus.plexus.component.configurator.converters.special.ClassRealmConverter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

/**
 * 
 * @plexus.component
 *                   role=
 *                   "org.codehaus.plexus.component.configurator.ComponentConfigurator"
 *                   role-hint="include-project-dependencies"
 * @plexus.requirement role=
 *                     "org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup"
 *                     role-hint="default"
 * 
 */
public class DomainMojoConfigurator extends AbstractComponentConfigurator
{
  
  public void configureComponent(Object component, PlexusConfiguration configuration,
      ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm, ConfigurationListener listener)
      throws ComponentConfigurationException
  {
    addProjectDependenciesToClassRealm(expressionEvaluator, containerRealm);
    converterLookup.registerConverter(new ClassRealmConverter(containerRealm));
    ObjectWithFieldsConverter converter = new ObjectWithFieldsConverter();
    converter.processConfiguration(converterLookup, component, containerRealm, configuration, expressionEvaluator,
        listener);
  }
  
  @SuppressWarnings("unchecked")
  private void addProjectDependenciesToClassRealm(ExpressionEvaluator expressionEvaluator, ClassRealm containerRealm)
      throws ComponentConfigurationException
  {
    Set<String> runtimeClasspathElements = new HashSet<String>();
    try
    {
      runtimeClasspathElements.addAll((List<String>) expressionEvaluator
          .evaluate("${project.runtimeClasspathElements}"));
      
    }
    catch (ExpressionEvaluationException e)
    {
      throw new ComponentConfigurationException("There was a problem evaluating: ${project.runtimeClasspathElements}",
          e);
    }
    
    Collection<URL> urls = buildURLs(runtimeClasspathElements);
    urls.addAll(buildAritfactDependencies(expressionEvaluator));
    for (URL url : urls)
    {
      // containerRealm.addConstituent(url);
      containerRealm.addURL(url);
    }
  }
  
  private Collection<URL> buildAritfactDependencies(ExpressionEvaluator expressionEvaluator)
      throws ComponentConfigurationException
  {
    MavenProject project;
    try
    {
      project = (MavenProject) expressionEvaluator.evaluate("${project}");
    }
    catch (ExpressionEvaluationException e1)
    {
      throw new ComponentConfigurationException("There was a problem evaluating: ${project}", e1);
    }
    Collection<URL> urls = new ArrayList<URL>();
    for (Object a : project.getArtifacts())
    {
      try
      {
        urls.add(((Artifact) a).getFile().toURI().toURL());
      }
      catch (MalformedURLException e)
      {
        throw new ComponentConfigurationException("Unable to resolve artifact dependency: " + a, e);
      }
    }
    return urls;
  }
  
  private Collection<URL> buildURLs(Set<String> runtimeClasspathElements) throws ComponentConfigurationException
  {
    
    List<URL> urls = new ArrayList<URL>(runtimeClasspathElements.size());
    for (String element : runtimeClasspathElements)
    {
      try
      {
        final URL url = new File(element).toURI().toURL();
        urls.add(url);
      }
      catch (MalformedURLException e)
      {
        throw new ComponentConfigurationException("Unable to access project dependency: " + element, e);
      }
    }
    
    return urls;
  }
  
}
