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
package gemlite.sample.order.domain;

import gemlite.core.annotations.domain.*;

@AutoSerialize
public class CustomerDBKey
{ 
  private String name;
  private String id_num;

  public String getName() 
  {
    return name;
  }
 
  public void setName(String name) 
  {
    this.name = name;
  }
  public String getId_num() 
  {
    return id_num;
  }
 
  public void setId_num(String id_num) 
  {
    this.id_num = id_num;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((id_num == null) ? 0 : id_num.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CustomerDBKey other = (CustomerDBKey) obj;
    if (name == null)
    {
      if (other.name != null)
        return false;
    }
    else if (!name.equals(other.name))
      return false;
    if (id_num == null)
    {
      if (other.id_num != null)
        return false;
    }
    else if (!id_num.equals(other.id_num))
      return false;
    return true;
  }
  
  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("CustomerDBKey [");
    builder.append("name=");
    builder.append(name);
    builder.append(", id_num=");
    builder.append(id_num);
    builder.append("]");
    return builder.toString();
  }
}
