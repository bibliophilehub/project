package com.inext.entity.fuyou;

import com.inext.constants.Constants;
import com.inext.entity.BackConfigParams;
import com.inext.utils.MD5Utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by IntelliJ IDEA
 * User : zhangsh
 * Date : 2016/12/26 0026
 * Time : 18:00
 */
@XmlRootElement(name = "ORDER")

@XmlAccessorType(XmlAccessType.FIELD)
public class MobilePayRecv {
    private String MCHNTCD;
    private String TYPE;
    private String VERSION;
    private String RESPONSECODE;
    private String RESPONSEMSG;
    private String MCHNTORDERID;
    private String ORDERID;
    private String BANKCARD;
    private String AMT;
    private String SIGN;

    public String getMCHNTCD() {
        return MCHNTCD;
    }

    public void setMCHNTCD(String MCHNTCD) {
        this.MCHNTCD = MCHNTCD;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getRESPONSECODE() {
        return RESPONSECODE;
    }

    public void setRESPONSECODE(String RESPONSECODE) {
        this.RESPONSECODE = RESPONSECODE;
    }

    public String getRESPONSEMSG() {
        return RESPONSEMSG;
    }

    public void setRESPONSEMSG(String RESPONSEMSG) {
        this.RESPONSEMSG = RESPONSEMSG;
    }

    public String getMCHNTORDERID() {
        return MCHNTORDERID;
    }

    public void setMCHNTORDERID(String MCHNTORDERID) {
        this.MCHNTORDERID = MCHNTORDERID;
    }

    public String getORDERID() {
        return ORDERID;
    }

    public void setORDERID(String ORDERID) {
        this.ORDERID = ORDERID;
    }

    public String getBANKCARD() {
        return BANKCARD;
    }

    public void setBANKCARD(String BANKCARD) {
        this.BANKCARD = BANKCARD;
    }

    public String getAMT() {
        return AMT;
    }

    public void setAMT(String AMT) {
        this.AMT = AMT;
    }

    public String getSIGN() {
        return SIGN;
    }

    public void setSIGN(String SIGN) {
        this.SIGN = SIGN;
    }

    public String toSign() {
        Map<String, String> map2 = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU);
        String key = map2.get("KEY");
        return MD5Utils.MD5Encode(TYPE + "|" + VERSION + "|" + RESPONSECODE + "|" + MCHNTCD + "|" + MCHNTORDERID + "|" + ORDERID + "|" + AMT + "|" + BANKCARD + "|" + key);
    }
}
