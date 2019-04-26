package com.inext.utils;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AjaxUtil {

    private static Logger loger = Logger.getLogger(AjaxUtil.class);

    /**
     * 输出数据-ajax
     *
     * @param response
     * @param data
     */
    public static void sendData(HttpServletResponse response, String data) {
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(data);
        } catch (IOException ex) {
            loger.info("sendData exception:" + data);
        } finally {
            if (null != printWriter) {
                printWriter.flush();
                printWriter.close();
            }
        }
    }

}
