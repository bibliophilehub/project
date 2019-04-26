package com.inext.utils.partner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.inext.utils.StringUtils;


/**
 * Created by xq.
 */
public class AesUtil_51kabao {

    public static void main(String[] args) {
        String key="939B59B847E51601";
        String str1= aesEncrypt("这是加密数据",key,"");
        String str=aesDecrypt(str1,key,"");


    }
    
    public static String aesEncrypt(String content, String password, String algorithm) {
        return aesEncrypt(content, password, algorithm, (String)null);
    }

    public static String aesEncrypt(String content, String password, String algorithm, String offset) {
        try {
            if(StringUtils.isEmpty(algorithm)) {
                algorithm = "AES/ECB/PKCS5Padding";
            }

            byte[] pswd = password.getBytes("utf-8");
            SecretKeySpec secretKey = new SecretKeySpec(pswd, "AES");
            Cipher cipher = Cipher.getInstance(algorithm);
            if(StringUtils.isEmpty(offset)) {
                cipher.init(1, secretKey);
            } else {
                IvParameterSpec iv = new IvParameterSpec(offset.getBytes("utf-8"));
                cipher.init(2, secretKey, iv);
            }

            byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
            return (new Base64()).encodeToString(encrypted);
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static String aesDecrypt(String content, String password, String algorithm) {
        return aesDecrypt(content, password, algorithm, (String)null);
    }

    public static String aesDecrypt(String content, String password, String algorithm, String offset) {
        try {
            if(StringUtils.isEmpty(algorithm)) {
                algorithm = "AES/ECB/PKCS5Padding";
            }

            byte[] pswd = password.getBytes("utf-8");
            SecretKey secretKey = new SecretKeySpec(pswd, "AES");
            Cipher cipher = Cipher.getInstance(algorithm);
            if(StringUtils.isEmpty(offset)) {
                cipher.init(2, secretKey);
            } else {
                IvParameterSpec iv = new IvParameterSpec(offset.getBytes("utf-8"));
                cipher.init(2, secretKey, iv);
            }

            byte[] encrypted1 = (new Base64()).decode(content);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }


}
