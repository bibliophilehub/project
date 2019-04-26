package com.inext.service.impl;

import com.inext.constants.Constants;
import com.inext.dao.ISysModuleDao;
import com.inext.dao.ISysRoleDao;
import com.inext.entity.BackUser;
import com.inext.entity.SysModule;
import com.inext.entity.SysRole;
import com.inext.service.IPermissionInfoService;
import com.inext.service.ISysRoleModuleService;
import com.inext.service.ISysRoleService;
import com.inext.service.ISysUserRoleService;
import com.inext.utils.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenkk on 2017/3/10 0010.
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    ISysRoleDao iSysRoleDao;

    @Resource
    ISysModuleDao sysModuleDao;
    @Resource
    ISysRoleModuleService sysRoleModuleService;
    @Resource
    ISysUserRoleService sysUserRoleService;
    @Resource
    private IPermissionInfoService iPermissionInfoService;

    @Override
    public String getRoleTree() {
        List<SysRole> sysRoleList = this.iSysRoleDao.getByParentId(SysRole.SUPER);
        String html = appendChildren(sysRoleList).toString();
        return html;
    }

    private StringBuffer appendChildren(List<SysRole> sysRoleList) {
        StringBuffer html = new StringBuffer();
        html.append("<ul>");
        for (SysRole sysRole : sysRoleList) {
            html.append("<li>");
            html.append("<span><i class=\"icon-minus-sign\"></i> " + sysRole.getName() + "</span> ");
            html.append("&nbsp;&nbsp;<button value=\"/sysRole/addRoleForward?parentId=" + sysRole.getId() + "\" onclick=\"location.href=this.value\">添加下级角色</button>");
            html.append("&nbsp;&nbsp;<button value=\"/sysRole/roleModuleManage?roleId=" + sysRole.getId() + "\" onclick=\"location.href=this.value\">角色授权</button>");

            List<SysRole> children = this.iSysRoleDao.getByParentId(sysRole.getId());
            if (children != null && children.size() != 0) {
                html.append(this.appendChildren(children));
            } else {
            }
            html.append("</li>");
        }
        html.append("</ul>");
        return html;
    }

    @Override
    public void saveRole(SysRole sysRole) {
        if (sysRole.getId() == null) {
            sysRole.setCreateTime(sysRole.getUpdateTime());
            sysRole.setCreateAccount(sysRole.getUpdateAccount());
            this.iSysRoleDao.saveSysRole(sysRole);
//    		SysUserRole sysUserRole=new SysUserRole();
//    		sysUserRole.setRoleId(sysRole.getId());
//    		sysUserRole.setUserId(BackUser.SUPER);
//    		this.sysUserRoleService.saveUserRole(sysUserRole);
        } else {
            this.iSysRoleDao.updateByPrimaryKeySelective(sysRole);
            //获取该角色被删掉的权限
            String delModuleIds = sysRoleModuleService.getDelModuleIdsByRoleId(sysRole.getId(), sysRole.getRoleModules());
            //同步到用户权限关系表删除。（一个用户有两个角色有相同的权限 会被删掉,解决方法尽量一个用户一个角色）
            if (StringUtils.isNotBlank(delModuleIds)) {
                String delUserIds = sysUserRoleService.getUserIdsByRoleId(sysRole.getId());
                if (StringUtils.isNotBlank(delUserIds)) {
                    iPermissionInfoService.deleteByModuleIdsAndUserIds(delUserIds, delModuleIds);
                }
                //同步到下级角色删除该权限
                String roleChildIds = this.getRoleChildIds(sysRole.getId() + "");
                if (StringUtils.isNotBlank(roleChildIds)) {
                    if (roleChildIds.indexOf(",") == 0) {
                        roleChildIds = roleChildIds.substring(1);
                    }
                    sysRoleModuleService.removeRoleModuleIdsAndRoleId(roleChildIds, delModuleIds);
                }
            }
        }
        if (StringUtils.isNotBlank(sysRole.getRoleModules())) {
            sysRoleModuleService.saveRoleModule(sysRole.getRoleModules().split(","), sysRole.getId());
        }
    }

    @Override
    public String getCheckboxModuleTree() {
        return getCheckboxModuleTree(null);
    }

    @Override
    public String getCheckboxModuleTree(Map params) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("status", SysModule.DISPLAT_STATUS_SHOW + "");
        params.put("enabled", SysModule.ENABLE_STATUS_USABLE + "");
        params.put("parentId", SysModule.SUPER + "");
        List<SysModule> sysModuleList = this.sysModuleDao.getModuleByParams(params);
        String html = appendCheckboxModuleTree(sysModuleList, params).toString();
        return html;

    }

    private StringBuffer appendCheckboxModuleTree(List<SysModule> sysModuleList, Map params) {
        StringBuffer html = new StringBuffer();
        html.append("<ul>");
        for (SysModule sysModule : sysModuleList) {
            html.append("<li>");
            html.append("<span><input type=\"checkbox\" pid=\"" + sysModule.getParentId() + "\" menus=\"" + sysModule.getMenus() + "\"  class=\"chp_" + sysModule.getId() + " chn_" + sysModule.getParentId() + "\" style=\"margin-top:-1px\" value='" + sysModule.getId() + "'/> <c>" + sysModule.getName() + "</c></span> ");
            params.put("parentId", sysModule.getId() + "");
            List<SysModule> children = this.sysModuleDao.getModuleByParams(params);
            if (children != null && children.size() != 0) {
                html.append(this.appendCheckboxModuleTree(children, params));
            } else {
            }
            html.append("</li>");
        }
        html.append("</ul>");
        return html;
    }


    @Override
    public List<Map<String, Object>> getRoleTreeList(String roleIds) {
        String newRoleIds = "";
        String[] roleIdList = roleIds.split(",");
        for (String roleId : roleIdList) {
            newRoleIds += iSysRoleDao.getRoleChildIds(roleId);
        }
        if (newRoleIds.indexOf(",") == 0) {
            newRoleIds = newRoleIds.substring(1);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleIds", "(" + newRoleIds + ")");
        return iSysRoleDao.getRoleTreeList(params);
    }

    @Override
    public String getRoleChildIds(String roleId) {
        return iSysRoleDao.getRoleChildIds(roleId);
    }

    @Override
    public List<SysRole> getRoleList() {
        List<SysRole> sysRoleList = this.iSysRoleDao.getByParentId(SysRole.SUPER);
        addChildren(sysRoleList);
        return sysRoleList;
    }

    private void addChildren(List<SysRole> sysRoleList) {
        for (SysRole sysRole : sysRoleList) {
            List<SysRole> children = this.iSysRoleDao.getByParentId(sysRole.getId());
            if (children != null && children.size() != 0) {
                sysRole.setChildren(children);
                addChildren(children);
            }
        }
    }

    @Override
    public SysRole findSysRoleById(String id) {
        // TODO Auto-generated method stub
        return iSysRoleDao.findSysRoleById(id);
    }

    @Override
    public int del(String id) {
        BackUser operbackUser = (BackUser) RequestUtils.getRequest().getSession().getAttribute(Constants.JIEJIEKAN_BACK_USER);
        SysRole sysRole = new SysRole();
        sysRole.setId(Integer.parseInt(id));
        sysRole.setEnabled(0);
        sysRole.setUpdateAccount(operbackUser.getAccount());
        sysRole.setUpdateTime(new Date());
//		sysUserRoleService.removeSysUserRoleByRoleId(sysRole.getId());
//		sysRoleModuleService.removeRoleModuleByRoleId(sysRole.getId());
        return iSysRoleDao.updateByPrimaryKeySelective(sysRole);
    }
}
