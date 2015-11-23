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
package gemlite.core.internal.support.jpa.files.domain;

public enum ConfigKeys
{
    /**
     * 导数-数据源为数据库的数据库配置
     */
    import_dbdriver("import-dbdriver"),import_dburl("import-dburl"),import_dbuser("import-dbuser"),import_dbpsw("import-dbpsw"),
    
    /**
     * 机器列表,root密码,用户名,用户密码
     */
    cluster_locatorlist("cluster-locatorlist"),cluster_list("cluster-hostlist"),cluster_rootpsw("cluster-rootpsw"),cluster_username("cluster-username"),cluster_userpsw("cluster-userpsw"),cluster_primaryip("cluster-primaryip"),
    cluster_start_datastore("cluster-start-datastore"),cluster_start_locator("cluster-start-locator");
    
    private String value;

    public String getValue()
    {
        return this.value;
    }

    private ConfigKeys(String value)
    {
        this.value = value;
    }
}
