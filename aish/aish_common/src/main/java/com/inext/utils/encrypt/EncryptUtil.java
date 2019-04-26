package com.inext.utils.encrypt;


/**
 * 加密验签工具类
 *
 * @author 李凯
 * @data 2017年8月24日
 */
public class EncryptUtil {

    /**
     * 生成DesKey
     *
     * @param str       需要加密的内容（此文本内容可随便定义）
     * @param publicKey 手机贷分配的公钥
     * @return
     * @author likai
     * @data 2017年8月24日
     * @Version V1.0
     */
    public static String generateDesKey(String str, String publicKey) throws Exception {
        return RSA.encryptByPublicKey(str, publicKey);
    }

    /**
     * 解密DesKey
     *
     * @param str       需要加密的内容（此文本内容可随便定义）
     * @param publicKey 手机贷分配的公钥
     * @return
     * @author likai
     * @data 2017年8月24日
     * @Version V1.0
     */
    public static String decryptDesKey(String str, String pk) throws Exception {
        return RSA.decryptByPrivateKey(str, pk);
    }


    /**
     * 报文加密
     *
     * @param desKey
     * @param content 需要加密的报文
     * @return
     * @author likai
     * @data 2017年8月24日
     * @Version V1.0
     */
    public static String encode(String desKey, String content) throws Exception {
        DES crypt = new DES(desKey);
        String cryptStr = "";
        cryptStr = crypt.encrypt(content);
        return cryptStr;
    }


    /**
     * 报文签名
     *
     * @param content    加密后的报文内容
     * @param privateKey 私钥
     * @return
     * @author likai
     * @data 2017年8月24日
     * @Version V1.0
     */
    public static String sign(String content, String privateKey) throws Exception {
        String sign = RSA.sign(content, privateKey);
        return sign;
    }
}
