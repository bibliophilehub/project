package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.SysRoleModule;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by wenkk on 2017/3/9 0009.
 */
@Repository("sysRoleModuleDao")
public interface ISysRoleModuleDao extends BaseDao<SysRoleModule> {


    void saveRoleModule(SysRoleModule sysRoleModule);

    Integer removeRoleModuleByRoleId(Integer roleId);

    String getModuleIdsByRoleId(Integer roleId);

    Integer removeRoleModuleByModuleId(Integer moduleId);

    String getDelModuleIdsByRoleId(Map<String, Object> params);

    int removeRoleModuleIdsAndRoleId(Map<String, Object> params);
}
