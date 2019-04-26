package com.inext.tasks;

import com.inext.result.ApiServiceResult;
import com.inext.service.CreditService;
import com.inext.utils.RedisUtil;
import com.inext.utils.StringUtils;
import com.inext.utils.ThreadPool;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: jzhang
 * @Date: 2018-03-28 0028 上午 10:53
 */

@Configuration
@EnableScheduling // 启用定时任务
public class CreditTask {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    CreditService creditService;
    /*----------------------------黑白名单相关--------------------start--------------*/
    private final int pageSize = 1000;//每次查询1000条记录

    private static final String PLATFORM="2";//platform: 2-花小侠

    private static final String OPT_TYPE_WHITE="1";//optType: 操作类型：1-设置白名单
    private static final String OPT_TYPE_BLACK="2";//optType: 操作类型：2-设置黑名单

    private static final String STATUS_DISABLE="0";//状态：0-无效
    private static final String STATUS_ENABLE="1";//状态：1-有效
    /*----------------------------黑白名单相关--------------------end--------------*/
//    @Scheduled(cron = "0 0/1 * * * ? ")
//    public void doOrderCredit() {
//        creditService.doOrderCredit();
//    }
//     @Scheduled(cron = "0 0/1 * * * ? ")
//     public void doGetJxlReport() {
//         creditService.doGetJxlReport();
//     }
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void doOrderRisk() throws Exception {
        creditService.doWuqiyouOrderRisk();
    }

    /**
     * 每天00:45:00 更新运营商认证过期的用户认证状态
     * @throws Exception
     */
    @Scheduled(cron = "0 45 0 * * ? ")
    public void doMobileAuthUpdate() throws Exception{
        creditService.doMobileAuthUpdate();
    }

    /**
     * 每天01:00:00  更新-入-白名单用户
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void doUserInWhiteList(){
        logger.info("-----------------更新-入-白名单数据-----------start-------------------------");
        String b_w_l_key = "doInWhiteList";
        if (redisUtil.get(b_w_l_key) != null) {
            logger.error("doInWhiteList 正在跑..");
            return;
        }
        try{
            redisUtil.set(b_w_l_key, b_w_l_key, 60 * 60L);//过期时间一个小时
            /*
             * 入-白名单-处理
             * */
            //查询符合-入-白名单-条件的用户-总数
            int inWhiteTotalRecord = creditService.getInWhiteListUserCount();
            if (inWhiteTotalRecord > 0) {
                int inWhiteTotalPage;
                if (inWhiteTotalRecord % pageSize > 0) {
                    inWhiteTotalPage = inWhiteTotalRecord / pageSize + 1;
                } else {
                    inWhiteTotalPage = inWhiteTotalRecord / pageSize;
                }
                logger.info("doInWhiteList-----共需要处理：" + inWhiteTotalPage + "页," + inWhiteTotalRecord + "条记录。");
                for (int i = 0; i < inWhiteTotalPage; i++) {
                    final int recordStart = pageSize * i;//分页开始
                    ThreadPool.getInstance().run(new Runnable() {
                        @Override
                        public void run() {
                            /*
                             * optType: 操作类型：1-设置白名单
                             * status: 状态：1-有效
                             * */
                            doBlackWhiteList(OPT_TYPE_WHITE, STATUS_ENABLE, recordStart, pageSize);
                        }
                    });
                }
            }


        } catch (Exception e) {
            redisUtil.remove(b_w_l_key);
            logger.info("doInWhiteList >>> 更新-入-白名单数据异常:{}", e.getMessage());
        } finally {
            redisUtil.remove(b_w_l_key);
        }
    }

    /**
     * 每天01:10:00  更新-入-名单用户
     */
    @Scheduled(cron = "0 10 1 * * ? ")
    public void doUserInBlackList(){
        logger.info("-----------------更新-入-黑名单用户-----------start-------------------------");
        String b_w_l_key = "doUserInBlackList";
        if (redisUtil.get(b_w_l_key) != null) {
            logger.error("doUserInBlackList 正在跑..");
            return;
        }
        try{
            redisUtil.set(b_w_l_key, b_w_l_key, 60 * 60L);//过期时间一个小时
            /*
             * 入-黑名单-处理
             * */
            //查询符合-入-黑名单-条件的用户
            int inBlackTotalRecord = creditService.getInBlackListUserCount();
            if (inBlackTotalRecord > 0) {
                int inBlackTotalPage;
                if (inBlackTotalRecord % pageSize > 0) {
                    inBlackTotalPage = inBlackTotalRecord / pageSize + 1;
                } else {
                    inBlackTotalPage = inBlackTotalRecord / pageSize;
                }
                logger.info("doUserInBlackList-----共需要处理：" + inBlackTotalPage + "页," + inBlackTotalRecord + "条记录。");
                for (int i = 0; i < inBlackTotalPage; i++) {
                    final int recordStart = pageSize * i;//分页开始
                    ThreadPool.getInstance().run(new Runnable() {
                        @Override
                        public void run() {
                            /*
                             * optType: 操作类型：2-设置黑名单
                             * status: 状态：1-有效
                             * */
                            doBlackWhiteList(OPT_TYPE_BLACK, STATUS_ENABLE, recordStart, pageSize);
                        }
                    });
                }
            }
        } catch (Exception e) {
            redisUtil.remove(b_w_l_key);
            logger.info("doUserInBlackList >>> 更新-入-名单用户数据异常:{}", e.getMessage());
        } finally {
            redisUtil.remove(b_w_l_key);
        }
    }

    /**
     * 每天01:20:00  更新-出-黑名单用户
     */
    @Scheduled(cron = "0 20 1 * * ? ")
    public void doUserOutBlackList(){
        logger.info("-----------------更新-出-黑名单用户数据-----------start-------------------------");
        String b_w_l_key = "doUserOutBlackList";
        if (redisUtil.get(b_w_l_key) != null) {
            logger.error("doUserOutBlackList 正在跑..");
            return;
        }
        try{
            redisUtil.set(b_w_l_key, b_w_l_key, 60 * 60L);//过期时间一个小时
            /*
             * 出-黑名单-处理
             * */
            //查询符合-出-黑名单-条件的用户
            int outBlackTotalRecord = creditService.getOutBlackListUserCount();
            if (outBlackTotalRecord > 0) {
                int outBlackTotalPage;
                if (outBlackTotalRecord % pageSize > 0) {
                    outBlackTotalPage = outBlackTotalRecord / pageSize + 1;
                } else {
                    outBlackTotalPage = outBlackTotalRecord / pageSize;
                }
                logger.info("doOutBlackList--共需要处理：" + outBlackTotalPage + "页," + outBlackTotalRecord + "条记录。");
                for (int i = 0; i < outBlackTotalPage; i++) {
                    final int recordStart = pageSize * i;//分页开始
                    ThreadPool.getInstance().run(new Runnable() {
                        @Override
                        public void run() {
                            /*
                             * optType: 操作类型：2-设置黑名单
                             * status: 状态：0-无效
                             * */
                            doBlackWhiteList(OPT_TYPE_BLACK, STATUS_DISABLE, recordStart, pageSize);
                        }
                    });
                }
            }
        } catch (Exception e) {
            redisUtil.remove(b_w_l_key);
            logger.info("doUserOutBlackList >>> 更新-出-黑白名单数据异常:{}", e.getMessage());
        } finally {
            redisUtil.remove(b_w_l_key);
        }
    }

    /**
     * 设置用户为白名单
     * @param start
     * @param pageSize
     */
    private void doBlackWhiteList(String optType, String status, int start, int pageSize){
        if(StringUtils.isEmpty(optType) || StringUtils.isEmpty(status)){
            return;
        }
        List<Map<String, String>> doList = null;
        /*-----------------白名单-----------------*/
        if(StringUtils.equals("1", optType)){
            //-出--白名单
//            if(StringUtils.equals("0", status)){
//
//            }
            //-入--白名单
            if(StringUtils.equals("1", status)) {
                //查询符合-入-白名单-条件的用户
                doList = creditService.getInWhiteListUser(start, pageSize);
            }
        }
        /*------------------黑名单----------------*/
        if(StringUtils.equals("2", optType)){
            //-出--黑名单
            if(StringUtils.equals("0", status)){
                //查询符合-出-黑名单-条件的用户
                doList = creditService.getOutBlackListUser(start, pageSize);
            }
            //-入--黑名单
            if(StringUtils.equals("1", status)){
                //查询符合-入-黑名单-条件的用户
                doList = creditService.getInBlackListUser(start, pageSize);
            }
        }
        //执行设置黑白名单操作
        if (CollectionUtils.isNotEmpty(doList)) {
            doList.forEach(u -> {
                if (StringUtils.isNotEmpty(u.get("userPhone")) && StringUtils.isNotEmpty(u.get("userName"))) {
                    /*
                     * platform: 2-花小侠
                     * optType: 操作类型：1-设置白名单，2-设置黑名单
                     * status: 状态：0-无效，1-有效
                     * */
                    ApiServiceResult result = creditService.saveBlackWhiteUser(
                            u.get("userPhone"),
                            u.get("userName"),
                            PLATFORM,
                            optType,
                            status
                    );
                    if(StringUtils.equals("1", optType)){
                        if(StringUtils.equals("0", status)){
                            logger.info(
                                    "doUserBlackWhiteList---设置用户[" + u.get("userPhone") + "][" + u.get("userName")
                                            + "]---[出-白-名单]--处理结果：" + result.getCode() + "," + result.getMsg()
                            );
                        }else if(StringUtils.equals("1", status)){
                            logger.info(
                                    "doUserBlackWhiteList---设置用户[" + u.get("userPhone") + "][" + u.get("userName")
                                            + "]---[入-白-名单]--处理结果：" + result.getCode() + "," + result.getMsg()
                            );
                        }
                    }else if(StringUtils.equals("2", optType)){
                        if(StringUtils.equals("0", status)){
                            logger.info(
                                    "doUserBlackWhiteList---设置用户[" + u.get("userPhone") + "][" + u.get("userName")
                                            + "]---[出-黑-名单]--处理结果：" + result.getCode() + "," + result.getMsg()
                            );
                        }else if(StringUtils.equals("1", status)){
                            logger.info(
                                    "doUserBlackWhiteList---设置用户[" + u.get("userPhone") + "][" + u.get("userName")
                                            + "]---[入-黑-名单]--处理结果：" + result.getCode() + "," + result.getMsg()
                            );
                        }
                    }
                }
            });
        }
    }

}
