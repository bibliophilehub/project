package com.inext.service.impl;

import com.inext.constants.SysConstant;
import com.inext.dao.ISysModuleDao;
import com.inext.dao.ISysRoleModuleDao;
import com.inext.entity.*;
import com.inext.service.IPermissionInfoService;
import com.inext.service.ISysModuleService;
import com.inext.service.ISysRoleModuleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenkk on 2017/3/9 0009.
 */
@Service("sysModuleService")
public class SysModuleServiceImpl implements ISysModuleService, InitializingBean {

    @Resource
    ISysModuleDao sysModuleDao;

    @Resource
    ISysRoleModuleDao sysRoleModuleDao;
    @Resource
    IPermissionInfoService permissionInfoService;
    @Resource
    ISysRoleModuleService sysRoleModuleService;

    @Override
    public Integer saveModule(SysModule sysModule) {
        if (sysModule.getId() == null) {
            sysModule.setCreateTime(sysModule.getUpdateTime());
            sysModule.setCreateAccount(sysModule.getUpdateAccount());
            this.sysModuleDao.saveModule(sysModule);
            SysRoleModule sysRoleModule = new SysRoleModule();
            sysRoleModule.setRoleId(SysRole.SUPERROLE);
            sysRoleModule.setModuleId(sysModule.getId());
            sysRoleModule.setRemark("系统默认给超级管理员授权");
            this.sysRoleModuleDao.saveRoleModule(sysRoleModule);
            PermissionInfo permissionInfo = new PermissionInfo();
            permissionInfo.setModuleId(sysModule.getId());
            permissionInfo.setUserId(BackUser.SUPER);
            permissionInfo.setCreateTime(new Date());
            permissionInfo.setRemarks("系统默认给超级管理员授权");
            permissionInfoService.insert(permissionInfo);
        } else {
            this.sysModuleDao.updateByPrimaryKeySelective(sysModule);

        }
        return sysModule.getId();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SysModule> list = sysModuleDao.selectAll();
        Map map = new HashMap<>();
        for (SysModule sm : list) {
            if (sm.getUrl().equalsIgnoreCase("javascipr:void(0);") || sm.getUrl().equalsIgnoreCase("javascript:;")) {
                continue;
            }
            map.put(sm.getUrl(), sm.getId());
        }
        SysConstant.ModuleMap = map;
    }

    @Override
    public List<SysModule> getModuleList() {
        List<SysModule> sysModuleList = this.sysModuleDao.getByParentId(SysModule.SUPER);
        addChildren(sysModuleList);
        return sysModuleList;
    }

    private void addChildren(List<SysModule> sysModuleList) {
        for (SysModule sysModule : sysModuleList) {
            List<SysModule> children = this.sysModuleDao.getByParentId(sysModule.getId());
            if (children != null && children.size() != 0) {
                sysModule.setChildren(children);
                addChildren(children);
            }
        }
    }

    @Override
    public int del(String id) {
//		SysModule sysModule=new SysModule();
//		sysModule.setId(Integer.parseInt(id));
//		sysModule.setEnabled(0);
        permissionInfoService.deleteByModuleId(Integer.parseInt(id));
        sysRoleModuleService.removeRoleModuleByModuleId(Integer.parseInt(id));
//		return sysModuleDao.updateByPrimaryKeySelective(sysModule);
        String moduleIds = sysModuleDao.getModuleChildIds(id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("moduleIds", moduleIds.substring(1));
        return sysModuleDao.delModule(params);
    }

    @Override
    public SysModule findSysModuleById(String id) {
        // TODO Auto-generated method stub
        return sysModuleDao.findSysModuleById(id);
    }

    @Override
    public List<Map<String, Object>> getModuleTreeList() {
        // TODO Auto-generated method stub
        return sysModuleDao.getModuleTreeList();
    }
}
