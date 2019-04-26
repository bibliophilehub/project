package com.inext.utils.encrypt.jfwk;

import com.inext.utils.encrypt.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * 玖富万卡RSA加密解密公共类
 *
 * @author admin
 */

/**
 * @author admin
 *
 */
public class JFwkRSAUtils {
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    /** */
    //	//公钥
//	public static final String public_key ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDD2DgyhQ+G05tdxrVTb9VZ20z2JCKZodDz8JIJ"+
//			"eIXjJdJiLr4w1Gpsm2s/7X9x53JKW3Jvc20uLcj0yD1IOBM+XBmmccGNSWRc/448LLvOzLAieesV"+
//			"+HWUqRIultS6xv2LqluHkVm+1TdrYGD1l98YX58xZSSse2qUvxLf7wniJwIDAQAB";
//	//私钥
//	public static final String private_key ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMPYODKFD4bTm13GtVNv1VnbTPYk"+
//            "Ipmh0PPwkgl4heMl0mIuvjDUamybaz/tf3Hnckpbcm9zbS4tyPTIPUg4Ez5cGaZxwY1JZFz/jjws"+
//            "u87MsCJ56xX4dZSpEi6W1LrG/YuqW4eRWb7VN2tgYPWX3xhfnzFlJKx7apS/Et/vCeInAgMBAAEC"+
//            "gYBfKmQ0eGARHiZzLSnTf5Zm8Z+2Q9zkVrNYtl/gZkZ5GFnhB+G3jKCiYet9xwSU7uikpUc4TRiS"+
//            "xgDOobbVrxn565+24H9MN9Z9vl5/slDBXhMu+LOODzQCgTceti45Ibd4pwpw6Qq2aq1DqVXTOqCp"+
//            "n/jJfmeZEtbt6MoA0ddKgQJBAOrS0KEVx2RVrz/1jZxh3bM7t3VHKhY3ZPKMYbQ9sIFoS1dzNQYb"+
//            "o5ZAVWDKRvsZSA+t3SyxPuEI73z85Se/JMcCQQDVgYeY1Tb5TyLhmR2ONZwHAnQFuraIbyTyhsP7"+
//            "30gCaCrXIpZUjOfukJcH0wgxHpdH3liNRC5RqdfOw3lCqzehAkAJNzTQ3ZXxrhsum2hvVrforNNK"+
//            "Wvyf2pSvoCrFdBZVPc6XJAJUtwj4gJXZMpcOi7N0ShKACoS5OCyN7y7fHHVJAkEAwl96Ixl5Qt9Y"+
//            "0imjTqRft8Hz/oNNSkhlSqaGJffQhuBuoA1M7wyY2geod+cXviArebJiy7kWsiH95q7u5lMaAQJB"+
//            "AJmJ75JmvjOCxfWts5BeeLCtp0CWQuwpuZPSUeo9KnwA0rE3PNz6p0KoLxwXUzjEo1rFxAGR3DMz"+
//            "NR/V5J+Wh8c=";
    //公钥
    public static String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDyt2tW1vpFsiUyZ32FxiOjyDkWqxuwndqGlqzr"
            + "e6lWTuzc4xW6zbh8UkQURDjxIHN6mHHOOPYCf7Mho9/00ArQ142wjYxPQJ8aktCJUEpOQwSQwbun"
            + "sxyS0eSxydspmk3ezSlaHboW3OPc3FXJRe3PrXvaE1lTGaRyNdMuom+U/QIDAQAB";

    /** */
    //私钥
    public static String private_key = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAPK3a1bW+kWyJTJnfYXGI6PIORar"
            + "G7Cd2oaWrOt7qVZO7NzjFbrNuHxSRBREOPEgc3qYcc449gJ/syGj3/TQCtDXjbCNjE9AnxqS0IlQ"
            + "Sk5DBJDBu6ezHJLR5LHJ2ymaTd7NKVoduhbc49zcVclF7c+te9oTWVMZpHI10y6ib5T9AgMBAAEC"
            + "gYEAmvGGf3hbapSv9D5mvjhf1hVihFsVISmUMCkOGIHn9yYJRXVeENN1O0AVH87xftTVg2S/+gMY"
            + "4T1+MDJ6LMGGtM8z2siOAQDYtCedx5SRLE1uc/NmiuA3wAKOXoQ4GOVT7qKPVlFc+a1Xw/amQB6+"
            + "Z72mnd0Jxtbrx1ZPKEy6riECQQD5sRrIGPOxOlj7dwXN8TjAmzjBmXPhVzAH0qTDa9zkheywdih7"
            + "gzyBIbybkzHg3o7WEsCoa6L1jFmjH3SvoggJAkEA+NkzimwMVRmMOb95IRaVbFE9HVYlEWkoviX+"
            + "AWtoQuLKO06sA+PMusC6nsVaOrs5cwnkDl5oGRBdurLnFsIaVQJBAKXP76oLr+D0j1zGqIsfcoGt"
            + "Up52ChcNsC6xuoqv2WvEZ+6zOKT0/LwALGDf+dKL2SzeHSR6/ie0AV6rJ/DuWAkCQEzm6pujamg+"
            + "HeyWtHs6PHEd5fIT9h6cTd/9Y7LSm4TYC0EqvdPx6Qnpl4KLYKf3xfg8HUMnf6oHOcABm5KX54UC"
            + "QQDae9w+x5K310TubHYpN1qbocZddqNJD8d+zW54QioCYK5jDv86CwO2ZsZG64jlM8iUVOa3RmUA"
            + "XQoeSLJp8oAl";

    /**
     * 公钥还原：
     * 1: 对公钥字符串Base64解密得到byte数组
     * 2：把公钥的byte数组放入规范
     * 3：实例化KeyFactory，
     * 4：从KeyFactory中获得公钥
     * @param public_key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String public_key) throws Exception {
        try {
            Base64 base64 = new Base64();
            byte[] buffer = base64.decode(public_key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 私钥还原：
     * 1: 对私钥字符串Base64解密得到byte数组
     * 2：把私钥的byte数组放入规范
     * 3：实例化KeyFactory，
     * 4：从KeyFactory中获得私钥
     * @param private_key
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String private_key) throws Exception {
        try {
            Base64 base64 = new Base64();
            byte[] buffer = base64.decode(private_key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 对内容进行加密
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) throws Exception {
        String base64String = null;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, JFwkRSAUtils.getPublicKey(JFwkRSAUtils.public_key));

            int inputLen = content.getBytes("utf-8").length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密  
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > JFwkRSAUtils.MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(content.getBytes(), offSet, JFwkRSAUtils.MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(content.getBytes(), offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * JFwkRSAUtils.MAX_ENCRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            Base64 base64 = new Base64();
            base64String = base64.encode(decryptedData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
        return base64String;//为最终加密的手机号  		
    }

    /**
     * 对内容进行解密
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt(String content) throws Exception {
        Base64 base64 = new Base64();
        byte[] buffer = base64.decode(content);
        String base64String = null;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, JFwkRSAUtils.getPrivateKey(JFwkRSAUtils.private_key));
            int inputLen = buffer.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密  
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > JFwkRSAUtils.MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(buffer, offSet, JFwkRSAUtils.MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(buffer, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * JFwkRSAUtils.MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            base64String = new String(decryptedData, "utf-8");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
        return base64String; //最后获得的手机号 		
    }

    public static void main(String[] args) throws Exception {
//		String desStr = JFwkRSAUtils.decrypt(encryptstr);
//		System.out.println(desStr);
//		for(int i=0;i<20;i++) {
//			Random random = new Random();
//			int num = random.nextInt(100);
//	    	long time = Calendar.getInstance().getTimeInMillis();
//	    	String phone = Constants.phoneNum[StringUtils.getInteger((time+num)%Constants.phoneNum.length)]+StringUtils.getString(time+num).substring(StringUtils.getString(time).length()-8, StringUtils.getString(time).length());
//	    	String encryptstr = UriEncoder.encode(JFwkRSAUtils.encrypt(phone));
//	    	String wkNum = UriEncoder.encode(JFwkRSAUtils.encrypt(Constants.JFWKAPISERVICECODE));
//			System.out.println(encryptstr);
//			String mobile = encryptstr;
//			String certNo = UriEncoder.encode(JFwkRSAUtils.encrypt("430522198909129090"));
//			String url = "http://106.14.183.68:8100/open/v1/jfwkApi/getJFwkApi";
////			String url = "http://172.17.24.109:8100/open/v1/jfwkApi/getJFwkApi";
//			url += "?channelNum=&wkNum="+wkNum+"&serialNumber=&mobile="+mobile+"&certNo="+certNo;
//			String result = HttpUtils.sendGet(url);
//			System.out.println(result);
//		}
    }
}
