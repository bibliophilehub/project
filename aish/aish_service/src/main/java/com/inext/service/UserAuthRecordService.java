package com.inext.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.inext.entity.BorrowUser;
import com.inext.entity.UserAuthRecord;
import com.inext.entity.UserMobileAuthLog;

public interface UserAuthRecordService {

    public void saveRecord(UserAuthRecord record);

    public void updateRecord(UserAuthRecord record);

    public List<UserAuthRecord> getRecordByUserId(Map<String, Object> params);


    /**
     * 新风控的运营商认证
     * @return
     */
    public Map<String, String> doMobileAuth(BorrowUser user);

    /**
     * 新风控的运营商认证结果查询
     * @return
     */
    void queryMobileAuth();

    /**
     * 获取运营商原始数据
     */
    void queryOperatorOriginalData();

    public void saveUserMobileAuthLog(UserMobileAuthLog log);

    public void updateUserMobileAuthLog(UserMobileAuthLog log);

    public List<UserMobileAuthLog> getUserMobileAuthLogByUserId(Map<String, Object> params);

}
