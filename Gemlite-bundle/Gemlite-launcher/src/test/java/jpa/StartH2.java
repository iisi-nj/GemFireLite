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
package jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.Server;

public class StartH2
{
  public static void main(String[] args)
  {
    System.setProperty("H2_HOME","d:/tmp/h2db/");
    String[] params = new String[]{"-tcpPort","8081","-tcp","-tcpAllowOthers","-baseDir","d:/tmp/h2"};
    Server h2serve =new Server();
    try
    {
      Server s = Server.createTcpServer(params);
      s.start();
      System.out.println(s);
      Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:8081/gemlite_manager2;USER=gemlite");
      System.out.println(conn);
      conn.close();
      Thread.sleep(Long.MAX_VALUE);
    }
    catch (SQLException | InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}
