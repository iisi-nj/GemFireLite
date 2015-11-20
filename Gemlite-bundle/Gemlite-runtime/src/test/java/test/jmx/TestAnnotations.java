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
package test.jmx;

import gemlite.core.internal.measurement.RemoteServiceStat;
import gemlite.core.internal.support.jmx.MBeanHelper;
import gemlite.core.internal.support.jmx.annotation.AggregateAttribute;

import java.lang.reflect.Method;

import javax.management.MBeanAttributeInfo;
import javax.management.modelmbean.ModelMBean;

public class TestAnnotations
{
  public static void main(String[] args)
  {
    RemoteServiceStat rss = new RemoteServiceStat();
    ModelMBean mmb =  MBeanHelper.createModelMBean(rss);
    MBeanAttributeInfo[] attrs= mmb.getMBeanInfo().getAttributes();
    for(MBeanAttributeInfo inf:attrs)
    {
      String getMethodName = (String) inf.getDescriptor().getFieldValue("getMethod");
      try
      {
        Method m= rss.getClass().getMethod(getMethodName);
        AggregateAttribute aa= m.getAnnotation(AggregateAttribute.class);
        System.out.println(inf.getName()+" "+aa.value()+" "+inf.getType() );
      }
      catch (NoSuchMethodException | SecurityException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    }
  }
}
