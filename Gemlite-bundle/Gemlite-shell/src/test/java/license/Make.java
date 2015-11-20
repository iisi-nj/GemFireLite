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
package license;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

import org.apache.derby.tools.sysinfo;

import gemlite.core.common.DateUtil;
import gemlite.core.util.RSAUtils;

/**
 * license
 * 
 * @author GSONG
 *         2015t818å
 */
public class Make
{
  static String publicKey;
  static String privateKey;
  
  public static void main(String[] args) throws Exception
  {
    // ûÖÁ
    String privFile = "D:/codes/vmgemlite/Gemlite-bundle/Gemlite-shell/src/test/java/license/private.key";
    String pubFile = "D:/codes/vmgemlite/Gemlite-bundle/Gemlite-shell/src/test/java/license/public.key";
    String license = "D:/codes/vmgemlite/Gemlite-bundle/Gemlite-shell/src/test/java/license/license.store";
    String privateKey = RSAUtils.readKey(privFile);
    String publicKey = RSAUtils.readKey(pubFile);
    if (privateKey.isEmpty() || publicKey.isEmpty())
    {
      Map<String, Object> keyMap = RSAUtils.genKeyPair();
      publicKey = RSAUtils.getPublicKey(keyMap);
      privateKey = RSAUtils.getPrivateKey(keyMap);
      RSAUtils.writeKey(privFile, privateKey);
      RSAUtils.writeKey(pubFile, publicKey);
      System.err.println("l¥: \n\r" + publicKey);
      System.err.println("Á¥ \n\r" + privateKey);
    }
    
    long today = DateUtil.today();
    
    // t„Ê)
    long nextYear = DateUtil.addDay(today, 565);
    Date d = new Date(nextYear);
    String dd = DateUtil.format(d);
    String tt = DateUtil.format(new Date(today));
    dd = "dev";
    byte[] data = dd.getBytes();
    byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
    
    //™eÆ‡
    RSAUtils.writeBytes(license, encodedData);
    
    //ûúe,ãÆ
    byte[] encoded = RSAUtils.readBytes(license);
    byte[] decodedData = RSAUtils.decryptByPublicKey(encoded, publicKey);
    String target = new String(decodedData);
    
    System.err.println(target);
    
    
  }
  
}
