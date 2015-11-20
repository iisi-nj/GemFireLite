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

@Region("product")
@Table("product")
@AutoSerialize
public class Product
{ 
  @Key
  private int product_id;
  private String name;
  private double price;

  public int getProduct_id() 
  {
    return product_id;
  }
 
  public void setProduct_id(int product_id) 
  {
    this.product_id = product_id;
  }
  public String getName() 
  {
    return name;
  }
 
  public void setName(String name) 
  {
    this.name = name;
  }
  public double getPrice() 
  {
    return price;
  }
 
  public void setPrice(double price) 
  {
    this.price = price;
  }

  @Override
  public int hashCode() 
  {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	long temp;
	temp = Double.doubleToLongBits(price);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + product_id;
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
	Product other = (Product) obj;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
		return false;
	if (product_id != other.product_id)
		return false;
	return true;
  }
  
  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("Product [");
    builder.append("product_id=");
    builder.append(product_id);
    builder.append(", name=");
    builder.append(name);
    builder.append(", price=");
    builder.append(price);
    builder.append("]");
    return builder.toString();
  }
}
