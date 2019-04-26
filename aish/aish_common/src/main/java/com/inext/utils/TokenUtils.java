package com.inext.utils;

import com.inext.entity.BorrowUser;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @类说明：app自动登录token相关工具类
 * @类 名：TokenUtils.java
 * @作 者：ttj
 * @创建日期：2018年3月21日
 */
public class TokenUtils {
    public static final Integer effSecond = 3 * 24 * 60 * 60;//app保持登录有效天数
    private static final Integer effDay = 3;//app保持登录有效天数
    private static final String password = "0123456789lbsoft";
    private static Logger logger = LoggerFactory.getLogger(TokenUtils.class);
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * @方法说明：token自动生成工具方法 token = des(userId###userAccount###userPassword###登录时间###有效时间);
     * @方法作者：ttj
     * @创建日期：2018年3月21日
     * @返回类型：String
     */
    public static String generateToken(BorrowUser user) {
        String encryptDES = "";
        //待加密明文
        JSONObject json = JSONObject.fromObject(user);
        try {
            encryptDES = RBAESUtil.encrypt(json.toString(), password);
            return encryptDES;
        } catch (Exception e) {
            logger.error("生成密文token出错，错误信息为：" + e.getMessage());
        }
        return encryptDES;
    }

    /**
     * @方法说明：解密token
     * @返回类型：Map<String,String> 具体为：
     * userId:userId;
     * userAccount:userAccount;
     * @方法作者：ttj
     * @创建日期：2018年3月21日
     */
    public static BorrowUser decryptToken(String token) {
        BorrowUser bu = null;
        try {
            if (token != null && StringUtils.isNotEmpty(token)) {
                String mingwen = new String(RBAESUtil.decrypt(StringUtils.trimToEmpty(token), password)).trim();
                bu = (BorrowUser) JSONObject.toBean(JSONObject.fromObject(mingwen), BorrowUser.class);
            }
        } catch (Exception e) {
            logger.error("解密密文token出错，错误信息为：" + e.getMessage());
        }
        return bu;
    }

    /**
     * ttj
     *
     * @param tempToken
     * @return 加密过后的临时令牌
     */
    public static String getTempToken(String tempToken) {
        String encryptDES = "";
        try {
            encryptDES = RBAESUtil.encrypt(tempToken, password);
        } catch (Exception e) {
            logger.error("生成密文tempToken出错，错误信息为：" + e.getMessage());
        }
        return encryptDES;
    }

    /**
     * ttj
     *
     * @param tempToken
     * @return 解密过后的临时令牌
     */
    public static String decryptTempToken(String tempToken) {
        String token = "";
        try {
            if (tempToken != null && StringUtils.isNotEmpty(tempToken)) {
                token = new String(RBAESUtil.decrypt(StringUtils.trimToEmpty(tempToken), password)).trim();
            }
        } catch (Exception e) {
            logger.error("解密密文tempToken出错，错误信息为：" + e.getMessage());
        }
        return token;
    }

    /**
     * token有效时间
     *
     * @param userLogintime 当前登录时间
     * @return
     */
    public static Date getEffTime(Date userLogintime) {
        Date effTime = DateUtil.addDay(userLogintime, effDay);
        return effTime;
    }

    public static void main(String[] args) {
        BorrowUser user = new BorrowUser();
        user.setUserPhone("13735803606");
        System.out.print(generateToken(user));

    }
}
