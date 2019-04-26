package com.inext.controller;

import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.entity.BorrowUser;
import com.inext.result.ResponseDto;
import com.inext.service.IBorrowUserService;
import com.inext.service.ISmsService;
import com.inext.utils.JsonUtils;
import com.inext.utils.MD5Utils;
import com.octo.captcha.service.image.ImageCaptchaService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/sms")
public class SmsController extends BaseController {
    Logger logger = LoggerFactory.getLogger(SmsController.class);
    @Resource
    ISmsService smsService;
    @Resource
    private ImageCaptchaService captchaService;
    @Resource
    private IBorrowUserService borrowUserService;



    @ApiOperation(value = "获取验证码接口", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userPhone", value = "用户手机号码", dataType = "String", paramType = "query")/*,
            @ApiImplicitParam(name = "mothed", value = "请求验证码类型", dataType = "String", paramType = "query")*/
    })
    @RequestMapping(value = "/getSmsCode")
    public void getSmsCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("start getSmsCode mothed.......ip:" + this.getIpAddr(request));
        JSONObject json = new JSONObject();
        smsService.getSmsCode(request, json);
        logger.info("end getSmsCode mothed.......");
        JsonUtils.toJson(response, json);
    }
    
    //获取短信验证码(防攻击)
    @RequestMapping(value = "/getSmsCodeCaptcha",method = RequestMethod.POST)
    public void getSmsCodeCaptcha(HttpServletRequest request, HttpServletResponse response) {
    	logger.info("start getSmsCode mothed.......ip:" + this.getIpAddr(request));
    	JSONObject json = new JSONObject();
    	
    	
    	/*String captcha=request.getParameter("captcha");
    	if(null==captcha){
    		json.put("code", "-1");
            json.put("message", "请先填写图形验证码");
            JsonUtils.toJson(response, json);
            return;
    	}

    	if (!validateSubmit(request, response)) {
    		json.put("code", "-2");
    		json.put("message", "图形验证码错误");
    		JsonUtils.toJson(response, json);
    		return;
    	}*/

        String userPhone=request.getParameter("userPhone");
        boolean flag = validateSmsToken(request.getParameter("smsToken"), userPhone);
        if (!flag){
            return;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userPhone", userPhone);
        BorrowUser user = borrowUserService.getBorrowUserByPhone(map);
        if(user!=null){
            json.put("code", "-1");
            json.put("message", "该手机号已注册");
            JsonUtils.toJson(response, json);
            return;
        }
    	smsService.getSmsCode(request, json);
    	logger.info("end getSmsCode mothed.......");
    	JsonUtils.toJson(response, json);
    }
    
    /**
     * 验证码
     *
     * @param request
     * @param response
     * @return
     */
    public boolean validateSubmit(HttpServletRequest request,
                                  HttpServletResponse response) {
        try {
            return captchaService.validateResponseForID(request.getSession(false).getId(), request.getParameter("captcha").toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 发送验证码验证token
     * @param token
     * @param userPhone
     */
    private boolean validateSmsToken(String token, String userPhone) {
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            logger.error("无效的token");
            return false;
        }

        String s = MD5Utils.md5(userPhone + Constants.SMSTOKEN_SALT);

        if (token.equals(s)) {
            return true;
        }else {
            logger.error("无效的token");
            return false;
        }
    }

}
