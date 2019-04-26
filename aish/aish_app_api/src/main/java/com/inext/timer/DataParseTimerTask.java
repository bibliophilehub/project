package com.inext.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataParseTimerTask {


    @Scheduled(cron = "0 * * * * ? ")
    public void dataParseTimerTask() {
    }

}
