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
package test.thred;

import gemlite.core.common.DateUtil;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestSchedule
{
  public static void main(String[] args)
  {
    ScheduledExecutorService srv = (ScheduledExecutorService) Executors.newSingleThreadScheduledExecutor();
    
    T1 t1 = new T1();
    t1.id=1;
    srv.scheduleAtFixedRate(t1, 0,2, TimeUnit.SECONDS);
    
    try
    {
      Thread.sleep(10*1000l);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    
    System.out.println("-----------------------------------------------------");
      srv.shutdownNow();
    T1 t2 = new T1();
    t2.id=2;
    srv.scheduleAtFixedRate(t2, 0,1, TimeUnit.SECONDS);
    try
    {
      Thread.sleep(5*10222200l);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}

class T1 extends Thread
{
  int id;
  @Override
  public void run()
  {
    System.out.println(id+">>" +DateUtil.format(new Date(),"HH:mm:ss.SSS"));
  }
}