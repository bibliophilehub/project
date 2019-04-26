package com.inext.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取config.properties配置
 *
 * @author LTQ
 */
public class PropertiesUtil {

    private static Properties properties = new Properties();

    static {
        InputStream in = PropertiesUtil.class.getResourceAsStream("/config.properties");
        try {
            properties.load(in);
        } catch (IOException e) {

        }
    }

    public static String get(String key) {
        return properties.getProperty(key).trim();
    }

    /**
     * 读取临时状态配置类
     *
     * @param key
     * @return
     */
    public static String getTempStatus(String key) {
        return properties.getProperty(key).trim();
    }

    /**
     * 写入临时状态
     *
     * @param key
     * @param value
     */
    public static void setTempStatus(String key, String value) {
        properties.setProperty(key, value);
    }
}
