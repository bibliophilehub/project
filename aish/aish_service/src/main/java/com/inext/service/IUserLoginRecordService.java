package com.inext.service;

import com.inext.exception.DataSaveFailException;
import com.inext.result.ServiceResult;

/**
 * Created by 李思鸽 on 2017/5/17 0017.
 */
public interface IUserLoginRecordService {

    /**
     * 保存用户登录记录
     *
     * @param userId  用户ID
     * @param loginIp 登录IP
     * @return
     */
    ServiceResult saveUserLoginRecord(Integer userId, String loginIp) throws DataSaveFailException;
}
