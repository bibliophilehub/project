package com.inext.controller;

import com.github.pagehelper.PageInfo;
import com.inext.constants.Constants;
import com.inext.entity.BackUser;
import com.inext.entity.BackUserLoginRecord;
import com.inext.entity.ChannelInfo;
import com.inext.entity.SysRole;
import com.inext.enumerate.AppStatus;
import com.inext.result.ResponseDto;
import com.inext.service.*;
import com.inext.utils.JSONUtil;
import com.inext.utils.WriteUtil;
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
 * 系统用户管理
 */
@Controller
@RequestMapping(value = "/sysUserManagement")
public class SysUserManagementController extends BaseController {

    private static Logger logger = Logger.getLogger(SysUserManagementController.class);

    private static String BACK_URL = "/";

    @Resource
    ISysUserRoleService sysUserRoleService;
    @Resource
    IPermissionInfoService iPermissionInfoService;
    @Resource
    IBackUserLoginRecordService backUserLoginRecordService;

    @Resource
    IBackUserService backUserService;
    @Resource
    ISysRoleService sysRoleService;
    @Resource
    IChannelService channelService;

    @RequestMapping(value = "/searchBackUser")
    public String searchBackUser(HttpServletRequest request, HttpServletResponse response, Model model,
                                 BackUser backUser) {
        logger.info("UserManageController search start");
        try {
            Map<String, Object> params = this.getParametersO(request);
            BackUser user = loginAdminUser(request);
            PageInfo<BackUser> backUserList = this.backUserService.getPageList(params);
            List<Map<String, Object>> roleList = null;
            if (user.getAccount().equalsIgnoreCase("superUser")) {
                roleList = sysRoleService.getRoleTreeList(SysRole.SUPER + "");//所有角色
            } else {
                String userRoles = sysUserRoleService.getRoleIdsByUserId(user.getId());
                roleList = sysRoleService.getRoleTreeList(userRoles);// 操作人员拥有角色
            }
            model.addAttribute("roleList", roleList);
            model.addAttribute("backUser", backUser);
            model.addAttribute("pageInfo", backUserList);
            model.addAttribute("sortColumn",params.get("sortColumn"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "v2/systemManagement/systemUserList";
    }

    @RequestMapping(value = "/getPageList")
    public void getPageList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = this.getParametersO(request);
        PageInfo<BackUser> backUserList = this.backUserService.getPageList(params);
        WriteUtil.writeDataToApp(response, JSONUtil.beanToJson(backUserList));
    }

    /**
     * 去添加系统用户页面
     *
     * @return
     */
    @GetMapping("to-save")
    public String toSaveBackUser(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        BackUser user = loginAdminUser(request);
        BackUser backUser = backUserService.findBackUserById(id);
        if (backUser == null) {
            backUser = new BackUser();
            backUser.setPortraitImg("http://souyijie.oss-cn-shanghai.aliyuncs.com/IMG/2017-08-31-1021483097641.png");
        } else {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", backUser.getId() + "");
            model.addAttribute("userLoginRecordList", backUserLoginRecordService.getPageList(params).getList());
            String userRoleId = sysUserRoleService.getRoleIdsByUserId(backUser.getId());
            backUser.setUserRoles(userRoleId);// 设置该用户的角色
            String userModuleId = iPermissionInfoService.getModuleIdsByUserId(backUser.getId());
            backUser.setUserModules(userModuleId);// 设置该用户的权限
        }
        List<Map<String, Object>> roleList = null;
        if (user.getAccount().equalsIgnoreCase("superUser")) {
            roleList = sysRoleService.getRoleTreeList(SysRole.SUPER + "");//所有角色
        } else {
            String userRoles = sysUserRoleService.getRoleIdsByUserId(user.getId());
            roleList = sysRoleService.getRoleTreeList(userRoles);// 操作人员拥有角色
        }
        model.addAttribute("roleList", roleList);
        model.addAttribute("backUser", backUser);

        //导流渠道列表
        List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
        model.addAttribute("channelList", channelList);

        return "v2/systemManagement/systemUserSave";
    }

    /**
     * 去预览系统用户页面
     *
     * @return
     */
    @GetMapping("to-Preview")
    public String toPreview(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        BackUser backUser = backUserService.findBackUserById(id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", backUser.getId() + "");
        PageInfo<BackUserLoginRecord> pageInfo = backUserLoginRecordService.getPageList(params);
        model.addAttribute("userLoginRecordList", pageInfo.getList());
        String userRoleNames = sysUserRoleService.getRoleNamesByUserId(backUser.getId());
        backUser.setUserRoleNames(userRoleNames);
        String userRoleId = sysUserRoleService.getRoleIdsByUserId(backUser.getId());
        backUser.setUserRoles(userRoleId);// 设置该用户的角色
        String userModuleId = iPermissionInfoService.getModuleIdsByUserId(backUser.getId());
        backUser.setUserModules(userModuleId);// 设置该用户的权限
        model.addAttribute("backUser", backUser);

        //导流渠道列表
        List<ChannelInfo> channelList = channelService.getList(new HashMap<String, Object>());
        model.addAttribute("channelList", channelList);
        return "v2/systemManagement/systemUserPreview";
    }

    /**
     * 保存系统用户
     *
     * @param request
     * @param backUser
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public Map<String, Object> doSave(HttpServletRequest request, BackUser backUser) {
        BackUser user = loginAdminUser(request);
        backUser.setUpdateTime(new Date());
        backUser.setUpdateAccount(user.getAccount());
        Map<String, Object> resultMap = backUserService.saveBackUser(backUser);
        return resultMap;
    }

    /**
     * 修改个人信息
     *
     * @param request
     * @param backUser
     * @return
     */
    @RequestMapping("updateUserInfo")
    @ResponseBody
    public Map<String, Object> updateUserInfo(HttpServletRequest request, BackUser backUser) {
        BackUser user = loginAdminUser(request);
        backUser.setUpdateTime(new Date());
        backUser.setUpdateAccount(user.getAccount());
        Map<String, Object> resultMap = backUserService.updateUserInfo(backUser);
        BackUser sessionBackUser = (BackUser) request.getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        if (sessionBackUser != null) {
            sessionBackUser.setNickName(backUser.getNickName());
            sessionBackUser.setPortraitImg(backUser.getPortraitImg());
            request.getSession(true).setAttribute(Constants.JIEJIEKAN_BACK_USER, sessionBackUser);
        }
        return resultMap;
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
        int result = backUserService.del(id);
        responseDto.setCode(AppStatus.SUCCESS.getCode());
        if (result > 0) {
            responseDto.setMessage("删除成功!");
        } else {
            responseDto.setMessage("删除失败!");
        }
        return responseDto;
    }
}
