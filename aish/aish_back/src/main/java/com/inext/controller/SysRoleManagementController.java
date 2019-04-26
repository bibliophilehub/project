package com.inext.controller;

import com.inext.entity.BackUser;
import com.inext.entity.SysRole;
import com.inext.enumerate.AppStatus;
import com.inext.result.ResponseDto;
import com.inext.service.IBackUserService;
import com.inext.service.ISysRoleModuleService;
import com.inext.service.ISysRoleService;
import com.inext.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
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
 * 系统角色管理
 */
@Controller
@RequestMapping(value = "/sysRoleManagement")
public class SysRoleManagementController extends BaseController {

    private static Logger loger = Logger.getLogger(SysRoleManagementController.class);

    private static String BACK_URL = "/";

    @Resource
    ISysRoleService sysRoleService;
    @Resource
    ISysRoleModuleService sysRoleModuleService;
    @Resource
    IBackUserService backUserService;

    @RequestMapping("/roleList")
    public String moduleList(HttpServletRequest request) {
        return "v2/sysRole/sysRoleList";
    }

    @RequestMapping("/getRoleData")
    @ResponseBody
    public Map<String, Object> getRoleData(HttpServletRequest request, HttpServletResponse response) {
        List<SysRole> sysRoleList = sysRoleService.getRoleList();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", sysRoleList);
        return resultMap;
    }

    @RequestMapping("/to-save")
    public String toSave(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        BackUser user = loginAdminUser(request);
        SysRole sysRole = sysRoleService.findSysRoleById(id);
        if (sysRole == null) {
            sysRole = new SysRole();
            //sysRole.setCreateTime(new Date());
            //sysRole.setCreateAccount(user.getAccount());
            //sysRole.setUpdateTime(sysRole.getCreateTime());
            //sysRole.setUpdateAccount(sysRole.getCreateAccount());
        } else {
            String roleModules = sysRoleModuleService.getModuleIdsByRoleId(sysRole.getId());
            sysRole.setRoleModules(roleModules);
            List<BackUser> backUserList = backUserService.getListByRoleId(sysRole.getId());
            model.addAttribute("backUserList", backUserList);
        }
        List<Map<String, Object>> roleList = sysRoleService.getRoleTreeList(SysRole.SUPER + "");//所有角色
        model.addAttribute("roleList", roleList);
        model.addAttribute("sysRole", sysRole);
        return "v2/sysRole/sysRoleSave";
    }

    /**
     * 去预览系统用户页面
     *
     * @return
     */
    @GetMapping("to-Preview")
    public String toPreview(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        SysRole sysRole = sysRoleService.findSysRoleById(id);
        String roleModules = sysRoleModuleService.getModuleIdsByRoleId(sysRole.getId());
        sysRole.setRoleModules(roleModules);
        List<BackUser> backUserList = backUserService.getListByRoleId(sysRole.getId());
        model.addAttribute("backUserList", backUserList);
        List<Map<String, Object>> roleList = sysRoleService.getRoleTreeList(SysRole.SUPER + "");//所有角色
        model.addAttribute("roleList", roleList);
        model.addAttribute("sysRole", sysRole);
        return "v2/sysRole/sysRolePreview";
    }

    @RequestMapping("/save")
    public void saveRole(HttpServletRequest request, HttpServletResponse response, SysRole sysRole) {
        BackUser user = loginAdminUser(request);
        sysRole.setUpdateAccount(user.getAccount());
        sysRole.setUpdateTime(new Date());
        if (sysRole.getParentId() == null) {
            sysRole.setParentId(SysRole.SUPER);
        }
        this.sysRoleService.saveRole(sysRole);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "保存成功");
        jsonObject.put("id", sysRole.getId());
        JsonUtils.toObjectJson(response, jsonObject);
    }

    @RequestMapping("/getModule")
    public void getModule(HttpServletRequest request, HttpServletResponse response, String roleId) {
        Map map = null;
        if (StringUtils.isNotBlank(roleId) && !roleId.equals(SysRole.SUPER + "")) {
            map = new HashMap();
            map.put("roleId", roleId);
        }
        String html = this.sysRoleService.getCheckboxModuleTree(map);
        request.setAttribute("checkboxModuleTree", html);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", html);
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
        responseDto.setCode(AppStatus.FAIL.getCode());
        responseDto.setMessage(AppStatus.FAIL.getDesc());
        int result = sysRoleService.del(id);
        responseDto.setCode(AppStatus.SUCCESS.getCode());
        if (result > 0) {
            responseDto.setMessage("删除成功!");
        } else {
            responseDto.setMessage("删除失败!");
        }
        return responseDto;
    }
}
