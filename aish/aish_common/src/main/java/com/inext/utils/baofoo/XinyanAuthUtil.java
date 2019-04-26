package com.inext.utils.baofoo;

import com.google.common.collect.Maps;
import com.inext.utils.baofoo.rsa.RsaCodingUtil;
import com.inext.utils.baofoo.util.HttpUtil;
import com.inext.utils.baofoo.util.JXMConvertUtil;
import com.inext.utils.baofoo.util.MapToXMLString;
import com.inext.utils.baofoo.util.SecurityUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dhl on 2017/6/19.
 */
@Component
public class XinyanAuthUtil {

    public static List<String> errorList = new ArrayList<String>();
    protected static String version;
    //新颜
    protected static String xypfxpath;
    protected static String xycerpath;
    protected static String xycername;
    protected static String xydate_type;
    protected static String xymember_id;
    protected static String xyterminal_id;
    protected static String bankCardBinUrl;


    private static Logger log = LoggerFactory.getLogger(XinyanAuthUtil.class);

    static {

        //新颜
        xypfxpath = PropertiesUtil.get("file.xy.auth.pfx.name");//商户私钥 认证
        xycerpath = PropertiesUtil.get("file.xy.cer.name");//宝付公钥
        xycername = PropertiesUtil.get("xy.cer.name");//宝付密码

        xymember_id = PropertiesUtil.get("xy.member.id");//商户号
        xyterminal_id = PropertiesUtil.get("xy.terminal.id");//终端号
        xydate_type = PropertiesUtil.get("xy.data.type");//数据类型


        bankCardBinUrl = PropertiesUtil.get("xy.bankCardBinUrl");//cardBin


    }

    static {
        errorList.add("S0001");
        errorList.add("S1010");
        errorList.add("S1011");
        errorList.add("S1012");
        errorList.add("S1013");
        errorList.add("S1014");
        errorList.add("S1016");

    }

    /**
     * 发送银行卡短信验证码
     * 由第三方发送
     *
     * @param trans_id_p
     * @param trade_date_p
     * @param acc_no_p
     * @param id_card_p
     * @param id_holder_p
     * @param mobile_p
     * @return
     */
    public Map sendSmsAuth(
            String trans_id_p,//商户订单号
            String trade_date_p,//订单交易时间日期 yyyyMMddHHmmss
            String acc_no_p,//银行卡号码
            String id_card_p,//身份证
            String id_holder_p,// 真实姓名
            String mobile_p //预留手机号
    ) throws IOException {
        return Auth(trans_id_p, trade_date_p, acc_no_p, id_card_p, id_holder_p, mobile_p, "101", "xy.authApplyUrl", "", "");
    }

    /**
     * 短信正式校验
     *
     * @return
     * @throws IOException
     */
    public Map realAuth(
            String trade_no_x_p,//交易流水
            String sms_code_p//短信验证码
    ) throws IOException {
        return Auth("", "", "", "", "", "", "", "xy.authConfirmUrl", trade_no_x_p, sms_code_p);
    }

    public Map Auth(
            String trans_id_p,//订单日期
            String trade_date_p,//商户订单号
            String acc_no_p,//银行卡号码
            String id_card_p,//身份证
            String id_holder_p,// 真实姓名
            String mobile_p,//预留手机号
            String card_type_p,//银行卡类型
            String type_p,//认证接口
            String trade_no_x_p,//交易流水
            String sms_code_p//短信验证码
    ) throws IOException {
        String acc_no = acc_no_p;
        String id_card = id_card_p;
        String id_holder = id_holder_p;
        String mobile = mobile_p;
        String card_type = card_type_p;
        String type = type_p;
        String trade_no_x = trade_no_x_p;
        String sms_code = sms_code_p;
        log.info("=====输入的信息为：银行卡号(acc_no):" + acc_no + " ,持卡人证件号(id_card)：" + id_card + " ,"
                + "持卡人姓名(id_holder):" + id_holder + " ,银行预留手机号(mobile):" + mobile + ""
                + " ,卡片类型(card_type):" + card_type);
        /** 1、 商户号 **/
        String member_id = xymember_id;
        /**2、终端号 **/
        String terminal_id = xyterminal_id;
        /** 3、 加密数据类型 **/
        String data_type = xydate_type;
        /** 4、加密数据 **/
        String trans_id = trans_id_p;//""+System.currentTimeMillis();// 商户订单号
        String trade_date = trade_date_p;//new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 订单日期

        /** 组装参数 (13) **/
        String request_url = PropertiesUtil.get(type);
        Map<Object, Object> ArrayData = new HashMap<Object, Object>();
        if ("xy.authApplyUrl".equals(type)) {
            ArrayData.put("member_id", member_id);
            ArrayData.put("terminal_id", terminal_id);
            ArrayData.put("id_holder", id_holder);
            ArrayData.put("id_card", id_card);
            ArrayData.put("acc_no", acc_no);
            ArrayData.put("card_type", card_type);
            ArrayData.put("mobile", mobile);
            ArrayData.put("trans_id", trans_id);
            ArrayData.put("trade_date", trade_date);

        } else {
            ArrayData.put("member_id", member_id);
            ArrayData.put("terminal_id", terminal_id);
            ArrayData.put("trade_no", trade_no_x);
            ArrayData.put("sms_code", sms_code);

        }


        Map<Object, Object> ArrayData1 = new HashMap<Object, Object>();
        String XmlOrJson = "";
        if (data_type.equals("xml")) {
            ArrayData1.putAll(ArrayData);
            XmlOrJson = MapToXMLString.converter(ArrayData1, "data_content");
        } else {
            JSONObject jsonObjectFromMap = JSONObject.fromObject(ArrayData);
            XmlOrJson = jsonObjectFromMap.toString();
        }
        log.info(" 新颜 请求未加密参数 =" + XmlOrJson);
        /** base64 编码 **/
        String base64str = SecurityUtil.Base64Encode(XmlOrJson);
        /** rsa加密  **/
        // 商户私钥
        String pfxpath = URLDecoder.decode(xypfxpath);
//        File pfxfile = new File(pfxpath);
//        if (!pfxfile.exists()) {
//            throw new RuntimeException("私钥文件不存在！");
//        }
        String pfxpwd = xycername;// 私钥密码

        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, pfxpwd);//加密数据

        /**============== http 请求==================== **/
        log.info(" 新颜 请求加密参数 =" + data_content);
        Map<String, String> HeadPostParam = new HashMap<String, String>();
        HeadPostParam.put("member_id", member_id);
        HeadPostParam.put("terminal_id", terminal_id);
        HeadPostParam.put("data_type", data_type);
        HeadPostParam.put("data_content", data_content);
        String PostString = HttpUtil.RequestForm(request_url, HeadPostParam);
        log.info(" 新颜 返回参数 =" + PostString);
        Map<String, Object> ArrayDataString = JSONObject.fromObject(PostString);//将JSON转化为Map对象。
        return ArrayDataString;
    }

    public com.alibaba.fastjson.JSONObject cardbin(String acc_no) throws IOException {

        /** 1、 商户号 **/
        String member_id = xymember_id;
        /**2、终端号 **/
        String terminal_id = xyterminal_id;
        /** 3、 加密数据类型 **/
        String data_type = xydate_type;
        /** 4、加密数据 **/
        String trans_id = "" + System.currentTimeMillis();// 商户订单号
        String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 订单日期


        /** 组装参数 (15) **/
        Map<Object, Object> ArrayData = new HashMap<Object, Object>();
        ArrayData.put("member_id", member_id);
        ArrayData.put("terminal_id", terminal_id);
        ArrayData.put("card_no", acc_no);
        ArrayData.put("industry_type", "A1");//具体请看文档，按照自己所做业务来传这个参数
        ArrayData.put("trans_id", trans_id);
        ArrayData.put("trade_date", trade_date);
        ArrayData.put("product_type", "0");

        Map<Object, Object> ArrayData1 = new HashMap<Object, Object>();
        String XmlOrJson = "";
        if (data_type.equals("xml")) {
            ArrayData1.putAll(ArrayData);
            XmlOrJson = MapToXMLString.converter(ArrayData1, "data_content");
        } else {
            JSONObject jsonObjectFromMap = JSONObject.fromObject(ArrayData);
            XmlOrJson = jsonObjectFromMap.toString();
        }

        /** base64 编码 **/
        String base64str = null;
        try {
            base64str = SecurityUtil.Base64Encode(XmlOrJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /** rsa加密  **/
        // 商户私钥
        String pfxpath = URLDecoder.decode(xypfxpath);

//        File pfxfile = new ClassPathResource(pfxpath).getFile();
////        File pfxfile = new File(pfxpath);
//
//        if (!pfxfile.exists()) {
//            throw new RuntimeException("私钥文件不存在！");
//        }
        String pfxpwd = xycername;// 私钥密码

        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, pfxpwd);//加密数据

        /**============== http 请求==================== **/
        Map<String, String> params = Maps.newHashMap();
        params.put("member_id", member_id);
        params.put("terminal_id", terminal_id);
        params.put("data_type", "json");
        params.put("data_content", data_content);
        String PostString = HttpUtil.RequestForm(bankCardBinUrl, params);

        /** ================处理返回============= **/
        if (PostString.isEmpty()) {//判断参数是否为空
            throw new RuntimeException("返回数据为空");
        }

        String dataResult = PostString;
        if (data_type.equals("xml")) {
            dataResult = JXMConvertUtil.XmlConvertJson(PostString);
        }

        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(dataResult);
        return jsonObject;


    }

    /**
     * 新颜卡宾查询接口——v1   2019-01-10
     * @param acc_no 卡号
     * @return
     * @throws IOException
     */
    public com.alibaba.fastjson.JSONObject cardbin_v1(String acc_no) throws IOException {

        /** 1、 商户号 **/
        String member_id = xymember_id;
        /**2、终端号 **/
        String terminal_id = xyterminal_id;
        /** 3、 加密数据类型 **/
        String data_type = xydate_type;
        /** 4、加密数据 **/
//        String trans_id = "" + System.currentTimeMillis();// 商户订单号
        String trans_id = get32UUID();// 商户订单号
//        String trade_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());// 订单日期


        /** 组装参数 (15) **/
        Map<Object, Object> ArrayData = new HashMap<Object, Object>();
        ArrayData.put("member_id", member_id);
        ArrayData.put("terminal_id", terminal_id);
        ArrayData.put("trans_id", trans_id);//商户订单号
        ArrayData.put("acc_no", acc_no);//银行卡号

        System.out.println(trans_id);
        Map<Object, Object> ArrayData1 = new HashMap<Object, Object>();
        String XmlOrJson = "";
        if (data_type.equals("xml")) {
            ArrayData1.putAll(ArrayData);
            XmlOrJson = MapToXMLString.converter(ArrayData1, "data_content");
        } else {
            JSONObject jsonObjectFromMap = JSONObject.fromObject(ArrayData);
            XmlOrJson = jsonObjectFromMap.toString();
        }

        /** base64 编码 **/
        String base64str = null;
        try {
            base64str = SecurityUtil.Base64Encode(XmlOrJson);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /** rsa加密  **/
        // 商户私钥
        String pfxpath = URLDecoder.decode(xypfxpath);

        String pfxpwd = xycername;// 私钥密码

        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, pfxpath, pfxpwd);//加密数据

        /**============== http 请求==================== **/
        Map<String, String> params = Maps.newHashMap();
        params.put("member_id", member_id);
        params.put("terminal_id", terminal_id);
        params.put("data_type", "json");
        params.put("data_content", data_content);
        String PostString = HttpUtil.RequestForm(bankCardBinUrl, params);

        /** ================处理返回============= **/
        if (PostString.isEmpty()) {//判断参数是否为空
            throw new RuntimeException("返回数据为空");
        }

        String dataResult = PostString;
        if (data_type.equals("xml")) {
            dataResult = JXMConvertUtil.XmlConvertJson(PostString);
        }

        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(dataResult);
        return jsonObject;


    }

    /**
     * 获得32个长度的十六进制的UUID
     * @return UUID
     */
    private static String get32UUID(){
        UUID id=UUID.randomUUID();
        String[] idd=id.toString().split("-");
        return idd[0]+idd[1]+idd[2]+idd[3]+idd[4];
    }

}
