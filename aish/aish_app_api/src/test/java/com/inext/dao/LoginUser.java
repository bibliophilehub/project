package com.inext.dao;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
//import Decoder.BASE64Encoder;

/**
 * @author admin1
 */
public class LoginUser {

    private final static String DES = "DES";

    public static void main(String[] args) throws Exception {
        System.out.println(encrypt("123456", "20150727"));
    }

    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        String strs = new sun.misc.BASE64Encoder().encode(bt);
//        String password=strs.replaceAll("\\+","\\[j]").replaceAll("/","\\[x]").replaceAll("=","\\[d]");
        return strs;
    }

    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
//        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

//        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//    this.key = keyFactory.generateSecret(new DESKeySpec(strKey
//          .getBytes("UTF8")));
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        //    this.key = keyFactory.generateSecret(new DESKeySpec(strKey
//    .getBytes("UTF8")));
        // 用密钥初始化Cipher对象
//        cipher.init(Cipher.ENCRYPT_MODE, securekey,null);
        cipher.init(Cipher.ENCRYPT_MODE, securekey);

        return cipher.doFinal(data);
    }
}
