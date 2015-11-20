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
package gemlite.core.internal.admin.snapshot;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.pdx.internal.EnumInfo;
import com.gemstone.gemfire.pdx.internal.PdxType;
import com.gemstone.gemfire.pdx.internal.TypeRegistry;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ExportedRegistry
{
  private final Map<Integer, PdxType> types;
  private final Map<Integer, EnumInfo> enums;
  
  public ExportedRegistry()
  {
    this.types = new HashMap();
    this.enums = new HashMap();
  }
  
  public ExportedRegistry(TypeRegistry tr)
  {
    this.types = new HashMap();
    this.enums = new HashMap();
  }
  
  public Map<Integer, PdxType> types()
  {
    return this.types;
  }
  
  public Map<Integer, EnumInfo> enums()
  {
    return this.enums;
  }
  
  public void addType(int id, PdxType type)
  {
    this.types.put(Integer.valueOf(id), type);
  }
  
  public void addEnum(int id, EnumInfo info)
  {
    this.enums.put(Integer.valueOf(id), info);
  }
  
  public PdxType getType(int id)
  {
    return (PdxType) this.types.get(Integer.valueOf(id));
  }
  
  public EnumInfo getEnum(int id)
  {
    return (EnumInfo) this.enums.get(Integer.valueOf(id));
  }
  
  public void toData(DataOutput out) throws IOException
  {
    out.writeInt(this.types.size());
    for (Map.Entry entry : this.types.entrySet())
    {
      DataSerializer.writeObject(entry.getValue(), out);
    }
    
    out.writeInt(this.enums.size());
    for (Map.Entry entry : this.enums.entrySet())
    {
      out.writeInt(((Integer) entry.getKey()).intValue());
      DataSerializer.writeObject(entry.getValue(), out);
    }
  }
  
  public void fromData(DataInput in) throws IOException, ClassNotFoundException
  {
    int typeCount = in.readInt();
    for (int i = 0; i < typeCount; ++i)
    {
      PdxType type = (PdxType) DataSerializer.readObject(in);
      this.types.put(Integer.valueOf(type.getTypeId()), type);
    }
    
    int enumCount = in.readInt();
    for (int i = 0; i < enumCount; ++i)
    {
      int id = in.readInt();
      EnumInfo ei = (EnumInfo) DataSerializer.readObject(in);
      this.enums.put(Integer.valueOf(id), ei);
    }
  }
}