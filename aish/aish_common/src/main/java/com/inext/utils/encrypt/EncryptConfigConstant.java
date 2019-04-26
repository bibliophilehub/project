package com.inext.utils.encrypt;

import java.util.ResourceBundle;

/**
 * 读取公钥、私钥配置文件
 *
 * @author gaoyuhai
 */
public class EncryptConfigConstant {

    public static ResourceBundle resourceBundle;

    public static void initConfig() {
        resourceBundle = ResourceBundle.getBundle("config/encrypt");
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
