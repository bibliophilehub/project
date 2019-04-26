package com.inext.service;

import com.inext.Application;
import com.inext.service.pay.IPayForAnotherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by 李思鸽 on 2017/3/15 0015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductInfoServiceTest {

    @Autowired
    private List<IPayForAnotherService> payForAnotherServices;

    @Test
    public void testAutowrite(){
        System.err.println(payForAnotherServices.isEmpty());
    }
}
