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
package gemlite.core.internal.admin.measurement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScannedMethodItem implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -8937281358513610449L;

    String className;
    
    String methodName;
    String methodDesc;
    List<ScannedMethodItem> children;
    
    public ScannedMethodItem(String className, String methodName,String methodDesc)
    {
      super();
      className = className.replace('/','.');
      this.className = className;
      this.methodName = methodName;
      this.methodDesc = methodDesc;
      children = new ArrayList<>();
    }
    
    public boolean hasChildren()
    {
        return children!=null && children.size()>0;
    }
    
    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public List<ScannedMethodItem> getChildren()
    {
        return children;
    }

    public void setChildren(List<ScannedMethodItem> children)
    {
        this.children = children;
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((className == null) ? 0 : className.hashCode());
      result = prime * result + ((methodDesc == null) ? 0 : methodDesc.hashCode());
      result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
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
      ScannedMethodItem other = (ScannedMethodItem) obj;
      if (className == null)
      {
        if (other.className != null)
          return false;
      }
      else if (!className.equals(other.className))
        return false;
      if (methodDesc == null)
      {
        if (other.methodDesc != null)
          return false;
      }
      else if (!methodDesc.equals(other.methodDesc))
        return false;
      if (methodName == null)
      {
        if (other.methodName != null)
          return false;
      }
      else if (!methodName.equals(other.methodName))
        return false;
      return true;
    }

    @Override
    public String toString()
    {
        return "ScannedMethodItem [className=" + className + ", methodName=" + methodName + ", methodDesc=" + methodDesc + ", children=" + children + "]";
    }

    public String getMethodDesc()
    {
      return methodDesc;
    }

    public void setMethodDesc(String methodDesc)
    {
      this.methodDesc = methodDesc;
    }
  
}
