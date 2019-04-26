package com.inext.scheduled;

import org.springframework.stereotype.Component;

/**
 * Created by 李思鸽 on 2017/5/5 0005.
 */
@Component("timedTask")
public class TimedTask {

    public void testQuartz() {
        System.err.println("this system current time:" + System.currentTimeMillis());
    }
}
