package com.inext.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 轻借联合第三方登录对接demo
 * 1、RSA加密生成DESKey
 * 2、DES加密业务请求报文
 * 3、DES解密业务请求报文
 * 4、RSA报文加签
 * 5、RSA报文解签匹配
 *
 * @author likai
 * @data 2018年8月23日
 */
public class SecureTest2 {

    /*
     * 说明： 用openssl生成的私钥必须要用pkcs8转换下，
     * <pre>
     * 转换命令为：pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform PEM -nocrypt
     */

//	public static  String sjd_publickey2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDrB4PWNANFxsohetLx/LD2ws8f3EGrKV7560YwcaYdXwTG4NjBaHMVxsHxAKPAc6Mtr7n20X+Govtbpx4su+wKQr65K+DHk8lQHBcyN3yZkxAC74nfLGglTyt5nKen/BaYqOVYhbYSg5oazEOZDD13chZu1V9c7WOyvX0upvYZyQIDAQAB";
//	public static String sjd_privatekey2 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOsHg9Y0A0XGyiF60vH8sPbCzx/cQaspXvnrRjBxph1fBMbg2MFocxXGwfEAo8Bzoy2vufbRf4ai+1unHiy77ApCvrkr4MeTyVAcFzI3fJmTEALvid8saCVPK3mcp6f8Fpio5ViFthKDmhrMQ5kMPXdyFm7VX1ztY7K9fS6m9hnJAgMBAAECgYBDXtoZsrsla0BRrIV/PDH16g25Uke4qplBBoIOLOWzEaOq5pT7i9dBbMH3NXm7Fuq1k+O4roDhtSJcOWFa7VtRYOnD+qkcX78sd5+fOf9bNDzWcjZd6dzcGFrCw2uRRAiVlBflXs9HAsVGGeLJuCPPL0ioohhAk8MjApwMl8zEqQJBAP7k/a2efVIoMjGxmJvUWNIPG9Ve9c0wRm4tXGvd7A1UkL+R8ErU5h+/p237/LmOBDhePsIO4Icl1MFVJS5qw58CQQDsDHfCpwkGaW7ukMlc4e8a/y0op+X2R0wSCe1bU9mJSsVtyf+CW9/33iU225tpNDAcQno5Z2XOKpZ5GmS4b+mXAkEA0MtbUcEl/wqWM0a7L8q+BzQBJMNorfABgXb+4g0js7e16nbtUx8acp9X0yw7VPQUWOg6mFP/cDV5FfVdQ6yJGQJAQo+BdbIEKXqxfTP2k+phsgsigMRXZArWuH90HryWKtCau0qqcefWT96kP8PNHu1IY6+bJ3SwkGIR7DQ1Eoz8IQJAV4YsnEUo98hyInfacmQH2ZYQFI2ZedljNpCknIRerbaGS5oezvXon1/RCJ0Kt8PSQOEFx+0COPaBIPAEztNjOA==";

    public static String sjd_publickey2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDrB4PWNANFxsohetLx/LD2ws8f3EGrKV7560YwcaYdXwTG4NjBaHMVxsHxAKPAc6Mtr7n20X+Govtbpx4su+wKQr65K+DHk8lQHBcyN3yZkxAC74nfLGglTyt5nKen/BaYqOVYhbYSg5oazEOZDD13chZu1V9c7WOyvX0upvYZyQIDAQAB";
    public static String sjd_privatekey2 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOsHg9Y0A0XGyiF60vH8sPbCzx/cQaspXvnrRjBxph1fBMbg2MFocxXGwfEAo8Bzoy2vufbRf4ai+1unHiy77ApCvrkr4MeTyVAcFzI3fJmTEALvid8saCVPK3mcp6f8Fpio5ViFthKDmhrMQ5kMPXdyFm7VX1ztY7K9fS6m9hnJAgMBAAECgYBDXtoZsrsla0BRrIV/PDH16g25Uke4qplBBoIOLOWzEaOq5pT7i9dBbMH3NXm7Fuq1k+O4roDhtSJcOWFa7VtRYOnD+qkcX78sd5+fOf9bNDzWcjZd6dzcGFrCw2uRRAiVlBflXs9HAsVGGeLJuCPPL0ioohhAk8MjApwMl8zEqQJBAP7k/a2efVIoMjGxmJvUWNIPG9Ve9c0wRm4tXGvd7A1UkL+R8ErU5h+/p237/LmOBDhePsIO4Icl1MFVJS5qw58CQQDsDHfCpwkGaW7ukMlc4e8a/y0op+X2R0wSCe1bU9mJSsVtyf+CW9/33iU225tpNDAcQno5Z2XOKpZ5GmS4b+mXAkEA0MtbUcEl/wqWM0a7L8q+BzQBJMNorfABgXb+4g0js7e16nbtUx8acp9X0yw7VPQUWOg6mFP/cDV5FfVdQ6yJGQJAQo+BdbIEKXqxfTP2k+phsgsigMRXZArWuH90HryWKtCau0qqcefWT96kP8PNHu1IY6+bJ3SwkGIR7DQ1Eoz8IQJAV4YsnEUo98hyInfacmQH2ZYQFI2ZedljNpCknIRerbaGS5oezvXon1/RCJ0Kt8PSQOEFx+0COPaBIPAEztNjOA==";

    public static String qj_publickey2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqdtJyNcKB4aAJOT2treVNaOfAMQR/IsgDTJMNVYKNIQiogGHV9k09fmGvlC0XPmkSs/yMbGdXI3y0eJsRPFMqjPRzFMsvTmuR+eyiSY5SJ6FjURcSPPX1/X4nbBZTo4nwzqg4EBLD9WoNwgRcO+ZjeVfA/MdwaxyXpu0a7bdEAQIDAQAB";

    public static String qj_privatekey2 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKp20nI1woHhoAk5Pa2t5U1o58AxBH8iyANMkw1Vgo0hCKiAYdX2TT1+Ya+ULRc+aRKz/IxsZ1cjfLR4mxE8UyqM9HMUyy9Oa5H57KJJjlInoWNRFxI89fX9fidsFlOjifDOqDgQEsP1ag3CBFw75mN5V8D8x3BrHJem7Rrtt0QBAgMBAAECgYBH8ytUhZh51dAE1KJSAoo8qEDZgFcTioy9x75x7FvUUdMjvDyPYQytHgEBaRu3KeKTwGCirMbZouLqEdXx6nJ3XNHhwsnDDoxWtEGIZ6YEHHAx97t9iPNUQE7Nnho9g6ymIERdSrmjRs6dV64rErFi1ENJ+F2Xe60oVppTRqDCAQJBANnxJlp0xF+Yhj+yn3a6T3RoacfRABrNKS5/8/IwTH7rwV5VNNQ8yrckZ/IG5sqiAPtW3J3Qk9wlCjaCGsnaD7ECQQDIOzqepv1M5LqdZ9ohJ6ySpMSsvgRST+rQtF5otHIIvds2s2TSh1jDzEzkTJQRLpEet6F2rbb93i0jZTCWlV1RAkAydflRXFPTZnc+7FaroLug0kQbtbJ2giYU1B9hZflwMZnWA5h1w5WUDePbT+nD5PqhNx1hlNeHUzQoJruZHNohAkAumgJhrHHltJJuUh5lewtiMfEIV01ALxNysZgLfHvR5wYfLA96JEAXifQXam9HtHPYF1r/+RxO56fPxhxR37+RAkEAyt2wRz3hX2CB/EGJUarD3PqsKfv0QJM+hgnjYsnASNLvQ2/2vpnA8PtOjh3JYt3zuNm9SEVpjW7IqCoZ7KRloQ==";


    /**
     * 有轻借分配的商户编码
     */
    public static String merchantId = "201708248210";
    String qj_privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALsunnzzUUPdX0fjrgDpFuY7n0GIsPrjiAJOKzfTRrBqQIpaxwuZ0vF/iEShwSv4hA+2xjYPHy6zspU8yL7/FbzMl2LOrRPV01PkQNIC8MfzJzsFGsm2urNTmPyXr3QiRfG1l1Gj1ilg5+SJ6gaNvffGcxNrS3OSP3Af3mg7MOAJAgMBAAECgYAkQjCTNbLdOYmWbGUtafl0mcIMuMTHpMGY6Ll4c+WykLJ12oXKGn8BJtonqUm5EcfLH3WfzSgWLNsAC6k7TkBQX+ZmjRxgF798UZCojTqRQP7D70w/kcyYzuuiVxzPYT5EWjI8JXQCLoNXZzQl4ftdDwfVfCX9RbRcnEP8qYiGzQJBAPQDFXujLlvFGv3yTVd+5denDS3UiXhKrZiIey/GsxRnLnwF78IyTsnebj1NlUYgHk7IfihDU6hsT0Qhfw8i7Z8CQQDEYMpcHVTttnYFHrQkMWeXVg4PFq7zerfNFVwvhg3780JB9wyrK43pGYP13ldiffy/vV3oeryGMdvwwifBS4FXAkBln3ss+LLv6VDqfiUT0Xerrpjgg2mVPZS6t+yF4zzlvGsxn89Qk18Y15gV09/dSrcUnI9d+Vw4ApOojkK6bROFAkBvun4HsKyMXt6BBToKyY0dvllbXlWFs9F1PmznoQa9zWZqL4wmqy4fbGP29FyVbgbqdGxEg+hVFXMEZQLHaBUbAkEAxXEdM3Q57OgfJ57ZtCGnOI9f0ly9rkYEFS2bgNQhr1lsojL/U2BiB/W5H+i4zlGPWPAY94kYEkErjSBCfTGu2w==";

    public static void main(String[] args) throws Exception {
        SecureTest2 test = new SecureTest2();
        String keyword = "qianlong";
//		// 请求示例
        test.requestQL(keyword);
//		// 响应示例
        test.responseQL("4d+cyjmbt2awDq7gh+C5QalKt+lHj3y7n7VJoU/K7zXnq1joRoQjlPPAnlbbLDgID2JGOojoub7ERA6mQSknFAkKZnjkihgSz6g7kXVWyZ20kr+r7/GIhXstE/rfNqa0zW8AAuVe84HGS/qaQXTUKwHzX3+i/YglV8AEZVWHeWA=");

    }

    /**
     * 生成DesKey
     *
     * @param str       需要加密的内容（此文本内容可随便定义）
     * @param publicKey 手机贷分配的公钥
     * @return
     * @author likai
     * @data 2018年8月23日
     * @Version V1.0
     */
    public String generateDesKey(String str, String publicKey) throws Exception {
        return RSA.encryptByPublicKey(str, publicKey);
    }

    /**
     * 报文加密
     *
     * @param desKey
     * @param content 需要加密的报文
     * @return
     * @author likai
     * @data 2018年8月23日
     * @Version V1.0
     */
    public String encode(String desKey, String content) {
        DES crypt = new DES(desKey);
        String cryptStr = "";
        try {
            cryptStr = crypt.encrypt(content);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("加密后的字符串：" + cryptStr);
        return cryptStr;
    }

    /**
     * 报文签名
     *
     * @param content    加密后的报文内容
     * @param privateKey 私钥
     * @return
     * @author likai
     * @data 2018年8月23日
     * @Version V1.0
     */
    public String sign(String content, String privateKey) throws Exception {
        String sign = RSA.sign(content, privateKey);
        System.out.println("报文数据签名：" + sign);
        return sign;
    }

    /**
     * 轻借请求第三方报文加签加密示例
     *
     * @author likai
     * @data 2018年8月23日
     * @Version V1.0
     */
    public void requestQL(String keyword) throws Exception {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("userPhone", "15300001111");
        String reqData = "";//JSON.toJSONString(dataMap);

//		String desKey = generateDesKey(keyword, QL_PUBLICKEY);
//		String encodeContent = encode(keyword, reqData);
//		String sign = sign(encodeContent, qj_privatekey2);
//
//		System.out.println("encodeContent="+encodeContent);
//		System.out.println("sign="+sign);


        String desKey = EncryptUtil.generateDesKey(keyword, sjd_publickey2);
        String encodeContent = EncryptUtil.encode(keyword, reqData);
        String sign = EncryptUtil.sign(encodeContent, qj_privatekey2);
        String token = UUID.randomUUID().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("merchantId", merchantId);
        params.put("desKey", desKey);
        params.put("sign", sign);
        params.put("reqData", encodeContent);

        for (Map.Entry<String, String> entry : params.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());


        }
    }

    /**
     * 第三方服务响应报文解析示例
     *
     * @author likai
     * @data 2018年8月23日
     * @Version V1.0
     */
    public void responseQL(String keyword) throws Exception {
        Map<String, String> responseData = simulationQianLongResponse();
        keyword = RSA.decryptByPrivateKey(keyword, sjd_privatekey2);
        System.out.println(keyword);
        String data = responseData.get("data");
        boolean bool = RSA.verify(data, responseData.get("sign"), qj_publickey2);
        if (!bool) {
            System.out.println("验签失败！");
            return;
        }
        //desKey保持和请求时一致
        DES newDes = new DES(keyword);
        String decryptData = "";
        try {
            decryptData = newDes.decrypt(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解密之后的data报文为：" + decryptData);
    }

    /**
     * 模拟生成第三方服务响应报文
     *
     * @return
     * @author likai
     * @data 2018年8月23日
     * @Version V1.0
     */
    public Map<String, String> simulationQianLongResponse() {
        Map<String, String> params = new HashMap<String, String>();
        String data = "iCajnd7Dkuf3VN4jVnUOd5fkAWAkJedtpPDZzP0wIzg=";// 使用des加密后的报文，desKey保持和请求时生成的key一致，注意最后要多用base64编码
        String sign = "HFnY7Cl+7WvBrnrgyeNdUrU/5PSwBcqVc8mT/OMttGueyLh1GUXp02WFkt6RCoor0Osbma3mAX3RlKBWipY2jmJ9Vu6rBaScBQPBR79NQg9PbpwNmMHUXF0KCSBusoqAEtWPiPZ6eLcLMcqevgLOG05t12FrUnm+0KWynn/xAKI=";// 使用轻借私钥对des加密后的data报文进行签名，注意最后要多用base64编码
        params.put("status", "1");
        params.put("error", "00000000");
        params.put("msg", "申请成功！");
        params.put("sign", sign);
        params.put("data", data);
        return params;
    }

    public void responseQL2(String keyword) throws Exception {
        Map<String, String> responseData = simulationQianLongResponse();
        keyword = RSA.decryptByPrivateKey(keyword, sjd_privatekey2);
        System.out.println(keyword);
        String data = responseData.get("data");
        boolean bool = RSA.verify(data, responseData.get("sign"), qj_publickey2);
        if (!bool) {
            System.out.println("验签失败！");
            return;
        }
        //desKey保持和请求时一致
        DES newDes = new DES(keyword);
        String decryptData = "";
        try {
            decryptData = newDes.decrypt(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解密之后的data报文为：" + decryptData);
    }

    public void requestQL2(String keyword) throws Exception {
        String channel = "59a5017ab684";
        String merchantId = "1025886";

        String sjd_publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDrB4PWNANFxsohetLx/LD2ws8f3EGrKV7560YwcaYdXwTG4NjBaHMVxsHxAKPAc6Mtr7n20X+Govtbpx4su+wKQr65K+DHk8lQHBcyN3yZkxAC74nfLGglTyt5nKen/BaYqOVYhbYSg5oazEOZDD13chZu1V9c7WOyvX0upvYZyQIDAQAB";
        String sjd_privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOsHg9Y0A0XGyiF60vH8sPbCzx/cQaspXvnrRjBxph1fBMbg2MFocxXGwfEAo8Bzoy2vufbRf4ai+1unHiy77ApCvrkr4MeTyVAcFzI3fJmTEALvid8saCVPK3mcp6f8Fpio5ViFthKDmhrMQ5kMPXdyFm7VX1ztY7K9fS6m9hnJAgMBAAECgYBDXtoZsrsla0BRrIV/PDH16g25Uke4qplBBoIOLOWzEaOq5pT7i9dBbMH3NXm7Fuq1k+O4roDhtSJcOWFa7VtRYOnD+qkcX78sd5+fOf9bNDzWcjZd6dzcGFrCw2uRRAiVlBflXs9HAsVGGeLJuCPPL0ioohhAk8MjApwMl8zEqQJBAP7k/a2efVIoMjGxmJvUWNIPG9Ve9c0wRm4tXGvd7A1UkL+R8ErU5h+/p237/LmOBDhePsIO4Icl1MFVJS5qw58CQQDsDHfCpwkGaW7ukMlc4e8a/y0op+X2R0wSCe1bU9mJSsVtyf+CW9/33iU225tpNDAcQno5Z2XOKpZ5GmS4b+mXAkEA0MtbUcEl/wqWM0a7L8q+BzQBJMNorfABgXb+4g0js7e16nbtUx8acp9X0yw7VPQUWOg6mFP/cDV5FfVdQ6yJGQJAQo+BdbIEKXqxfTP2k+phsgsigMRXZArWuH90HryWKtCau0qqcefWT96kP8PNHu1IY6+bJ3SwkGIR7DQ1Eoz8IQJAV4YsnEUo98hyInfacmQH2ZYQFI2ZedljNpCknIRerbaGS5oezvXon1/RCJ0Kt8PSQOEFx+0COPaBIPAEztNjOA==";
        //		String sjd_publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGjlZwLQmGfCQoX+6O667kgvGzjapsQlC3JOujwcJcitfP/DHahIOD0Qsa46R90obF4k8kEGQAOgjtGTXpbUOKJfbhDbm5AzQxjmQwhGAbeZgot0T7cw71knWelLI06r6oeGIkga4+KfXDBMLW3qsdvPbCbts2vItfYM6Hvj/0SwIDAQAB";

//		String qj_publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7Lp5881FD3V9H464A6RbmO59BiLD644gCTis300awakCKWscLmdLxf4hEocEr+IQPtsY2Dx8us7KVPMi+/xW8zJdizq0T1dNT5EDSAvDH8yc7BRrJtrqzU5j8l690IkXxtZdRo9YpYOfkieoGjb33xnMTa0tzkj9wH95oOzDgCQIDAQAB";
        String qj_publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqdtJyNcKB4aAJOT2treVNaOfAMQR/IsgDTJMNVYKNIQiogGHV9k09fmGvlC0XPmkSs/yMbGdXI3y0eJsRPFMqjPRzFMsvTmuR+eyiSY5SJ6FjURcSPPX1/X4nbBZTo4nwzqg4EBLD9WoNwgRcO+ZjeVfA/MdwaxyXpu0a7bdEAQIDAQAB";

        String qj_privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKp20nI1woHhoAk5Pa2t5U1o58AxBH8iyANMkw1Vgo0hCKiAYdX2TT1+Ya+ULRc+aRKz/IxsZ1cjfLR4mxE8UyqM9HMUyy9Oa5H57KJJjlInoWNRFxI89fX9fidsFlOjifDOqDgQEsP1ag3CBFw75mN5V8D8x3BrHJem7Rrtt0QBAgMBAAECgYBH8ytUhZh51dAE1KJSAoo8qEDZgFcTioy9x75x7FvUUdMjvDyPYQytHgEBaRu3KeKTwGCirMbZouLqEdXx6nJ3XNHhwsnDDoxWtEGIZ6YEHHAx97t9iPNUQE7Nnho9g6ymIERdSrmjRs6dV64rErFi1ENJ+F2Xe60oVppTRqDCAQJBANnxJlp0xF+Yhj+yn3a6T3RoacfRABrNKS5/8/IwTH7rwV5VNNQ8yrckZ/IG5sqiAPtW3J3Qk9wlCjaCGsnaD7ECQQDIOzqepv1M5LqdZ9ohJ6ySpMSsvgRST+rQtF5otHIIvds2s2TSh1jDzEzkTJQRLpEet6F2rbb93i0jZTCWlV1RAkAydflRXFPTZnc+7FaroLug0kQbtbJ2giYU1B9hZflwMZnWA5h1w5WUDePbT+nD5PqhNx1hlNeHUzQoJruZHNohAkAumgJhrHHltJJuUh5lewtiMfEIV01ALxNysZgLfHvR5wYfLA96JEAXifQXam9HtHPYF1r/+RxO56fPxhxR37+RAkEAyt2wRz3hX2CB/EGJUarD3PqsKfv0QJM+hgnjYsnASNLvQ2/2vpnA8PtOjh3JYt3zuNm9SEVpjW7IqCoZ7KRloQ==";

//		String qj_privatekey  = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALsunnzzUUPdX0fjrgDpFuY7n0GIsPrjiAJOKzfTRrBqQIpaxwuZ0vF/iEShwSv4hA+2xjYPHy6zspU8yL7/FbzMl2LOrRPV01PkQNIC8MfzJzsFGsm2urNTmPyXr3QiRfG1l1Gj1ilg5+SJ6gaNvffGcxNrS3OSP3Af3mg7MOAJAgMBAAECgYAkQjCTNbLdOYmWbGUtafl0mcIMuMTHpMGY6Ll4c+WykLJ12oXKGn8BJtonqUm5EcfLH3WfzSgWLNsAC6k7TkBQX+ZmjRxgF798UZCojTqRQP7D70w/kcyYzuuiVxzPYT5EWjI8JXQCLoNXZzQl4ftdDwfVfCX9RbRcnEP8qYiGzQJBAPQDFXujLlvFGv3yTVd+5denDS3UiXhKrZiIey/GsxRnLnwF78IyTsnebj1NlUYgHk7IfihDU6hsT0Qhfw8i7Z8CQQDEYMpcHVTttnYFHrQkMWeXVg4PFq7zerfNFVwvhg3780JB9wyrK43pGYP13ldiffy/vV3oeryGMdvwwifBS4FXAkBln3ss+LLv6VDqfiUT0Xerrpjgg2mVPZS6t+yF4zzlvGsxn89Qk18Y15gV09/dSrcUnI9d+Vw4ApOojkK6bROFAkBvun4HsKyMXt6BBToKyY0dvllbXlWFs9F1PmznoQa9zWZqL4wmqy4fbGP29FyVbgbqdGxEg+hVFXMEZQLHaBUbAkEAxXEdM3Q57OgfJ57ZtCGnOI9f0ly9rkYEFS2bgNQhr1lsojL/U2BiB/W5H+i4zlGPWPAY94kYEkErjSBCfTGu2w==";

//		String keyword = "qingjie1";
//		String url = "http://192.168.33.215:80/wap/user/quickLogin/";		

        String url = "http://paydayloanzyx.testing2.kuainiujinke.com/asset/qingjie/login/";

        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("phone", "15312341111");
//		dataMap.put("cardId", "15300001111"); 
//		dataMap.put("userName", "15300001111");
        String reqData = "";//JSON.toJSONString(dataMap);
        System.out.println("reqData=" + reqData);

        //加密
        String desKey = EncryptUtil.generateDesKey(keyword, sjd_publickey2);
        String keyword2 = EncryptUtil.decryptDesKey(desKey, sjd_privatekey2);
        String encodeContent = EncryptUtil.encode(keyword, reqData);
        String sign = EncryptUtil.sign(encodeContent, qj_privatekey2);
        String token = UUID.randomUUID().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("merchantId", merchantId);
        params.put("Key", desKey);
        params.put("sign", sign);
        params.put("reqData", encodeContent);
        params.put("token", token);
//		params.put("key", desKey);
        params.put("channel", channel);

        StringBuffer strBuffer = new StringBuffer("?");
        try {

            for (Map.Entry<String, String> entry : params.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

                strBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String resultStr = strBuffer.toString();
        resultStr = url + resultStr.substring(0, resultStr.length() - 1);

        System.out.println(resultStr);
        System.out.println(URLDecoder.decode(resultStr, "UTF-8"));
        System.out.println("{" + "\"" + "phone" + "\"" + ":" + "\"" + "15300001111" + "\"" + "}");
        System.out.println(URLEncoder.encode("{" + "\"" + "phone" + "\"" + ":" + "\"" + "15300001111" + "\"" + "}", "UTF-8"));
    }

}
