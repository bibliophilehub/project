package com.inext.service;

import com.inext.entity.PermissionInfo;

import java.util.List;

/**
 * 开发人员：jzhang
 * 创建时间：2017-04-28 下午 13:45
 */
public interface IPermissionInfoService {
    /****
     * 保存
     * @param black
     */
    public int insert(PermissionInfo black);

    public List<PermissionInfo> getInfoByUserId(int userId);

    public int deleteByUserId(Integer userId);

    public int deleteByModuleId(Integer moduleId);

    public String getModuleIdsByUserId(Integer userId);

    /**
     * 获取首页用户权限树
     *
     * @param backUserId
     * @return
     */
    public String getUserModule(Integer backUserId);

    /**
     * 获取首页用户权限树
     *
     * @param backUserId
     * @return
     */
    public int deleteByModuleIdsAndUserIds(String delUserIds, String delModuleIds);
}
