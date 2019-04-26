package com.inext.controller;

import com.alibaba.fastjson.JSONObject;
import com.inext.constants.Constants;
import com.inext.constants.RedisCacheConstants;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BorrowUser;
import com.inext.result.ApiResult;
import com.inext.result.ResponseDto;
import com.inext.service.IBackConfigParamsService;
import com.inext.service.IBorrowUserService;
import com.inext.service.ISysCodeService;
import com.inext.service.impl.InitializingBeanServiceImpl;
import com.inext.utils.JsonUtils;
import com.inext.utils.baofoo.ChuQiYouAuthUtil;
import com.inext.utils.baofoo.XinyanAuthUtil;
import com.inext.view.result.MapResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础数据控制器
 *
 * @author ttj
 */
@Controller
@RequestMapping(value = "/sysCode")
public class SysCodesController extends BaseController {
    Logger logger = LoggerFactory.getLogger(SysCodesController.class);

    @Resource
    ISysCodeService sysCodeService;
    @Resource
    InitializingBeanServiceImpl initializingBeanService;
    @Resource
    IBorrowUserService borrowUserService;
    @Resource
    private IBackConfigParamsService backConfigParamsService;
    @Autowired
    XinyanAuthUtil xinyanAuthUtil;
    @Resource
    ChuQiYouAuthUtil chuQiYouAuthUtil;

    @ApiOperation(value = "获取省市区信息", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
    })
    @RequestMapping(value = "/getRbArea")
    public void getRbArea(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        sysCodeService.getRbArea(json, request);
        JsonUtils.toJson(response, json);
    }

    @ApiOperation(value = "Ios参数借口", httpMethod = "POST", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "token", value = "用户授权码", dataType = "String", paramType = "header"),
    })
    @RequestMapping(value = "/getIosParams")
    @ResponseBody
    public Map getIosParams(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> iosMap = backConfigParamsService.getBackConfig(BackConfigParams.IOS, null);
        Boolean isget = true;
        if (request.getHeader(RedisCacheConstants.TOKEN) != null) {
            try {
                Integer userId = this.getLoginUser(request.getHeader(RedisCacheConstants.TOKEN)).getId();
                BorrowUser user = borrowUserService.getBorrowUserById(userId);
                iosMap.put("isVerified", user.getIsVerified() + "");//是否实名认证
                iosMap.put("isCard", user.getIsCard() + "");//是否绑定银行卡
                iosMap.put("isOperator", user.getIsOperator() + "");//是否运营商认证
                iosMap.put("isZhima", user.getIsZhima() + "");//是否芝麻
                iosMap.put("isPhone", user.getIsPhone() + "");//是否提交了通讯录
                isget = false;
            } catch (Exception e) {
            }
        }
        if (isget) {
            iosMap.put("isVerified", "");//是否实名认证
            iosMap.put("isCard", "");//是否绑定银行卡
            iosMap.put("isOperator", "");//是否运营商认证
            iosMap.put("isZhima", "");//是否芝麻
            iosMap.put("isPhone", "");//是否提交了通讯录
        }
        Map map = new HashMap();
        map.put("code", "0");
        map.put("message", "Ios参数");
        map.put("result", iosMap);
        return map;
    }

    @ApiOperation(value = "获取导流链接", response = ApiResult.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
    })
    @GetMapping(value = "/getGuiadosUrl")
    @ResponseBody
    public ApiResult<MapResult> getGuiadosUrl() {
        //Map<String, String> map2 = Constants.BACK_CONFIG_PARAMS_MAP.get(BackConfigParams.CHANNEL);
        Map<String, String> map2 = backConfigParamsService.getBackConfig(BackConfigParams.CHANNEL, null);
        return new ApiResult(new MapResult(new HashMap<String, String>() {{
            put("location1", map2.get("MORE_BTN_LOCATION_1")); //订单提交成功页面
            put("location2", map2.get("MORE_BTN_LOCATION_2")); //被拒绝的订单详情页面
            put("guiadosUrl", map2.get("GUIADOS_URL"));
        }}));
    }

    @GetMapping(value = "/getAboutUs")
    @ResponseBody
    public ApiResult<MapResult> getAboutUs() {
        Map<String, String> map2 = backConfigParamsService.getBackConfig(BackConfigParams.WEBSITE, null);
        return new ApiResult(new MapResult(new HashMap<String, String>() {{
            put("aboutUs", map2.get("ABOUT_US"));
        }}));
    }

    @ApiOperation(value = "刷新系统常量缓存", response = ResponseDto.class, protocols = "HTTP 1.0/2.0")
    @ApiImplicitParams(value = {
    })
    @GetMapping(value = "/refreshSysMap")
    public void refreshSysMap(HttpServletRequest request, HttpServletResponse response) {
        initializingBeanService.initBackConfigParamsMap();
    }

//    @GetMapping(value = "/test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        try {
            //银行卡宾测试
//            JSONObject jsonObject = xinyanAuthUtil.cardbin_v1("6217921380116635");
//            System.out.println(jsonObject.toJSONString());
            //运营商原始数据查询测试
//            chuQiYouAuthUtil.queryOperatorOriginalData("13817400429");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
