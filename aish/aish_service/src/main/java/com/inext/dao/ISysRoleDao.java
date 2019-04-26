package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ISysRoleDao extends BaseDao<SysRole> {

    List<SysRole> getByParentId(Integer parentId);


    int saveSysRole(SysRole sysRole);

    List<SysRole> getRoleByParams(Map<String, String> params);


    List<Map<String, Object>> getRoleTreeList(Map<String, Object> params);

    /**
     * 根据角色id获取所有子角色id
     */
    String getRoleChildIds(String roleId);


    SysRole findSysRoleById(String id);
}
