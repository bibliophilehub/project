
package com.inext.service;

import com.inext.dto.AssetRepaymentOrderDetailDto;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.AssetRepaymentOrderDetail;

import java.math.BigDecimal;

public interface IAssetRepaymentOrderDetailService extends BaseSerivce<AssetRepaymentOrderDetail>
{

    /**
     * 根据还款订单查询历史所有的各项还款总额 
     * @author lihong
     * @date 2018年6月15日
     * @return
     */
    AssetRepaymentOrderDetail selectDetailsByRepaymentOrderId(Integer repaymentOrderId);

    /**
     * 
     * 增加还款流水<br/>
     * 自动计算 把当前还款金额 还到 本金、违约金、滞纳金上<br/>
     * 且需支持分多批次部分还款的情况
     * @author lihong
     * @date 2018年6月15日
     * @param currentAmount 本次用户还的总金额
     * @param ceditAmount 本次用户减免金额
     * @param rawRepaymentOrder 原始数据库中的还款订单
     * @param orderStatus 本次修改为的状态
     * @param repaymentComment 本次用户还款说明
     * @param repaymentType 本次用户还款类型(支付宝、微信、快捷支付等)
     * @param outOrderNo 本次支付外部订单号
     * @param operator 本次还款操作人
     */
    public void insertAssetRepaymentOrderDetail(BigDecimal currentAmount, BigDecimal ceditAmount, AssetRepaymentOrder rawRepaymentOrder, Integer orderStatus, String repaymentComment, Integer repaymentType,
                                                String outOrderNo, String operator);

    /**
     * 管理员还款
     */
    void trueRepaymentMoney(AssetRepaymentOrder aro, AssetRepaymentOrderDetailDto assetRepaymentOrderDetail);


}
