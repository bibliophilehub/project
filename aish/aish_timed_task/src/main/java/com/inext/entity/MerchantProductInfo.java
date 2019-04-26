package com.inext.entity;

import javax.persistence.Id;
import java.util.Date;

/**
 * 开发人员：jzhang
 * 创建时间：2017-05-18 下午 14:14
 */
public class MerchantProductInfo {

    @Id
    private Integer id;
    private Integer merchantId;
    private String productCode;
    private Date onSaleDate;
    private Date offSaleDate;
    private String interestWay;
    private String rate;
    private String loanPrescriptionStart;
    private String loanPrescriptionEnd;
    private String qutaRangeStart;
    private String qutaRangeEnd;
    private String productType;
    private String applySuccessNum;
    private String keyRifi;
    private String productFeatures;
    private String applyFeatures;
    private String applyConditions;
    private String requiredMaterials;
    private String newerNote;
    private String jumpLink;
    private String status;
    private Integer score;
    private Integer subStatus;
    private String productName;
    private String todayClick;
    private Integer accumulation;
    private Integer crediCard;
    private Integer shoppingAccount;
    private String provinceCode;
    private String cityCode;
    private String repaymentMethod;
    private String repaymentPeriod;
    private String repaymentUnit;
    private String studentOnly;
    private Integer search_howLong;
    private Integer rank;
    private Integer host;
    private String endRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Date getOnSaleDate() {
        return onSaleDate;
    }

    public void setOnSaleDate(Date onSaleDate) {
        this.onSaleDate = onSaleDate;
    }

    public Date getOffSaleDate() {
        return offSaleDate;
    }

    public void setOffSaleDate(Date offSaleDate) {
        this.offSaleDate = offSaleDate;
    }

    public String getInterestWay() {
        return interestWay;
    }

    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLoanPrescriptionStart() {
        return loanPrescriptionStart;
    }

    public void setLoanPrescriptionStart(String loanPrescriptionStart) {
        this.loanPrescriptionStart = loanPrescriptionStart;
    }

    public String getLoanPrescriptionEnd() {
        return loanPrescriptionEnd;
    }

    public void setLoanPrescriptionEnd(String loanPrescriptionEnd) {
        this.loanPrescriptionEnd = loanPrescriptionEnd;
    }

    public String getQutaRangeStart() {
        return qutaRangeStart;
    }

    public void setQutaRangeStart(String qutaRangeStart) {
        this.qutaRangeStart = qutaRangeStart;
    }

    public String getQutaRangeEnd() {
        return qutaRangeEnd;
    }

    public void setQutaRangeEnd(String qutaRangeEnd) {
        this.qutaRangeEnd = qutaRangeEnd;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getApplySuccessNum() {
        return applySuccessNum;
    }

    public void setApplySuccessNum(String applySuccessNum) {
        this.applySuccessNum = applySuccessNum;
    }

    public String getKeyRifi() {
        return keyRifi;
    }

    public void setKeyRifi(String keyRifi) {
        this.keyRifi = keyRifi;
    }

    public String getProductFeatures() {
        return productFeatures;
    }

    public void setProductFeatures(String productFeatures) {
        this.productFeatures = productFeatures;
    }

    public String getApplyFeatures() {
        return applyFeatures;
    }

    public void setApplyFeatures(String applyFeatures) {
        this.applyFeatures = applyFeatures;
    }

    public String getApplyConditions() {
        return applyConditions;
    }

    public void setApplyConditions(String applyConditions) {
        this.applyConditions = applyConditions;
    }

    public String getRequiredMaterials() {
        return requiredMaterials;
    }

    public void setRequiredMaterials(String requiredMaterials) {
        this.requiredMaterials = requiredMaterials;
    }

    public String getNewerNote() {
        return newerNote;
    }

    public void setNewerNote(String newerNote) {
        this.newerNote = newerNote;
    }

    public String getJumpLink() {
        return jumpLink;
    }

    public void setJumpLink(String jumpLink) {
        this.jumpLink = jumpLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(Integer subStatus) {
        this.subStatus = subStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTodayClick() {
        return todayClick;
    }

    public void setTodayClick(String todayClick) {
        this.todayClick = todayClick;
    }

    public Integer getAccumulation() {
        return accumulation;
    }

    public void setAccumulation(Integer accumulation) {
        this.accumulation = accumulation;
    }

    public Integer getCrediCard() {
        return crediCard;
    }

    public void setCrediCard(Integer crediCard) {
        this.crediCard = crediCard;
    }

    public Integer getShoppingAccount() {
        return shoppingAccount;
    }

    public void setShoppingAccount(Integer shoppingAccount) {
        this.shoppingAccount = shoppingAccount;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public String getRepaymentPeriod() {
        return repaymentPeriod;
    }

    public void setRepaymentPeriod(String repaymentPeriod) {
        this.repaymentPeriod = repaymentPeriod;
    }

    public String getRepaymentUnit() {
        return repaymentUnit;
    }

    public void setRepaymentUnit(String repaymentUnit) {
        this.repaymentUnit = repaymentUnit;
    }

    public String getStudentOnly() {
        return studentOnly;
    }

    public void setStudentOnly(String studentOnly) {
        this.studentOnly = studentOnly;
    }

    public Integer getSearch_howLong() {
        return search_howLong;
    }

    public void setSearch_howLong(Integer search_howLong) {
        this.search_howLong = search_howLong;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getHost() {
        return host;
    }

    public void setHost(Integer host) {
        this.host = host;
    }

    public String getEndRate() {
        return endRate;
    }

    public void setEndRate(String endRate) {
        this.endRate = endRate;
    }
}
