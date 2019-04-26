
package com.inext.service.dk.impl;

import com.alibaba.fastjson.JSON;
import com.inext.dto.AssetRepaymentOrderDto;
import com.inext.dto.RoutingItem;
import com.inext.entity.AssetOrderStatusHis;
import com.inext.entity.AssetRepaymentOrder;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BorrowUser;
import com.inext.enums.DaiKouBusinessType;
import com.inext.enums.RepaymentLockMsgEnum;
import com.inext.result.ApiServiceResult;
import com.inext.service.IAssetRepaymentOrderService;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBorrowUserService;
import com.inext.service.dk.RepaymentDKService;
import com.inext.service.handler.DaikouHandler;
import com.inext.service.handler.invocation.DKParam;
import com.inext.service.handler.invocation.DaiKouRedisKey;
import com.inext.service.handler.items.YopDaikouItem;
import com.inext.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class RepaymentDKServiceImpl implements RepaymentDKService {
    private static Logger LOGGER = LoggerFactory.getLogger(RepaymentDKServiceImpl.class);
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Autowired
    DaikouHandler daikouHandler;
    @Autowired
    private IAssetRepaymentOrderService repaymentOrderService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    IBorrowUserService borrowUserService;
    @Autowired
    YopDaikouItem yopDaikouItem;

    private final int pageSize = 10;

    @Override
    public void doRepaymenytDK() {
        String dKRedisKey = DaiKouRedisKey.DK.getCode();

        if (redisUtil.get(dKRedisKey) != null) {
            LOGGER.error("doDKPay 正在跑.." + dKRedisKey);
            return;
        }
        try {
            // 代扣总开关
            Map<String, String> mapFee = backConfigParamsService.getBackConfig(BackConfigParams.FEE_ACCOUNT, null);
            //用户借款打款开关
            String allDkIsopen = mapFee.get("ALL_DK_ISOPEN");
            LOGGER.info("用户代扣扣款开关 结果为: {}", allDkIsopen);
            if (!"1".equals(allDkIsopen)) {
                return;
            }
            redisUtil.set(dKRedisKey, dKRedisKey, 60 * 60l);//过期时间一个小时
            Integer statuses[] = new Integer[]{//
                    AssetOrderStatusHis.STATUS_DHK, //  待还款
                    AssetOrderStatusHis.STATUS_YYQ, //  已逾期
                    //AssetOrderStatusHis.STATUS_YXQ, // 已续期
                    AssetOrderStatusHis.STATUS_BHG // 不合格
            };
            AssetRepaymentOrderDto dto = new AssetRepaymentOrderDto();
            dto.setStatuses(statuses);
            dto.setWithholdingTime(DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
            int totalRecord = repaymentOrderService.calculationDKOrderNum(dto);
            LOGGER.info("doDKPay 初始化需处理：" + totalRecord);
            if (totalRecord <= 0) {
                return;
            }

            int totalPage = 0;

            if (totalRecord > 0) {
                if (totalRecord % pageSize > 0) {
                    totalPage = totalRecord / pageSize + 1;
                } else {
                    totalPage = totalRecord / pageSize;
                }
            }
            LOGGER.info("doDKPay 共需要处理：" + totalPage + "页");
            for (int i = 1; i < totalPage + 1; i++) {
                final int pageNo = i;

                ThreadPool.getInstance().run(new Runnable() {
                    @Override
                    public void run() {
                        dkJob(pageNo, pageSize);
                    }
                });

            }
        } catch (Exception e) {
            redisUtil.remove(dKRedisKey);
            LOGGER.info("代扣处理异常:", e.getMessage());
        } finally {
            redisUtil.remove(dKRedisKey);
        }
    }

    /**
     * TODO 代扣子线程
     *
     * @param pageNum
     * @param pageSize
     * @author wxy
     * @date 2018年7月6日
     */
    private void dkJob(Integer pageNum, Integer pageSize) {
        //请求代扣
        Integer statuses[] = new Integer[]{//
               AssetOrderStatusHis.STATUS_DHK, //  待还款
                AssetOrderStatusHis.STATUS_YYQ, //  已逾期
//                AssetOrderStatusHis.STATUS_YXQ, // 已续期
                AssetOrderStatusHis.STATUS_BHG // 不合格
        };
        AssetRepaymentOrderDto dto = new AssetRepaymentOrderDto();
        dto.setStatuses(statuses);
        dto.setWithholdingTime(DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        List<AssetRepaymentOrder> list = repaymentOrderService.getDKList(dto);

        LOGGER.info("doDKPay 当前处理第:" + pageNum + "页,当前页处理条数:" + (CollectionUtils.isEmpty(list) ? 0 : list.size()));

        for (AssetRepaymentOrder order : list) {
            final Integer repaymentId = order.getId();
            final String key = "doDKTask_repaymentId_" + repaymentId;
            final String lockKey = RedisUtil.ORDER_PAY_SUBFIX + repaymentId;
            final String time = System.currentTimeMillis() + RedisUtil.ORDER_PAY_LOCK_TIMEOUT + "";
            final long timeout = RedisUtil.ORDER_PAY_LOCK_TIMEOUT;

            //判断当前订单是否还清、逾期还清
            List<Integer> orderStatuses = Arrays.asList(
                    AssetOrderStatusHis.STATUS_YHK);

            if (orderStatuses.contains(order.getOrderStatus().intValue())) {
                LOGGER.info("代扣:当前订单已经还清  还款id=" + order.getId());
                continue;
            }
            //拿锁  拿到锁则可以代扣，否则不可以代扣
            if (!redisUtil.lock(lockKey, time)) {
                String msg = "订单正在支付处理中";
                Object value = redisUtil.get(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG);
                if (value != null) {
                    msg = RepaymentLockMsgEnum.getDescByValue(Integer.valueOf(value.toString()));
                }
                LOGGER.info("doDKPayTask 当前订单{},已经在进行其它方式还款:{}", lockKey, msg);
                continue;
            }
            //设置锁 消息提示
            redisUtil.set(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG, RepaymentLockMsgEnum.SYSTEM_DK.getValue(), timeout / 1000);
            try {
                if (redisUtil.get(key) != null) {
                    LOGGER.info("doDKPayTask 当前订单已经在处理了...key=" + key);
                    continue;
                }
                redisUtil.set(key, key, 60 * 60l);

                ThreadPool.getInstance().run(new Runnable() {
                    @Override
                    public void run() {
                        //执行代扣策略
                        DKParam request = new DKParam();
                        //执行代扣策略
                        BorrowUser user = borrowUserService.getBorrowUserById(order.getUserId());

                        /**对路由规则进行修改**/
                        try {
                            String dkRouting = getDKRouting(user.getIsYop());
                            if (StringUtils.isEmpty(dkRouting)) {
                                LOGGER.info("没有代扣方被选中");
                                redisUtil.remove(key);
                                redisUtil.unlock(lockKey);
                                return;
                            }
                            request.setTopic(dkRouting);
                        } catch (Exception e) {
                            redisUtil.remove(key);
                            redisUtil.unlock(lockKey);
                            LOGGER.error("代扣路由出错" + e.getMessage());
                            return;
                        }

                        request.setRepaymentOrder(order);
                        try {
                            ApiServiceResult<Object> result = yopDaikouItem.excute(request);
                            LOGGER.info("doDKPayTask 代扣results=" + JSON.toJSONString(result));
                            if (result.isSuccessed()) {
                                LOGGER.info("doDKPayTask success:" + JSON.toJSONString(result));
                            } else {
                                redisUtil.remove(key);
                                redisUtil.unlock(lockKey);
                                LOGGER.info("doDKPayTask run 0 代扣订单 处理失败，稍后重试,并释放当前支付方式,key=" + key + "----失败原因：" + JSON.toJSONString(result));
                            }
                        } catch (Exception e) {
                            redisUtil.remove(key);
                            redisUtil.unlock(lockKey);
                            LOGGER.error("doDKPayTask run 1 代扣订单处理失败，稍后重试,key=" + key, e);
                        }
                    }

                });
            } catch (Exception e) {
                redisUtil.remove(key);
                redisUtil.unlock(lockKey);
                LOGGER.info("错误：", e.getMessage());
                LOGGER.error("doDKPayTask run 2 代扣订单 处理失败，稍后重试,key=" + key);
            }
        }
    }

    public String getDKRouting(int isYop) {


        //易宝代扣开关
        Map<String, String> yopMapFee = backConfigParamsService.getBackConfig(BackConfigParams.YIBAO, null);
        String yopDkIsopen = yopMapFee.get("DK_ISOPEN");
        LOGGER.info("易宝代扣扣款开关 结果为: {}", yopDkIsopen);
     /*   if(isYop == 1 && yopDkIsopen.equals("0")){
            LOGGER.info("为易宝绑卡用户，易宝开关未打开不能代扣: {}", yopDkIsopen);
            return null;
        }*/
        List<RoutingItem> dkRoutingList = new ArrayList<RoutingItem>();
        if (isYop == 1 && yopDkIsopen.equals("1")) {
            RoutingItem yb = new RoutingItem();
            yb.setDesc(DaiKouBusinessType.YOP.getDesc());
            yb.setRoutingName(DaiKouBusinessType.YOP.getCode());
            dkRoutingList.add(yb);
        }

        int randomNum = dkRoutingList.size();

        if (randomNum == 0) {
            LOGGER.info("没有路由: {}", randomNum);
            return null;
        }
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        int rand = secureRandom.nextInt(randomNum);
        RoutingItem selectRoutingItem = dkRoutingList.get(rand);

        LOGGER.info("选择的支付路由是{},随机数是{}", JSONUtil.beanToJson(selectRoutingItem), rand);
        return selectRoutingItem.getRoutingName();
    }

    
    /**
     * 合利宝代扣业务
     */
	@Override
	public void doSubstitute(String channel) {
		if(channel.equals("合利宝")){
			String dKRedisKey = DaiKouRedisKey.DK.getCode();

	        if (redisUtil.get(dKRedisKey) != null) {
	            LOGGER.error("doDKPay 正在跑.." + dKRedisKey);
	            return;
	        }
	        try {
	            // 代扣总开关
	            Map<String, String> mapFee = backConfigParamsService.getBackConfig(BackConfigParams.FEE_ACCOUNT, null);
	            //用户借款打款开关
	            String allDkIsopen = mapFee.get("ALL_DK_ISOPEN");
	            LOGGER.info("用户代扣扣款开关 结果为: {}", allDkIsopen);
	            if (!"1".equals(allDkIsopen)) {
	                return;
	            }
	            redisUtil.set(dKRedisKey, dKRedisKey, 60 * 60l);//过期时间一个小时
	            Integer statuses[] = new Integer[]{//
	                    AssetOrderStatusHis.STATUS_DHK, //  待还款
	                    AssetOrderStatusHis.STATUS_YYQ, //  已逾期
	                    AssetOrderStatusHis.STATUS_BHG // 不合格
	            };
	            AssetRepaymentOrderDto dto = new AssetRepaymentOrderDto();
	            dto.setStatuses(statuses);
	            dto.setWithholdingTime(DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
	            int totalRecord = repaymentOrderService.calculationDKOrderNum(dto);
	            LOGGER.info("doDKPay 初始化需处理：" + totalRecord);
	            if (totalRecord <= 0) {
	                return;
	            }

	            int totalPage = 0;

	            if (totalRecord > 0) {
	                if (totalRecord % pageSize > 0) {
	                    totalPage = totalRecord / pageSize + 1;
	                } else {
	                    totalPage = totalRecord / pageSize;
	                }
	            }
	            LOGGER.info("doDKPay 共需要处理：" + totalPage + "页");
	            for (int i = 1; i < totalPage + 1; i++) {
	                final int pageNo = i;

	                ThreadPool.getInstance().run(new Runnable() {
	                    @Override
	                    public void run() {
	                    	substituteJob(pageNo, pageSize);
	                    }
	                });

	            }
	        } catch (Exception e) {
	            redisUtil.remove(dKRedisKey);
	            LOGGER.info("代扣处理异常:", e.getMessage());
	        } finally {
	            redisUtil.remove(dKRedisKey);
	        }
			
		}
		
	}
	
	
    /**
     * TODO 代扣子线程
     *
     * @param pageNum
     * @param pageSize
     * @author wxy
     * @date 2018年7月6日
     */
    private void substituteJob(Integer pageNum, Integer pageSize) {
        //请求代扣
        Integer statuses[] = new Integer[]{//
               AssetOrderStatusHis.STATUS_DHK, //  待还款
                AssetOrderStatusHis.STATUS_YYQ, //  已逾期
                AssetOrderStatusHis.STATUS_BHG // 不合格
        };
        AssetRepaymentOrderDto dto = new AssetRepaymentOrderDto();
        dto.setStatuses(statuses);
        dto.setWithholdingTime(DateUtils.stringToDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        dto.setPageNum(pageNum);
        dto.setPageSize(pageSize);
        List<AssetRepaymentOrder> list = repaymentOrderService.getDKList(dto);

        LOGGER.info("doDKPay 当前处理第:" + pageNum + "页,当前页处理条数:" + (CollectionUtils.isEmpty(list) ? 0 : list.size()));

        for (AssetRepaymentOrder order : list) {
            final Integer repaymentId = order.getId();
            final String key = "doDKTask_repaymentId_" + repaymentId;
            final String lockKey = RedisUtil.ORDER_PAY_SUBFIX + repaymentId;
            final String time = System.currentTimeMillis() + RedisUtil.ORDER_PAY_LOCK_TIMEOUT + "";
            final long timeout = RedisUtil.ORDER_PAY_LOCK_TIMEOUT;

            //判断当前订单是否还清、逾期还清
            List<Integer> orderStatuses = Arrays.asList(
                    AssetOrderStatusHis.STATUS_YHK);

            if (orderStatuses.contains(order.getOrderStatus().intValue())) {
                LOGGER.info("代扣:当前订单已经还清  还款id=" + order.getId());
                continue;
            }
            //拿锁  拿到锁则可以代扣，否则不可以代扣
            if (!redisUtil.lock(lockKey, time)) {
                String msg = "订单正在支付处理中";
                Object value = redisUtil.get(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG);
                if (value != null) {
                    msg = RepaymentLockMsgEnum.getDescByValue(Integer.valueOf(value.toString()));
                }
                LOGGER.info("doDKPayTask 当前订单{},已经在进行其它方式还款:{}", lockKey, msg);
                continue;
            }
            //设置锁 消息提示
            redisUtil.set(lockKey + RedisUtil.ORDER_PAY_LOCK_MSG, RepaymentLockMsgEnum.SYSTEM_DK.getValue(), timeout / 1000);
            try {
                if (redisUtil.get(key) != null) {
                    LOGGER.info("doDKPayTask 当前订单已经在处理了...key=" + key);
                    continue;
                }
                redisUtil.set(key, key, 60 * 60l);

                ThreadPool.getInstance().run(new Runnable() {
                    @Override
                    public void run() {
                        //执行代扣策略
                        DKParam request = new DKParam();
                        //执行代扣策略
                        BorrowUser user = borrowUserService.getBorrowUserById(order.getUserId());

                        /**对路由规则进行修改**/
                        try {
                            String dkRouting = getDKRouting(user.getIsYop());
                            if (StringUtils.isEmpty(dkRouting)) {
                                LOGGER.info("没有代扣方被选中");
                                redisUtil.remove(key);
                                redisUtil.unlock(lockKey);
                                return;
                            }
                            request.setTopic(dkRouting);
                        } catch (Exception e) {
                            redisUtil.remove(key);
                            redisUtil.unlock(lockKey);
                            LOGGER.error("代扣路由出错" + e.getMessage());
                            return;
                        }

                        request.setRepaymentOrder(order);
                        try {
                            ApiServiceResult<Object> result = yopDaikouItem.excute(request);
                            LOGGER.info("doDKPayTask 代扣results=" + JSON.toJSONString(result));
                            if (result.isSuccessed()) {
                                LOGGER.info("doDKPayTask success:" + JSON.toJSONString(result));
                            } else {
                                redisUtil.remove(key);
                                redisUtil.unlock(lockKey);
                                LOGGER.info("doDKPayTask run 0 代扣订单 处理失败，稍后重试,并释放当前支付方式,key=" + key + "----失败原因：" + JSON.toJSONString(result));
                            }
                        } catch (Exception e) {
                            redisUtil.remove(key);
                            redisUtil.unlock(lockKey);
                            LOGGER.error("doDKPayTask run 1 代扣订单处理失败，稍后重试,key=" + key, e);
                        }
                    }

                });
            } catch (Exception e) {
                redisUtil.remove(key);
                redisUtil.unlock(lockKey);
                LOGGER.info("错误：", e.getMessage());
                LOGGER.error("doDKPayTask run 2 代扣订单 处理失败，稍后重试,key=" + key);
            }
        }
    }

}
