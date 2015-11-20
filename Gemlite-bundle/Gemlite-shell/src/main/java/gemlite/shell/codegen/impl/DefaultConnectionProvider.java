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
package gemlite.shell.codegen.impl;

import gemlite.shell.codegen.ConnectionProvider;
import gemlite.shell.codegen.exception.DataAccessException;
import gemlite.shell.codegen.tools.jdbc.JDBCUtils;
import gemlite.core.util.LogUtil;
import java.sql.Connection;
import java.sql.Savepoint;

public class DefaultConnectionProvider implements ConnectionProvider
{
  private Connection connection;
  private boolean finalize;
  
  public DefaultConnectionProvider(Connection connection)
  {
    this(connection, false);
  }
  
  DefaultConnectionProvider(Connection connection, boolean finalize)
  {
    this.connection = connection;
    this.finalize = finalize;
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  @Override
  public final Connection acquire()
  {
    return connection;
  }
  
  @Override
  public final void release(Connection released)
  {
  }
  
  @Override
  protected void finalize() throws Throwable
  {
    if (finalize)
      JDBCUtils.safeClose(connection);
    super.finalize();
  }
  
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  public final void setConnection(Connection connection)
  {
    this.connection = connection;
  }
  
  public final void commit() throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("commit");
      connection.commit();
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot commit transaction", e);
    }
  }
  
  public final void rollback() throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("rollback");
      connection.rollback();
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot rollback transaction", e);
    }
  }
  
  public final void rollback(Savepoint savepoint) throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("rollback to savepoint");
      connection.rollback(savepoint);
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot rollback transaction", e);
    }
  }
  
  public final Savepoint setSavepoint() throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("set savepoint");
      return connection.setSavepoint();
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot set savepoint", e);
    }
  }
  
  public final Savepoint setSavepoint(String name) throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("set savepoint", name);
      return connection.setSavepoint(name);
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot set savepoint", e);
    }
  }
  
  public final void releaseSavepoint(Savepoint savepoint) throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("release savepoint");
      connection.releaseSavepoint(savepoint);
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot release savepoint", e);
    }
  }
  
  public final void setAutoCommit(boolean autoCommit) throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("setting auto commit", autoCommit);
      connection.setAutoCommit(autoCommit);
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot set autoCommit", e);
    }
  }
  
  public final boolean getAutoCommit() throws DataAccessException
  {
    try
    {
      return connection.getAutoCommit();
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot get autoCommit", e);
    }
  }
  
  public final void setHoldability(int holdability) throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("setting holdability", holdability);
      connection.setHoldability(holdability);
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot set holdability", e);
    }
  }
  
  public final int getHoldability() throws DataAccessException
  {
    try
    {
      return connection.getHoldability();
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot get holdability", e);
    }
  }
  
  public final void setTransactionIsolation(int level) throws DataAccessException
  {
    try
    {
      LogUtil.getCoreLog().debug("setting tx isolation", level);
      connection.setTransactionIsolation(level);
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot set transactionIsolation", e);
    }
  }
  
  public final int getTransactionIsolation() throws DataAccessException
  {
    try
    {
      return connection.getTransactionIsolation();
    }
    catch (Exception e)
    {
      throw new DataAccessException("Cannot get transactionIsolation", e);
    }
  }
}
