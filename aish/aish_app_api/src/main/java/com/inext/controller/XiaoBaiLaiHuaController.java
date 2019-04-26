package com.inext.controller;

import com.inext.utils.Result;
import com.inext.utils.xiaobailaihua.dto.RequestDto;
import com.inext.utils.xiaobailaihua.dto.ResultDto;
import com.inext.utils.xiaobailaihua.util.RsaUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 小白来花 全流程api对接
 */
@RestController
@RequestMapping(value = "/xblh")
public class XiaoBaiLaiHuaController extends BaseController {

    Logger logger = LoggerFactory.getLogger(XiaoBaiLaiHuaController.class);


    @ApiOperation(value = "撞库")
    @RequestMapping (value = "/getChatUrl")
    public ResultDto getChatUrl(HttpServletRequest request , RequestDto requestDto) {
        //验证签名
        boolean b = RsaUtils.rsaSignCheck("", requestDto.getBiz_data(), requestDto.getSign());
        if (!b){

        }
        return null;
    }

    @ApiOperation(value = "银行卡列表")
    @PostMapping(value = "/aa1")
    public Map<String, Object> aa(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

    @ApiOperation(value = "支持开户行")
    @PostMapping(value = "/aa2")
    public Map<String, Object> bb(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

    @ApiOperation(value = "绑卡")
    @PostMapping(value = "/aa3")
    public Map<String, Object> cc(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

    @ApiOperation(value = "推单")
    @PostMapping(value = "/aa4")
    public Map<String, Object> dd(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

    @ApiOperation(value = "推送补充信息")
    @PostMapping(value = "/aa5")
    public Map<String, Object> ee(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

    @ApiOperation(value = "拉取订单状态")
    @PostMapping(value = "/aa6")
    public Map<String, Object> gg(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

    @ApiOperation(value = "主动还款接口")
    @PostMapping(value = "/aa7")
    public Map<String, Object> sa(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

    @ApiOperation(value = "还款计划拉取接口")
    @PostMapping(value = "/aa8")
    public Map<String, Object> sdf(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

    @ApiOperation(value = "获取协议接口")
    @PostMapping(value = "/aa9")
    public Map<String, Object> sd(HttpServletRequest request, HttpServletResponse response) {
       return null;
    }

}
