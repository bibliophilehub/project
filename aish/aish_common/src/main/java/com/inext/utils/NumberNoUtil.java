package com.inext.utils;

public class NumberNoUtil {
    public static String getNo(String title, int id, int num) {
        String no = title;
        String str = id + "";
        for (int i = 0; i < num - str.length(); i++) {
            no = no + "0";
        }
        no += id;
        return no;
    }
}
