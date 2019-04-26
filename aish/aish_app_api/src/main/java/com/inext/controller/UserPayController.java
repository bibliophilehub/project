package com.inext.controller;


import com.inext.result.ResponseDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户借款记录
 * ios 专用
 * @param ios 专用
 * @author
 */
@Controller
@RequestMapping("credit-pay/")
public class UserPayController extends UserloanController {

    /**
     * 申请借款
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "申请借款(我要卖手机)", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "perPayMoney", value = "预付款金额", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "penaltyAmount", value = "违约金额", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "moneyDay", value = "履约期限", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "moneyAmount", value = "应退还总额", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "payPassword", value = "支付密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceNumber", value = "设备号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceModel", value = "手机型号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型(android,ios,h5) 本机参数", dataType = "String", paramType = "query")
    })
    @RequestMapping("apply-pay")
    public void applyLoan(HttpServletRequest request, HttpServletResponse response) {
        super.applyLoan(request,response);
    }
}
