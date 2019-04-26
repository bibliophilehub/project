package com.inext.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by 李思鸽 on 2017/3/14 0014.
 */
@Configuration
@ImportResource(locations= {"classpath:client.xml"})
public class PpdXmlConfig {

}
