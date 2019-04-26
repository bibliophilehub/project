package com.inext.enums;

/**
 * @Author Administrator
 * @Create 2017-07-07 20:38
 **/
public enum ActivityResultEnum {

    /**
     * SUCCESS
     */
    SUCCESS("200", "SUCCESS"),
    LOGIN_SUCCESS("200", "登录成功，获得一次机会抽奖"),
    FLOWSTATIS_SUCCESS("200", "三次借款申请成功，又获得一次机会抽奖"),
    SHARE_SUCCESS("200", "分享成功，又获得一次机会抽奖"),
    FLOWSTATIS_HIS_WARN("200", "您的借款申请奖励已发放"),
    SHARE_HIS_WARN("200", "您好!<br/>该平台分享奖励已发放<br/>可通过分享其他社交平台获取奖励"),

    /**
     * ERROR
     */
    PARAMS_ERROR("500", "缺失参数"),
    UNKONW_ERROR("500", "系统繁忙,请稍后再试"),
    FLOWSTATIS_ERROR("500", "对不起，您当前未完成了三次借款申请"),
    ACTIVITY_INIT_ERROR("500", "活动已下架"),
    ACTIVITY_END_ERROR("500", "当日活动已结束，请明日再来!"),
    ACTIVITY_UNREGISTER_ERROR("500", "您好，请先登录"),
    ACTIVITY_UN_REGISTER_ERROR("500", "未注册用户"),
    ACTIVITY_FIRSTPLAY_ERROR("500", "不过瘾？完成三次借款申请，可额外获得一次抽奖机会！"),
    ACTIVITY_NO_PLAYCOUNT_ERROR("500", "今天的机会已用完，咱明儿见!"),
    ACTIVITY_ZERO_BONUS_ERROR("500", "暂无剩余奖励，默认为谢谢参与"),
    ACTIVITY_NOWIN_ERROR("500", "暂无奖品"),

    /**
     * 日志记录-类型
     */
    LOTTERY_REWARD_TYPE(0, "抽奖消费"),
    LOGIN_REWARD_TYPE(1, "登录成功，获得一次机会抽奖"),
    FLOWSTATIS_REWARD_TYPE(2, "三次借款申请成功，又获得一次机会抽奖"),
    SHARE_REWARD_TYPE(3, "分享活动，又获得一次机会抽奖"),
    BILL_REWARD_TYPE(4, "记满3笔账单信息，又获得一次机会抽奖"),;

    private String code;
    private Integer type;
    private String msg;

    ActivityResultEnum() {
    }

    ActivityResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ActivityResultEnum(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
