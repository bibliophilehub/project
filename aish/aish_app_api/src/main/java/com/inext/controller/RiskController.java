package com.inext.controller;

import com.inext.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lisige
 */
@RestController
public class RiskController {


    @Autowired
    RedisUtil redisUtil;


    public String callback() {


        return "SUCCESS";
    }

}
