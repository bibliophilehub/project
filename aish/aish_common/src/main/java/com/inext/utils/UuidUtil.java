package com.inext.utils;

import java.util.UUID;

public class UuidUtil {

    public static String getRandomUUID32() {
        String uuid = UUID.randomUUID().toString(); // 获取UUID并转化为String对象
        uuid = uuid.replace("-", "");

        return uuid;
    }

}
