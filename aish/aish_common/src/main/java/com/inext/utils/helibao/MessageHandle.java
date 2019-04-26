package com.inext.utils.helibao;

import java.lang.reflect.Field;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import com.inext.utils.baofoo.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inext.utils.helibao.annotation.FieldEncrypt;
import com.inext.utils.helibao.annotation.SignExclude;

/**
 * Created by heli50 on 2018-06-25.
 */
public class MessageHandle {

	private static final Log log = LogFactory.getLog(MessageHandle.class);


	private static final String CERT_PATH = "/project/helibao/helipay.cer";//"/project/helibao_key/helipay.cer";    //合利宝cert
	//private static final String PFX_PATH = "/project/helibao/C1800438880_888666.pfx";//"/project/helibao_key/C1800358240.pfx";        //商户pfx
	private static final String PFX_PATH = "/project/helibao/helibaaopay.pfx";

	//	private static final String CERT_PATH = "d:/project/helibao/helipay.cer";//"/project/helibao_key/helipay.cer";    //合利宝cert
//	private static final String PFX_PATH = "d:/project/helibao/helibaaopay.pfx";//"/project/helibao_key/C1800358240.pfx";        //商户pfx
//
	//private static final String CERT_PATH =PropertiesUtil.get("file.helipay.cer.name");
	//private static final String PFX_PATH =PropertiesUtil.get("file.helipay.pfx.name");
	private static final String PFX_PWD = PropertiesUtil.get("file.helipay.pfx.pwd");    //pfx密码
	//private static final String PFX_PWD = PropertiesUtil.get("hlb.pfx.pwd");    //pfx密码
	private static final String ENCRYPTION_KEY = "encryptionKey";
	private static final String SPLIT = "&";
	private static final String SIGN = "sign";


	/**
	 * 获取map
	 */
	public static Map getReqestMap(Object bean) throws Exception {

		Map retMap = new HashMap();

		boolean isEncrypt = false;
		String aesKey = AES.generateString(16);
		StringBuilder sb = new StringBuilder();

		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String key = field.toString().substring(field.toString().lastIndexOf(".") + 1);
			String value = (String) field.get(bean);
			if (value == null) {
				value = "";
			}
			//查看是否有需要加密字段的注解,有则加密
			//这部分是将需要加密的字段先进行加密
			if (field.isAnnotationPresent(FieldEncrypt.class) && StringUtils.isNotEmpty(value)) {
				isEncrypt = true;
				value = AES.encryptToBase64(value, aesKey);
			}

			//字段没有@SignExclude注解的拼签名串
			//这部分是把需要参与签名的字段拼成一个待签名的字符串
			if (!field.isAnnotationPresent(SignExclude.class)) {
				sb.append(SPLIT);
				sb.append(value);
			}

			retMap.put(key, value);
		}

		//如果有加密的，需要用合利宝的公钥将AES加密的KEY进行加密使用BASE64编码上送
		if (isEncrypt) {
			PublicKey publicKey = RSA.getPublicKeyByCert(CERT_PATH);
			String encrytionKey = RSA.encodeToBase64(aesKey, publicKey, ConfigureEncryptAndDecrypt.KEY_ALGORITHM);
			retMap.put(ENCRYPTION_KEY, encrytionKey);
		}


		log.info("原签名串：" + sb.toString());
		//使用商户的私钥进行签名
		PrivateKey privateKey = RSA.getPrivateKey(PFX_PATH, PFX_PWD);
		String sign = RSA.sign(sb.toString(), privateKey);
		retMap.put(SIGN, sign);
		log.info("签名sign：" + sign);


		return retMap;
	}


	public static boolean checkSign(Object bean) throws Exception {

		boolean flag = false;

		StringBuilder sb = new StringBuilder();

		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		String sign = "";
		for (Field field : fields) {
			field.setAccessible(true);
			String key = field.toString().substring(field.toString().lastIndexOf(".") + 1);
			String value = (String) field.get(bean);
			if (value == null) {
				value = "";
			}

			if (SIGN.equals(key)) {
				sign = value;
			}

			//字段没有@SignExclude注解的拼签名串
			//这部分是把需要参与签名的字段拼成一个待签名的字符串
			if (!field.isAnnotationPresent(SignExclude.class)) {
				sb.append(SPLIT);
				sb.append(value);
			}

		}
		log.info("response验签原签名串：" + sb.toString());

		//使用合利宝的公钥进行验签
		PublicKey publicKey = RSA.getPublicKeyByCert(CERT_PATH);
		flag = RSA.verifySign(sb.toString(), sign, publicKey);
		if (flag) {
			log.info("验签成功");
		} else {
			log.info("验签失败");
		}
		return flag;

	}


	public static void main(String[] args) throws Exception {
		String sb_str = "&QuickPayBindCard&0000&认证成功&C1800000002&126531&YZB_126531_1545014871559&SUCCESS&CMBCHINA&2198&d067a12c592e41899e23f6eb185b29b0&AUTHENTICATION1812171048238JEX&AiPzAewGhQYwPLcQr/L3tNn6oqV+FnmXFfKG6wx/I+IsEEfVvbDaKLs8T8Kadfg/h8ucN10VWpy/NSlCcwDDjU/xs8Wlq/cQ7e8V43HbN2JA77F7YHnhp4v7sY1YitTkYBkejtfLimXutP5jgNV3xNUmOF9H4nSTtflii8peFP3LsK647bXME46jxc9b49NF94PGgcUmPPNmamQ+E/0RA461PkfIGykX3xh8Ght5shX36JCYhoP4ZEg0dhsbj4ZSTv8OTj1ZdeRNRHRDb2hL/yHDbLH9KmtJ5fM9t1cdQfImHEHMMGU00NRsR0jiP5j6xngB+3ik/d2wID6vVt4pDA==";
		PublicKey publicKey = RSA.getPublicKeyByCert(CERT_PATH);
		boolean flag = RSA.verifySign(sb_str, "AiPzAewGhQYwPLcQr/L3tNn6oqV+FnmXFfKG6wx/I+IsEEfVvbDaKLs8T8Kadfg/h8ucN10VWpy/NSlCcwDDjU/xs8Wlq/cQ7e8V43HbN2JA77F7YHnhp4v7sY1YitTkYBkejtfLimXutP5jgNV3xNUmOF9H4nSTtflii8peFP3LsK647bXME46jxc9b49NF94PGgcUmPPNmamQ+E/0RA461PkfIGykX3xh8Ght5shX36JCYhoP4ZEg0dhsbj4ZSTv8OTj1ZdeRNRHRDb2hL/yHDbLH9KmtJ5fM9t1cdQfImHEHMMGU00NRsR0jiP5j6xngB+3ik/d2wID6vVt4pDA==", publicKey);
		if (flag) {
			log.info("验签成功");
		} else {
			log.info("验签失败");
		}
	}

}
