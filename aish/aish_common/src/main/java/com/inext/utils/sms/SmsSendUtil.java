package com.inext.utils.sms;

import com.alibaba.fastjson.JSON;
import com.inext.constants.Constants;
import com.inext.entity.BackConfigParams;
import com.inext.utils.MD5Utils;
import com.inext.utils.OkHttpUtils;
import com.inext.utils.StringUtils;
import com.inext.utils.sms.request.SmsSendRequest;
import com.inext.utils.sms.util.ChuangLanSmsUtil;
import com.inext.utils.sms.util.HttpSender;
import com.inext.utils.sms.util.YunXinSmsUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 短信工具类
 *
 * @author FUMENG
 * @date 2017年7月26日 下午9:39:26
 */
public class SmsSendUtil {

    public static final Logger logger = LoggerFactory.getLogger(SmsSendUtil.class);

    public static final String charset = "utf-8";
    // 用户平台API账号(非登录账号,示例:N1234567) 帮助借
    public static String project_name = "回购";
    public static String account = "N5767540";
    // 用户平台API密码(非登录密码)
    public static String pswd = "itJyBqs6Qo46b0";

    public static Map<String, String> indexHome = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.SMS);

    public static Map<String, String> SMS_TIANCHANG = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.SMS_TIANCHANG);
    //財焱短信
    public static Map<String, String> SMS_CAIYAN = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.SMS_CAI_YAN);
    /**
     * 財焱短信相关
     */
    public static  String url="";
    public static  String ver_code_account="";
    public static  String ver_code_possword="";
    public static  String inform_account="";
    public static  String inform_possword="";
    public static  String cuishou_account="";
    public static  String cuishou_possword="";

    public static String sendSmsDiy(String phone, String msg) {
        return sendSmsDiyCLandYX(phone, msg);
    }

    /**
     * 云信优先，速盾备用
     * @param phone
     * @param msg
     * @return
     */
    public static String sendSmsDiyYXandCL(String phone, String msg) {
        if (indexHome != null && indexHome.size() > 0) {
            String yxUrl = indexHome.get("SMS_YX_SEND_URL");
            String yxAccount = indexHome.get("SMS_YX_ACCOUNT");
            String yxPwd = indexHome.get("SMS_YX_PSWD");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("userCode", yxAccount));
            nvps.add(new BasicNameValuePair("userPass", yxPwd));
            nvps.add(new BasicNameValuePair("DesNo", phone));
            nvps.add(new BasicNameValuePair("Msg", msg+ Constants.SIGN));
            nvps.add(new BasicNameValuePair("Channel", "0"));
            String response = YunXinSmsUtil.httpPost(yxUrl, nvps);
            if (null == response) {
                //云信短信发送失败则用速盾短信发送
                account = indexHome.get("SMS_ACCOUNT");
                pswd = indexHome.get("SMS_PSWD");
                String smsSingleRequestServerUrl = indexHome.get("SMS_SEND_URL");
                // 状态报告
                boolean needstatus = true;
                boolean encrypt = true;
                try {
                    response = HttpSender.send(smsSingleRequestServerUrl, account, pswd, phone, msg, needstatus, "", "", "json", encrypt);
                }catch (Exception e){
                    logger.error("SmsSendUtil >>> sendSmsDiyYXandCL 短信验证码发送异常:{}",e.getMessage());
                }
            }
            return response != null ? "0" : "";
        }
        return "";
    }

    /**
     * 財焱优先，速盾备用短信类
     * @param phone
     * @param msg
     * @return
     */
    public static String sendSmsDiyCLandYX(String phone, String msg) {
        String response = null;

        if (indexHome != null && indexHome.size() > 0) {
            account = indexHome.get("SMS_ACCOUNT");
            pswd = indexHome.get("SMS_PSWD");
        }
        String smsSingleRequestServerUrl = indexHome.get("SMS_SEND_URL");
        // 状态报告
        boolean needstatus = true;
        boolean encrypt = true;// 密码使用时间戳加密
        try {
            response = HttpSender.send(smsSingleRequestServerUrl, account, pswd, phone, msg, needstatus, "", "", "json", encrypt);
        } catch (Exception e) {
            logger.error("SmsSendUtil >>> sendSmsDiyCLandYX 短信验证码发送异常:{}", e.getMessage());
        }

        if(null==response) {
            url = SMS_CAIYAN.get("SMS_CY_REQUEST_URL");
            ver_code_account = SMS_CAIYAN.get("SMS_CY_VER_CODE_ACCOUNT");
            ver_code_possword = SMS_CAIYAN.get("SMS_CY_VER_CODE_POSSWORD");
            StringBuffer sb = new StringBuffer(2000);
            sb.append(url);
            sb.append("?uid=").append(ver_code_account);
            sb.append("&pw=").append(ver_code_possword);
            sb.append("&mb=").append(phone);
            sb.append("&ms=").append(Constants.SIGN + msg);
            logger.info("SmsSendUtil >>> sendSMSByCaiYan 短信内容" + sb.toString());
            try {
                response = OkHttpUtils.get(sb.toString());
            } catch (Exception e) {
                logger.error("SmsSendUtil >>> sendSmsDiyCLandYXByRepaymentRemind 短信验证码发送异常:{}", e.getMessage());

            }
        }
        return response != null ? "0" : "";
    }

    /**
     * 財焱优先，速盾备用--催收类短信
     * @param phone
     * @param msg
     * @return
     */
    public static String sendSmsDiyCLandCollection(String phone, String msg) {
        String response = null;
        url = SMS_CAIYAN.get("SMS_CY_REQUEST_URL");
        cuishou_account = SMS_CAIYAN.get("SMS_CY_CUISHOU_ACCOUNT");
        cuishou_possword = SMS_CAIYAN.get("SMS_CY_CUISHOU_POSSWORD");
        StringBuffer sb = new StringBuffer(2000);
        sb.append(url);
        sb.append("?uid=").append(cuishou_account);
        sb.append("&pw=").append(cuishou_possword);
        sb.append("&mb=").append(phone);
        sb.append("&ms=").append(Constants.SIGN + msg);
        logger.info("SmsSendUtil >>> sendSMSByCaiYan 短信内容" + sb.toString());
        try {
            response = OkHttpUtils.get(sb.toString());
        } catch (Exception e) {
            logger.error("SmsSendUtil >>> sendSmsDiyCLandYXByRepaymentRemind 短信验证码发送异常:{}", e.getMessage());

        }
        if(null==response) {
            if (indexHome != null && indexHome.size() > 0) {
                account = indexHome.get("SMS_ACCOUNT");
                pswd = indexHome.get("SMS_PSWD");
            }
            String smsSingleRequestServerUrl = indexHome.get("SMS_SEND_URL");
            // 状态报告
            boolean needstatus = true;
            boolean encrypt = true;// 密码使用时间戳加密
            try {
                response = HttpSender.send(smsSingleRequestServerUrl, account, pswd, phone, msg, needstatus, "", "", "json", encrypt);
            } catch (Exception e) {
                logger.error("SmsSendUtil >>> sendSmsDiyCLandYX 短信验证码发送异常:{}", e.getMessage());
            }
        }
        return response != null ? "0" : "";
    }


    /** -----------------
     *
     */
    /**
     * 营销短信
     */
    public static String sendSmsZL(String phone, String msg) {
        if (indexHome != null && indexHome.size() > 0) {
            project_name = indexHome.get("PROJECT_NAME");
            account = "M4004406";
            pswd = "Z7tdQwj2r8ceae";
        }
        // 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
        String smsSingleRequestServerUrl = indexHome.get("SMS_SEND_URL");
        // 状态报告
        boolean needstatus = true;
        boolean encrypt = true;// 密码使用时间戳加密
        String response = null;
        try {
            response = HttpSender.send(smsSingleRequestServerUrl, account, pswd, phone, msg, needstatus, "", "", "json", encrypt);
        }catch (Exception e){
            logger.error("SmsSendUtil >>> sendSmsZL 短信验证码发送异常:{}",e.getMessage());
        }
        if (null == response) {
//            //创蓝短信发送失败则用云信短信发送
//            String yxUrl = "http://smssh1.253.com/msg/send/json";
//            String yxAccount = "M4004406";
//            String yxPwd = "Z7tdQwj2r8ceae";
//
//            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//            nvps.add(new BasicNameValuePair("userCode", yxAccount));
//            nvps.add(new BasicNameValuePair("userPass", yxPwd));
//            nvps.add(new BasicNameValuePair("DesNo", phone));
//            nvps.add(new BasicNameValuePair("Msg", msg));
//            nvps.add(new BasicNameValuePair("Channel", "0"));



        }
        return response != null ? "0" : "";
    }

    /**
     * 天畅短信
     *
     * @param userPhone
     * @param message
     * @return
     */
    public static boolean sendByTianChang(String userPhone, String message) {

        // 必须的验证参数
        // 短信相关的必须参数

        String extendCode = SMS_TIANCHANG.get("extendCode");
        String username = SMS_TIANCHANG.get("username");
        String password = SMS_TIANCHANG.get("password");
        String url = SMS_TIANCHANG.get("url");

        // 装配GET所需的参数
        StringBuffer sb = new StringBuffer(2000);
        try {
            sb.append(url);
            sb.append("?dc=15"); // 表明发送的是中文

            sb.append("&sm=").append(URLEncoder.encode(message, "utf8"));

            sb.append("&da=").append(userPhone);
            sb.append("&sa=").append(extendCode);
            sb.append("&un=").append(username);
            // 这里采用明码验证方式，不安全；签名方式请参看SendWithSign
            sb.append("&pw=").append(password);
            // 表示短信内容为 urlencode+utf8
            sb.append("&tf=3");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String rest = OkHttpUtils.get(sb.toString());


        return StringUtils.indexOf(rest, "id=") != -1;
    }

//    /**
//     * 速盾优先，云信备用-------催收提醒短信通道
//     * @param phone
//     * @param msg
//     * @return
//     */
//    public static String sendSmsDiyCLandYXByRepaymentRemindSD(String phone, String msg) {
//        String response = null;
//        if (indexHome != null && indexHome.size() > 0) {
//            account = indexHome.get("SMS_CUISHOU_ACCOUNT");
//            pswd = indexHome.get("SMS_CUISHOU_PSWD");
//        }
//        // 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//        String smsSingleRequestServerUrl = indexHome.get("SMS_CUISHOU_SEND_URL");
//        //http://host:port/sms?action=send&account=账号&password=密码&mobile=15023239810&content=内容&extno=1069012345&rt=json
//        try {
//            response = HttpSender.postSend(smsSingleRequestServerUrl, "send", account, pswd, phone, msg, "", "json");
//        }catch (Exception e){
//            logger.error("SmsSendUtil >>> sendSmsDiyCLandYXByRepaymentRemind 短信验证码发送异常:{}",e.getMessage());
//        }
//        logger.info("SmsSendUtil.response:" + response);
//        if (null == response) {
////            //创蓝短信发送失败则用云信短信发送
////            String yxUrl = indexHome.get("SMS_YX_SEND_URL");
////            String yxAccount = indexHome.get("SMS_YX_ACCOUNT");
////            String yxPwd = indexHome.get("SMS_YX_PSWD");
////
////            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
////            nvps.add(new BasicNameValuePair("userCode", yxAccount));
////            nvps.add(new BasicNameValuePair("userPass", yxPwd));
////            nvps.add(new BasicNameValuePair("DesNo", phone));
////            nvps.add(new BasicNameValuePair("Msg", msg+ Constants.SIGN));
////            nvps.add(new BasicNameValuePair("Channel", "0"));
//
//            url= SMS_CAIYAN.get("SMS_CY_REQUEST_URL");
//            inform_account= SMS_CAIYAN.get("SMS_CY_INFORM_ACCOUNT");
//            inform_possword= SMS_CAIYAN.get("SMS_CY_INFORM_POSSWORD");
//
//            StringBuffer sb = new StringBuffer(2000);
//            sb.append(url);
//            sb.append("?uid=").append(inform_account);
//            sb.append("&pw=").append(inform_possword);
//            sb.append("&mb=").append(phone);
//            sb.append("&ms=").append(Constants.SIGN+msg);
//            logger.info("SmsSendUtil >>> sendSMSByCaiYan 短信内容"+sb.toString());
//            try {
//                response = OkHttpUtils.get(sb.toString());
//
//            }catch (Exception e){
//                logger.error("SmsSendUtil >>> sendSmsDiyCLandYXByRepaymentRemind 短信验证码发送异常:{}",e.getMessage());
//
//            }
//            if(response==null || response==""){
//                if (indexHome != null && indexHome.size() > 0) {
//                    account = indexHome.get("SMS_CUISHOU_ACCOUNT");
//                    pswd = indexHome.get("SMS_CUISHOU_PSWD");
//                }
//                String smsSingleRequestServerUrl = indexHome.get("SMS_CUISHOU_SEND_URL");
//                //http://host:port/sms?action=send&account=账号&password=密码&mobile=15023239810&content=内容&extno=1069012345&rt=json
//                try {
//                    response = HttpSender.postSend(smsSingleRequestServerUrl, "send", account, pswd, phone, msg, "", "json");
//                }catch (Exception e){
//                    logger.error("SmsSendUtil >>> sendSmsDiyCLandYXByRepaymentRemind 短信验证码发送异常:{}",e.getMessage());
//                }
//                logger.info("SmsSendUtil.response:" + response);
//            }
//
//
//        }
//        return response != null ? "0" : "";
//    }

    /**
     * 財焱优先，速盾备用-------通知类短信通道
     * @param phone
     * @param msg
     * @return
     */
    public static String sendSmsDiyCLandYXByRepaymentRemind(String phone, String msg) {
        String response = null;
        url = SMS_CAIYAN.get("SMS_CY_REQUEST_URL");
        inform_account = SMS_CAIYAN.get("SMS_CY_INFORM_ACCOUNT");
        inform_possword = SMS_CAIYAN.get("SMS_CY_INFORM_POSSWORD");

        StringBuffer sb = new StringBuffer(2000);
        sb.append(url);
        sb.append("?uid=").append(inform_account);
        sb.append("&pw=").append(inform_possword);
        sb.append("&mb=").append(phone);
        sb.append("&ms=").append(Constants.SIGN + msg);
        logger.info("SmsSendUtil >>> sendSMSByCaiYan 短信内容" + sb.toString());
        try {
            response = OkHttpUtils.get(sb.toString());

        } catch (Exception e) {
            logger.error("SmsSendUtil >>> sendSmsDiyCLandYXByRepaymentRemind 短信验证码发送异常:{}", e.getMessage());

        }
        if (response == null || response == "") {
            if (indexHome != null && indexHome.size() > 0) {
                account = indexHome.get("SMS_CUISHOU_ACCOUNT");
                pswd = indexHome.get("SMS_CUISHOU_PSWD");
            }
            // 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
            String smsSingleRequestServerUrl = indexHome.get("SMS_CUISHOU_SEND_URL");
            //http://host:port/sms?action=send&account=账号&password=密码&mobile=15023239810&content=内容&extno=1069012345&rt=json
            try {
                response = HttpSender.postSend(smsSingleRequestServerUrl, "send", account, pswd, phone, msg, "", "json");
            } catch (Exception e) {
                logger.error("SmsSendUtil >>> sendSmsDiyCLandYXByRepaymentRemind 短信验证码发送异常:{}", e.getMessage());
            }
            logger.info("SmsSendUtil.response:" + response);
        }
        return response != null ? "0" : "";
    }

    /**
     * 財焱验证码短信
     * @param userPhone
     * @param message
     * @return
     */
//    public static String sendSMSByCaiYan(String userPhone, String message) {
//        // 必须的验证参数
//        // 短信相关的必须参数
//
//        if(SMS_CAIYAN !=null && SMS_CAIYAN.size()>0){
//            url= SMS_CAIYAN.get("SMS_CY_REQUEST_URL");
//            ver_code_account= SMS_CAIYAN.get("SMS_CY_VER_CODE_ACCOUNT");
//            ver_code_possword= SMS_CAIYAN.get("SMS_CY_VER_CODE_POSSWORD");
//        }
//
//        String pwd=ver_code_possword;
//        logger.info("pwd======:"+pwd);
//
//
//        StringBuffer sb = new StringBuffer(2000);
//            sb.append(url);
//            sb.append("?uid=").append(ver_code_account);
//            sb.append("&pw=").append(pwd);
//            sb.append("&mb=").append(userPhone);
//            sb.append("&ms=").append(message);
//            logger.info("SmsSendUtil >>> sendSMSByCaiYan 短信内容"+sb.toString());
//        String rest = OkHttpUtils.get(sb.toString());
//        logger.info(rest);
//        boolean status=rest.contains("-1");
//        if(status)
//        {
//            if (indexHome != null && indexHome.size() > 0) {
//                account = indexHome.get("SMS_ACCOUNT");
//                pswd = indexHome.get("SMS_PSWD");
//            }
//            // 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
//            String smsSingleRequestServerUrl = indexHome.get("SMS_SEND_URL");
//            // 状态报告
//            boolean needstatus = true;
//            boolean encrypt = true;// 密码使用时间戳加密
//            String response = null;
//            try {
//                response = HttpSender.send(smsSingleRequestServerUrl, account, pswd, userPhone, message, needstatus, "", "", "json", encrypt);
//            }catch (Exception e){
//                logger.error("SmsSendUtil >>> sendSmsDiyCLandYX 短信验证码发送异常:{}",e.getMessage());
//            }
//        }
//
//
//        return response != null ? "0" : "";
//    }

    /**
     * 財焱催收类短信
     * @throws Exception
     */
    public static boolean sendMarketingSMSByCaiYan(String userPhone, String message) {
        // 必须的验证参数
        // 短信相关的必须参数

        if(SMS_CAIYAN !=null && SMS_CAIYAN.size()>0){
            url= SMS_CAIYAN.get("SMS_CY_REQUEST_URL");
            inform_account= SMS_CAIYAN.get("SMS_CY_INFORM_ACCOUNT");
            inform_possword= SMS_CAIYAN.get("SMS_CY_INFORM_POSSWORD");
        }

        StringBuffer sb = new StringBuffer(2000);
        try {
            sb.append(url);
            sb.append("?uid=").append(inform_account);
            sb.append("&pw=").append(inform_possword);
            sb.append("&mb=").append(userPhone);
            sb.append("&ms=").append(URLEncoder.encode(message, "utf8"));
            logger.info("SmsSendUtil >>> sendSMSByCaiYan 短信内容"+sb.toString());
        } catch (UnsupportedEncodingException e) {
            logger.info("SmsSendUtil >>> sendSMSByCaiYan 短信发送异常"+e.getMessage());
        }

        String rest = OkHttpUtils.get(sb.toString());

        return StringUtils.indexOf(rest, "rest=") != -1;

    }
    public static void main(String[] args) throws Exception {
        // 必须的验证参数
        String username = "1822";
        String password = "052149";
        String url = "http://47.96.96.236:18002/send.do";

        // 短信相关的必须参数
        String mobile = "13636458639";
        String extendCode = "1299";
        String message = "【花小侠】本次验证码为0718";

        // 装配GET所需的参数
        StringBuilder sb = new StringBuilder(2000);
        sb.append(url);
        sb.append("?dc=15"); // 表明发送的是中文
        sb.append("&sm=").append(URLEncoder.encode(message, "utf8"));
        sb.append("&da=").append(mobile);
        sb.append("&sa=").append(extendCode);
        sb.append("&un=").append(username);
        sb.append("&pw=").append(password); // 这里采用明码验证方式，不安全；签名方式请参看SendWithSign
        sb.append("&tf=3"); // 表示短信内容为 urlencode+utf8

        String request = sb.toString();
        System.err.println("url " + url);
        String rest = OkHttpUtils.get(request);
        System.err.println("rest " + rest);
    }
}
