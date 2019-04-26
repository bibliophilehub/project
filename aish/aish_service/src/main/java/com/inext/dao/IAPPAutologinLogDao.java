package com.inext.dao;

import com.inext.configuration.BaseDao;
import com.inext.entity.AppAutoLoginLog;

import java.util.Map;

public interface IAPPAutologinLogDao extends BaseDao<AppAutoLoginLog> {
    /***
     * 查询
     * @param params
     * @return
     */
    public AppAutoLoginLog selectByParams(Map<String, String> params);

    public AppAutoLoginLog selectByUserId(Integer userId);

}
