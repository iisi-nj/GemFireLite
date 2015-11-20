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
package gemlite.core.internal.testing.processor.mq;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqSender
{
	private BufferedReader br;
	private String fileEncoding;
	private ConnectionFactory factory = new ConnectionFactory();
	private Connection connection = null;
	private Channel channel = null;
	private String host = "162.16.3.71";
	private int port = 5673;
	private String user = "guest";
	private String password = "guest";
	private String virtualHost = "/";
	private String exchange = "amq.direct";
	private String routeKey;
	  
	protected synchronized Channel getChannel()
	{
	    if (null == channel || !channel.isOpen())
	    {
	      try
	      {
	        channel = getConnection().createChannel();
	      }
	      catch (Throwable e)
	      {
	        return null;
	      }
	    }
	    
	    return channel;
	  }
	  
	  protected synchronized Connection getConnection()
	  {
	    if (null == connection || !connection.isOpen())
	    {
	      try
	      {
	        factory.setHost(host);
	        factory.setPort(port);
	        factory.setVirtualHost(virtualHost);
	        factory.setUsername(user);
	        factory.setPassword(password);
	        System.out.println("Host=" + host + " port=" + port + " virtualHost=" + virtualHost + " user=" + user);
	        connection = factory.newConnection();
	      }
	      catch (Throwable e)
	      {
	        System.err.println("RabbitMQ connect error!");
	        return null;
	      }
	    }
	    
	    return connection;
	  }
	  
	  public String getHost()
	  {
	    return host;
	  }
	  
	  public void setHost(String host)
	  {
	    this.host = host;
	  }
	  
	  public int getPort()
	  {
	    return port;
	  }
	  
	  public void setPort(int port)
	  {
	    this.port = port;
	  }
	
	  public String getExchange()
	  {
	    return exchange;
	  }
	  
	  public void setExchange(String exchange)
	  {
	    this.exchange = exchange;
	  }
	  
	  public String getRouteKey()
	  {
	    return routeKey;
	  }
	  
	  public void setRouteKey(String routeKey)
	  {
	    this.routeKey = routeKey;
	  }
	  
	  public void setInputSource(String fileName, String fileEncoding) 
	  {
		  try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), fileEncoding));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		  this.fileEncoding = fileEncoding;
	  }
	
	  private List<String> readPkgs() throws IOException
	  {
		  List<String> allPkgs = new ArrayList<String>();
		  StringBuffer sb = new StringBuffer();
		  String line = "";
		  while((line=br.readLine()) != null)
		  {
			  if(StringUtils.isBlank(line))
			  {
				allPkgs.add(sb.toString());
				sb = new StringBuffer();
			  }
			  else
				  sb.append(line+"\n");
		  }
		
		  if(StringUtils.isNotEmpty(sb.toString()))
				  allPkgs.add(sb.toString());
		  
		  return allPkgs;
	  }
	  
	  private void close()
	  {
		  try
		  {
			  channel.close();
			  connection.close();
		  }
		  catch (IOException e)
		  {
			  e.printStackTrace();
		  }
	  }
	  
	  public boolean send()
	  {
		  channel = getChannel();
		  if (channel != null)
		  {
			  try
			  {
				  List<String> allPkgs = readPkgs();
				  for(int i=0; i<allPkgs.size(); i++)
				  {
					  String pkg = allPkgs.get(i);
					  byte[] pkgBytes = pkg.getBytes(fileEncoding);
					  sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
					  String b64Str = enc.encodeBuffer(pkgBytes);
					  sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
					  byte[] decodeBytes2 = dec.decodeBuffer(b64Str);
					  channel.basicPublish(getExchange(), getRouteKey(), null, decodeBytes2);
				  }
				  
				  close();
				  return true;
			  }
			  catch (Exception e)
			  {
				  e.printStackTrace();
			  }
		  }
		  
		  return false;
	}
}
