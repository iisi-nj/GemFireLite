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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 
/**
 * <p>
 * BASE64��w
 * </p>
 * <p>
 * �Vjavabase64-1.3.1.jar
 * </p>
 * 
 * @author gsong
 * @date 2015-08-18
 * @version 1.0
 */
@SuppressWarnings("restriction")
public class Base64Utils {
 
    /**
     * �����:'
     */
    private static final int CACHE_SIZE = 1024;
     
    /**
     * <p>
     * BASE64W&2�:��6pn
     * </p>
     * 
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64Decode(base64);
    }
     
    /**
     * <p>
     * ��6pn:BASE64W&2
     * </p>
     * 
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
        return Base64Encode(bytes);
    }
     
    /**
     * <p>
     * ��:BASE64W&2
     * </p>
     * <p>
     * '��N(����X��
     * </p>
     * 
     * @param filePath �����
     * @return
     * @throws Exception
     */
    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }
     
    /**
     * <p>
     * BASE64W&2lއ�
     * </p>
     * 
     * @param filePath �����
     * @param base64 W&2
     * @throws Exception
     */
    public static void decodeToFile(String filePath, String base64) throws Exception {
        byte[] bytes = decode(base64);
        byteArrayToFile(bytes, filePath);
    }
     
    /**
     * <p>
     * ��lb:��6p�
     * </p>
     * 
     * @param filePath ���
     * @return
     * @throws Exception
     */
    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
         }
        return data;
    }
     
    /**
     * <p>
     * ��6pn���
     * </p>
     * 
     * @param bytes ��6pn
     * @param filePath ���U
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);   
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {   
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        in.close();
    }
     
    
    /**
     * base64��
     * 
     * @param bytes
     * @return
     */
    public static String Base64Encode(byte[] bytes)
    {
      return new BASE64Encoder().encode(bytes);
    }
    
    /**
     * base64��
     * 
     * @param str
     * @return
     */
    public static byte[] Base64Decode(String str) throws IOException
    {
        return new BASE64Decoder().decodeBuffer(str);
    }
     
}