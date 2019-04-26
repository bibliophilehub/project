package com.inext.constants;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public final static String HTTP = "http://";

    public final static String HTTPS = "https://";

    public final static String POST = "post";
    /**
     * /**
     * 当前页数
     */
    public final static String CURRENT_PAGE = "pageNum";


    /**
     * 每页显示多少条
     */
    public static final String PAGE_SIZE = "numPerPage";

    /**
     * 总页数
     */
    public static final String PAGED_TOTAL_PAGE = "totalPage";

    /**
     * 初始页数
     */
    public static final Integer INITIAL_CURRENT_PAGE = 1;

    /**
     * 初始数据记录条数
     */
    public static final Integer INITIAL_PAGE_SIZE = 10;

    /**
     * 前台用户session
     */
    public static final String JIEJIEKAN_WEB_USER = "JIEJIEKAN_WEB_USER";

    /**
     * 后台session名
     */
    public static final String JIEJIEKAN_BACK_USER = "JIEJIEKAN_BACK_USER";


    public static final String JIEJIEKAN_CHANNEL_USER = "JIEJIEKAN_CHANNEL_USER";

    /**
     * 默认IP
     */
    public static final String DEFAULT_IP = "127.0.0.1";


    /***/
    /**
     * 后台用户是否有效标识
     */
    public static final String BACK_USER_STATUS_USABLE = "1";
    public static final String BACK_USER_STATUS_UNUSABLE = "0";

    /***/
    /**
     * 渠道用户是否有效标识
     */
    public static final Integer CHANNEL_USER_STATUS_USABLE = 1;
    public static final Integer CHANNEL_USER_STATUS_UNUSABLE = 0;
    /**
     * 前台用户session
     */
    public static final String YIDAI_WEB_USER = "YIDAI_WEB_USER";
    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";
    public static final String DES_PUBLIC_ENCRYPT_KEY = "20150727"; //DES加密key
    public static final String DES_PRIVATE_ENCRYPT_KEY = "o0al4OaEWBzA1";


    /*********************************************前台************************************************/

    /**
     * 有效
     */
    public static final String STATUS_VALID = "1";
    /**
     * 无效
     */
    public static final String STATUS_INVALID = "0";


    /**
     * 有效
     */
    public final static Integer ENABLE_VALID = 1;

    /**
     * 无效
     */
    public final static Integer ENABLE_INVALID = 0;

    /**
     * flowType 统计流量类型
     */
    public static final String FLOW_JUMP = "6";//跳转商家h5页面
    public static final String FLOW_REGIST = "3";//h5推广注册
    public static final String FLOW_DETAIL = "4";//商品点击记录

    /**
     * 短信类型
     */
    public static final String VERIFY_CODE = "verify_code";//验证码类

    public static final String SMS_SEND_SUCC = "000000";

    /***
     * App注册
     */
    public static final String APP_REGISTER = "App";

    /***
     * App注册
     */
    public static final String APP_MOBILE = "H5";

    // 应用类型
    public static final String APP_TYPE_ANDROID = "0";
    public static final String APP_TYPE_IOS = "1";

    // IOS 是否审核通过
    public static final Integer APP_EXAMINE_NO = 0;
    public static final Integer APP_EXAMINE_YES = 1;

    //滚动实时资讯信息模板
    public static final String NOTICMSG = "尾号为{0}的用户，成功在{1}借款{2}元。";

    public static final int dayNum = 5;
    public static final int num = 1000;
    public static final String dayKey = "sms:day:checkSendNumCode:";
    public static final String checkKey = "sms:num:checkCode:";
    public static final String numKey = "sms:num:checkSendnumCode:";
    public static final String smsCodeKey = "sms:code:";
    public static final String imageCode = "sms:image:code:";


    public static final Long KEYTIME = 300L;

    public static final String USER_NEW_01 = "0"; //联合登陆新用户

    public static final String USER_OLD_02 = "1"; //联合登陆老用户

    public static final String USER_BLACK_01 = "0"; //黑名单否

    public static final String USER_BLACK_02 = "1"; //黑名单是
    //33个手机号码前缀
    public static final String[] phoneNum = new String[]{
            "139", "138", "137", "136", "135", "134", "178", "170", "188", "187", "183", "182", "159", "158", "157", "152", "150", "147", "186", "185", "170", "156", "155", "130", "131", "132", "189", "180", "170", "153", "199", "198", "166"
    };
    //图片路径
    public static final String IMGURL = "/files/userheadImg/";
    //图片后缀
    public static final String IMGHZ = ".jpg";

    public static final long IMGNUM = 40;

    public static final String SYJCONFIGCODE = "syjconfig:app";

    //获取系统配置有效时间1小时
    public static final Long SYJCONFIGCODETIME = 60 * 30l;

    public static final String USERLOGINERRORNUM = "userLogin:error:";

    public static final String USERLOGINFREEZE = "userLogin:freeze:";

    public static final String SENDSMSLOGCODE = "sms:sendlog:";

    public static final String SENDSMSLOGERRORCODE = "sms:senderror";

    public static final String SIGN = "【花小侠】";
    /**
     * 上传附件地址
     */
    public static final String FILEPATH = "files";
    //该值会在Spring上下文初始完成时由InitializingBeanServiceImpl初始化
    public static Map<String, Map<String, String>> BACK_CONFIG_PARAMS_MAP = null;

    //快递
    public static   Map  LOGISTICS_COMPANY=new HashMap(){{
        put("1","顺丰快递");
        put("2","申通快递");
        put("3","圆通快递");
        put("4","EMS快递");
        put("5","韵达快递");
        put("6","中通快递");
        put("7","天天快递");
        put("8","德邦快递");
        put("9","百世汇通快递");
        put("10","万象快递");
        put("11","中国邮政快递");
        put("12","其他快递");
    }};
    
    public static String CHANNEL_51_KABAO = "51卡宝";

    public static String SMSTOKEN_SALT = "as@*gx1.6kj";//发短信接口token加密的盐


}
