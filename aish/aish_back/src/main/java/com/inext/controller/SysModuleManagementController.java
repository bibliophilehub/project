package com.inext.controller;

import com.inext.entity.BackUser;
import com.inext.entity.SysModule;
import com.inext.enumerate.ApiStatus;
import com.inext.result.ResponseDto;
import com.inext.service.ISysModuleService;
import com.inext.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统模块管理
 */
@Controller
@RequestMapping(value = "/sysModuleManagement")
public class SysModuleManagementController extends BaseController {

    private static Logger loger = Logger.getLogger(SysModuleManagementController.class);

    private static String BACK_URL = "/";

    @Resource
    private ISysModuleService sysModuleService;

    @RequestMapping("/moduleList")
    public String moduleList(HttpServletRequest request) {
        return "v2/sysModule/sysModuleList";
    }

    @RequestMapping("/getModuleData")
    @ResponseBody
    public Map<String, Object> getModuleData(HttpServletRequest request, HttpServletResponse response) {
        List<SysModule> sysModuleList = sysModuleService.getModuleList();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", sysModuleList);
        return resultMap;
    }

    @RequestMapping("/to-save")
    public String toSave(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
//    	BackUser user = loginAdminUser(request);
        SysModule sysModule = sysModuleService.findSysModuleById(id);
        if (sysModule == null) {
            sysModule = new SysModule();
            //sysModule.setCreateTime(new Date());
            //sysModule.setCreateAccount(user.getAccount());
            //sysModule.setUpdateTime(sysModule.getCreateTime());
            //sysModule.setUpdateAccount(sysModule.getCreateAccount());
            sysModule.setMenus(1);
        }
        List<Map<String, Object>> sysModuleList = sysModuleService.getModuleTreeList();
        model.addAttribute("sysModuleList", sysModuleList);
        model.addAttribute("sysModule", sysModule);
        return "v2/sysModule/sysModuleSave";
    }

    /**
     * 去预览系统用户页面
     *
     * @return
     */
    @GetMapping("to-Preview")
    public String toPreview(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        SysModule sysModule = sysModuleService.findSysModuleById(id);
        List<Map<String, Object>> sysModuleList = sysModuleService.getModuleTreeList();
        model.addAttribute("sysModuleList", sysModuleList);
        model.addAttribute("sysModule", sysModule);
        return "v2/sysModule/sysModulePreview";
    }

    @RequestMapping("/save")
    public void saveModule(HttpServletRequest request, HttpServletResponse response, SysModule sysModule) {
        BackUser user = loginAdminUser(request);
        sysModule.setUpdateAccount(user.getAccount());
        sysModule.setUpdateTime(new Date());
        if (sysModule.getParentId() == null) {
            sysModule.setParentId(SysModule.SUPER);
        }
        Integer id = this.sysModuleService.saveModule(sysModule);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "保存成功");
        jsonObject.put("id", id);
        JsonUtils.toObjectJson(response, jsonObject);
    }

    /**
     * 删除
     *
     * @return
     */
    @PostMapping("del")
    @ResponseBody
    public ResponseDto del(String id) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode(ApiStatus.FAIL.getCode());
        responseDto.setMessage(ApiStatus.FAIL.getValue());
        int result = sysModuleService.del(id);
        responseDto.setCode(ApiStatus.SUCCESS.getCode());
        if (result > 0) {
            responseDto.setMessage("删除成功!");
        } else {
            responseDto.setMessage("删除失败!");
        }
        return responseDto;
    }
}
