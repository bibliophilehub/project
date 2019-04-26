package com.inext.tasks;

import com.inext.service.IAssetRepaymentOrderService;
import com.inext.service.kq.webservice.KqPaySevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * 添加黑名单
 */

@Configuration
@EnableScheduling // 启用定时任务
public class ppdTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ppdTask.class);

    public String ENCODING = "utf-8";
    public String memberCode = "10012138842";//"10012151864";//"10012140357";

    @Resource
    private IAssetRepaymentOrderService assetRepaymentOrderService;

    @Resource
    private KqPaySevice kqPaySevice;
    /**
     * 批量扣款
     * 每天8点、12点、下午7点进行代扣
     */
    //@Scheduled(cron = "11 5 0 * * ?")
    //@Scheduled(cron = "1 0 8,12,19 * * ?")
//    public void doBatchBatch() {
//        kqPaySevice.advDebitBatch();
////    	System.out.print("处理1");
//    }

    /**
     * 批次查询扣款状态
     * 每隔5分钟执行一次
     */
    //@Scheduled(cron = "0 0/5 * * * ? ")
//    public void doOrderQuery() throws Exception {
//        kqPaySevice.pkiBatchQuery();
////    	System.out.print("处理2");
//    }

    public static void main(String[] args) {
//        ppdTask task = new ppdTask();

    }

}
