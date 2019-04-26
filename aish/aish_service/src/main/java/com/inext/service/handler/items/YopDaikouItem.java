
package com.inext.service.handler.items;

import com.alibaba.fastjson.JSON;
import com.inext.dto.AssetRepaymentOrderDaikouDto;
import com.inext.dto.AssetRepaymentOrderDto;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.AssetRepaymentOrderDaikou;
import com.inext.enumerate.ApiStatus;
import com.inext.enums.RepaymentLockMsgEnum;
import com.inext.result.ApiServiceResult;
import com.inext.service.IAssetRepaymentOrderService;
import com.inext.service.RepaymentOrderDaikouService;
import com.inext.service.handler.invocation.DKParam;
import com.inext.service.rmi.webservice.YeePayService;
import com.inext.utils.DateUtils;
import com.inext.utils.RedisUtil;
import com.inext.utils.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 易宝处理类
 * @编码实现人员 lihong
 * @需求提出人员 TODO 填写需求填写人员
 * @实现日期 2018年6月25日
 */
@Component
public class YopDaikouItem extends BasicPayItem
{
    private static Logger LOGGER = LoggerFactory.getLogger(YopDaikouItem.class);

    @Autowired
    private IAssetRepaymentOrderService repaymentOrderService;
    @Autowired
    private YeePayService yeePayService;
    @Autowired
    private RepaymentOrderDaikouService repaymentOrderDaikouService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DataSourceTransactionManager txManager;

    @Override
    public ApiServiceResult<Object> excute(DKParam request) throws Exception
    {
        LOGGER.info("易宝代扣处理类被调用");
        AssetRepaymentOrder order = request.getRepaymentOrder();
        ApiServiceResult<Object> result = yeePayService.autoFirstPay(order);
        return result;
    }

    /**
     * 
     * TODO 代扣子线程
     * @author wxy
     * @date 2018年7月6日
     * @param pageNum
     * @param pageSize
     * @param mapFee
     */
    @SuppressWarnings("unused")
    @Deprecated
    private void yopDKJob(Integer pageNum, Integer pageSize, Map<String, String> mapFee)
    {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务

        TransactionStatus txStatus = txManager.getTransaction(def);
        //请求代扣

        Integer statuses[] = new Integer[] {//
            AssetOrderStatusHis.STATUS_DHK, //  待还款
            AssetOrderStatusHis.STATUS_YYQ, //  已逾期
            AssetOrderStatusHis.STATUS_YXQ, // 已续期
            AssetOrderStatusHis.STATUS_BHG, // 不合格
        };
        AssetRepaymentOrderDto dto = new AssetRepaymentOrderDto();
        dto.setStatuses(statuses);
        dto.setWithholdingTime(DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        List<AssetRepaymentOrder> list = repaymentOrderService.getYOPDKList(dto);

        for (AssetRepaymentOrder order : list)
        {
            final Integer repaymentId = order.getId();
            final String key = "doYOPDKTask_repaymentId_" + repaymentId;
            final String lockKey = RedisUtil.ORDER_PAY_SUBFIX + repaymentId;
            final String time = System.currentTimeMillis() + RedisUtil.ORDER_PAY_LOCK_TIMEOUT + "";
            final long timeout = RedisUtil.ORDER_PAY_LOCK_TIMEOUT;

            //判断当前订单是否还清、逾期还清
            List<Integer> orderStatuses = Arrays.asList(//
                AssetOrderStatusHis.STATUS_YHK);

            if (orderStatuses.contains(order.getOrderStatus().intValue()))
            {
                LOGGER.info("易宝代扣:当前订单已经还清  还款id=" + order.getId());
                continue;
            }
            //拿锁  拿到锁则可以代扣，否则不可以代扣
            if (!redisUtil.lock(lockKey, time))
            {
                String msg = "订单正在支付处理中";
                Object value = redisUtil.get(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG);
                if (value != null)
                {
                    msg = RepaymentLockMsgEnum.getDescByValue(Integer.valueOf(value.toString()));
                }
                LOGGER.info("doYOPDKPayTask 当前订单{},已经在进行其它方式还款:{}", lockKey, msg);
                continue;
            }
            //设置锁 消息提示
            redisUtil.set(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG, RepaymentLockMsgEnum.SYSTEM_DK.getValue(), timeout / 1000);
            try
            {
                if (redisUtil.get(key) != null)
                {
                    LOGGER.info("doYOPDKPayTask 当前订单已经在处理了...key=" + key);
                    continue;
                }
                redisUtil.set(key, key, 60 * 60l);
                /*Boolean flag = canDk(order, mapFee);
                if (!flag)
                {
                    continue;
                }*/

                ThreadPool.getInstance().run(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            ApiServiceResult<Object> result = yeePayService.autoFirstPay(order);
                            LOGGER.info("doYOPDKPayTask 易宝代扣results=" + JSON.toJSONString(result));
                            if (result.isSuccessed())
                            {
                                LOGGER.info("doYOPDKPayTask success:" + JSON.toJSONString(result));
                            }
                            else
                            {
                                redisUtil.remove(key);
                                redisUtil.unlock(lockKey);
                                LOGGER.info("doYOPDKPayTask run 0 易宝代扣订单 处理失败，稍后重试,并释放当前支付方式,key=" + key + "----失败原因：" + JSON.toJSONString(result));
                            }
                        }
                        catch (Exception e)
                        {
                            redisUtil.remove(key);
                            redisUtil.unlock(lockKey);
                            LOGGER.error("doYOPDKPayTask run 1 易宝代扣订单 处理失败，稍后重试,key=" + key, e);
                        }
                    }
                });
            }
            catch (Exception e)
            {
                redisUtil.remove(key);
                redisUtil.unlock(lockKey);
                LOGGER.info("易宝代扣错误：", e.getMessage());
                LOGGER.error("doYOPDKPayTask run 2 易宝代扣订单 处理失败，稍后重试,key=" + key);
                txManager.rollback(txStatus);
            }
            finally
            {
                txManager.commit(txStatus);
            }
        }
    }

    /**
     * 
     * TODO 该用户是否可以继续代扣
     * @author wxy
     * @date 2018年6月28日
     * @param order
     * @return true 可以继续代扣请求 <br/>
     *         false 不可以进行代扣
     */
    @SuppressWarnings("unused")
    @Deprecated
    private Boolean canDk(AssetRepaymentOrder order, Map<String, String> mapFee)
    {
        Date now = new Date();
        //易宝每天代扣次数
        int dayTime = Integer.parseInt(mapFee.get("DAY_DK_TIME"));
        //易宝代扣总次数
        int totalTime = Integer.parseInt(mapFee.get("TOTAL_DK_TIME"));

        Date beginCreateTime = DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");

        Date endCreateTime = DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");

        AssetRepaymentOrderDaikouDto dayDK = new AssetRepaymentOrderDaikouDto();
        dayDK.setRepaymentOrderId(order.getId());
        dayDK.setBeginCreateTime(beginCreateTime);
        dayDK.setEndCreateTime(endCreateTime);
        int userDayDKCount = repaymentOrderDaikouService.countDayRepaymentDKNum(dayDK);

        AssetRepaymentOrderDaikouDto totalDK = new AssetRepaymentOrderDaikouDto();
        totalDK.setRepaymentOrderId(order.getId());
        int userTotalDKCount = repaymentOrderDaikouService.countTotalRepaymentDKNum(totalDK);

        LOGGER.info("易宝每天可代扣次数:{},易宝一共可以代扣次数:{},用户已经代扣次数:{},用户代扣总数:{}", dayTime, totalTime, userDayDKCount, userTotalDKCount);
        if (userTotalDKCount > totalTime)
        {
            LOGGER.info("用户:{},该用户id为:{},总代扣次数已达上限", order.getUserName(), order.getUserId());
            return false;
        }
        if (userDayDKCount > dayTime)
        {
            LOGGER.info("用户:{},该用户id为:{},每天可代扣次数已达上限", order.getUserName(), order.getUserId());
            return false;
        }
        return true;
    }

    /**
     * 
     * 开闭原则
     * 
     */

    @Override
    public String getMessageTopic()
    {
        return "yop";
    }

    @Override
    public ApiServiceResult<Object> query(DKParam request) throws Exception
    {
        LOGGER.info("易宝代扣处理查询被调用");
        AssetRepaymentOrderDaikou repaymentDK = request.getRepayMentDaiKou();
        ApiServiceResult<Object> result = yeePayService.queryFirstPay(repaymentDK);
        return result;
    }

    @SuppressWarnings("unused")
    @Deprecated
    private void queryDKOrder(Integer pageNum, Integer pageSize)
    {
        AssetRepaymentOrderDaikouDto dto = new AssetRepaymentOrderDaikouDto();
        dto.setPayBussiness(1);
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        List<AssetRepaymentOrderDaikou> list = repaymentOrderDaikouService.getQueryDKList(dto);

        //请求查询
        for (final AssetRepaymentOrderDaikou repaymentDK : list)
        {
            final Integer daikouId = repaymentDK.getId();
            final String dkKey = "doYOPDKTask_repaymentId_" + repaymentDK.getRepaymentOrderId();
            final String key = "doYOPDKQueryTask_daikouId_" + daikouId;
            final String lockKey = RedisUtil.ORDER_PAY_SUBFIX + repaymentDK.getRepaymentOrderId();
            try
            {
                if (redisUtil.get(key) != null)
                {
                    LOGGER.info("doYOPDKQueryPay 当前订单已经在处理了...key=" + key);
                    continue;
                }

                redisUtil.set(key, key, 60 * 60l);

                ThreadPool.getInstance().run(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            ApiServiceResult<Object> result = yeePayService.queryFirstPay(repaymentDK);
                            LOGGER.info("doYOPDKQueryPay 易宝查询=" + JSON.toJSONString(result));

                            String code = result.getCode();
                            //易宝代付失败、成功、异常时释放该支付方式的锁
                            if (code.equals(ApiStatus.SUCCESS.getCode()))
                            {
                                redisUtil.unlock(lockKey);
                                LOGGER.info("doYOPDKQueryPay success:" + JSON.toJSONString(result));
                            }
                            else if (code.equals(ApiStatus.FAIL.getCode()))
                            {
                                redisUtil.remove(dkKey);
                                redisUtil.unlock(lockKey);
                                LOGGER.info("doYOPDKQueryPay fail:" + JSON.toJSONString(result));
                            }
                            else if (code.equals(ApiStatus.ERROR.getCode()))
                            {
                                redisUtil.remove(dkKey);
                                LOGGER.info("doYOPDKQueryPay error:" + JSON.toJSONString(result));
                            }
                            else
                            {
                                redisUtil.remove(key);
                                LOGGER.info("doYOPDKQueryPay run 0 易宝代扣订单 处理中，稍后重试,key=" + key + "----失败原因：" + JSON.toJSONString(result));
                            }
                        }
                        catch (Exception e)
                        {
                            redisUtil.remove(key);
                            LOGGER.error("doYOPDKQueryPay run 1 易宝代扣订单 处理失败，稍后重试,key=" + key, e);
                        }
                    }
                });
            }
            catch (Exception e)
            {
                redisUtil.remove(key);
                LOGGER.error("doYOPDKQueryPay run 2 易宝代扣订单 处理失败，稍后重试,key=" + key);
            }
        }
    }
}
