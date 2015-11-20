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
package gemlite.core.internal.mq;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang.math.NumberUtils;

public class MqThreadPool
{
  private static int parser_pool_size = NumberUtils.toInt(System.getProperty("gemlite.core.mq.parser-pool-size","128"));
  private static int merge_pool_parallelism  = NumberUtils.toInt(System.getProperty("gemlite.core.mq.merge-pool-parallelism","4"));
  public static ThreadPoolExecutor mqParserTaskPool ;
  public static ForkJoinPool mqSyncTaskPool ;
  public static ForkJoinPool mqMergeTaskPool;
  static
  {
    mqParserTaskPool =(ThreadPoolExecutor)Executors.newFixedThreadPool(parser_pool_size);
    mqMergeTaskPool = new ForkJoinPool(merge_pool_parallelism);
    mqSyncTaskPool = new ForkJoinPool(merge_pool_parallelism);
  }
  
}
