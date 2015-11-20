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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "gm_checkpoints")
public class GmCheckPoints implements Serializable
{
    private static final long serialVersionUID = -1143009433884376795L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(length=128)
    private String module;
    
    @Column(name="servicename",length=128)
    private String servicename;
    
    @Column(name="classname",length=128)
    private String classname;

    @Column(length=128)
    private String method;
    
    @Lob
    private String desc;
    
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getModule()
    {
        return module;
    }

    public void setModule(String module)
    {
        this.module = module;
    }

    public String getServicename()
    {
        return servicename;
    }

    public void setServicename(String servicename)
    {
        this.servicename = servicename;
    }

    public String getClassname()
    {
        return classname;
    }

    public void setClassname(String classname)
    {
        this.classname = classname;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }
    
    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classname == null) ? 0 : classname.hashCode());
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((module == null) ? 0 : module.hashCode());
        result = prime * result + ((servicename == null) ? 0 : servicename.hashCode());
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
        GmCheckPoints other = (GmCheckPoints) obj;
        if (classname == null)
        {
            if (other.classname != null)
                return false;
        }
        else if (!classname.equals(other.classname))
            return false;
        if (desc == null)
        {
            if (other.desc != null)
                return false;
        }
        else if (!desc.equals(other.desc))
            return false;
        if (id != other.id)
            return false;
        if (method == null)
        {
            if (other.method != null)
                return false;
        }
        else if (!method.equals(other.method))
            return false;
        if (module == null)
        {
            if (other.module != null)
                return false;
        }
        else if (!module.equals(other.module))
            return false;
        if (servicename == null)
        {
            if (other.servicename != null)
                return false;
        }
        else if (!servicename.equals(other.servicename))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "GmCheckPoints [id=" + id + ", module=" + module + ", servicename=" + servicename + ", classname=" + classname + ", method=" + method
                + ", desc=" + desc + "]";
    }
}