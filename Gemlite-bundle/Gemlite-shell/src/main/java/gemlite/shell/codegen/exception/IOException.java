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
package gemlite.shell.codegen.exception;

public class IOException extends DataAccessException
{
  private static final long serialVersionUID = 491834858363345767L;
  
  public IOException(String message, java.io.IOException cause)
  {
    super(message, cause);
  }
  
  @Override
  public synchronized java.io.IOException getCause()
  {
    return (java.io.IOException) super.getCause();
  }
  
  @Override
  public synchronized Throwable initCause(Throwable cause)
  {
    if (!(cause instanceof java.io.IOException))
      throw new IllegalArgumentException("Can only wrap java.io.IOException: " + cause);
    return super.initCause(cause);
  }
}
