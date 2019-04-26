package com.inext.service.pay;

import com.inext.entity.AssetBorrowOrder;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiServiceResult;

/**
 * @author lisige
 */
public interface IPaymentService {

    /**
     * 订单支付
     *
     * @param assetBorrowOrder
     * @return
     */
    ApiServiceResult<PaymentChannelEnum> paymentOrder(AssetBorrowOrder assetBorrowOrder) throws Exception;

    /**
     * 订单支付状态查询
     *
     * @param assetBorrowOrder
     * @return
     */
    ApiServiceResult queryOrder(AssetBorrowOrder assetBorrowOrder) throws Exception;
}
