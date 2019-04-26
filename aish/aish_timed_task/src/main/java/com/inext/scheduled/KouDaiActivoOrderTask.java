package com.inext.scheduled;

import com.inext.entity.AssetBorrowOrder;
import com.inext.enums.PaymentChannelEnum;
import com.inext.result.ApiServiceResult;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IKouDaiActivoOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 口袋理财资产订单推送任务
 *
 * @author lisige
 */
@Configuration
public class KouDaiActivoOrderTask {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IAssetBorrowOrderService assetBorrowOrderService;

    @Autowired
    private IKouDaiActivoOrderService kouDaiActivoOrderService;

    /**
     * 每分钟执行口袋理财资产订单推送扫描
     */
    /* @Scheduled(cron = "35 * * * * ?")
    public void createActivoOrder() {
        HashMap<String, Object> paramsM = new HashMap<String, Object>();
        paramsM.put("status", AssetBorrowOrder.STATUS_YFK);
        paramsM.put("paymentChannel", PaymentChannelEnum.KOUDAI.getCode());
        paramsM.put("querylimit", 30);
        paramsM.put("isPush", "0");
        List<AssetBorrowOrder> bos = assetBorrowOrderService.findAll(paramsM);
        if (bos != null && bos.size() > 0) {
            for (int i = 0; i < bos.size(); i++) {

                try {
                    ApiServiceResult apiServiceResult = kouDaiActivoOrderService.createActivoOrder(bos.get(i));
                    if (apiServiceResult.isSuccessed()) {
                        AssetBorrowOrder assetBorrowOrder = new AssetBorrowOrder();
                        assetBorrowOrder.setId(bos.get(i).getId());
                        assetBorrowOrder.setIsPush(1);
                        assetBorrowOrderService.updateByPrimaryKeySelective(assetBorrowOrder);
                    }

                } catch (Exception e) {
                    logger.error("推送到口袋理财的资产订单异常", e);
                }

            }

        }
    } */
}
