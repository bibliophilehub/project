package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.HomeInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface IHomeDao extends BaseDao<HomeInfo> {

    // 查询总已认证用户数 -->
    public Long getIdentificationUserCount();

    // 今日认证用户数 -->
    public Long getDayIdentificationUserCount();

    // 累计放款金额 -->
    public Long getTotalLoanMoney();

    // 累计放款笔数 -->
    public Long getTotalLoanCount();

    // 当日放款笔数 -->
    public Long getDayTotalLoanCount();

    //今日统计
    // 今日注册用户数
    public Long getDayRegisterUserCount();
    // 今日申请用户数
    public Long getDayApproveUserCount();
    //今日首次申请用户数
    public Long getDayFirstApproveUseCount();
    //今日审核通过
    public Long getDayAdoptUserCount();
    //当日放款金额 -->
    public Long getDayTotalLoanMoney();
    //今日首次放款量
    public Long getDayFirstTotalMoney();
    //今日赎回量
    public  Long getDayRepaymentMoney();
    //今日首次申请通过
    public Long getDayFirstAdoptCount();

    //历史统计
    //历史注册查询总用户数 -->
    public Long getUserCount();
    //历史放款总量
    public String getTotalBorrowInfo();
    //历史赎回总量
    public String getTotalRepaymentInfo();
    //历史续期总量
    public String getTotalRenewalInfo();
    //待赎回总量
    public String getTotalNoRepaymentInfo();
    //逾期未赎回总量
    public String getTotalOverdueInfo();
    //逾期S1占比
    public String getTotalOverdueS1Info();
    //逾期S2占比
    public String getTotalOverdueS2Info();

    //风控统计
    // 机审累计订单数 -->
    public Long getForInstanceOrderCount();

    // 机审通过累计订单数 -->
    public Long getForInstanceAdoptOrderCount();

    // 今日机审订单数 -->
    public Long getDayForInstanceOrderCount();

    // 今日机审通过订单数-->
    public Long getDayForInstanceAdoptOrderCount();

    //今日老客申请数
    public Long getDayOldApproveCount();

    //今日老客通过数
    public Long getDayOldAdoptCount();

}
