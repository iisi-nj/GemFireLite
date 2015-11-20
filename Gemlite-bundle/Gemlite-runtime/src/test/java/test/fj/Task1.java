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
package test.fj;

import gemlite.core.internal.mq.domain.ParseredValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.lang.math.RandomUtils;

public class Task1 extends RecursiveTask<List<ParseredValue>>
{

  @Override
  protected List<ParseredValue> compute()
  {
    List<ParseredValue> l = new ArrayList<>();
    int n = RandomUtils.nextInt(10);
    for(int i=0;i<n;i++)
    {
      l.add(new ParseredValue());
    }
    
    return l;
  }
  
}
