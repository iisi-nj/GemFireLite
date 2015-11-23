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
package gemlite.shell.commands;

public interface CommandMeta
{
   public static final String LIST_SERVICES = "list services";
   public static final String LIST_REGIONS = "list regions";
   public static final String DESCRIBE_REGION = "describe region";
   public static final String SIZEM = "size -m";
   public static final String PRB = "pr -b";
   
   public static final String LIST_JOBS = "list jobs";
   public static final String DESCRIBE_JOB = "describe job";
   
   public static final String LIST_ASYNCQUEUE = "list asyncqueue";
   
   public static final String LIST_INDEX = "list index";
   public static final String DESCRIBE_INDEX = "describe index";
   public static final String QUERY_INDEX = "queryIndex";
   
   //列出check points
   public static final String LIST_CHECKPOINTS = "list checkpoints";
   public static final String RELOAD_CHECKPOINTS = "reload checkpoints";
   
   //单次查询
   public static final String QUERY = "query";
   
      //view
   public static final String LIST_VIEW = "list view";
   public static final String DESCRIBE_VIEW = "describe view"; 
}
