package com.inext.service.dk.impl;

import com.bill99.asap.exception.CryptoException;
import com.inext.dto.AssetRepaymentOrderByWithholdDto;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.AssetRepaymentOrderWithhold;
import com.inext.service.*;
import com.inext.service.dk.BaoFooDkService;
import com.inext.service.handler.invocation.DaiKouRedisKey;
import com.inext.utils.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class BaoFooDkServiceImpl implements BaoFooDkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaoFooDkServiceImpl.class);

    @Autowired
    private IAssetRepaymentOrderService assetRepaymentOrderService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    IAssetBorrowOrderService borrowOrderService;
    @Autowired
    IAssetOrderStatusHisService orderStatusHisService;
    @Autowired
    IAssetRepaymentOrderDetailService repaymentOrderDetailService;
    @Autowired
    IBorrowUserService borrowUserService;
    @Autowired
    private IBackConfigParamsService backConfigParamsService;
    @Autowired
    IRepaymentService iRepaymentService;

    private Map<String, Object> map = new HashMap<>();;

    /***
     * 宝付免签约代扣
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    @Override
    public void doDebitBatch() {
        String dKRedisKey = DaiKouRedisKey.DK.getCode();

        if (redisUtil.get(dKRedisKey) != null) {
            LOGGER.error("doDKPay 正在跑.." + dKRedisKey);
            return;
        }
        try {
            redisUtil.set(dKRedisKey, dKRedisKey, 60 * 60L);//过期时间一个小时
            map.clear();
            map.put("repaymentTime", new Date());
            List<AssetRepaymentOrderByWithholdDto> list = assetRepaymentOrderService.getListByWithhold(map);
            if (CollectionUtils.isEmpty(list)) {
                return;
            }

            List<Integer> orderStatuses = Arrays.asList(AssetOrderStatusHis.STATUS_YHK);
            //
            list.forEach(p ->{
                try {
                    //过滤招商银行卡
//                if(p.getCardType()==3){
//                    dkFlag=false;
//                }
                    //判断当前订单是否还清、逾期还清
                    boolean dkFlag=true;
                    AssetRepaymentOrder repaymentOrder = assetRepaymentOrderService.getRepaymentByOrderId(p.getOrderId());
                    if (orderStatuses.contains(repaymentOrder.getOrderStatus().intValue())) {
                        LOGGER.info("扣款:当前订单已经还清  还款id=" + p.getId());
                        dkFlag=false;
                    }
                    if(dkFlag && p.getUserId() != null && p.getOrderId() != null){
                        //代扣状态为true，则执行代扣
                        iRepaymentService.baoFooConfirmPay(p.getUserId(), p.getOrderId(), 1, 1);
                    }
                } catch (Exception e) {
                    LOGGER.error("BaoFooDkServiceImpl >>> doDebitBatch 扣款处理结果异常:{}", ExceptionUtils.getStackTrace(e));
                } finally {
                    redisUtil.unlock(RedisUtil.ORDER_PAY_SUBFIX + p.getId());
                }
            });
        } catch (Exception e) {
            redisUtil.remove(dKRedisKey);
            LOGGER.info("BaoFooDkServiceImpl >>> doDebitBatch 扣款处理异常:{}", e.getMessage());
        } finally {
            redisUtil.remove(dKRedisKey);
        }
    }

    /**
     * 明细查询
     *
     * @throws JiBXException
     * @throws IOException
     * @throws CryptoException
     */
    @Override
    public void debitQuery(AssetRepaymentOrderWithhold withhold){
        try {

        }catch (Exception e){
            LOGGER.error("BaoFooDkServiceImpl  >>> debitQuery 扣款明细查询异常：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}

