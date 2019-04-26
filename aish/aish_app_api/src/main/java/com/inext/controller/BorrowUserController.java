package com.inext.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inext.enums.GnhResultEnum;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.util.StringUtil;
import com.inext.constants.Constants;
import com.inext.constants.RedisCacheConstants;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BorrowUser;
import com.inext.entity.ChannelInfo;
import com.inext.entity.UserMobileAuthLog;
import com.inext.entity.ZmScoreLog;
import com.inext.entity.chuqiyou.ChuqiyouAuthReturn;
import com.inext.entity.zm.SesameData;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ApiResult;
import com.inext.result.ApiServiceResult;
import com.inext.service.CreditService;
import com.inext.service.IAssetBorrowOrderService;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBorrowUserService;
import com.inext.service.IChannelService;
import com.inext.service.IChannelStatisticsLogService;
import com.inext.service.IRiskCreditUserService;
import com.inext.service.UserAuthRecordService;
import com.inext.service.impl.credit.FkServiceImpl;
import com.inext.utils.DateUtils;
import com.inext.utils.JsonUtils;
import com.inext.utils.MD5Utils;
import com.inext.utils.TokenUtils;
import com.inext.utils.baofoo.ChuQiYouAuthUtil;
import com.inext.utils.encrypt.AESUtil;
import com.inext.utils.encrypt.MD5Util;
import com.inext.utils.helibao.HelibaoAuthUtil;
import com.inext.utils.partner.AesUtil_51kabao;
import com.inext.utils.sms.SmsSendUtil;
import com.inext.view.params.ApplyCollectParams;
import com.inext.view.result.JxlCollectResult;
import com.octo.captcha.service.image.ImageCaptchaService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * APP用户控制器
 *
 * @author ttj
 */

@RestController
@RequestMapping(value = "/borrowUser")
public class BorrowUserController extends BaseController {
    Logger logger = LoggerFactory.getLogger(BorrowUserController.class);
    //	@Value("${operUrl}")
//    private String operUrl;
    @Resource
    IBorrowUserService borrowUserService;

    @Autowired
    IAssetBorrowOrderService assetBorrowOrderService;

    @Resource
    private ImageCaptchaService captchaService;

    private final String CUSTOMER_IDENTIFICATION = "KACUIDKZR";

    private final String CUSTOMER_REGIST = "XGXYQM";
    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @Autowired
    private IChannelService channelService;

    @Resource
    private IChannelStatisticsLogService channelStatisticsLogService;

    @Autowired
    private UserAuthRecordService userAuthRecordService;



    @ApiOperation(value = "app输入手机号码第一步接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userPhone", value = "用户手机号码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/loginOne")
    public void loginOne(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        borrowUserService.loginOne(json, request);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "app登录接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userPhone", value = "用户手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userPassword", value = "用户登录密码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        borrowUserService.login(json, request);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "app短信验证码登录接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userPhone", value = "用户手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "smsCode", value = "用户短信验证码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/smsLogin")
    public void smsLogin(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        borrowUserService.smsLogin(json, request);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "app注册接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userPhone", value = "用户手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userPassword", value = "用户登录密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "smsCode", value = "验证码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "channelCode", value = "渠道来源", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/registered")
    public void registered(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        borrowUserService.registered(json, request);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "app找回密码第一步校验验证码接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userPhone", value = "用户手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "smsCode", value = "验证码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/getBackOne")
    public void getBackOne(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        borrowUserService.getBackOne(json, request);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "app找回密码第二步接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userPhone", value = "用户手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userPassword", value = "密码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/getBackTwo")
    public void getBackTwo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        borrowUserService.getBackTwo(json, request);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "查询app是否设置了交易密码接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header")
    })
    @PostMapping(value = "/getIsTransPassWord")
    public void getIsTransPassWord(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.getIsTransPassWord(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "app设置交易密码接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "transPasswordOne", value = "用户手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "transPasswordTwo", value = "验证码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/setTransPassWord")
    public void setTransPassWord(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.setTransPassWord(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "app修改登录或者交易密码接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "type", value = "修改密码类型（1:登录密码，0：交易密码）", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oldPassWord", value = "老密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "newPassWord", value = "新密码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updatePassWord")
    public void updatePassWord(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.updatePassWord(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "完善信息实名认证接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            //@ApiImplicitParam(name = "files", value = "图片对象集合（1:人脸图片;2:身份证正面图片;3:身份证反面图片）", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userCardNo", value = "用户身份证号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userEducation", value = "学历", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userMarriage", value = "婚姻状态", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userProvince", value = "省", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userCity", value = "市", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userArea", value = "区", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userAddress", value = "详细地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "lengthOfStay", value = "居住时长", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateBorrowUserInfo")
    public void updateBorrowUserInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.updateBorrowUserInfo(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "完善信息实名认证数据回显接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header")
    })
    @PostMapping(value = "/queryBorrowUserInfo")
    public void queryBorrowUserInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.queryBorrowUserInfo(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "完善信息主页数据回显接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header")
    })
    @PostMapping(value = "/queryBorrowUserIndex")
    public void queryBorrowUserIndex(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.queryBorrowUserIndex(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "上传手机通讯录信息接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "userPhones", value = "用户通讯录数据(userPhones为集合，集合中使用对象，(name:姓名，phone:手机号码))组成json串 ", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/uploadBorrowUserPhone")
    public void uploadBorrowUserPhone(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.uploadBorrowUserPhone(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "用户运营商认证接口 第一步  ")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "version", value = "1.1.1", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateOperatorInfo")
    public void updateOperatorInfo(HttpServletRequest request, HttpServletResponse response) {
        String version = request.getHeader("version");
        JSONObject jsonObject = new JSONObject();
        if(version==null || version.equals("")){
            jsonObject.put("code", "-1");
            jsonObject.put("message", "请升级APP至最新版本");

        }else{
            BorrowUser bu = this.getLoginUser(request.getHeader("token"));

            if(bu.getIsOperator().equals(0) || bu.getIsOperator().equals(9)){
                Map<String, String> auth_map =  userAuthRecordService.doMobileAuth(bu);
                if(auth_map.get("code").equals("0000")){
                    jsonObject.put("code", "0");
                    jsonObject.put("message", "成功");
                    jsonObject.put("result", JSONObject.toJSON(auth_map.get("operatorUrl")));
                }else if(auth_map.get("code").equals("0001")){
                    jsonObject.put("code", "-1");
                    jsonObject.put("message", "您已通过认证");
                }else{
                    jsonObject.put("code", "-1");
                    jsonObject.put("message", auth_map.get("message"));
                }
            }else if(bu.getIsOperator().equals(1)){
                jsonObject.put("code", "-1");
                jsonObject.put("message", "您已通过认证");
            }else if(bu.getIsOperator().equals(3)){
                jsonObject.put("code", "-1");
                jsonObject.put("message", "正在等待运营商认证结果");
            }
        }

        JsonUtils.toJson(response, jsonObject);
    }

    @ApiOperation(value = "用户运营商认证异步通知")
    @PostMapping(value = "/applyCollect")
    public void applyCollect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String message = request.getParameter("message");
        String userId = request.getParameter("userId");

        System.out.print("无其右认证异步通知返回： code="+code+" message="+message+" userId="+userId);

        if(code.equals("0000")){

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            List<UserMobileAuthLog> userlog_list = userAuthRecordService.getUserMobileAuthLogByUserId(params);

            if(userlog_list.size()>0){
                UserMobileAuthLog userlog = userlog_list.get(0);
                if(userlog.getAuth_status()==1){
                    userlog.setNotify_json(userlog.getNotify_json()+"||"+ "异步通知返回code="+code+", message="+message);
                    userlog.setAuth_status(2); //处理成功
                    userAuthRecordService.updateUserMobileAuthLog(userlog);

                    BorrowUser user = borrowUserService.getBorrowUserById(Integer.valueOf(userId));
                    user.setIsOperator(1);  //运营商认证成功
                    user.setOperatorTime(new Date());
                    borrowUserService.updateUser(user);
                }

            }

            //已经认证
        }else if(code.equals("0001")){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            List<UserMobileAuthLog> userlog_list = userAuthRecordService.getUserMobileAuthLogByUserId(params);

            if(userlog_list.size()>0){
                UserMobileAuthLog userlog = userlog_list.get(0);
                userlog.setNotify_json(userlog.getNotify_json()+"||"+ "异步通知返回code="+code+", message="+message);
                userlog.setAuth_status(2); //处理成功
                userAuthRecordService.updateUserMobileAuthLog(userlog);

                BorrowUser user = borrowUserService.getBorrowUserById(Integer.valueOf(userId));
                user.setIsOperator(1);  //运营商认证成功
                user.setOperatorTime(new Date());
                borrowUserService.updateUser(user);

            }
        }else{

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", userId);
            List<UserMobileAuthLog> userlog_list = userAuthRecordService.getUserMobileAuthLogByUserId(params);

            if(userlog_list.size()>0){
                UserMobileAuthLog userlog = userlog_list.get(0);
                if(userlog.getAuth_status()==1){

                    userlog.setNotify_json(userlog.getNotify_json()+"||"+ "异步通知返回code="+code+", message="+message);

                    userAuthRecordService.updateUserMobileAuthLog(userlog);

                }

            }
        }


        PrintWriter out = response.getWriter();
        Map<String, String> out_map = new HashMap<String, String>();
        out_map.put("result", "true");
        out_map.put("code", "0000");

        String json = JSON.toJSONString(out_map);

        out.print(json);
    }

    @ApiOperation(value = "用户运营商认证回调")
    @GetMapping(value = "/return_applyCollect")
    public ModelAndView return_applyCollect(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mav = new ModelAndView();

        ChuqiyouAuthReturn car = new ChuqiyouAuthReturn();
        car.setAccount(request.getParameter("account"));
        car.setMxcode(request.getParameter("mxcode"));
        car.setTaskId(request.getParameter("taskId"));
        car.setTaskType(request.getParameter("taskType"));
        String re_userId = request.getParameter("userId");

        String userId_str = re_userId.contains("_100")
                ? re_userId.substring(re_userId.lastIndexOf("_") + 4)
                : re_userId.substring(re_userId.lastIndexOf("_") + 1);

        car.setUserId(userId_str);
        car.setMessage(request.getParameter("message"));
        car.setLoginDone(request.getParameter("loginDone"));

        logger.info("出其右运营商认证回调-callback:" + JSON.toJSONString(car));

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId_str);
        List<UserMobileAuthLog> userlog_list = userAuthRecordService.getUserMobileAuthLogByUserId(params);
        if(userlog_list.size()>0){
            if(car.getMxcode().equals("1") && car.getLoginDone().equals("1")){
                UserMobileAuthLog userlog = userlog_list.get(0);
                userlog.setNotify_json(JSON.toJSONString(car));
                userlog.setAuth_status(2); //处理成功
                userAuthRecordService.updateUserMobileAuthLog(userlog);
                if(userlog.getUserId()!=null && NumberUtils.isNumber(userlog.getUserId())){
                    BorrowUser user = borrowUserService.getBorrowUserById(NumberUtils.toInt(userlog.getUserId()));
                    if(user != null && user.getIsOperator() != null && user.getIsOperator() != 1){
                        //更新用户表状态
                        user.setIsOperator(1); //运营商已认证
                        user.setOperatorTime(new Date()); //运营商已认证
                        borrowUserService.updateUser(user);
                    }
                }
                mav.addObject("errmsg", "处理成功，请等待运营商认证");
            }else{
                mav.addObject("errmsg", "未处理成功，请检查密码是否错误后再次提交");
            }
        }else{
            mav.addObject("errmsg", "请求错误");
        }
        mav.setViewName("zm/mobileAuth");
        return mav;
    }


    @ApiOperation(value = "用户绑定银行卡接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "userName", value = "用户姓名 ", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cardCode", value = "用户银行卡号 ", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cardType", value = "所属银行 ", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cardPhone", value = "银行预留手机号码 ", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/updateBlackInfo")
    public void updateBlackInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.updateBlackInfo(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "用户查询站内信消息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "pageNum", value = "当前页数", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "numPerPage", value = "每页显示多少条", dataType = "String", paramType = "header")
    })
    @PostMapping(value = "/getBorrowUserMessage")
    public void getBorrowUserMessage(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.getBorrowUserMessage(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "用户查询站内信消息详细数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "消息id", dataType = "String", paramType = "header")
    })
    @PostMapping(value = "/getBorrowUserMessageDetail")
    public void getBorrowUserMessageDetail(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.getBorrowUserMessageDetail(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "花小侠首页数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header")/*,
            @ApiImplicitParam(name = "advCode", value = "用户授权码", dataType = "String", paramType = "header")*/
    })
    @PostMapping(value = "/getIndexInfo")
    public void getIndexInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.getIndexInfo(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "提交订单验证交易密码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "transPassword", value = "交易密码", dataType = "String", paramType = "query"),
    })
    @PostMapping(value = "/checkTRansPassword")
    public void checkTRansPassword(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.checkTRansPassword(json, request, bu);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "获取设备列表信息")
    @ApiImplicitParams(value = {})
    @PostMapping(value = "/getEquipmentInfo")
    public void getEquipmentInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        borrowUserService.getEquipmentInfo(json);
        JsonUtils.toJson(response, json);
    }

    @PostMapping(value = "/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) {

    }

    @RequestMapping(value = "/toTgRedister")
    public ModelAndView toTgRedister(HttpServletRequest request, HttpServletResponse response) {
        boolean isUvFirst=false;

        ModelAndView mav = new ModelAndView();
        String channelCode = request.getParameter("code");

        String channelCodeMing = AESUtil.decrypt(channelCode, "");
        ChannelInfo channelInfo=channelService.getChannelByCode(channelCodeMing);
        if(null!=channelInfo){
            //pv
            channelStatisticsLogService.addChannleStatistics(channelCode,1,channelInfo.getId());

            Cookie[] cookies = request.getCookies();
            int count = 0;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (org.apache.commons.lang3.StringUtils.equals(cookie.getName(), CUSTOMER_IDENTIFICATION+"_"+channelInfo.getId())) {
                        isUvFirst=false;
                        break;
                    }
                    count++;
                    if (count == cookies.length) {
                        isUvFirst=true;
                    }
                }
            } else {
                isUvFirst=true;
            }

            //用户第一次访问
            if(isUvFirst){
                String uvFlag = UUID.randomUUID().toString().replaceAll("[-]", "");
                Cookie cidCookie = new Cookie(CUSTOMER_IDENTIFICATION+"_"+channelInfo.getId(), uvFlag);
                cidCookie.setMaxAge((int)DateUtils.nowToMidNight());
                response.addCookie(cidCookie);
                //uv
                channelStatisticsLogService.addChannleStatistics(channelCode,2,channelInfo.getId());
            }
            Map<String, String> mapFee = backConfigParamsService.getBackConfig(BackConfigParams.CHANNEL, null);
            String androidUrl = mapFee.get("android_down_url");
            String iosUrl = mapFee.get("ios_down_url");

            mav.addObject("androidUrl", androidUrl);
            mav.addObject("channelCode", channelCode);

            mav.addObject("iosUrl", iosUrl);
            if(channelCodeMing.equals("c33")){
            	mav.setViewName("tg/index_icp");
            }else{
            	mav.setViewName("tg/index");
            	
            }
        }else{
            mav.addObject("channelCode", null);
            mav.setViewName("tg/err_index");
        }


        return mav;
    }

    @GetMapping(value = "/goDown")
    public ModelAndView goDown(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();

        boolean hasRegistered=false;

        //判断有没有注册过，如果没有注册成功过，跳往注册页面
        Cookie[] cookies = request.getCookies();
        int count = 0;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (org.apache.commons.lang3.StringUtils.equals(cookie.getName(), CUSTOMER_REGIST)) {
                    hasRegistered=true;
                    break;
                }
                count++;
                if (count == cookies.length) {
                    hasRegistered=false;
                }
            }
        } else {
            hasRegistered=false;
        }


        if(!hasRegistered){
            String channelCode=request.getParameter("channelCode");
            mav.setViewName("redirect:/borrowUser/toTgRedister?code="+channelCode);
        }else{
            if(null!=request.getParameter("isRegistered")){
                mav.addObject("isRegistered", 1);
            }else{
                mav.addObject("isRegistered", 0);
            }


            Map<String, String> mapFee = backConfigParamsService.getBackConfig(BackConfigParams.CHANNEL, null);
            String androidUrl = mapFee.get("android_down_url");
            String iosUrl = mapFee.get("ios_down_url");

            mav.addObject("androidUrl", androidUrl);
            mav.addObject("iosUrl", iosUrl);

            mav.setViewName("tg/download");
        }


        return mav;
    }

    @ApiOperation(value = "检查用户是否存在借款")
    @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", required = true, paramType = "header")
    @PostMapping(value = "/checkBorrow")
    public ApiResult checkBorrow(HttpServletRequest request) {

        BorrowUser borrowUser = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN));
        Integer result = this.assetBorrowOrderService.checkBorrow(borrowUser.getId(), null);

        if (1 == result.intValue()) {
            return new ApiResult(ApiStatus.FAIL.getCode(), "您有未完成的订单\n" +
                    "请处理后再发起新的订单哦");
        } else {
            Map<String, String> interval = assetBorrowOrderService
                    .findAuditFailureOrderByUserId(borrowUser.getId());
            if ("-1".equals(interval.get("code"))) {
                return new ApiResult(ApiStatus.FAIL.getCode(), interval.get("msg"));
            } else {
            }
        }
        return new ApiResult();
    }

    @RequestMapping(value = "/tgRedister")
    public void tgRedister(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Map<String, String> params = getParameters(request);

        borrowUserService.tgRedister(params, json);
        if(null!=json.get("code")){
            if("0".equals(json.get("code").toString())||"100".equals(json.get("code").toString())){
                //如果注册成功向客户端写入cookie标记
                Cookie cidCookie = new Cookie(CUSTOMER_REGIST, "1");
                cidCookie.setMaxAge(60*60*24*30);
                response.addCookie(cidCookie);
            }

        }

        JsonUtils.toJson(response, json);

    }

    /**----------------------------------------------*/
    /**---------------- 联合登录 ---------------------*/
    /**----------------------------------------------*/

    /**
     * 去哪借登录注册
     * @param request
     * @param response
     */
    @PostMapping(value = "/partner/login")
    public void plogin(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("userPhone", request.getParameter("phoneNo"));
        params.put("registerFrom", request.getParameter("registerFrom"));
        params.put("maskType", request.getParameter("maskType"));
        params.put("unionSign", request.getParameter("unionSign"));
        String phone = String.valueOf(request.getParameter("phoneNo"));

        //生成密码
        String random_str = "";
        for (int i = 0;i<4;i++){
            random_str = random_str+ (char)(Math.random()*26+'a');
        }

        String password = random_str+phone;
        params.put("password_md5", MD5Utils.MD5Encode(password));

        int flag = borrowUserService.partnerRedister(params);

        if(flag==0){
            json.put("code", "0000");
            json.put("msg", "success");
            json.put("businessCode", "0000");
            HashMap<String,String> data_params = new HashMap<String,String>();
            data_params.put("phoneNo", phone);

            if(String.valueOf(request.getParameter("maskType")).equals("0")){
                BorrowUser bu = new BorrowUser();
                String tokenstr  =  TokenUtils.generateToken(bu);
                data_params.put("token", tokenstr);
                redisUtil.set(tokenstr, password+"_"+phone, Long.valueOf(259200));
            }

            json.put("data", JSONObject.toJSON(data_params));

        }else if(flag==1){
            json.put("code", "0000");
            json.put("msg", "success");
            json.put("businessCode", "0003");
            HashMap<String,String> data_params = new HashMap<String,String>();
            data_params.put("phoneNo", phone);
            json.put("data", JSONObject.toJSON(data_params));
        }else if(flag==2){
            json.put("code", "0000");
            json.put("msg", "平台拒绝");
            json.put("businessCode", "0010");
            HashMap<String,String> data_params = new HashMap<String,String>();
            data_params.put("phoneNo", phone);
            json.put("data", JSONObject.toJSON(data_params));
        }

        JsonUtils.toJson(response, json);
    }


    /**
     * 去哪借落地页
     * @param response
     */
    @GetMapping(value = "/partner/app-landing")
    public ModelAndView app_landing(HttpServletResponse response, @RequestParam("registerFrom") String registerFrom,
                                    @RequestParam("channelId") String channelId, @RequestParam("token") String token) {

        String redis_token = (String) redisUtil.get(token);
        if(redis_token!=null){

            //读取redis并发送短消息
            String password_phone =  (String) redisUtil.get("redis_token");
            if(password_phone!=null){
                String[] sourceStrArray = password_phone.split("_");
                if(sourceStrArray.length>1){
                    String msg_content = "恭喜您已成为花小侠会员，登录账号: "+sourceStrArray[1]+" 登录密码:"+sourceStrArray[0]+"，请妥善保管，建议马上登录APP修改密码。";
                    SmsSendUtil.sendSmsDiyCLandYX(sourceStrArray[1], msg_content);
                }
            }

            ModelAndView mav = new ModelAndView();
            mav.setViewName("union/download");
            return mav;
        }else{
            return null;
        }
    }

    private String key_51kabao = "A25D211A909E1FF4";

    /**
     * 51卡宝验证用户
     * @param response
     */
    @PostMapping(value = "/partner/kabao/validate")
    public void kvalidate(@RequestBody String rawStr, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Map<String, Object> params = JSONObject.parseObject(rawStr, new TypeReference<Map<String, Object>>(){});
        String sign = (String) params.get("sign");
        String postData = (String) params.get("postData");

        try{
            if(StringUtil.isNotEmpty(sign) && StringUtil.isNotEmpty(postData)){

                //签名对比
                String sign_local = MD5Util.build(MD5Util.build(postData) + key_51kabao);

                if(sign_local.equals(sign)){
                    Map<String, Object> params_req = JSONObject.parseObject(AesUtil_51kabao.aesDecrypt(postData,key_51kabao,null), new TypeReference<Map<String, Object>>(){});
                    params_req.put("userPhone", params_req.get("mobile"));
                    params_req.put("maskType", "511");
                    int code = borrowUserService.partnerRedister(params_req);
                    json.put("code", code);
                    json.put("msg", "success");
//            		json.put("url", "http://47.100.111.113:8080/borrowUser/partner/app_download");

                    json.put("url", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("HL_BACK_PATH")+"/borrowUser/partner/app_download");
                }else{
                    json.put("code", 2);  //2表示失败
                    json.put("msg", "验签失败");
                }

            }else{
                json.put("code", 2);  //2表示失败
                json.put("msg", "参数不正确");
            }
        }catch (Exception e) {
            json.put("code", 2);  //2表示失败
            json.put("msg", "系统异常");
        }


        JsonUtils.toJson(response, json);
    }


    /**
     * 51卡宝验证用户
     * @param response
     */
    @PostMapping(value = "/partner/kabao/register")
    public void kregister(@RequestBody String rawStr, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Map<String, Object> params = JSONObject.parseObject(rawStr, new TypeReference<Map<String, Object>>(){});
        String sign = (String) params.get("sign");
        String postData = (String) params.get("postData");

        String postData_de = AesUtil_51kabao.aesDecrypt(postData,key_51kabao,null);

        try{
            if(StringUtil.isNotEmpty(sign) && StringUtil.isNotEmpty(postData)){

                //签名对比
                String sign_local = MD5Util.build(MD5Util.build(postData) + key_51kabao);

                if(sign_local.equals(sign)){

                    Map<String, Object> params_request = JSONObject.parseObject(postData_de, new TypeReference<Map<String, Object>>(){});
                    String mobile = String.valueOf(params_request.get("mobile"));
                    params_request.put("maskType", "510");

                    //生成密码
                    String random_str = "";
                    for (int i = 0;i<4;i++){
                        random_str = random_str+ (char)(Math.random()*26+'a');
                    }

                    String password = random_str+mobile;
                    params_request.put("password_md5", MD5Utils.MD5Encode(password));
                    int code = borrowUserService.partnerRedister(params_request);

                    if(code==0){
                        //发送短消息
                        String msg_content = "恭喜您已成为花小侠会员，登录账号: "+mobile+" 登录密码:"+password+"，请妥善保管，建议马上登录APP修改密码。";
                        SmsSendUtil.sendSmsDiyCLandYX(mobile, msg_content);

                        json.put("msg", "success");
//                		json.put("url", "http://47.100.111.113:8080/borrowUser/partner/app_download");

                        json.put("url", Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.HELIPAY).get("HL_BACK_PATH")+"/borrowUser/partner/app_download");
                    }else{
                        json.put("msg", "系统异常");
                    }

                    json.put("code", code);

                }else{
                    json.put("code", 2);  //2表示失败
                    json.put("msg", "验签失败");
                }

            }else{
                json.put("code", 2);  //2表示失败
                json.put("msg", "参数不正确");
            }
        }catch (Exception e) {
            json.put("code", 2);  //2表示失败
            json.put("msg", "系统异常");
        }

        JsonUtils.toJson(response, json);
    }


    /**
     * 联合登录下载页
     * @param request
     * @param response
     */
    @GetMapping(value = "/partner/app_download")
    public ModelAndView app_download(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("union/download");

        return mav;
    }


    @Resource
    CreditService creditService;

    @Resource
    FkServiceImpl fkService;

    @Resource
    IRiskCreditUserService riskCreditUserService;

    @GetMapping(value = "/testrisk/test")
    public ModelAndView testrisk(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("tg/index");

        return mav;

    }


    @GetMapping(value = "/xuyao/duanxin")
    public void getxuyao(HttpServletRequest request, HttpServletResponse response) {
        List<BorrowUser> userlist = borrowUserService.getXuyao();
        ArrayList<BorrowUser> userlist_1 = new ArrayList<BorrowUser>();
        ArrayList<BorrowUser> userlist_2 = new ArrayList<BorrowUser>();
        ArrayList<BorrowUser> userlist_3 = new ArrayList<BorrowUser>();
        ArrayList<BorrowUser> userlist_4 = new ArrayList<BorrowUser>();
        ArrayList<BorrowUser> userlist_5 = new ArrayList<BorrowUser>();
        ArrayList<BorrowUser> userlist_6 = new ArrayList<BorrowUser>();
        ArrayList<BorrowUser> userlist_7 = new ArrayList<BorrowUser>();
        ArrayList<BorrowUser> userlist_8 = new ArrayList<BorrowUser>();
        ArrayList<BorrowUser> userlist_9 = new ArrayList<BorrowUser>();
        for (int i = 0; i < userlist.size(); i++) {
            if(i<=1000){
                userlist_1.add(userlist.get(i));
            }else if(i>1000 && i<=2000){
                userlist_2.add(userlist.get(i));
            }else if(i>2000 && i<=3000){
                userlist_3.add(userlist.get(i));
            }else if(i>3000 && i<=4000){
                userlist_4.add(userlist.get(i));
            }else if(i>4000 && i<=5000){
                userlist_5.add(userlist.get(i));
            }else if(i>6000 && i<=7000){
                userlist_6.add(userlist.get(i));
            }else if(i>7000 && i<=8000){
                userlist_7.add(userlist.get(i));
            }else if(i>8000 && i<=9000){
                userlist_8.add(userlist.get(i));
            }else if(i>9000){
                userlist_9.add(userlist.get(i));
            }

        }

        System.out.print("aaaa");

        //SmsSendUtil.sendSmsZL("13735803606", "您已经注册成为花小侠会员，请关注周周宝App下载最新版本。退订回T");
    }

    /**
     *  给你花撞库接口请求
     * @param appId
     * @param mobile
     * @param mobileMd5
     * @param channelId
     * @param timestamp
     * @param sign
     * @return
     */
    @PostMapping(value = "/partner/geinihua/validate")
    public Map<String,Object> gnhvalidate(@RequestParam String appId,
                                          @RequestParam String mobile,
                                          @RequestParam String mobileMd5,
                                          @RequestParam String channelId,
                                          @RequestParam String timestamp,
                                          @RequestParam String sign) {

        try {

            Map<String,Object> map = borrowUserService.gnhvalidate(appId, channelId, timestamp, mobile, sign,mobileMd5);
            return map;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //1002：服务内部错误
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("code",1002);
            resultMap.put("message","服务内部错误");
            return resultMap;
        }
    }

    @PostMapping(value = "/partner/geinihua/register")
    public Map<String,Object> gnhRegister(@RequestParam String appId,
                                       @RequestParam String mobile,
                                       @RequestParam String channelId,
                                       @RequestParam String timestamp,
                                       @RequestParam String sign){
        try {
            Map<String, Object> map = borrowUserService.gnhRegister(appId, channelId, timestamp, mobile, sign);
            return map;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //1002：服务内部错误
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("code", GnhResultEnum.SYS_ERROR.getCode());
            resultMap.put("message",GnhResultEnum.SYS_ERROR.getMsg());
            return resultMap;
        }
    }
    @PostMapping (value = "/getLoanInfo")
    public  void queryBorrowUserAmount(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        BorrowUser bu = this.getLoginUser(request.getHeader("token"));
        borrowUserService.queryBorrowUserAmount(json, request, bu);
        JsonUtils.toJson(response, json);
    }


    /**
     * 融易来（贷上线）
     * @param phone
     * @param timestamp
     * @param sign
     * @return
     */
    @PostMapping(value = "/partner/rongyilai/validate")
    public Map<String,Object> rylValidate(
                                          @RequestParam String phone,
                                          @RequestParam String timestamp,
                                          @RequestParam String sign,
                                          @RequestParam String channelCode) {

        try {

            Map<String,Object> map = borrowUserService.rylValidate(phone, timestamp, sign, channelCode);
            return map;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //1002：服务内部错误
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("code",-1);
            resultMap.put("message","服务内部错误");
            return resultMap;
        }
    }

    @PostMapping(value = "/partner/rongyilai/register")
    public Map<String,Object> rydRegister(
                                          @RequestParam String bizData,
                                          @RequestParam String channelCode,
                                          @RequestParam String timestamp,
                                          @RequestParam String sign){
        try {
            Map<String, Object> map = borrowUserService.rydRegister(channelCode, timestamp, bizData, sign);
            return map;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            //1002：服务内部错误
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("code", GnhResultEnum.SYS_ERROR.getCode());
            resultMap.put("message",GnhResultEnum.SYS_ERROR.getMsg());
            return resultMap;
        }
    }
    /**
     * 360联合注册
     * 注册
     * 回调360接口
     * 返回落地页
     * @param mobile
     * @param channelCode
     * @param pre_orderid
     * @return
     */
    @RequestMapping(value = "/partner/360/down_new_version")
    public ModelAndView partner360Register(
                                          @RequestParam String mobile,
                                          @RequestParam String channelCode,
                                          @RequestParam String pre_orderid){

        ModelAndView mav = new ModelAndView();
        Map<String, String> mapFee = backConfigParamsService.getBackConfig(BackConfigParams.CHANNEL, null);
        String androidUrl = mapFee.get("android_down_url");
        String iosUrl = mapFee.get("ios_down_url");


        mav.addObject("androidUrl", androidUrl);
        mav.addObject("iosUrl", iosUrl);

        mav.setViewName("tg/newversion_download");
        try {
            borrowUserService.partner360Register(mobile, channelCode ,pre_orderid);
            return mav;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return mav;
        }
    }
    public static void main(String[] args) {
        SmsSendUtil.sendSmsZL("13735803606", "您已经注册成为花小侠会员，请关注周周宝App下载最新版本。退订回T");
        System.out.print("ok");
    }



}
