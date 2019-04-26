package com.inext.utils.partner.D360;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/***
 * DES ECB对称加密 解密
 * 
 * @author spring sky Email:vipa1888@163.com QQ:840950105
 * 
 */
public class DES {
	public static String encrypt(String souce, String key)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key1 = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, key1, sr);
		// 现在，获取数据并加密
		byte encryptedData[] = cipher.doFinal(souce.getBytes("UTF-8"));
		// 通过BASE64位编码成字符创形式
		String base64Str = Base64Utils.encode(encryptedData);

		return base64Str;
	}

	public static String decrypt(String souce, String key)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			IOException, IllegalBlockSizeException, BadPaddingException {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key1 = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, key1, sr);
		// 将加密报文用BASE64算法转化为字节数组
		byte[] encryptedData = Base64Utils.decode(souce);
		// 用DES算法解密报文
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return new String(decryptedData, "UTF-8");
	}

}