package com.inext.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 流量统计
 *
 * @author gaoyuhai
 * 2016-11-25 下午03:09:07
 */
@Aspect
@Component
public class ControllerCountAspect {

    private static Logger loger = LoggerFactory.getLogger(ControllerCountAspect.class);

}
