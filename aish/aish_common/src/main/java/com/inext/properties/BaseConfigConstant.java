package com.inext.properties;

import java.util.ResourceBundle;

/**
 * 读取公钥、私钥配置文件
 *
 * @author gaoyuhai
 */
public class BaseConfigConstant {

    public static ResourceBundle resourceBundle;

    public static void initConfig() {
        resourceBundle = ResourceBundle.getBundle("config/base");
    }

    public static String getConstant(String key) {
        if (resourceBundle == null) initConfig();
        return resourceBundle.getString(key);

    }

    public static String getConstant(String key, String defaultValue) {
        String value = null;
        try {
            if (resourceBundle == null) initConfig();
            value = resourceBundle.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
        return value;
    }

}
