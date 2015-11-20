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

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(persistLocation="d:/tmp/x1/",persistName="ak12",persistPeriod=5,persistPolicy="OnUpdate")
public class MyBean implements Serializable
{
  private String str1;
  private int int1;
  private long long1;
  private AtomicInteger ai1;
  private AtomicLong al1;
  
  @ManagedOperation
  public void test1()
  {
//    System.out.println(Util.getCallingClassName()+" "+Util.getCallingMethodName());
  }
  public String test2(String s1)
  {
//    String s=(Util.getCallingClassName()+" "+Util.getCallingMethodName());
    return null;
  }
  @ManagedOperation
  public void test4(String s1,String s2)
  {
  }
  
  @ManagedOperation
  public void test5(String s1,String s2,String s3,String s4,String s5,String s6,String s7)
  {
  }
  
  @ManagedAttribute
  public String getStr1()
  {
    return str1;
  }
  @ManagedAttribute
  public void setStr1(String str1)
  {
    this.str1 = str1;
  }
  public int getInt1()
  {
    return int1;
  } @ManagedAttribute
  public void setInt1(int int1)
  {
    this.int1 = int1;
  }
  
  @ManagedAttribute
  public long getLong1()
  {
    return long1;
  }
  public void setLong1(long long1)
  {
    this.long1 = long1;
  }
  public AtomicInteger getAi1()
  {
    return ai1;
  }
  public void setAi1(AtomicInteger ai1)
  {
    this.ai1 = ai1;
  }
  public AtomicLong getAl1()
  {
    return al1;
  }
  public void setAl1(AtomicLong al1)
  {
    this.al1 = al1;
  }
  
  public void set1(){
    
  }
  
}
