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
package gemlite.shell.codegen.tools.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reflect
{
  // ---------------------------------------------------------------------
  // Static API used as entrance points to the fluent API
  // ---------------------------------------------------------------------
  public static Reflect on(String name) throws ReflectException
  {
    return on(forName(name));
  }
  
  public static Reflect on(Class<?> clazz)
  {
    return new Reflect(clazz);
  }
  
  public static Reflect on(Object object)
  {
    return new Reflect(object);
  }
  
  public static <T extends AccessibleObject> T accessible(T accessible)
  {
    if (accessible == null)
    {
      return null;
    }
    if (accessible instanceof Member)
    {
      Member member = (Member) accessible;
      if (Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(member.getDeclaringClass().getModifiers()))
      {
        return accessible;
      }
    }
    if (!accessible.isAccessible())
    {
      accessible.setAccessible(true);
    }
    return accessible;
  }
  
  // ---------------------------------------------------------------------
  // Members
  // ---------------------------------------------------------------------
  private final Object object;
  private final boolean isClass;
  
  // ---------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------
  private Reflect(Class<?> type)
  {
    this.object = type;
    this.isClass = true;
  }
  
  private Reflect(Object object)
  {
    this.object = object;
    this.isClass = false;
  }
  
  // ---------------------------------------------------------------------
  // Fluent Reflection API
  // ---------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  public <T> T get()
  {
    return (T) object;
  }
  
  public Reflect set(String name, Object value) throws ReflectException
  {
    try
    {
      // Try setting a public field
      Field field = type().getField(name);
      field.set(object, unwrap(value));
      return this;
    }
    catch (Exception e1)
    {
      // Try again, setting a non-public field
      try
      {
        accessible(type().getDeclaredField(name)).set(object, unwrap(value));
        return this;
      }
      catch (Exception e2)
      {
        throw new ReflectException(e2);
      }
    }
  }
  
  public <T> T get(String name) throws ReflectException
  {
    return field(name).<T> get();
  }
  
  public Reflect field(String name) throws ReflectException
  {
    try
    {
      // Try getting a public field
      Field field = type().getField(name);
      return on(field.get(object));
    }
    catch (Exception e1)
    {
      // Try again, getting a non-public field
      try
      {
        return on(accessible(type().getDeclaredField(name)).get(object));
      }
      catch (Exception e2)
      {
        throw new ReflectException(e2);
      }
    }
  }
  
  public Map<String, Reflect> fields()
  {
    Map<String, Reflect> result = new LinkedHashMap<String, Reflect>();
    for (Field field : type().getFields())
    {
      if (!isClass ^ Modifier.isStatic(field.getModifiers()))
      {
        String name = field.getName();
        result.put(name, field(name));
      }
    }
    return result;
  }
  
  public Reflect call(String name) throws ReflectException
  {
    return call(name, new Object[0]);
  }
  
  public Reflect call(String name, Object... args) throws ReflectException
  {
    Class<?>[] types = types(args);
    // Try invoking the "canonical" method, i.e. the one with exact
    // matching argument types
    try
    {
      Method method = type().getMethod(name, types);
      return on(method, object, args);
    }
    // If there is no exact match, try to find one that has a "similar"
    // signature if primitive argument types are converted to their wrappers
    catch (NoSuchMethodException e)
    {
      for (Method method : type().getMethods())
      {
        if (method.getName().equals(name) && match(method.getParameterTypes(), types))
        {
          return on(method, object, args);
        }
      }
      throw new ReflectException(e);
    }
  }
  
  public Reflect create() throws ReflectException
  {
    return create(new Object[0]);
  }
  
  public Reflect create(Object... args) throws ReflectException
  {
    Class<?>[] types = types(args);
    // Try invoking the "canonical" constructor, i.e. the one with exact
    // matching argument types
    try
    {
      Constructor<?> constructor = type().getConstructor(types);
      return on(constructor, args);
    }
    // If there is no exact match, try to find one that has a "similar"
    // signature if primitive argument types are converted to their wrappers
    catch (NoSuchMethodException e)
    {
      for (Constructor<?> constructor : type().getConstructors())
      {
        if (match(constructor.getParameterTypes(), types))
        {
          return on(constructor, args);
        }
      }
      throw new ReflectException(e);
    }
  }
  
  @SuppressWarnings("unchecked")
  public <P> P as(Class<P> proxyType)
  {
    final boolean isMap = (object instanceof Map);
    final InvocationHandler handler = new InvocationHandler()
    {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
      {
        String name = method.getName();
        // Actual method name matches always come first
        try
        {
          return on(object).call(name, args).get();
        }
        // [#14] Simulate POJO behaviour on wrapped map objects
        catch (ReflectException e)
        {
          if (isMap)
          {
            Map<String, Object> map = (Map<String, Object>) object;
            int length = (args == null ? 0 : args.length);
            if (length == 0 && name.startsWith("get"))
            {
              return map.get(property(name.substring(3)));
            }
            else if (length == 0 && name.startsWith("is"))
            {
              return map.get(property(name.substring(2)));
            }
            else if (length == 1 && name.startsWith("set"))
            {
              map.put(property(name.substring(3)), args[0]);
              return null;
            }
          }
          throw e;
        }
      }
    };
    return (P) Proxy.newProxyInstance(proxyType.getClassLoader(), new Class[] { proxyType }, handler);
  }
  
  private static String property(String string)
  {
    int length = string.length();
    if (length == 0)
    {
      return "";
    }
    else if (length == 1)
    {
      return string.toLowerCase();
    }
    else
    {
      return string.substring(0, 1).toLowerCase() + string.substring(1);
    }
  }
  
  // ---------------------------------------------------------------------
  // Object API
  // ---------------------------------------------------------------------
  private boolean match(Class<?>[] declaredTypes, Class<?>[] actualTypes)
  {
    if (declaredTypes.length == actualTypes.length)
    {
      for (int i = 0; i < actualTypes.length; i++)
      {
        if (!wrapper(declaredTypes[i]).isAssignableFrom(wrapper(actualTypes[i])))
        {
          return false;
        }
      }
      return true;
    }
    else
    {
      return false;
    }
  }
  
  @Override
  public int hashCode()
  {
    return object.hashCode();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Reflect)
    {
      return object.equals(((Reflect) obj).get());
    }
    return false;
  }
  
  @Override
  public String toString()
  {
    return object.toString();
  }
  
  // ---------------------------------------------------------------------
  // Utility methods
  // ---------------------------------------------------------------------
  private static Reflect on(Constructor<?> constructor, Object... args) throws ReflectException
  {
    try
    {
      return on(constructor.newInstance(args));
    }
    catch (Exception e)
    {
      throw new ReflectException(e);
    }
  }
  
  private static Reflect on(Method method, Object object, Object... args) throws ReflectException
  {
    try
    {
      accessible(method);
      if (method.getReturnType() == void.class)
      {
        method.invoke(object, args);
        return on(object);
      }
      else
      {
        return on(method.invoke(object, args));
      }
    }
    catch (Exception e)
    {
      throw new ReflectException(e);
    }
  }
  
  private static Object unwrap(Object object)
  {
    if (object instanceof Reflect)
    {
      return ((Reflect) object).get();
    }
    return object;
  }
  
  private static Class<?>[] types(Object... values)
  {
    if (values == null)
    {
      return new Class[0];
    }
    Class<?>[] result = new Class[values.length];
    for (int i = 0; i < values.length; i++)
    {
      Object value = values[i];
      result[i] = value == null ? Object.class : value.getClass();
    }
    return result;
  }
  
  private static Class<?> forName(String name) throws ReflectException
  {
    try
    {
      return Class.forName(name);
    }
    catch (Exception e)
    {
      throw new ReflectException(e);
    }
  }
  
  public Class<?> type()
  {
    if (isClass)
    {
      return (Class<?>) object;
    }
    else
    {
      return object.getClass();
    }
  }
  
  public static Class<?> wrapper(Class<?> type)
  {
    if (type == null)
    {
      return null;
    }
    else if (type.isPrimitive())
    {
      if (boolean.class == type)
      {
        return Boolean.class;
      }
      else if (int.class == type)
      {
        return Integer.class;
      }
      else if (long.class == type)
      {
        return Long.class;
      }
      else if (short.class == type)
      {
        return Short.class;
      }
      else if (byte.class == type)
      {
        return Byte.class;
      }
      else if (double.class == type)
      {
        return Double.class;
      }
      else if (float.class == type)
      {
        return Float.class;
      }
      else if (char.class == type)
      {
        return Character.class;
      }
      else if (void.class == type)
      {
        return Void.class;
      }
    }
    return type;
  }
}
