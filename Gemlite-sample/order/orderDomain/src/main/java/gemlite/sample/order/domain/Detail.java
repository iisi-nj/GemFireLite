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


@Region("detail")
@Table("detail")
@Key(DetailKey.class)
@AutoSerialize
public class Detail
{ 
  private int sequence;
  private int product_id;
  private int ord_sequence;
  private String name;
  private String id_num;
  private int product_quantity;

  public int getSequence() 
  {
    return sequence;
  }
 
  public void setSequence(int sequence) 
  {
    this.sequence = sequence;
  }
  public int getProduct_id() 
  {
    return product_id;
  }
 
  public void setProduct_id(int product_id) 
  {
    this.product_id = product_id;
  }
  public int getOrd_sequence() 
  {
    return ord_sequence;
  }
 
  public void setOrd_sequence(int ord_sequence) 
  {
    this.ord_sequence = ord_sequence;
  }
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
  public int getProduct_quantity() 
  {
    return product_quantity;
  }
 
  public void setProduct_quantity(int product_quantity) 
  {
    this.product_quantity = product_quantity;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + sequence;
    result = prime * result + product_id;
    result = prime * result + ord_sequence;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((id_num == null) ? 0 : id_num.hashCode());
    result = prime * result + product_quantity;
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
    Detail other = (Detail) obj;
    if (sequence != other.sequence)
      return false;
    if (product_id != other.product_id)
      return false;
    if (ord_sequence != other.ord_sequence)
      return false;
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
    if (product_quantity != other.product_quantity)
      return false;
    return true;
  }
  
  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("Detail [");
    builder.append("sequence=");
    builder.append(sequence);
    builder.append(", product_id=");
    builder.append(product_id);
    builder.append(", ord_sequence=");
    builder.append(ord_sequence);
    builder.append(", name=");
    builder.append(name);
    builder.append(", id_num=");
    builder.append(id_num);
    builder.append(", product_quantity=");
    builder.append(product_quantity);
    builder.append("]");
    return builder.toString();
  }
}
