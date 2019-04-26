package com.inext.utils.baofoo;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

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
    private static WebApplicationContext webApplicationContext;

    //测试环境	/CER_boofoo_real/app_real.properties
    static {
        //正式环境  /CER_boofoo_cutpay/app_test.properties
        webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        InputStream in = PropertiesUtil.class.getResourceAsStream("/config/config.properties");

        try {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            value = value.trim();
        }
//        if (key.startsWith("file.")) {
//            if (webApplicationContext == null) {
//                value = PropertiesUtil.class.getResource(value).getPath();
//            } else {
//                value = webApplicationContext.getServletContext().getRealPath("/WEB-INF/classes/" + value);
//            }
//        }
        return value;
    }
}
