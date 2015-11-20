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
package gemlite.core.util;

import gemlite.core.internal.support.context.GemliteContext;
import gemlite.core.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * RSAl�/��/~�w
 * </p>
 * <p>
 * W���N��yRon [R]ivest	?�(�Adi [S]hamir	�&���?��Leonard [A]dleman	
 * </p>
 * <p>
 * W&2<�ƥ(*(y��ŵ�:BASE64<<br/>
 * 1�^�����vb ,��(�e��/(���<br/>
 * ^��Ɨ���(e���Ƅƥ���7��ƥ��h_1���pn��h
 * </p>
 * 
 * @author gsong
 * @date 2015-08-18
 * @version 1.0
 */
public class RSAUtils
{
  
  /**
   * �Ɨ�RSA
   */
  public static final String KEY_ALGORITHM = "RSA";
  
  /**
   * ~��
   */
  public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
  
  /**
   * ��l��key
   */
  private static final String PUBLIC_KEY = "RSAPublicKey";
  
  /**
   * �����key
   */
  private static final String PRIVATE_KEY = "RSAPrivateKey";
  
  /**
   * RSA '���'
   */
  private static final int MAX_ENCRYPT_BLOCK = 117;
  
  /**
   * RSA '��Ƈ'
   */
  private static final int MAX_DECRYPT_BLOCK = 128;
  
  /**
   * <p>
   * ƥ�(l����)
   * </p>
   * 
   * @return
   * @throws Exception
   */
  public static Map<String, Object> genKeyPair() throws Exception
  {
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
    keyPairGen.initialize(512);
    KeyPair keyPair = keyPairGen.generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    Map<String, Object> keyMap = new HashMap<String, Object>(2);
    keyMap.put(PUBLIC_KEY, publicKey);
    keyMap.put(PRIVATE_KEY, privateKey);
    return keyMap;
  }
  
  /**
   * <p>
   * (����opW~
   * </p>
   * 
   * @param data
   *          ��pn
   * @param privateKey
   *          ��(BASE64)
   * 
   * @return
   * @throws Exception
   */
  public static String sign(byte[] data, String privateKey) throws Exception
  {
    byte[] keyBytes = Base64Utils.decode(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initSign(privateK);
    signature.update(data);
    return Base64Utils.encode(signature.sign());
  }
  
  /**
   * <p>
   * !�pW~
   * </p>
   * 
   * @param data
   *          ��pn
   * @param publicKey
   *          l�(BASE64)
   * @param sign
   *          pW~
   * 
   * @return
   * @throws Exception
   * 
   */
  public static boolean verify(byte[] data, String publicKey, String sign) throws Exception
  {
    byte[] keyBytes = Base64Utils.decode(publicKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PublicKey publicK = keyFactory.generatePublic(keySpec);
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initVerify(publicK);
    signature.update(data);
    return signature.verify(Base64Utils.decode(sign));
  }
  
  /**
   * <P>
   * ����
   * </p>
   * 
   * @param encryptedData
   *          ��pn
   * @param privateKey
   *          ��(BASE64)
   * @return
   * @throws Exception
   */
  public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception
  {
    byte[] keyBytes = Base64Utils.decode(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.DECRYPT_MODE, privateK);
    int inputLen = encryptedData.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // �pn���
    while (inputLen - offSet > 0)
    {
      if (inputLen - offSet > MAX_DECRYPT_BLOCK)
      {
        cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
      }
      else
      {
        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_DECRYPT_BLOCK;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    return decryptedData;
  }
  
  /**
   * <p>
   * l���
   * </p>
   * 
   * @param encryptedData
   *          ��pn
   * @param publicKey
   *          l�(BASE64)
   * @return
   * @throws Exception
   */
  public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception
  {
    byte[] keyBytes = Base64Utils.decode(publicKey);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key publicK = keyFactory.generatePublic(x509KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.DECRYPT_MODE, publicK);
    int inputLen = encryptedData.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // �pn���
    while (inputLen - offSet > 0)
    {
      if (inputLen - offSet > MAX_DECRYPT_BLOCK)
      {
        cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
      }
      else
      {
        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_DECRYPT_BLOCK;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    return decryptedData;
  }
  
  /**
   * <p>
   * l���
   * </p>
   * 
   * @param data
   *          �pn
   * @param publicKey
   *          l�(BASE64)
   * @return
   * @throws Exception
   */
  public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception
  {
    byte[] keyBytes = Base64Utils.decode(publicKey);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key publicK = keyFactory.generatePublic(x509KeySpec);
    // �pn��
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, publicK);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // �pn���
    while (inputLen - offSet > 0)
    {
      if (inputLen - offSet > MAX_ENCRYPT_BLOCK)
      {
        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
      }
      else
      {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_ENCRYPT_BLOCK;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return encryptedData;
  }
  
  /**
   * <p>
   * ����
   * </p>
   * 
   * @param data
   *          �pn
   * @param privateKey
   *          ��(BASE64)
   * @return
   * @throws Exception
   */
  public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception
  {
    byte[] keyBytes = Base64Utils.decode(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, privateK);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;
    byte[] cache;
    int i = 0;
    // �pn���
    while (inputLen - offSet > 0)
    {
      if (inputLen - offSet > MAX_ENCRYPT_BLOCK)
      {
        cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
      }
      else
      {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * MAX_ENCRYPT_BLOCK;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return encryptedData;
  }
  
  /**
   * <p>
   * ����
   * </p>
   * 
   * @param keyMap
   *          ƥ�
   * @return
   * @throws Exception
   */
  public static String getPrivateKey(Map<String, Object> keyMap) throws Exception
  {
    Key key = (Key) keyMap.get(PRIVATE_KEY);
    return Base64Utils.encode(key.getEncoded());
  }
  
  /**
   * <p>
   * ��l�
   * </p>
   * 
   * @param keyMap
   *          ƥ�
   * @return
   * @throws Exception
   */
  public static String getPublicKey(Map<String, Object> keyMap) throws Exception
  {
    Key key = (Key) keyMap.get(PUBLIC_KEY);
    return Base64Utils.encode(key.getEncoded());
  }
  
  /**
   * ��keypn
   * @param filePath
   * @return
   */
  public static String readKey(String filePath)
  {
    StringBuilder sb = new StringBuilder();
    File file = new File(filePath);
    if(file.exists() && file.isFile())
    try
    {
      InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "gbk");
      BufferedReader reader = new BufferedReader(inputStreamReader);
      String nextLine;
      while ((nextLine = reader.readLine()) != null)
      {
        sb.append(nextLine);
      }
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().warn("readKey file :{} failure :{}",filePath,e);
    }
    return sb.toString();
  }
  
  
  /**
   * ��keypn
   * @param filePath
   * @return
   */
  public static String writeKey(String filePath,String content)
  {
    StringBuilder sb = new StringBuilder();
    File file = new File(filePath);
    try
    {
      String charsetName = "utf-8";
      OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file),charsetName);
      BufferedWriter pw=new BufferedWriter(writer);
      pw.write(content);
      pw.close();
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().warn("writeKey file :{} failure :{}",filePath,e);
    }
    return sb.toString();
  }
  
  
  public static  byte[] readBytes(String filePath)
  {
    byte[] content = new byte[0];
    File file = new File(filePath);
    if(file.exists() && file.isFile())
    try
    {
      InputStream in = new FileInputStream(file);
      BufferedInputStream bufin = new BufferedInputStream(in);
      int buffSize = 1024;
      ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);
      byte[] temp = new byte[buffSize];
      int size = 0;
      while ((size = bufin.read(temp)) != -1) {
        out.write(temp, 0, size);
      }
      bufin.close();
      content = out.toByteArray();
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().warn("readKey file :{} failure :{}",filePath,e);
    }
    return content;
  }
  
  
  /**
   * ��keypn
   * @param filePath
   * @return
   */
  public static String writeBytes(String filePath,byte[] content)
  {
    StringBuilder sb = new StringBuilder();
    File file = new File(filePath);
    if(file.exists())
      file.delete();
    try
    {
      FileOutputStream out = new FileOutputStream(file);
      out.write(content);
      out.close();
    }
    catch (Exception e)
    {
      LogUtil.getCoreLog().warn("writeBytes file :{} failure :{}",filePath,e);
    }
    return sb.toString();
  }
  
  public static boolean checkLicense()
  {
    String pubFile=RSAUtils.class.getClassLoader().getResource("public.key").getPath();
    String license=RSAUtils.class.getClassLoader().getResource("license.store").getPath();
    String publicKey =RSAUtils.readKey(pubFile);
    long today = DateUtil.today();
    try
    {
      byte[] encoded = RSAUtils.readBytes(license);
      byte[] decodedData = RSAUtils.decryptByPublicKey(encoded, publicKey);
      String dd = new String(decodedData);
      if(StringUtils.equals(dd, "dev"))
        return true;
      long d = DateUtil.toLong(dd);
      
      if(d<today)
      {
        LogUtil.getCoreLog().error("License expired at :{}",new Date(d));
        return false;
      }
      //�M30)В
      if(d<(today+30*24*60*60*1000L))
      {
        LogUtil.getCoreLog().warn("License will expired at :{}",new Date(d));
      }
    }
    catch(Exception e)
    {
      LogUtil.getCoreLog().error("License error:{}",e);
      return false;
    }
    return true;
  }
}