package com.inext.service.impl;

import com.inext.dao.ISysModuleDao;
import com.inext.dao.ISysRoleModuleDao;
import com.inext.entity.SysRoleModule;
import com.inext.service.ISysRoleModuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenkk on 2017/3/9 0009.
 */
@Service("sysRoleModuleService")
public class SysRoleModuleServiceImpl implements ISysRoleModuleService {

    @Resource
    ISysRoleModuleDao sysRoleModuleDao;

    @Resource
    ISysModuleDao sysModuleDao;

    @Override
    public void saveRoleModule(String[] moduleIdArray, Integer roleId) {
        this.sysRoleModuleDao.removeRoleModuleByRoleId(roleId);
        for (int i = 0; i < moduleIdArray.length; i++) {
            SysRoleModule sysRoleModule = new SysRoleModule();
            sysRoleModule.setRoleId(roleId);
            sysRoleModule.setModuleId(Integer.parseInt(moduleIdArray[i]));
            this.sysRoleModuleDao.saveRoleModule(sysRoleModule);
        }

    }

    @Override
    public String getModuleIdsByRoleId(Integer roleId) {
        // TODO Auto-generated method stub
        return sysRoleModuleDao.getModuleIdsByRoleId(roleId);
    }

    @Override
    public Integer removeRoleModuleByRoleId(Integer roleId) {
        // TODO Auto-generated method stub
        return sysRoleModuleDao.removeRoleModuleByRoleId(roleId);
    }

    @Override
    public Integer removeRoleModuleByModuleId(Integer moduleId) {
        // TODO Auto-generated method stub
        return sysRoleModuleDao.removeRoleModuleByModuleId(moduleId);
    }

    @Override
    public String getDelModuleIdsByRoleId(Integer roleId, String moduleIds) {
        // TODO Auto-generated method stub
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleId", roleId);
        params.put("moduleIds", moduleIds);
        return sysRoleModuleDao.getDelModuleIdsByRoleId(params);
    }

    @Override
    public int removeRoleModuleIdsAndRoleId(String roleIds, String moduleIds) {
        // TODO Auto-generated method stub
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleIds", roleIds);
        params.put("moduleIds", moduleIds);
        return sysRoleModuleDao.removeRoleModuleIdsAndRoleId(params);
    }
}
