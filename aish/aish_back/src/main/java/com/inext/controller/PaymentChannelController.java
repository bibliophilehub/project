package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.entity.PaymentChannel;
import com.inext.result.AjaxResult;
import com.inext.service.IPaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author lisige
 */
@RestController
@RequestMapping("paymentChannel")
public class PaymentChannelController extends BaseController {

    @Autowired
    IPaymentChannelService paymentChannelService;


    @GetMapping
    public ModelAndView selectPaging(HttpServletRequest request) {

        Map<String, Object> params = this.getParametersO(request);
        PageInfo<PaymentChannel> page = paymentChannelService.selectPaging(params);
        ModelAndView modelAndView = new ModelAndView("v2/paymentChannel/paymentChannelList");
        modelAndView.addObject("pageInfo", page);
        return modelAndView;
    }


    @PostMapping
    public AjaxResult update(PaymentChannel paymentChannel) {

        this.paymentChannelService.update(paymentChannel);
        return new AjaxResult();
    }
}
