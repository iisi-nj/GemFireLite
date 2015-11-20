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
package gemlite.core.internal.context.registryClass;

import gemlite.core.annotations.domain.AutoSerialize;
import gemlite.core.internal.asm.serialize.DataSerializeHelper;
import gemlite.core.internal.asm.serialize.PdxSerializeHelper;
import gemlite.core.internal.support.annotations.GemliteRegistry;
import gemlite.core.internal.support.context.IModuleContext;
import gemlite.core.internal.support.context.RegistryParam;
import gemlite.core.util.GemliteHelper;

import org.objectweb.asm.tree.ClassNode;
import org.springframework.util.StringUtils;

/***
 * 
 * @author gsong
 * 
 */

@GemliteRegistry(value = AutoSerialize.class, priority = 0)
public class AutoSerializeRegistry extends AbstractGemliteRegistry
{
  
  private static final String AutoSerializeType = System.getProperty("Gemlite.AutoSerialize.Type",
      AutoSerialize.Type.DS.name());

  @Override
  protected void doRegistry(IModuleContext context, RegistryParam param) throws Exception
  {
    String className = param.getClassName();
    ClassNode cn = param.getClassNode();
    String type = (String) param.getAnnValue("type");
    
    if (StringUtils.isEmpty(type))
      type = AutoSerializeType;
    
    if (AutoSerialize.Type.PDX.name().equals(type)) //generate pdx serialize
      PdxSerializeHelper.getInstance().process(cn);
    else
      DataSerializeHelper.getInstance().process(cn);
    
    byte[] bt = GemliteHelper.classNodeToBytes(cn);
    
    context.getLoader().addDynamicClass(className, bt);
  }
  
  @Override
  public void cleanAll()
  {
  }
  
  @Override
  public Object getItem(Object key)
  {
    return null;
  }
  
}
