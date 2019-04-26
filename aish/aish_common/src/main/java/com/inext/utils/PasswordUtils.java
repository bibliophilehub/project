package com.inext.utils;

public class PasswordUtils {

    private static String PRE = "SYJ";

    public static String getPassword() {
        return PRE + String.valueOf(Math.random()).substring(2).substring(0, 3);// 3位固定长度
    }
}
