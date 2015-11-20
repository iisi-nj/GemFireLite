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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * (Ž°Upnå×
 * 
 * @author GSONG
 *         2015t720å
 */
@Entity
@Table(name = "gm_data_log")
public class GmDataLog implements Serializable
{
  private static final long serialVersionUID = 201864779443559982L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(length = 32)
  private String type;
  @Lob
  private String msg;
  
  private Date create_time;
  
  public GmDataLog()
  {
    
  }
  
  
  public GmDataLog(String msg)
  {
    this.msg = msg;
    this.type = "DataLog";
    this.create_time = new Date();
  }
  
  public long getId()
  {
    return id;
  }
  
  public void setId(long id)
  {
    this.id = id;
  }
  
  public String getType()
  {
    return type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  public String getMsg()
  {
    return msg;
  }
  
  public void setMsg(String msg)
  {
    this.msg = msg;
  }
  
  public Date getCreate_time()
  {
    return create_time;
  }
  
  public void setCreate_time(Date create_time)
  {
    this.create_time = create_time;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((create_time == null) ? 0 : create_time.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((msg == null) ? 0 : msg.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    GmDataLog other = (GmDataLog) obj;
    if (create_time == null)
    {
      if (other.create_time != null)
        return false;
    }
    else if (!create_time.equals(other.create_time))
      return false;
    if (id != other.id)
      return false;
    if (msg == null)
    {
      if (other.msg != null)
        return false;
    }
    else if (!msg.equals(other.msg))
      return false;
    if (type == null)
    {
      if (other.type != null)
        return false;
    }
    else if (!type.equals(other.type))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "GmDataLog [id=" + id + ", type=" + type + ", msg=" + msg + ", create_time=" + create_time + "]";
  }
}
