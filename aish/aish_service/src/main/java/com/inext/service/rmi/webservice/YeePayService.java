
package com.inext.service.rmi.webservice;

import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.AssetRepaymentOrderDaikou;
import com.inext.entity.BorrowUser;
import com.inext.result.ApiServiceResult;

import java.io.IOException;
import java.util.Map;

/**
 * 
 * 功能描述: TODO 易宝对接服务接口
 * @编码实现人员 wxy
 * @实现日期 2018年6月25日
 *      --------------------------------------------------
 */
public interface YeePayService
{

    /**
     * 
     * TODO 易宝鉴权绑卡，系统自动绑卡
     * @author wxy
     * @date 2018年6月25日
     * @param user 用户表
     * @return
     */
    ApiServiceResult<Object> authBindCard(BorrowUser user) throws IOException;

    /**
     * 
     * TODO 易宝鉴权绑卡,用户主动绑卡，发送短信
     * @author wxy
     * @date 2018年6月25日
     * @param repayment 还款表信息
     * @return
     */
    Map<String, String> userBindCard(Map<String, String> bindParaMap) throws IOException;

    /**
     * 
     * TODO 易宝鉴权绑卡,用户主动绑卡，短信确认
     * @author wxy
     * @date 2018年6月25日
     * @param repayment 还款表信息
     * @return
     */
    Map<String, String> userBindCardConfrim(Map<String, String> bindParaMap) throws IOException;

    /**
     * 
     * TODO 易宝自动代扣
     * @author wxy
     * @date 2018年6月26日
     * @param repayment
     * @return
     */
    ApiServiceResult<Object> autoFirstPay(AssetRepaymentOrder repayment) throws IOException;

    /**
     * 
     * TODO 易宝自动代扣查询
     * @author wxy
     * @date 2018年6月26日
     * @param daiKou
     * @return
     */
    ApiServiceResult<Object> queryFirstPay(AssetRepaymentOrderDaikou daiKou) throws IOException;
}
