package com.inext.service;

import com.inext.entity.SysUserRole;

/**
 * Created by wenkk on 2017/3/9 0009.
 */
public interface ISysUserRoleService {
    /**
     * 删除角色关系
     *
     * @param userId
     */
    void removeSysUserRoleByUserId(Integer userId);

    /**
     * 删除角色关系
     *
     * @param roleId
     */
    void removeSysUserRoleByRoleId(Integer roleId);

    /**
     * 保存角色关系至数据库
     *
     * @param sysUserRole
     */
    void saveUserRole(SysUserRole sysUserRole);

    /**
     * 通过用户id获取角色编号
     */
    String getRoleIdsByUserId(Integer userId);

    /**
     * 通过用户id获取角色名称
     */
    String getRoleNamesByUserId(Integer userId);

    /**
     * 通过角色id获取用户编号
     */
    String getUserIdsByRoleId(Integer roleId);
}
