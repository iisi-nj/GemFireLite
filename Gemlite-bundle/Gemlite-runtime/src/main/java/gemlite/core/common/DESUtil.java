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
package gemlite.core.common;

import gemlite.core.util.LogUtil;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESUtil
{
  private final static String DES = "DES";
  public final static String keySecret = "lJUyPnj58hB89jhJBNmLIhFqThbkcyuuKEWRGNOWTIHEWF";
  
  public static void main(String[] args) throws Exception
  {
    String data = "123 456";
    String key = "wang!@#$%";
    System.err.println(encrypt(data, key));
    System.err.println(decrypt(encrypt(data, key), key));
    
  }
  
  public static String encrypt(String data)
  {
    return encrypt(data,keySecret);
  }
  /**
   * Description 9n.<�L��
   * 
   * @param data
   * @param key
   *          ��.bytep�
   * @return
   * @throws Exception
   */
  public static String encrypt(String data, String key)
  {
      try
      {
          byte[] bt = encrypt(data.getBytes(), key.getBytes());
          String strs = new BASE64Encoder().encode(bt);
          return strs;
      }
      catch(Exception e)
      {
          LogUtil.getCoreLog().error("encrypt error:",e);
      }
      return data;
  }
  
  
  public static String decrypt(String data)
  {
      return decrypt(data,keySecret);
  }
  
  /**
   * Description 9n.<�L��
   * 
   * @param data
   * @param key
   *          ��.bytep�
   * @return
   * @throws IOException
   * @throws Exception
   */
  public static String decrypt(String data, String key)
  {
    if (data == null)
      return null;
    BASE64Decoder decoder = new BASE64Decoder();
    try
    {
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt);
    }
    catch(Exception e)
    {
        LogUtil.getCoreLog().error("decoder error:",e);
    }
    return data;
  }
  
  /**
   * Description 9n.<�L��
   * 
   * @param data
   * @param key
   *          ��.bytep�
   * @return
   * @throws Exception
   */
  private static byte[] encrypt(byte[] data, byte[] key) throws Exception
  {
    //  *�����:p�
    SecureRandom sr = new SecureRandom();
    
    // Ο�ƥpn�DESKeySpec�a
    DESKeySpec dks = new DESKeySpec(key);
    
    // � *ƥ�6(��DESKeySpeclbSecretKey�a
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
    SecretKey securekey = keyFactory.generateSecret(dks);
    
    // Cipher�a�E����\
    Cipher cipher = Cipher.getInstance(DES);
    
    // (ƥ�Cipher�a
    cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
    
    return cipher.doFinal(data);
  }
  
  /**
   * Description 9n.<�L��
   * 
   * @param data
   * @param key
   *          ��.bytep�
   * @return
   * @throws Exception
   */
  private static byte[] decrypt(byte[] data, byte[] key) throws Exception
  {
    //  *�����:p�
    SecureRandom sr = new SecureRandom();
    
    // Ο�ƥpn�DESKeySpec�a
    DESKeySpec dks = new DESKeySpec(key);
    
    // � *ƥ�6(��DESKeySpeclbSecretKey�a
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
    SecretKey securekey = keyFactory.generateSecret(dks);
    
    // Cipher�a�E����\
    Cipher cipher = Cipher.getInstance(DES);
    
    // (ƥ�Cipher�a
    cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
    
    return cipher.doFinal(data);
  }
}