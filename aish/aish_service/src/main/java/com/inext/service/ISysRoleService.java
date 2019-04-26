package com.inext.service;

import com.inext.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * Created by wenkk  on 2017/3/9 0009.
 */
public interface ISysRoleService {

    /**
     * 获取角色树
     *
     * @return
     */
    @Deprecated
    String getRoleTree();

    /**
     * 根据角色id获取所有子角色id
     */
    String getRoleChildIds(String roleId);

    /**
     * 前端树形结构展示角色
     */
    List<Map<String, Object>> getRoleTreeList(String roleIds);

    /**
     * 保存系统角色
     *
     * @param sysRole
     */
    void saveRole(SysRole sysRole);

    String getCheckboxModuleTree();

    String getCheckboxModuleTree(Map params);

    /**
     * 前端tableTree列表展示
     */
    List<SysRole> getRoleList();

    /**
     * 根据主键查询系统角色
     *
     * @param id
     */
    SysRole findSysRoleById(String id);

    int del(String id);
}
