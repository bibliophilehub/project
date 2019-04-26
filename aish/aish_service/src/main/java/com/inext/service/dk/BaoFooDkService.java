package com.inext.service.dk;

import com.inext.entity.AssetRepaymentOrderWithhold;


public interface BaoFooDkService {

    /**
     * 宝付代扣
     */
    void doDebitBatch();

    /**
     * 订单查询
     * @param withhold
     */
    void debitQuery(AssetRepaymentOrderWithhold withhold);

}
