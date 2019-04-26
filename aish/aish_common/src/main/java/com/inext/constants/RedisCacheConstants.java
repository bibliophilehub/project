package com.inext.constants;

/**
 * Created by 李思鸽 on 2017/4/20 0020.
 */
public class RedisCacheConstants {


    // 缓存失效时间默认600秒 10分钟
    public final static Long TIMEOUT_OUT_SECOND_DEFAULT = 180L;

    // 渠道列表缓存key
    public final static String CHANNEL_LIST = "channelList";


    // 短信验证码是否处于保护期內不允许重复发送的缓存key前缀
    public final static String SMS_CODE_IS_PROTECT_LIMIT_PREFIX = "sms:code:is:protect:limit:";

    // 短信验证码相同手机号码一天发3次
    public final static String SMS_MOBILE = "sms:mobile:";
    //public final static String SMS_CODE_IS_PROTECT_LIMIT_NUM = "sms:code:is:protect:num:";
    public final static String SMS_IP = "sms:ip:";
    public final static String SMS_CODE_IS_PROTECT_IP_NUM = "sms:code:is:ip:num:";
    //public final static String SMS_CODE_IS_PROTECT_SESSION_NUM = "sms:code:is:session:num:";

    public final static Long SMS_IP_MAX_VALUE = 1000L;//相同ip发送短信验证码，最大数量
    public final static Long SMS_IP_DAY_MAX_VALUE = 10L;//10分钟5次，最高频率
    public final static Long SMS_MOBILE_DAY_MAX_VALUE = 8L;//手机号1天发送的最大次数

    public final static String TIME_DAY = "day:";

    public final static String DIS_ALLOW = "disallow:";

    public final static String TIME_MINUTE = "minute:";


    // 短信验证码登录前缀
    public final static String SMS_CODE_VERIFICATION_LOGIN_PREFIX = "sms:code:verification:login:prefix:";


    // 短信验证码注册前缀
    public final static String SMS_CODE_REGISTER_PREFIX = "sms:code:register:prefix:";

    // 琥珀数据授权token前缀
    public final static String HUPOSHUJU_TOKEN_PREFIX = "huposhuju:token:prefix:";


    /**
     * 缓存安卓前台首页
     */
    public static final String CACHE_INDEX_ANDROID = "cache:app:index:android";

    /**
     * 缓存IOS前台首页
     */
    public static final String CACHE_INDEX_IOS = "cache:app:index:ios";

    /**
     * 默认首页缓存
     */
    public static final String CACHE_INDEX_DEFAULT = "cache:app:index:default";

    // APP端商户产品列表缓存前缀
    public static final String APP_MERCHANT_PRODUCT_LIST = "app:merchant:product:list:";

    // 移动浏览器端商户产品列表缓存前缀
    public static final String MOBILE_MERCHANT_PRODUCT_LIST = "mobile:merchant:product:list:";

    //缓存首页实时资讯信息
    public static final String NOTICE_LIST = "notice_list";

    public static final String TIME = "time:";

    public static final String YJTIME = "yjtime:";

    public static final String YJ = "yj:";

    public static final String TOKEN = "token";

    public static final String TOKENCODE = "app:qhg:token:";

    public static final Long TOKENTIME = 30 * 24 * 60 * 60l;
}
