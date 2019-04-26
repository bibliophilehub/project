package com.inext.entity.fuyou;

import com.google.common.collect.Maps;
import com.inext.constants.Constants;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BorrowUser;
import com.inext.entity.RepaymentInfo;
import com.inext.utils.MD5Utils;
import com.inext.utils.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: jzhang
 * @Date: 2018-04-12 0012 上午 10:55
 */
public class Fuiou {


    //生产地址
    public static final String URL = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("FUYOU_PATH");
//    public static final String URL = "https://mpay.fuiou.com:16128/h5pay/payAction.pay";
//    //测试地址
//    public static final String TEST_URL = "http://www-1.fuiou.com:18670/mobile_pay/h5pay/payAction.pay";
    //商户32位密钥key
    public static final String KEY = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("KEY");
    //富友分配给各合作商户的唯一识别码
    public static final String MCHNTCD = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("MCHNTCD");
    //交易类型
    public static final String TYPE = "10";
    //是否对订单信息FM域的内容进行加密，1表示加密，0表示不加密
    public static final String ENCTP = "0";
    //接口版本号，请填固定值2.0
    public static final String VERSION = "2.0";
    //是否隐藏支付页面富友的logo，1隐藏，0显示
    public static final String LOGOTP = "1";
    //商户接收支付结果的后台通知地址
    public static final String BACKURL = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("BACK_PATH")+"/baoFooPay/callback";
    public static  String HOMEURL = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("BACK_PATH")+"/baoFooPay/homeurl?repaymentInfoId=";
    public static final String REURL = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("BACK_PATH")+"/baoFooPay/reurl";
    public static final String SIGNTP = "md5";
    public static final String IDKEY = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.FUYOU).get("ID_KEY");

    private String mchntorderid;//商户订单号
    private String userid;//用户编号
    private String amt;//交易金额
    private String bankcard;//银行卡号
    private String name;//姓名
    private String homeurl;//页面通知URL
    private String idtype="0";//证件类型 0身份证
    private String idno;//身份证号码
    private String rem1;//保留字段1
    private String rem2;//保留字段2
    private String rem3;//保留字段3
    private String sign;//签名

    public Fuiou() {
        super();
    }
    public Fuiou(BorrowUser user, String repaymentInfoId, String amt) {
        if(user!=null){
            this.mchntorderid= IDKEY +repaymentInfoId;
            this.userid=user.getId().toString();
            this.amt=amt;
            this.bankcard=user.getCardCode();
            this.name=user.getUserName();
            this.idno=user.getUserCardNo();
            this.homeurl=HOMEURL+repaymentInfoId;
        }
    }
    public Fuiou(BorrowUser user, RepaymentInfo repaymentInfo) {
        if(user!=null){
            this.mchntorderid= IDKEY +repaymentInfo.getId().toString();
            this.userid=user.getId().toString();
            //富有 实际支付的交易金额，交易金额分为单位 所以这里需要*100
            this.amt=repaymentInfo.getReqAmt().multiply(new BigDecimal(100)).intValue()+"";
            this.bankcard=user.getCardCode();
            this.name=user.getUserName();
            this.idno=user.getUserCardNo();
        }
    }

    public String getMchntorderid() {
        return mchntorderid;
    }

    public void setMchntorderid(String mchntorderid) {
        this.mchntorderid = mchntorderid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getRem1() {
        return rem1;
    }

    public void setRem1(String rem1) {
        this.rem1 = rem1;
    }

    public String getRem2() {
        return rem2;
    }

    public void setRem2(String rem2) {
        this.rem2 = rem2;
    }

    public String getRem3() {
        return rem3;
    }

    public void setRem3(String rem3) {
        this.rem3 = rem3;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getHomeurl() {
        return homeurl=HOMEURL+mchntorderid;
    }

    public String getFM() {
        if(!checkVal()){
            return null;
        }
        homeurl=HOMEURL+mchntorderid;
        Map<String, String> params = Maps.newHashMap();
        // 需要进行加签字段
        {
            params.put("MCHNTCD", MCHNTCD);
            params.put("TYPE", TYPE);
            params.put("VERSION", VERSION);
            params.put("LOGOTP", LOGOTP);
            params.put("MCHNTORDERID", mchntorderid);
            params.put("USERID", userid);
            params.put("AMT", amt);
            params.put("BANKCARD", bankcard);
            params.put("BACKURL", BACKURL);
            params.put("REURL", REURL);
            params.put("HOMEURL", homeurl);
            params.put("NAME", name);
            params.put("IDTYPE", idtype);
            params.put("IDNO", idno);
        }
        params.put("REM1", rem1);
        params.put("REM2", rem2);
        params.put("REM3", rem2);
        params.put("SIGNTP", SIGNTP);
        params.put("SIGN", getSign());

        String str = "<ORDER>";
        for (String key : params.keySet()) {
            str += "<" + key + ">" + params.get(key) + "</" + key + ">";
        }
        str += "</ORDER>";
        return str;
    }


    public String getSign() {
        homeurl=HOMEURL+mchntorderid;
        if (checkVal()) {
            System.out.println(TYPE + "|" + VERSION + "|" + MCHNTCD + "|" + mchntorderid + "|" + userid + "|" + amt + "|" + bankcard + "|" + BACKURL + "|" + name + "|" + idno + "|" + idtype + "|" + LOGOTP + "|" + homeurl + "|" + REURL + "|" + KEY);
            this.sign = MD5Utils.md5(TYPE + "|" + VERSION + "|" + MCHNTCD + "|" + mchntorderid + "|" + userid + "|" + amt + "|" + bankcard + "|" + BACKURL + "|" + name + "|" + idno + "|" + idtype + "|" + LOGOTP + "|" + homeurl + "|" + REURL + "|" + KEY);
            System.out.println(this.sign);
        }
        return sign;
    }

    public static final Map<String, String> getFuyouApiParams() {
        Map<String, String> fuyouApiParams = new HashMap<>();
        fuyouApiParams.put("ENCTP", ENCTP);
        fuyouApiParams.put("MCHNTCD", MCHNTCD);
        fuyouApiParams.put("TYPE", TYPE);
        fuyouApiParams.put("VERSION", VERSION);
        return fuyouApiParams;
    }

    public boolean checkVal() {
        return StringUtils.isNotEmpty(mchntorderid) &&
                StringUtils.isNotEmpty(userid) &&
                StringUtils.isNotEmpty(amt) &&
                StringUtils.isNotEmpty(bankcard) &&
                StringUtils.isNotEmpty(name) &&
                StringUtils.isNotEmpty(idtype) &&
                StringUtils.isNotEmpty(idno);
    }

}
