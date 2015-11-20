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
package test.domain;

import java.util.Date;

//@TableMapper(tableName=DomainConstant.T_ac02,regionName=DomainConstant.R_ac02)
//@Region(name="")
//@Table(name="")
//@Index()
//@Index()
//@KeyClass(value=class)
//@TableMapper(idClass=Ac02Key.class,table="",region="")
public class Ac02
{
  
  // @Field
  private String aac001;
  private String aae140;
  // @Key
  private Date aac030;
  private String aac031;
  private String aae011;
  
  transient private Date aae036;
  
  private Ac02Key ac02key;
  
  public String getAac001()
  {
    return aac001;
  }
  
  public void setAac001(String aac001)
  {
    this.aac001 = aac001;
  }
  
  public String getAae140()
  {
    return aae140;
  }
  
  public void setAae140(String aae140)
  {
    this.aae140 = aae140;
  }
  
  public Date getAac030()
  {
    return aac030;
  }
  
  public void setAac030(Date aac030)
  {
    this.aac030 = aac030;
  }
  
  public String getAac031()
  {
    return aac031;
  }
  
  public void setAac031(String aac031)
  {
    this.aac031 = aac031;
  }
  
  public String getAae011()
  {
    return aae011;
  }
  
  public void setAae011(String aae011)
  {
    this.aae011 = aae011;
  }
  
  public Date getAae036()
  {
    return aae036;
  }
  
  public void setAae036(Date aae036)
  {
    this.aae036 = aae036;
  }
  
  public Ac02Key getAc02key()
  {
    return ac02key;
  }
  
  public void setAc02key(Ac02Key ac02key)
  {
    this.ac02key = ac02key;
  }
  
}