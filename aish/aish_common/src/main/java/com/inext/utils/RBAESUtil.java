package com.inext.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.KeySpec;

/**
 * @类说明：AES加密工具类
 * @类名：LBAESUtil.java
 * @作者：李江华
 * @创建日期：2013-1-19 下午03:15:40
 */
public class RBAESUtil {
    /**
     * @方法说明：将16进制转换为二进制
     * @方法作者：李江华
     * @创建日期：2013-1-19 下午03:15:10
     * @返回类型：byte[]
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * @方法说明：将二进制转换成16进制
     * @方法作者：李江华
     * @创建日期：2013-1-19 下午03:15:07
     * @返回类型：String
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * @方法说明：对传入的字符串做AES操作，返回16进制字符串
     * @方法作者：李江华
     * @创建日期：2013-1-19 下午03:19:16
     * @返回类型：String
     */
    public static String encrypt(String input, String password)
            throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes("utf-8"),
                "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210"
                .getBytes("utf-8"));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] byteContent = input.getBytes("utf-8");
        byte[] outText = cipher.doFinal(byteContent);
        return parseByte2HexStr(outText).toLowerCase();
    }

    /**
     * @方法说明：对传入的已经AES编码的16进制字符串进行解码操作，注意该方法要求传入待解码字符串长度必须是16的整数倍，不足采取的是补零处理。
     * @方法作者：李江华
     * @创建日期：2013-1-19 下午03:20:07
     * @返回类型：byte[]
     */
    public static byte[] decrypt(String input, String password)
            throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes("utf-8"),
                "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210"
                .getBytes("utf-8"));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] outText = cipher.doFinal(parseHexStr2Byte(input));
        return outText;
    }

    /**
     * @方法说明：对传入的字符串做AES操作，返回16进制字符串(长密码)
     * @方法作者：李江华
     * @创建日期：2013-1-19 下午03:19:16
     * @返回类型：String
     */
    public static String encrypt_longpassword(String input, String password)
            throws Exception {
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec ks = new PBEKeySpec(password.toCharArray(), "lubansoft"
                .getBytes(), 1024, 128);
        SecretKey s = f.generateSecret(ks);
        Key k = new SecretKeySpec(s.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210"
                .getBytes("utf-8"));
        cipher.init(Cipher.ENCRYPT_MODE, k, ivSpec);
        byte[] byteContent = input.getBytes("utf-8");
        byte[] outText = cipher.doFinal(byteContent);
        return parseByte2HexStr(outText).toLowerCase();
    }

    /**
     * @方法说明：对传入的已经AES编码的16进制字符串进行解码操作(长密码)
     * @方法作者：李江华
     * @创建日期：2013-1-19 下午03:20:07
     * @返回类型：byte[]
     */
    public static byte[] decrypt_longpassword(String input, String password)
            throws Exception {
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec ks = new PBEKeySpec(password.toCharArray(), "lubansoft"
                .getBytes(), 1024, 128);
        SecretKey s = f.generateSecret(ks);
        Key k = new SecretKeySpec(s.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210"
                .getBytes("utf-8"));
        cipher.init(Cipher.DECRYPT_MODE, k, ivSpec);
        byte[] outText = cipher.doFinal(parseHexStr2Byte(input));
        return outText;
    }

    public static void main(String[] args) {
        try {
            String mingwen = "sdfsdfsd快乐";
            String password = "0123456789lbsoft";
            String miwen = encrypt(mingwen, password);
            System.out.println(miwen);

            byte[] result = decrypt(miwen, password);

            System.out.println(new String(result).trim());// 去掉补全的空格
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}