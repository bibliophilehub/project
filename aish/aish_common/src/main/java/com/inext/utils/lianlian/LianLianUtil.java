package com.inext.utils.lianlian;

import com.alibaba.fastjson.JSONObject;
import com.lianpay.api.util.TraderRSAUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LianLianUtil {
    private final static String prikeyvalue = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMoyfGQMIrY1y08FKps4js9voq5qvZe/hmsul1WYKkS2uz9bpxqJ0RRj/FlTpUYtca3axdVRzRRMFuPdhtp5xo/dNVOE+0IJOJkhoYeEQVedzfTVKi3mHzfxfTuU7EwOPUIVJrtODqolL/MMKbO5JvvK7+mecNoQSnd+Xy6OQyqxAgMBAAECgYB73CVa3Bw/ZgCe7VlGMIU7CPWsxnJRjN94tNe7dTUfVpipnSlmFlpflUg2KAksThY/G67ol+16q4myGDfNiZ9Z1nKBcuY4LzRvbs5i9JXh+U+j10gurr0TQPAd5xuMGRFr8KHdhwm/5ab4E6ztMUUBZ1nzpsZd/RXbW5Vl7RwDKQJBAO3SfV+QHMi53He0F7+zG4FeBPY4/Wbdf4xpdTCeXsLP3k/PqPul9BP8xuenzdUxIVIRPwCBTzxf8XhybDu06XsCQQDZpupDHFXFDOvOLIeItgjl+4Qi+AnMGQluH5rUusUaxqUK7JeNKzVGHwYRx2laDlk2sj1kSj1uO9y5g5wpMlbDAkBODHpg1mbb6UNVdaElpJ4uQrDWLbXcAG1Po3x2TENFI/sNrlUC4V/M/3Q3qnZ+tPT+ffJzM/zRD5urr8Gdhq3pAkEAybez17z0FOtzxCJq/p5PQu7HOS2OzvTJU6HA+rWapcVDAwvhLXnFJxbrpiCdmFRCBLpOwZs4seiTGfBLE7dOxwJBAK4+OdqEPFhbGeDn7dL2UXNoM69KcsmPgkDom+jsfs21ksvsX2rA65xdCSCRBaWxwUVHWeqGsjUvGUbWTQ3MfKQ=";
    /***
     * 放款url
     */
    public static String PAYMENTAPI = "https://instantpay.lianlianpay.com/paymentapi/payment.htm";

    public static String genSign(JSONObject reqObj) {
        String sign = reqObj.getString("sign");
        String sign_type = reqObj.getString("sign_type");
        //生成待签名串
        String sign_src = genSignData(reqObj);
        System.out.println("商户[" + reqObj.getString("oid_partner") + "]待签名原串"
                + sign_src);
        System.out.println("商户[" + reqObj.getString("oid_partner") + "]签名串"
                + sign);
        return getSignRSA(sign_src);
    }

    /**
     * 生成待签名串
     *
     * @param paramMap
     * @return
     */
    public static String genSignData(JSONObject jsonObject) {
        StringBuffer content = new StringBuffer();

        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(jsonObject.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            // sign 和ip_client 不参与签名
            if ("sign".equals(key)) {
                continue;
            }
            String value = (String) jsonObject.getString(key);
            // 空串不参与签名
            if (null == value) {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);

        }
        String signSrc = content.toString();
        if (signSrc.startsWith("&")) {
            signSrc = signSrc.replaceFirst("&", "");
        }
        return signSrc;
    }

    /**
     * RSA签名验证
     *
     * @param reqObj
     * @return
     */
    public static String getSignRSA(String sign_src) {
        return TraderRSAUtil.sign(prikeyvalue, sign_src);

    }
}
