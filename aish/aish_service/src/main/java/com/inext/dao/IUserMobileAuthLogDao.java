package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.UserMobileAuthLog;

import java.util.List;
import java.util.Map;

public interface IUserMobileAuthLogDao extends BaseDao<UserMobileAuthLog> {


    void saveMobileAuth(UserMobileAuthLog log);

    void updateMobileAuth(UserMobileAuthLog log);

    List<UserMobileAuthLog> getMobileAuthLog(Map<String, Object> params);

    List<UserMobileAuthLog> getUnAuthUserList();

}
