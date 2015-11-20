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
package test.compile;

import gemlite.core.annotations.logic.CheckPoint;
import gemlite.core.internal.measurement.MeasureHelper;

public class Logic2
{
  
  public Object setp1()
  {
    long start = System.currentTimeMillis();
    f1();
    long end = System.currentTimeMillis();
    MeasureHelper.recordCheckPoint("cla","check", start, end);
    return null;
  }
  
  @CheckPoint("f1")
  public void f1()
  {
    long logic1 = System.currentTimeMillis();
    System.out.println(logic1);
  }
  
  
  public int f2()
  {
    long l0=System.currentTimeMillis();
    long logic1 = System.currentTimeMillis();
    System.out.println(logic1);
    long l1 = System.currentTimeMillis();
    MeasureHelper.recordCheckPoint("classname", "cpname",l0, l1);
    return 1;
  }
  
  public int f3()
  {
    String s1 = "sadfsadfsf";
    String s2 = "sadfsadfsf";
    int i1=0;
    int i2=0;
    long l0=System.currentTimeMillis();
    long logic1 = System.currentTimeMillis();
    String s21 = "sadfsadfsf";
    System.out.println(logic1);
    long l1 = System.currentTimeMillis();
    MeasureHelper.recordCheckPoint("classname", "cpname",l0, l1);
    return 1;
  }
}
