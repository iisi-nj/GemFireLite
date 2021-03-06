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
package gemlite.core.internal.testing.benchmark;

/**
 * 实现查询dao
 * @author GSONG
 * 2015年3月17日
 */
public interface IQueryDao
{
    /**
     * 文件传入的参数全是String类型,实现类需要自行转换
     * @param show 是否显示结果
     * @param args
     */
  public void doQuery(boolean show,String ...args);
}
