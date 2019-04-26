package com.stylefeng.guns.modular.system.model;

import com.inext.entity.BorrowUserPhone;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BorrowUserAllInfoDto implements Serializable {
    private int code;//返回的状态码，200成功，500失败
    private BorrowUser borrowUser;//客户基本信息，认证xinx
    private List<BorrowUserPhone> borrowUserPhones;//客户通训录
    private List<BizAssetBorrowOrder> assetBorrowOrders; //客户的历史借款订单
    private List<CallRecords> callRecords;//通话记录
    private String callRecordsJson;

    public BorrowUser getBorrowUser() {
        return borrowUser;
    }

    public void setBorrowUser(BorrowUser borrowUser) {
        this.borrowUser = borrowUser;
    }

    public List<BorrowUserPhone> getBorrowUserPhones() {
        return borrowUserPhones;
    }

    public void setBorrowUserPhones(List<BorrowUserPhone> borrowUserPhones) {
        this.borrowUserPhones = borrowUserPhones;
    }


    public List<CallRecords> getCallRecords() {
        return callRecords;
    }

    public void setCallRecords(List<CallRecords> callRecords) {
        this.callRecords = callRecords;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCallRecordsJson() {
        return callRecordsJson;
    }

    public void setCallRecordsJson(String callRecordsJson) {
        this.callRecordsJson = callRecordsJson;
    }

    public List<BizAssetBorrowOrder> getAssetBorrowOrders() {
        return assetBorrowOrders;
    }

    public void setAssetBorrowOrders(List<BizAssetBorrowOrder> assetBorrowOrders) {
        this.assetBorrowOrders = assetBorrowOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowUserAllInfoDto that = (BorrowUserAllInfoDto) o;
        return code == that.code &&
                Objects.equals(borrowUser, that.borrowUser) &&
                Objects.equals(borrowUserPhones, that.borrowUserPhones) &&
                Objects.equals(assetBorrowOrders, that.assetBorrowOrders) &&
                Objects.equals(callRecords, that.callRecords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code, borrowUser, borrowUserPhones, assetBorrowOrders, callRecords);
    }
}
