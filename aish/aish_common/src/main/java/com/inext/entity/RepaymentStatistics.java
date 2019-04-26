package com.inext.entity;

/**
 * describe:
 *
 * @author mlk
 * @date 2018/03/29
 */
public class RepaymentStatistics {
    private String repaymentTime;//'应还款日期',
    private Long orderQuantity;//应还款单数',
    private Long totalRepayment;//'应还款总额',
    private Long totalRepaymentSuccess;// '还款成功总额',
    private Long orderQuantityNo;//'待还款笔数',
    private Long totalRepaymentNo;//'待还款总额',
    private float overdueRate;//'首逾率',


    public String getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(String repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public Long getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Long orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Long getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(Long totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public Long getTotalRepaymentSuccess() {
        return totalRepaymentSuccess;
    }

    public void setTotalRepaymentSuccess(Long totalRepaymentSuccess) {
        this.totalRepaymentSuccess = totalRepaymentSuccess;
    }

    public Long getOrderQuantityNo() {
        return orderQuantityNo;
    }

    public void setOrderQuantityNo(Long orderQuantityNo) {
        this.orderQuantityNo = orderQuantityNo;
    }

    public Long getTotalRepaymentNo() {
        return totalRepaymentNo;
    }

    public void setTotalRepaymentNo(Long totalRepaymentNo) {
        this.totalRepaymentNo = totalRepaymentNo;
    }

    public float getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(float overdueRate) {
        this.overdueRate = overdueRate;
    }
}
