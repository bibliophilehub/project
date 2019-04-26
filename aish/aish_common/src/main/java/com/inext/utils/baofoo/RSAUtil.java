package com.inext.utils.baofoo;

import org.apache.log4j.Logger;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * RSA签名公共类
 *
 * @author shmily
 */
public class RSAUtil {

    private static Logger log = Logger.getLogger(RSAUtil.class);
    private static RSAUtil instance;

    private RSAUtil() {

    }

    public static RSAUtil getInstance() {
        if (null == instance)
            return new RSAUtil();
        return instance;
    }


    /**
     * 签名处理
     *
     * @param prikeyvalue：私钥文件
     * @param sign_str：签名源内容
     * @return
     */
    public static String sign(String prikeyvalue, String sign_str) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64
                    .getBytesBASE64(prikeyvalue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            // 用私钥对信息生成数字签名
            java.security.Signature signet = java.security.Signature
                    .getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(sign_str.getBytes("UTF-8"));
            byte[] signed = signet.sign(); // 对信息的数字签名
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
        } catch (Exception e) {
            log.error("签名失败," + e.getMessage());
        }
        return null;
    }

    /**
     * 签名验证
     *
     * @param pubkeyvalue：公钥
     * @param oid_str：源串
     * @param signed_str：签名结果串
     * @return
     */
    public static boolean checksign(String pubkeyvalue, String oid_str,
                                    String signed_str) {
        try {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64
                    .getBytesBASE64(pubkeyvalue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            byte[] signed = Base64.getBytesBASE64(signed_str);// 这是SignatureData输出的数字签名
            java.security.Signature signetcheck = java.security.Signature
                    .getInstance("MD5withRSA");
            signetcheck.initVerify(pubKey);
            signetcheck.update(oid_str.getBytes("UTF-8"));
            return signetcheck.verify(signed);
        } catch (Exception e) {
            log.error("签名验证异常," + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        // 商户（RSA）私钥 TODO 强烈建议将私钥
        String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMhKNA1Ws0H6PrZ8t1lQxhQjERj0hYf8QWBlF2DtlMajYU52WsiGIvid6iQQhJGc+aPNTf3MfWCWSHk2XRIYRpjoVPQ8Oz8sLF8j3pT3I2h2gDRNvO2xqX+x+jyFDMnAXm4uMyBYS9wabuhUchF5JkHT1A3rZZFYapPqMTj/zeEFAgMBAAECgYB+uPwwCFAIiYVOPqBe4U1CBmHV8TffLwpKLAvbptX/y/VQCHAt+Th9JqSyxsSpwLDuI4KZ9tzI1KzsDCpcvYFEMuoPNgwjZBFBsmTdXD+nxUTKVbTII6kITyzMMWDBnF8LxAicMKpYcRKaVOULCg/AHPGV32Efd4pH8cyJGcJ6TQJBAP+7+YygfcJLvxI9kk/2Se+dI//mX6WVh1V0RFgSl0cWry+xq9xTQofy0wU++TiXkA05aCJbwY0EjyodUOcpHkMCQQDIf3r3WVpW4Fx6t6B2geew4mllckFEHHDf0pXE5GWymccQHHxo6knFrzZ8F/97XwAIGTabNBXQiWd9G1DfEyMXAkEAow/84wpCpe0efEb+UDY+lqagGb+PJUne7UIhgfb4tr9kHQkxCF+egIj4vNOWndsmYwhDugS/uWc60iO3Pm4deQJAC3qA57hN27tsj/oDTcWSJiZQMmagJe4a6DV+LY+F4vu60clPthHzt0WYsPIOxllh/xSyc6A/v3ieXCM8Ngk6cQJBAJiX6nzlyLyHrHQ0jIdQ97bYtJTqh0ZC6bZ3PShCj3we/Cu+5v6L5Rmwx0s+OJ84OnWIopuuc5QwmOT53VRIntE=";
        // 银通支付（RSA）公钥
        String RSA_YT_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDISjQNVrNB+j62fLdZUMYUIxEY9IWH/EFgZRdg7ZTGo2FOdlrIhiL4neokEISRnPmjzU39zH1glkh5Nl0SGEaY6FT0PDs/LCxfI96U9yNodoA0Tbztsal/sfo8hQzJwF5uLjMgWEvcGm7oVHIReSZB09QN62WRWGqT6jE4/83hBQIDAQAB";

        // RSAUtil.getInstance().generateKeyPair("D:\\CertFiles\\inpour\\",
        // "ll_yt");
        String sign = RSAUtil.sign(RSA_PRIVATE, "busi_partner=101001&dt_order=20130521175800&money_order=12.10&name_goods=%E5%95%86%E5%93%81%E5%90%8D%E7%A7%B0&notify_url=http%3A%2F%2Fwww.baidu.com&no_order=20130521175800&oid_partner=201103171000000000&sign_type=RSA");

        System.out.println(sign);
        System.out.println(RSAUtil.checksign(RSA_YT_PUBLIC, "busi_partner=101001&dt_order=20130521175800&money_order=12.10&name_goods=%E5%95%86%E5%93%81%E5%90%8D%E7%A7%B0&notify_url=http%3A%2F%2Fwww.baidu.com&no_order=20130521175800&oid_partner=201103171000000000&sign_type=RSA", sign));
    }
}
