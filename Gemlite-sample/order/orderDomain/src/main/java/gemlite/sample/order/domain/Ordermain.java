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

@Region("ordermain")
@Table("ordermain")
@Key(OrderKey.class)
@AutoSerialize
public class Ordermain
{ 
  private int sequence;
  private String name;
  private String id_num;
  private java.util.Date orderdate;

  public int getSequence() 
  {
    return sequence;
  }
 
  public void setSequence(int sequence) 
  {
    this.sequence = sequence;
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
  public java.util.Date getOrderdate() 
  {
    return orderdate;
  }
 
  public void setOrderdate(java.util.Date orderdate) 
  {
    this.orderdate = orderdate;
  }

  @Override
  public int hashCode()
  {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id_num == null) ? 0 : id_num.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((orderdate == null) ? 0 : orderdate.hashCode());
	result = prime * result + sequence;
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
	Ordermain other = (Ordermain) obj;
	if (id_num == null) {
		if (other.id_num != null)
			return false;
	} else if (!id_num.equals(other.id_num))
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	if (orderdate == null) {
		if (other.orderdate != null)
			return false;
	} else if (!orderdate.equals(other.orderdate))
		return false;
	if (sequence != other.sequence)
		return false;
	return true;
}
  
  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("Ordermain [");
    builder.append("sequence=");
    builder.append(sequence);
    builder.append(", name=");
    builder.append(name);
    builder.append(", id_num=");
    builder.append(id_num);
    builder.append(", orderdate=");
    builder.append(orderdate);
    builder.append("]");
    return builder.toString();
  }
}
