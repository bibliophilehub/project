package com.inext.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by 李思鸽 on 2017/3/14 0014.
 */
@Configuration
// 设置session时间为 8h
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30)
public class RedisSessionConfig {

}
