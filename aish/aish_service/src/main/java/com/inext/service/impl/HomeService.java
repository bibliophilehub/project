package com.inext.service.impl;

import com.inext.dao.IHomeDao;
import com.inext.entity.HomeInfo;
import com.inext.service.IHomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;

@Service
public class HomeService implements IHomeService {
    Logger logger = LoggerFactory.getLogger(HomeService.class);
    @Resource
    IHomeDao homeDao;


    @Override
    public HomeInfo getAllCount() {
        HomeInfo homeInfo = new HomeInfo();
        DecimalFormat format = new DecimalFormat("0.00");// 本例实现小数点后保留六位有效数字
        // 查询总已认证用户数 -->
        homeInfo.setIdentificationUserCount(homeDao.getIdentificationUserCount());
        // 今日认证用户数 -->
        homeInfo.setDayIdentificationUserCount(homeDao.getDayIdentificationUserCount());

        // 累计放款金额 -->
        homeInfo.setTotalLoanMoney(homeDao.getTotalLoanMoney());
        // 累计放款笔数 -->
        homeInfo.setTotalLoanCount(homeDao.getTotalLoanCount());
        // 当日放款笔数 -->
        homeInfo.setDayTotalLoanCount(homeDao.getDayTotalLoanCount());

        //今日统计
        //今日注册
        homeInfo.setDayRegisterUserCount(homeDao.getDayRegisterUserCount());
        //今日申请
        Long instanceDayApproveUserCount = homeDao.getDayApproveUserCount();
        homeInfo.setDayApproveUserCount(instanceDayApproveUserCount);
        //今日首次申请用户数
        homeInfo.setDayFirstApproeUseCount(homeDao.getDayFirstApproveUseCount());
        //今日审核通过
        Long instanceDayAdoptUserCount = homeDao.getDayAdoptUserCount();
        homeInfo.setDayAdoptUserCount(instanceDayAdoptUserCount);
        //今日审核通过率
        if(instanceDayApproveUserCount != null && instanceDayApproveUserCount.longValue()!=0) {
            double tmp = instanceDayAdoptUserCount / (double)instanceDayApproveUserCount;
            homeInfo.setDayAdoptRate(format.format(tmp));
        }
        else
            homeInfo.setDayAdoptRate("0");
        //今日首次通过数
        homeInfo.setDayFirstAdoptCount(homeDao.getDayFirstAdoptCount());

        //今日放款量
        homeInfo.setDayTotalLoanMoney(homeDao.getDayTotalLoanMoney());
        //今日首次放款量
        homeInfo.setDayFirstLoanMoney(homeDao.getDayFirstTotalMoney());
        //今日赎回量
        homeInfo.setDayRepaymentMoney(homeDao.getDayRepaymentMoney());

        //历史统计
        //历史注册
        homeInfo.setUserCount(homeDao.getUserCount());
        //历史放款总量
        homeInfo.setTotalBorrowInfo(homeDao.getTotalBorrowInfo());
        //历史赎回总量
        homeInfo.setTotalRepaymentInfo(homeDao.getTotalRepaymentInfo());
        //历史续期总量
        homeInfo.setTotalRenewalInfo(homeDao.getTotalRenewalInfo());
        //待赎回总量
        homeInfo.setTotalNoRepaymentInfo(homeDao.getTotalNoRepaymentInfo());
        //逾期未赎回总量
        homeInfo.setTotalOverdueInfo(homeDao.getTotalOverdueInfo());
        //逾期S1占比
        homeInfo.setTotalOverdueS1Info(homeDao.getTotalOverdueS1Info());
        //逾期S2占比
        homeInfo.setTotalOverdueS2Info(homeDao.getTotalOverdueS2Info());


        //风控统计
        // 机审累计订单数 -->
        Long instanceOrderCount = homeDao.getForInstanceOrderCount();
        homeInfo.setForInstanceOrderCount(instanceOrderCount);
        // 机审通过累计订单数 -->
        Long instanceAdoptOrderCount = homeDao.getForInstanceAdoptOrderCount();
        homeInfo.setForInstanceAdoptOrderCount(instanceAdoptOrderCount);
        // 今日机审订单数 -->
        Long dayInstanceOrderCount = homeDao.getDayForInstanceOrderCount();
        homeInfo.setDayForInstanceOrderCount(dayInstanceOrderCount);
        // 今日机审通过订单数-->
        Long dayInstanceAdoptOrderCount = homeDao.getDayForInstanceAdoptOrderCount();
        homeInfo.setDayForInstanceAdoptOrderCount(dayInstanceAdoptOrderCount);
        if (instanceOrderCount != null && dayInstanceOrderCount.longValue() != 0) {
            double tmp = instanceAdoptOrderCount / instanceOrderCount;
            homeInfo.setPassRate1(format.format(tmp));
        } else {
            homeInfo.setPassRate1("0");
        }
        if (dayInstanceOrderCount != null && dayInstanceOrderCount.longValue() != 0) {
            double tmp = dayInstanceAdoptOrderCount / dayInstanceOrderCount;
            homeInfo.setPassRate2(format.format(tmp));
        } else {
            homeInfo.setPassRate2("0");
        }

        //老客申请统计
        Long dayOldApproveCount = homeDao.getDayOldApproveCount();
        Long dayOldAdoptCount=homeDao.getDayOldAdoptCount();
        homeInfo.setDayOldApporveCount(dayOldApproveCount);
        homeInfo.setDayOldAdoptCount(dayOldAdoptCount);

        return homeInfo;
    }
}
