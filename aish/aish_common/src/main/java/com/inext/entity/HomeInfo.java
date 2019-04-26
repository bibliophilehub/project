package com.inext.entity;

import java.io.Serializable;

/**
 * describe:
 *
 * @author mlk
 * @date 2018/03/23
 */
public class HomeInfo implements Serializable {

    // 查询总已认证用户数 -->
    private Long identificationUserCount;
    // 今日认证用户数 -->
    private Long dayIdentificationUserCount;

    // 累计放款金额 -->
    private Long totalLoanMoney;
    // 累计放款笔数 -->
    private Long totalLoanCount;

    // 当日放款笔数 -->
    private Long dayTotalLoanCount;

    //今日统计
    //今日注册
    private Long dayRegisterUserCount;
    //今日申请
    private Long dayApproveUserCount;
    //今日首次申请用户数
    private Long dayFirstApproeUseCount;
    //今日审核通过
    private Long dayAdoptUserCount;
    //今日审核通过率
    private String dayAdoptRate;
    //今日放款量
    private Long dayTotalLoanMoney;
    //今日首次放款量
    private  Long dayFirstLoanMoney;
    //今日赎回量
    private  Long dayRepaymentMoney;
    //今日首次通过单数
    private Long dayFirstAdoptCount;

    //历史统计
    //历史注册
    private Long userCount;
    //历史放款总量
    private String totalBorrowInfo;
    //历史赎回总量
    private String totalRepaymentInfo;
    //历史续期总量
    private String totalRenewalInfo;
    //待赎回总量
    private String totalNoRepaymentInfo;
    //逾期未赎回总量
    private String totalOverdueInfo;
    //逾期S1占比
    private String totalOverdueS1Info;
    //逾期S2占比
    private String totalOverdueS2Info;

    //风控统计
    // 机审累计订单数 -->
    private Long forInstanceOrderCount;
    // 机审通过累计订单数 -->
    private Long forInstanceAdoptOrderCount;
    // 今日机审订单数 -->
    private Long dayForInstanceOrderCount;
    // 今日机审通过订单数-->
    private Long dayForInstanceAdoptOrderCount;

    //老客申请数
    private Long dayOldApporveCount;
    //老客通过数
    private Long dayOldAdoptCount;
    //老客通过率
    private String dayOldAdoptRate;

    private String passRate1;
    private String passRate2;

    public String getPassRate1() {
        return passRate1;
    }

    public void setPassRate1(String passRate1) {
        this.passRate1 = passRate1;
    }

    public String getPassRate2() {
        return passRate2;
    }

    public void setPassRate2(String passRate2) {
        this.passRate2 = passRate2;
    }

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount == null ? 0L : userCount;
    }

    public Long getIdentificationUserCount() {
        return identificationUserCount;
    }

    public void setIdentificationUserCount(Long identificationUserCount) {
        this.identificationUserCount = identificationUserCount == null ? 0L : identificationUserCount;
    }

    public Long getDayRegisterUserCount() {
        return dayRegisterUserCount;
    }

    public void setDayRegisterUserCount(Long dayRegisterUserCount) {
        this.dayRegisterUserCount = dayRegisterUserCount == null ? 0L : dayRegisterUserCount;
    }

    public Long getDayIdentificationUserCount() {
        return dayIdentificationUserCount;
    }

    public void setDayIdentificationUserCount(Long dayIdentificationUserCount) {
        this.dayIdentificationUserCount = dayIdentificationUserCount == null ? 0L : dayIdentificationUserCount;
    }

    public Long getTotalLoanMoney() {
        return totalLoanMoney;
    }

    public void setTotalLoanMoney(Long totalLoanMoney) {
        this.totalLoanMoney = totalLoanMoney == null ? 0L : totalLoanMoney;
    }

    public Long getTotalLoanCount() {
        return totalLoanCount;
    }

    public void setTotalLoanCount(Long totalLoanCount) {
        this.totalLoanCount = totalLoanCount == null ? 0L : totalLoanCount;
    }

    public Long getDayTotalLoanMoney() {
        return dayTotalLoanMoney;
    }

    public void setDayTotalLoanMoney(Long dayTotalLoanMoney) {
        this.dayTotalLoanMoney = dayTotalLoanMoney == null ? 0L : dayTotalLoanMoney;
    }

    public Long getDayTotalLoanCount() {
        return dayTotalLoanCount;
    }

    public void setDayTotalLoanCount(Long dayTotalLoanCount) {
        this.dayTotalLoanCount = dayTotalLoanCount == null ? 0L : dayTotalLoanCount;
    }

    public Long getForInstanceOrderCount() {
        return forInstanceOrderCount;
    }

    public void setForInstanceOrderCount(Long forInstanceOrderCount) {
        this.forInstanceOrderCount = forInstanceOrderCount;
    }

    public Long getForInstanceAdoptOrderCount() {
        return forInstanceAdoptOrderCount;
    }

    public void setForInstanceAdoptOrderCount(Long forInstanceAdoptOrderCount) {
        this.forInstanceAdoptOrderCount = forInstanceAdoptOrderCount;
    }

    public Long getDayForInstanceOrderCount() {
        return dayForInstanceOrderCount;
    }

    public void setDayForInstanceOrderCount(Long dayForInstanceOrderCount) {
        this.dayForInstanceOrderCount = dayForInstanceOrderCount;
    }

    public Long getDayForInstanceAdoptOrderCount() {
        return dayForInstanceAdoptOrderCount;
    }

    public void setDayForInstanceAdoptOrderCount(Long dayForInstanceAdoptOrderCount) {
        this.dayForInstanceAdoptOrderCount = dayForInstanceAdoptOrderCount;
    }

    public Long getDayApproveUserCount() {
        return dayApproveUserCount;
    }

    public void setDayApproveUserCount(Long dayApproveUserCount) {
        this.dayApproveUserCount = dayApproveUserCount;
    }

    public Long getDayFirstApproeUseCount() {
        return dayFirstApproeUseCount;
    }

    public void setDayFirstApproeUseCount(Long dayFirstApproeUseCount) {
        this.dayFirstApproeUseCount = dayFirstApproeUseCount;
    }

    public Long getDayAdoptUserCount() {
        return dayAdoptUserCount;
    }

    public void setDayAdoptUserCount(Long dayAdoptUserCount) {
        this.dayAdoptUserCount = dayAdoptUserCount;
    }

    public String getDayAdoptRate() {
        return dayAdoptRate;
    }

    public void setDayAdoptRate(String dayAdoptRate) {
        this.dayAdoptRate = dayAdoptRate;
    }

    public Long getDayFirstLoanMoney() {
        return dayFirstLoanMoney;
    }

    public void setDayFirstLoanMoney(Long dayFirstLoanMoney) {
        this.dayFirstLoanMoney = dayFirstLoanMoney;
    }

    public Long getDayRepaymentMoney() {
        return dayRepaymentMoney;
    }

    public void setDayRepaymentMoney(Long dayRepaymentMoney) {
        this.dayRepaymentMoney = dayRepaymentMoney;
    }

    public String getTotalBorrowInfo() {
        return totalBorrowInfo;
    }

    public void setTotalBorrowInfo(String totalBorrowInfo) {
        this.totalBorrowInfo = totalBorrowInfo;
    }

    public String getTotalRepaymentInfo() {
        return totalRepaymentInfo;
    }

    public void setTotalRepaymentInfo(String totalRepaymentInfo) {
        this.totalRepaymentInfo = totalRepaymentInfo;
    }

    public String getTotalRenewalInfo() {
        return totalRenewalInfo;
    }

    public void setTotalRenewalInfo(String totalRenewalInfo) {
        this.totalRenewalInfo = totalRenewalInfo;
    }

    public String getTotalNoRepaymentInfo() {
        return totalNoRepaymentInfo;
    }

    public void setTotalNoRepaymentInfo(String totalNoRepaymentInfo) {
        this.totalNoRepaymentInfo = totalNoRepaymentInfo;
    }

    public String getTotalOverdueInfo() {
        return totalOverdueInfo;
    }

    public void setTotalOverdueInfo(String totalOverdueInfo) {
        this.totalOverdueInfo = totalOverdueInfo;
    }

    public String getTotalOverdueS1Info() {
        return totalOverdueS1Info;
    }

    public void setTotalOverdueS1Info(String totalOverdueS1Info) {
        this.totalOverdueS1Info = totalOverdueS1Info;
    }

    public String getTotalOverdueS2Info() {
        return totalOverdueS2Info;
    }

    public void setTotalOverdueS2Info(String totalOverdueS2Info) {
        this.totalOverdueS2Info = totalOverdueS2Info;
    }

    public Long getDayOldApporveCount() {
        return dayOldApporveCount;
    }

    public void setDayOldApporveCount(Long dayOldApporveCount) {
        this.dayOldApporveCount = dayOldApporveCount;
    }

    public Long getDayOldAdoptCount() {
        return dayOldAdoptCount;
    }

    public void setDayOldAdoptCount(Long dayOldAdoptCount) {
        this.dayOldAdoptCount = dayOldAdoptCount;
    }

    public String getDayOldAdoptRate() {
        return dayOldAdoptRate;
    }

    public void setDayOldAdoptRate(String dayOldAdoptRate) {
        this.dayOldAdoptRate = dayOldAdoptRate;
    }

    public Long getDayFirstAdoptCount() { return dayFirstAdoptCount; }

    public void setDayFirstAdoptCount(Long dayFirstAdoptCount) { this.dayFirstAdoptCount = dayFirstAdoptCount;}
}
