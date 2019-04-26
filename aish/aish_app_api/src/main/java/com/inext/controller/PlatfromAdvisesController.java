package com.inext.controller;

import com.alibaba.fastjson.JSONObject;
import com.inext.entity.BorrowUser;
import com.inext.result.ResponseDto;
import com.inext.service.IAdviseService;
import com.inext.utils.JsonUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 意见反馈控制器
 *
 * @author admin
 */
@Controller
@RequestMapping(value = "/platfromAdvise")
public class PlatfromAdvisesController extends BaseController {
    Logger logger = LoggerFactory.getLogger(PlatfromAdvisesController.class);
    @Resource
    IAdviseService adviseService;


    @ApiOperation(value = "app用户意见反馈接口", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "adviseContent", value = "反馈内容", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/savePlatfromAdvise")
    public void savePlatfromAdvise(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        adviseService.savePlatfromAdvise(json, request, bu);
        JsonUtils.toJson(response, json);
    }
}