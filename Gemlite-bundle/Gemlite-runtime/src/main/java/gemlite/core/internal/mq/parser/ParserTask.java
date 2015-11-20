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
package gemlite.core.internal.mq.parser;

import gemlite.core.internal.mq.domain.ParseredValue;
import gemlite.core.util.LogUtil;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author ynd
 *
 */
public class ParserTask implements Callable<List<ParseredValue>>
{
  private String pkg;
  private ITableParser parser ;
  
  
  /**
   * @param pkg
   * @param parser
   */
  public ParserTask(String pkg, ITableParser parser)
  {
    super();
    this.pkg = pkg;
    this.parser = parser;
  }


  @Override
  public List<ParseredValue> call() throws Exception
  {
    List<ParseredValue> subList = parser.doBatchParser(pkg);
    if(LogUtil.getMqSyncLog().isDebugEnabled())
      LogUtil.getMqSyncLog().debug("SQL String to ParseredValue, size:"+subList.size()+" len:"+pkg.length());
    return subList;
  }
  
}
