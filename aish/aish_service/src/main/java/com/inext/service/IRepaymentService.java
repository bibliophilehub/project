package com.inext.service;

import com.inext.entity.BorrowUser;
import com.inext.entity.fuyou.Fuiou;

import java.util.Map;

/**
 * @Author: jzhang
 * @Date: 2018-04-14 0014 下午 02:13
 */
public interface IRepaymentService {
    public Fuiou getRepaymentFromParams(Integer userId, Integer orderId, Integer type);

    int doCcallback(Map<String, String> params);

    void setOrderHisStauts(String repaymentInfoId);

    public Map getRepaymentParams(Integer userId, Integer orderId, Integer type,String ip);

    Map confirmPay(String infoId, String validateCode, String ip);

    /**
     * 宝付主动还款
     * @param userId 用户id
     * @param orderId 订单id
     * @param type 还款类型：1-还款，2-续期
     * @param hkType 还款方式：0-主动还款，1-后台代扣
     * @return
     */
    Map baoFooConfirmPay(int userId, int orderId, int type, int hkType);

    /**
     * 宝付主动还款回调
     * @param params
     * @return
     */
    int doBaoFooCallback(Map<String, String> params);

    /**
     * 宝付主动还款订单查询
     * @param transId
     * @return
     * @throws Exception
     */
    Map queryBaoFooPayOrder(String transId) throws Exception ;
}
