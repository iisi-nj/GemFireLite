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
package gemlite.core.internal.support.batch;

import gemlite.core.util.LogUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

public class BigFilePartitioner implements Partitioner
{
  private Resource resource;
  public final static String PARTITION_KEY="NIO_KEY";
  
  @Override
  public Map<String, ExecutionContext> partition(int gridSize)
  {
    try
    {
      FileInputStream fi = new FileInputStream(resource.getFile());
      FileChannel fc = (FileChannel) Channels.newChannel(fi);
      long fileSize = fc.size();
      long blockSize = fileSize / gridSize;
      Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>();
      for (int i = 0; i < gridSize; i++)
      {
        ExecutionContext context = new ExecutionContext();
        long position = i*blockSize;
        context.putLong("position",position);
        long limit = blockSize*(i+1);
        if(fileSize-limit<blockSize)
        {
          limit = 0;
        }
        context.putLong("limit",limit);
        map.put(PARTITION_KEY + i, context);
        
        LogUtil.getAppLog().info("position="+position+" limit="+limit);
      }
      return map;
    }
    catch (FileNotFoundException e)
    {
      LogUtil.getAppLog().error("fileName->" + resource.getFilename(), e);
    }
    catch (IOException e)
    {
      LogUtil.getAppLog().error("fileName->" + resource.getFilename(), e);
    }
    
    return null;
  }

  public Resource getResource()
  {
    return resource;
  }

  public void setResource(Resource resource)
  {
    this.resource = resource;
  }
  
}
