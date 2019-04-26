package com.stylefeng.guns.modular.system.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BizCollectionOrder implements  Serializable{

    /**
     * <p>
     * 催收订单表
     * </p>
     *
     * @author stylefeng
     * @since 2018-07-16
     */
//@Data


        private static final long serialVersionUID = 1L;

        private Integer id;
        /**
         * 还款id
         */
        private Integer repaymentId;
        /**
         * 用户id
         */
        private Integer userId;
        /**
         * 用户手机号码
         */
        private String userPhone;
        /**
         * 用户姓名
         */
        private String userName;
        /**
         * 借款订单id
         */
        private Integer orderId;
        /**
         * 借款本金
         */
        private BigDecimal moneyAmount;
        /**
         * 违约金（头 利息）
         */
        private BigDecimal penaltyAmount;
        /**
         * 滞纳金（逾期费）
         */
        private BigDecimal planLateFee;
        /**
         * 总减免金额
         */
        private BigDecimal ceditAmount;
        /**
         * 应还总还款金额（本金+违约金+滞纳金）
         */
        private BigDecimal repaymentAmount;
        /**
         * 已还款金额
         */
        private BigDecimal repaymentedAmount;
        /**
         * 还款方式（1、邮寄手机2、后台手工3、主动支付））
         */
        private Integer repaymentType;
        /**
         * 滞纳金利率
         */
        private BigDecimal lateFeeApr;
        /**
         * 放款时间
         */
        private Date creditRepaymentTime;
        /**
         * 应还款时间
         */
        private Date repaymentTime;
        /**
         * 实际还款总期限
         */
        private Integer period;
        /**
         * 实际还款日期
         */
        private Date repaymentRealTime;
        /**
         * 滞纳金计算开始时间
         */
        private Date lateFeeStartTime;
        /**
         * 滞纳金更新时间
         */
        private Date interestUpdateTime;
        /**
         * 逾期天数
         */
        private Integer lateDay;
        /**
         * 还款订单状态：3： 未还款，2：部分还款，1：已还款
         */
        private Integer orderStatus;
        /**
         * 0:初始状态；1:未支付；2：成功；3：已取消；4：已退款；5：失败; 6：处理中
         */
        private Integer repaymentPayStatus;
        /**
         * 代扣支付状态:0:初始状态；1:处理中；2：成功；3：失败;
         */
        private Integer dkPayStatus;
        /**
         * 订单是否续期9已续期
         */
        private Integer orderRenewal;
        /**
         * 是否寄出，10已寄出
         */
        private Integer orderMail;
        /**
         * 商户订单号
         */
        private String noOrder;
        /**
         * 催收机构id
         */
        private Integer companyId;
        /**
         * 项目id
         */
        private Integer itemId;
        /**
         * 催收员id
         */
        private Integer collectorId;
        /**
         * 催收员姓名
         */
        private String collectorName;
        /**
         * 用户组
         */
        private Integer collectorGroupName;
        /**
         * 派单时间
         */
        private Date distributeTime;
        /**
         * 联系状态
         */
        private Integer contactStatus;
        /**
         * 催收状态
         */
        private Integer collectionStatus;

        /**
         * 是否逾期
         */
        private Integer isLate;//0:否；1：是
        /**
         * 下次贷款建议
         */
        private Integer nextLoanAdvice;
        /**
         * 最近催收记录
         */
        private String lastCollectionRecord;
        /**
         * 修改时间
         */
        private Date updateTime;
        /**
         * 承诺还款时间
         */
    /*@TableField("commitment_time")
    private Date commitmentTime;*/
        /**
         * 修改人
         */
        private String updateAccount;


        public static long getSerialVersionUID() {
            return serialVersionUID;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getRepaymentId() {
            return repaymentId;
        }

        public void setRepaymentId(Integer repaymentId) {
            this.repaymentId = repaymentId;
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

        public BigDecimal getCeditAmount() {
            return ceditAmount;
        }

        public void setCeditAmount(BigDecimal ceditAmount) {
            this.ceditAmount = ceditAmount;
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

        public Integer getRepaymentType() {
            return repaymentType;
        }

        public void setRepaymentType(Integer repaymentType) {
            this.repaymentType = repaymentType;
        }

        public BigDecimal getLateFeeApr() {
            return lateFeeApr;
        }

        public void setLateFeeApr(BigDecimal lateFeeApr) {
            this.lateFeeApr = lateFeeApr;
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

        public Integer getRepaymentPayStatus() {
            return repaymentPayStatus;
        }

        public void setRepaymentPayStatus(Integer repaymentPayStatus) {
            this.repaymentPayStatus = repaymentPayStatus;
        }

        public Integer getDkPayStatus() {
            return dkPayStatus;
        }

        public void setDkPayStatus(Integer dkPayStatus) {
            this.dkPayStatus = dkPayStatus;
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

        public String getNoOrder() {
            return noOrder;
        }

        public void setNoOrder(String noOrder) {
            this.noOrder = noOrder;
        }

        public Integer getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public Integer getCollectorId() {
            return collectorId;
        }

        public void setCollectorId(Integer collectorId) {
            this.collectorId = collectorId;
        }

        public String getCollectorName() {
            return collectorName;
        }

        public void setCollectorName(String collectorName) {
            this.collectorName = collectorName;
        }

        public Integer getCollectorGroupName() {
            return collectorGroupName;
        }

        public void setCollectorGroupName(Integer collectorGroupName) {
            this.collectorGroupName = collectorGroupName;
        }

        public Date getDistributeTime() {
            return distributeTime;
        }

        public void setDistributeTime(Date distributeTime) {
            this.distributeTime = distributeTime;
        }

        public Integer getContactStatus() {
            return contactStatus;
        }

        public void setContactStatus(Integer contactStatus) {
            this.contactStatus = contactStatus;
        }

        public Integer getCollectionStatus() {
            return collectionStatus;
        }

        public void setCollectionStatus(Integer collectionStatus) {
            this.collectionStatus = collectionStatus;
        }

        public Integer getIsLate() {
            return isLate;
        }

        public void setIsLate(Integer isLate) {
            this.isLate = isLate;
        }

        public Integer getNextLoanAdvice() {
            return nextLoanAdvice;
        }

        public void setNextLoanAdvice(Integer nextLoanAdvice) {
            this.nextLoanAdvice = nextLoanAdvice;
        }

        public String getLastCollectionRecord() {
            return lastCollectionRecord;
        }

        public void setLastCollectionRecord(String lastCollectionRecord) {
            this.lastCollectionRecord = lastCollectionRecord;
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
}
