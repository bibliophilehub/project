package com.inext.service.pay.impl;

import com.inext.configuration.SpringContextAware;
import com.inext.constants.Constants;
import com.inext.dao.IPaymentChannelDao;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.PaymentChannel;
import com.inext.enumerate.ApiStatus;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiServiceResult;
import com.inext.service.pay.IPayForAnotherService;
import com.inext.service.pay.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lisige
 * @description 所有的放款订单和查询订单由该类负责 由该类决定到底该哪个放款通道走
 */
@Service
public class PaymentServiceImpl implements IPaymentService {

    /**
     * 所有的放款通道
     */
    @Autowired
    List<IPayForAnotherService> payForAnotherServices;


    @Autowired
    private IPaymentChannelDao paymentChannelDao;


    @Override
    public ApiServiceResult<PaymentChannelEnum> paymentOrder(AssetBorrowOrder assetBorrowOrder) throws Exception {

        if (payForAnotherServices == null || payForAnotherServices.isEmpty()) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "无可用的放款通道");
        }

        PaymentChannel paymentChannelParam = new PaymentChannel();
        paymentChannelParam.setEnable(Constants.ENABLE_VALID);
        List<PaymentChannel> paymentChannels = this.paymentChannelDao.selectListOrderBySort(paymentChannelParam);

        if (paymentChannels == null || paymentChannels.isEmpty()) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "系统未配置可用的放款通道");
        }

        // 以筛选并根据优先级排序之后的可用的放款通道循环调用尝试放款
        for (int i = 0; i < paymentChannels.size(); i++) {
            PaymentChannel paymentChannel = paymentChannels.get(i);

            IPayForAnotherService payForAnotherService = (IPayForAnotherService)
                    SpringContextAware.getApplicationContext().getBean(paymentChannel.getServiceName());
            // 如果满足条件的话执行该通道的放款操作
            if (payForAnotherService.verifyMakeLoansCondition(assetBorrowOrder, paymentChannel.getId()).isSuccessed()) {
                return payForAnotherService.paymentApi(assetBorrowOrder);
            }
        }

        return new ApiServiceResult(ApiStatus.FAIL.getCode(), "暂无满足条件的放款通道");
    }

    @Override
    public ApiServiceResult queryOrder(AssetBorrowOrder assetBorrowOrder) throws Exception {

        if (payForAnotherServices == null || payForAnotherServices.isEmpty()) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "系统内无可用的放款通道,无法查询");
        }

        String serviceBeanName = PaymentChannelEnum.getServiceName(assetBorrowOrder.getPaymentChannel());
        Object payForAnotherService = SpringContextAware.getApplicationContext().getBean(serviceBeanName);
        if (payForAnotherService == null) {
            return new ApiServiceResult(ApiStatus.FAIL.getCode(), "未知的放款通道,无法查询");
        }

        return ((IPayForAnotherService) payForAnotherService).queryPaymentApi(assetBorrowOrder);


    }
}
