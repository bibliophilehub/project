package com.inext.utils;

import org.apache.log4j.Logger;

import java.util.UUID;


/**
 * 随机数
 *
 * @author gaoyuhai
 * 2016-6-17 下午04:27:18
 */
public class RandomUtil {
    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private static Logger loger = Logger.getLogger(RandomUtil.class);

    public static int nextRand(int n) {
        return (1103515245 * n + 12345) & 0x0FFFFFFF;
    }

    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    public static String generateShortUuid(int num) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < num; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    public static void main(String[] args) {
        for (int i = 0; i <= 10; i++) {
            System.out.println(generateShortUuid(5));
        }
    }
}
