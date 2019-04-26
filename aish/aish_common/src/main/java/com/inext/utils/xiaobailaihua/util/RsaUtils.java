package com.inext.utils.xiaobailaihua.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
*	RSA加解密工具类
******
******加密调用String signVal = RsaUtils.rsaSign(Constants.HAH_WALLET_PRIVATE_KEY, '待加密字符串');
******
******验签调用boolean signCheckResult = RsaUtils.rsaSignCheck(Constants.XIAO_BAI_LAI_HUA_PUBLIC_KEY, requestDTO.getBiz_data(), requestDTO.getSign());
******
 */
public class RsaUtils {

    public static PrivateKey loadPrivateKey(String privateKey) {
        byte[] keyBates = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBates);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static PublicKey loadPublicKey(String publicKey) {
        byte[] buffer = Base64.decodeBase64(publicKey);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] rsaEncrypt(RSAPublicKey publicKey, byte[] plainTextData) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plainTextData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] rsaDecrypt(RSAPrivateKey privateKey, byte[] cipherData) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(cipherData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String rsaSign(String privateKeyStr, String plainText) {
        try {
            PrivateKey privateKey = loadPrivateKey(privateKeyStr);
            Signature sign = Signature.getInstance("SHA1WithRSA");
            sign.initSign(privateKey);
            byte[] plainTextData = plainText.getBytes("UTF-8");
            sign.update(plainTextData);
            byte[] signature = sign.sign();
            return Base64.encodeBase64String(signature);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean rsaSignCheck(String publicKeyStr, String plainText, String cipherText) {
        try {
            PublicKey publicKey = loadPublicKey(publicKeyStr);
            Signature sign = Signature.getInstance("SHA1WithRSA");
            sign.initVerify(publicKey);
            byte[] plainTextData = plainText.getBytes("UTF-8");
            sign.update(plainTextData);
            byte[] signature = Base64.decodeBase64(cipherText);
            return sign.verify(signature);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
