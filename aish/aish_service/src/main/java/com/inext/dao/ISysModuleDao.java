package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.SysModule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("sysModuleDao")
public interface ISysModuleDao extends BaseDao<SysModule> {

    List<SysModule> getByParentId(Integer parentId);


    List<SysModule> getModuleByParams(Map<String, String> params);


    Integer saveModule(SysModule sysModule);

    Integer delModule(Map<String, Object> params);


    SysModule findSysModuleById(String id);


    List<Map<String, Object>> getModuleTreeList();

    /**
     * 根据模块id获取所有子模块id
     */
    String getModuleChildIds(String moduleId);
}
