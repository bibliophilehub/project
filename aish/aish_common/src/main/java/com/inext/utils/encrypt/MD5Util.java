package com.inext.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5生成摘要工具
 */
public final class MD5Util {

    public static String build(String content) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        messageDigest.update(content.getBytes());
        byte[] domain = messageDigest.digest();
        StringBuilder md5StrBuff = new StringBuilder();
        // converting domain to String
        for (int i = 0; i < domain.length; i++) {
            if (Integer.toHexString(0xFF & domain[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & domain[i]));
            } else
                md5StrBuff.append(Integer.toHexString(0xFF & domain[i]));
        }
        return md5StrBuff.toString();
    }
}
