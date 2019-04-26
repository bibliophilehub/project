package com.inext.utils.sms.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YunXinSmsUtil {

	public static final Logger logger = LoggerFactory.getLogger(YunXinSmsUtil.class);

	public final static Map<String, String> errorMsg = new HashMap<String, String>();

	static {
		errorMsg.put("-1", "提交接口错误");
		errorMsg.put("-3", "用户名或密码错误");
		errorMsg.put("-4", "短信内容和备案的模板不一样");
		errorMsg.put("-5", "签名不正确(格式为: 短信内容......【签名内容】)签名一定要放 在短信最后");
		errorMsg.put("-7", "余额不足");
		errorMsg.put("-8", "通道错误");
		errorMsg.put("-9", "无效号码");
		errorMsg.put("-10", "签名内容不符合长度");
		errorMsg.put("-11", "用户有效期过期");
		errorMsg.put("-12", "黑名单");
		errorMsg.put("-13", "语音验证码的 Amount 参数必须是整形字符串");
		errorMsg.put("-14", "语音验证码的内容只能为数字");
		errorMsg.put("-15", "语音验证码的内容最长为 6 位");
		errorMsg.put("-16", "余额请求过于频繁，5 秒才能取余额一次");
		errorMsg.put("-17", "非法 IP");
		errorMsg.put("-23", "解密失败");
	}

/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://120.55.197.77:1210/Services/MsgSend.asmx/SendMsg";

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userCode", "XLWLCF"));
		nvps.add(new BasicNameValuePair("userPass", "Q41#si!mbK"));
		nvps.add(new BasicNameValuePair("DesNo", "13524208214"));
		nvps.add(new BasicNameValuePair("Msg", "【花小侠】本次验证码为1234"));
		nvps.add(new BasicNameValuePair("Channel", "0"));
		String post = httpPost(url, nvps); // post请求
 
	}*/

	public static String httpPost(String url, List<NameValuePair> params) {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			//httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				result = convertStreamToString(instreams);
				result = getRealResult(result);
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static String httpGet(String url, String params) {
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			if (params != "") {
				url = url + "?" + params;
			}
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				result = convertStreamToString(instreams);
				//System.out.println(result);
				result = getRealResult(result);
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 返回真实的结果code
	 * @param xml
	 * @return
	 */
	public static String getRealResult(String xml) {
		try {
			Document document = DocumentHelper.parseText(xml);
			Element nodeElement = document.getRootElement();
			String result = nodeElement.getStringValue();
			if(result.compareTo("0") > 0) {
				return result;
			} else {
				logger.info("发送云信短信失败：{}, {}", result, errorMsg.get(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
