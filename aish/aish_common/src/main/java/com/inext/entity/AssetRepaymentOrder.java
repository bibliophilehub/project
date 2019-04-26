package com.inext.entity;

import com.inext.enums.PaymentTypeEnum;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 还款订单
 */
public class AssetRepaymentOrder implements Serializable {
    private static final long serialVersionUID = 8098342754628169918L;
    public static Map<Integer, Object> orderStatusMap = new LinkedHashMap<Integer, Object>();
    public static Map<Integer, Object> repaymentTypeMap = new LinkedHashMap<Integer, Object>();
    public static Map<String, Object> repaymentChannelMap = new LinkedHashMap<String, Object>();

    /**
     * 邮寄手机
     */
    public static final Integer REPAYMENT_TYPE_MAIL = 1;

    /**
     * 后台手动
     */
    public static final Integer REPAYMENT_TYPE_BACK = 2;

    /**
     * 主动支付
     */
    public static final Integer REPAYMENT_TYPE_INITIATIVE = 3;
    /**
     * 代扣
     */
    public static final Integer REPAYMENT_TYPE_DAIKOU = PaymentTypeEnum.YOP_DK.getValue();
    /**
     * 线下还款
     */
    public static final Integer UNDER_LINE_BACK = 5;
    //待还款
    public static final Integer STATUS_DHK = 6;
    //已逾期
    public static final Integer STATUS_YYQ = 7;
    //已还款
    public static final Integer STATUS_YHK = 8;

    /**
     * 代扣初始化
     */
    public static final Integer DK_PAY_INIT = 0;
    /**
     * 代扣处理中
     */
    public static final Integer DK_PAY_DOING = 1;
    /**
     * 代扣成功
     */
    public static final Integer DK_PAY_SUCCESS = 2;
    /**
     * 代扣失败
     */
    public static final Integer DK_PAY_FAILED = 3;

    /**
     * 合利宝
     */
    public static final String ASSET_REPAYMENT_ORDER = "1";

    /**
     * 宝付
     */
    public static final String REPAYMENT = "2";
    /**
     * 汇潮
     */
    public static final String HC_REPAYMENT = "3";
    /**
     * 后台还款-支付宝
     */
    public static final String ZHIFUBAO = "4";
    /**
     * 后台还款-银行卡主动
     */
    public static final String BANK_CARD_INITIATIVE = "5";
    /**
     * 后台还款-银行卡自动扣款
     */
    public static final String BANK_CARD_AUTOMATION = "6";
    /**
     * 后台还款-对公银行卡转账
     */
    public static final String PUBLIC_BANK_CARD = "7";
    /**
     * 后台还款-微信主动转账
     */
    public static final String WECHAT = "8";

    static {
        orderStatusMap.put(STATUS_DHK, "待还款");
        orderStatusMap.put(STATUS_YYQ, "已逾期");
        orderStatusMap.put(STATUS_YHK, "已还款");
    }

    static {
        repaymentChannelMap.put(ASSET_REPAYMENT_ORDER, "宝付");
        repaymentChannelMap.put(REPAYMENT, "宝付");
        repaymentChannelMap.put(HC_REPAYMENT, "汇潮");
        repaymentChannelMap.put(ZHIFUBAO, "支付宝");
        repaymentChannelMap.put(BANK_CARD_INITIATIVE, "银行卡对公");
        repaymentChannelMap.put(BANK_CARD_AUTOMATION, "银行卡自动扣款");
        repaymentChannelMap.put(PUBLIC_BANK_CARD, "对公银行卡转账");
        repaymentChannelMap.put(WECHAT, "微信主动转账");
    }

    static {
        repaymentTypeMap.put(REPAYMENT_TYPE_MAIL, "邮寄手机");
        repaymentTypeMap.put(REPAYMENT_TYPE_BACK, "后台手工");
        repaymentTypeMap.put(REPAYMENT_TYPE_INITIATIVE, "主动支付");
        repaymentTypeMap.put(REPAYMENT_TYPE_DAIKOU, "代扣");
        repaymentTypeMap.put(UNDER_LINE_BACK, "线下还款");
//        repaymentTypeMap.put(1, "支付宝");
//        repaymentTypeMap.put(2, "银行卡主动还款");
//        repaymentTypeMap.put(3, "银行卡自动扣款");
//        repaymentTypeMap.put(4, "对公银行卡转账");
    }

    @Id
    private Integer id;
    private Integer userId; //用户id
    private String userPhone;//用户手机号码
    private String userName;//用户姓名
    private Integer orderId;//借款订单id
    private BigDecimal moneyAmount; //借款金额，单位为分
    private BigDecimal penaltyAmount;//违约金，单位为分
    private BigDecimal planLateFee;//滞纳金，单位为分
    private BigDecimal repaymentAmount;//总还款金额（本金+服务费+滞纳金），单位为分
    private BigDecimal repaymentedAmount;//已还款金额，单位为分
    private Integer repaymentType;//还款方式（1、邮寄手机2、后台手工3、主动支付）
    private BigDecimal lateFeeApr;//滞纳金利率，单位为万分之几
    private BigDecimal ceditAmount;//总减免金额
    private Date creditRepaymentTime;// 放款时间
    private Date repaymentTime;//应还款时间
    private Integer period;//实际还款总期限
    private Date repaymentRealTime;//实际还款日期
    private Date lateFeeStartTime;//滞纳金计算开始时间
    private Date interestUpdateTime;//滞纳金更新时间
    private Integer lateDay;//逾期天数
    private Integer orderStatus;//还款订单状态：6待还款，7已逾期，8已还款
    private Integer dkPayStatus;//代扣支付状态:0:初始状态；1:处理中；2：成功；3：失败;
    private Integer orderRenewal;//订单是否续期9已续期
    private Integer orderMail;//是否寄出，10已寄出
    private String noOrder;//三方付款订单id
    private Date updateTime;//修改时间
    private String updateAccount;//修改人

    private String seqId;//流水号
    private String errMessage;//错误原因
    private Integer withholdId;//批次id

    @Transient
    private Date renewalRepaymentTime;//续期后应还款时间
    @Transient
    private String channelName;
    @Transient
    private BigDecimal shouldRepayAmount; //剩余应还金额
    @Transient
    private String score;
    @Transient
    private String detail;
    @Transient
    private Integer applyNew;
    @Transient
    private Integer paymentChannel;
    @Transient
    private String orderno;//还款订单号
    @Transient
    private String repaymentChannel;//还款渠道
    @Transient
    private String lateLevel;//逾期手别
    @Transient
    private String bankName;//银行名称
    @Transient
    private String cardCode;//绑定卡号
    @Transient
    private String hlOrderNo;
    @Transient
    private Date hlTime;
    @Transient
    private String hcOrderNo;
    @Transient
    private Date hcxTime;
    @Transient
    private Integer issue;
    @Transient
    private Date creatTime;//完成时间
    @Transient
    private String creater;//创建人
    @Transient
    private Integer type;//状态
    @Transient
    private String refundOrderNo;
    @Transient
    private String refundBank;
    @Transient
    private String refundCardNo;
    @Transient
    private String amount;
    @Transient
    private String remark;
    @Transient
    private String nickName;
    @Transient
    private Integer payType;
    @Transient
    private Integer refundChannel;
    @Transient
    private BigDecimal refundAmount;//累计退款金额
    @Transient
    private BigDecimal totalRepaymentAmount;//累计还款金额
    @Transient
    private BigDecimal reqAmt;

    public BigDecimal getReqAmt() {
        return reqAmt;
    }

    public void setReqAmt(BigDecimal reqAmt) {
        this.reqAmt = reqAmt;
    }

    private BigDecimal trueRepaymentMoney;//该还款单号对应的还款金额

    public BigDecimal getTrueRepaymentMoney() {
        return trueRepaymentMoney;
    }

    public void setTrueRepaymentMoney(BigDecimal trueRepaymentMoney) {
        this.trueRepaymentMoney = trueRepaymentMoney;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getTotalRepaymentAmount() {
        return totalRepaymentAmount;
    }

    public void setTotalRepaymentAmount(BigDecimal totalRepaymentAmount) {
        this.totalRepaymentAmount = totalRepaymentAmount;
    }

    public Date getRenewalRepaymentTime() {
        return renewalRepaymentTime;
    }

    public void setRenewalRepaymentTime(Date renewalRepaymentTime) {
        this.renewalRepaymentTime = renewalRepaymentTime;
    }

    public Integer getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(Integer refundChannel) {
        this.refundChannel = refundChannel;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    public String getRefundBank() {
        return refundBank;
    }

    public void setRefundBank(String refundBank) {
        this.refundBank = refundBank;
    }

    public String getRefundCardNo() {
        return refundCardNo;
    }

    public void setRefundCardNo(String refundCardNo) {
        this.refundCardNo = refundCardNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getHlOrderNo() {
        return hlOrderNo;
    }

    public void setHlOrderNo(String hlOrderNo) {
        this.hlOrderNo = hlOrderNo;
    }


    public String getHcOrderNo() {
        return hcOrderNo;
    }

    public void setHcOrderNo(String hcOrderNo) {
        this.hcOrderNo = hcOrderNo;
    }

    public Date getHlTime() {
        return hlTime;
    }

    public void setHlTime(Date hlTime) {
        this.hlTime = hlTime;
    }

    public Date getHcxTime() {
        return hcxTime;
    }

    public void setHcxTime(Date hcxTime) {
        this.hcxTime = hcxTime;
    }

    public Integer getIssue() {
        return issue;
    }

    public void setIssue(Integer issue) {
        this.issue = issue;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getRepaymentChannel() {
        return repaymentChannel;
    }

    public void setRepaymentChannel(String repaymentChannel) {
        this.repaymentChannel = repaymentChannel;
    }

    public String getDetail() {
        return detail;
    }

    public String getScore() {
        return score;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }


    public Date getCreditRepaymentTime() {
        return creditRepaymentTime;
    }

    public void setCreditRepaymentTime(Date creditRepaymentTime) {
        this.creditRepaymentTime = creditRepaymentTime;
    }

    public Date getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Date repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getRepaymentRealTime() {
        return repaymentRealTime;
    }

    public void setRepaymentRealTime(Date repaymentRealTime) {
        this.repaymentRealTime = repaymentRealTime;
    }

    public Date getLateFeeStartTime() {
        return lateFeeStartTime;
    }

    public void setLateFeeStartTime(Date lateFeeStartTime) {
        this.lateFeeStartTime = lateFeeStartTime;
    }

    public Date getInterestUpdateTime() {
        return interestUpdateTime;
    }

    public void setInterestUpdateTime(Date interestUpdateTime) {
        this.interestUpdateTime = interestUpdateTime;
    }

    public Integer getLateDay() {
        return lateDay;
    }

    public void setLateDay(Integer lateDay) {
        this.lateDay = lateDay;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderRenewal() {
        return orderRenewal;
    }

    public void setOrderRenewal(Integer orderRenewal) {
        this.orderRenewal = orderRenewal;
    }

    public Integer getOrderMail() {
        return orderMail;
    }

    public void setOrderMail(Integer orderMail) {
        this.orderMail = orderMail;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(String updateAccount) {
        this.updateAccount = updateAccount;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getPlanLateFee() {
        return planLateFee;
    }

    public void setPlanLateFee(BigDecimal planLateFee) {
        this.planLateFee = planLateFee;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public BigDecimal getRepaymentedAmount() {
        return repaymentedAmount;
    }

    public void setRepaymentedAmount(BigDecimal repaymentedAmount) {
        this.repaymentedAmount = repaymentedAmount;
    }

    public BigDecimal getLateFeeApr() {
        return lateFeeApr;
    }

    public void setLateFeeApr(BigDecimal lateFeeApr) {
        this.lateFeeApr = lateFeeApr;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getCeditAmount() {
        return ceditAmount;
    }

    public void setCeditAmount(BigDecimal ceditAmount) {
        this.ceditAmount = ceditAmount;
    }

    public BigDecimal getShouldRepayAmount() {
        return shouldRepayAmount;
    }

    public void setShouldRepayAmount(BigDecimal shouldRepayAmount) {
        this.shouldRepayAmount = shouldRepayAmount;
    }

    public String getNoOrder() {
        return noOrder;
    }

    public void setNoOrder(String noOrder) {
        this.noOrder = noOrder;
    }

    public Integer getDkPayStatus() {
        return dkPayStatus;
    }

    public void setDkPayStatus(Integer dkPayStatus) {
        this.dkPayStatus = dkPayStatus;
    }

    public Integer getApplyNew() {
        return applyNew;
    }

    public void setApplyNew(Integer applyNew) {
        this.applyNew = applyNew;
    }

    public Integer getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(Integer paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public Integer getWithholdId() {
        return withholdId;
    }

    public void setWithholdId(Integer withholdId) {
        this.withholdId = withholdId;
    }

    /**
     * 统计用的放款时间
     */
    @Transient
    private String statisticsLoanTime;

    @Transient
    private int isOld;

    public int getIsOld() {
        return isOld;
    }

    public void setIsOld(int isOld) {
        this.isOld = isOld;
    }

    public String getStatisticsLoanTime() {
        return statisticsLoanTime;
    }

    public void setStatisticsLoanTime(String statisticsLoanTime) {
        this.statisticsLoanTime = statisticsLoanTime;
    }

    public String getLateLevel() {
        return lateLevel;
    }

    public void setLateLevel(String lateLevel) {
        this.lateLevel = lateLevel;
    }
}
