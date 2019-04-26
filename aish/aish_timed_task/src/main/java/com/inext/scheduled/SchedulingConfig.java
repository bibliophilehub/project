package com.inext.scheduled;


import com.alibaba.fastjson.JSON;
import com.inext.dto.AssetRepaymentOrderDaikouDto;
import com.inext.entity.AssetBorrowOrder;
import com.inext.entity.AssetRepaymentOrderDaikou;
import com.inext.entity.BackConfigParams;
import com.inext.enumerate.ApiStatus;
import com.inext.enums.DaiKouBusinessType;
import com.inext.result.ApiServiceResult;
import com.inext.service.*;
import com.inext.service.dk.RepaymentDKService;
import com.inext.service.handler.DaikouHandler;
import com.inext.service.handler.invocation.DKParam;
import com.inext.service.handler.invocation.DaiKouRedisKey;
import com.inext.utils.RedisUtil;
import com.inext.utils.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务配置类
 * Created by Administrator on 2017/6/5 0005.
 */
@Configuration
@EnableScheduling // 启用定时任务
public class SchedulingConfig {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ILoanStatisticsService loanStatisticsService;
    @Resource
    private IRepaymentStatisticsService repaymentStatisticsService;
    @Resource
    private IAssetBorrowOrderService assetBorrowOrderService;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Resource
    private IZmService zmService;

    @Resource
    private IChannelStatisticsPerdayService channelStatisticsPerdayService;
    @Resource
    private IChannelStatisticsService channelStatisticsService;

    @Resource
    private RepaymentDKService repaymentDKService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RepaymentOrderDaikouService repaymentOrderDaikouService;


    private final int pageSize = 10;

    @Autowired
    DaikouHandler daikouHandler;

    @Resource
    private IAssetRepaymentOrderService iAssetRepaymentOrderService;

    @Resource
    private IBorrowUserService userService;


    /**
     * 代付
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateLoanTerm() {
        // 可以在后台设置打款开关
        Map<String, String> mapFee = backConfigParamsService.getBackConfig(BackConfigParams.FEE_ACCOUNT, null);
        //用户借款打款开关
        String userFeeIsopen = mapFee.get("user_fee_isopen");
        logger.info("用户借款打款开关 结果为 {}", userFeeIsopen);
        if (!"1".equals(userFeeIsopen)) {
            return;
        }
        HashMap<String, Object> paramsM = new HashMap<String, Object>();
        paramsM.put("status", AssetBorrowOrder.STATUS_DFK);
        paramsM.put("payStatus", AssetBorrowOrder.SUB_PAY_CSZT);
        paramsM.put("querylimit", 28);
        List<AssetBorrowOrder> bos = assetBorrowOrderService.findAll(paramsM);
        if (bos != null && bos.size() > 0) {

            //请求打款
            try {
                assetBorrowOrderService.payForAnother(bos);
            } catch (Exception e) {
                logger.error("代付异常", e);
            }

        }
    }

    /**
     * 查询打款状态  lianLian代付
     */
    @Scheduled(cron = "0 * * * * ?")
    public void queryPaysStateLianLian() {

        //放款中的状态全部查询 28
        HashMap<String, Object> paramsM = new HashMap<String, Object>();
        paramsM.put("status", AssetBorrowOrder.STATUS_FKZ);
        paramsM.put("querylimit", 28);
        List<AssetBorrowOrder> bos = assetBorrowOrderService.findAll(paramsM);
        if (bos != null && bos.size() > 0) {

            //查询
            // assetBorrowOrderService.queryPaysStateLLianUser(bos);

            try {
                assetBorrowOrderService.queryPayForAnother(bos);
            } catch (Exception e) {
                logger.error("代付对账异常", e);
            }
        }

    }


    //每天00:05:00 开始跑逾期任务
    @Scheduled(cron = "0 5 0 * * ?")
    public void overdue() {
        logger.info("======overdue======");
        assetBorrowOrderService.overdueTask();
    }

    //履约期前一天早上10:00发短信提醒还款
    @Scheduled(cron = "0 1 10 * * ?")
    public void sendRemindMessageGap1() {
        assetBorrowOrderService.sendRemindMessage(1);
    }

    //履约期最后一天早上10:00&下午5:00发短信提醒还款
    @Scheduled(cron = "0 2 10,17 * * ?")
    public void sendRemindMessageGap0() {
        assetBorrowOrderService.sendRemindMessage(0);
    }

    //已违约第一天早上10:00发短信提醒还款
    @Scheduled(cron = "0 57 9 * * ?")
    public void sendRemindMessageGapf1() {
        assetBorrowOrderService.sendRemindMessage(-1);
    }

    //每天00:10:00 芝麻认证过期的设置为未授权
    @Scheduled(cron = "0 10 0 * * ?")
    public void zmAuthExpire() {
        logger.info("芝麻认证过期查询开始...");
        zmService.zmAuthExpire();
    }

    //每天早上10:00查询前一天注册，但是5项信息没有完全认证完成的客户，
    @Scheduled(cron = "0 1 10 * * ?")
    public void sendUnIdentification() {
        assetBorrowOrderService.sendUnIdentification();
    }

    /**
     * 代扣job,每天8点、11点、下午6点进行代扣
     */
    @Scheduled(cron = "0 0 8,11,18 * * ?")
    public void doDaiKou() {
//        repaymentDKService.doRepaymenytDK();
    }

    /**
     * 代扣结果查询job
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void queryPayStatusDaiKou() {
//        logger.info("代扣查询结果被调用");
//        String dKQueryRedisKey = DaiKouRedisKey.DK_QUERY.getCode();
//
//        if (redisUtil.get(dKQueryRedisKey) != null) {
//            logger.info("doDKQueryPay 正在跑.." + dKQueryRedisKey);
//            return;
//        }
//        try {
//            redisUtil.set(dKQueryRedisKey, dKQueryRedisKey, 60 * 60l);//过期时间一个小时
//            /* AssetRepaymentOrderDaikouDto dto = new AssetRepaymentOrderDaikouDto();
//            dto.setPayBussiness(DaiKouBusinessType.UCF.getValue());*/
//            int totalRecord = repaymentOrderDaikouService.calculationDKQueryNum(null);
//            logger.info("doDKQueryPay 初始化需查询：" + totalRecord);
//            if (totalRecord <= 0) {
//                return;
//            }
//            int totalPage = 0;
//
//            if (totalRecord > 0) {
//                if (totalRecord % pageSize > 0) {
//                    totalPage = totalRecord / pageSize + 1;
//                } else {
//                    totalPage = totalRecord / pageSize;
//                }
//            }
//
//            for (int i = 1; i < totalPage + 1; i++) {
//                final int pageNo = i;
//
//                ThreadPool.getInstance().run(new Runnable() {
//                    @Override
//                    public void run() {
//                        queryDKOrder(pageNo, pageSize);
//                    }
//                });
//
//            }
//        } catch (Exception e) {
//            logger.error("代扣查询异常：", e.getMessage());
//            redisUtil.remove(dKQueryRedisKey);
//        } finally {
//            redisUtil.remove(dKQueryRedisKey);
//        }
    }

//    private void queryDKOrder(Integer pageNum, Integer pageSize) {
//        AssetRepaymentOrderDaikouDto dto = new AssetRepaymentOrderDaikouDto();
//        /* dto.setPayBussiness(DaiKouBusinessType.UCF.getValue());*/
//        dto.setPageNum(pageNum);
//        dto.setPageSize(pageSize);
//        List<AssetRepaymentOrderDaikou> list = repaymentOrderDaikouService.getQueryDKList(dto);
//        //请求查询
//        for (final AssetRepaymentOrderDaikou repaymentDK : list) {
//            final Integer daikouId = repaymentDK.getId();
//            //发起代扣锁住的redis key
//            final String dkKey = "doDKTask_repaymentId_" + repaymentDK.getRepaymentOrderId();
//            //本次查询发起的key
//            final String key = "doDKQueryTask_daikouId_" + daikouId;
//
//            try {
//                if (redisUtil.get(key) != null) {
//                    logger.info("doDKQueryPay 当前订单已经在处理了...key=" + key);
//                    continue;
//                }
//
//                redisUtil.set(key, key, 60 * 60l);
//
//                ThreadPool.getInstance().run(new Runnable() {
//                    @Override
//                    public void run() {
//                        //并发加锁key
//                        final String lockKey = RedisUtil.ORDER_PAY_SUBFIX + repaymentDK.getRepaymentOrderId();
//                        try {
//                            //执行代扣查询
//                            DKParam request = new DKParam();
//                            String topic = DaiKouBusinessType.getCodeByVaule(repaymentDK.getPayBussiness());
//                            request.setTopic(topic);
//                            request.setRepayMentDaiKou(repaymentDK);
//                            ApiServiceResult<Object> result = daikouHandler.query(request);
//                            logger.info("doDKQueryPay 查询=" + JSON.toJSONString(result));
//                            String code = result.getCode();
//                            //代付失败、成功、异常时释放该支付方式的锁
//                            if (code.equals(ApiStatus.SUCCESS.getCode()))//代付成功
//                            {
//                                redisUtil.unlock(lockKey);
//                                logger.info("doDKQueryPay success:" + JSON.toJSONString(result));
//                            } else if (code.equals(ApiStatus.FAIL.getCode()))//代付失败
//                            {
//                                //代扣失败时，释放发起请求缓存该笔订单锁
//                                redisUtil.remove(dkKey);
//                                redisUtil.unlock(lockKey);
//                                logger.info("doDKQueryPay fail:" + JSON.toJSONString(result));
//                            } else//查询结果中，处理中.....
//                            {
//                                redisUtil.remove(key);
//                                logger.info("doDKQueryPay run 0 代扣订单 处理中，稍后重试,key=" + key + "----失败原因：" + JSON.toJSONString(result));
//                            }
//                        } catch (Exception e) {
//                            redisUtil.remove(key);
//                            logger.error("doDKQueryPay run 1 代扣订单 处理失败，稍后重试,key=" + key, e);
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                redisUtil.remove(key);
//                logger.error("doDKQueryPay run 2 代扣订单 处理失败，稍后重试,key=" + key);
//            }
//        }
//    }


    //每天00:12:00 统计渠道pv uv
    @Scheduled(cron = "0 12 0 * * ?")
    public void channelStatistics() {
        logger.info("统计渠道开始...");
        channelStatisticsPerdayService.channelStatistics(null);
    }

    //每天00:15:00 渠道结算统计
    @Scheduled(cron = "0 15 0 * * ?")
    public void channelBalanceStatistics() {
        logger.info("渠道结算统计...");
        channelStatisticsPerdayService.channelBalanceStatistics(null);
    }


    /**
     * 每天零点三十分更新昨天的渠道统计数据
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void updatePreviousChannelStatistics() {
        logger.info("更新昨天的渠道统计数据的任务开始...");
        channelStatisticsService.updatePreviousChannelStatistics();
    }

    /**
     * 每小时更新今天的渠道统计数据
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void updateTodayChannelStatistics() {
        logger.info("更新今天的渠道统计数据的任务开始...");
        channelStatisticsService.updateTodayChannelStatistics();
    }

    /** ---------------------------------------*/

//    //每天00:45:00 开始跑芝麻信用是否过期任务
//    @Scheduled(cron = "0 45 0 * * ?")
//    public void zhima() {
//        logger.info("======zhima======");
//        userService.overdueTask();
//    }


//    //下面是往催收推送数据的=========================================================
//    //每天00:15:00 开始跑已逾期订单推送催收系统
//    @Scheduled(cron = "0 15 1 * * ?")
//    public void pushCuiShouTask()
//    {
//        logger.info("==(======pushcuishou======");
//        iAssetRepaymentOrderService.pushCuiShouTask();
//    }
//
//    //每天定时2分钟推送逾期订单已还款到催收系统
//    @Scheduled(cron ="0 */2 * * * ?")
//    public void pushCuiShouRepayment(){
//        logger.info("==(======pushCuiShouRepayment======");
//        iAssetRepaymentOrderService.findRaymentedHuanKuan();
//    }
//
//    //推送提醒订单到催收
//    //@Scheduled(cron = "0 0 3 * * ?")
//    public void queryTomOvue(){
//        logger.info("==(=======明天逾期查询被调用");
//        this.iAssetRepaymentOrderService.findCurrentOverdue();
//        logger.info("==(=======明天逾期查询调用结束");
//    }
//
//    //每天定时3分钟推送未逾期已还款
//    // @Scheduled(cron ="0 */3 * * * ?")
//    public void pushCuishouNotOverdue(){
//        logger.info("-------------------------pushCuiShouNotOverdue未逾期已还款任务开始");
//        iAssetRepaymentOrderService.pushCuishouNotOverdue();
//    }

}
