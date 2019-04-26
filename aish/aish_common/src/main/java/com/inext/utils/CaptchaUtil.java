package com.inext.utils;

import org.apache.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.Date;


/**
 * 验证码
 *
 * @author gaoyuhai
 * 2016-6-17 下午04:27:18
 */
public class CaptchaUtil {
    private static Logger loger = Logger.getLogger(CaptchaUtil.class);

    /**
     * 获取验证码
     *
     * @return
     */
    public static String getCaptcha() {
        Date date = new Date();
        String passMD5 = "";
        try {
            passMD5 = new MD5codingLowCase().code(String.valueOf(date.getTime()));
        } catch (NoSuchAlgorithmException e) {
            loger.info("-getCaptcha-exception");
        }
        passMD5 = passMD5.substring(16, passMD5.length() - 12);
        return passMD5;
    }

}
