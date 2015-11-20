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
import java.util.HashMap;
import java.util.Map;

public class IndexClassLoader  extends ClassLoader
{
  private Map<String,byte[]> classBytesMap;
  
  public Map<String,String> mapping=new HashMap<String, String>();
  
  public IndexClassLoader(Map<String,byte[]> classBytesMap)
  {
    this(classBytesMap, Thread.currentThread().getContextClassLoader());
  }
  
  public IndexClassLoader(Map<String,byte[]> classBytesMap, ClassLoader loader)
  {
    super(loader);
    this.classBytesMap = classBytesMap;
  }
  
  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException
  {
    String name1 = mapping.get(name);
    byte[] bytes= classBytesMap.get(name1);
    return defineClass(name, bytes, 0, bytes.length);
  }
  
  public void clean()
  {
    classBytesMap.clear();
    classBytesMap=null;
  }
}
