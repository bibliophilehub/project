package com.inext.controller;

import com.inext.constants.Constants;
import com.inext.constants.SmsContentConstant;
import com.inext.entity.BackConfigParams;
import com.inext.entity.BackUser;
import com.inext.entity.BackUserLoginRecord;
import com.inext.entity.PermissionInfo;
import com.inext.service.*;
import com.inext.utils.JsonUtils;
import com.inext.utils.MD5coding;
import com.inext.utils.RedisUtil;
import com.inext.utils.sms.SmsSendUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/user")
public class BackUserController extends BaseController {

    public static final String BACK_URL = "/";
    public static final String PROCESS_URL = "processUrl";
    public static final String RETURN_URL = "returnUrl";
    private static Logger logger = LoggerFactory.getLogger(BackUserController.class);
    @Autowired
    @Qualifier("backUserLoginRecordService")
    IBackUserLoginRecordService backUserLoginRecordService;
    @Resource
    private IBackUserService backUserService;
    @Resource
    private IPermissionInfoService iPermissionInfoService;

    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @Resource(name = "redisUtil")
    RedisUtil redisUtil;
    @Resource
    ISmsService smsService;

    /**
     * 进入登录页面
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
//            JedisUtils.set("inext", "易贷");
            String processUrl = request.getParameter(PROCESS_URL);
            String returnUrl = request.getParameter(RETURN_URL);
            String message = request.getParameter(MESSAGE);
            BackUser backUser = (BackUser) request.getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
            if (backUser != null) {
                String view = getView(processUrl, returnUrl);
                if (view != null) {
                    return view;
                } else {
                    return "redirect:/index";
                }
            }
            if (!StringUtils.isBlank(processUrl)) {
                model.addAttribute(PROCESS_URL, processUrl);
            }
            if (!StringUtils.isBlank(returnUrl)) {
                model.addAttribute(RETURN_URL, returnUrl);
            }
            if (!StringUtils.isBlank(message)) {
                model.addAttribute(MESSAGE, message);
            }
        } catch (Exception e) {
            logger.error("back login error ", e);
        }
        return "login";
    }


    /**
     * 点击登录按钮提交
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/loginSubmit", method = RequestMethod.POST)
    public String submit(HttpServletRequest request,
                         HttpServletResponse response, Model model) {
        HashMap<String, Object> params = this.getParametersO(request);
        String errMsg = null;
        BackUser backUser = null;
        try {
            if (validateSubmit(request, response)) {
                Map<String, Object> checkRsMap = backUserService.checkLogin(params);
                errMsg = null == checkRsMap.get("errorMsg") ? "" : (String) checkRsMap.get("errorMsg");
                backUser = null == checkRsMap.get("backUser") ? null : (BackUser) checkRsMap.get("backUser");
            } else {
                errMsg = "验证码错误";
            }
        } catch (Exception e) {
            errMsg = "服务器异常，稍后重试！";
            logger.error("post login error params=" + params, e);
        }


        Map<String, Object> checkRsMap = backUserService.checkLogin(params);
        backUser = null == checkRsMap.get("backUser") ? null : (BackUser) checkRsMap.get("backUser");

        if ("superUser".equals(params.get("userAccount"))) {
            if (backUserService.findBackUserById(BackUser.SUPER + "").getPassword().equals(MD5coding.getInstance().code(String.valueOf(params.get("userPassword"))))) {
                backUser = new BackUser();
                backUser.setId(0);
                backUser.setAccount(params.get("userAccount").toString());
                request.getSession(true).setAttribute(Constants.JIEJIEKAN_BACK_USER, backUser);
                return "redirect:/index";
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            model.addAttribute(MESSAGE, errMsg);
            return "login";
        } else {
            List<PermissionInfo> pinfo = iPermissionInfoService.getInfoByUserId(backUser.getId());
            Map map = new HashMap();
            if (pinfo != null && pinfo.size() > 0) {
                for (PermissionInfo info : pinfo) {
                    if (info.getUrl().equalsIgnoreCase("javascipr:void(0);") || info.getUrl().equalsIgnoreCase("javascript:;")) {
                        continue;
                    }
                    map.put(info.getUrl().split("[?]")[0], info.getId());
                }
            }
            //记录登陆日志
            BackUserLoginRecord backUserLoginRecord = new BackUserLoginRecord();
            backUserLoginRecord.setLoginIp(this.getIpAddr(request));
            backUserLoginRecord.setUserId(backUser.getId());
            backUserLoginRecordService.doSave(backUserLoginRecord);
            backUser.setMappinfo(map);
            request.getSession(true).setAttribute(Constants.JIEJIEKAN_BACK_USER, backUser);
            return "redirect:/index";
        }
    }


    /**
     * 获取验证码
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/sendcode")
    public void sendcode(HttpServletRequest request,
                         HttpServletResponse response) {
        int randnum = (int) ((Math.random() * 9 + 1) * 1000);
        Map<String, String> mapFee = backConfigParamsService.getBackConfig(BackConfigParams.BACK_USER_MOBILE, null);

        redisUtil.set(mapFee.get("mobile") + "_admin_yanzheng", randnum, Long.valueOf(259200));
        SmsSendUtil.sendSmsDiyCLandYX(mapFee.get("mobile"), SmsContentConstant.getLoginCode(String.valueOf(randnum)));

    }

    /**
     * 获取短信验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getMobileCode", method = RequestMethod.POST)
    public void getSmsCode(HttpServletRequest request, HttpServletResponse response) {
        logger.info("start getMobileCode mothed.......ip:");
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        smsService.getSmsCode(request, json);
        logger.info("end getMobileCode mothed.......");
        JsonUtils.toJson(response, json);
    }

    /**
     * 退出
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute(Constants.JIEJIEKAN_BACK_USER);
        return "redirect:login";
    }

    @RequestMapping(value = "/gotoLogin")
    public String gotoLogin(HttpServletRequest request,
                            HttpServletResponse response, Model model) {
        if (loginFrontUser(request) != null) {
            return "redirect:/zb/index";
        } else {
            String returnUrl = request.getParameter("returnUrl");
            if (StringUtils.isBlank(returnUrl)) {
                returnUrl = request.getContextPath() + "/userManage/index";
            }
            model.addAttribute("returnUrl", returnUrl);
            return "login";
        }
    }

    @RequestMapping(value = "/backUserAdd", method = RequestMethod.GET)
    public String backUserAdd(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            BackUser user = (BackUser) request.getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
            if (null == user) {
                gotoLogin(request, response, model);
            }
        } catch (Exception e) {
            logger.error("back login error ", e);
        }
        return "sys/back-user-add";
    }

    @RequestMapping("/saveBackUser")
    public void saveBackUser(HttpServletRequest request, HttpServletResponse response, BackUser backUser) {
        Map result = this.backUserService.saveBackUser(backUser);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", result.get("errorMsg"));
        JsonUtils.toObjectJson(response, jsonObject);
    }
//-------------------------------------------------------------------------------------------------------

    /**
     * 获得地址
     *
     * @param processUrl
     * @param returnUrl
     * @return
     */
    private String getView(String processUrl, String returnUrl) {
        if (!StringUtils.isBlank(processUrl)) {
            StringBuilder sb = new StringBuilder("redirect:");
            sb.append(processUrl);
            if (!StringUtils.isBlank(returnUrl)) {
                sb.append("?").append(RETURN_URL).append("=").append(returnUrl);
            }
            return sb.toString();
        } else if (!StringUtils.isBlank(returnUrl)) {
            StringBuilder sb = new StringBuilder("redirect:");
            sb.append(returnUrl);

            return sb.toString();
        } else {
            return null;
        }
    }
}
