package com.inext.utils.partner.D360;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class D360Client {

	Logger logger = LoggerFactory.getLogger(D360Client.class);
	private String appId = "167";
	private String encoding = "UTF-8";
//	private String notifyUrl = "http://wlj.xdapi-pt.360.cn/openapi/proxy/hdo";
	private String notifyUrl = "http://demo.t.360.cn/xdpt/openapi/proxy/hdo";
	private String daiPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0gXqDS0UHfrX47hxY5U4Q1Bh3egTBSbwhU+Jin/pflwVIgigYVSID9FOZrV1oW2Bo5kgOhj/V1AYldHLYTcpTvTy6iYRny5a6lIhLzJy7uGadck/IAfWRK1OQSq+wNx2RjeIA9Yfz7tARt//bnldx+HyXxExa+PRi8fT5r/S6xwIDAQAB";
	private String myPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALfDqngDpOTk7g87miWQ3FAa0XoBIQcLFgTtk95C8HSiT52lzjlJhUmnhIm9Xe2dhN+ndYwi3tdGUkBaQ75/8nZlMRX6zqkF3zEOcKYvFcC55PZlJEm/diQTrPuAKUymS3gngF6PGZZUZqq9rvOM/9ztgSI+UyinIFxY3cEmUJnnAgMBAAECgYEAmyCWG1IYNRH81OcfrW6oTQcWpsOlOejl4asdFJe1nQAX7aE2ga3K0u6TOCOf0yMSNCuNU4tsSpaWgLSyU3J3HoShwXN+SlSG8Tg9L0bvu+rwOH0ox6sg9E7vSvZwpCE5Nsix2DqGT1yf3tWd/HJADNpZusK0ubI/IxZA5dd4KAECQQDjCXylKaEGhrwXYfqpHRj1TjJM++oCi6pgw+zbvM8ZHY/pjCaPGBhLSyFL29Kkt/nzXa5N3tfd4rYu8hKzZLLnAkEAzzT9bR2N6qV7322yvcftEoME9mjf4I/IfilBs3R+NY0B1NGx5kjtzHmj0DL1mEgv2l1mXrv2beA1b8YzMd8BAQJASe1g5Pb1NAbTYFnLIR4UbmwT/PVacZHA3NlvWu3UEb2KiuXT+GxEHOBN60GdCX3OqewrQbf6WonkAM1aQYfcqQJAD6TcjWRx1olv9Mp/eDyj9YXW1hPM81Nnu765qybccIaY4MyENfLDoXB9obZ3PGwW0NHEbNqcHGeVIT9Blj5UAQJAHAGXD6kLBo1Lx69vzn3fLfy4NEfssTThD1u/mikC0UjEAMuL1kalaM5ZPElU+hBnfvC3xGpO7Jh2Cpr7N2qVfA==";

	/**
	 * 对des使用360贷款导航ras公钥加密
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String encryptDesKey(String deskey) throws Exception {

		return Base64Utils.encode(RSAUtils.encryptByPublicKey(deskey.getBytes(), daiPublicKey));
	}

	public String encryptBizData(String plainData, String deskey) throws Exception {

		return DES.encrypt(plainData, deskey);
	}

	public String makeSign(TreeMap<String, String> data,String privateKey) throws UnsupportedEncodingException, Exception {

		StringBuffer strbuf = new StringBuffer();
		for (String key : data.keySet()) {
			if("sign".equals(key)) continue;
			strbuf.append("&");
			strbuf.append(key);
			strbuf.append("=");
			strbuf.append(data.get(key));
		}
		String signStr = strbuf.toString();
		if(!"".equals(signStr)){
			signStr = signStr.substring(1);
		}
		System.out.println("signStr ==== " + signStr);
		
		/*
		 * 如果签名报以下错误：

java.security.spec.InvalidKeySpecException: java.security.InvalidKeyException: IOException : algid parse error, not a sequence

则说明rsa私钥的格式不是pksc8格式，需要使用以下命令转换一下：

openssl pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform PEM -nocrypt
		 */

		return RSAUtils.sign(signStr.getBytes(this.encoding), privateKey);
	}

	public String call360(Map<String, Object> bizData, String notifyUrl,String privateKey,String appId) throws Exception {

		String EncFlag = "0"; // 是否加密
		// 通知的业务数据
		/*Map<String, Object> bizData = new HashMap<String, Object>();
		bizData.put("pre_orderid", "6278890717491040256");
		bizData.put("event", "regist");
		bizData.put("user_flag", "1");
		bizData.put("update_time", String.valueOf(System.currentTimeMillis() / 1000));

		// 通知的扩展数据
		Map<String, String> regist = new HashMap<String, String>();
		regist.put("mobile", "18612345678");

		bizData.put("regist", regist);*/

		JSONObject bizJson = new JSONObject(bizData);
		String bizDataStr = bizJson.toString();

		// 通知参数，需要对key做字典序排序算签名
		TreeMap<String, String> requestParam = new TreeMap<String, String>();
		requestParam.put("method", "event.notify");
		requestParam.put("app_id", appId);
		requestParam.put("sign_type", "RSA");
		requestParam.put("version", "1.0");
		requestParam.put("format", "json");
		requestParam.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

		if ("1".equals(EncFlag)) { // 需要加密
			String deskey = "12345678"; // 随机产生一个des密钥
			requestParam.put("biz_enc", "1");
			requestParam.put("des_key", encryptDesKey(deskey));
			requestParam.put("biz_data", encryptBizData(bizDataStr, deskey));
		} else {
			requestParam.put("biz_enc", "0");
			requestParam.put("biz_data", bizDataStr);
		}

		String sign = makeSign(requestParam,privateKey);
		requestParam.put("sign", sign);
		JSONObject jsonObject = new JSONObject(requestParam);
		String postData = jsonObject.toString();
//		System.out.println("postData ==== " + postData);
        logger.info("360贷款导航 postData ====================================:{}",postData);

		StringBuffer response = new StringBuffer();
		try {
			// 需要请求的restful地址
			URL url = new URL(notifyUrl);

			// 打开restful链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 提交模式
			conn.setRequestMethod("POST");// POST GET PUT DELETE

			// 设置访问提交模式，
			conn.setRequestProperty("Content-Type", "application/json");

			conn.setConnectTimeout(10000);// 连接超时 单位毫秒
			conn.setReadTimeout(2000);// 读取超时 单位毫秒

			conn.setDoOutput(true);// 是否输入参数

			OutputStream outputStream = conn.getOutputStream();
			conn.getOutputStream().write(postData.getBytes(this.encoding));
			outputStream.flush();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = responseBuffer.readLine()) != null) {
//				System.out.println(output);
				logger.info("360贷款导航 response =======================================:{}",output);
				response.append(output);
			}
			conn.disconnect();
		} catch (Exception e) {
			System.out.println(e);
		}
		return "";
	}

	public String testNotify() throws Exception {

		String EncFlag = "0"; // 是否加密
		// 通知的业务数据
		Map<String, Object> bizData = new HashMap<String, Object>();
		bizData.put("pre_orderid", "6278890717491040256");
		bizData.put("event", "regist");
		bizData.put("user_flag", "1");
		bizData.put("update_time", String.valueOf(System.currentTimeMillis() / 1000));

		// 通知的扩展数据
		Map<String, String> regist = new HashMap<String, String>();
		regist.put("mobile", "18612345678");

		bizData.put("regist", regist);

		JSONObject bizJson = new JSONObject(bizData);
		String bizDataStr = bizJson.toString();

		// 通知参数，需要对key做字典序排序算签名
		TreeMap<String, String> requestParam = new TreeMap<String, String>();
		requestParam.put("method", "event.notify");
		requestParam.put("app_id", this.appId);
		requestParam.put("sign_type", "RSA");
		requestParam.put("version", "1.0");
		requestParam.put("format", "json");
		requestParam.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

		if ("1".equals(EncFlag)) { // 需要加密
			String deskey = "12345678"; // 随机产生一个des密钥
			requestParam.put("biz_enc", "1");
			requestParam.put("des_key", encryptDesKey(deskey));
			requestParam.put("biz_data", encryptBizData(bizDataStr, deskey));
		} else {
			requestParam.put("biz_enc", "0");
			requestParam.put("biz_data", bizDataStr);
		}

		String sign = makeSign(requestParam,this.myPrivateKey);
		requestParam.put("sign", sign);
		JSONObject jsonObject = new JSONObject(requestParam);
		String postData = jsonObject.toString();
		System.out.println("postData ==== " + postData);

		StringBuffer response = new StringBuffer();
		try {
			// 需要请求的restful地址
			URL url = new URL(this.notifyUrl);

			// 打开restful链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 提交模式
			conn.setRequestMethod("POST");// POST GET PUT DELETE

			// 设置访问提交模式，
			conn.setRequestProperty("Content-Type", "application/json");

			conn.setConnectTimeout(10000);// 连接超时 单位毫秒
			conn.setReadTimeout(2000);// 读取超时 单位毫秒

			conn.setDoOutput(true);// 是否输入参数

			OutputStream outputStream = conn.getOutputStream();
			conn.getOutputStream().write(postData.getBytes(this.encoding));
			outputStream.flush();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = responseBuffer.readLine()) != null) {
				System.out.println(output);
				response.append(output);
			}
			conn.disconnect();
		} catch (Exception e) {
			System.out.println(e);
		}
		return "";
	}
	public static void main(String[] args) throws Exception {

		D360Client client = new D360Client();
		String response = client.testNotify();
		System.out.println(response);
	}
}
