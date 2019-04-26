package com.inext.tasks;

import com.inext.service.dk.BaoFooDkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * 宝付代扣
 */
@Configuration
@EnableScheduling // 启用定时任务
public class BaoFooDkTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaoFooDkTask.class);

    public String ENCODING = "utf-8";

    @Resource
    private BaoFooDkService baoFooDkService;
    /**
     * 批量扣款
     * 每天8点、12点、下午7点进行代扣
     */
    @Scheduled(cron = "1 0 8,12,19 * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void doBaoFooDk() {
        baoFooDkService.doDebitBatch();
    }

}
