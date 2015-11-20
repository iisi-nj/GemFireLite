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
package gemlite.shell.common.converters;

import org.junit.Test;
import org.springframework.util.Assert;

public class DerbyConverter implements TableConverter
{

    @Override
    public String converte(String table, String sortKey)
    {
        Assert.notNull(sortKey, "Derby Server converter error:Missing parameter sortKey!");
        StringBuilder sb = new StringBuilder();
        sb.append("select row_number() over()  as gf_rowid,a.* from ");
        sb.append(table);
        sb.append(" a Order by gf_rowid asc");
        return sb.toString();
    }
    
    @Test
    public void test(){
        DerbyConverter s = new DerbyConverter();
        System.out.println(s.converte("ordermain", "name"));
    }

}
