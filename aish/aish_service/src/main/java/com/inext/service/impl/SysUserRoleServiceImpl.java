package com.inext.service.impl;

import com.inext.dao.ISysUserRoleDao;
import com.inext.entity.SysUserRole;
import com.inext.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wenkk on 2017/3/13 0013.
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements ISysUserRoleService {

    @Resource
    ISysUserRoleDao sysUserRoleDao;

    @Override
    public void saveUserRole(SysUserRole sysUserRole) {
        this.sysUserRoleDao.saveSysUserRole(sysUserRole);
    }

    @Override
    public String getRoleIdsByUserId(Integer userId) {
        return sysUserRoleDao.getRoleIdsByUserId(userId);
    }

    @Override
    public void removeSysUserRoleByRoleId(Integer roleId) {
        this.sysUserRoleDao.removeSysUserRoleByRoleId(roleId);
    }

    @Override
    public void removeSysUserRoleByUserId(Integer userId) {
        this.sysUserRoleDao.removeSysUserRoleByUserId(userId);
    }

    @Override
    public String getRoleNamesByUserId(Integer userId) {
        return sysUserRoleDao.getRoleNamesByUserId(userId);
    }

    @Override
    public String getUserIdsByRoleId(Integer roleId) {
        // TODO Auto-generated method stub
        return sysUserRoleDao.getUserIdsByRoleId(roleId);
    }
}
