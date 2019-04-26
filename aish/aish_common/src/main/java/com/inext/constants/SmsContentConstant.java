package com.inext.constants;

/**
 * 错误信息常类
 *
 * @author user
 */
public class SmsContentConstant {

    //明天履约期
    public final static String TOMORROW_TREM =  "请留意您的订单，请尽快寄出手机，或（去花小侠APP内）取消订单。";
    //今天履约期
    public final static String TODAY_TREM = "请留意您的订单，请尽快寄出手机，或（去花小侠APP内）取消订单。";;
    //履约期已过
    public final static String DEFULT_TREM = "请留意您的订单，请尽快寄出手机，或（去花小侠APP内）取消订单。";
    //注册
    //public final static String REGISTER = "您已成功注册花小侠，请完成所有步骤，订单才能成功。";
    //取消订单
    //public final static String CANCEL_ORDER = "您申请的手机回购订单已取消，期待您下次手机回购之旅。";
    public final static String CANCEL_ORDER = "您的借款订单已完成。";
    /**
     * 预付款短信
     * @param content1  截止时间
     * @param content2  尾号
     * @param content3  天数
     * @return
     */
    public static String getContent(String content1,String content2,Integer content3) {
        String result = "您申请的订单已审核通过，款已于" +content1+ "打至您绑定的尾号" +
                content2 + "银行卡中，请在规定期内尽快处理订单。";
        return result;
    }

    /**
     * 短信登陆验证
     * @param code 验证码
     * @return
     */
    public static String getLoginCode(String code){
        //return "本次验证码为"+code;
        //return "客官，您的验证码为："+code+"，5分钟内有效，如非本人操作，请致电400-850-8033。";
        return "验证码为："+code+",请于5分钟内填写。如非本人操作，请忽略本短信。";
    }

    /**
     * 短信登陆验证
     * @param code 验证码
     * @return
     */
    public static String getLoginVerificationCode(String code){
        //return "尊敬的用户，本次验证码为"+code+"请勿泄露，5分钟内有效！";
        //return "客官，您的验证码为："+code+"，5分钟内有效，如非本人操作，请致电400-850-8033。";
        return "验证码为："+code+",请于5分钟内填写。如非本人操作，请忽略本短信。";
    }

    /**
     * 找回交易密码
     * @param code 验证码
     * @return
     */
    public static  String getRetrieveTradingPassword(String code){
        return "验证码"+code+"有效时间5分钟，您正在找回花小侠账户交易密码，请勿将验证码告诉他人。";
    }

    /**
     *  打款短信
     * @param money 金额
     * @return
     */
    public static String getMakeMoney(String money){
        return "您收到花小侠打款"+money+"元,预计很快到账，请注意查收！";
    }

    /**
     *  累计、提升额度
     * @param money 累计金额
     * @param increment 提升额度
     * @return
     */
    public static String getLiftingAmount(String money,String increment){
        return "恭喜您已经正常还款累计"+money+"元，获得提额："+increment+"元，请继续保持良好的还款习惯！";
    }

    /**
     *  风控提额
     * @param money 金额
     * @return
     */
    public static String getRiskMoney(String money){
        return "通过风控运行，您的额度更改为："+money+"元，请保持良好的还款习惯！";
    }

    /**
     * 注册成功短信
     * @param user 用户
     * @return
     */
    public static String getRegisterSuccess (String user){
        return user+"您已成功注册花小侠！请尽快到APP完善资料，获取您的额度。";
    }

    /**
     * 明日逾期短信提示
     * @param name 姓名
     * @param money 应还款金额
     * @param bankCard 银行卡尾号
     * @return
     */
    public static String getTomorrowOverdue (String name,String money,String bankCard){
        return "尊敬的"+name+"，您的"+money+"元借款明日到期，请至APP还款，若到期未还款，" +
                "平台将自动扣款，请确保尾号"+bankCard+"银行卡资金充足。如已还款，请忽略。退订回T";
    }

    /**
     * 今日逾期短信提示
     * @param name 姓名
     * @param money 应还款金额
     * @param bankCard 银行卡尾号
     * @return
     */
    public static String getTodayOverdue (String name,String money,String bankCard){
        return "尊敬的"+name+"，您的"+money+"元借款今日到期，请至APP还款，若到期未还款，" +
                "平台将自动扣款，请确保尾号"+bankCard+"银行卡资金充足。如已还款，请忽略。退订回T";
    }

    /**
     * 逾期短信提示
     * @param name 姓名
     * @param money 应还款金额
     * @param lateDay 逾期天数
     * @return
     */
    public static String getOverdue (String name,String money,String lateDay){
        return "尊敬的"+name+"，您的"+money+"元账单已逾*期"+lateDay+"，请及时至APP还款。如已还款，请忽略。";
    }

    public static String REGISTER(String name){
        //return "尊敬的用户"+name+"，您已成功注册花小侠，请完成所有步骤，订单才能成功。";
        return "您已注册成功！恭喜您获得免审金额，仅差一步，认证即可提现。";
    }
}
