package com.inext.utils.baofoo;

import com.inext.utils.baofoo.rsa.RsaCodingUtil;
import com.inext.utils.baofoo.util.JXMConvertUtil;
import com.inext.utils.baofoo.util.SecurityUtil;
import org.slf4j.Logger;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl on 2017/5/17.
 */

public class BaofooAuthUtil extends BaseUtils {


    private static Logger log = org.slf4j.LoggerFactory.getLogger(BaofooAuthUtil.class);

    private static WebApplicationContext webApplicationContext;


    //获取公共的报文
    public static Map getPublicPost() {
        Map<String, String> HeadPostParam = new HashMap<String, String>();

        HeadPostParam.put("version", version);
        HeadPostParam.put("member_id", member_id);//商户号
        HeadPostParam.put("terminal_id", auth_terminal_id);//终端号
        HeadPostParam.put("txn_type", "0431");
        HeadPostParam.put("txn_sub_type", "01");//直接绑卡
        HeadPostParam.put("data_type", "xml");
        return HeadPostParam;
    }

    public static Map simpleApi(
            String m_acc_no,          //银行卡
            String m_id_card,           //身份证
            String m_id_holder,         //真实姓名
            String m_mobile,            //手机号
            String m_trans_id          //订单号

    ) throws IOException, URISyntaxException {
        return api(
                m_acc_no,
                m_id_card,
                m_id_holder,
                m_mobile,
                m_trans_id,
                "",
                "",
                "",
                ""
        );
    }

    //直接绑卡交易
    public static Map /*Map<String, Object>*/ api(
            String m_acc_no,          //银行卡
            String m_id_card,           //身份证
            String m_id_holder,         //真实姓名
            String m_mobile,            //手机号
            String m_trans_id,          //订单号
            String m_txn_amt,          //金额
            String c_valid_date,   //格式：YYMM              如：07月/18年则写成1807
            String c_valid_no,         //银行卡背后最后三位数字
            String o_cardAction   // 2 则为信用卡
    ) throws IOException, URISyntaxException {

        Map errorMap = new HashMap();
        String txn_sub_type = "01";
        Map<String, String> HeadPostParam = getPublicPost();

        String pay_code = BaofooBankCode.getBaofooBankCode(m_acc_no);//request.getParameter("pay_code");//银行卡编码
        String acc_no = m_acc_no;//request.getParameter("acc_no");//银行卡卡号
        String id_card = m_id_card;//request.getParameter("id_card");//身份证号码
        String id_holder = m_id_holder;//request.getParameter("id_holder");//姓名
        String mobile = m_mobile;//request.getParameter("mobile");//银行预留手机号
        String trans_id = m_trans_id;//request.getParameter("trans_id");	//商户订单号

        String request_url = PropertiesUtil.get("tran.pay.url");//测试地址 tran.pay.url


        /**
         * 报文体
         * =============================================
         *
         */
        String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
        Map<String, Object> XMLArray = new HashMap<String, Object>();


        /**
         *
         * 公共参数
         *
         */
        XMLArray.put("txn_sub_type", HeadPostParam.get("txn_sub_type"));
        XMLArray.put("biz_type", "0000");
        XMLArray.put("terminal_id", HeadPostParam.get("terminal_id"));
        XMLArray.put("member_id", HeadPostParam.get("member_id"));
        XMLArray.put("trans_serial_no", "TISN" + System.currentTimeMillis());
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");
        XMLArray.put("trans_id", trans_id);//商户订单号
        XMLArray.put("trade_date", trade_date);//订单日期

        errorMap.put("code", "-101");
        if (txn_sub_type.equals("01")) {

            log("========直接绑卡类交易===========");
            XMLArray.put("acc_no", acc_no);//卡号
            XMLArray.put("id_card_type", "01");//证件类型固定01（身份证）
            XMLArray.put("id_card", id_card);
            XMLArray.put("id_holder", id_holder);
            XMLArray.put("mobile", mobile);
            XMLArray.put("acc_pwd", "");
            XMLArray.put("valid_date", "");
            XMLArray.put("valid_no", "");
            XMLArray.put("pay_code", pay_code);
            //
        } else if (txn_sub_type.equals("12")) {
            //log("========确认绑卡交易===========");
            if (trans_id.isEmpty()) {

                errorMap.put("msg", "商户订单号不能为空【trans_id】,注：需和预绑卡交易订单号一致");
                return errorMap;
            }

            String sms_code = "sms_code";//短信验证码
            if (sms_code.isEmpty()) {
                errorMap.put("msg", "短信验证码不能为空【sms_code】");
                return errorMap;
            }
            XMLArray.put("sms_code", sms_code);
            XMLArray.put("trans_id", trans_id);//商户订单号

        } else if (txn_sub_type.equals("02")) {

            String bind_id = "bind_id";//获取绑定标识
            XMLArray.put("bind_id", bind_id);

        } else if (txn_sub_type.equals("03")) {

            //log("========查询绑定关系类交易===========");
            XMLArray.put("acc_no", acc_no);//卡号

        } else if (txn_sub_type.equals("15")) {

            //log("========支付类预支付交易===========");txn_amt
            BigDecimal txn_amt_num = new BigDecimal("0.01").multiply(BigDecimal.valueOf(100));//金额转换成分
            String txn_amt = String.valueOf(txn_amt_num.setScale(0));//支付金额
            String bind_id = "bind_id";//获取绑定标识

            Map<String, String> ClientIp = new HashMap<String, String>();

            ClientIp.put("client_ip", "100.0.0.0");
            XMLArray.put("bind_id", bind_id);

            XMLArray.put("trans_id", trans_id);

            XMLArray.put("risk_content", ClientIp);

            XMLArray.put("txn_amt", txn_amt);//金额以分为单位(整型数据)并把元转换成分

        } else if (txn_sub_type.equals("16")) {
            //log("========支付类支付确认交易===========");

            String sms_code = "sms_code";//支付短信验证码
            String business_no = "business_no";//宝付业务流水号
            if (business_no.isEmpty()) {
                errorMap.put("msg", "宝付业务流水号不能为空【business_no】");
                return errorMap;
            }
            XMLArray.put("sms_code", sms_code);
            XMLArray.put("business_no", business_no);

        } else if (txn_sub_type.equals("06")) {

            //log("======【 交易状态查询类交易】=======");
            String orig_trans_id = "orig_trans_id";//订单号
            XMLArray.put("orig_trans_id", orig_trans_id);
        } else {
            errorMap.put("msg", "【txn_sub_type】参数错误！");
            return errorMap;
        }

        String PostString = BaofooCutPayUtil.getHttpByData(XMLArray, HeadPostParam, request_url, pfxpath);
        //log("请求返回："+ PostString);

        PostString = RsaCodingUtil.decryptByPubCerFile(PostString, cerpath);

        if (PostString.isEmpty()) {//判断解密是否正确。如果为空则宝付公钥不正确
            //log("=====检查解密公钥是否正确！");
            errorMap.put("msg", "检查解密公钥是否正确！");
            return errorMap;
        }

        PostString = SecurityUtil.Base64Decode(PostString);

        //此处待商定

        //log("=====返回查询数据解密结果:"+PostString);

        if (HeadPostParam.get("data_type").equals("xml")) {
            PostString = JXMConvertUtil.XmlConvertJson(PostString);
            //log("=====返回结果转JSON:"+PostString);
        }

        Map<String, Object> ArrayDataString = JXMConvertUtil.JsonConvertHashMap(PostString);//将JSON转化为Map对象。
        //log("转换为MAP对象："+ArrayDataString);

        return ArrayDataString;
    }

    private static void log(String s) {
        log.debug(s);
    }

    /**
     * //银行卡
     * //身份证
     * //真实姓名
     * //手机号
     * //订单号
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println(BaofooAuthUtil.simpleApi(
                "6217580800001693154",//
                "340621198907314059",//
                "任成龙",
                "13120806278",//13120806278
                "123456789"
        ));
    }
}
