package com.inext.service;

import com.github.pagehelper.PageInfo;
import com.inext.entity.BackUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IBackUserService {

    public BackUser findBackUserById(String id);

    Map<String, Object> saveBackUser(BackUser backUser);

    Map<String, Object> updateUserInfo(BackUser backUser);

    PageInfo<BackUser> getPageList(Map<String, Object> params);

    /**
     * 通过角色查询用户列表（角色修改页面）
     */
    public List<BackUser> getListByRoleId(Integer roleId);

    public int del(String id);

    public void addBackUserBlacklist(String userId);

    public void delBackUserBlacklist(String userId);

    /**
     * 后台用户登录验证（根据帐号，密码 ）
     *
     * @param params
     * @return
     */
    public Map<String, Object> checkLogin(HashMap<String, Object> params);
}
