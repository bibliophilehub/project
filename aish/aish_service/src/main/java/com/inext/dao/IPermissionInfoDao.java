package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.PermissionInfo;
import com.inext.entity.SysModule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 开发人员：jzhang
 * 创建时间：2017-04-28 下午 13:44
 */
@Repository
public interface IPermissionInfoDao extends BaseDao<PermissionInfo> {
    List<PermissionInfo> getInfoByUserId(int userId);

    String getModuleIdsByUserId(Integer userId);

    List<SysModule> getModuleByUserId(Map<String, Object> params);

    int deleteByModuleIdsAndUserIds(Map<String, Object> params);
}
