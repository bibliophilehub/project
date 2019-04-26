package com.inext.dao;

import com.inext.entity.SysUserRole;


public interface ISysUserRoleDao {


    int saveSysUserRole(SysUserRole sysUserRole);

    int removeSysUserRoleByUserId(Integer userId);

    String getRoleIdsByUserId(Integer userId);

    String getRoleNamesByUserId(Integer userId);

    int removeSysUserRoleByRoleId(Integer roleId);

    String getUserIdsByRoleId(Integer roleId);
}
