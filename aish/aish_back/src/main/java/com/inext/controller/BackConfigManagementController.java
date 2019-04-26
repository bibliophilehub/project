package com.inext.controller;

import com.inext.entity.BackConfigParams;
import com.inext.service.IBackConfigParamsService;
import com.inext.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统模块管理
 */
@Controller
@RequestMapping(value = "/backConfigManagement")
public class BackConfigManagementController extends BaseController {

    private static Logger loger = Logger.getLogger(BackConfigManagementController.class);

    @Resource
    private IBackConfigParamsService backConfigParamsService;

    @RequestMapping("/backConfigList")
    public String backConfigList(HttpServletRequest request, Model model) {
        HashMap<String, Object> params = new HashMap<>();
        //params.put("sysKey", "FK_RS_SCORE");
        //List<BackConfigParams> backConfigParamsList = backConfigParamsService.findParams(params);
        List<String> sysKeys = new ArrayList<>();
        sysKeys.add("FK_RS_SCORE");
        sysKeys.add("GUIADOS_URL");
        sysKeys.add("MORE_BTN_LOCATION_1");
        sysKeys.add("MORE_BTN_LOCATION_2");
        sysKeys.add("LOAN_PER");
        sysKeys.add("LOAN_WY");
        sysKeys.add("LOAN_ALL");
        sysKeys.add("LOAN_OLD_ADD");
        sysKeys.add("LOAN_OLD_WY_POINT");
        sysKeys.add("LOAN_OLD_MAX");
        sysKeys.add("LOAN_OLD_INTERVAL_DAY");
        sysKeys.add("CHANNEL_REF");
        sysKeys.add("android_down_url");
        sysKeys.add("ios_down_url");
        sysKeys.add("version_android");
        sysKeys.add("version_ios");
        sysKeys.add("need_update");
        sysKeys.add("user_fee_isopen");
        sysKeys.add("CREDIT_REF_SWITCH");
        params.put("sysKeys", sysKeys);
        List<BackConfigParams> backConfigParamsList = backConfigParamsService.findParamsBySyskeys(params);
        model.addAttribute("list", backConfigParamsList);
        return "v2/backConfig/backConfigList";
    }

    @RequestMapping("/to-save")
    public String toSave(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        BackConfigParams backConfigParams = backConfigParamsService.getBackConfigById(Integer.parseInt(id));
        model.addAttribute("backConfig", backConfigParams);
        // 根据不同的配置项，可以返回不同的配置页面
        if("FK_RS_SCORE".equals(backConfigParams.getSysKey())) {
            Map<String, String> configParamsMap = backConfigParamsService.getBackConfig(BackConfigParams.FK, null);
            String fkScore = configParamsMap.get("FK_SCORE");
            model.addAttribute("fkScore", fkScore);
            return "v2/backConfig/rsScoreConfigSave";
        }
        return "v2/backConfig/backConfigSave";
    }

    @RequestMapping("/save")
    public void save(HttpServletRequest request, HttpServletResponse response, BackConfigParams backConfigParams) {
        int updateNumber = backConfigParamsService.updateBackConfigParams(backConfigParams);
        if(updateNumber == 1) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 200);
            jsonObject.put("msg", "保存成功");
            JsonUtils.toObjectJson(response, jsonObject);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", -1);
            jsonObject.put("msg", "保存失败");
            JsonUtils.toObjectJson(response, jsonObject);
        }
    }
}
