package com.inext.service;

/**
 * Created by wenkk on 2017/3/9 0009.
 */
public interface ISysRoleModuleService {

    void saveRoleModule(String[] moduleIdArray, Integer roleId);

    /**
     * 获取角色对应的权限id
     *
     * @param roleId
     * @return
     */
    public String getModuleIdsByRoleId(Integer roleId);

    Integer removeRoleModuleByRoleId(Integer roleId);

    Integer removeRoleModuleByModuleId(Integer moduleId);

    /**
     * 获取该角色id 被删掉的权限Id
     *
     * @return
     */
    public String getDelModuleIdsByRoleId(Integer roleId, String moduleIds);

    /**
     * 删除角色对应的权限
     *
     * @return
     */
    public int removeRoleModuleIdsAndRoleId(String roleIds, String moduleIds);
}
