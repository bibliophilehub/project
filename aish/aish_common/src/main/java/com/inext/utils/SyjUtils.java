package com.inext.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 开发人员：jzhang
 * 创建时间：2017-05-23 上午 9:53
 */
public class SyjUtils {
    /**
     * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700
     **/
    private static final String CHINA_TELECOM_PATTERN = "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";
    /**
     * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1709
     **/
    private static final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
    /**
     * 中国移动号码格式验证
     * 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184
     * ,187,188,147,178,1705
     **/
    private static final String CHINA_MOBILE_PATTERN = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";

    public static void main(String[] args) {
        System.out.print(isChinaMobilePhoneNum("15205061491"));
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 16+任意数
     * 18+任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(16[0-9])|(18[0-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */

    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 验证【电信】手机号码的格式
     *
     * @param str 校验手机字符串
     * @return 返回true, 否则为false
     * @author LinBilin
     */
    public static boolean isChinaTelecomPhoneNum(String str) {

        return str == null || str.trim().equals("") ? false : match(
                CHINA_TELECOM_PATTERN, str);
    }

    /**
     * 验证【联通】手机号码的格式
     *
     * @param str 校验手机字符串
     * @return 返回true, 否则为false
     * @author LinBilin
     */
    public static boolean isChinaUnicomPhoneNum(String str) {

        return str == null || str.trim().equals("") ? false : match(
                CHINA_UNICOM_PATTERN, str);
    }

    /**
     * 验证【移动】手机号码的格式
     *
     * @param str 校验手机字符串
     * @return 返回true, 否则为false
     * @author LinBilin
     */
    public static boolean isChinaMobilePhoneNum(String str) {
        return str == null || str.trim().equals("") ? false : match(
                CHINA_MOBILE_PATTERN, str);
    }


    /**
     * 执行正则表达式
     *
     * @param pat 表达式
     * @param str 待验证字符串
     * @return 返回true, 否则为false
     */
    private static boolean match(String pat, String str) {
        Pattern pattern = Pattern.compile(pat);
        Matcher match = pattern.matcher(str);
        return match.find();
    }
}
