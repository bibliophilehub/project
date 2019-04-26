
package com.inext.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AssetRepaymentOrderDetail
{
    private Integer id;

    private Integer borrowUserId;

    private Integer borrowOrderId;

    private Integer repaymentOrderId;

    private String outOrderNo;

    private BigDecimal moneyAmount;

    private BigDecimal penaltyAmount;

    private BigDecimal planLateFee;

    private BigDecimal ceditAmount;

    private BigDecimal sumAmount;

    private String comment;

    private Integer repaymentWay;

    private Integer currRepStatus;

    private Date payTime;

    private String createBy;

    private Date createTime;

    public String getOutOrderNo()
    {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo)
    {
        this.outOrderNo = outOrderNo;
    }

    public BigDecimal getSumAmount()
    {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount)
    {
        this.sumAmount = sumAmount;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getBorrowUserId()
    {
        return borrowUserId;
    }

    public void setBorrowUserId(Integer borrowUserId)
    {
        this.borrowUserId = borrowUserId;
    }

    public Integer getBorrowOrderId()
    {
        return borrowOrderId;
    }

    public void setBorrowOrderId(Integer borrowOrderId)
    {
        this.borrowOrderId = borrowOrderId;
    }

    public Integer getRepaymentOrderId()
    {
        return repaymentOrderId;
    }

    public void setRepaymentOrderId(Integer repaymentOrderId)
    {
        this.repaymentOrderId = repaymentOrderId;
    }

    public BigDecimal getMoneyAmount()
    {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount)
    {
        this.moneyAmount = moneyAmount;
    }

    public BigDecimal getPenaltyAmount()
    {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount)
    {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getPlanLateFee()
    {
        return planLateFee;
    }

    public void setPlanLateFee(BigDecimal planLateFee)
    {
        this.planLateFee = planLateFee;
    }

    public BigDecimal getCeditAmount()
    {
        return ceditAmount;
    }

    public void setCeditAmount(BigDecimal ceditAmount)
    {
        this.ceditAmount = ceditAmount;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getRepaymentWay()
    {
        return repaymentWay;
    }

    public void setRepaymentWay(Integer repaymentWay)
    {
        this.repaymentWay = repaymentWay;
    }

    public Integer getCurrRepStatus()
    {
        return currRepStatus;
    }

    public void setCurrRepStatus(Integer currRepStatus)
    {
        this.currRepStatus = currRepStatus;
    }

    public Date getPayTime()
    {
        return payTime;
    }

    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}