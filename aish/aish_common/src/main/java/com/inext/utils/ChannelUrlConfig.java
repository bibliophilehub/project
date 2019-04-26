package com.inext.utils;

import java.util.ResourceBundle;

/**
 * @author admin
 */
public class ChannelUrlConfig {

    public static ResourceBundle resourceBundle;

    public static void initConfig() {
        resourceBundle = ResourceBundle.getBundle("channelUrl");
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

