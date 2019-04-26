package com.inext.utils;

import com.squareup.okhttp.*;
import com.squareup.okhttp.Request.Builder;
import okhttp3.FormBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.net.ssl.*;
import java.io.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

public class OkHttpUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType JSON_GBK = MediaType.parse("application/json; charset=gbk");

    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");
    public static final int BUFFER = 1024;
    private static Logger LOG = LoggerFactory.getLogger(OkHttpUtils.class);
    private static OkHttpClient client = new OkHttpClient();
    private static okhttp3.OkHttpClient client3;

    static {
        // 超时配置
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        client.setReadTimeout(120, TimeUnit.SECONDS);

        client3 = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .build();
    }

    /**
     * post方法 htpps协议 自定义mediaType json参数
     *
     * @param url
     * @param json
     * @param mediaType
     * @param charsetName
     * @return
     */
    public static String post(String url, String json, MediaType mediaType, String charsetName) {
        String result = "";
        Response response = null;
        Request request = null;
        try {
            OkHttpClient client = getConnection();

            RequestBody formBody = RequestBody.create(mediaType, json);

            request = new Builder().url(url).post(formBody).build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = new String(response.body().bytes(), charsetName);
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},params:{1},response:{2}", url, json, result);
            LOG.error(msg);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * post方法 htpps协议 json参数
     *
     * @param url
     * @param json
     * @return
     */
    public static String post(String url, String json) {
        String result = "";
        Response response = null;
        Request request = null;
        try {
            OkHttpClient client = getConnection();

            RequestBody formBody = RequestBody.create(JSON, json);

            request = new Builder().url(url).post(formBody).build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},params:{1},response:{2}", url, json, result);
            LOG.error(msg);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * post方法 htpps协议 json参数
     *
     * @param url
     * @param json
     * @return
     */
    public static String post(String url, String json, Builder builder) {
        String result = "";
        Response response = null;
        Request request = null;
        try {
            OkHttpClient client = getConnection();

            RequestBody formBody = RequestBody.create(JSON, json);

            request = builder.url(url).post(formBody).build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},params:{1},response:{2}", url, json, result);
            LOG.error(msg);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * post方法 表单提交
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public static String postForm(String url, Map<String, Object> paramsMap) {
        String result = "";
        okhttp3.Response response = null;
        okhttp3.Request request = null;
        try {

            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                builder = builder.add(entry.getKey(), StringUtils.toString(entry.getValue()));
            }
            FormBody formBody = builder.build();

            request = new okhttp3.Request.Builder().url(url).post(formBody).build();

            response = client3.newCall(request).execute();
            LOG.info(response.code() + "");
            LOG.info(response.message());
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},params:{1},response:{2}", url, paramsMap, result);
            LOG.error(msg);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get方法 Http Basic Authentication
     * @param url
     * @param userName
     * @param password
     * @return
     */
    public static String get(String url,String userName,String password) {
        String result = "";
        Response response = null;
        Request request = null;
        try {
            OkHttpClient client = getConnection();
            client.setConnectTimeout(5,TimeUnit.SECONDS);
            client.setReadTimeout(120,TimeUnit.SECONDS);

            request = new Builder().url(url).get().addHeader("Authorization",Credentials.basic(userName, password)).build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},response:{1}", url, result);
            LOG.error(msg);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get方法 htpps协议
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        String result = null;
        Response response = null;
        Request request = null;
        try {
            OkHttpClient client = getConnection();
            request = new Builder().url(url).get().build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},response:{1}", url, result);
            LOG.error(msg);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 访问https证书验证
     *
     * @return
     * @throws Exception
     */
    public static OkHttpClient getConnection() throws Exception {
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(new KeyManager[0], new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        }}, new SecureRandom());
        client.setSslSocketFactory(ctx.getSocketFactory());
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        return client;
    }

    /**
     * post方法 htpps协议 json参数
     *
     * @param url
     * @param json
     * @return
     */
    public static String postBase64Gzip(String url, String json) {
        String result = "";
        Response response = null;
        Request request = null;
        try {
            OkHttpClient client = getConnection();

            RequestBody formBody = RequestBody.create(JSON, Base64Utils.encode(compress(json.getBytes())));
            Builder builder = new Builder().url(url);
            builder = builder.addHeader("Content-Encoding", "gzip");

            request = builder.post(formBody).build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},params:{1},response:{2}", url, json, result);
            LOG.error(msg);
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] compress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 压缩
        compress(bais, baos);
        byte[] output = baos.toByteArray();
        baos.flush();
        baos.close();
        bais.close();
        return output;
    }

    /**
     * 数据压缩
     *
     * @param is
     * @param os
     * @throws Exception
     */
    public static void compress(InputStream is, OutputStream os) throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = is.read(data, 0, BUFFER)) != -1) {
            gos.write(data, 0, count);
        }
        gos.finish();
        gos.flush();
        gos.close();
    }

    // 测试方法
    public static void main(String[] args) throws IOException {


    }
}
