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
package batch;

import gemlite.core.util.Util;

import java.net.URL;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

public class TestResource
{
  public static void main(String[] args)
  {
    ClassPathXmlApplicationContext ctx = Util.initContext("simple-res.xml");
    System.out.println("res loaderd");
    ctx.close();
    try
    {
      String s1 ="D:/tmp/gemlite/data/prod*";
      String s2 ="classpath:prod*";
      String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(s2);
      URL url = ResourceUtils.getURL(resolvedLocation);
      System.out.println(resolvedLocation+" "+url);
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
  }
}
