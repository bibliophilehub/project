package com.inext.service.pay;

import com.inext.entity.AssetBorrowOrder;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiServiceResult;

/**
 * 代付
 * 将所有的代付状态整理后返回处理结果 成功/失败/处理中/退款
 *
 * @author lisige
 */
public interface IPayForAnotherService {

    /**
     * 代付放款接口
     * @param assetBorrowOrder
     * @return
     * @throws Exception
     */
    ApiServiceResult<PaymentChannelEnum> paymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception;

    /**
     * 放款之后查询接口
     * @param assetBorrowOrder
     * @return
     * @throws Exception
     */
    ApiServiceResult queryPaymentApi(AssetBorrowOrder assetBorrowOrder) throws Exception;


    /**
     * 当前放款通道是否达到放款条件
     *
     * @param assetBorrowOrder
     * @return
     */
    ApiServiceResult verifyMakeLoansCondition(AssetBorrowOrder assetBorrowOrder, Integer paymentChannelId);
}
