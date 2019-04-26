package com.inext.service;

import com.inext.entity.AppAutoLoginLog;
import com.inext.entity.BorrowUser;
import com.inext.result.ApiServiceResult;

import java.util.Map;

public interface IAppAutoLoginLogService {
    /***
     * 查询
     * @param params
     * @return
     */
    public AppAutoLoginLog selectByParams(Map<String, String> params);

    /****
     * 新增
     * @param log
     */
    public void saveAPPAutologinLog(AppAutoLoginLog log);

    /***
     * 修改
     */
    public void updateAPPAutologinLog(AppAutoLoginLog log);

    public AppAutoLoginLog selectByUserId(Integer userId);

    public ApiServiceResult generateToken(BorrowUser user);
}
