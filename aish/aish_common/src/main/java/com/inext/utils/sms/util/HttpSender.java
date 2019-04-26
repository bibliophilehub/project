//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.inext.utils.sms.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpSender {
    private static String[] HexCode = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public HttpSender() {
    }

    public static String send(String uri, String account, String pswd, String mobile, String msg, boolean needstatus, String product, String extno, String resptype, boolean encrypt) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod();

        String var16;
        try {
            method.setURI(new URI(uri, false));
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String ts = df.format(new Date(System.currentTimeMillis()));
            method.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
            method.getParams().setParameter("http.protocol.content-charset", "UTF-8");
            method.setRequestBody(new NameValuePair[]{new NameValuePair("account", account), new NameValuePair("mobile", mobile), new NameValuePair("msg", URLEncoder.encode(msg, "UTF-8")), new NameValuePair("needstatus", String.valueOf(needstatus)), new NameValuePair("product", product), new NameValuePair("extno", extno), new NameValuePair("resptype", resptype)});
            if (encrypt) {
                method.addParameter(new NameValuePair("ts", ts));
                method.addParameter(new NameValuePair("pswd", getMd5Str(account + pswd + ts)));
            } else {
                method.addParameter(new NameValuePair("pswd", pswd));
            }

            int result = client.executeMethod(method);
            if (result != 200) {
                throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
            }

            var16 = new String(method.getResponseBody(), "UTF-8");
        } finally {
            method.releaseConnection();
        }

        return var16;
    }

    /**
     * 催收提醒通道
     * @param uri
     * @param action
     * @param account
     * @param password
     * @param mobile
     * @param msg
     * @param extno
     * @param resptype
     * @return
     * @throws Exception
     */
    public static String postSend(
            String uri,
            String action,
            String account,
            String password,
            String mobile,
            String msg,
            String extno,
            String resptype
    ) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod();

        String var16;
        try {
            method.setURI(new URI(uri, false));
            method.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
            method.getParams().setParameter("http.protocol.content-charset", "UTF-8");
            method.setRequestBody(
                    new NameValuePair[]{
                            new NameValuePair("action", action),
                            new NameValuePair("account", account),
                            new NameValuePair("password", password),
                            new NameValuePair("mobile", mobile),
                            //new NameValuePair("content", URLEncoder.encode(msg, "UTF-8")),
                            new NameValuePair("content", msg),
                            new NameValuePair("extno", extno),
                            new NameValuePair("rt", resptype)
                    });

            int result = client.executeMethod(method);
            if (result != 200) {
                throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
            }

            var16 = new String(method.getResponseBody(), "UTF-8");
        } finally {
            method.releaseConnection();
        }

        return var16;
    }

    public static String getMd5Str(String password) {
        try {
            return getMd5Str(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException var2) {
            return getMd5Str(password.getBytes());
        }
    }

    private static String getMd5Str(byte[] data) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var3) {
            throw new RuntimeException(var3.getMessage());
        }

        md.update(data);
        return byteArrayToHexString(md.digest());
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = b + 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return HexCode[d1] + HexCode[d2];
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer result = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            result = result.append(byteToHexString(b[i]));
        }

        return result.toString();
    }
}
