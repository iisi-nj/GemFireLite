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
package gemlite.core.internal.mq;

import org.kohsuke.args4j.Option;

public class MqShellParameter extends ShellParameter
{
  @Option(name = "-h", usage = "rabbmitmq server host1:port1,[host2:port2]...")
  private String hostsAndPorts = "198.216.27.1:5672";
  
  @Option(name = "-v", usage = "vhost ,default %20")
  private String vhost = "/";
  
  @Option(name = "-e", usage = "rabbitmq server exchange, default: amq.direct ")
  private String exchange = "amq.topic";
  @Option(name = "-r", usage = "rabbitmq server routekey, default: order")
  private String routeKey = "";
  @Option(name = "-q", usage = "queue name")
  private String queue = "order";
  @Option(name = "-u", usage = "username:guest")
  private String userName = "guest";
  @Option(name = "-p", usage = "passward:guest")
  private String passward = "guest";

  @Option(name = "-s", usage = "the sink for the data, default:gemfire")
  private String sink = "gemfire";
  
  @Option(name = "-encoding", usage = "default:gb18030")
  private String encoding = "gb18030";
  @Option(name = "-base64", usage = "default:1 ")
  private int base64 = 1;
  
  
  public String getVhost()
  {
    return vhost;
  }
  
  public void setVhost(String vhost)
  {
    this.vhost = vhost;
  }
  
  public boolean isValid()
  {
    return true;
  }
  
  public String getHostsAndPorts()
  {
    return hostsAndPorts;
  }
  
  public void setHostsAndPorts(String hostsAndPorts)
  {
    this.hostsAndPorts = hostsAndPorts;
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
  
  public String getQueue()
  {
    return queue;
  }
  
  public void setQueue(String queue)
  {
    this.queue = queue;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public String getPassward()
  {
    return passward;
  }

  public void setPassward(String passward)
  {
    this.passward = passward;
  }

  public String getSink()
  {
    return sink;
  }

  public void setSink(String sink)
  {
    this.sink = sink;
  }

  public String getEncoding()
  {
    return encoding;
  }

  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
  }

  public boolean isBase64Used()
  {
    return base64 == 1;
  }

  public void setBase64(int base64)
  {
    this.base64 = base64;
  }
  
  

}
