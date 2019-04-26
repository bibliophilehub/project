package com.inext.utils;

import com.inext.constants.Constants;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

/**
 * des加密解密
 *
 * @author liutq
 */
public class DesEncrypt {

    Key key;

    public DesEncrypt() {
        setKey(Constants.DES_PRIVATE_ENCRYPT_KEY);
    }


    public DesEncrypt(String str) {
        setKey(str);// 生成密匙
    }

    public static void main(String[] args) {
        //密码加密
//        DesEncrypt desEncrypt = new DesEncrypt("20150727");
//        //System.out.println(desEncrypt.encrypt("jingli001"));
////		System.out.println(desEncrypt.encrypt("123456"));//c7vtWVd1Dow=
////		System.out.println(desEncrypt.encrypt("bh8765"));//3mxv4peno9E=
//        System.out.println(desEncrypt.encrypt("123456"));//4ngoTpohF1Q=
//        //	System.out.println(desEncrypt.decrypt("RUIPlZXPq8w="));j
////
//
//        //密码解密
//        DesEncrypt deEncrypt = new DesEncrypt("20150727");
//        String transactionPwdDes = deEncrypt.decrypt("i27iy2m38xw=".replaceAll("\\[j]", "+").replaceAll("\\[d]", "=").replaceAll("\\[x]", "/"));
//        System.out.println(transactionPwdDes);
        DesEncrypt desEncrypt = new DesEncrypt();

        String eStr = desEncrypt.encrypt("284");

        System.err.println("eStr :" + eStr);
        try {
            System.err.println("dStr :" + desEncrypt.decrypt(eStr));
        } catch (Exception e) {
            e.printStackTrace();
        }

//		//i27iy2m38xw  A8F5mKJb+o4=
////		BigDecimal myCommission =new BigDecimal(36);
////		BigDecimal totalCommission = new BigDecimal(10);
////		BigDecimal divide = myCommission.divide(totalCommission, BigDecimal.ROUND_HALF_UP);
//
//		//密码解密
//		DesEncrypt deEncrypt = new DesEncrypt("ABCDEFGHIJKLMNOPQRSTWXYZabcdefghijklmnopqrstwxyz0123456789-_.");
//		String transactionPwdDes = deEncrypt.decrypt("4ngoTpohF1Q=".replaceAll("\\[j]", "+").replaceAll("\\[d]", "=").replaceAll("\\[x]", "/"));
//		System.out.println(transactionPwdDes);
//
//		String pwd = "i27iy2m38xw";
//		DesEncrypt deEncrypt = new DesEncrypt(pwd);
//		String string =deEncrypt.decrypt("4ngoTpohF1Q=".replaceAll("\\[j]", "+").replaceAll("\\[d]", "=").replaceAll("\\[x]", "/"));
//
//		System.out.println(string);
    }

    /**
     * 根据参数生成KEY
     */
    public void setKey(String strKey) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            this.key = keyFactory.generateSecret(new DESKeySpec(strKey
                    .getBytes("UTF8")));
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        }
    }

    /**
     * 加密String明文输入,String密文输出
     */
    public String encrypt(String strMing) {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            byteMing = strMing.getBytes("UTF-8");
            byteMi = this.getEncCode(byteMing);
            strMi = base64en.encode(byteMi);
            // 特殊字符会被认为sql注入攻击被过滤掉 替换掉
            strMi = org.apache.commons.lang3.StringUtils.replace(strMi, "+", "@");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            base64en = null;
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    /**
     * 解密 以String密文输入,String明文输出
     *
     * @param strMi
     * @return
     */
    public String decrypt(String strMi) {
        strMi = org.apache.commons.lang3.StringUtils.replace(strMi, "@", "+");

        BASE64Decoder base64De = new BASE64Decoder();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try {
            byteMi = base64De.decodeBuffer(strMi);
            byteMing = this.getDesCode(byteMi);
            strMing = new String(byteMing, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            base64De = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }

    /**
     * 加密以byte[]明文输入,byte[]密文输出
     *
     * @param byteS
     * @return
     */
    private byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key,
                    SecureRandom.getInstance("SHA1PRNG"));
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     *
     * @param byteD
     * @return
     */
    private byte[] getDesCode(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key,
                    SecureRandom.getInstance("SHA1PRNG"));
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error initializing SqlMap class. Cause: " + e);
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 加密list中的某个值，加密后的值key=e+原来的key
     *
     * @param list
     * @param keys
     */
    @SuppressWarnings("unchecked")
    public void encryptInList(List<Map> list, Object[] keys) {
        for (Map m : list) {
            for (Object key : keys) {
                if (m.containsKey(key)) {
                    Object val = m.get(key);
                    String param = "";
                    if (val instanceof String) {
                        param = (String) val;
                    } else if (val instanceof Integer) {
                        param = String.valueOf((Integer) val);
                    } else if (val instanceof Long) {
                        param = String.valueOf((Long) val);
                    }
                    String encKey = "e" + key;
                    m.put(encKey, encrypt(param));
//					System.out.println("加密前：" + key + "=[" + param + "],加密后："
//							+ encKey + "=[" + m.get(encKey) + "]");
                }
            }
        }
    }
}
