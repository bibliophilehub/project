package com.inext.utils.encrypt.yrqb;

import com.inext.utils.DateUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public final class AesYrqbUtil {

    /** 字符串默认键值 */
    //private static String strDefaultKey = "lLrR3qIC766SZAya";
    /** 偏移量 */
    //private static String iv = "2BdAa5BDYz3DZFUj";
    /**
     * 加密工具
     */
    private Cipher encryptCipher = null;
    /**
     * 解密工具
     */
    private Cipher decryptCipher = null;

    /**
     * 默认构造方法，使用默认密钥
     *
     * @throws Exception
     */
    public AesYrqbUtil() {
        //this(strDefaultKey);
    }


    /**
     * 指定密钥构造方法
     *
     * @param strKey 指定的密钥
     * @throws Exception
     */
    public AesYrqbUtil(String strKey, String iv) {

        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
            encryptCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getCurTime() {

        Date date = new Date();
        String dateTime = date.getTime() + "";
        return dateTime.substring(0, dateTime.length() - 3);
    }

    /**
     * main方法 。
     *
     * @param args
     */

    public static void main(String[] args) throws UnsupportedEncodingException {
        AesYrqbUtil des = new AesYrqbUtil("lLrR3qIC766SZAya", "2BdAa5BDYz3DZFUj");// 自定义密钥
        String abd = "DAnGpVwUMDiq/PSyGslrZcQpqU69ljIN2+rY2IU8rP8xcldeHJYsrkPKGpbg+7zQ3sZBxeB6HB0bzRavlfDFX1+10grwpHb4RLitc3d6X2lI44v1UkHFMO+fDIVPqlMUGN0ACEqoux9CW4ksyc/kRtT/9xb3WIfL1WYxElxyhg7fJhAByZYBUc72xBPyd6J4Qu8XkzK+TsRRICI6aX4P+cmWgCdNh0uckzC7W9VqTcPqWQRVjPR9XhO7LtwhO3Cm3l92PCDReUbF+1frgnRKHw==";
        byte[] decode = Base64.decode(abd);
        byte[] decrypt = des.decrypt(decode);
        String abc = new String(decrypt, "UTF-8");
        System.out.println("解密后的字符：" + abc);


        byte[] encrypt = abc.getBytes("UTF-8");
        byte[] encode = des.encrypt(encrypt);
        String abe = Base64.encode(encode);
        System.out.println("加密：" + abe);

        decode = Base64.decode(abe);
        decrypt = des.decrypt(decode);
        abc = new String(decrypt, "UTF-8");
        System.out.println("解密后的字符：" + abc);
        System.out.println("时间：" + DateUtils.formatDate(new Date(), DateUtils.timePattern2));//2017 0918 2017 06
        Date date = new Date();
        //1505 7843 31 903
        String dateTime = date.getTime() + "";
        System.out.println(dateTime);
        System.out.println(dateTime.substring(0, dateTime.length() - 3));
    }

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */

    public byte[] encrypt(byte[] arrB) {
        try {
            return encryptCipher.doFinal(arrB);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) {
        try {
            return decryptCipher.doFinal(arrB);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
