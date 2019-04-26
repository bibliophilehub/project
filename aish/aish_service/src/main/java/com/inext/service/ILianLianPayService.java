package com.inext.service;

import com.alibaba.fastjson.JSONObject;
import com.inext.entity.AssetBorrowOrder;
import com.inext.result.ApiServiceResult;

public interface ILianLianPayService {
    /**
     * 放款接口
     * @param assetBorrowOrder
     * @return 500是是服务器异常，本地代码错误或者连接异常<br>
     *     100 数据处理中 需要定时查询任务处理
     *     -1 代发失败
     *     这里没有代发成功返回，在异步或者定时查询才有
     */
    public ApiServiceResult paymentApi(AssetBorrowOrder assetBorrowOrder);

    /**
     * 放款之后查询接口
     * @param assetBorrowOrder
     * @return
     */
    public ApiServiceResult queryPaymentApi(AssetBorrowOrder assetBorrowOrder);
}
