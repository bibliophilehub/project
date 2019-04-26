package com.inext.utils.huichaopay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: AliAppH5Demo
 * @Description: 支付宝跳转demo(需要安装支付宝)
 * @date 2018年9月20日 下午1:55:28
 *
 */
public class AliAppH5Demo {
	// 测试用参数值
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	static String sdate = sdf.format(new Date());// 格式化当前时间
	static String merchantOutOrderNo = sdf.format(new Date());// 订单号，根据实际情况生成
	static String merid = "yft2017082500005";// 分配的商户号
	static String noncestr = "aishtest2";// 随机字符串，内容自定义
	static String orderMoney = "1.00";// 订单金额 单位 元
	static String orderTime = sdate;// 下单时间
	static String sign = "";// 签名 请
	static String key = "gNociwieX1aCSkhvVemcXkaF9KVmkXm8";// 商户号对应的密钥
	static String notifyUrl = "localhost:8084/huichao/callback";// 用于接收回调通知的地址
	static String id = "";// 不参与验签
	// 跳转请求地址
	static String payUrl = "https://alipay.3c-buy.com/api/createOrder";


	public static String getUrl(String deviceType){
		try {
			Map<String, String> paraMap = new HashMap<String, String>();
			merchantOutOrderNo = sdf.format(new Date());
			notifyUrl = "localhost:8084/huichao/callback";
			paraMap.put("merchantOutOrderNo", "AISHTEST" + merchantOutOrderNo);
			paraMap.put("merid", merid);
			paraMap.put("noncestr", noncestr);
			paraMap.put("orderMoney", orderMoney);
			paraMap.put("orderTime", orderTime);
			paraMap.put("notifyUrl", notifyUrl);
			/**
			 * 对参数按照 key=value 的格式，并参照参数名 ASCII 码排序后得到字符串 stringA
			 */
			String stringA = TestUtil.formatUrlMap(paraMap, false, false);
			System.out.println("stringA:" + stringA);
			/**
			 * 在 stringA 最后拼接上 key 得到 stringsignTemp 字符串， 并对 stringsignTemp 进行 MD5 运算，得到
			 * sign 值
			 */
			String stringsignTemp = stringA + "&key=" + key;
			sign = TestUtil.getMD5(stringsignTemp);
			System.out.println("sign:" + sign);

			/**
			 * 对参数按照 key=value 的格式,参照参数名 ASCII 码排序,并对value做utf-8的encode编码后得到字符串 param
			 */
			String param = TestUtil.formatUrlMap(paraMap, true, false);

			//将此URL送至APP前端页面或手机浏览器打开，即可自动调起支付宝(需要安装)发起支付
			String url = URLEncoder.encode(payUrl + "?" + param + "&sign=" + sign + "&id=" + id, "UTF-8");
			if ("1".equals(deviceType)) {
				//安卓处理
				url = "alipays://platformapi/startApp?appId=10000011&url=" + url;
			}
			if ("2".equals(deviceType)) {
				//ios处理
				url = "alipay://platformapi/startApp?appId=10000011&url=" + url;
			}
			System.out.println("url:" + url);
			return url;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("merchantOutOrderNo", "AISHTEST"+merchantOutOrderNo);
		paraMap.put("merid", merid);
		paraMap.put("noncestr", noncestr);
		paraMap.put("orderMoney", orderMoney);
		paraMap.put("orderTime", orderTime);
		paraMap.put("notifyUrl", notifyUrl);
		/**
		 * 对参数按照 key=value 的格式，并参照参数名 ASCII 码排序后得到字符串 stringA
		 */
		String stringA = TestUtil.formatUrlMap(paraMap, false, false);
		System.out.println("stringA:" + stringA);
		/**
		 * 在 stringA 最后拼接上 key 得到 stringsignTemp 字符串， 并对 stringsignTemp 进行 MD5 运算，得到
		 * sign 值
		 */
		String stringsignTemp = stringA + "&key=" + key;
		sign = TestUtil.getMD5(stringsignTemp);
		System.out.println("sign:" + sign);

		/**
		 * 对参数按照 key=value 的格式,参照参数名 ASCII 码排序,并对value做utf-8的encode编码后得到字符串 param
		 */
		String param = TestUtil.formatUrlMap(paraMap, true, false);
		
		//将此URL送至APP前端页面或手机浏览器打开，即可自动调起支付宝(需要安装)发起支付
		String url = payUrl + "?" + param + "&sign=" + sign + "&id=" + id;
		System.out.println("url:");
		System.out.println(url);
	}
}
