package com.inext.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author licheng
 * @version 1.0
 * @Title JSONTest.java
 * @description TODO
 * @time 2017年12月11日 下午3:33:38
 */
public class JSONTest {

    /*public static void main(String[] args) {

        //JSONObject respMap = JSON.parseObject("{\"data\":[{\"phone\":\"15721589364\",\"type\":\"2\",\"deal_time\":\"2017-12-11 15:07:49\",\"amount\":\"100\",\"months\":\"6\",\"rate\":\"1\"}]}");
        JSONObject respMap = JSON.parseObject("{\r\n" +
                "  \"showapi_res_code\": 0,\r\n" +
                "  \"showapi_res_error\": \"\",\r\n" +
                "  \"showapi_res_body\": {\r\n" +
                "    \"code\": \"0\",\r\n" +
                "    \"msg\": \"匹配\",\r\n" +
                "    \"address\": \"湖南省湘西土家族苗族自治州泸溪县\",\r\n" +
                "    \"birthday\": \"1991-06-10\",\r\n" +
                "    \"sex\": \"M\",\r\n" +
                "    \"error\": \"\",\r\n" +
                "    \"ret_code\": \"0\"\r\n" +
                "  }\r\n" +
                "}");
        //JSONArray repBizData = respMap.getJSONArray("data");
        Integer respCode1 = (Integer) respMap.get("showapi_res_code");
        String respCode2 = (String) respMap.get("showapi_res_error");
        JSONObject respBody = (JSONObject) respMap.get("showapi_res_body");
        System.out.println(respCode1);
        System.out.println(respCode2);
        System.out.println(respBody.toString());
        if (respBody != null) {
            String code = (String) respBody.get("code");
            String msg = (String) respBody.get("msg");
            String address = (String) respBody.get("address");
            String birthday = (String) respBody.get("birthday");
            String sex = (String) respBody.get("sex");
            String error = (String) respBody.get("error");
            String ret_code = (String) respBody.get("ret_code");

            System.out.println("code=" + code);
            System.out.println("msg=" + msg);
            System.out.println("address=" + address);
            System.out.println("birthday=" + birthday);
            System.out.println("sex=" + sex);
            System.out.println("error=" + error);
            System.out.println("ret_code=" + ret_code);
        }
    }*/

    public static void main(String[] args) {
        //花小侠app借款协议
        String app_jiekuan_url = "花小侠app借款协议.html";
        //授权扣款委托书
        String auth_koukuan_url = "授权扣款委托书.html";
        //平台服务协议
        String platform_service_url = "平台服务协议.html";

        try {
            System.out.println("花小侠app借款协议:" + URLEncoder.encode(app_jiekuan_url,"UTF-8"));
            System.out.println("授权扣款委托书:" + URLEncoder.encode(auth_koukuan_url,"UTF-8"));
            System.out.println("平台服务协议:" + URLEncoder.encode(platform_service_url,"UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
  