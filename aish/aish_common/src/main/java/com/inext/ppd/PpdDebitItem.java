package com.inext.ppd;

import lombok.Data;

@Data
public class PpdDebitItem {

    private String seqId;
    private String seqNumber;
    private String bankAcctId;
    private String bankAcctName;
    private String amount;
    private String remark;
    private String dealResult;
    private String errMessage;

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(String seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getBankAcctId() {
        return bankAcctId;
    }

    public void setBankAcctId(String bankAcctId) {
        this.bankAcctId = bankAcctId;
    }

    public String getBankAcctName() {
        return bankAcctName;
    }

    public void setBankAcctName(String bankAcctName) {
        this.bankAcctName = bankAcctName;
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

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
