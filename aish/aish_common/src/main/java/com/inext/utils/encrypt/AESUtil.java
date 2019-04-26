package com.inext.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
/**
 * AES编码器工具
 * 使用AES加密解密 AES-128-ECB加密
 *
 * @version 1.0
 */
public class AESUtil {
	public final static String KEY = "!@#$%^&*(";

    /**
     * KEY算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加密算法
     * "/算法/模式/补码方式"
     */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 编码格式
     */
    private static final String data_encoding = "utf-8";

    /**
     * 数据加密
     *
     * @param data 待加密的数据
     * @param key  加密使用的KEY
     * @return 加密之后的数据
     */
    public static String encryptAndBase64Encode(String data, String key) {
        try {
            return encryptAndBase64Encode(data.getBytes(data_encoding), key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对字节数组加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String encryptAndBase64Encode(byte[] data, String key) {
        try {
            if (key == null || "".equals(key.trim().length())) {
                return null;
            }
            byte[] raw = key.getBytes(data_encoding);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(data);
            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 数据解密，返回字符串
     *
     * @param data 待解密的数据
     * @param key  解密使用的KEY
     * @return 解密之后的数据
     */
    public static String decryptAfterBase64Decode(String data, String key) {
        try {
            byte[] bytes = decryptAfterBase64DecodeToByte(data, key);
            if (bytes != null && bytes.length > 0) {
                return new String(bytes, data_encoding);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据解密，返回字节数据
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] decryptAfterBase64DecodeToByte(String data, String key) {
        try {
            if (key == null || "".equals(key.trim().length())) {
                return null;
            }
            byte[] raw = key.getBytes(data_encoding);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(data);
            byte[] original = cipher.doFinal(encrypted1);
            return original;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String encrypt(String content, String password) {
		try {
			if (StringUtils.isBlank(password)) {
				password = KEY;
			}
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(byteContent);
			return ByteUtil.byteArr2HexStr(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String content, String password) {
		try {
			if (StringUtils.isBlank(password)) {
				password = KEY;
			}
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");// 修复linux系统报错问题
			secureRandom.setSeed(password.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(ByteUtil.hexStr2ByteArr(content));
			return new String(result, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		
		System.out.println(encrypt("aaa111",""));
	}

}
