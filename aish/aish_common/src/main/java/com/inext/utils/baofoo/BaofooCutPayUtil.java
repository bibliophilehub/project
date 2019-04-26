package com.inext.utils.baofoo;

import com.inext.entity.OutOrders;
import com.inext.utils.baofoo.rsa.RsaCodingUtil;
import com.inext.utils.baofoo.util.HttpUtil;
import com.inext.utils.baofoo.util.JXMConvertUtil;
import com.inext.utils.baofoo.util.MapToXMLString;
import com.inext.utils.baofoo.util.SecurityUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dhl on 2017/5/16.
 */
//宝付代付 查询接口
public class BaofooCutPayUtil extends BaseUtils {

    /**
     * 代扣未确认的错误码
     */
    public static List errorCodes = new ArrayList();
    /**
     * 代扣查询状态码
     */
    public static List queryCodes = new ArrayList();
    private static Logger log = org.slf4j.LoggerFactory.getLogger(BaofooCutPayUtil.class);

    static {
        errorCodes.add("BF00100");
        errorCodes.add("BF00112");
        errorCodes.add("BF00113");
        errorCodes.add("BF00115");
        errorCodes.add("BF00144");
        errorCodes.add("BF00202");

        //queryCodes.add("S"); //交易成功
        //queryCodes.add("F"); //交易失败
        queryCodes.add("I"); //处理中
        queryCodes.add("FF");//交易失败

    }

    private static void log(String s) {
        log.info(s);
    }

    //查询代扣状态 简单
    public static Map<String, Object> simpleTransApi(
            String m_orig_trans_id,         //商户订单号
            String m_orig_trade_date            //时间 格式 yyyyMMddHHmmss
    ) throws IOException {
        return transApi(m_orig_trans_id, m_orig_trade_date, "", "", "");
    }

    //查询代扣状态
    public static Map<String, Object> transApi(
            String m_orig_trans_id,         //商户订单号
            String m_orig_trade_date,            //格式 yyyyMMddHHmmss
            String o_additional_info,          //附加字段 非必填
            String o_req_reserved,          //请求方保留域 非必填
            String o_cardAction   // 2 则为信用卡 默认不支持信用卡
    ) throws IOException {
        String txn_sub_type = "31";

        Map<String, String> HeadPostParam = new HashMap<String, String>();

        HeadPostParam.put("version", PropertiesUtil.get("version"));
        HeadPostParam.put("terminal_id", PropertiesUtil.get("terminal.id"));//终端号
        HeadPostParam.put("txn_type", "0431");
        HeadPostParam.put("txn_sub_type", txn_sub_type);
        HeadPostParam.put("member_id", PropertiesUtil.get("member.id"));//商户号
        HeadPostParam.put("data_type", "xml");
        String biz_type = "0000";
        String request_url = PropertiesUtil.get("tran.pay.url");//地址

        if ("2".equals(o_cardAction)) {
            biz_type = "0102";
        }
        /**
         * 报文体
         * =============================================
         *
         */
        String trade_date = m_orig_trade_date;//交易日期
        Map<String, Object> XMLArray = new HashMap<String, Object>();

        XMLArray.put("txn_sub_type", HeadPostParam.get("txn_sub_type"));
        XMLArray.put("biz_type", biz_type);
        XMLArray.put("terminal_id", HeadPostParam.get("terminal_id"));
        XMLArray.put("member_id", HeadPostParam.get("member_id"));
        XMLArray.put("trans_serial_no", "TISN" + System.currentTimeMillis());
        XMLArray.put("orig_trans_id", m_orig_trans_id);
        XMLArray.put("orig_trade_date", trade_date);
        XMLArray.put("additional_info", o_additional_info);
        XMLArray.put("req_reserved", o_req_reserved);

        String PostString = getHttpByData(XMLArray, HeadPostParam, request_url, cutpfxpath);
        PostString = RsaCodingUtil.decryptByPubCerFile(PostString, cerpath);
        if (PostString.isEmpty()) {//判断解密是否正确。如果为空则宝付公钥不正确
            log("=====检查解密公钥是否正确！");
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("resp_code", "OW0001");
            result.put("resp_msg", "检查解密公钥是否正确!");
            return result;
        }

        PostString = SecurityUtil.Base64Decode(PostString);
        log("=====返回查询数据解密结果:" + PostString);

        if (HeadPostParam.get("data_type").equals("xml")) {
            PostString = JXMConvertUtil.XmlConvertJson(PostString);
            log("=====返回结果转JSON:" + PostString);
        }

        Map<String, Object> arrayDataString = JXMConvertUtil.JsonConvertHashMap(PostString);//将JSON转化为Map对象。
        log("转换为MAP对象：" + arrayDataString);
        arrayDataString.put("qbm_signParam", HeadPostParam.toString());//请求参数
        arrayDataString.put("errorCodes", errorCodes);//交易需要再次查询的错误码
        arrayDataString.put("payCutType", OutOrders.TYPE_BAOFOO);//交易需要再次查询的错误码
        arrayDataString.put("orderNo", m_orig_trans_id);//订单号
        return arrayDataString;
    }


    public static String getHttpByData(Map<String, Object> xmlArray,
                                       Map<String, String> HeadPostParam,
                                       String request_url,
                                       String cutpfxpath
    ) throws UnsupportedEncodingException {

        Map<Object, Object> ArrayToObj = new HashMap<Object, Object>();

        String XmlOrJson = "";
        if (HeadPostParam.get("data_type").equals("xml")) {
            ArrayToObj.putAll(xmlArray);
            XmlOrJson = MapToXMLString.converter(ArrayToObj, "data_content");
        } else {
            JSONObject jsonObjectFromMap = JSONObject.fromObject(xmlArray);
            XmlOrJson = jsonObjectFromMap.toString();
        }


        String base64str = SecurityUtil.Base64Encode(XmlOrJson);
        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, cutpfxpath, PropertiesUtil.get("pfx.pwd"));

        HeadPostParam.put("data_content", data_content);

        String PostString = HttpUtil.RequestForm(request_url, HeadPostParam);

        return PostString;
    }


    //只支持储蓄卡交易
    public static Map<String, Object> cutApi(String m_acc_no,          //银行卡
                                             String m_id_card,           //身份证
                                             String m_id_holder,         //真实姓名
                                             String m_mobile,            //手机号
                                             String m_trans_id,          //订单号
                                             String m_txn_amt          //金额
    ) throws IOException, URISyntaxException {
        return api(m_acc_no, m_id_card, m_id_holder, m_mobile, m_trans_id, m_txn_amt, "", "", "");


    }

    //支持信用卡交易 o_cardAction 2为
    public static Map<String, Object> api(
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
        String txn_sub_type = "13";

        String pay_code = BaofooBankCode.getBaofooBankCode(m_acc_no);//request.getParameter("pay_code");//银行卡编码
        String acc_no = m_acc_no;//request.getParameter("acc_no");//银行卡卡号
        String id_card = m_id_card;//request.getParameter("id_card");//身份证号码
        String id_holder = m_id_holder;//request.getParameter("id_holder");//姓名
        String mobile = m_mobile;//request.getParameter("mobile");//银行预留手机号
        String trans_id = m_trans_id;//request.getParameter("trans_id");	//商户订单号
        String valid_date = c_valid_date;//request.getParameter("valid_date"); //信用卡有效期
        String valid_no = c_valid_no;//request.getParameter("valid_no");//信用卡安全码

        String CardAction = o_cardAction;//request.getParameter("CardAction");//卡的类型
        if (trans_id == null) {
            trans_id = "TID" + System.currentTimeMillis();
        }

        Map<String, String> HeadPostParam = new HashMap<String, String>();

        HeadPostParam.put("version", PropertiesUtil.get("version"));
        HeadPostParam.put("member_id", PropertiesUtil.get("member.id"));//商户号
        HeadPostParam.put("terminal_id", PropertiesUtil.get("terminal.id"));//终端号
        HeadPostParam.put("txn_type", "0431");
        HeadPostParam.put("txn_sub_type", txn_sub_type);
        HeadPostParam.put("data_type", "xml");
        String biz_type = "0000";
        String request_url = PropertiesUtil.get("pay.url");//测试地址


        if ("2".equals(CardAction)) {
            biz_type = "0102";
        }

        /**
         * 报文体
         * =============================================
         *
         */
        String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易日期
        Map<String, Object> XMLArray = new HashMap<String, Object>();

        XMLArray.put("txn_sub_type", HeadPostParam.get("txn_sub_type"));
        XMLArray.put("biz_type", biz_type);
        XMLArray.put("terminal_id", HeadPostParam.get("terminal_id"));
        XMLArray.put("member_id", HeadPostParam.get("member_id"));
        XMLArray.put("trans_serial_no", "TISN" + System.currentTimeMillis());
        XMLArray.put("trade_date", trade_date);
        XMLArray.put("additional_info", "附加信息");
        XMLArray.put("req_reserved", "保留");

        if (txn_sub_type.equals("13")) {
            String txn_amt = String.valueOf(new BigDecimal(m_txn_amt).multiply(BigDecimal.valueOf(100)).setScale(0));//支付金额转换成分
            XMLArray.put("pay_code", pay_code);
            XMLArray.put("pay_cm", "2");
            XMLArray.put("id_card_type", "01");
            XMLArray.put("acc_no", acc_no);
            XMLArray.put("id_card", id_card);
            XMLArray.put("id_holder", id_holder);
            XMLArray.put("mobile", mobile);
            XMLArray.put("valid_date", valid_date);
            XMLArray.put("valid_no", valid_no);
            XMLArray.put("trans_id", trans_id);
            XMLArray.put("txn_amt", txn_amt);
        } else if (txn_sub_type.equals("06")) {
            //String orig_trans_id = "123456";//request.getParameter("orig_trans_id");//订单号
            XMLArray.put("orig_trans_id", trans_id);
        }
        String PostString = getHttpByData(XMLArray, HeadPostParam, request_url, cutpfxpath);
        log("请求返回：" + PostString);

        PostString = RsaCodingUtil.decryptByPubCerFile(PostString, cerpath); //1

        if (PostString.isEmpty()) {//判断解密是否正确。如果为空则宝付公钥不正确
            log("=====检查解密公钥是否正确！");
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("resp_code", "OW0001");
            result.put("resp_msg", "检查解密公钥是否正确!");
            return result;
        }

        PostString = SecurityUtil.Base64Decode(PostString);//2
        //----------测试环境打开 1 2
        log("=====返回查询数据解密结果:" + PostString);

        if (HeadPostParam.get("data_type").equals("xml")) {
            PostString = JXMConvertUtil.XmlConvertJson(PostString);
            log("=====返回结果转JSON:" + PostString);
        }

        Map<String, Object> arrayDataString = JXMConvertUtil.JsonConvertHashMap(PostString);//将JSON转化为Map对象。
        log("转换为MAP对象：" + arrayDataString);
        arrayDataString.put("qbm_signParam", HeadPostParam.toString());//请求参数
        arrayDataString.put("errorCodes", errorCodes);//交易需要再次查询的错误码
        arrayDataString.put("payCutType", OutOrders.TYPE_BAOFOO);//交易需要再次查询的错误码
        arrayDataString.put("orderNo", m_trans_id);//订单号
        arrayDataString.put("postString", PostString);//返回参数 明文
        return arrayDataString;
    }

    //银行卡
/*	String m_id_card,           //身份证
	String m_id_holder,         //真实姓名
	String m_mobile,            //手机号
	String m_trans_id,          //订单号
	String m_txn_amt          //金额*/
    public static void main(String[] args) throws IOException, URISyntaxException {

        BaofooCutPayUtil.cutApi(
                "6222801332051125666",
                "321281199210101252",
                "邓海磊",
                "13061966998",
                "001",
                "0"
        );
    }
}
