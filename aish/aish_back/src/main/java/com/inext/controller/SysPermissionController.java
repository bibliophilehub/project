package com.inext.controller;

import com.inext.constants.Constants;
import com.inext.entity.BackUser;
import com.inext.entity.SysModule;
import com.inext.service.IPermissionInfoService;
import com.inext.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 系统模块管理
 */
@Controller
@RequestMapping(value = "/sysPermission")
public class SysPermissionController extends BaseController {

    private static Logger loger = Logger.getLogger(SysPermissionController.class);

    private static String BACK_URL = "/";

    @Resource
    IPermissionInfoService permissionInfoService;


    @RequestMapping("/getUserModule")
    public void getUserModule(HttpServletRequest request, HttpServletResponse response, SysModule sysModule) {
        BackUser user = (BackUser) request.getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        Integer userId = user.getId();
        if (user.getAccount().equalsIgnoreCase("superUser")) {//超级管理员不要判断 防止数据库表出现异常不能使用
            userId = null;
        }
        String html = this.permissionInfoService.getUserModule(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", html);
        JsonUtils.toObjectJson(response, jsonObject);
    }
}
