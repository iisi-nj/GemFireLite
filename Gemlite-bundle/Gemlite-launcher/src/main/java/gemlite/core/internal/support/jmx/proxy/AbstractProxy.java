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
package gemlite.core.internal.support.jmx.proxy;

import gemlite.core.internal.support.jmx.annotation.AggregateOperation;
import gemlite.core.internal.support.jmx.domain.JMXBeanInfo;
import gemlite.core.util.LogUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBean;

public abstract class AbstractProxy implements InvocationHandler
{
  private ModelMBean originMBean;
  private String objectName;
  private ObjectName remoteObjectName;
  private Map<String, Object> values;
  private JMXBeanInfo beanInfo;
  private AttributeList attrList;
  
  /**
   * (ŽÔÞpn„ùa
   */
  private Map<String, Object> returnvalues;
  private AttributeList returnattrList;
  
  // private static Object ATTR_LOCK = new Object();
  
  public AbstractProxy(ModelMBean originBean)
  {
    this.originMBean = originBean;
    values = new ConcurrentHashMap<>();
    attrList = new AttributeList();
    returnvalues = new ConcurrentHashMap<>();
    returnattrList = new AttributeList();
  }
  
  public void resetValues()
  {
    values.clear();
    // ^'{z
    // attrList.clear();
  }
  
  public Object getValue(String name)
  {
    return values.get(name);
  }
  
  public void setValue(String name, Object value)
  {
    values.put(name, value);
    Attribute attr = new Attribute(name, value);
    // $­/&	<, d
    int index = attrList.indexOf(attr);
    if (index > 0)
    {
      attrList.remove(index);
    }
    attrList.add(attr);
  }
  
  public void updateReturnData()
  {
    if (values.size() > 0 && attrList.size() > 0)
    {
      returnvalues.clear();
      returnattrList.clear();
      returnvalues.putAll(values);
      returnattrList.addAll(attrList);
    }
  }
  
  public void setAttributeList(AttributeList list)
  {
    attrList.clear();
    attrList.addAll(list);
    for (int i = 0; i < list.size(); i++)
    {
      Attribute attr = (Attribute) list.get(i);
      values.put(attr.getName(), attr.getValue());
    }
  }
  
  /**
   * S·ÖAttributeListè1%ö,¾nvalues
   * 
   * @param list
   */
  public void setValues(AttributeList list)
  {
    for (int i = 0; i < list.size(); i++)
    {
      Attribute attr = (Attribute) list.get(i);
      values.put(attr.getName(), attr.getValue());
    }
  }
  
  public Map<String, Object> getValues()
  {
    return values;
  }
  
  public AttributeList getAttributeList()
  {
    return attrList;
  }
  
  public AttributeList getAttributes(String[] names)
  {
    return attrList;
  }
  
  public Map<String, Object> getReturnvalues()
  {
    return returnvalues.isEmpty() ? values : returnvalues;
  }
  
  public void setReturnvalues(Map<String, Object> returnvalues)
  {
    this.returnvalues = returnvalues;
  }
  
  public AttributeList getReturnattrList()
  {
    return returnattrList;
  }
  
  public void setReturnattrList(AttributeList returnattrList)
  {
    this.returnattrList = returnattrList;
  }
  
  // ^'{z,z$­,êvalues,¹¿¡—pn
  public boolean isEmpty()
  {
    // return attrList.size() == 0 || values.size() == 0;
    return values.size() == 0;
  }
  
  public boolean containsAttribute(String name)
  {
    return beanInfo.getFieldInfo().containsKey(name);
  }
  
  public boolean containsOperation(String name)
  {
    return beanInfo.getOperInfo().containsKey(name);
  }
  
  public boolean supportOperation(String name)
  {
    AggregateOperation op = beanInfo.getOperInfo().get(name);
    return op == null ? false : op.support();
  }
  
  public JMXBeanInfo getBeanInfo()
  {
    return beanInfo;
  }
  
  public void setBeanInfo(JMXBeanInfo beanInfo)
  {
    this.beanInfo = beanInfo;
  }
  
  public ModelMBean getOriginMBean()
  {
    return originMBean;
  }
  
  public void setOriginMBean(ModelMBean originMBean)
  {
    this.originMBean = originMBean;
  }
  
  public String getObjectName()
  {
    return objectName;
  }
  
  public void setObjectName(String objectName)
  {
    this.objectName = objectName;
  }
  
  protected abstract Object remoteCall(ObjectName objectName, String methodName, Object[] params, String[] signature);
  
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
  {
    String key = method.getName();
    if (LogUtil.getCoreLog().isTraceEnabled())
      LogUtil.getCoreLog().trace("MBeanProxy ObjectName:{} invoke:{} key:{} id:{}", remoteObjectName, method.getName(),
          key, this);
    if (("invoke").equals(key) || ("getAttribute").equals(key))
    {
      key = (String) args[0];
      if (containsAttribute(key))
        return getValue(key);
      else if (("getAttributesValues").equals(key))
      {
        return getReturnvalues();
      }
    }
    else if (("getAttributes").equals(key))
    {
      String[] names = (String[]) args[0];
      return getAttributes(names);
    }
    if (("invoke").equals(method.getName()) && containsOperation(key))
    {
      Object[] newArgs = (Object[]) args[1];
      String[] signature = (String[]) args[2];
      return remoteCall(remoteObjectName, key, newArgs, signature);
      
    }
    else
    {
      return method.invoke(getOriginMBean(), args);
    }
  }
  
  public ObjectName getRemoteObjectName()
  {
    return remoteObjectName;
  }
  
  public void setRemoteObjectName(ObjectName remoteObjectName)
  {
    this.remoteObjectName = remoteObjectName;
  }
  
}