package com.inext.service;

import com.inext.result.ApiServiceResult;

import java.util.List;
import java.util.Map;

/**
 * @Author: jzhang
 * @Date: 2018-03-28 0028 上午 10:55
 */
public interface CreditService {
    /**
     * 处理订单征信
     */
    void doOrderCredit();

    /**
     * 处理jxl征信
     */
    void doGetJxlReport();

    /**
     * 处理风控-创建风控订单
     */
    void doOrderRisk();

    /**
     * 处理风控-无其右风控订单
     * @throws Exception
     */
    void doWuqiyouOrderRisk() throws Exception;

    /**
     * 运营商认证状态更新处理
     * @throws Exception
     */
    void doMobileAuthUpdate() throws Exception;;

    /**
     * 查询符合-入-白名单-的用户-总数
     * @return
     */
    int getInWhiteListUserCount();

    /**
     *  查询符合-入-白名单-的用户-数据
     * @return
     */
    List<Map<String, String>> getInWhiteListUser(int start, int pageSize);

    /**
     *  查询符合-入-黑名单-的用户-总数
     * @return
     */
    int getInBlackListUserCount();

    /**
     *  查询符合-入-黑名单-的用户-数据
     * @return
     */
    List<Map<String, String>> getInBlackListUser(int start, int pageSize);

    /**
     *  查询符合-出-黑名单-的用户-总数
     * @return
     */
    int getOutBlackListUserCount();

    /**
     *  查询符合-出-黑名单-的用户-数据
     * @return
     */
    List<Map<String, String>> getOutBlackListUser(int start, int pageSize);


    /**
     * 查询用户是否为黑/白名单
     * @param phone 手机号
     * @param name 姓名
     * @param platform 平台：1-爱上花，2-花小侠
     * @return
     */
    String getIsBlackWhiteUser(String phone, String name, String platform);

    /**
     * 设置用户为黑/白名单
     * @param phone 手机号
     * @param name 姓名
     * @param platform 平台：1-爱上花，2-花小侠
     * @param optType 操作类型：1-设置白名单，2-设置黑名单
     * @param status 状态：0-无效，1-有效
     * @return
     */
    ApiServiceResult saveBlackWhiteUser(String phone, String name, String platform, String optType, String status);
}
