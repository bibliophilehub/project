package test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.inext.constants.BizTypeConstant;
import com.inext.constants.ChannelAPIConstant;
import com.inext.utils.DateUtils;
import com.inext.utils.HttpUtil;
import com.inext.utils.UuidUtil;
import com.inext.utils.encrypt.DES;
import com.inext.utils.encrypt.EncryptUtil;
import com.inext.utils.encrypt.RSA;

import java.util.HashMap;
import java.util.Map;


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
public class EncryptSyjTest {

    /*
     * 说明： 用openssl生成的私钥必须要用pkcs8转换下，
     * <pre>
     * 转换命令为：pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform PEM -nocrypt
     */

    public static String sjd_publickey2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDrB4PWNANFxsohetLx/LD2ws8f3EGrKV7560YwcaYdXwTG4NjBaHMVxsHxAKPAc6Mtr7n20X+Govtbpx4su+wKQr65K+DHk8lQHBcyN3yZkxAC74nfLGglTyt5nKen/BaYqOVYhbYSg5oazEOZDD13chZu1V9c7WOyvX0upvYZyQIDAQAB";
    public static String sjd_privatekey2 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOsHg9Y0A0XGyiF60vH8sPbCzx/cQaspXvnrRjBxph1fBMbg2MFocxXGwfEAo8Bzoy2vufbRf4ai+1unHiy77ApCvrkr4MeTyVAcFzI3fJmTEALvid8saCVPK3mcp6f8Fpio5ViFthKDmhrMQ5kMPXdyFm7VX1ztY7K9fS6m9hnJAgMBAAECgYBDXtoZsrsla0BRrIV/PDH16g25Uke4qplBBoIOLOWzEaOq5pT7i9dBbMH3NXm7Fuq1k+O4roDhtSJcOWFa7VtRYOnD+qkcX78sd5+fOf9bNDzWcjZd6dzcGFrCw2uRRAiVlBflXs9HAsVGGeLJuCPPL0ioohhAk8MjApwMl8zEqQJBAP7k/a2efVIoMjGxmJvUWNIPG9Ve9c0wRm4tXGvd7A1UkL+R8ErU5h+/p237/LmOBDhePsIO4Icl1MFVJS5qw58CQQDsDHfCpwkGaW7ukMlc4e8a/y0op+X2R0wSCe1bU9mJSsVtyf+CW9/33iU225tpNDAcQno5Z2XOKpZ5GmS4b+mXAkEA0MtbUcEl/wqWM0a7L8q+BzQBJMNorfABgXb+4g0js7e16nbtUx8acp9X0yw7VPQUWOg6mFP/cDV5FfVdQ6yJGQJAQo+BdbIEKXqxfTP2k+phsgsigMRXZArWuH90HryWKtCau0qqcefWT96kP8PNHu1IY6+bJ3SwkGIR7DQ1Eoz8IQJAV4YsnEUo98hyInfacmQH2ZYQFI2ZedljNpCknIRerbaGS5oezvXon1/RCJ0Kt8PSQOEFx+0COPaBIPAEztNjOA==";

    public static String syj_publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqdtJyNcKB4aAJOT2treVNaOfAMQR/IsgDTJMNVYKNIQiogGHV9k09fmGvlC0XPmkSs/yMbGdXI3y0eJsRPFMqjPRzFMsvTmuR+eyiSY5SJ6FjURcSPPX1/X4nbBZTo4nwzqg4EBLD9WoNwgRcO+ZjeVfA/MdwaxyXpu0a7bdEAQIDAQAB";
    public static String syj_privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKp20nI1woHhoAk5Pa2t5U1o58AxBH8iyANMkw1Vgo0hCKiAYdX2TT1+Ya+ULRc+aRKz/IxsZ1cjfLR4mxE8UyqM9HMUyy9Oa5H57KJJjlInoWNRFxI89fX9fidsFlOjifDOqDgQEsP1ag3CBFw75mN5V8D8x3BrHJem7Rrtt0QBAgMBAAECgYBH8ytUhZh51dAE1KJSAoo8qEDZgFcTioy9x75x7FvUUdMjvDyPYQytHgEBaRu3KeKTwGCirMbZouLqEdXx6nJ3XNHhwsnDDoxWtEGIZ6YEHHAx97t9iPNUQE7Nnho9g6ymIERdSrmjRs6dV64rErFi1ENJ+F2Xe60oVppTRqDCAQJBANnxJlp0xF+Yhj+yn3a6T3RoacfRABrNKS5/8/IwTH7rwV5VNNQ8yrckZ/IG5sqiAPtW3J3Qk9wlCjaCGsnaD7ECQQDIOzqepv1M5LqdZ9ohJ6ySpMSsvgRST+rQtF5otHIIvds2s2TSh1jDzEzkTJQRLpEet6F2rbb93i0jZTCWlV1RAkAydflRXFPTZnc+7FaroLug0kQbtbJ2giYU1B9hZflwMZnWA5h1w5WUDePbT+nD5PqhNx1hlNeHUzQoJruZHNohAkAumgJhrHHltJJuUh5lewtiMfEIV01ALxNysZgLfHvR5wYfLA96JEAXifQXam9HtHPYF1r/+RxO56fPxhxR37+RAkEAyt2wRz3hX2CB/EGJUarD3PqsKfv0QJM+hgnjYsnASNLvQ2/2vpnA8PtOjh3JYt3zuNm9SEVpjW7IqCoZ7KRloQ==";
    public static String caixiaoxian_publickey = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgGj72Re5B+ZhY7KZRl2DBV1n5z197ZzyhkIRRKR0lWrNquq+sBy7ZoUCrE8N3cL0GkPXOMQ4WbsJC0iBBwIDdcWm3BhS8Li3Df0IPu1I0mXAZW5EhUwxfj2bGWkpNG3MvzTa3eCJTb6yKU3AFYnvHZPSN5PzPZ7f+0uk6aKs3LxTAgMBAAE=";

    public static String zhugejiebei_publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDbuCXfmqENB7N+O1K82+YVvIZwKDwiiaPVZmS7rRUoJwXD8s7V88gVpwTNL8J0Ggrgw6rhBclfDrc6GQMs82iIxHJ9IDWJjyQfFs47GoGmG1g3t8j7eSth2hT06SgbS/w1YojzhzIndAbEjcKdE4Z8FxUR4j5VNAmcLdaeJcqmMwIDAQAB";
    /**
     * 有轻借分配的商户编码
     */
    public static String merchantId = "201708248210";
    /**
     * merchantId
     * desKey
     * sign
     * reqData
     */
    static Map<String, String> simulationReqBizDataMap = new HashMap<String, String>();
    /**
     *
     */
    Map<String, String> simulationReqDataMap = new HashMap<String, String>();

    public static void main(String[] args) throws Exception {
        EncryptSyjTest test = new EncryptSyjTest();
        // 请求示例
        String keyword = DateUtils.getDateTime();
        System.out.println(test.requestZgjb(keyword));
        //String reqData=test.requestQL(keyword);
        //System.out.println(reqData);
        // 响应示例
        //test.responseQL(reqData);

//		String sign="GoghRuVDp1Y0H8VHMrEaxh0gI2tC0AJI/CCqQIMgNKGra4/3UmL2CJZp5kmDKXkw/0I2xmjQDaTpffsREUOvyKLvIFoJ0tYXs4b/A9QT6Nvc996pbn+jkeoLwOrXwU71i/MtosNcVm2s4gQLRCbLsitDbye9pH5MRPs0B2QS6gM=";
//		System.out.println("sign="+Base64.decode(sign));


    }

    /**
     * 请求第三方报文加签加密示例
     *
     * @author likai
     * @data 2018年8月23日
     * @Version V1.0
     */
    public String requestQL(String keyword) throws Exception {

        simulationReqBizDataMap.put("mobile", "13651679999");
        simulationReqBizDataMap.put("userAccount", "李思");
        simulationReqBizDataMap.put("identityCard", "420203198010212356");
        simulationReqBizDataMap.put("useSex", "0");
        simulationReqBizDataMap.put("userCity", "上海");
        simulationReqBizDataMap.put("userProfessional", "1");
        simulationReqBizDataMap.put("education", "6");
        simulationReqBizDataMap.put("userCreditCard", "1");
        simulationReqBizDataMap.put("userFund", "1");
        simulationReqBizDataMap.put("userSocialSecurity", "1");
        simulationReqBizDataMap.put("loanAmount", "3000.00");
        simulationReqBizDataMap.put("loanLimit", "14天");
        simulationReqBizDataMap.put("interest", "200.00");
        String bizData = JSON.toJSONString(simulationReqBizDataMap);
        String desKey = EncryptUtil.generateDesKey(keyword, caixiaoxian_publickey);
        String encodeContent = EncryptUtil.encode(keyword, bizData);
        String sign = EncryptUtil.sign(encodeContent, syj_privatekey);

        simulationReqDataMap.put("bizData", encodeContent);
        simulationReqDataMap.put("bizType", "1");
        simulationReqDataMap.put("encryption", "1");
        simulationReqDataMap.put("sourceChannel", "syj");
        simulationReqDataMap.put("targetChannel", "zj");

        simulationReqDataMap.put("sign", sign);
        simulationReqDataMap.put("returnUrl", "");
        simulationReqDataMap.put("uuid", "011bb13f388e4ea698cbc648ea4218e2");
        simulationReqDataMap.put("desKey", desKey);

        return JSON.toJSONString(simulationReqDataMap);
    }

    public String requestZgjb(String keyword) throws Exception {

        Map<String, String> simulationReqBizDataMap = new HashMap<String, String>();
        String startTime = DateUtils.getOneHoursAgoTime();
        String endTime = DateUtils.getCurHoursAgoTime();
        simulationReqBizDataMap.put("startTime", startTime);
        simulationReqBizDataMap.put("endTime", endTime);
        System.out.println("startTime:" + startTime + ",endTime=" + endTime);
        String bizData = JSON.toJSONString(simulationReqBizDataMap);
        String desKey = EncryptUtil.generateDesKey(keyword, zhugejiebei_publickey);
        String encodeContent = EncryptUtil.encode(keyword, bizData);
        String sign = EncryptUtil.sign(encodeContent, syj_privatekey);
        Map<String, String> simulationReqDataMap = new HashMap<String, String>();
        simulationReqDataMap.put("bizData", encodeContent);
        simulationReqDataMap.put("bizType", BizTypeConstant.BIZ_TYPE_02);
        simulationReqDataMap.put("sourceChannel", ChannelAPIConstant.CHANNEL_01);
        simulationReqDataMap.put("targetChannel", ChannelAPIConstant.CHANNEL_05);
        simulationReqDataMap.put("sign", sign);
        simulationReqDataMap.put("uuid", UuidUtil.getRandomUUID32());
        simulationReqDataMap.put("desKey", desKey);
        String reqDataStr = JSON.toJSONString(simulationReqDataMap);
        String reqUrl = "http://1885j81v05.iask.in/qbm_api/SouyijieController/getLoan";
        String responeDataStr = HttpUtil.doPostByJson(reqUrl, reqDataStr);

        return responeDataStr;
    }

    /**
     * 第三方服务响应报文解析示例
     *
     * @author likai
     * @data 2018年8月23日
     * @Version V1.0
     */
    public void responseQL(String reqData) throws Exception {

        JSONObject json = JSON.parseObject(reqData);
        String desKey = (String) json.get("desKey");
        desKey = RSA.decryptByPrivateKey(desKey, sjd_privatekey2);
        System.out.println("desKey=" + desKey);
        String data = (String) json.get("bizData");
        String sign = (String) json.get("sign");
        boolean bool = RSA.verify(data, sign, syj_publickey);
        if (!bool) {
            System.out.println("验签失败！");
            return;
        }
        //desKey保持和请求时一致
        DES newDes = new DES(desKey);
        String decryptData = "";
        try {
            decryptData = newDes.decrypt(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解密之后的data报文为：" + decryptData);
    }
}
