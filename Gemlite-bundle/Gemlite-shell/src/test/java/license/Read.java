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

import gemlite.core.common.DateUtil;
import gemlite.core.util.LogUtil;
import gemlite.core.util.RSAUtils;

/**
 * license
 * 
 * @author GSONG
 *         2015t818å
 */
public class Read
{
  static String publicKey;
  static String privateKey;
  
  public static void main(String[] args) throws Exception
  {
    // ûÖÁ
    String pubFile = "D:/codes/vmgemlite/Gemlite-bundle/Gemlite-shell/src/test/java/license/public.key";
    String license = "D:/codes/vmgemlite/Gemlite-bundle/Gemlite-shell/src/test/java/license/license.store";
    String publicKey = RSAUtils.readKey(pubFile);
    long today = DateUtil.today();
    
    //ûúe,ãÆ
    byte[] encoded = RSAUtils.readBytes(license);
    byte[] decodedData = RSAUtils.decryptByPublicKey(encoded, publicKey);
    String dd = new String(decodedData);
    
    long d = DateUtil.toLong(dd);
    
    if(d<today)
    {
      System.err.println("expired.");
    }
    System.err.println(dd);
  }
}
