package com.inext.service;

import com.inext.entity.SysModule;

import java.util.List;
import java.util.Map;

/**
 * Created by wenkk on 2017/3/9 0009.
 */
public interface ISysModuleService {

    /**
     * 获取模块列表
     *
     * @return
     */
    List<SysModule> getModuleList();


    /**
     * 保存系统模块并添加至超级管理员角色中
     *
     * @param sysModule
     */
    Integer saveModule(SysModule sysModule);

    int del(String id);

    /**
     * 根据主键查询系统模块
     *
     * @param id
     */
    SysModule findSysModuleById(String id);

    /**
     * 前端树形结构展示模块
     */
    List<Map<String, Object>> getModuleTreeList();


}
